package algorithms;

import Server.game_service;
import dataStructure.Arena;
import dataStructure.Fruit;
import dataStructure.Robot;
import dataStructure.edge_data;
import dataStructure.node_data;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;


public class Game_Algo {
    private Arena arena;
    private Graph_Algo ga;

    public Game_Algo(Arena arena) {
        this.arena = arena;
        ga = new Graph_Algo(arena.getGraph());
    }

    public void setArena(Arena arena) {
        this.arena = arena;
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
            //System.out.println(f.getEdge());
            ls.add(f.getEdge().getSrc());
            count++;
        }
        for (int i = count; i < numOfRobots; i++) {
            ls.add(1 + (int) Math.random() * arena.getGraph().getV().size());
        }
        return ls;
    }

    //random walk
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
                game.chooseNextEdge(r.getID(), r.getDest());
                arena.updateRobots(game.move());
                System.out.println("moved robot " + r.getID() + " to node " + r.getDest());

            }

        }
        /*
        int rToMove = closestRobotsToNode(t).get(0).getID();
        arena.updateRobots(game.move());
        if (arena.getRobots().get(rToMove).getDest() == -1) {
            arena.updateRobots(game.move());
            game.chooseNextEdge(rToMove, t);
            Robot r = arena.getRobots().get(rToMove);
            r.setDest(t);
        }
         */
    }

    //greedy algorithm
    public void greedyMove(game_service game) {
        for (Fruit f : arena.getFruits()) {
            if (f.isAssigned()) continue;
            Robot r = closestRobotsToFruit(f).get(0);
            //System.out.println("fruit edge"+f.getEdge().getSrc() + " " + f.getEdge().getDest());
            List<node_data> ls = ga.shortestPath(r.getSrc(), f.getEdge().getSrc());

            BlockingQueue<Integer> dest = new LinkedBlockingQueue<>();
            for (node_data n : ls) {
                dest.add(n.getKey());
            }
            dest.add(f.getEdge().getDest());
            if (dest.peek() == r.getSrc())
                dest.remove();
            r.setTargetNodes(dest);
            r.setFruit(f);
            f.setAssigned(true);
        }
        for (Robot r : arena.getRobots()) {
            if (r.getDest() == -1 && r.getTargetNodes() != null && r.getTargetNodes().size() > 0) {
                r.setDest(r.getTargetNodes().remove());
                System.out.println(r.getTargetNodes().toString());
                game.chooseNextEdge(r.getID(), r.getDest());

                System.out.println("moved robot " + r.getID() + " to node " + r.getDest());
            } else if (r.getTargetNodes() != null && r.getTargetNodes().size() == 0) {
                if (r.getFruit() != null)
                    r.getFruit().setAssigned(false);
                r.setFruit(null);
                r.setTargetNodes(null);
            }
        }
        arena.updateRobots(game.move());
    }
}

