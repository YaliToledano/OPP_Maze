package dataStructure;

import utils.Point3D;

public interface _robot {
    public Point3D getCurrentLocation();

    public double currentScore();

    public int targetNode();

    public int getID();

    public double getSpeed();

    public void move(int target);//set target to go to
}
