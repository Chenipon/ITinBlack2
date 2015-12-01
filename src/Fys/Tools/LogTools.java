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
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class LogTools {
    public java.util.Date getCustomerRegisterDate(int id) throws SQLException, ParseException, ClassNotFoundException {
        String registerdate = "";
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT registerdate FROM customers WHERE id=" + id);
            while (result.next()) {
                registerdate = result.getString(1);
            }
        }
        return (new DateConverter().convertSqlDateToJavaDate(registerdate));
    }
    
    public String getCustomerRegisterEmployee(int id) {
        String username = "";
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT username FROM users WHERE id=" + id);
            while (result.next()) {
                username = result.getString(1);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LogTools.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            
        }
        return username;
    }
    
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

    public String getLuggageRegisterEmployee(int id) throws SQLException, ClassNotFoundException {
        String username = "";
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT username FROM users WHERE id=" + id);
            while (result.next()) {
                username = result.getString(1);
            }
        }
        return username;
    }
    
    public boolean checkLuggageChanged(Luggage editLuggage) throws ClassNotFoundException, SQLException {
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
            return (!dbLuggage.getType().equals(editLuggage.getType()) || 
                    !dbLuggage.getBrand().equals(editLuggage.getBrand()) || 
                    !dbLuggage.getMaterial().equals(editLuggage.getMaterial()) || 
                    !dbLuggage.getColor().equals(editLuggage.getColor()) ||
                    !dbLuggage.getComment().equals(editLuggage.getComment()) ||
                    dbLuggage.getStatusId() != editLuggage.getStatusId());
        }
    }
    
    public void logLuggageChanged(Luggage editLuggage, User currentUser) throws ClassNotFoundException, SQLException {
        StringBuilder sb = new StringBuilder(128);
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
            if (!dbLuggage.getType().equals(editLuggage.getType())) {
                sb.append("Type: ").append(dbLuggage.getType()).append(" was changed to: ").append(editLuggage.getType()).append("\n");
            }
            if (!dbLuggage.getBrand().equals(editLuggage.getBrand())) {
                sb.append("Brand: ").append(dbLuggage.getBrand()).append(" was changed to: ").append(editLuggage.getBrand()).append("\n");
            }
            if (!dbLuggage.getMaterial().equals(editLuggage.getMaterial())) {
                sb.append("Material: ").append(dbLuggage.getMaterial()).append(" was changed to: ").append(editLuggage.getMaterial()).append("\n");
            }
            if (!dbLuggage.getColor().equals(editLuggage.getColor())) {
                sb.append("Color: ").append(dbLuggage.getColor()).append(" was changed to: ").append(editLuggage.getColor()).append("\n");
            }
//            if (!dbLuggage.getComment().equals(editLuggage.getComment())) {
//                sb.append("Comment: \"").append(dbLuggage.getComment()).append("\" was changed to: \"").append(editLuggage.getComment()).append("\"\n");
//            }
            if (dbLuggage.getStatusId() != (editLuggage.getStatusId())) {
                sb.append("Status: ").append(new Status().getStatusById(dbLuggage.getStatusId()).getStatusName()).append(" was changed to: ").append(new Status().getStatusById(editLuggage.getStatusId()).getStatusName()).append("\n");
            }
            db.close();
        }
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            String query = ("INSERT INTO log (logdate,employeeid,logtext) VALUES (?,?,?)");
            PreparedStatement preparedStatement = (PreparedStatement) db.prepareStatement(query);
            preparedStatement.setString(1, (new DateConverter().convertJavaDateToSqlDate(new java.util.Date())));
            preparedStatement.setInt(2, currentUser.getId());
            String test = sb.toString();
            preparedStatement.setString(3, test);
            preparedStatement.executeUpdate();
            db.close();
        }
    }
}
