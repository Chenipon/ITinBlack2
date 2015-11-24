package Fys.Tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Daan Befort, IS106-2
 */
public class ConnectMysqlServer {
    private Connection connection;
    public Connection dbConnect() throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/fys", "root", "12qw12qw");
        return connection;
    }
}
