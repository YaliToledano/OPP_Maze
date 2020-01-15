package dataStructure;

import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;

public class Robot implements _robot {

    private int id;
    private Point3D pos;
    private int src;
    private int dest;
    private int targetNode;
    private double speed;
    private double value;

    public Robot(){}

    public Robot(int src){
        this.src = src;
    }

    public Robot(String json) {
        try {
            this.id = id;
            JSONObject jj = new JSONObject(json);
            JSONObject j = jj.getJSONObject("Robot");
            this.id = j.getInt("id");
            this.src = j.getInt("src");
            this.dest=-1;
            this.targetNode=-1;
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

    public Point3D getPos() {
        return pos;
    }

    @Override
    public int getID() {
        return id;
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



    public void setTargetNode(int targetNode) {
        this.targetNode = targetNode;
    }

    public void setValue(double value) {
        this.value = value;
    }


    public int getTargetNode() {
        return targetNode;
    }

    public int getDesttNode() {
        return dest;
    }


    @Override
    public void move(int target) {
        this.dest = target;
    }

}
