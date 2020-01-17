package dataStructure;

import utils.Point3D;

public interface _robot {
    public Point3D getCurrentLocation();

    public double currentScore();

    public int getId();

    public Fruit getFruit();

    public double getSpeed();

    public void setId(int id);

    public void setDest(int dest);

    public void setPos(Point3D p);

    public void setFruit(Fruit f);
}
