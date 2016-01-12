package Fys.Models;

import Fys.Tools.ConnectMysqlServer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The Role class defines the role and gets users by the Role attributes.
 * @author Daan
 */
public class Role {

    private int id;
    private String roleName;

    /**
     * public Role() is the constructor for the Role class.
     */
    public Role() {
        this.roleName = "";
    }

    /**
     * public Role(String roleName) is the alternative constructor for the 
     * Role class.
     * @param roleName The roleName for the Role class.
     */
    public Role(String roleName) {
        this.roleName = roleName;
    }

    /**
     * public int getId() returns the id of the Role.
     * @return id The id of the Role class.
     */
    public int getId() {
        return id;
    }

    /**
     * public void setId(int id) sets the Id of the Role.
     * @param id The id of the Role class.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * public String getRoleName() gets the rolename of the Role.
     * @return roleName The rolename of the Role.
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * public void setRoleName(String roleName) sets the rolename of the Role.
     * @param roleName The rolename of the Role.
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * public void insertRole(Role role) throws ClassNotFoundException, 
     * SQLException inserts the role into the database.
     * @param role The Role instance.
     * @throws ClassNotFoundException MySqlConnector.jar not found.
     * @throws SQLException Can't connect to the MySQL database.
     */
    public void insertRole(Role role) throws ClassNotFoundException, SQLException {
        Connection db = new ConnectMysqlServer().dbConnect();
        Statement statement = db.createStatement();
        statement.executeQuery("INSERT INTO role (rolename) VALUES (" + role.getRoleName() + ")");
        db.close();
    }

    /**
     * public Role getRoleById(int id) throws ClassNotFoundException, 
     * SQLException
     * @param id The id of the Role class.
     * @return Role Current role instance.
     * @throws ClassNotFoundException MySqlConnector.jar not found.
     * @throws SQLException Can't connect to the MySQL database.
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
     * public ObservableList<Role> getRoles() throws Exception stores all roles 
     * in an FXCollection Arraylist.
     * @return ObservableList<Role>
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
     * public Role getRoleByName(String name) throws ClassNotFoundException, 
     * SQLException gets the role by ID.
     * @param name The name of the role.
     * @return Role Current Role class.
     * @throws ClassNotFoundException MySqlConnector.jar not found.
     * @throws SQLException Can't connect to the MySQL database.
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
