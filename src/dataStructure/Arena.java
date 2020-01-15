package dataStructure;

import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;

import java.util.ArrayList;
import java.util.List;

public class Arena implements _arena {
    private graph graph;
    private List<Fruit> fruits;
    private List<Robot> robots;
    private int fruitsCount;
    private int robotsCount;

    Arena() {
        fruits = new ArrayList<Fruit>();
        robots = new ArrayList<Robot>();
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

    public void setFruits(List<Fruit> fruits) {
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

    public void setFruitEdge(Fruit f)
    {
        Point3D pos =  f.getLocation();
        ArrayList<edge_data> edges = (ArrayList<edge_data>)((DGraph)graph).getAllE();
        ArrayList<edge_data> match =  new ArrayList<edge_data>();
        for (edge_data e:edges) {
            if(isOnEdge((Edge)e,pos))
                match.add(e);
        }

        edge_data selected_edge = null;

        if(f.getType()==1) {
            double max = Double.NEGATIVE_INFINITY;
            for (edge_data e : match) {
                if(max<e.getWeight()){
                    max = e.getWeight();
                    selected_edge = e;
                }
            }
        }
        else
        {
            double min = Double.POSITIVE_INFINITY;
            for (edge_data e : match) {
                if(min>e.getWeight()){
                    min = e.getWeight();
                    selected_edge = e;
                }
            }
        }
        f.setEdge((Edge)selected_edge);
    }

    private boolean isOnEdge(Edge e ,Point3D pos)
    {
        Point3D p1 = this.graph.getNode(e.getSrc()).getLocation();
        Point3D p2 = this.graph.getNode(e.getDest()).getLocation();
        return (((p1.y()-pos.y())/(p1.x()-pos.x()))==((pos.y()-p2.y())/(pos.x()-p2.x())));
    }


    public void addFruit(String json) {
        try {
            Fruit f = new Fruit(fruitsCount,json);
            setFruitEdge(f);
            fruits.add(f);
            fruitsCount++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRobot(Robot robot) {
        try {
            robots.add(robot);
            robotsCount++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addRobots(List<String> json) {
        try {
            robots = new ArrayList<Robot>();
            for (String j:json) {
            Robot r = new Robot(j);
            robots.add(r);
            robotsCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public edge_data getEdge() {
        return null;
    }

    @Override
    public int move(Robot r, Node n) {
    }
}
