package Fys.Views.ViewModels;

import Fys.Models.Luggage;
import Fys.Models.User;
import Fys.Tools.ConnectMysqlServer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * LogLuggageTabelView processes the tables for the history of a certain piece of
 * luggage.
 * @author Jeffrey van der Lingen, IS106-2
 */
public class LogLuggageTabelView {

    private int id;
    private String employee;
    private String registerdate;
    private String change;

    /**
     * int getId() gets the id of the luggage.
     * 
     * @return int id 
     */
    public int getId() {
        return id;
    }

    /**
     * void setId(int id) sets the id of the luggage.
     * 
     * @param id is the id that needs to be set in this class
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * String getRegisterdate() gets the Registerdate of the Luggage.
     * 
     * @return String registerdate 
     */
    public String getRegisterdate() {
        return registerdate;
    }

    /**
     * setRegisterdate(String registerdate) sets the Registerdate of the Luggage.
     * 
     * @param registerdate is the registerdate that needs to be set in this class
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
     * String getChange() gets the changes that are made to the luggage. 
     * 
     * @return String change
     */
    public String getChange() {
        return change;
    }

    /**
     * void setChange(String change) sets the changes that are made to the luggage.
     * 
     * @param change is the change that needs to be set in this class
     */
    public void setChange(String change) {
        this.change = change;
    }

    /**
     * String getEmployee() gets the employee that is needed for this table.
     * 
     * @return String employee
     */
    public String getEmployee() {
        return employee;
    }

    /**
     * void setEmployee(String employee) sets the employee for the table.
     * 
     * @param employee is the employee that needs to be set in this class
     */
    public void setEmployee(String employee) {
        this.employee = employee;
    }

    /**
     * ObservableList<LogLuggageTabelView> getLogList(Luggage luggage) returns an ObservableList
     * that can be used in the TableView.
     * 
     * @param luggage is the luggage that needs to be set in this class
     * @return logEntry
     * @throws SQLException when there is an SQL error.
     * @throws ClassNotFoundException when the jdbc can't be found.
     */
    public ObservableList<LogLuggageTabelView> getLogList(Luggage luggage) throws ClassNotFoundException, SQLException {
        ObservableList<LogLuggageTabelView> logEntry = FXCollections.observableArrayList();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery(
                      " SELECT * FROM logluggage"
                    + " WHERE luggageid=" + luggage.getId() + ""
                    + " ORDER BY logdate DESC");
            while (result.next()) {
                LogLuggageTabelView logLuggage = new LogLuggageTabelView();
                logLuggage.setRegisterdate(result.getString(2));
                logLuggage.setEmployee(new User().getUserById(result.getInt(4)).getUsername());
                logLuggage.setChange(result.getString(5));
                logEntry.add(logLuggage);
            }
        }
        return logEntry;
    }

}
