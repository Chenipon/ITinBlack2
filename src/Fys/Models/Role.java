package Fys.Models;

import Fys.Tools.ConnectMysqlServer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Daan
 */
public class Role {

    private int id;
    private String roleName;

    public Role() {
        this.roleName = "";
    }

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void insertRole(Role role) throws ClassNotFoundException, SQLException {
        Connection db = new ConnectMysqlServer().dbConnect();
        Statement statement = db.createStatement();
        statement.executeQuery("INSERT INTO roles (rolename) VALUES (" + role.getRoleName() + ")");
        db.close();
    }

    public Role getRoleById(int id) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM roles WHERE id=" + id);
            while (result.next()) {
                this.id = result.getInt(1);
                this.roleName = result.getString(2);
            }
        }
        return this;
    }
    
    public Role getRoleByName(String name) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM roles WHERE rolename='" + name + "'");
            while (result.next()) {
                this.id = result.getInt(1);
                this.roleName = result.getString(2);
            }
        }
        return this;
    }
    
}
