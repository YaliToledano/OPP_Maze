package gameClient;


import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

import algorithms.Game_Algo;
import algorithms.Graph_Algo;
import dataStructure.*;
import dataStructure.Robot;
import gui.Graph_GUI;
import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import utils.Point3D;
import utils.Range;
import utils.StdDraw;

/**
 * class that runs as thread (ex. in main) responsible for drawing and update the drawing  of fruits and robots 10 times a second
 * it waits for input from GUI and then runs the play method.
 */
public class MyGameGUI implements Runnable {

    public static void main(String[] args) {
        Thread t = new Thread(new MyGameGUI());
        t.start();
    }


    private static void play(int scenario_num, String Mode) {
        //init game service and graph
        Arena arena = new Arena();
        int id = 322663816;
        game_service game = null;
        //Game_Server.login(id);
        game = Game_Server.getServer(scenario_num);
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

        //creating KML option
        //this kml is updating while any game is running and you must end the program in order to guarantee it will saving
        KML_Logger kml = new KML_Logger(game, scenario_num);

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

        Game_Algo game_algo = new Game_Algo(arena);
        game_algo.setGame(game);
        game_algo.setScenario_num(scenario_num);
        //robots init and placement
        Point3D p = StdDraw.getLastLoc();
        for (int i = 0; i < numRobots; i++) {
            if (Mode.equals("Manual")) {
                while (StdDraw.getLastLoc() == null || close(p, StdDraw.getLastLoc())) {

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                p = StdDraw.getLastLoc();
                //System.out.println("robot in " + p.toString());
                StdDraw.setPenRadius(0.05);
                StdDraw.setPenColor(Color.red);
                StdDraw.point(p.x(), p.y());
                int src = nodeFromLoc(p, arena.getGraph());
                game.addRobot(src);
            } else {
                //System.out.println(numRobots);
                List<Integer> ls = game_algo.placeRobots(numRobots);
                for (int j = 0; j < ls.size(); j++) {
                    game.addRobot(ls.get(j));
                    StdDraw.setPenRadius(0.05);
                    StdDraw.setPenColor(Color.red);
                    p = arena.getGraph().getNode(ls.get(j)).getLocation();
                    StdDraw.point(p.x(), p.y());
                }
            }
        }
        System.out.println("init robot");

        StdDraw.enableDoubleBuffering();

        //moves and game loop
        game.startGame();
        System.out.println("game started ");
        arena.addRobots(game.getRobots());
        game_algo.placeRobotsF(numRobots);
        game_algo.setGame(game);
        Thread gameA = new Thread(game_algo);
        if (Mode.equals("Automatic"))
            gameA.start();
        while (game.isRunning()) {
            //StdDraw.text(,0,"time: " + game.timeToEnd());
            //System.out.println(game.timeToEnd());
            //Range rx = Graph_GUI.getX(),ry=Graph_GUI.getY();
            //StdDraw.text(rx.get_max()-0.5,ry.get_max()-0.5,game.timeToEnd()+"");
            if (Mode.equals("Manual")) {
                if (StdDraw.getLastLoc() != null || !close(p, StdDraw.getLastLoc()) || nodeFromLoc(StdDraw.getLastLoc(), arena.getGraph()) != -1) {
                    p = StdDraw.getLastLoc();
                    move(game, arena, nodeFromLoc(p, arena.getGraph()), game_algo);
                    kml.update(arena); //update the kml with new information
                }
            } else {
                //game_algo.basicG(game);

            }
            reDraw(game, arena, gui);
            try {
                Thread.sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(game.toString());
        //kml.saveKML();
        //game.sendKML(kml.toString());
    }

    //draws all required components
    private static void reDraw(game_service game, Arena arena, Thread gui) {
        StdDraw.clear();
        Graph_GUI.setLastGraph(arena.getGraph());
        gui = new Thread(new Graph_GUI());
        gui.start();
        try {
            gui.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        drawFruits(arena);
        drawRobots(arena);
        StdDraw.show();
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
            if (fruits.get(i).getType() == -1) {
                StdDraw.picture(fruits.get(i).getLocation().x(), fruits.get(i).getLocation().y(), "banana.jpg", 0.0008, 0.0008);
            } else {
                StdDraw.picture(fruits.get(i).getLocation().x(), fruits.get(i).getLocation().y(), "apple.png", 0.0008, 0.0008);
            }
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
        int rToMove = game_algo.closestRobotsToNode(t).get(0).getId();
        arena.updateRobots(game.move());
        arena.updateFruits(game.getFruits());
        ;
        if (arena.getRobots().get(rToMove).getDest() == -1) {
            arena.updateRobots(game.move());
            game.chooseNextEdge(rToMove, t);
            Robot r = arena.getRobots().get(rToMove);
            r.setDest(t);
        }
        System.out.println("moved robot: " + rToMove + " to node " + t);
    }

    /**
     * uncomment while loop and play (try catch )if you want to choose yourself stages and modes
     */
    @Override
    public void run() {
        int[] a = {0, 1, 3, 5, 9, 11, 13, 16, 19, 20, 23};
        for (int k = 0; k < a.length; k++) play(a[k], "Automatic");

            /*
            while (StdDraw.getMode().equals("") || StdDraw.getMap().equals("")) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
             */
        //System.out.println(Integer.parseInt(StdDraw.getMap().substring(1)) + StdDraw.getMode());
        try {
            //System.out.println("$$$$$$ stage: " + i + " $$$$$$");

            //play(Integer.parseInt(StdDraw.getMap()), "Automatic");
            //System.out.println("$$$$$$ stage: " + i + " $$$$$$");

            //i = i + 1;
        } catch (Exception e) {
            //e.printStackTrace();
        }
        StdDraw.setMap("");
        StdDraw.setMode("");
    }

    int t = 4;
}
