package algorithms;

import Server.Game_Server;
import Server.game_service;
import dataStructure.Arena;
import dataStructure.DGraph;
import dataStructure.Fruit;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Game_AlgoTest {
    static Arena arena;
    static game_service game;

    @BeforeAll
    static void setup() {
        arena = new Arena();
        game = Game_Server.getServer(0);
        String graph = game.getGraph();
        DGraph graph1 = new DGraph();
        graph1.init(graph);
        arena.setGraph(graph1);
    }

    @Test
    void placeRobots() {
        game.startGame();
        Game_Algo ga = new Game_Algo(arena);
        arena.addFruits(game.getFruits());
        int numRobots = 0;
        try {
            numRobots = new JSONObject(game.toString()).getJSONObject("GameServer").getInt("robots");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<Integer> ls = ga.placeRobots(numRobots);
        boolean b = false;
        for (Fruit f : arena.getFruits()) {
            for (int i : ls) {
                if (f.getEdge().getSrc() == i) b = true;
            }
            assertTrue(b);
            b = false;
        }
    }

    @Test
    void basicG() {
    }
}