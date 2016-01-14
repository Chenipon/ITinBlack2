package Fys.Tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class contains the JDBC Connector. It contains the Connection object
 * that can be used to make connections with the Database.
 *
 * @author Daan Befort, IS106-2
 */
public class ConnectMysqlServer {

    private Connection connection;

    /**
     * This method contains the connector to the database, used to make
     * connections with the Database to insert, modify or create data.
     *
     * @return the Connection object that can be used to connect with the
     * Database.
     * @throws ClassNotFoundException when the jdbc could not be found.
     * @throws SQLException when an SQL exception occurred.
     */
    public Connection dbConnect() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/fys", "root", "12qw12qw");
        return connection;
    }
}
