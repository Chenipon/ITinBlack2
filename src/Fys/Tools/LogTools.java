package Fys.Tools;

import Fys.Models.Customer;
import Fys.Models.Luggage;
import Fys.Models.Status;
import Fys.Models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is a tool to assist with the logging of certain activities of certain
 * types such as change information about a Customer or Luggage object. All data
 * is being stored in the Database that contains the logs of these types.
 * @author Jeffrey van der Lingen, IS106-2
 */
public class LogTools {

    /**
     * The method getCustomerRegisterDate(int id) grabs the Customers register date.
     * @param id the id of a registered Customer.
     * @return java.util.Date format of the register date of the Customer;
     * @throws SQLException when the Database gives errors
     * @throws ParseException when the date can't be parsed correctly
     * @throws ClassNotFoundException when the Database class can't be found
     */
    public java.util.Date getCustomerRegisterDate(int id) throws SQLException, ParseException, ClassNotFoundException {
        String registerdate = "";
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT registerdate FROM customer WHERE id=" + id);
            while (result.next()) {
                registerdate = result.getString(1);
            }
        }
        return (new DateConverter().convertSqlDateToJavaDate(registerdate));
    }
    
    /**
     * The method getCustomerRegisterEmployee(int id) grabs the username of the
     * Employee who registered the Customer.
     * @param id the id of the Employee that is provided in the Customer Object.
     * @return the username of the Employee who registered the Customer.
     */
    public String getCustomerRegisterEmployee(int id) {
        String username = "";
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT username FROM user WHERE id=" + id);
            while (result.next()) {
                username = result.getString(1);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LogTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        return username;
    }
    
    /**
     * This method grabs the date when the luggage is registered.
     * @see getLuggageRegisterDate()
     * @param id
     * @return
     * @throws SQLException
     * @throws ParseException
     * @throws ClassNotFoundException
     */
    public java.util.Date getLuggageRegisterDate(int id) throws SQLException, ParseException, ClassNotFoundException {
        String registerdate = "";
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT registerdate FROM luggage WHERE id=" + id);
            while (result.next()) {
                registerdate = result.getString(1);
            }
        }
        return (new DateConverter().convertSqlDateToJavaDate(registerdate));
    }

    /**
     * This class grabs the luggage registerd per employee.
     * @see getLuggageRegisterEmployee()
     * @param id
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public String getLuggageRegisterEmployee(int id) throws SQLException, ClassNotFoundException {
        String username = "";
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT username FROM user WHERE id=" + id);
            while (result.next()) {
                username = result.getString(1);
            }
        }
        return username;
    }
    
    /**
     * This method checks if an element in the luggage has changed.
     * @param type
     * @param editLuggage
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public boolean checkLuggageChanged(Luggage editLuggage, String type) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM luggage WHERE id=" + editLuggage.getId());
            Luggage dbLuggage = new Luggage();
            while (result.next()) {
                dbLuggage.setType(result.getString(2));
                dbLuggage.setBrand(result.getString(3));
                dbLuggage.setMaterial(result.getString(4));
                dbLuggage.setColor(result.getString(5));
                dbLuggage.setComment(result.getString(6));
                dbLuggage.setStatusId(result.getInt(8));
                dbLuggage.setResolved(result.getBoolean(11));
            }
            switch (type) {
                case("type"): {
                    return !dbLuggage.getType().equals(editLuggage.getType());
                }
                case("brand"): {
                    return !dbLuggage.getBrand().equals(editLuggage.getBrand());
                }
                case("material"): {
                    return !dbLuggage.getMaterial().equals(editLuggage.getMaterial());
                }
                case("color"): {
                    return !dbLuggage.getColor().equals(editLuggage.getColor());
                }
                case("comment"): {
                    if (dbLuggage.getComment() != null && editLuggage.getComment() != null) {
                        return !dbLuggage.getComment().equals(editLuggage.getComment());
                    } else {
                        return false;
                    }
                }
                case("status"): {
                    return (dbLuggage.getStatusId() != editLuggage.getStatusId());
                }
                case("resolved"): {
                    return (dbLuggage.isResolved() != editLuggage.isResolved());
                }
                default: {
                    return false;
                }
            }
        }
    }
    
    /**
     * This method logs whenever a piece of luggage has been changed by an employee.
     * @param editLuggage
     * @param currentUser
     * @param type
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void logLuggageChanged(Luggage editLuggage, User currentUser, String type) throws ClassNotFoundException, SQLException {
        String change = "";
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM luggage WHERE id=" + editLuggage.getId());
            Luggage dbLuggage = new Luggage();
            while (result.next()) {
                dbLuggage.setType(result.getString(2));
                dbLuggage.setBrand(result.getString(3));
                dbLuggage.setMaterial(result.getString(4));
                dbLuggage.setColor(result.getString(5));
                dbLuggage.setComment(result.getString(6));
                dbLuggage.setStatusId(result.getInt(8));
                dbLuggage.setResolved(result.getBoolean(11));
            }
            if (type.equals("type") && !dbLuggage.getType().equals(editLuggage.getType())) {
                change = ("Type: " + dbLuggage.getType() + " was changed to: " + editLuggage.getType());
            }
            if (type.equals("brand") && !dbLuggage.getBrand().equals(editLuggage.getBrand())) {
                change = ("Brand: " + dbLuggage.getBrand() + " was changed to: " + editLuggage.getBrand());
            }
            if (type.equals("material") && !dbLuggage.getMaterial().equals(editLuggage.getMaterial())) {
                change = ("Material: " + dbLuggage.getMaterial() + " was changed to: " + editLuggage.getMaterial());
            }
            if (type.equals("color") && !dbLuggage.getColor().equals(editLuggage.getColor())) {
                change = ("Color: " + dbLuggage.getColor() + " was changed to: " + editLuggage.getColor());
            }
            if (editLuggage.getComment() != null) {
                if (!dbLuggage.getComment().equals(editLuggage.getComment())) {
                    if (editLuggage.getComment().equals("")) {
                        change = ("Comment: \"" + dbLuggage.getComment() + "\" was removed");
                    } else if (dbLuggage.getComment().equals("")) {
                        change = ("Comment: \"" + editLuggage.getComment() + "\" was added");
                    } else {
                        change = ("Comment: \"" + dbLuggage.getComment() + "\" was changed to: \"" + editLuggage.getComment() + "\"");
                    }
                }
            }
            if (type.equals("status") && (dbLuggage.getStatusId() != editLuggage.getStatusId())) {
                change = ("Status: " + (new Status().getStatusById(dbLuggage.getStatusId()).getStatusName())) + 
                        " was changed to: " + (new Status().getStatusById(editLuggage.getStatusId()).getStatusName());
            }
            System.out.println(dbLuggage.isResolved() != editLuggage.isResolved());
            System.out.println(dbLuggage.isResolved() + " != " + editLuggage.isResolved());
            if (type.equals("resolved") && (dbLuggage.isResolved() != editLuggage.isResolved())) {
                System.out.println(editLuggage.isResolved());
                if (editLuggage.isResolved()) {
                    change = ("Luggage has been marked as \"Resolved\"");
                } else {
                    change = ("Luggage has been marked as \"Unresolved\"");
                }
                System.out.println(change);
            }
            db.close();
        }
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            String query = ("INSERT INTO logluggage (logdate,luggageid,employeeid,logtext) VALUES (?,?,?,?)");
            PreparedStatement preparedStatement = (PreparedStatement) db.prepareStatement(query);
            preparedStatement.setString(1, (new DateConverter().convertJavaDateToSqlDate(new java.util.Date())));
            preparedStatement.setInt(2, editLuggage.getId());
            preparedStatement.setInt(3, currentUser.getId());
            preparedStatement.setString(4, change);
            preparedStatement.executeUpdate();
            db.close();
        }
    }
    
    /**
     * This method checks if an element of the Customer object has changed.
     * @param type The type of a Customer value
     * @param editCustomer The Customer object that might have been edited
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public boolean checkCustomerChanged(Customer editCustomer, String type) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM customer WHERE id=" + editCustomer.getId());
            Customer dbCustomer = new Customer();
            while (result.next()) {
                dbCustomer.setFirstName(result.getString(2));
                dbCustomer.setLastName(result.getString(3));
                dbCustomer.setGender(result.getString(4));
                dbCustomer.setPhone(result.getString(5));
                dbCustomer.setAddress(result.getString(6));
                dbCustomer.setEmail(result.getString(7));
            }
            switch (type) {
                case("firstName"): {
                    return !dbCustomer.getFirstName().equals(editCustomer.getFirstName());
                }
                case("lastName"): {
                    return !dbCustomer.getLastName().equals(editCustomer.getLastName());
                }
                case("gender"): {
                    return !dbCustomer.getGender().equals(editCustomer.getGender());
                }
                case("phone"): {
                    return !dbCustomer.getPhone().equals(editCustomer.getPhone());
                }
                case("address"): {
                    return !dbCustomer.getAddress().equals(editCustomer.getAddress());
                }
                case("email"): {
                    return !dbCustomer.getEmail().equals(editCustomer.getEmail());
                }
                default: {
                    return false;
                }
            }
        }
    }
    
    /**
     * This method logs the changes that have been made to a Customer if a value was changed.
     * @param editCustomer The Customer to check next to the one registered in the Database.
     * @param currentUser The current user that is editing the Customer, needed to get the ID of this user.
     * @param type The type of luggage that has been changed. Needed to prevent double entries into the logs.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void logCustomerChanged(Customer editCustomer, User currentUser, String type) throws ClassNotFoundException, SQLException {
        String change = "";
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM customer WHERE id=" + editCustomer.getId());
            Customer dbCustomer = new Customer();
            while (result.next()) {
                dbCustomer.setFirstName(result.getString(2));
                dbCustomer.setLastName(result.getString(3));
                dbCustomer.setGender(result.getString(4));
                dbCustomer.setPhone(result.getString(5));
                dbCustomer.setAddress(result.getString(6));
                dbCustomer.setEmail(result.getString(7));
            }
            if (type.equals("firstName") && !dbCustomer.getFirstName().equals(editCustomer.getFirstName())) {
                change = ("First name: " + dbCustomer.getFirstName() + " was changed to: " + editCustomer.getFirstName());
            }
            if (type.equals("lastName") && !dbCustomer.getLastName().equals(editCustomer.getLastName())) {
                change = ("Last name: " + dbCustomer.getLastName() + " was changed to: " + editCustomer.getLastName());
            }
            if (type.equals("gender") && !dbCustomer.getGender().equals(editCustomer.getGender())) {
                change = ("Gender: " + dbCustomer.getGender() + " was changed to: " + editCustomer.getGender());
            }
            if (type.equals("phone") && !dbCustomer.getPhone().equals(editCustomer.getPhone())) {
                change = ("Phone number: " + dbCustomer.getPhone() + " was changed to: " + editCustomer.getPhone());
            }
            if (type.equals("address") && !dbCustomer.getAddress().equals(editCustomer.getAddress())) {
                change = ("Address: " + dbCustomer.getAddress() + " was changed to: " + editCustomer.getAddress());
            }
            if (type.equals("email") && !dbCustomer.getEmail().equals(editCustomer.getEmail())) {
                change = ("Email: " + dbCustomer.getEmail() + " was changed to: " + editCustomer.getEmail());
            }
            db.close();
        }
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            String query = ("INSERT INTO logcustomer (logdate,customerid,employeeid,logtext) VALUES (?,?,?,?)");
            PreparedStatement preparedStatement = (PreparedStatement) db.prepareStatement(query);
            preparedStatement.setString(1, (new DateConverter().convertJavaDateToSqlDate(new java.util.Date())));
            preparedStatement.setInt(2, editCustomer.getId());
            preparedStatement.setInt(3, currentUser.getId());
            preparedStatement.setString(4, change);
            preparedStatement.executeUpdate();
            db.close();
        }
    }
}
