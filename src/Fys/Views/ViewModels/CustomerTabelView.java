package Fys.Views.ViewModels;

import Fys.Tools.ConnectMysqlServer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class CustomerTabelView {

    private String firstname;
    private String lastname;
    private String gender;
    private String phone;
    private String address;
    private String email;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ObservableList<CustomerTabelView> getCustomerList() throws Exception {
        ObservableList<CustomerTabelView> customers = FXCollections.observableArrayList();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM customers");
            while (result.next()) {
                CustomerTabelView foundCustomers = new CustomerTabelView();
                foundCustomers.setFirstname(result.getString(2));
                foundCustomers.setLastname(result.getString(3));
                foundCustomers.setGender(result.getString(4));
                foundCustomers.setPhone(result.getString(5));
                foundCustomers.setAddress(result.getString(6));
                foundCustomers.setEmail(result.getString(7));
                customers.add(foundCustomers);
            }
        }
        return customers;
    }

    public ObservableList<CustomerTabelView> getCustomerList(String searchTerm) throws Exception {
        ObservableList<CustomerTabelView> customers = FXCollections.observableArrayList();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result;
            result = statement.executeQuery("SELECT * FROM customers WHERE firstname LIKE '%"
                    + searchTerm + "%' OR lastname LIKE '%" + searchTerm + "%' OR address LIKE '%"
                    + searchTerm + "%' OR email LIKE '%" + searchTerm + "%'");
            while (result.next()) {
                CustomerTabelView foundCustomers = new CustomerTabelView();
                foundCustomers.setFirstname(result.getString(2));
                foundCustomers.setLastname(result.getString(3));
                foundCustomers.setGender(result.getString(4));
                foundCustomers.setPhone(result.getString(5));
                foundCustomers.setAddress(result.getString(6));
                foundCustomers.setEmail(result.getString(7));
                customers.add(foundCustomers);
            }
            return customers;
        }
    }
}
