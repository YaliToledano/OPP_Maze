package gameClient;


import java.awt.*;
import java.util.*;

import dataStructure.*;
import dataStructure.Robot;
import gui.Graph_GUI;
import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import utils.Point3D;
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
        //init game service and graph
        game_service game = Game_Server.getServer(scenario_num);
        String graph = game.getGraph();
        DGraph graph1 = new DGraph();
        graph1.init(graph);
        System.out.println(game.toString());

        //Graph thread
        StdDraw.setCanvasSize(600, 600);
        Graph_GUI.setLastGraph(graph1);
        Thread gui = new Thread(new Graph_GUI());
        gui.start();
        try {
            gui.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //fruits placement
        Iterator<String> fruits = game.getFruits().iterator();
        ArrayList<Fruit> fruits1 = new ArrayList<>();
        while (fruits.hasNext()) {
          //  fruits1.add(new Fruit(fruits.next()));
        }
        for (int i = 0; i < fruits1.size(); i++) {
            StdDraw.picture(fruits1.get(i).getLocation().x(), fruits1.get(i).getLocation().y(), "banana.jpg", 0.0005, 0.0005);
        }

        System.out.println("init fruit");

        int numRobots = 0;
        try {
            numRobots = new JSONObject(game.toString()).getJSONObject("GameServer").getInt("robots");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(numRobots);
        ArrayList<_robot> robots = new ArrayList<>();
        //robots init and placement
        int inited = 0;
        int count = 0;
        Point3D p = StdDraw.getLastLoc();
        for (int i = 0; i < numRobots; i++) {
            while (StdDraw.getLastLoc() == null || close(p, StdDraw.getLastLoc())) {
                System.out.println("in");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            p = StdDraw.getLastLoc();
            System.out.println("robot in " + p.toString());
            StdDraw.setPenRadius(0.05);
            StdDraw.setPenColor(Color.red);
            StdDraw.point(p.x(), p.y());
            robots.add(new Robot());

        }
        System.out.println("init robot");

        //moves and game
        while (StdDraw.getLastLoc() == null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int next = nodeFromLoc(StdDraw.getLastLoc(), graph1);
        int temp;
        while (game.isRunning()) {
            temp = nodeFromLoc(StdDraw.getLastLoc(), graph1);
            if (next != temp) {
                next = temp;
                nextNode = next;
                move(game, robots);
            }
        }
    }

    //finds a node in the graph that matches given location
    private static int nodeFromLoc(Point3D p, graph g) {
        ArrayList<node_data> l = (ArrayList<node_data>) g.getV();
        for (node_data n : l) {
            if (close(n.getLocation(), p)) {
                return n.getKey();
            }
        }
        return -1;
    }

    //finds if to points are close enough to be the same one
    private static boolean close(Point3D p1, Point3D p2) {
        if (p1 == null || p2 == null) return false;
        double EPSILONX = 0.00001;
        double EPSILONY = 0.00001;
        if (Math.abs(p1.x() - p2.x()) > EPSILONX) return false;
        if (Math.abs(p1.y() - p2.y()) > EPSILONY) return false;
        return true;
    }
    //moves the closest robot to desired node
    private static void move(game_service game, ArrayList<_robot> robots) {
        int rToMove = findClosest(robots, nextNode);
        game.chooseNextEdge(rToMove, nextNode);
        System.out.println("moved: " + rToMove + " to node" + nextNode);
    }

    /**
     * @param robots
     * @param nextNode
     * @return the id of the closest Robot to nextNode
     */
    private static int findClosest(ArrayList<_robot> robots, int nextNode) {
        return 0;
    }

    @Override
    public void run() {
        while (StdDraw.getMode().equals("")
                || StdDraw.getMap().equals("")) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        ManualMode(Integer.parseInt(StdDraw.getMap().substring(1)));
    }
}
