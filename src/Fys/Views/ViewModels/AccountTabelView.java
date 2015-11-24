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
 */
public class AccountTabelView {

    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private String role;
    private String active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public ObservableList<AccountTabelView> getAccountList() throws Exception {
        ObservableList<AccountTabelView> users = FXCollections.observableArrayList();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM users");
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

    public ObservableList<AccountTabelView> getAccountList(String searchTerm) throws Exception {
        ObservableList<AccountTabelView> users = FXCollections.observableArrayList();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result;
            result = statement.executeQuery("SELECT * FROM users WHERE username LIKE '%" + searchTerm + "%' OR firstname LIKE '%" + searchTerm + "%' OR lastname LIKE '%" + searchTerm + "%'");
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
