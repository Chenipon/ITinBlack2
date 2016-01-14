package Fys.Models;

import Fys.Tools.ConnectMysqlServer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class contains the attributes, the constructor and the methods of the
 * Customer object. This is a Customer in the application that can be connected
 * to a Luggage object.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class Customer {

    private int id;
    private String firstName;
    private String lastName;
    private String gender;
    private String phone;
    private String address;
    private String email;
    private String registerDate;
    private int employeeId;

    /**
     * Constructor Customer() is the empty constructor of a Customer object. It
     * initializes empty strings into all the attributes.
     */
    public Customer() {
        this.firstName = "";
        this.lastName = "";
        this.gender = "";
        this.phone = "";
        this.address = "";
        this.email = "";
        this.registerDate = "";
        this.employeeId = 0;
    }

    /**
     * public int getId() gets the id of a Customer object. This is also the
     * Primary Key in the database.
     *
     * @return the id of the Customer.
     */
    public int getId() {
        return id;
    }

    /**
     * public int setId(int id) sets the id of a Customer object. This is also
     * the Primary Key in the database and therefore should not be changed in
     * any normal circumstances.
     *
     * @param id the id of the Customer.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * public String getFirstName() gets the first name of a Customer object.
     *
     * @return the first name of a Customer object.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * public void setFirstName(String firstName) sets the first name of a
     * Customer object.
     *
     * @param firstName the first name of a Customer object.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * public String getLastName() gets the last name of a Customer object.
     *
     * @return the last name of a Customer object.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * public void setLastName(String lastName) sets the last name of a Customer
     * object.
     *
     * @param lastName the last name of a Customer object.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * public String getFullName() gets the full name of a Customer object made
     * by combining the first name and the last name of a Customer object.
     *
     * @return the full name of a Customer object.
     */
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    /**
     * public String getGender() gets the gender of a Customer object.
     *
     * @return the gender of a Customer object.
     */
    public String getGender() {
        return gender;
    }

    /**
     * public void setGender(String gender) sets the gender of a Customer
     * object.
     *
     * @param gender the gender of a Customer object.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * public String getPhone() gets the phone number of a Customer object.
     *
     * @return the phone number of a Customer object.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * public void setPhone(String phone) sets the phone number of a Customer
     * object.
     *
     * @param phone the phone number of a Customer object.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * public void getAddress() gets the address of a Customer object.
     *
     * @return the address of a Customer object.
     */
    public String getAddress() {
        return address;
    }

    /**
     * public void setAddress(String address) sets the address of a Customer
     * object.
     *
     * @param address the address of a Customer object.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * public String getEmail() gets the email of a Customer object.
     *
     * @return the email of a Customer object.
     */
    public String getEmail() {
        return email;
    }

    /**
     * public void setEmail(String email) sets the email of a Customer object.
     *
     * @param email the email of a Customer object.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * public String getRegisterDate() gets the register date of a Customer
     * object.
     *
     * @return the register date of a Customer object.
     */
    public String getRegisterDate() {
        return registerDate;
    }

    /**
     * public void setRegisterDate(String registerDate) sets the register date
     * of a Customer object.
     *
     * @param registerDate the register date of a Customer object.
     */
    public void setRegisterDate(String registerDate) {
        /* 
         * For a strange reason, MySQL adds ".0" after the date. 
         * This is being manually removed here by using a String splitter 
         */
        if (registerDate.contains(".")) {
            String[] split = registerDate.split("\\.");
            registerDate = split[0];
        }
        this.registerDate = registerDate;
    }

    /**
     * public int getEmployeeId() gets the employee id of a Customer object.
     *
     * @return the employee id of a Customer object.
     */
    public int getEmployeeId() {
        return employeeId;
    }

    /**
     * public void setEmployeeId(int employeeId) sets the employee id of a
     * Customer object.
     *
     * @param employeeId the employee id of a Customer object.
     */
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * public void insertCustomer(Customer customer) inserts a new Customer into
     * the database.
     *
     * @param customer is the customer that needs to be inserted into the
     * database.
     * @throws ClassNotFoundException when the jdbc could not be found.
     * @throws SQLException when an SQL exception occurred.
     */
    public void insertCustomer(Customer customer) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            String query = ("INSERT INTO customer (firstname,lastname,gender,phone,"
                    + "address,email,registerdate,employeeid) VALUES (?,?,?,?,?,?,?,?)");
            PreparedStatement preparedStatement = (PreparedStatement) db.prepareStatement(query);
            preparedStatement.setString(1, customer.firstName);
            preparedStatement.setString(2, customer.lastName);
            preparedStatement.setString(3, customer.gender);
            preparedStatement.setString(4, customer.phone);
            preparedStatement.setString(5, customer.address);
            preparedStatement.setString(6, customer.email);
            preparedStatement.setString(7, customer.registerDate);
            preparedStatement.setInt(8, customer.employeeId);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * public void updateCustomer(Customer customer) updates a customer in the
     * database.
     *
     * @param customer is the customer that needs to be updated in the database.
     * @throws ClassNotFoundException when the jdbc could not be found.
     * @throws SQLException when an SQL exception occurred.
     */
    public void updateCustomer(Customer customer) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            String query = "UPDATE customer SET firstname = ?,lastname = ?,gender = ?,phone = ?,"
                    + "address = ?,email = ? WHERE id=" + customer.getId();
            PreparedStatement preparedStatement = (PreparedStatement) db.prepareStatement(query);
            preparedStatement.setString(1, customer.firstName);
            preparedStatement.setString(2, customer.lastName);
            preparedStatement.setString(3, customer.gender);
            preparedStatement.setString(4, customer.phone);
            preparedStatement.setString(5, customer.address);
            preparedStatement.setString(6, customer.email);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * public Customer getCustomerById(int id) gets a Customer object from the
     * database based off it's id.
     *
     * @param id is the id of the Customer that needs to be fetched.
     * @return the found Customer in the database.
     * @throws ClassNotFoundException when the jdbc could not be found.
     * @throws SQLException when an SQL exception occurred.
     */
    public Customer getCustomerById(int id) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM customer WHERE id=" + id);
            while (result.next()) {
                this.id = result.getInt(1);
                this.firstName = result.getString(2);
                this.lastName = result.getString(3);
                this.gender = result.getString(4);
                this.phone = result.getString(5);
                this.address = result.getString(6);
                this.email = result.getString(7);
                this.setRegisterDate(result.getString(8)); //Fix the .0 issue here.
                this.employeeId = result.getInt(9);
            }
        }
        return this;
    }

}
