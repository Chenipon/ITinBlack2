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
 *
 * @author Daan Befort, IS106-2
 */
public class Role {

    private int id;
    private String roleName;

    /**
     * Constructor Role() is the empty constructor for the Role class.
     */
    public Role() {
        this.roleName = "";
    }

    /**
     * Constructor Role(String roleName) is the main constructor for the Role
     * class.
     *
     * @param roleName The roleName for the Role class.
     */
    public Role(String roleName) {
        this.roleName = roleName;
    }

    /**
     * int getId() gets the id of the Role.
     *
     * @return id The id of the Role class.
     */
    public int getId() {
        return id;
    }

    /**
     * void setId(int id) sets the Id of the Role.
     *
     * @param id The id of the Role class.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * String getRoleName() gets the rolename of the Role.
     *
     * @return roleName The rolename of the Role.
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * void setRoleName(String roleName) sets the rolename of the Role.
     *
     * @param roleName The rolename of the Role.
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * Role getRoleById(int id) gets a role from a given roleid from the
     * database.
     *
     * @param id The id of the a role.
     * @return the role created using the roleid.
     * @throws ClassNotFoundException when the jdbc could not be found.
     * @throws SQLException is thrown when no connection to the database could
     * be established.
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
     * public ObservableList<Role> getRoles() gets all the roles from the
     * database and stores them in an ObservableList.
     *
     * @return an ObservableList containing all roles.
     * @throws ClassNotFoundException when the jdbc could not be found.
     * @throws SQLException when there is an error in the SQL syntax.
     */
    public ObservableList<Role> getRoles() throws ClassNotFoundException, SQLException {
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
     * public Role getRoleByName(String name) SQLException gets the role by
     * their name.
     *
     * @param name The name of the role.
     * @return The Role of who the name belongs to.
     * @throws ClassNotFoundException When the jdbc could not be found.
     * @throws SQLException when there is an error in the SQL syntax.
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
