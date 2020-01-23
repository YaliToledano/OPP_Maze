package dataStructure;

import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;

import javax.management.Query;
import java.util.*;

/**
 * this class contains and keeps track of every data structure for the game
 */
public class Arena implements _arena {
    private graph graph;
    private Vector<Fruit> fruits;
    private List<Robot> robots;
    private int fruitsCount;
    private int robotsCount;
    private Queue<Fruit> unassigned;

    public Arena() {
        fruits = new Vector<>();
        robots = new ArrayList<Robot>();
        unassigned = new LinkedList<>();
        fruitsCount = 0;
        robotsCount = 0;
    }

    public dataStructure.graph getGraph() {
        return graph;
    }

    public List<Fruit> getFruits() {
        return fruits;
    }

    public List<Robot> getRobots() {
        return robots;
    }

    public int getFruitsCount() {
        return fruitsCount;
    }

    public int getRobotsCount() {
        return robotsCount;
    }

    public Queue<Fruit> getUnassigned() {
        return unassigned;
    }

    public void setFruits(Vector<Fruit> fruits) {
        this.fruits = fruits;
    }

    public void setGraph(dataStructure.graph graph) {
        this.graph = graph;
    }

    public void setRobots(List<Robot> robots) {
        this.robots = robots;
    }

    public void setFruitsCount(int fruitsCount) {
        this.fruitsCount = fruitsCount;
    }

    public void setRobotsCount(int robotsCount) {
        this.robotsCount = robotsCount;
    }

    public void setUnassigned(Queue<Fruit> unassigned) {
        this.unassigned = unassigned;
    }

    //parameter for deciding if a fruit is on an edge
    private double dist(edge_data e, Point3D pos) {
        Point3D p1 = this.graph.getNode(e.getSrc()).getLocation();
        Point3D p2 = this.graph.getNode(e.getDest()).getLocation();
        return Math.abs((p1.distance2D(pos) + p2.distance2D(pos)) - p1.distance2D(p2));
    }

    public void setFruitEdge(Fruit f) {
        Point3D pos = f.getLocation();
        ArrayList<edge_data> edges = (ArrayList<edge_data>) ((DGraph) graph).getAllE();
        /*
        ArrayList<edge_data> match = new ArrayList<edge_data>();
        for (edge_data e : edges) {
            if (isOnEdge((Edge) e, pos))
                match.add(e);
        }

        edge_data selected_edge = null;

        if (f.getType() == 1) {
            double min = Double.POSITIVE_INFINITY;
            for (edge_data e : match) {
                if (min >e.getWeight()) {
                    min =e.getWeight();
                    selected_edge = e;
                }
            }

        } else {
            double max = Double.NEGATIVE_INFINITY;
            for (edge_data e : match) {
                if (max < e.getWeight()) {
                    max = e.getWeight();
                    selected_edge = e;
                }
            }
        }
        // System.out.println("Edge is "+selected_edge.getSrc() +" "+selected_edge.getDest());
        f.setEdge((Edge) selected_edge);

         */
        double minDist = Double.POSITIVE_INFINITY;
        edge_data Edge = null;
        for (edge_data e : edges) {
            if (minDist >= dist(e, pos)) {
                if ((f.getType() == -1 && e.getSrc() > e.getDest()) || (f.getType() == 1 && e.getSrc() < e.getDest())) {
                    minDist = dist(e, pos);
                    Edge = e;
                }
            }
        }
        f.setEdge((Edge) Edge);
    }

    /**
     *uses distance to decide if a point is on a given edge.(used in fruit edge finding.
     * @param e   edge to check
     * @param pos of point
     * @return if the ethe given pos is on the edge
     */
    private boolean isOnEdge(Edge e, Point3D pos) {
        Point3D p1 = this.graph.getNode(e.getSrc()).getLocation();
        Point3D p2 = this.graph.getNode(e.getDest()).getLocation();
        if (Math.abs((p1.distance2D(pos) + p2.distance2D(pos)) - p1.distance2D(p2)) <= 0.0001) return true;
        return false;
    }

    public void addFruits(List<String> json) {
        try {
            //fruits = new Vector<>();
            fruitsCount = 0;
            Iterator<String> it = json.iterator();
            while (it.hasNext()) {
                Fruit f = new Fruit(fruitsCount, it.next());
                setFruitEdge(f);
                fruits.add(f);
                fruitsCount++;
            }
            /*
            for (Fruit fr:fruits) {
                if(!fr.isAssigned())
                    unassigned.add(fr);
            }
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRobot(Robot robot) {
        try {
            robots.add(robot);
            robot.setDest(-1);
            robotsCount++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * adds all data realted to the robots from the game server.
     *
     * @param json output from the game server
     */
    public void addRobots(List<String> json) {
        try {
            robots = new ArrayList<Robot>();
            for (String j : json) {
                Robot r = new Robot(j);
                robots.add(r);
                robotsCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateRobots(List<String> json) {
        if (json == null) return;
        try {
            for (String s : json) {

                JSONObject jj = new JSONObject(s);
                JSONObject j = jj.getJSONObject("Robot");
                Robot r = searchRobot(j.getInt("id"));
                r.update(s);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateFruits(List<String> json) {
        try {
            int count = 0;
            for (String s : json) {
                Fruit f = fruits.get(count);
                JSONObject jj = new JSONObject(s);
                JSONObject j = jj.getJSONObject("Fruit");
                f.setValue(j.getDouble("value"));
                f.setType(j.getInt("type"));
                String[] pos1 = j.getString("pos").split(",");
                f.setPos(new Point3D(Double.parseDouble(pos1[0]), Double.parseDouble(pos1[1])));
                setFruitEdge(f);
                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * search an returns a robot by it's id
     *
     * @param id
     * @return
     */
    public Robot searchRobot(int id) { //search robot in the list by id
        for (Robot r : robots) {
            if (r.getId() == id)
                return r;
        }
        return null;
    }


    @Override
    public edge_data getEdge() {
        return null;
    }

    @Override
    public int move(Robot r, Node n) {

        return 1;
    }
}
