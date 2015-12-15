package Fys.Tools;

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
 * @author Jeffrey van der Lingen, IS106-2
 */
public class ChartTools {

    private String date;
    private int amount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ObservableList<ChartTools> getLostOrFoundLuggage(LocalDate startDate, LocalDate endDate, int interval, int type) throws SQLException, ClassNotFoundException {
        ObservableList<ChartTools> lostLuggage = FXCollections.observableArrayList();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            switch (interval) {
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
                        lostLuggage.add(chartTools);
                    }
                    break;
                }
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
                        lostLuggage.add(chartTools);
                    }
                    break;
                }
                case (3): {
                    Statement statement = db.createStatement();
                    ResultSet result = statement.executeQuery("SELECT registerdate, YEAR(registerdate) AS year, statusid AS status, COUNT(registerdate) AS aantal "
                            + "FROM luggage "
                            + "WHERE statusid = " + type + " GROUP BY YEAR(registerdate), statusid "
                            + "HAVING DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('2015-11-22', '%y-%M-01') AND LAST_DAY('2015-12-12');");
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
                        lostLuggage.add(chartTools);
                    }
                    break;
                }

            }

        }
        return lostLuggage;
    }

    public ObservableList<ChartTools> getConnectedLuggage(LocalDate startDate, LocalDate endDate, int interval) throws SQLException, ClassNotFoundException {
        ObservableList<ChartTools> lostLuggage = FXCollections.observableArrayList();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            switch (interval) {
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
                        lostLuggage.add(chartTools);
                    }
                    break;
                }
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
                        lostLuggage.add(chartTools);
                    }
                    break;
                }
                case (3): {
                    Statement statement = db.createStatement();
                    ResultSet result = statement.executeQuery("SELECT connectiondate, YEAR(connectiondate) AS year, COUNT(connectiondate) AS aantal "
                            + "FROM connection GROUP BY YEAR(connectiondate) "
                            + "HAVING DATE_FORMAT(connectiondate, '%Y-%m-%d') BETWEEN DATE_FORMAT('2015-11-22', '%y-%M-01') AND LAST_DAY('2015-12-12');");
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
                        lostLuggage.add(chartTools);
                    }
                    break;
                }

            }

        }
        return lostLuggage;
    }
}
