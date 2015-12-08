package Fys.Views.ViewModels;

import Fys.Models.Luggage;
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
public class LogLuggageTabelView {

    private int id;
    private int employeeId;
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

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getRegisterdate() {
        return registerdate;
    }

    public void setRegisterdate(String registerdate) {
        this.registerdate = registerdate;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public ObservableList<LogLuggageTabelView> getLogList(Luggage luggage) throws Exception {
        ObservableList<LogLuggageTabelView> logEntry = FXCollections.observableArrayList();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM logluggage WHERE id=" + luggage.getId());
            while (result.next()) {
                LogLuggageTabelView logLuggage = new LogLuggageTabelView();
                logLuggage.setRegisterdate(result.getString(2));
                logLuggage.setEmployeeId(result.getInt(4));
                logLuggage.setChange(result.getString(5));
                logEntry.add(logLuggage);
            }
        }
        return logEntry;
    }

}
