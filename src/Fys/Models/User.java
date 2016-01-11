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
 *
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
     *
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
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     *
     * @param firstname
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     *
     * @return
     */
    public String getLastname() {
        return lastname;
    }

    /**
     *
     * @param lastname
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     *
     * @return
     */
    public int getRoleId() {
        return roleId;
    }

    /**
     *
     * @param roleId
     */
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    /**
     *
     * @return
     */
    public String getRegisterDate() {
        return registerDate;
    }

    /**
     *
     * @param registerDate
     */
    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    /**
     *
     * @return
     */
    public boolean isActive() {
        return active;
    }

    /**
     *
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     *
     * @param role
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     *
     * @return
     */
    public Role getRole() {
        return this.role;
    }
    
    /**
     *
     * @return
     */
    public String fullName() {
        return this.firstname + " " + this.lastname;
    }

    /**
     *
     * @param user
     * @throws ClassNotFoundException
     * @throws SQLException
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
     *
     * @param user
     * @throws ClassNotFoundException
     * @throws SQLException
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
     *
     * @param id
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
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
     *
     * @param username
     * @return
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
     *
     * @return
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
