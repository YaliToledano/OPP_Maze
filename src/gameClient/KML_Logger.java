package gameClient;


import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import Server.Game_Server;
import Server.fruits;
import Server.game_service;
import algorithms.Game_Algo;
import algorithms.Graph_Algo;
import dataStructure.*;
import dataStructure.Robot;
import gui.Graph_GUI;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;
import utils.StdDraw;


public class KML_Logger implements Runnable {
    private int scenario_num;
    private game_service game;
    private Arena arena;
    private Graph_Algo ga;
    private DGraph graph;
    private String robotsKML = "";
    private String fruitsKML = "";
    private String nodesKML = "";
    private String edgesKML = "";
    private long timeOfGame;
    private String start = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" +
            "  <Document>\r\n";
    private String end = "</Document>\r\n" +
            "</kml>";

    public KML_Logger(int scenario_num) {
        this.scenario_num = scenario_num;
    }

    public KML_Logger(game_service game, int scenario_num) {
        this.game = game;
        ga = new Graph_Algo();
        this.graph = new DGraph();
        this.graph.init(game.getGraph());
        this.scenario_num = scenario_num;
        arena = new Arena();
        this.arena.setGraph(graph);
        setNodes_kml();
        setEdges_kml();
    }

    //saving kml as kml file at the end of the play run

    public void saveKML() throws IOException {
        save(start + nodesKML + edgesKML + robotsKML + fruitsKML + end);
    }

    //saving kml
    public void save(String kml) throws IOException {
        FileWriter fw = new FileWriter("data/"+scenario_num + ".kml");
        PrintWriter outs = new PrintWriter(fw);
        outs.println(kml);
        outs.close();
        fw.close();
    }

