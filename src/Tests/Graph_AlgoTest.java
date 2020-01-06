import org.junit.jupiter.api.Test;
import algorithms.Graph_Algo;
import dataStructure.*;
import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.edge_data;
import dataStructure.node_data;
import utils.Point3D;
import utils.Range;
import utils.StdDraw;
import gui.Graph_GUI;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Graph_AlgoTest {

    static graph g = new DGraph();
    static Graph_Algo ga = new Graph_Algo();

    @Test
    public void testInitGraph() {
        ga.init(g);
    }

    @Test
    public void testInitString() {
        try {
            ga.save("graph");
            ga.init("graph.ser");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSave() {
        try {
            ga.save("graph");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsConnected() {
        g = new DGraph();
        ga = new Graph_Algo();
        Point3D[] pp = {new Point3D(-20, 20, 1), new Point3D(-23, -15, 6), new Point3D(-10, 9, 2), new Point3D(31, -5, 1), new Point3D(11, 22, 21), new Point3D(0, -7, 5)};
        for (int i = 0; i < 6; i++) {
            node_data a = new Node(i, pp[i], 0);
            g.addNode(a);
        }
        g.connect(0, 1, 2);
        g.connect(3, 0, 12);
        g.connect(0, 3, 12);
        g.connect(1, 2, 7);
        g.connect(1, 4, 5);
        g.connect(2, 4, 1);
        g.connect(4, 3, 3);
        g.connect(2, 3, 3);
        g.connect(3, 5, 6);
        g.connect(5, 1, 5);
        ga.init(g);
        assertTrue(ga.isConnected());
        g.removeEdge(0, 1);
        assertTrue(ga.isConnected());
        g.removeEdge(5, 1);
        assertFalse(ga.isConnected());
    }

    @Test
    public void testShortestPathDist() {
        g = new DGraph();
        ga = new Graph_Algo();
        Point3D[] pp = {new Point3D(-20, 20, 1), new Point3D(-23, -15, 6), new Point3D(-10, 9, 2),
                new Point3D(31, -5, 1), new Point3D(11, 22, 21), new Point3D(0, -7, 5),
                new Point3D(20, 20, -9), new Point3D(30, 10, -1)};
        for (int i = 0; i < 8; i++) {
            node_data a = new Node(i, pp[i], 0);
            g.addNode(a);
        }
        g.connect(0, 1, 2);
        g.connect(3, 0, 12);
        g.connect(6, 7, 4);
        g.connect(7, 0, 6);
        g.connect(1, 2, 7);
        g.connect(1, 4, 5);
        g.connect(7, 5, 1);
        g.connect(2, 4, 1);
        g.connect(4, 3, 2.75);
        g.connect(2, 3, 3);
        g.connect(3, 5, 6);
        g.connect(0, 3, 12);
        g.connect(4, 6, 2);
        g.connect(5, 1, 5);
        ga.init(g);
        assertEquals(9.75, ga.shortestPathDist(0, 3), 0.01);
        g.removeEdge(0, 1);
        g.removeEdge(0, 3);
        assertEquals(Double.MAX_VALUE, ga.shortestPathDist(0, 3), 10);
        g.connect(0, 1, 2);
        g.connect(0, 3, 12);
        Graph_GUI.draw(g);
        assertEquals(16, ga.shortestPathDist(3, 4), 0.01);
        assertEquals(11, ga.shortestPathDist(7, 4), 0.01);
        assertEquals(0, ga.shortestPathDist(5, 5), 0.01);

    }

    @Test
    public void testShortestPath() {

        g = new DGraph();
        ga = new Graph_Algo();
        Point3D[] pp = {new Point3D(-20, 20, 1), new Point3D(-23, -15, 6), new Point3D(-10, 9, 2),
                new Point3D(31, -5, 1), new Point3D(11, 22, 21), new Point3D(0, -7, 5),
                new Point3D(20, 20, -9), new Point3D(30, 10, -1)};
        for (int i = 0; i < 8; i++) {
            node_data a = new Node(i, pp[i], 0);
            g.addNode(a);
        }
        g.connect(0, 1, 2);
        g.connect(3, 0, 12);
        g.connect(6, 7, 4);
        g.connect(0, 3, 12);
        g.connect(7, 0, 6);
        g.connect(1, 2, 7);
        g.connect(1, 4, 5);
        g.connect(7, 5, 1);
        g.connect(2, 4, 1);
        g.connect(4, 3, 2.75);
        g.connect(2, 3, 3);
        g.connect(3, 5, 6);
        g.connect(4, 6, 2);
        g.connect(5, 1, 5);
        ga.init(g);
        List<Integer> testlist = new ArrayList<Integer>();
        testlist.add(4);
        testlist.add(6);
        testlist.add(7);
        testlist.add(5);
        testlist.add(1);
        List<Integer> actual = new ArrayList<Integer>();
        List<node_data> actualnode = ga.shortestPath(4, 1);
        for (int i = 0; i < actualnode.size(); i++) {
            actual.add(actualnode.get(i).getKey());
        }
        assertEquals(testlist, actual);

        testlist = new ArrayList<Integer>();
        testlist.add(2);
        actual = new ArrayList<Integer>();
        actualnode = ga.shortestPath(2, 2);
        for (int i = 0; i < actualnode.size(); i++) {
            actual.add(actualnode.get(i).getKey());
        }
        assertEquals(testlist, actual);

        testlist = new ArrayList<Integer>();
        actual = new ArrayList<Integer>();

        g.removeEdge(7, 5);
        g.removeEdge(3, 5);
        actualnode = ga.shortestPath(1, 5);
        for (int i = 0; i < actualnode.size(); i++) {
            actual.add(actualnode.get(i).getKey());
        }
        assertEquals(testlist, actual);
        g.connect(3, 5, 6);
        g.connect(7, 5, 1);
    }


    @Test
    public void testTSP() {
        g = new DGraph();
        ga = new Graph_Algo();
        Point3D[] pp = {new Point3D(-20, 20, 1), new Point3D(-23, -15, 6), new Point3D(-10, 9, 2),
                new Point3D(31, -5, 1), new Point3D(11, 22, 21), new Point3D(0, -7, 5),
                new Point3D(20, 20, -9), new Point3D(30, 10, -1)};
        for (int i = 0; i < 8; i++) {
            node_data a = new Node(i, pp[i], 0);
            g.addNode(a);
        }
        g.connect(0, 1, 2);
        g.connect(3, 0, 12);
        g.connect(6, 7, 4);
        g.connect(0, 3, 12);
        g.connect(7, 0, 6);
        g.connect(1, 2, 7);
        g.connect(1, 4, 5);
        g.connect(7, 5, 1);
        g.connect(2, 4, 1);
        g.connect(4, 3, 2.75);
        g.connect(2, 3, 3);
        g.connect(3, 5, 6);
        g.connect(4, 6, 2);
        g.connect(5, 1, 5);
        ga.init(g);

        List<Integer> travel = new ArrayList<Integer>();
        travel.add(1);
        travel.add(5);
        travel.add(4);
        travel.add(2);
        List<Integer> testlist = new ArrayList<Integer>();
        testlist.add(5);
        testlist.add(1);
        testlist.add(2);
        testlist.add(4);
        List<node_data> actualnode = new ArrayList<node_data>();
        actualnode = ga.TSP(travel);
        List<Integer> actual = new ArrayList<Integer>();
        for (int i = 0; i < actualnode.size(); i++) {
            actual.add(actualnode.get(i).getKey());
        }
        assertEquals(testlist, actual);

        g.removeEdge(5, 1);

        testlist = new ArrayList<Integer>();
        testlist.add(1);
        testlist.add(2);
        testlist.add(4);
        testlist.add(6);
        testlist.add(7);
        testlist.add(5);
        actualnode = new ArrayList<node_data>();
        actualnode = ga.TSP(travel);
        actual = new ArrayList<Integer>();
        for (int i = 0; i < actualnode.size(); i++) {
            actual.add(actualnode.get(i).getKey());
        }
        assertEquals(testlist, actual);

        g.removeEdge(1, 2);
        testlist = new ArrayList<Integer>();
        testlist.add(2);
        testlist.add(4);
        testlist.add(6);
        testlist.add(7);
        testlist.add(0);
        testlist.add(1);
        testlist.add(4);
        testlist.add(6);
        testlist.add(7);
        testlist.add(5);
        actualnode = new ArrayList<node_data>();
        actualnode = ga.TSP(travel);
        actual = new ArrayList<Integer>();
        for (int i = 0; i < actualnode.size(); i++) {
            actual.add(actualnode.get(i).getKey());
        }
        assertEquals(testlist, actual);

        g.connect(3, 2, 1);
        testlist = new ArrayList<Integer>();
        testlist.add(1);
        testlist.add(4);
        testlist.add(3);
        testlist.add(2);
        testlist.add(4);
        testlist.add(6);
        testlist.add(7);
        testlist.add(5);
        actualnode = new ArrayList<node_data>();
        actualnode = ga.TSP(travel);
        actual = new ArrayList<Integer>();
        for (int i = 0; i < actualnode.size(); i++) {
            actual.add(actualnode.get(i).getKey());
        }
        assertEquals(testlist, actual);

        travel = new ArrayList<>();
        travel.add(1);
        testlist = new ArrayList<Integer>();
        testlist.add(1);
        actualnode = new ArrayList<node_data>();
        actualnode = ga.TSP(travel);
        actual = new ArrayList<Integer>();
        for (int i = 0; i < actualnode.size(); i++) {
            actual.add(actualnode.get(i).getKey());
        }
        assertEquals(testlist, actual);
        travel = new ArrayList<>();
        testlist = new ArrayList<Integer>();
        actualnode = new ArrayList<node_data>();
        actualnode = ga.TSP(travel);
        actual = new ArrayList<Integer>();
        for (int i = 0; i < actualnode.size(); i++) {
            actual.add(actualnode.get(i).getKey());
        }
        assertEquals(testlist, actual);

    }

    @Test
    public void testCopy() {
        g = new DGraph();
        ga = new Graph_Algo();
        Point3D[] pp = {new Point3D(-20, 20, 1), new Point3D(-23, -15, 6), new Point3D(-10, 9, 2),
                new Point3D(31, -5, 1), new Point3D(11, 22, 21), new Point3D(0, -7, 5),
                new Point3D(20, 20, -9), new Point3D(30, 10, -1)};
        for (int i = 0; i < 8; i++) {
            node_data a = new Node(i, pp[i], 0);
            g.addNode(a);
        }
        g.connect(0, 1, 2);
        g.connect(3, 0, 12);
        g.connect(6, 7, 4);
        g.connect(0, 3, 12);
        g.connect(7, 0, 6);
        g.connect(1, 2, 7);
        g.connect(1, 4, 5);
        g.connect(7, 5, 1);
        g.connect(2, 4, 1);
        g.connect(4, 3, 2.75);
        g.connect(2, 3, 3);
        g.connect(3, 5, 6);
        g.connect(4, 6, 2);
        g.connect(5, 1, 5);
        ga.init(g);
        graph newG = ga.copy();
        assertEquals(newG.getMC(), g.getMC());
    }
}