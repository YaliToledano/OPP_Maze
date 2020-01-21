package dataStructure;

import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

public class Robot implements _robot {

    private int id;
    private Point3D pos;
    private int src;
    private int dest;
    private Queue<Integer> targetNodes;
    private double speed;
    private double value;
    private Fruit fruit;

    public Robot() {
    }

    public Robot(int src) {
        this.src = src;
    }

    public Robot(String json) {
        try {
            JSONObject jj = new JSONObject(json);
            JSONObject j = jj.getJSONObject("Robot");
            this.id = j.getInt("id");
            this.src = j.getInt("src");
            this.dest = -1;
            this.value = j.getDouble("value");
            this.speed = j.getDouble("speed");
            String[] pos1 = j.getString("pos").split(",");
            pos = new Point3D(Double.parseDouble(pos1[0]), Double.parseDouble(pos1[1]));
            targetNodes = new LinkedBlockingQueue<>();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void update(String json) {
        try {
            JSONObject jj = new JSONObject(json);
            JSONObject j = jj.getJSONObject("Robot");
            this.src = j.getInt("src");
            this.dest = j.getInt("dest");
            this.value = j.getDouble("value");
            this.speed = j.getDouble("speed");
            String[] pos1 = j.getString("pos").split(",");
            pos = new Point3D(Double.parseDouble(pos1[0]), Double.parseDouble(pos1[1]));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Point3D getCurrentLocation() {
        return pos;
    }

    @Override
    public double currentScore() {
        return 0;
    }

    @Override
    public int getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public int getSrc() {
        return src;
    }

    public int getDest() {
        return dest;
    }


    public void setSrc(int src) {
        this.src = src;
    }


    public void setDest(int dest) {
        this.dest = dest;
    }

    @Override
    public double getSpeed() {
        return speed;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setPos(Point3D pos) {
        this.pos = pos;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setTargetNodes(Queue<Integer> targetNodes) {
        this.targetNodes = targetNodes;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setFruit(Fruit f) {
        this.fruit = f;
    }


    public Queue<Integer> getTargetNodes() {
        return targetNodes;
    }

    public int getDesttNode() {
        return dest;
    }

    public Fruit getFruit() {
        return fruit;
    }
}
