package Fys.Models;

import Fys.Tools.ConnectMysqlServer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * User is the model for the user, having all attributes for the database model.
 * @author Daan
 */
public class User {

    private int id;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private int roleId;
    private String registerDate;
    private boolean active;

    private Role role;

    /**
     * public User() is the constructor for the User object.
     */
    public User() {
        this.username = "";
        this.password = "";
        this.firstname = "";
        this.lastname = "";
        this.roleId = 0;
        this.registerDate = null;
        this.active = true;
        this.role = new Role();
    }

    /**
     * public int getId() returns the id of the User object.
     * @return id The id of the User
     */
    public int getId() {
        return id;
    }

    /**
     * public void setId(int id) sets the id of the User object.
     * @param id The id of the User
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * public String getUsername() returns the username attribute of the User.
     * @return username The username of the User object.
     */
    public String getUsername() {
        return username;
    }

    /**
     * public void setUsername(String username) sets the username attribute of the User.
     * @param username The username of the User object.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * public String getPassword() returns the password of the User.
     * @return password The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * public void setPassword(String password) sets the password of the user.
     * @param password The password of the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * public String getFirstname() returns the firstname attribute of the User.
     * @return firstname The first name of the user.
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * public void setFirstname(String firstname) sets the firstname of the User.
     * @param firstname The first name of the user.
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * public String getLastname() returns the lastnamme attribute of the User.
     * @return lastname The last name of the user.
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * public void setLastname(String lastname) sets the lastname of the User.
     * @param lastname The last name of the user.
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * public int getRoleId() returns the roleId of the User.
     * @return roleId The id of the role assigned to the User.
     */
    public int getRoleId() {
        return roleId;
    }

    /**
     * public void setRoleId(int roleId) sets the roleId of the User.
     * @param roleId The id of the role assigned to the User.
     */
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    /**
     * public String getRegisterDate() returns the registerDate of the User.
     * @return registerDate The date when the user is registered
     */
    public String getRegisterDate() {
        return registerDate;
    }

    /**
     * public void setRegisterDate(String registerDate) sets the registerdate
     * of the User.
     * @param registerDate The date when the user is registered
     */
    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    /**
     * public boolean isActive() returns the active value of the User.
     * @return active Determines if the user is active or disabled.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * public void setActive(boolean active) sets the active value of 
     * the User.
     * @param active Determines if the user is active or disabled.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * public void setRole(Role role) sets the role for the User.
     * @param role The role of the user.
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * public Role getRole() returns the role of the user.
     * @return role The role of the user.
     */
    public Role getRole() {
        return this.role;
    }
    
    /**
     * public String fullName() returns the full name of the user
     * @return firstname, lastname The attributes for the Full Name of the User.
     */
    public String fullName() {
        return this.firstname + " " + this.lastname;
    }

    /**
     * public void insertUser(User user) throws ClassNotFoundException, 
     * SQLException inserts the User object to the database.
     * @param user The User object.
     * @throws ClassNotFoundException MySqlConnector.jar not found.
     * @throws SQLException Can't connect to the MySQL database.
     */
    public void insertUser(User user) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            String query = ("INSERT INTO user (username,password,firstname,lastname,roleid,registerdate,active) VALUES (?,?,?,?,?,?,?)");
            PreparedStatement preparedStatement = (PreparedStatement) db.prepareStatement(query);
            preparedStatement.setString(1, user.username);
            preparedStatement.setString(2, user.password);
            preparedStatement.setString(3, user.firstname);
            preparedStatement.setString(4, user.lastname);
            preparedStatement.setInt(5, user.roleId);
            preparedStatement.setString(6, user.registerDate);
            preparedStatement.setBoolean(7, user.active);
            preparedStatement.executeUpdate();
        }
    }
    
    /**
     * public void updateUser(User user) throws ClassNotFoundException, 
     * SQLException updates the User inserted in the method.
     * @param user Current User object.
     * @throws ClassNotFoundException MySqlConnector.jar not found.
     * @throws SQLException Can't connect to the MySQL database.
     */
    public void updateUser(User user) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            String query = ("UPDATE user SET username = ?, password=?,firstname=?,lastname=?,roleId=?,registerDate=?,active=? WHERE id=" + user.getId());
            PreparedStatement preparedStatement = (PreparedStatement) db.prepareStatement(query);
            preparedStatement.setString(1, user.username);
            preparedStatement.setString(2, user.password);
            preparedStatement.setString(3, user.firstname);
            preparedStatement.setString(4, user.lastname);
            preparedStatement.setInt(5, user.roleId);
            preparedStatement.setString(6, user.registerDate);
            preparedStatement.setBoolean(7, user.active);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * public User getUserById(int id) throws ClassNotFoundException, 
     * SQLException gets the user by the id typed in the method.
     * @param id The id of the user.
     * @return user The current user object.
     * @throws ClassNotFoundException MySqlConnector.jar not found.
     * @throws SQLException Can't connect to the MySQL database.
     */
    public User getUserById(int id) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM user WHERE id=" + id);
            while (result.next()) {
                this.id = result.getInt(1);
                this.username = result.getString(2);
                this.password = result.getString(3);
                this.firstname = result.getString(4);
                this.lastname = result.getString(5);
                this.roleId = result.getInt(6);
                this.registerDate = result.getString(7);
                this.active = result.getBoolean(8);
                this.role = new Role().getRoleById(this.roleId);
            }
        }
        return this;
    }

    /**
     * public User getUserByUsername(String username) throws ClassNotFoundException, 
     * SQLException searchs on the username the user provided.
     * @param username The username of the user.
     * @return User The current User object.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public User getUserByUsername(String username) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM user WHERE username='" + username + "'");
            while (result.next()) {
                this.id = result.getInt(1);
                this.username = result.getString(2);
                this.password = result.getString(3);
                this.firstname = result.getString(4);
                this.lastname = result.getString(5);
                this.roleId = result.getInt(6);
                this.registerDate = result.getString(7);
                this.active = result.getBoolean(8);
                this.role = new Role().getRoleById(this.roleId);
            }
        }
        return this;
    }

    /**
     * public ObservableList<User> getEmployees() throws ClassNotFoundException, 
     * SQLException returns the employees in the form of an ObservableList.
     * @return ObservableList<User> userlist The FXCollection containing all employees.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public ObservableList<User> getEmployees() throws ClassNotFoundException, SQLException {
        ObservableList<User> userList = FXCollections.observableArrayList();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM user WHERE roleid=3");
            while (result.next()) {
                User addUser = new User();
                addUser.setId(result.getInt(1));
                addUser.setUsername(result.getString(2));
                addUser.setPassword(result.getString(3));
                addUser.setFirstname(result.getString(4));
                addUser.setLastname(result.getString(5));
                addUser.setRoleId(result.getInt(6));
                addUser.setRegisterDate(result.getString(7));
                addUser.setActive(result.getBoolean(8));
                addUser.setRole(new Role().getRoleById(this.roleId));
                userList.add(addUser);
            }
        }
        return userList;
    }

}
