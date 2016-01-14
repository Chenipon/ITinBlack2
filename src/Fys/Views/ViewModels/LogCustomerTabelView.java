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
     * int getId() This method grabs the id of the Luggage, used to create the table.
     * @return id The id of the customer.
     */
    public int getId() {
        return id;
    }

    /**
     * void setId() This method sets the id of the Luggage, used to create the table.
     * @param id The id of the customer.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * String getRegisterdate() returns the registerdate of the Customer.
     * @return registerdate The registerdate of the customer.
     */
    public String getRegisterdate() {
        return registerdate;
    }

    /**
     * void setRegisterdate(String registerdate) sets the registerdate of the
     * customer.
     * @param registerdate The registerdate of the customer.
     */
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

    /**
     * String getChange() returns the changes made to the luggage of a Customer.
     * @return change The changes made to the luggage.
     */
    public String getChange() {
        return change;
    }

    /**
     * void setChange(String change) fills the String change.
     * @param change The changes made to the luggage.
     */
    public void setChange(String change) {
        this.change = change;
    }

    /**
     * void setEmployee(String employee) returns the employee that made a change.
     * @return employee The employee that made the change to the luggage or customer.
     */
    public String getEmployee() {
        return employee;
    }

    /**
     * void setEmployee(String employee) sets the employee that made the change.
     * @param employee The employee that made the change to the luggage or customer.
     */
    public void setEmployee(String employee) {
        this.employee = employee;
    }

    /**
     * ObservableList<LogCustomerTabelView> getLogList(Customer customer)
     * composes the loglist and puts all the data in the ObservableList.
     * @param customer The customer typed in 
     * @return logEntry FXCollection that logs all the changes made.
     * @throws Exception Catches exceptions made while looping the data to
     * the ObservableList.
     */
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
