package Fys.Tools;

import Fys.Models.User;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Jeffrey van der Lingen, Daan Befort, IS106-2
 */
public class ChartTools {

    private String date;
    private int amount;

    /**
     *
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *
     * @return
     */
    public int getAmount() {
        return amount;
    }

    /**
     *
     * @param amount
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     *
     * @param startDate
     * @param endDate
     * @param interval
     * @param type
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public ObservableList<ChartTools> getLostOrFoundLuggage(LocalDate startDate, LocalDate endDate, int interval, int type) throws SQLException, ClassNotFoundException {
        ObservableList<ChartTools> luggageData = FXCollections.observableArrayList();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            switch (interval) {
                /* Luggage per day */
                case (1): {
                    Statement statement = db.createStatement();
                    ResultSet result = statement.executeQuery("SELECT registerdate, DATE_FORMAT(registerdate, '%Y-%m-%d') AS date, statusid AS status, COUNT(registerdate) AS aantal "
                            + "FROM luggage "
                            + "WHERE statusid = " + type + " GROUP BY DATE_FORMAT(registerdate, '%Y-%m-%d'), statusid "
                            + "HAVING DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN '" + startDate + "' AND '" + endDate + "'");
                    LocalDate iterateDate = startDate;
                    ArrayList<String> dateArray = new ArrayList<String>();
                    ArrayList<Integer> amountArray = new ArrayList<Integer>();
                    while (result.next()) {
                        dateArray.add(result.getString(2));
                        amountArray.add(result.getInt(3));
                    }
                    while (!iterateDate.equals(endDate.plusDays(1))) {
                        ChartTools chartTools = new ChartTools();
                        chartTools.setDate(iterateDate.toString());
                        for (int i = 0; i < dateArray.size(); i++) {
                            if (dateArray.get(i).equals(iterateDate.toString())) {
                                chartTools.setAmount(amountArray.get(i));
                                break;
                            } else {
                                chartTools.setAmount(0);
                            }
                        }
                        iterateDate = iterateDate.plusDays(1);
                        luggageData.add(chartTools);
                    }
                    break;
                }
                /* Luggage per month */
                case (2): {
                    Statement statement = db.createStatement();
                    ResultSet result = statement.executeQuery("SELECT registerdate, MONTH(registerdate) AS month, YEAR(registerdate) AS year, statusid AS status, COUNT(registerdate) AS aantal "
                            + "FROM luggage "
                            + "WHERE statusid = " + type + " GROUP BY MONTH(registerdate), YEAR(registerdate), statusid "
                            + "HAVING DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%y-%M-01') AND LAST_DAY('" + endDate + "');");
                    LocalDate iterateDate = startDate;
                    ArrayList<String> dateArray = new ArrayList<String>();
                    ArrayList<Integer> amountArray = new ArrayList<Integer>();
                    while (result.next()) {
                        dateArray.add(result.getString(3) + " - " + result.getString(2));
                        amountArray.add(result.getInt(5));
                    }

                    String stringIterateDate = iterateDate.getYear() + " - " + iterateDate.getMonth().getValue();
                    endDate = endDate.plusMonths(1);
                    String stringEndDate = endDate.getYear() + " - " + endDate.getMonth().getValue();

                    while (!stringIterateDate.equals(stringEndDate)) {
                        ChartTools chartTools = new ChartTools();
                        chartTools.setDate(Integer.toString(iterateDate.getYear()) + " - " + iterateDate.getMonth());
                        for (int i = 0; i < dateArray.size(); i++) {
                            if (dateArray.get(i).equals(stringIterateDate)) {
                                chartTools.setAmount(amountArray.get(i));
                                break;
                            } else {
                                chartTools.setAmount(0);
                            }
                        }
                        iterateDate = iterateDate.plusMonths(1);
                        stringIterateDate = iterateDate.getYear() + " - " + iterateDate.getMonth().getValue();
                        luggageData.add(chartTools);
                    }
                    break;
                }
                /* Luggage per year */
                case (3): {
                    Statement statement = db.createStatement();
                    ResultSet result = statement.executeQuery("SELECT registerdate, YEAR(registerdate) AS year, statusid AS status, COUNT(registerdate) AS aantal "
                            + "FROM luggage "
                            + "WHERE statusid = " + type + " GROUP BY YEAR(registerdate), statusid "
                            + "HAVING DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%y-%M-01') AND LAST_DAY('" + endDate + "');");
                    LocalDate iterateDate = startDate;
                    ArrayList<String> dateArray = new ArrayList<String>();
                    ArrayList<Integer> amountArray = new ArrayList<Integer>();
                    while (result.next()) {
                        dateArray.add(result.getString(2));
                        amountArray.add(result.getInt(4));
                    }

                    int iterateYear = startDate.getYear();
                    int endYear = endDate.getYear() + 1;

                    while (iterateYear != endYear) {
                        ChartTools chartTools = new ChartTools();
                        chartTools.setDate(Integer.toString(iterateYear));
                        for (int i = 0; i < dateArray.size(); i++) {
                            if (dateArray.get(i).equals(Integer.toString(iterateYear))) {
                                chartTools.setAmount(amountArray.get(i));
                                break;
                            } else {
                                chartTools.setAmount(0);
                            }
                        }
                        iterateYear = iterateYear + 1;
                        luggageData.add(chartTools);
                    }
                    break;
                }

            }

        }
        return luggageData;
    }

    /**
     *
     * @param startDate
     * @param endDate
     * @param interval
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public ObservableList<ChartTools> getConnectedLuggage(LocalDate startDate, LocalDate endDate, int interval) throws SQLException, ClassNotFoundException {
        ObservableList<ChartTools> luggageData = FXCollections.observableArrayList();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            switch (interval) {
                /* Connections per day */
                case (1): {
                    Statement statement = db.createStatement();
                    ResultSet result = statement.executeQuery("SELECT connectiondate, DATE_FORMAT(connectiondate, '%Y-%m-%d') AS date, COUNT(connectiondate) AS aantal "
                            + "FROM connection GROUP BY DATE_FORMAT(connectiondate, '%Y-%m-%d') "
                            + "HAVING DATE_FORMAT(connectiondate, '%Y-%m-%d') BETWEEN '" + startDate + "' AND '" + endDate + "';");
                    LocalDate iterateDate = startDate;
                    ArrayList<String> dateArray = new ArrayList<String>();
                    ArrayList<Integer> amountArray = new ArrayList<Integer>();
                    while (result.next()) {
                        dateArray.add(result.getString(2));
                        amountArray.add(result.getInt(3));
                    }
                    while (!iterateDate.equals(endDate.plusDays(1))) {
                        ChartTools chartTools = new ChartTools();
                        chartTools.setDate(iterateDate.toString());
                        for (int i = 0; i < dateArray.size(); i++) {
                            if (dateArray.get(i).equals(iterateDate.toString())) {
                                chartTools.setAmount(amountArray.get(i));
                                break;
                            } else {
                                chartTools.setAmount(0);
                            }
                        }
                        iterateDate = iterateDate.plusDays(1);
                        luggageData.add(chartTools);
                    }
                    break;
                }
                /* Connections per month */
                case (2): {
                    Statement statement = db.createStatement();
                    ResultSet result = statement.executeQuery("SELECT connectiondate, MONTH(connectiondate) AS month, YEAR(connectiondate) AS year, COUNT(connectiondate) AS aantal "
                            + "FROM connection GROUP BY MONTH(connectiondate), YEAR(connectiondate) "
                            + "HAVING DATE_FORMAT(connectiondate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%y-%M-01') AND LAST_DAY('" + endDate + "');");
                    LocalDate iterateDate = startDate;
                    ArrayList<String> dateArray = new ArrayList<String>();
                    ArrayList<Integer> amountArray = new ArrayList<Integer>();
                    while (result.next()) {
                        dateArray.add(result.getString(3) + " - " + result.getString(2));
                        amountArray.add(result.getInt(4));
                    }

                    String stringIterateDate = iterateDate.getYear() + " - " + iterateDate.getMonth().getValue();
                    endDate = endDate.plusMonths(1);
                    String stringEndDate = endDate.getYear() + " - " + endDate.getMonth().getValue();

                    while (!stringIterateDate.equals(stringEndDate)) {
                        ChartTools chartTools = new ChartTools();
                        chartTools.setDate(Integer.toString(iterateDate.getYear()) + " - " + iterateDate.getMonth());
                        for (int i = 0; i < dateArray.size(); i++) {
                            if (dateArray.get(i).equals(stringIterateDate)) {
                                chartTools.setAmount(amountArray.get(i));
                                break;
                            } else {
                                chartTools.setAmount(0);
                            }
                        }
                        iterateDate = iterateDate.plusMonths(1);
                        stringIterateDate = iterateDate.getYear() + " - " + iterateDate.getMonth().getValue();
                        luggageData.add(chartTools);
                    }
                    break;
                }
                /* Connections per year */
                case (3): {
                    Statement statement = db.createStatement();
                    ResultSet result = statement.executeQuery("SELECT connectiondate, YEAR(connectiondate) AS year, COUNT(connectiondate) AS aantal "
                            + "FROM connection GROUP BY YEAR(connectiondate) "
                            + "HAVING DATE_FORMAT(connectiondate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%y-%M-01') AND LAST_DAY('" + endDate + "');");
                    LocalDate iterateDate = startDate;
                    ArrayList<String> dateArray = new ArrayList<String>();
                    ArrayList<Integer> amountArray = new ArrayList<Integer>();
                    while (result.next()) {
                        dateArray.add(result.getString(2));
                        amountArray.add(result.getInt(3));
                    }

                    int iterateYear = startDate.getYear();
                    int endYear = endDate.getYear() + 1;

                    while (iterateYear != endYear) {
                        ChartTools chartTools = new ChartTools();
                        chartTools.setDate(Integer.toString(iterateYear));
                        for (int i = 0; i < dateArray.size(); i++) {
                            if (dateArray.get(i).equals(Integer.toString(iterateYear))) {
                                chartTools.setAmount(amountArray.get(i));
                                break;
                            } else {
                                chartTools.setAmount(0);
                            }
                        }
                        iterateYear = iterateYear + 1;
                        luggageData.add(chartTools);
                    }
                    break;
                }

            }

        }
        return luggageData;
    }
    
    /**
     *
     * @param startDate
     * @param endDate
     * @param interval
     * @param type
     * @param employee
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public ObservableList<ChartTools> getLostOrFoundLuggagePerEmployee(LocalDate startDate, LocalDate endDate, int interval, int type, User employee, int resolved) throws SQLException, ClassNotFoundException {
        ObservableList<ChartTools> luggageData = FXCollections.observableArrayList();
        int employeeId = employee.getId();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            switch (interval) {
                /* Luggage per day */
                case (1): {
                    Statement statement = db.createStatement();
                    ResultSet result = null;
                    if (resolved != 3) {
                        result = statement.executeQuery("SELECT registerdate, DATE_FORMAT(registerdate, '%Y-%m-%d') AS date, statusid AS status, COUNT(registerdate) AS aantal FROM luggage" +
                                        " WHERE statusid= " + type + " AND employeeid=" + employeeId + " AND resolved=" + resolved +
                                        " GROUP BY DATE_FORMAT(registerdate, '%Y-%m-%d')" +
                                        " HAVING DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN '" + startDate + "' AND '" + endDate + "'");
                    } else {
                        result = statement.executeQuery("SELECT registerdate, DATE_FORMAT(registerdate, '%Y-%m-%d') AS date, statusid AS status, COUNT(registerdate) AS aantal FROM luggage" +
                                        " WHERE statusid= " + type + " AND employeeid=" + employeeId +
                                        " GROUP BY DATE_FORMAT(registerdate, '%Y-%m-%d')" +
                                        " HAVING DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN '" + startDate + "' AND '" + endDate + "'");
                    }
                    LocalDate iterateDate = startDate;
                    ArrayList<String> dateArray = new ArrayList<String>();
                    ArrayList<Integer> amountArray = new ArrayList<Integer>();
                    while (result.next()) {
                        dateArray.add(result.getString(2));
                        amountArray.add(result.getInt(4));
                    }
                    while (!iterateDate.equals(endDate.plusDays(1))) {
                        ChartTools chartTools = new ChartTools();
                        chartTools.setDate(iterateDate.toString());
                        for (int i = 0; i < dateArray.size(); i++) {
                            if (dateArray.get(i).equals(iterateDate.toString())) {
                                chartTools.setAmount(amountArray.get(i));
                                break;
                            } else {
                                chartTools.setAmount(0);
                            }
                        }
                        iterateDate = iterateDate.plusDays(1);
                        luggageData.add(chartTools);
                    }
                    break;
                }
                /* Luggage per month */
                case (2): {
                    Statement statement = db.createStatement();
                    ResultSet result = null;
                    if (resolved != 3) {
                        result = statement.executeQuery("SELECT registerdate, MONTH(registerdate) AS month, YEAR(registerdate) AS year, statusid AS status, COUNT(registerdate) AS aantal" +
                                        " FROM luggage" +
                                        " WHERE statusid="+ type +" AND employeeid=" + employeeId + " AND resolved=" + resolved +
                                        " GROUP BY MONTH(registerdate), YEAR(registerdate)" +
                                        " HAVING DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('"+ startDate +"', '%Y-%m-01') AND LAST_DAY('"+ endDate +"');");
                    } else {
                        result = statement.executeQuery("SELECT registerdate, MONTH(registerdate) AS month, YEAR(registerdate) AS year, statusid AS status, COUNT(registerdate) AS aantal" +
                                        " FROM luggage" +
                                        " WHERE statusid="+ type +" AND employeeid=" + employeeId +
                                        " GROUP BY MONTH(registerdate), YEAR(registerdate)" +
                                        " HAVING DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('"+ startDate +"', '%Y-%m-01') AND LAST_DAY('"+ endDate +"');");
                    }
                    LocalDate iterateDate = startDate;
                    ArrayList<String> dateArray = new ArrayList<String>();
                    ArrayList<Integer> amountArray = new ArrayList<Integer>();
                    while (result.next()) {
                        dateArray.add(result.getString(3) + " - " + result.getString(2));
                        amountArray.add(result.getInt(5));
                    }

                    String stringIterateDate = iterateDate.getYear() + " - " + iterateDate.getMonth().getValue();
                    endDate = endDate.plusMonths(1);
                    String stringEndDate = endDate.getYear() + " - " + endDate.getMonth().getValue();
                    while (!stringIterateDate.equals(stringEndDate)) {
                        ChartTools chartTools = new ChartTools();
                        chartTools.setDate(Integer.toString(iterateDate.getYear()) + " - " + iterateDate.getMonth());
                        for (int i = 0; i < dateArray.size(); i++) {
                            if (dateArray.get(i).equals(stringIterateDate)) {
                                chartTools.setAmount(amountArray.get(i));
                                break;
                            } else {
                                chartTools.setAmount(0);
                            }
                        }
                        iterateDate = iterateDate.plusMonths(1);
                        stringIterateDate = iterateDate.getYear() + " - " + iterateDate.getMonth().getValue();
                        luggageData.add(chartTools);
                    }
                    break;
                }
                /* Luggage per year */
                case (3): {
                    Statement statement = db.createStatement();
                    ResultSet result = null;
                    if (resolved != 3) {
                        result = statement.executeQuery("SELECT registerdate, YEAR(registerdate) AS year, statusid AS status, COUNT(registerdate) AS aantal "
                            + "FROM luggage "
                            + "WHERE statusid=" + type + " AND employeeid=" + employeeId + " AND resolved= " + resolved
                            + " GROUP BY YEAR(registerdate) "
                            + "HAVING DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%y-%M-01') AND LAST_DAY('" + endDate + "');");
                    } else {
                        result = statement.executeQuery("SELECT registerdate, YEAR(registerdate) AS year, statusid AS status, COUNT(registerdate) AS aantal "
                            + "FROM luggage "
                            + "WHERE statusid=" + type + " AND employeeid=" + employeeId
                            + " GROUP BY YEAR(registerdate) "
                            + "HAVING DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%y-%M-01') AND LAST_DAY('" + endDate + "');");
                    }
                    LocalDate iterateDate = startDate;
                    ArrayList<String> dateArray = new ArrayList<String>();
                    ArrayList<Integer> amountArray = new ArrayList<Integer>();
                    while (result.next()) {
                        dateArray.add(result.getString(2));
                        amountArray.add(result.getInt(4));
                    }

                    int iterateYear = startDate.getYear();
                    int endYear = endDate.getYear() + 1;

                    while (iterateYear != endYear) {
                        ChartTools chartTools = new ChartTools();
                        chartTools.setDate(Integer.toString(iterateYear));
                        for (int i = 0; i < dateArray.size(); i++) {
                            if (dateArray.get(i).equals(Integer.toString(iterateYear))) {
                                chartTools.setAmount(amountArray.get(i));
                                break;
                            } else {
                                chartTools.setAmount(0);
                            }
                        }
                        iterateYear = iterateYear + 1;
                        luggageData.add(chartTools);
                    }
                    break;
                }

            }

        } catch(Exception ex){
            System.out.println(ex.toString());
        }
        return luggageData;
    }

    /**
     *
     * @param startDate
     * @param endDate
     * @param interval
     * @param employee
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public ObservableList<ChartTools> getConnectedLuggagePerEmployee(LocalDate startDate, LocalDate endDate, int interval, User employee) throws SQLException, ClassNotFoundException {
        ObservableList<ChartTools> luggageData = FXCollections.observableArrayList();
        int employeeId = employee.getId();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            switch (interval) {
                /* Connections per day */
                case (1): {
                    Statement statement = db.createStatement();
                    String query =  "SELECT lug.employeeid, con.connectiondate, DATE_FORMAT(connectiondate, '%Y-%m-%d') AS date, COUNT(connectiondate) AS aantal " +
                                    "FROM connection AS con INNER JOIN luggage AS lug ON con.luggageid = lug.id " +
                                    "WHERE lug.employeeid = " + employeeId +
                                    " GROUP BY DATE_FORMAT(connectiondate, '%Y-%m-%d') " +
                                    "HAVING DATE_FORMAT(connectiondate, '%Y-%m-%d') BETWEEN '" + startDate + "' AND '" + endDate + "';";
                    ResultSet result = statement.executeQuery(query);
                    
                    
                    LocalDate iterateDate = startDate;
                    ArrayList<String> dateArray = new ArrayList<String>();
                    ArrayList<Integer> amountArray = new ArrayList<Integer>();
                    while (result.next()) {
                        dateArray.add(result.getString(2));
                        amountArray.add(result.getInt(3));
                    }
                    while (!iterateDate.equals(endDate.plusDays(1))) {
                        ChartTools chartTools = new ChartTools();
                        chartTools.setDate(iterateDate.toString());
                        for (int i = 0; i < dateArray.size(); i++) {
                            if (dateArray.get(i).equals(iterateDate.toString())) {
                                chartTools.setAmount(amountArray.get(i));
                                break;
                            } else {
                                chartTools.setAmount(0);
                            }
                        }
                        iterateDate = iterateDate.plusDays(1);
                        luggageData.add(chartTools);
                    }
                    break;
                }
                /* Connections per month */
                case (2): {
                    Statement statement = db.createStatement();
                        ResultSet result = statement.executeQuery("SELECT con.connectiondate, MONTH(connectiondate) AS month, YEAR(connectiondate) AS year, COUNT(connectiondate) AS aantal " +
                                        "FROM connection as con INNER JOIN luggage as lug on con.luggageid = lug.id " +
                                        "WHERE lug.employeeid = " + employeeId +
                                        " GROUP BY MONTH(connectiondate), YEAR(connectiondate) " +
                                        "HAVING DATE_FORMAT(connectiondate, '%Y-%m-%d') BETWEEN DATE_FORMAT('"+ startDate +"', '%y-%M-01') AND LAST_DAY('"+ endDate +"');");
                    LocalDate iterateDate = startDate;
                    ArrayList<String> dateArray = new ArrayList<String>();
                    ArrayList<Integer> amountArray = new ArrayList<Integer>();
                    while (result.next()) {
                        dateArray.add(result.getString(3) + " - " + result.getString(2));
                        amountArray.add(result.getInt(4));
                    }

                    String stringIterateDate = iterateDate.getYear() + " - " + iterateDate.getMonth().getValue();
                    endDate = endDate.plusMonths(1);
                    String stringEndDate = endDate.getYear() + " - " + endDate.getMonth().getValue();

                    while (!stringIterateDate.equals(stringEndDate)) {
                        ChartTools chartTools = new ChartTools();
                        chartTools.setDate(Integer.toString(iterateDate.getYear()) + " - " + iterateDate.getMonth());
                        for (int i = 0; i < dateArray.size(); i++) {
                            if (dateArray.get(i).equals(stringIterateDate)) {
                                chartTools.setAmount(amountArray.get(i));
                                break;
                            } else {
                                chartTools.setAmount(0);
                            }
                        }
                        iterateDate = iterateDate.plusMonths(1);
                        stringIterateDate = iterateDate.getYear() + " - " + iterateDate.getMonth().getValue();
                        luggageData.add(chartTools);
                    }
                    break;
                }
                /* Connections per year */
                case (3): {
                    Statement statement = db.createStatement();
                    ResultSet result = statement.executeQuery("SELECT connectiondate, YEAR(connectiondate) AS year, COUNT(connectiondate) AS aantal " +
                                        "FROM connection AS con INNER JOIN luggage AS lug ON con.luggageid = lug.id " +
                                        "WHERE lug.employeeid = " + employeeId +
                                        " GROUP BY YEAR(connectiondate) " +
                                        "HAVING DATE_FORMAT(connectiondate, '%Y-%m-%d') BETWEEN DATE_FORMAT('"+ startDate +"', '%y-%M-01') AND LAST_DAY('"+ endDate +"');");
                    LocalDate iterateDate = startDate;
                    ArrayList<String> dateArray = new ArrayList<String>();
                    ArrayList<Integer> amountArray = new ArrayList<Integer>();
                    while (result.next()) {
                        dateArray.add(result.getString(2));
                        amountArray.add(result.getInt(3));
                    }

                    int iterateYear = startDate.getYear();
                    int endYear = endDate.getYear() + 1;

                    while (iterateYear != endYear) {
                        ChartTools chartTools = new ChartTools();
                        chartTools.setDate(Integer.toString(iterateYear));
                        for (int i = 0; i < dateArray.size(); i++) {
                            if (dateArray.get(i).equals(Integer.toString(iterateYear))) {
                                chartTools.setAmount(amountArray.get(i));
                                break;
                            } else {
                                chartTools.setAmount(0);
                            }
                        }
                        iterateYear = iterateYear + 1;
                        luggageData.add(chartTools);
                    }
                    break;
                }

            }

        }
        return luggageData;
    }
}
