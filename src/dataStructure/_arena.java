package dataStructure;

import utils.Point3D;

public interface _arena {

    public edge_data getEdge();//returns the edge fruit is currently on

    public int move(Robot r,Node n);//send a robot to a reachable node on a graph

}
