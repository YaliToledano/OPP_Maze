package dataStructure;

import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;

/**
 * this class represents a fruit from game server init by json string
 */
public class Fruit implements _fruit {
    private int id;
    private double value;
    private int type;
    private Point3D pos;
    private Edge edge;
    boolean assigned;

    public Fruit() {}

    public Fruit(int id ,String json) {
        try {
            this.id = id;
            JSONObject jj = new JSONObject(json);
            JSONObject j = jj.getJSONObject("Fruit");
            value = j.getDouble("value");
            type = j.getInt("type");
            String[] pos1 = j.getString("pos").split(",");
            pos = new Point3D(Double.parseDouble(pos1[0]), Double.parseDouble(pos1[1]));
            assigned = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //getters


    public int getId() {
        return id;
    }

    public Point3D getLocation() {
        return pos;
    }

    public edge_data getEdge() {
        return this.edge;
    }

    @Override
    public int getType() {
        return type;
    }

    public double getValue() {
        return value;
    }

    public Point3D getPos() {
        return pos;
    }

    //setters
    public void setPos(Point3D pos) {
        this.pos = new Point3D(pos);
        //setAssigned(false);
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public boolean isAssigned() {
        return assigned;
    }


}
