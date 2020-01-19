package gameClient;

import dataStructure.Fruit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Point3D;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class KML_LoggerTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void constructTest() throws IOException {
        KML_Logger kml = new KML_Logger(5);
        kml.playAutomatic();
        kml.saveKML(); //it will save as data/5.kml
        File f = new File("data/5.kml");
        boolean empty = !f.exists()||f.length() == 0;
        assert(!empty);
    }
}