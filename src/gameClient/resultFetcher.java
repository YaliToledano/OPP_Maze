package gameClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * responsible for fetching our results from the database and formatting it.
 */
public class resultFetcher {
    private static final String jdbcUrl = "jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
    private static final String jdbcUser = "student";
    private static final String jdbcUserPassword = "OOP2020student";
    private static int id = 322663816;

    public static String fetch(int rid) {
        id = rid;
        return getUserData() + "\nresults:" + getBestScores();
    }

    //basic user info
    private static String getUserData() {
        String query = "SELECT * FROM Users WHERE UserID=" + id + ";";
        //query = " DESCRIBE Logs" ;
        String data = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                data += "id - " + resultSet.getInt("UserID") + " max level = " + resultSet.getInt("levelNum");
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException sqle) {
            System.out.println("SQLException: " + sqle.getMessage());
            System.out.println("Vendor Error: " + sqle.getErrorCode());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    //gets best stats for each level returns string
    private static String getBestScores() {
        String query = "SELECT * FROM Logs WHERE UserID=" + id + ";";
        StringBuilder sb = new StringBuilder();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            int levelId = 0;
            int max = Integer.MIN_VALUE;
            String rowData = "";
            while (resultSet.next()) {
                if (resultSet.getInt("levelID") != levelId) {
                    sb.append("\n" + rowData);
                    levelId++;
                    max = Integer.MIN_VALUE;
                }

                if (resultSet.getInt("levelID") == levelId) {
                    if (max < resultSet.getInt("score")) {
                        max = resultSet.getInt("score");
                        rowData = "levelID= " + resultSet.getInt("levelID") + ", moves= " + resultSet.getInt("moves") + ", Score " + resultSet.getDouble("score");
                    }
                }
                sb.append("\n" + rowData);
            }
            resultSet.close();
            statement.close();
            connection.close();


        } catch (SQLException sqle) {
            System.out.println("SQLException: " + sqle.getMessage());
            System.out.println("Vendor Error: " + sqle.getErrorCode());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        System.out.print(resultFetcher.fetch(322663816));
    }
}
