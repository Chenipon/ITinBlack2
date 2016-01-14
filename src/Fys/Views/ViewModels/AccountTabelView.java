package Fys.Views.ViewModels;

import Fys.Models.Role;
import Fys.Tools.ConnectMysqlServer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Daan
 * @author Javadoc: John Ghatas, IS106-2
 */
public class AccountTabelView {

    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private String role;
    private String active;
    
    /**
     * This method grabs the id of the user, used to create the table.
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * This method sets the id of the user, used to create the table.
     * @see setId()
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * This method gets the username of the user, used to create the table.
     * @see getUsername()
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method sets the username of the user, used to create the table.
     * @see setUsername()
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * This method gets the first name of the user, used to create the table.
     * @see getFirstname()
     * @return
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * This method sets the first name of the user, used to create the table.
     * @see setFirstname()
     * @param firstname
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * This method gets the last name of the user, used to create the table.
     * @see getLastname()
     * @return
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * This method sets the last name of the user, used to create the table.
     * @see setLastname()
     * @param lastname
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * This method gets the role of the user, used to create the table.
     * @see getRole()
     * @return
     */
    public String getRole() {
        return role;
    }

    /**
     * This method sets the role of the user, used to create the table.
     * @see setRole()
     * @param role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * This method gets the state of the user's account (disabled or active), 
     * used to create the table.
     * @see getActive()
     * @return
     */
    public String getActive() {
        return active;
    }

    /**
     * This method sets the state of the user's account (disabled or active), 
     * used to create the table.
     * @see setActive()
     * @param active
     */
    public void setActive(String active) {
        this.active = active;
    }

    /**
     * This method gets the list of accounts, and the details
     * After that has been done, the method generates a table containing those details.
     * @see getAccountList()
     * @return
     * @throws Exception
     */
    public ObservableList<AccountTabelView> getAccountList() throws Exception {
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
                foundUsers.setRole(role.getRoleName());
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
     * This method gets the list of accounts, and the details
     * After that has been done, the method generates a table containing those details, 
     * this method reacts to the user's search terms.
     * @see getAccountList()
     * @param searchTerm
     * @return
     * @throws Exception
     */
    public ObservableList<AccountTabelView> getAccountList(String searchTerm) throws Exception {
        ObservableList<AccountTabelView> users = FXCollections.observableArrayList();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result;
            result = statement.executeQuery("SELECT * FROM user WHERE username LIKE "
                    + "'%" + searchTerm + "%' OR firstname LIKE '%" + searchTerm + "%' OR lastname LIKE '%" + searchTerm + "%'");
            while (result.next()) {
                AccountTabelView foundUsers = new AccountTabelView();
                Role role = new Role().getRoleById(result.getInt(6));
                foundUsers.setId(result.getInt(1));
                foundUsers.setUsername(result.getString(2));
                foundUsers.setFirstname(result.getString(4));
                foundUsers.setLastname(result.getString(5));
                foundUsers.setRole(role.getRoleName());
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
