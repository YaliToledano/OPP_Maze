package gameClient;


import java.util.*;

import dataStructure.DGraph;
import dataStructure.Fruit;
import dataStructure.Node;
import dataStructure.Robot;
import gui.Graph_GUI;
import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import utils.StdDraw;

public class MyGameGUI implements Runnable {
    private static int nextNode;//will be changed by GUI input

    public static void setNextNode(int node) {
        nextNode = node;
    }

    public static void main(String[] args) {
        Thread t = new Thread(new MyGameGUI());
        t.start();
    }

    //all process pf manual handling of robots in the scenario
    private static void ManualMode(int scenario_num) {
        game_service game = Game_Server.getServer(scenario_num);
        String graph = game.getGraph();
        DGraph graph1 = new DGraph();
        graph1.init(graph);
        Graph_GUI.setLastGraph(graph1);
        Thread gui = new Thread(new Graph_GUI());
        gui.start();
        Iterator<String> fruits = game.getFruits().iterator();
        ArrayList<Fruit> fruits1 = new ArrayList<>();
        try {
            int numRobots = new JSONObject(game.toString()).getInt("robots");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList<Robot> robots = new ArrayList<>();
        while (fruits.hasNext()) {
            fruits1.add(new Fruit(fruits.next()));
        }
        for (Fruit f : fruits1) {
            graph1.addNode(new Node(new Random().nextInt(), f.getLocation(), 0));
        }
        int next = nextNode;
        while (game.isRunning()) {
            if (next != nextNode) {
                move(game, robots);
                next = nextNode;
            }
        }
    }

    //moves the closest robot to desired node
    private static void move(game_service game, ArrayList<Robot> robots) {
        game.chooseNextEdge(findClosest(robots, nextNode), nextNode);
    }

    /**
     * @param robots
     * @param nextNode
     * @return the id of the closest Robot to nextNode
     */
    private static int findClosest(ArrayList<Robot> robots, int nextNode) {
        return 0;
    }

    @Override
    public void run() {
        ManualMode(0);
    }
}
