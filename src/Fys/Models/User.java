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
 * This class contains the attributes, the constructor and the methods of the
 * User object. The User object is the user of the application the logs in to do
 * things inside the application.
 *
 * @author Daan Befort, IS106-2
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
     * Constructor User() is the empty constructor for the User object.
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
     * int getId() gets the id of the User object.
     *
     * @return id The id of the User
     */
    public int getId() {
        return id;
    }

    /**
     * void setId(int id) sets the id of the User object.
     *
     * @param id The new id of the User
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * String getUsername() gets the username attribute of the User.
     *
     * @return username The username of the User object.
     */
    public String getUsername() {
        return username;
    }

    /**
     * public void setUsername(String username) sets the username attribute of
     * the User.
     *
     * @param username The username of the User object.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * String getPassword() gets the password of the User. This is the encrypted
     * password from the database.
     *
     * @return password The password of the User.
     */
    public String getPassword() {
        return password;
    }

    /**
     * void setPassword(String password) sets the password of the user.
     *
     * @param password The password of the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * String getFirstname() gets the firstname attribute of the User.
     *
     * @return firstname The first name of the User.
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * void setFirstname(String firstname) sets the firstname of the User.
     *
     * @param firstname The first name of the User.
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * String getLastname() gets the lastnamme attribute of the User.
     *
     * @return lastname The last name of the User.
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * void setLastname(String lastname) sets the lastname of the User.
     *
     * @param lastname The last name of the User.
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * int getRoleId() gets the roleId of the User.
     *
     * @return roleId The id of the role assigned to the User.
     */
    public int getRoleId() {
        return roleId;
    }

    /**
     * void setRoleId(int roleId) sets the roleId of the User.
     *
     * @param roleId The id of the role assigned to the User.
     */
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    /**
     * String getRegisterDate() gets the registerDate of the User.
     *
     * @return registerDate The date when the User is registered
     */
    public String getRegisterDate() {
        return registerDate;
    }

    /**
     * void setRegisterDate(String registerDate) sets the registerdate of the
     * User.
     *
     * @param registerDate The date when the User is registered
     */
    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    /**
     * boolean isActive() returns the active value of the User.
     *
     * @return active gets the status if the User is enabled or disabled, if
     * they can log in or not.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * void setActive(boolean active) sets the active value of the User.
     *
     * @param active sets the status of the User to active or inactive, if they
     * can log in or not.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * void setRole(Role role) sets the role for the User.
     *
     * @param role The role of the user.
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * public Role getRole() returns the role of the user.
     *
     * @return role The role of the user.
     */
    public Role getRole() {
        return this.role;
    }

    /**
     * String fullName() returns the full name of the User.
     *
     * @return firstname, lastname The attributes for the Full Name of the User.
     */
    public String fullName() {
        return (this.firstname + " " + this.lastname);
    }

    /**
     * void insertUser(User user) inserts a new User object into the database.
     *
     * @param user The User object that needs to be inserted into the database.
     * @throws ClassNotFoundException when the jdbc could not be found.
     * @throws SQLException when an SQL exception occurs.
     */
    public void insertUser(User user) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            String query = ("INSERT INTO user (username,password,firstname,lastname,"
                    + "roleid,registerdate,active) VALUES (?,?,?,?,?,?,?)");
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
     * void updateUser(User user) updates the given User object in the database.
     *
     * @param user The User object that needs to be updated in the databases.
     * @throws ClassNotFoundException when the jdbc could not be found.
     * @throws SQLException when an SQL exception occurs.
     */
    public void updateUser(User user) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            String query = ("UPDATE user SET username = ?, password=?,firstname=?,lastname=?,"
                    + "roleId=?,registerDate=?,active=? WHERE id=" + user.getId());
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
     * User getUserById(int id) gets a User object from the database by it's id.
     *
     * @param id The id of the User that needs to be fetched from the database.
     * @return user The found User object.
     * @throws ClassNotFoundException when the jdbc could not be found.
     * @throws SQLException when an SQL excepion occurs.
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
     * User getUserByUsername(String username) gets a User object from the
     * database based of it's username.
     *
     * @param username The username of the User that needs to be fetched from
     * the database.
     * @return User The found User object.
     * @throws ClassNotFoundException when the jdbc could not be found.
     * @throws SQLException when an SQL excepion occurs.
     */
    public User getUserByUsername(String username) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM user WHERE username='"
                    + username + "'");
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
     * ObservableList<User> getEmployees() returns an ObservableList containing
     * all Users that have roleid 3. This is used for the Manager screen to get
     * the statistics per employee.
     *
     * @return an ObservableList of User objects that have roleid 3.
     * @throws ClassNotFoundException when the jdbc could not be found.
     * @throws SQLException when an SQL excepion occurs.
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
