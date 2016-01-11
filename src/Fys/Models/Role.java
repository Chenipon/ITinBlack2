package Fys.Models;

import Fys.Tools.ConnectMysqlServer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Daan
 */
public class Role {

    private int id;
    private String roleName;

    /**
     *
     */
    public Role() {
        this.roleName = "";
    }

    /**
     *
     * @param roleName
     */
    public Role(String roleName) {
        this.roleName = roleName;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     *
     * @param roleName
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     *
     * @param role
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void insertRole(Role role) throws ClassNotFoundException, SQLException {
        Connection db = new ConnectMysqlServer().dbConnect();
        Statement statement = db.createStatement();
        statement.executeQuery("INSERT INTO role (rolename) VALUES (" + role.getRoleName() + ")");
        db.close();
    }

    /**
     *
     * @param id
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Role getRoleById(int id) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM role WHERE id=" + id);
            while (result.next()) {
                this.id = result.getInt(1);
                this.roleName = result.getString(2);
            }
        }
        return this;
    }
    
    /**
     *
     * @return
     * @throws Exception
     */
    public ObservableList<Role> getRoles() throws Exception {
        ObservableList<Role> roleList = FXCollections.observableArrayList();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM role");
            while (result.next()) {
                Role addRole = new Role();
                addRole.id = result.getInt(1);
                addRole.roleName = result.getString(2);
                roleList.add(addRole);
            }
        }
        return roleList;
    }
    
    /**
     *
     * @param name
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Role getRoleByName(String name) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM role WHERE rolename='" + name + "'");
            while (result.next()) {
                this.id = result.getInt(1);
                this.roleName = result.getString(2);
            }
        }
        return this;
    }
    
}
