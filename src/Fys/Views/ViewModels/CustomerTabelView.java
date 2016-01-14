package Fys.Views.ViewModels;

import Fys.Tools.ConnectMysqlServer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class is used to fill data into a TableView, 
 * which is displayed on the CustomerOverview screen.
 * 
 * @author Jeffrey van der Lingen, Daan Befort IS106-2
 */


public class CustomerTabelView {

    private int id;
    private String firstname;
    private String lastname;
    private String gender;
    private String phone;
    private String address;
    private String email;

    /**
     * This method grabs the id of the Customer
     * @return the id of user
     */
    public int getId() {
        return id;
    }

    /**
     * This method sets the id of the Customer
     * @param id the id of the customer
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * This method gets the first name of the Customer
     * @return the firstname of customer
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * This method sets the first name of the Customer
     * @param firstname the firstname of customer
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * This method gets the last name of the Customer
     * @return the last name of customer
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * This method sets the last name of the Customer
     * @param lastname the lastname of customer
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * This method gets the gender of the Customer
     * @return the gender of customer
     */
    public String getGender() {
        return gender;
    }

    /**
     * This method sets the gender of the Customer
     * @param gender the gender of customer
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * This method gets the phone number of the Customer
     * @return the pohne number of customer
     */
    public String getPhone() {
        return phone;
    }

    /**
     * This method sets the phone number of the Customer
     * @param phone the phone number of customer
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * This method gets the address of the Customer
     * @return the address of customer
     */
    public String getAddress() {
        return address;
    }

    /**
     * This method sets the address of the Customer
     * @param address the address of customer
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * This method gets the E-Mail of the Customer
     * @return the email of customer
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method sets the E-Mail of the Customer
     * @param email the email of customer
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * This method gets the list of Customers
     * 
     * @return ObservableList with customer data
     * @throws SQLException when there is an SQL error.
     * @throws ClassNotFoundException when the jdbc can't be found.
     */
    public ObservableList<CustomerTabelView> getCustomerList() 
            throws SQLException, ClassNotFoundException {
        ObservableList<CustomerTabelView> customers = FXCollections.observableArrayList();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM customer");
            while (result.next()) {
                CustomerTabelView foundCustomers = new CustomerTabelView();
                foundCustomers.setId(result.getInt(1));
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

    /**
     * This method gets the list of Customers that is valid by searchTerm
     * 
     * @param searchTerm the searchTerm the user search by
     * @return a list that is valid with the searchTerm
     * @throws SQLException when there is an SQL error.
     * @throws ClassNotFoundException when the jdbc can't be found.
     */
    public ObservableList<CustomerTabelView> getCustomerList(String searchTerm) 
            throws SQLException, ClassNotFoundException {
        ObservableList<CustomerTabelView> customers = FXCollections.observableArrayList();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result;
            result = statement.executeQuery("SELECT * FROM customer WHERE firstname LIKE '%"
                    + searchTerm + "%' OR lastname LIKE '%" + searchTerm + "%' OR address LIKE '%"
                    + searchTerm + "%' OR email LIKE '%" + searchTerm + "%'");
            while (result.next()) {
                CustomerTabelView foundCustomers = new CustomerTabelView();
                foundCustomers.setId(result.getInt(1));
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
