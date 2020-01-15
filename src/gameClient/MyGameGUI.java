package gameClient;


import java.awt.*;
import java.util.*;
import java.util.List;

import algorithms.Game_Algo;
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
        Arena arena = new Arena();
        game_service game = Game_Server.getServer(scenario_num);
        String graph = game.getGraph();
        DGraph graph1 = new DGraph();
        graph1.init(graph);
        arena.setGraph(graph1);
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
        arena.addFruits(game.getFruits());
        drawFruits(arena);

        System.out.println("init fruit");

        int numRobots = 0;
        try {
            numRobots = new JSONObject(game.toString()).getJSONObject("GameServer").getInt("robots");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //robots init and placement
        Point3D p = StdDraw.getLastLoc();
        for (int i = 0; i < numRobots; i++) {
            while (StdDraw.getLastLoc() == null || close(p, StdDraw.getLastLoc())) {

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
            int src = nodeFromLoc(p, arena.getGraph());
            game.addRobot(src);
        }
        System.out.println("init robot");

        StdDraw.enableDoubleBuffering();

        //moves and game loop
        game.startGame();
        System.out.println("game started" + game.timeToEnd());
        arena.addRobots(game.getRobots());
        System.out.println(p.toString());
        Game_Algo game_algo = new Game_Algo(arena);
        while (game.isRunning()) {
            System.out.println(game.timeToEnd());
            if (StdDraw.getLastLoc() != null || !close(p, StdDraw.getLastLoc()) || nodeFromLoc(StdDraw.getLastLoc(), arena.getGraph()) != -1) {
                p = StdDraw.getLastLoc();
                move(game, arena, nodeFromLoc(p, arena.getGraph()), game_algo);
            }
            StdDraw.clear();
            Graph_GUI.setLastGraph(arena.getGraph());
            gui = new Thread(new Graph_GUI());
            gui.start();
            try {
                gui.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            arena.addFruits(game.getFruits());
            drawFruits(arena);
            drawRobots(arena);
            StdDraw.show();
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(game.toString());
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

    private static void drawFruits(Arena a) {
        List<Fruit> fruits = a.getFruits();
        for (int i = 0; i < fruits.size(); i++) {
            StdDraw.picture(fruits.get(i).getLocation().x(), fruits.get(i).getLocation().y(), "banana.jpg", 0.0005, 0.0005);
        }
    }

    private static void drawRobots(Arena a) {
        List<Robot> robots = a.getRobots();
        StdDraw.setPenRadius(0.05);
        StdDraw.setPenColor(Color.red);
        for (int i = 0; i < robots.size(); i++) {
            StdDraw.point(robots.get(i).getCurrentLocation().x(), robots.get(i).getCurrentLocation().y());
        }
    }
    //finds if to points are close enough to be the same one
    private static boolean close(Point3D p1, Point3D p2) {
        if (p1 == null || p2 == null) return false;
        double EPSILONX = 0.001;
        double EPSILONY = 0.001;
        if (Math.abs(p1.x() - p2.x()) > EPSILONX) return false;
        if (Math.abs(p1.y() - p2.y()) > EPSILONY) return false;
        return true;
    }

    //moves the closest robot to desired node
    private static void move(game_service game, Arena arena, int t, Game_Algo game_algo) {
        int rToMove = game_algo.closestRobotsToNode(t).get(0).getID();
        arena.updateRobots(game.move());
        if (arena.getRobots().get(rToMove).getDest() == -1) {
            arena.updateRobots(game.move());
            game.chooseNextEdge(rToMove, t);
            Robot r = arena.getRobots().get(rToMove);
            r.setDest(t);
        }
        System.out.println("moved robot: " + rToMove + " to node " + t);
    }

    @Override
    public void run() {
        while (StdDraw.getMode().equals("") || StdDraw.getMap().equals("")) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //System.out.println(Integer.parseInt(StdDraw.getMap().substring(1)));
        ManualMode(Integer.parseInt(StdDraw.getMap().substring(1)));
    }
}
