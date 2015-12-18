package Fys.Views.ViewModels;

import Fys.Models.Customer;
import Fys.Models.User;
import Fys.Tools.ConnectMysqlServer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * LogLuggageTabelView processes the tables for the history of a certain piece of
 * luggage.
 * @author Jeffrey van der Lingen, IS106-2
 */
public class LogCustomerTabelView {

    private int id;
    private String employee;
    private String registerdate;
    private String change;

    /**
     * This method grabs the id of the Luggage, used to create the table.
     * @see getId()
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * This method sets the id of the Luggage, used to create the table.
     * @see setId()
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    public String getRegisterdate() {
        return registerdate;
    }

    public void setRegisterdate(String registerdate) {
        /* 
         * For a strange reason, MySQL adds ".0" after the date. 
         * This is being manually removed here by using a String splitter 
         */
        if (registerdate.contains(".")) {
            String[] split = registerdate.split("\\.");
            registerdate = split[0];
        }
        this.registerdate = registerdate;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public ObservableList<LogCustomerTabelView> getLogList(Customer customer) throws Exception {
        ObservableList<LogCustomerTabelView> logEntry = FXCollections.observableArrayList();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM logcustomer WHERE customerid=" + customer.getId() + " ORDER BY logdate DESC");
            while (result.next()) {
                LogCustomerTabelView logCustomer = new LogCustomerTabelView();
                logCustomer.setRegisterdate(result.getString(2));
                logCustomer.setEmployee(new User().getUserById(result.getInt(4)).getUsername());
                logCustomer.setChange(result.getString(5));
                logEntry.add(logCustomer);
            }
        }
        return logEntry;
    }

}