    //adding the nodes to the kml file
    private void setNodes_kml() {
        try {
            for (node_data n : graph.getV()) {
                nodesKML += "<Placemark>" +
                        "<Style id=\"placemark-pink\">\r\n" +
                        "      <IconStyle>\r\n" +
                        "        <Icon>\r\n" +
                        "          <href>http://maps.google.com/mapfiles/kml/paddle/pink-stars.png\r\n" +
                        "</href>\r\n" +
                        "        </Icon>\r\n" +
                        "      </IconStyle>\r\n" +
                        "    </Style>" +
                        "<Point>\r\n" +
                        "      <coordinates>" + n.getLocation().x() + "," + n.getLocation().y() + ",0</coordinates>\r\n" +
                        "    </Point>" +
                        "</Placemark>";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //adding the edges to the kml file
    private void setEdges_kml() {
        try {
            for (edge_data e : graph.getAllE()) {
                edgesKML += "<name>polygon</name>\r\n" +
                        "\r\n" +
                        "	<Style id=\"green-5px\">\r\n" +
                        "		<LineStyle>\r\n" +
                        "			<color>5000D214</color>\r\n" +
                        "			<width>5</width>\r\n" +
                        "		</LineStyle>\r\n" +
                        "	</Style>\r\n" +
                        "\r\n" +
                        "\r\n" +
                        "	<Placemark>\r\n" +
                        "\r\n" +
                        "		<name>Polygon</name>\r\n" +
                        "		<styleUrl>#green-5px</styleUrl>\r\n" +
                        "\r\n" +
                        "		<LineString>\r\n" +
                        "\r\n" +
                        "			<tessellate>1</tessellate>\r\n" +
                        "			<coordinates>\r\n" +
                        graph.getNode(e.getSrc()).getLocation().x() + "," + graph.getNode(e.getSrc()).getLocation().y() + ",0\r\n" +
                        graph.getNode(e.getDest()).getLocation().x() + "," + graph.getNode(e.getDest()).getLocation().y() + ",0\r\n" +
                        "			</coordinates>\r\n" +
                        "\r\n" +
                        "		</LineString>\r\n" +
                        "\r\n" +
                        "	</Placemark>";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //automatic gameplay
    @Override
    public void run()
    {
        synchronized(this)
        {
            try {
                playAutomatic();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        synchronized (this) { //saving the file
            String kml = start + nodesKML + edgesKML + robotsKML + fruitsKML + end;
            try {
                save(kml);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //adding new time stamps to the kml fields (update)

    public void update(Arena arena)
    {
        this.arena = arena;
        robotsKML+=robotsKML();//adding to the robots new information
        fruitsKML+=fruitsKML();//adding to the fruits new information
    }

    //create kml file for specific game number as automatic gameplay
    public void playAutomatic() {
        //init game service and graph
        this.arena = new Arena();
        this.game = Game_Server.getServer(scenario_num);
        String graph = this.game.getGraph();
        DGraph graph1 = new DGraph();
        graph1.init(graph);
        this.arena.setGraph(graph1);

        this.graph = graph1;
        setNodes_kml();
        setEdges_kml();

        //fruits placement
        this.arena.addFruits(this.game.getFruits());


        int numRobots = 0;
        try {
            numRobots = new JSONObject(this.game.toString()).getJSONObject("GameServer").getInt("robots");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Game_Algo game_algo = new Game_Algo(arena);
        //robots init and placement
        for (int i = 0; i < numRobots; i++) {
            List<Integer> ls = game_algo.placeRobots(numRobots);
            for (int j = 0; j < ls.size(); j++) {
                this.game.addRobot(ls.get(i));
            }
        }

        this.game.startGame();
        timeOfGame = this.game.timeToEnd();
        System.out.println("game started" + this.game.timeToEnd());
        arena.addRobots(this.game.getRobots());
        while (this.game.isRunning()) {

            game_algo.secG(this.game, 0.2);//the automatic play and update
            robotsKML+=robotsKML();
            fruitsKML+=fruitsKML();


            try {
                Thread.sleep(8);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //creating in kml time stamp for robots at specific time
    private String robotsKML() {
        String output = "";
        long time = (this.timeOfGame - game.timeToEnd()) / 1000;
        for (Robot r : this.arena.getRobots()) {
            output += "<Placemark>\r\n" +
                    "      <TimeSpan>\r\n" +
                    "     <begin>" + time + "</begin>\r\n" +
                    "        <end>" + (time + 1) + "</end>" +
                    " </TimeSpan>\r\n" +
                    "<Style id=\"lodging\">\r\n" +
                    "      <IconStyle>\r\n" +
                    "        <Icon>\r\n" +
                    "          <href>http://maps.google.com/mapfiles/kml/shapes/cabs.png\r\n" +
                    "</href>\r\n" +
                    "        </Icon>\r\n" +
                    "      </IconStyle>\r\n" +
                    "    </Style>" +
                    "      <Point>\r\n" +
                    "        <coordinates>" + r.getCurrentLocation().x() + "," + r.getCurrentLocation().y() + ",0 </coordinates>\r\n" +
                    "      </Point>\r\n" +
                    "    </Placemark>";
        }
        return output;
    }


    //creating in kml time stamp for fruits at specific time
    private String fruitsKML() {
        String output = "";
        long time = (this.timeOfGame - game.timeToEnd()) / 1000;
        for (Fruit f : this.arena.getFruits()) {
            if (f.getType() == -1) {
                output += "<Placemark>\r\n" +
                        "      <TimeSpan>\r\n" +
                        "     <begin>" + time + "</begin>\r\n" +
                        "        <end>" + (time + 1) + "</end>" +
                        " </TimeSpan>\r\n" +
                        "<Style id=\"electronics\">\r\n" +
                        "      <IconStyle>\r\n" +
                        "        <Icon>\r\n" +
                        "          <href>http://maps.google.com/mapfiles/kml/shapes/euro.png\r\n" +
                        "\r\n" +
                        "</href>\r\n" +
                        "        </Icon>\r\n" +
                        "      </IconStyle>\r\n" +
                        "    </Style>" +
                        "      <Point>\r\n" +
                        "        <coordinates>" + f.getPos().x() + "," + f.getPos().y() + ",0 </coordinates>\r\n" +
                        "      </Point>\r\n" +
                        "    </Placemark>";
            } else {
                output += "<Placemark>\r\n" +
                        "      <TimeSpan>\r\n" +
                        "     <begin>" + time + "</begin>\r\n" +
                        "        <end>" + (time + 1) + "</end>" +
                        " </TimeSpan>\r\n" +
                        "<Style id=\"movies\">\r\n" +
                        "      <IconStyle>\r\n" +
                        "        <Icon>\r\n" +
                        "          <href>http://maps.google.com/mapfiles/kml/shapes/earthquake.png\r\n" +
                        "\r\n" +
                        "</href>\r\n" +
                        "        </Icon>\r\n" +
                        "      </IconStyle>\r\n" +
                        "    </Style>" +
                        "      <Point>\r\n" +
                        "        <coordinates>" + f.getPos().x() + "," + f.getPos().y() + ",0 </coordinates>\r\n" +
                        "      </Point>\r\n" +
                        "    </Placemark>";
            }
        }
        return output;
    }

    public void setGame(game_service game) {
        this.game = game;
    }

    //get the kml string
    public String getKML() {
        return start + nodesKML + edgesKML + robotsKML + fruitsKML + end;
    }

    //creating 24 kml's for each game number
    public static void main(String[] args) {
        for (int i = 0; i < 24; i++) {
            KML_Logger kml = new KML_Logger(i);
            Thread t= new Thread(kml);
            t.start();
        }
    }
}

