import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.graph;
import dataStructure.node_data;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.Point3D;

import static org.junit.jupiter.api.Assertions.*;

class DGraphTest {
    static graph g;

    @BeforeAll
    static void init() {
        g = new DGraph();
        Point3D[] pp = {new Point3D(-20, 20, 1), new Point3D(-23, -15, 6), new Point3D(-10, 9, 2), new Point3D(31, -5, 1), new Point3D(11, 22, 21), new Point3D(0, -7, 5)};
        for (int i = 0; i < 6; i++) {
            node_data a = new Node(i, pp[i], 0);
            g.addNode(a);
        }

    }
    @Test
    void addNode() {
        g.addNode(new Node(100, new Point3D(20, 10), 5.0));
        assertNotNull(g.getNode(100));
    }

    @Test
    void connect() {
        g.connect(1, 6, 7.8);
        assertNotNull(g.getEdge(1, 6));
    }

    @Test
    void removeNode() {
        g.removeNode(100);
        assertNull(g.getNode(100));
    }

    @Test
    void removeEdge() {
        g.removeEdge(1, 6);
        assertNull(g.getEdge(1, 6));
    }
}