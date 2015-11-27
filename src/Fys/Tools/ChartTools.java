package Fys.Tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class ChartTools {

    public int getLostLuggage(LocalDate date) throws ClassNotFoundException, SQLException {
        int lostLuggage = 0;
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT id FROM luggage WHERE registerdate LIKE '"
                    + date.toString() + "%' AND statusId=1");
            while (result.next()) {
                lostLuggage++;
            }
        }
        return lostLuggage;
    }
    
    public int getFoundLuggage(LocalDate date) throws ClassNotFoundException, SQLException {
        int foundLuggage = 0;
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT id FROM luggage WHERE registerdate LIKE '"
                    + date.toString() + "%' AND statusId=2");
            while (result.next()) {
                foundLuggage++;
            }
        }
        return foundLuggage;
    }
    
    public int getConnectedLuggage(LocalDate date) throws ClassNotFoundException, SQLException {
        int connectedLuggage = 0;
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT id FROM connections WHERE connectiondate LIKE '"
                    + date.toString() + "%'");
            while (result.next()) {
                connectedLuggage++;
            }
        }
        return connectedLuggage;
    }
}
