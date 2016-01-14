package Fys.Views.ViewModels;

import Fys.Models.Role;
import Fys.Tools.ConnectMysqlServer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class is used to fill data into a TableView, which is displayed on the
 * AccountOverview screen.
 *
 * @author Daan Befort, IS106-2
 */
public class AccountTabelView {

    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private String roleName;
    private String active;

    /**
     * public int getId() gets the id of the User object.
     *
     * @return the id of the User object. This is the Primary Key in the
     * database.
     */
    public int getId() {
        return id;
    }

    /**
     * public void setId(int id) sets the id of the User object.
     *
     * @param id the id of the User object. This is the Primary Key in the
     * database.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * public String getUsername() gets the username of the User object.
     *
     * @return the username of the User object.
     */
    public String getUsername() {
        return username;
    }

    /**
     * public void setUsername(String username) sets the username of the User
     * object.
     *
     * @param username the username of the User object.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * public String getFirstname() gets the first name of the User object.
     *
     * @return the first name of the User object.
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * public void setFirstname(String firstname) sets the first name of the
     * User object.
     *
     * @param firstname the first name of the User object.
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * public String getLastname() gets the last name of the User object.
     *
     * @return the last name of the User object.
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * public void setLastname(String lastname) sets the last name of the User
     * object.
     *
     * @param lastname the last name of the User object.
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * public String getRoleName() gets the roleName of the User object.
     *
     * @return the roleName name of the User object.
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * public void setRoleName(String roleName) sets the roleName of the User
     * object.
     *
     * @param roleName the roleName name of the User object.
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * public String getActive() gets the state of the User object (disabled or
     * active).
     *
     * @return the activity of the User object.
     */
    public String getActive() {
        return active;
    }

    /**
     * public void setActive(String active) sets the state of the User object
     * (disabled or active).
     *
     * @param active the activity of the User object.
     */
    public void setActive(String active) {
        this.active = active;
    }

    /**
     * public ObservableList<AccountTabelView> getAccountList() gets the list of
     * accounts from the database.
     *
     * @return an ObservableList containing a list of all Users from the
     * database.
     * @throws SQLException when an SQL exception occurred.
     * @throws ClassNotFoundException when the jdbc could not be found.
     */
    public ObservableList<AccountTabelView> getAccountList()
            throws SQLException, ClassNotFoundException {
        ObservableList<AccountTabelView> users = FXCollections.observableArrayList();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM user");
            while (result.next()) {
                AccountTabelView foundUsers = new AccountTabelView();
                Role role = new Role().getRoleById(result.getInt(6));
                foundUsers.setId(result.getInt(1));
                foundUsers.setUsername(result.getString(2));
                foundUsers.setFirstname(result.getString(4));
                foundUsers.setLastname(result.getString(5));
                foundUsers.setRoleName(role.getRoleName());
                if (result.getBoolean(8)) {
                    foundUsers.setActive("Active");
                } else {
                    foundUsers.setActive("Inactive");
                }
                users.add(foundUsers);
            }
        }
        return users;
    }

    /**
     * public ObservableList<AccountTabelView> getAccountList(String searchTerm)
     * gets the list of accounts from the database based of a search term.
     *
     * @param searchTerm the search term.
     * @return an ObservableList containing a list of all Users from the
     * database.
     * @throws SQLException when an SQL exception occurred.
     * @throws ClassNotFoundException when the jdbc could not be found.
     */
    public ObservableList<AccountTabelView> getAccountList(String searchTerm)
            throws SQLException, ClassNotFoundException {
        ObservableList<AccountTabelView> users = FXCollections.observableArrayList();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result;
            result = statement.executeQuery("SELECT * FROM user WHERE username LIKE "
                    + "'%" + searchTerm + "%' OR firstname LIKE '%"
                    + searchTerm + "%' OR lastname LIKE '%"
                    + searchTerm + "%'");
            while (result.next()) {
                AccountTabelView foundUsers = new AccountTabelView();
                Role role = new Role().getRoleById(result.getInt(6));
                foundUsers.setId(result.getInt(1));
                foundUsers.setUsername(result.getString(2));
                foundUsers.setFirstname(result.getString(4));
                foundUsers.setLastname(result.getString(5));
                foundUsers.setRoleName(role.getRoleName());
                if (result.getBoolean(8)) {
                    foundUsers.setActive("Active");
                } else {
                    foundUsers.setActive("Inactive");
                }
                users.add(foundUsers);
            }
        }
        return users;
    }
}
