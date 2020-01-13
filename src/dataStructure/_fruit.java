package dataStructure;

import utils.Point3D;

/**
 * interface to show capabilities of a Fruit
 */
public interface _fruit {
    public Point3D getLocation();

    public double getValue();

    public edge_data getEdge();//returns the edge fruit is currently on

    public int getType();
}
