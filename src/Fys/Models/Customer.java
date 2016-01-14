package Fys.Models;

import Fys.Tools.ConnectMysqlServer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
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
     *
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
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    /**
     *
     * @return
     */
    public String getGender() {
        return gender;
    }
    
    public String getFullname(){
        return this.firstName + " " + this.lastName;
    }
    
    /**
     *
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     *
     * @return
     */
    public String getPhone() {
        return phone;
    }

    /**
     *
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     *
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }
    
    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }
    
    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
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
     *
     * @return
     */
    public int getEmployeeId() {
        return employeeId;
    }

    /**
     *
     * @param employeeId
     */
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    /**
     *
     * @param customer
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void insertCustomer(Customer customer) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            String query = ("INSERT INTO customer (firstname,lastname,gender,phone,address,email,registerdate,employeeid) VALUES (?,?,?,?,?,?,?,?)");
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
     *
     * @param customer
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void updateCustomer(Customer customer) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            String query = "UPDATE customer SET firstname = ?,lastname = ?,gender = ?,phone = ?,address = ?,email = ? WHERE id=" + customer.getId();
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
     *
     * @param id
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
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
                this.setRegisterDate(result.getString(8)); //Fix the .0 issue by calling on the .setRegisterDate method here.
                this.employeeId = result.getInt(9);
            }
        }
        return this;
    }

}
