package dataStructure;

import Server.Game_Server;
import Server.game_service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Point3D;

import static org.junit.jupiter.api.Assertions.*;

class FruitTest {
    Fruit f;
    game_service game;

    @BeforeEach
    void setUp() {
        game = Game_Server.getServer(0);
        f = new Fruit(0, game.getFruits().get(0));//type -1 value 5 loc 35.197656770719604,32.10191878639921,0.0
    }

    @Test
    void constructTest() {
        Fruit f2 = new Fruit();
        f2.setPos(new Point3D(35.197656770719604, 32.10191878639921, 0.0));
        f2.setType(-1);
        f2.setValue(5.0);
        assertEquals(f2.getValue(), f.getValue());
        assertEquals(f2.getType(), f.getType());
        assertEquals(f2.getType(), f.getType());
    }
}