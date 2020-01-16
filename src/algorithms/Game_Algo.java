package algorithms;

import dataStructure.Arena;
import dataStructure.Fruit;
import dataStructure.Robot;
import java.util.*;

public class Game_Algo {
    private Arena arena;
    private Graph_Algo ga;

    public Game_Algo(Arena arena){
        this.arena = arena;
        ga = new Graph_Algo(arena.getGraph());
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public List<Robot> closestRobotsToFruit(Fruit fruit)
    {
        List<Robot> robotorder = new ArrayList<Robot>();
        List<Robot> robots = arena.getRobots(); //original robots list
        //creating distance list
        List<Double> dist = new ArrayList<Double>();
        for (Robot r:robots) {
            dist.add(ga.shortestPathDist(r.getSrc(),fruit.getEdge().getSrc()));
        }
        List<Double> distorder = new ArrayList<Double>(); //crating empty distance list that will be parallel to the new robots list
        for (int i = 0; i < robots.size(); i++) {  //sorting the new robots list with the distance list (insertion sort)
            int j = 0;
            while(j<robotorder.size()&&distorder.get(j)<dist.get(i)){
                j++;
            }
            robotorder.add(j,robots.get(i));
            distorder.add(j,dist.get(i));
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
        for (Fruit f:arena.getFruits()) {
            ls.add(f.getEdge().getSrc());
            count++;
        }
        for (int i = count; i < numOfRobots; i++) {
            ls.add(1 + (int) Math.random() * arena.getGraph().getV().size());
        }
        return ls;
    }

    public void AutomaticMode()
    {

    }





}
