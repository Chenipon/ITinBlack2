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
 * This class manages the tool used to fill a ChartTool object, which is used to
 * collect data from the database to fill the graphs and charts.
 *
 * @author Jeffrey van der Lingen, Daan Befort, IS106-2
 */
public class ChartTools {

    private String date;
    private int amount;

    /**
     * String getDate() gets the date of a charttool object.
     *
     * @return String date
     */
    public String getDate() {
        return date;
    }

    /**
     * void setDate(String date) sets the date of a charttool object.
     *
     * @param date String date is the new date.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * int getAmount() gets the amount of a charttool object, which is the data
     * connected to a date.
     *
     * @return the amount of luggage connected to a date.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * void setAmount(int amount) sets the amount of luggage that needs to be
     * connected to a date.
     *
     * @param amount the amount of luggage that needs to be connected to a date.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * ObservableList<ChartTools> getLostOrFoundLuggage(LocalDate startDate,
     * LocalDate endDate, int interval, int type, int resolved) gets data based
     * off a lot of seperate parameters from the database and fills this data
     * connected to a date in an ObservableList containing ChartTools elements
     * that contain these 2 pieces of date. This ObservableList is then used for
     * adding to a graph or chart to display a graph or chart.
     *
     * @param startDate is the LocalDate that resembles the starting date of the
     * data that needs to be pulled from the database.
     * @param endDate is the LocalDate that resembles the ending date of the
     * data that needs to be pulled from the database.
     * @param interval is the interval of the data that needs to be pulled from
     * the database as follow: 1 = day, 2 = month, 3 = year.
     * @param type is the type of luggage that needs to be pulled from the
     * database as follow: 1 = lost, 2 = found.
     * @param resolved is the resolved parameter that pulls data as follow: 0 =
     * unresolved, 1 = resolved, 2 = all.
     * @return an ObservableList containing ChartTools elements per date
     * interval.
     * @throws SQLException when an SQL error has occured.
     * @throws ClassNotFoundException when the jdbc can't be found.
     */
    public ObservableList<ChartTools> getLostOrFoundLuggage(LocalDate startDate,
            LocalDate endDate, int interval, int type, int resolved)
            throws SQLException, ClassNotFoundException {
        ObservableList<ChartTools> luggageData = FXCollections.observableArrayList();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            switch (interval) {
                /* Luggage per day */
                case (1): {
                    Statement statement = db.createStatement();
                    ResultSet result;
                    if (resolved == 2) {
                        result = statement.executeQuery("SELECT registerdate, DATE_FORMAT(registerdate, '%Y-%m-%d') AS date, statusid AS status, COUNT(registerdate) AS aantal FROM luggage"
                                + " WHERE statusid= " + type
                                + " GROUP BY DATE_FORMAT(registerdate, '%Y-%m-%d')"
                                + " HAVING DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN '" + startDate + "' AND '" + endDate + "'");
                    } else {
                        result = statement.executeQuery("SELECT registerdate, DATE_FORMAT(registerdate, '%Y-%m-%d') AS date, statusid AS status, COUNT(registerdate) AS aantal FROM luggage"
                                + " WHERE statusid= " + type + " AND resolved=" + resolved
                                + " GROUP BY DATE_FORMAT(registerdate, '%Y-%m-%d')"
                                + " HAVING DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN '" + startDate + "' AND '" + endDate + "'");
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
                    ResultSet result;
                    if (resolved == 2) {
                        result = statement.executeQuery("SELECT registerdate, MONTH(registerdate) AS month, YEAR(registerdate) AS year, statusid AS status, COUNT(registerdate) AS aantal"
                                + " FROM luggage"
                                + " WHERE statusid=" + type
                                + " GROUP BY MONTH(registerdate), YEAR(registerdate)"
                                + " HAVING DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%m-01') AND LAST_DAY('" + endDate + "');");
                    } else {
                        result = statement.executeQuery("SELECT registerdate, MONTH(registerdate) AS month, YEAR(registerdate) AS year, statusid AS status, COUNT(registerdate) AS aantal"
                                + " FROM luggage"
                                + " WHERE statusid=" + type + " AND resolved=" + resolved
                                + " GROUP BY MONTH(registerdate), YEAR(registerdate)"
                                + " HAVING DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%m-01') AND LAST_DAY('" + endDate + "');");
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
                    ResultSet result;
                    if (resolved == 2) {
                        result = statement.executeQuery("SELECT registerdate, YEAR(registerdate) AS year, statusid AS status, COUNT(registerdate) AS aantal "
                                + "FROM luggage "
                                + "WHERE statusid=" + type
                                + " GROUP BY YEAR(registerdate) "
                                + "HAVING DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%y-%M-01') AND LAST_DAY('" + endDate + "');");
                    } else {
                        result = statement.executeQuery("SELECT registerdate, YEAR(registerdate) AS year, statusid AS status, COUNT(registerdate) AS aantal "
                                + "FROM luggage "
                                + "WHERE statusid=" + type + " AND resolved= " + resolved
                                + " GROUP BY YEAR(registerdate) "
                                + "HAVING DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%y-%M-01') AND LAST_DAY('" + endDate + "');");
                    }
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
     * ObservableList<ChartTools> getLostOrFoundLuggageEmployee(LocalDate
     * startDate, LocalDate endDate, int interval, int type, User employee, int
     * resolved) gets data based off a lot of seperate parameters from the
     * database and fills this data connected to a date in an ObservableList
     * containing ChartTools elements that contain these 2 pieces of date. This
     * ObservableList is then used for adding to a graph or chart to display a
     * graph or chart.
     *
     * @param startDate is the LocalDate that resembles the starting date of the
     * data that needs to be pulled from the database.
     * @param endDate is the LocalDate that resembles the ending date of the
     * data that needs to be pulled from the database.
     * @param interval is the interval of the data that needs to be pulled from
     * the database as follow: 1 = day, 2 = month, 3 = year.
     * @param type is the type of luggage that needs to be pulled from the
     * database as follow: 1 = lost, 2 = found.
     * @param employee is the employee of who the data belongs to.
     * @param resolved is the resolved parameter that pulls data as follow: 0 =
     * unresolved, 1 = resolved, 2 = all.
     * @return an ObservableList containing ChartTools elements per date
     * interval.
     * @throws SQLException when an SQL error has occured.
     * @throws ClassNotFoundException when the jdbc can't be found.
     */
    public ObservableList<ChartTools> getLostOrFoundLuggagePerEmployee(LocalDate startDate,
            LocalDate endDate, int interval, int type, User employee, int resolved)
            throws SQLException, ClassNotFoundException {
        ObservableList<ChartTools> luggageData = FXCollections.observableArrayList();
        int employeeId = employee.getId();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            switch (interval) {
                /* Luggage per day */
                case (1): {
                    Statement statement = db.createStatement();
                    ResultSet result;
                    if (resolved == 2) {
                        result = statement.executeQuery("SELECT registerdate, DATE_FORMAT(registerdate, '%Y-%m-%d') AS date, statusid AS status, COUNT(registerdate) AS aantal FROM luggage"
                                + " WHERE statusid= " + type + " AND employeeid=" + employeeId
                                + " GROUP BY DATE_FORMAT(registerdate, '%Y-%m-%d')"
                                + " HAVING DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN '" + startDate + "' AND '" + endDate + "'");
                    } else {
                        result = statement.executeQuery("SELECT registerdate, DATE_FORMAT(registerdate, '%Y-%m-%d') AS date, statusid AS status, COUNT(registerdate) AS aantal FROM luggage"
                                + " WHERE statusid= " + type + " AND employeeid=" + employeeId + " AND resolved=" + resolved
                                + " GROUP BY DATE_FORMAT(registerdate, '%Y-%m-%d')"
                                + " HAVING DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN '" + startDate + "' AND '" + endDate + "'");
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
                    ResultSet result;
                    if (resolved == 2) {
                        result = statement.executeQuery("SELECT registerdate, MONTH(registerdate) AS month, YEAR(registerdate) AS year, statusid AS status, COUNT(registerdate) AS aantal"
                                + " FROM luggage"
                                + " WHERE statusid=" + type + " AND employeeid=" + employeeId
                                + " GROUP BY MONTH(registerdate), YEAR(registerdate)"
                                + " HAVING DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%m-01') AND LAST_DAY('" + endDate + "');");
                    } else {
                        result = statement.executeQuery("SELECT registerdate, MONTH(registerdate) AS month, YEAR(registerdate) AS year, statusid AS status, COUNT(registerdate) AS aantal"
                                + " FROM luggage"
                                + " WHERE statusid=" + type + " AND employeeid=" + employeeId + " AND resolved=" + resolved
                                + " GROUP BY MONTH(registerdate), YEAR(registerdate)"
                                + " HAVING DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%m-01') AND LAST_DAY('" + endDate + "');");
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
                    ResultSet result;
                    if (resolved == 2) {
                        result = statement.executeQuery("SELECT registerdate, YEAR(registerdate) AS year, statusid AS status, COUNT(registerdate) AS aantal "
                                + "FROM luggage "
                                + "WHERE statusid=" + type + " AND employeeid=" + employeeId
                                + " GROUP BY YEAR(registerdate) "
                                + "HAVING DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%y-%M-01') AND LAST_DAY('" + endDate + "');");
                    } else {
                        result = statement.executeQuery("SELECT registerdate, YEAR(registerdate) AS year, statusid AS status, COUNT(registerdate) AS aantal "
                                + "FROM luggage "
                                + "WHERE statusid=" + type + " AND employeeid=" + employeeId + " AND resolved= " + resolved
                                + " GROUP BY YEAR(registerdate) "
                                + "HAVING DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%y-%M-01') AND LAST_DAY('" + endDate + "');");
                    }
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

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return luggageData;
    }

}
