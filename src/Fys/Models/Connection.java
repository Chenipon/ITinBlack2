package Fys.Models;

import Fys.Tools.ConnectMysqlServer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class contains the attributes, the constructor and the methods of the
 * Connection object. This object contains the information about a connection
 * between Luggage and Customer.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class Connection {

    int id;
    int luggageId;
    int customerId;

    /**
     * Constructor Connection() is the empty constructor of a Connection object.
     * It initializes the ids to 0.
     */
    public Connection() {
        id = 0;
        luggageId = 0;
        customerId = 0;
    }

    /**
     * public int getId() gets the id of a Connection object. This is also the
     * Primary Key in the database.
     *
     * @return the id of a Connection object.
     */
    public int getId() {
        return id;
    }

    /**
     * public void setId(int id) sets the id of a Connection object. This is
     * also the Primary Key in the database and should therefore be avoided.
     *
     * @param id the id of a Connection object.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * public int getLuggageId() gets the Luggage id of a Connection object.
     *
     * @return the luggage id of a Connection object.
     */
    public int getLuggageId() {
        return luggageId;
    }

    /**
     * public void setLuggageId(int luggageId) sets the Luggage id of a
     * Connection object.
     *
     * @param luggageId the id of a Connection object.
     */
    public void setLuggageId(int luggageId) {
        this.luggageId = luggageId;
    }

    /**
     * public int getCustomerId() gets the Customer id of a Connection object.
     *
     * @return the customer id of a Connection object.
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * public void setCustomerId(int customerId) sets the Customer id of a
     * Connection object.
     *
     * @param customerId the customer id of a Connection object.
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * public void insertConnection(Connection connection) inserts a new
     * Connection into the database.
     *
     * @param connection is the connection that needs to be inserted into the
     * database.
     * @throws ClassNotFoundException when the jdbc could not be found.
     * @throws SQLException when an SQL exception occured.
     */
    public void insertConnection(Connection connection)
            throws ClassNotFoundException, SQLException {
        try (java.sql.Connection db = new ConnectMysqlServer().dbConnect()) {
            String query = ("INSERT INTO connection (luggageid,customerid) VALUES (?,?)");
            PreparedStatement preparedStatement = (PreparedStatement) db.prepareStatement(query);
            preparedStatement.setInt(1, connection.getLuggageId());
            preparedStatement.setInt(2, connection.getCustomerId());
            preparedStatement.executeUpdate();
        }
    }

    /**
     * public void updateConnection(Connection connection) updates an already
     * existing connection in the database.
     *
     * @param connection is the connection that needs to be updated in the
     * database.
     * @throws ClassNotFoundException when the jdbc could not be found.
     * @throws SQLException when an SQL exception occured.
     */
    public void updateConnection(Connection connection)
            throws ClassNotFoundException, SQLException {
        try (java.sql.Connection db = new ConnectMysqlServer().dbConnect()) {
            String query = ("UPDATE connection SET luggageid=?,customerid=? WHERE id="
                    + connection.getId());
            PreparedStatement preparedStatement = (PreparedStatement) db.prepareStatement(query);
            preparedStatement.setInt(1, connection.getLuggageId());
            preparedStatement.setInt(2, connection.getCustomerId());
            preparedStatement.executeUpdate();
        }
    }

    /**
     * public void deleteConnection(Connection connection) deletes a connection
     * from the database.
     *
     * @param connection is the connection that needs to be deleted from the
     * database.
     * @throws ClassNotFoundException when the jdbc could not be found.
     * @throws SQLException when an SQL exception occured.
     */
    public void deleteConnection(Connection connection)
            throws ClassNotFoundException, SQLException {
        try (java.sql.Connection db = new ConnectMysqlServer().dbConnect()) {
            String query = "DELETE FROM connection WHERE id= ?";
            PreparedStatement preparedStatement = (PreparedStatement) db.prepareStatement(query);
            preparedStatement.setInt(1, connection.getId());
            preparedStatement.execute();
        }
    }

    /**
     * public Connection getConnectionByLuggageId(int luggageId) gets a
     * Connection object from the database containing a given luggage id.
     *
     * @param luggageId is the luggage id that the Connection contains.
     * @return a Connection object initialized from given luggageId.
     * @throws SQLException when an SQL exception occured.
     * @throws ClassNotFoundException when the jdbc could not be found.
     */
    public Connection getConnectionByLuggageId(int luggageId)
            throws SQLException, ClassNotFoundException {
        try (java.sql.Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM connection WHERE luggageid="
                    + luggageId);
            while (result.next()) {
                this.id = result.getInt(1);
                this.luggageId = result.getInt(2);
                this.customerId = result.getInt(3);
            }
        }
        return this;
    }

    /**
     * public Connection getConnectionByCustomerId(int customerId) gets a
     * Connection object from the database containing a given customer id.
     *
     * @param customerId is the customer id that the Connection contains.
     * @return a Connection object initialized from given customerId.
     * @throws SQLException when an SQL exception occured.
     * @throws ClassNotFoundException when the jdbc could not be found.
     */
    public Connection getConnectionByCustomerId(int customerId)
            throws SQLException, ClassNotFoundException {
        try (java.sql.Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM connection WHERE customerid="
                    + customerId);
            while (result.next()) {
                this.id = result.getInt(1);
                this.luggageId = result.getInt(2);
                this.customerId = result.getInt(3);
            }
        }
        return this;
    }
}
