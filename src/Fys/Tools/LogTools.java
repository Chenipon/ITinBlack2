package Fys.Tools;

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
     * @see checkLuggageChanged()
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
            if (!dbLuggage.getComment().equals(editLuggage.getComment())) {
                if (editLuggage.getComment().equals("")) {
                    change = ("Comment: \"" + dbLuggage.getComment() + "\" was removed");
                } else if (dbLuggage.getComment().equals("")) {
                    change = ("Comment: \"" + editLuggage.getComment() + "\" was added");
                } else {
                    change = ("Comment: \"" + dbLuggage.getComment() + "\" was changed to: \"" + editLuggage.getComment() + "\"");
                }
            }
            if (type.equals("status") && dbLuggage.getStatusId() != (editLuggage.getStatusId())) {
                change = ("Status: " + (new Status().getStatusById(dbLuggage.getStatusId()).getStatusName())) + 
                        " was changed to: " + (new Status().getStatusById(editLuggage.getStatusId()).getStatusName());
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
}
