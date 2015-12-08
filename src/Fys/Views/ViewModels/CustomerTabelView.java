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
 * @author Javadoc: John Ghatas, IS106-2
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
     * This method grabs the id of the Customer, used to create the table.
     * @see getId()
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * This method sets the id of the Customer, used to create the table.
     * @see setId().
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * This method gets the first name of the Customer, used to create the table.
     * @see getFirstName().
     * @return
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * This method sets the first name of the Customer, used to create the table.
     * @see setFirstName().
     * @param firstname
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * This method gets the last name of the Customer, used to create the table.
     * @see getLastName()
     * @return
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * This method sets the last name of the Customer, used to create the table.
     * @see setLastName()
     * @param lastname
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * This method gets the gender of the Customer, used to create the table.
     * @see getGender()
     * @return
     */
    public String getGender() {
        return gender;
    }

    /**
     * This method sets the gender of the Customer, used to create the table.
     * @see setGender()
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * This method gets the phone number of the Customer, used to create the table.
     * @see getPhone()
     * @return
     */
    public String getPhone() {
        return phone;
    }

    /**
     * This method sets the phone number of the Customer, used to create the table.
     * @see setPhone()
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * This method gets the address of the Customer, used to create the table.
     * @see getAddress()
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     * This method sets the address of the Customer, used to create the table.
     * @see setAddress()
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * This method gets the E-Mail of the Customer, used to create the table.
     * @see getEmail()
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method sets the E-Mail of the Customer, used to create the table.
     * @see setEmail()
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * This method gets the list of Customers, and the details
     * ,after that has been done, the method generates a table containing those details.
     * @see getCustomerList()
     * @return
     * @throws Exception
     */
    public ObservableList<CustomerTabelView> getCustomerList() throws Exception {
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
     * This method gets the list of accounts, and the details
     * After that has been done, the method generates a table containing those details, this method reacts to the user's search terms.
     * @see getCustomerList()
     * @param searchTerm
     * @return
     * @throws Exception
     */
    public ObservableList<CustomerTabelView> getCustomerList(String searchTerm) throws Exception {
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
