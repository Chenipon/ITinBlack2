package Fys.Models;

import Fys.Tools.ConnectMysqlServer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class Status {

    private int id;
    private String statusName;

    /**
     * public Status() Status is the constructor for the Status class.
     */
    public Status() {
        this.statusName = "";
    }

    /**
     * public Status(String statusName) is the alternative constructor for the Status class.
     * @param statusName
     */
    public Status(String statusName) {
        this.statusName = statusName;
    }

    /**
     * public int getId() returns the id of the Status.
     * @return id The id of the Status.
     */
    public int getId() {
        return id;
    }

    /**
     * public void setId(int id) sets the id of the Status.
     * @param id The id of the Status.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * public String getStatusName() returns the statusname of the Status.
     * @return statusname The statusname of the Status.
     */
    public String getStatusName() {
        return statusName;
    }

    /**
     * public void setStatusName(String statusName) sets the statusname for the
     * Status.
     * @param statusName
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    /**
     * public Status getStatusById(int id) throws ClassNotFoundException, 
     * SQLException gets the status by ID.
     * @param id The id of the status.
     * @return Status The current status object.
     * @throws ClassNotFoundException MySqlConnector.jar not found.
     * @throws SQLException Can't connect to the MySQL database.
     */
    public Status getStatusById(int id) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM luggagestatus WHERE id=" + id);
            while (result.next()) {
                this.id = result.getInt(1);
                this.statusName = result.getString(2);
            }
        }
        return this;
    }
    
    /**
     * public Status getStatusByName(String statusName) throws 
     * ClassNotFoundException, SQLException gets the status by name.
     * @param statusName The statusname of the Status.
     * @return Status The current status object.
     * @throws ClassNotFoundException MySqlConnector.jar not found.
     * @throws SQLException Can't connect to the MySQL database.
     */
    public Status getStatusByName(String statusName) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM luggagestatus WHERE statusname='" + statusName + "'");
            while (result.next()) {
                this.id = result.getInt(1);
                this.statusName = result.getString(2);
            }
        }
        return this;
    }
    
}
