package Fys.Tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

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
    
    public String getCustomerRegisterEmployee(int id) throws SQLException, ClassNotFoundException {
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
}
