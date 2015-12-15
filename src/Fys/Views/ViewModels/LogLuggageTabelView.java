package Fys.Views.ViewModels;

import Fys.Models.Luggage;
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
public class LogLuggageTabelView {

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

    public ObservableList<LogLuggageTabelView> getLogList(Luggage luggage) throws Exception {
        ObservableList<LogLuggageTabelView> logEntry = FXCollections.observableArrayList();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM logluggage WHERE luggageid=" + luggage.getId() + " ORDER BY logdate DESC");
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
