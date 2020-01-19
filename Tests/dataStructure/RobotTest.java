package dataStructure;

import Server.Game_Server;
import Server.game_service;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Point3D;

import static org.junit.jupiter.api.Assertions.*;

class RobotTest {
    game_service game;
    Robot r;

    @BeforeEach
    void setUp() {
        game = Game_Server.getServer(0);
        game.addRobot(0);
        game.startGame();
    }

    @Test
    void update() {
        Robot r = new Robot();
        r.update(game.getRobots().get(0));
        Robot r2 = new Robot();
        r2.setPos(new Point3D(35.18753053591606, 32.10378225882353, 0.0));
        r2.setSpeed(1.0);
        r2.setValue(0.0);
        r2.setSrc(0);
        //assertEquals(r2.getSrc(),r.getSrc());
        assertEquals(r2.getSpeed(), r.getSpeed());
        assertEquals(r2.getValue(), r.getValue());
        assertEquals(r2.getCurrentLocation(), r.getCurrentLocation());
    }
}