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

    public Status() {
        this.statusName = "";
    }

    public Status(String statusName) {
        this.statusName = statusName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

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
