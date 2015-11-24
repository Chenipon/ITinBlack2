/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Fys.Tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Daan
 */
public class ConnectMysqlServer {
    private Connection connection;
    public Connection dbConnect() throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.jdbc.Driver");
        System.out.println("Driver Loaded");
        connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/fys", "root", "12qw12qw");
        System.out.println("Database connected");
        return connection;
    }
}
