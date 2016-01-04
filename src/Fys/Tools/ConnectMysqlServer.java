package Fys.Tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Daan Befort, IS106-2
 * @author Javadoc: John Ghatas, IS106-2
 */
public class ConnectMysqlServer {
    private Connection connection;

    /**
     * This method contains the connector to the database, for the program to function correctly.
     * @see dbConnect()
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Connection dbConnect() throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/fys", "root", "Bouwblok123");
        return connection;
    }
}
