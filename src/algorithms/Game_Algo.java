package algorithms;

import Server.Game_Server;
import Server.game_service;
import dataStructure.*;
import gameClient.MyGameGUI;
import utils.Point3D;


import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * class for all algorithms used in the automatic solution of the stages
 */

public class Game_Algo implements Runnable {
    private Arena arena;
    private Graph_Algo ga;
    private game_service game;
    private int flag = 0;
    private long lasttime;

    public void setGame(game_service game) {
        this.game = game;
    }

    public Game_Algo(Arena arena) {
        this.arena = arena;
        ga = new Graph_Algo(arena.getGraph());
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public List<Fruit> priorityFruitToRobot(Robot r) {
        List<Fruit> fruitsorder = new ArrayList<Fruit>();
        List<Fruit> fruits = arena.getFruits(); //original fruits list
        // System.out.println(fruits.get(0).getEdge().getSrc());
        //creating distance list
        List<Double> dist = new ArrayList<>();
        List<Double> distorder = new ArrayList<Double>();
        Double close = Double.POSITIVE_INFINITY;

        double x = 0;
        for (Fruit f : fruits) {
            dist.add(Math.pow(ga.shortestPathDist(r.getSrc(), f.getEdge().getSrc()), 3) / f.getValue());
        }
        for (Fruit f : fruits) {
            x = ga.shortestPathDist(r.getSrc(), f.getEdge().getSrc()) + f.getEdge().getWeight();
            if (x < close) {
                close = x;
            }
        }

        for (int i = 0; i < fruits.size(); i++) {  //sorting the new robots list with the distance list (insertion sort)
            int j = 0;
            while (j < fruitsorder.size() && distorder.get(j) < dist.get(i)) {
                j++;
            }
            fruitsorder.add(j, fruits.get(i));
            distorder.add(j, dist.get(i));
        }


        return fruitsorder;
    }

    public List<Fruit> priorityFruitToRobot2(Robot r) {
        List<Fruit> fruitsorder = new ArrayList<Fruit>();
        List<Fruit> fruits = arena.getFruits(); //original fruits list
        //creating distance list
        List<Double> dist = new ArrayList<>();
        List<Double> distorder = new ArrayList<Double>();
        Double close = Double.POSITIVE_INFINITY;

        double x = 0;
        for (Fruit f : fruits) {
            dist.add(ga.shortestPathDist(r.getSrc(), f.getEdge().getSrc()) + f.getEdge().getWeight() + f.getValue());
        }
        for (Fruit f : fruits) {
            x = ga.shortestPathDist(r.getSrc(), f.getEdge().getSrc()) + f.getEdge().getWeight();
            if (x < close) {
                close = x;
            }
        }

        for (int i = 0; i < fruits.size(); i++) {  //sorting the new robots list with the distance list (insertion sort)
            int j = 0;
            while (j < fruitsorder.size() && distorder.get(j) < dist.get(i)) {
                j++;
            }
            fruitsorder.add(j, fruits.get(i));
            distorder.add(j, dist.get(i));
        }


        return fruitsorder;
    }

    //finds fruit with shortest dist to  given robot
    public List<Fruit> closetFruitToRobot(Robot r) {
        List<Fruit> fruitsorder = new ArrayList<Fruit>();
        List<Fruit> fruits = arena.getFruits(); //original fruits list
        // System.out.println(fruits.get(0).getEdge().getSrc());
        //creating distance list
        List<Double> dist = new ArrayList<>();
        List<Double> distorder = new ArrayList<Double>();
        Double close = Double.POSITIVE_INFINITY;
        Fruit closeFruit = null;
        double x = 0;
        for (Fruit f : fruits) {
            dist.add(ga.shortestPathDist(r.getSrc(), f.getEdge().getSrc()));
        }
        for (Fruit f : fruits) {
            //System.out.println("fruit in " + f.getEdge().getSrc() + " -> " + f.getEdge().getDest());
            x = ga.shortestPathDist(r.getSrc(), f.getEdge().getSrc()) + f.getEdge().getWeight();
            if (x < close) {
                close = x;
                closeFruit = f;
            }
        }

        for (int i = 0; i < fruits.size(); i++) {  //sorting the new robots list with the distance list (insertion sort)
            int j = 0;
            while (j < fruitsorder.size() && distorder.get(j) < dist.get(i)) {
                j++;
            }
            fruitsorder.add(j, fruits.get(i));
            distorder.add(j, dist.get(i));
        }


        return fruitsorder;
    }

    public List<Robot> closestRobotsToFruit(Fruit fruit) {
        List<Robot> robotorder = new ArrayList<Robot>();
        List<Robot> robots = arena.getRobots(); //original robots list
        //creating distance list
        List<Double> dist = new ArrayList<Double>();
        for (Robot r : robots) {
            dist.add(ga.shortestPathDist(r.getSrc(), fruit.getEdge().getSrc()));
        }
        List<Double> distorder = new ArrayList<Double>(); //crating empty distance list that will be parallel to the new robots list
        for (int i = 0; i < robots.size(); i++) {  //sorting the new robots list with the distance list (insertion sort)
            int j = 0;
            while (j < robotorder.size() && distorder.get(j) < dist.get(i)) {
                j++;
            }
            robotorder.add(j, robots.get(i));
            distorder.add(j, dist.get(i));
        }
        return robotorder;
    }

    /**
     * @param t - target node
     * @return list of robots in descending order of distance from t(on the graph not actual distance)
     */
    public List<Robot> closestRobotsToNode(int t) {
        List<Robot> robotorder = new ArrayList<Robot>();
        List<Robot> robots = arena.getRobots(); //original robots list
        //creating distance list
        List<Double> dist = new ArrayList<Double>();
        for (Robot r : robots) {
            dist.add(ga.shortestPathDist(r.getSrc(), t));
        }
        List<Double> distorder = new ArrayList<Double>(); //crating empty distance list that will be parallel to the new robots list
        for (int i = 0; i < robots.size(); i++) {  //sorting the new robots list with the distance list (insertion sort)
            int j = 0;
            while (j < robotorder.size() && distorder.get(j) < dist.get(i)) {
                j++;
            }
            robotorder.add(j, robots.get(i));
            distorder.add(j, dist.get(i));
        }
        return robotorder;
    }

    /**
     * @param numOfRobots
     * @return list of vertexes to place robots on
     */
    public List<Integer> placeRobots(int numOfRobots) //place robots at the beginning
    {
        List<Integer> ls = new ArrayList<>();
        int count = 0;
        for (Fruit f : arena.getFruits()) {
            ls.add(f.getEdge().getSrc());
            count++;
        }
        for (int i = count; i < numOfRobots; i++) {
            //ls.add(1 + (int) Math.random() * arena.getGraph().getV().size());
        }
        return ls;
    }

    /**
     * random walk algorithm
     *
     * @param game current game service
     */
    public void AutomaticMode(game_service game) {
        for (Robot r : arena.getRobots()) {
            if (r.getDest() == -1) {
                Collection<edge_data> ee = this.arena.getGraph().getE(r.getSrc());
                Iterator<edge_data> itr = ee.iterator();
                int s = ee.size();
                int d = (int) (Math.random() * s);
                int i = 0;
                while (i < d) {
                    itr.next();
                    i++;
                }
                //System.out.println(i + " " + d + " " + s);
                r.setDest(itr.next().getDest());
                game.chooseNextEdge(r.getId(), r.getDest());
                arena.updateRobots(game.move());
                arena.updateFruits(game.getFruits());
                System.out.println("moved robot " + r.getId() + " to node " + r.getDest());

            }
        }
    }

    //greedy algorithm - fruit first approach
    public void greedyMove(game_service game) {
        for (Fruit f : arena.getFruits()) {
            //if (f.isAssigned()==true) continue;
            Robot r = closestRobotsToFruit(f).get(0);
            //System.out.println("fruit edge"+f.getEdge().getSrc() + " " + f.getEdge().getDest());
            if (r.getTargetNodes().size() == 0) {
                List<node_data> ls = ga.shortestPath(r.getSrc(), f.getEdge().getSrc());
                Queue<Integer> dest = r.getTargetNodes();
                for (node_data n : ls) {
                    dest.add(n.getKey());
                }
                dest.add(f.getEdge().getDest());
                if (dest.peek() == r.getSrc())
                    dest.remove();
                //r.setTargetNodes(dest);
                r.setFruit(f);
                f.setAssigned(true);
            }
        }
        for (Robot r : arena.getRobots()) {
            if (r.getDest() == -1 && r.getTargetNodes().size() > 0) {
                r.setDest(r.getTargetNodes().remove());
                //System.out.println(r.getTargetNodes().toString());
                game.chooseNextEdge(r.getId(), r.getDest());

                System.out.println("moved robot " + r.getId() + " to node " + r.getDest());
            }
            if (r.getDest() == -1 && r.getTargetNodes().size() == 0) {
                if (r.getFruit() != null)
                    r.getFruit().setAssigned(false);
                r.setFruit(null);
                //r.setTargetNodes(null);
            }
        }
        arena.updateRobots(game.move());
        arena.updateFruits(game.getFruits());
    }

    /**
     *  greedy approach to solve the stages sends each robot to it's closest fruit and if two robots want to go to the
     *  same fruit it sends one to a random fruit
     * @param game
     */
    public void basicG(game_service game) {
        List<Fruit> ff = null;
        List<Robot> ls = arena.getRobots();
        for (Robot r : ls) {
            if (r.getDest() == -1 && r.getFruit() == null) {

                ff = closetFruitToRobot(r);
                Fruit f = ff.remove(0);

                //while (f.isAssigned()) {//if any other robot already goes to that fruit chooses next closest fruit
                //  f = ff.remove(0);
                //}
                r.setFruit(f);
                f.setAssigned(true);

                List<node_data> nodesPath = ga.shortestPath(r.getSrc(), f.getEdge().getSrc());
                for (node_data n : nodesPath) {
                    r.getTargetNodes().add(n.getKey());
                }
                //System.out.println(r.getTargetNodes());
                r.getTargetNodes().add(f.getEdge().getDest());

                //System.out.println("course is " + r.getTargetNodes().toString());

            } else if (r.getDest() == -1 && !r.getTargetNodes().isEmpty()) {
                r.setDest(r.getTargetNodes().remove());
                System.out.println("moved robot " + r.getId() + " to node " + r.getDest());
                game.chooseNextEdge(r.getId(), r.getDest());
            } else if (r.getDest() != -1 && !r.getTargetNodes().isEmpty()) {
                arena.updateRobots(game.move());
            }
            if (r.getTargetNodes().isEmpty() && r.getDest() == -1) {
                r.setFruit(null);
            }
            arena.updateFruits(game.getFruits());
            arena.updateRobots(game.getRobots());
            //System.out.println(game.getRobots());
            //System.out.println(game.move());
            //System.out.println(arena.getFruits().get(0).getEdge().getSrc() + " " +arena.getFruits().get(0).getEdge().getDest() );
        }
    }


    public static void main(String[] args) {
        game_service game = Game_Server.getServer(1);
        String graph = game.getGraph();
        DGraph graph1 = new DGraph();
        graph1.init(graph);
        ArrayList<node_data> ln = (ArrayList<node_data>)graph1.getV();
        for (node_data n:ln) {
            System.out.println(n.getKey()+"   ("+n.getLocation().x()+","+n.getLocation().y());
        }
    }

    public void secG(game_service game) {
        for (Robot r : arena.getRobots()) {
            if (r.getDest() == -1) {
                if (r.getTargetNodes().isEmpty()) {
                    List<Fruit> ff = closetFruitToRobot(r);
                    Fruit f = ff.remove(0);
                    while (f.isAssigned()) {//if any other robot already goes to that fruit chooses next closest fruit
                        f = ff.remove(0);
                    }
                    List<node_data> path = ga.shortestPath(r.getSrc(), f.getEdge().getSrc());
                    for (node_data n : path) {
                        r.getTargetNodes().add(n.getKey());
                    }
                    r.getTargetNodes().add(f.getEdge().getDest());
                    f.setAssigned(true);
                    r.setFruit(f);
                } else {
                    r.setDest(r.getTargetNodes().remove());
                    game.chooseNextEdge(r.getId(), r.getDest());
                    r.setEdge((Edge) arena.getGraph().getEdge(r.getSrc(), r.getDest()));
                    this.lasttime = (new Date()).getTime();
                }
            } else {
                long now = (new Date()).getTime();
                double dt = (double) (now - this.lasttime) / 1000.0D;
                double v = r.getSpeed();
                double pr = v * dt / r.getEdge().getWeight();
                int dest = r.getEdge().getDest();
                Point3D psrc = arena.getGraph().getNode(r.getFruit().getEdge().getSrc()).getLocation();
                Point3D pdest = arena.getGraph().getNode(r.getFruit().getEdge().getDest()).getLocation();
                double prf = psrc.distance2D(r.getFruit().getLocation()) / psrc.distance2D(pdest);
                if (Math.abs(pr - prf) <= 0.02 || pr >= 0.996) {
                    arena.updateRobots(game.move());
                } else {
                    //arena.updateRobots(game.move());
                }
            }
            arena.updateFruits(game.getFruits());
            arena.updateRobots(game.getRobots());
        }
    }

    public void thG(game_service game) {
        for (Robot r : arena.getRobots()) {
            if (r.getDest() == -1) {
                if (r.getTargetNodes().isEmpty()) {
                    List<Fruit> ff = priorityFruitToRobot(r);
                    Fruit f = ff.remove(0);
                    while (f.isAssigned()) {//if any other robot already goes to that fruit chooses next closest fruit
                        f = ff.remove(0);
                    }
                    List<node_data> path = ga.shortestPath(r.getSrc(), f.getEdge().getSrc());
                    for (node_data n : path) {
                        r.getTargetNodes().add(n.getKey());
                    }
                    r.getTargetNodes().add(f.getEdge().getDest());
                    f.setAssigned(true);
                    r.setFruit(f);
                } else {
                    r.setDest(r.getTargetNodes().remove());
                    game.chooseNextEdge(r.getId(), r.getDest());
                    r.setEdge((Edge) arena.getGraph().getEdge(r.getSrc(), r.getDest()));
                    this.lasttime = (new Date()).getTime();
                }
            } else {
                long now = (new Date()).getTime();
                double dt = (double) (now - this.lasttime) / 1000.0D;
                double v = r.getSpeed();
                double pr = v * dt / r.getEdge().getWeight();
                int dest = r.getEdge().getDest();
                Point3D psrc = arena.getGraph().getNode(r.getFruit().getEdge().getSrc()).getLocation();
                Point3D pdest = arena.getGraph().getNode(r.getFruit().getEdge().getDest()).getLocation();
                double prf = psrc.distance2D(r.getFruit().getLocation()) / psrc.distance2D(pdest);
                if (Math.abs(pr - prf) <= 0.015 || pr >= 0.996) {
                    arena.updateRobots(game.move());
                } else {
                    //arena.updateRobots(game.move());
                }
            }
            arena.updateFruits(game.getFruits());
            arena.updateRobots(game.getRobots());
        }
    }
    @Override
    public void run() {
        while (game.isRunning()) {
            secG(game);
            try {
                Thread.sleep(9);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

