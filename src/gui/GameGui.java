package gui;

import dataStructure.Fruit;
import dataStructure.Robot;
import utils.StdDraw;

import java.util.ArrayList;
import java.util.List;

/**
 * class that is responsible on drawing fruits and robots and watching over their locations
 */
public class GameGui implements Runnable {
    private static int MCF = 0, MCR = 0;
    private static List<Fruit> f = new ArrayList<>();
    private static List<Robot> r = new ArrayList<>();

    //draws a fruit and adds it to watched list
    public void drawFruits(Fruit fruit) {
        StdDraw.picture(fruit.getLocation().x(), fruit.getLocation().y(), "banana.jpg", 1, 1);
        f.add(fruit);
    }

    //draws a robot and adds it to watched list
    public void drawRobots(Robot r) {

    }

    @Override
    public void run() {
        int mcf = MCF, mcr = MCR;
        while (true) {
            if (mcf < mcf) {

                StdDraw.clear();
                for (Fruit fruit : f) {

                }
                //drawFruits();
            } else {
                try {
                    Thread.sleep(1500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
