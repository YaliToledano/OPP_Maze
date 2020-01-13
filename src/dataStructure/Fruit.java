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
    private static int count = 0;

    public Fruit(String json) {
        try {
            id = count++;
            JSONObject jj = new JSONObject(json);
            JSONObject j = jj.getJSONObject("Fruit");
            value = j.getDouble("value");
            type = j.getInt("type");
            String[] pos1 = j.getString("pos").split(",");
            pos = new Point3D(Double.parseDouble(pos1[0]), Double.parseDouble(pos1[1]));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //getters
    public Point3D getLocation() {
        return pos;
    }

    public edge_data getEdge() {
        return null;
    }

    @Override
    public int getType() {
        return 0;
    }

    public double getValue() {
        return value;
    }

    //setters
    public void setPos(Point3D pos) {
        this.pos = new Point3D(pos);
    }
}
