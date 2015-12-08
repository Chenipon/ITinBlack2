package Fys.Models;

import Fys.Tools.ConnectMysqlServer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class Connection {
    
    int id;
    int luggageId;
    int customerId;
    String connectionDate;
    
    public Connection() {
        id = 0;
        luggageId = 0;
        customerId = 0;
        connectionDate = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLuggageId() {
        return luggageId;
    }

    public void setLuggageId(int luggageId) {
        this.luggageId = luggageId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getConnectionDate() {
        return connectionDate;
    }

    public void setConnectionDate(String connectionDate) {
        this.connectionDate = connectionDate;
    }
    
    public void insertConnection(Connection connection) throws ClassNotFoundException, SQLException {
        try (java.sql.Connection db = new ConnectMysqlServer().dbConnect()) {
            String query = ("INSERT INTO connection (luggageid,customerid,connectiondate) VALUES (?,?,?)");
            PreparedStatement preparedStatement = (PreparedStatement) db.prepareStatement(query);
            preparedStatement.setInt(1, connection.getLuggageId());
            preparedStatement.setInt(2, connection.getCustomerId());
            preparedStatement.setString(3, connection.getConnectionDate());
            preparedStatement.executeUpdate();
        }
    }
    
    public void updateConnection(Connection connection) throws ClassNotFoundException, SQLException {
        try (java.sql.Connection db = new ConnectMysqlServer().dbConnect()) {
            String query = ("UPDATE connection SET luggageid=?,customerid=?,connectiondate=? WHERE id=" + connection.getId());
            PreparedStatement preparedStatement = (PreparedStatement) db.prepareStatement(query);
            preparedStatement.setInt(1, connection.getLuggageId());
            preparedStatement.setInt(2, connection.getCustomerId());
            preparedStatement.setString(3, connection.getConnectionDate());
            preparedStatement.executeUpdate();
        }
    }
    
    public void deleteConnection(Connection connection) throws ClassNotFoundException, SQLException {
        try (java.sql.Connection db = new ConnectMysqlServer().dbConnect()) {
            String query = "DELETE FROM connection WHERE id= ?";
            PreparedStatement preparedStatement = (PreparedStatement) db.prepareStatement(query);
            preparedStatement.setInt(1, connection.getId());
            preparedStatement.execute();
        }
    }
    
    public Connection getConnectionByLuggageId(int luggageId) throws SQLException, ClassNotFoundException {
        try (java.sql.Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM connection WHERE luggageid=" + luggageId);
            while (result.next()) {
                this.id = result.getInt(1);
                this.luggageId = result.getInt(2);
                this.customerId = result.getInt(3);
                this.connectionDate = result.getString(4);
            }
        }
        return this;
    }
    
    public Connection getConnectionByCustomerId(int customerId) throws SQLException, ClassNotFoundException {
        try (java.sql.Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM connection WHERE customerid=" + customerId);
            while (result.next()) {
                this.id = result.getInt(1);
                this.luggageId = result.getInt(2);
                this.customerId = result.getInt(3);
                this.connectionDate = result.getString(4);
            }
        }
        return this;
    }
}
