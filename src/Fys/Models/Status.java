package Fys.Models;

import Fys.Tools.ConnectMysqlServer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class contains the attributes, constructor and methods of a Status
 * object, which is the status of a piece of luggage.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class Status {

    private int id;
    private String statusName;

    /**
     * Status() Status is the empty constructor for the Status object.
     */
    public Status() {
        this.statusName = "";
    }

    /**
     * int getId() gets the id of the Status.
     *
     * @return id The id of the Status.
     */
    public int getId() {
        return id;
    }

    /**
     * void setId(int id) sets the id of the Status.
     *
     * @param id The id of the Status.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * String getStatusName() gets the statusname of the Status.
     *
     * @return statusname The statusname of the Status.
     */
    public String getStatusName() {
        return statusName;
    }

    /**
     * void setStatusName(String statusName) sets the statusname for the Status.
     *
     * @param statusName The statusname of the new status.
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    /**
     * Status getStatusById(int id) gets a Status object based off it's id.
     *
     * @param id The id of the status that needs to be fetched from the
     * database.
     * @return Status The Status object that was fetched from the database.
     * @throws ClassNotFoundException when the jdbc can't be found.
     * @throws SQLException when an SQL exception occurs.
     */
    public Status getStatusById(int id) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM luggagestatus"
                    + " WHERE id=" + id);
            while (result.next()) {
                this.id = id;
                this.statusName = result.getString(2);
            }
        }
        return this;
    }

    /**
     * Status getStatusByName(String statusName) gets a Status object based
     * based off it's name.
     *
     * @param statusName The statusname of the Status.
     * @return Status The Status object that was found in the database.
     * @throws ClassNotFoundException when the jdbc could not be found.
     * @throws SQLException when an SQL exception occurs.
     */
    public Status getStatusByName(String statusName) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM luggagestatus"
                    + " WHERE statusname='" + statusName + "'");
            while (result.next()) {
                this.id = result.getInt(1);
                this.statusName = result.getString(2);
            }
        }
        return this;
    }

}
