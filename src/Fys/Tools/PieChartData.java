package Fys.Tools;

import Fys.Models.User;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 * This class contains the PieChartData object, which can be used to fill up a
 * PieChart with data.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class PieChartData {

    private int lostLuggageResolved;
    private int foundLuggageResolved;
    private int lostLuggageUnresolved;
    private int foundLuggageUnresolved;
    private int totalLuggage;

    /**
     * Constructor PieChartDate() creates an empty PieChartData object.
     */
    public PieChartData() {
        this.lostLuggageResolved = 0;
        this.foundLuggageResolved = 0;
        this.foundLuggageUnresolved = 0;
        this.foundLuggageUnresolved = 0;
        this.totalLuggage = 0;
    }

    /**
     * void getData(LocalDate startDate, LocalDate endDate, int interval, int
     * resolved, int type) get data from the database by executing the queries
     * to fill the piechart.
     *
     *
     * @param startDate is the LocalDate that resembles the starting date of the
     * data that needs to be pulled from the database.
     * @param endDate is the LocalDate that resembles the ending date of the
     * data that needs to be pulled from the database.
     * @param interval is the interval of the data that needs to be pulled from
     * the database as follow: 1 = day, 2 = month, 3 = year.
     * @param statusid is the type of luggage that needs to be pulled from the
     * database as follow: 1 = lost, 2 = found.
     * @param resolved is the resolved parameter that pulls data as follow: 0 =
     * unresolved, 1 = resolved, 2 = all.
     * @throws ClassNotFoundException when the class could not be found.
     * @throws SQLException when no connection with the Database could be
     * established.
     */
    public void getData(LocalDate startDate, LocalDate endDate, int interval, int resolved,
            int statusid) throws ClassNotFoundException, SQLException {
        switch (interval) {
            case (1): {
                try (Connection db = new ConnectMysqlServer().dbConnect()) {
                    Statement statement = db.createStatement();
                    ResultSet result;
                    if (resolved == 2) {
                        if (statusid == 3) {
                            result = statement.executeQuery(
                                    " SELECT registerdate, statusid, COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE DATE_FORMAT(registerdate, '%Y-%m-%d') "
                                    + " BETWEEN '" + startDate + "' AND '" + endDate + "'"
                                    + " GROUP BY statusid, resolved;");
                        } else {
                            result = statement.executeQuery(
                                    " SELECT registerdate, statusid, COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE statusid= " + statusid
                                    + " AND DATE_FORMAT(registerdate, '%Y-%m-%d')"
                                    + " BETWEEN '" + startDate + "' AND '" + endDate + "'"
                                    + " GROUP BY statusid, resolved;");
                        }

                    } else {
                        if (statusid == 3) {
                            result = statement.executeQuery(
                                    "SELECT registerdate, statusid,"
                                    + " COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE resolved=" + resolved
                                    + " AND DATE_FORMAT(registerdate, '%Y-%m-%d')"
                                    + " BETWEEN '" + startDate + " 'AND' " + endDate + "'"
                                    + " GROUP BY statusid;");
                        } else {
                            result = statement.executeQuery(
                                    " SELECT registerdate, statusid,"
                                    + " COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE statusid= " + statusid + " AND resolved=" + resolved
                                    + " AND DATE_FORMAT(registerdate, '%Y-%m-%d') "
                                    + " BETWEEN '" + startDate + "' AND '" + endDate + "'"
                                    + "GROUP BY statusid;");
                        }
                    }
                    while (result.next()) {
                        if (result.getInt(2) == 1) {
                            if (result.getInt(4) == 0) {
                                this.lostLuggageUnresolved = result.getInt(3);
                            } else {
                                this.lostLuggageResolved = result.getInt(3);
                            }
                        } else if (result.getInt(2) == 2) {
                            if (result.getInt(4) == 0) {
                                this.foundLuggageUnresolved = result.getInt(3);
                            } else {
                                this.foundLuggageResolved = result.getInt(3);
                            }
                        }
                    }
                }
                break;
            }
            case (2): {
                try (Connection db = new ConnectMysqlServer().dbConnect()) {
                    Statement statement = db.createStatement();
                    ResultSet result;
                    if (resolved == 2) {
                        if (statusid == 3) {
                            result = statement.executeQuery(
                                    " SELECT registerdate, MONTH(registerdate) AS month, statusid,"
                                    + " COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE DATE_FORMAT(registerdate, '%Y-%m-%d') "
                                    + " BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%m-01') "
                                    + " AND LAST_DAY('" + endDate + "')"
                                    + " GROUP BY statusid, resolved;");
                        } else {
                            result = statement.executeQuery(
                                    " SELECT registerdate, MONTH(registerdate) AS month, statusid,"
                                    + " COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE statusid= " + statusid + " AND "
                                    + " DATE_FORMAT(registerdate, '%Y-%m-%d') "
                                    + " BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%m-01') "
                                    + " AND LAST_DAY('" + endDate + "')"
                                    + " GROUP BY statusid, resolved;");
                        }
                    } else {
                        if (statusid == 3) {
                            result = statement.executeQuery(
                                    " SELECT registerdate, MONTH(registerdate)"
                                    + " AS month, statusid, COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE resolved= " + resolved + ""
                                    + " AND DATE_FORMAT(registerdate, '%Y-%m-%d')"
                                    + " BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%m-01')"
                                    + " AND LAST_DAY('" + endDate + "')"
                                    + " GROUP BY statusid;");
                        } else {
                            result = statement.executeQuery(
                                    " SELECT registerdate, MONTH(registerdate)"
                                    + " AS month, statusid, COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE statusid= " + statusid + " AND resolved= " + resolved
                                    + " AND DATE_FORMAT(registerdate, '%Y-%m-%d') "
                                    + " BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%m-01') "
                                    + " AND LAST_DAY('" + endDate + "')"
                                    + " GROUP BY statusid;");
                        }
                    }
                    while (result.next()) {
                        if (result.getInt(3) == 1) {
                            if (result.getInt(5) == 0) {
                                this.lostLuggageUnresolved = result.getInt(4);
                            } else {
                                this.lostLuggageResolved = result.getInt(4);
                            }
                        } else if (result.getInt(3) == 2) {
                            if (result.getInt(5) == 0) {
                                this.foundLuggageUnresolved = result.getInt(4);
                            } else {
                                this.foundLuggageResolved = result.getInt(4);
                            }
                        }
                    }
                }
                break;
            }
            case (3): {
                try (Connection db = new ConnectMysqlServer().dbConnect()) {
                    Statement statement = db.createStatement();
                    ResultSet result;
                    if (resolved == 2) {
                        if (statusid == 3) {
                            result = statement.executeQuery(
                                    " SELECT registerdate, statusid, COUNT(*) as aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE DATE_FORMAT(registerdate, '%Y-%m-%d')"
                                    + " BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%01-01')"
                                    + " AND DATE_FORMAT('" + endDate + "', '%Y-%12-%31')"
                                    + " GROUP BY statusid, resolved;");
                        } else {
                            result = statement.executeQuery(
                                    " SELECT registerdate, statusid, COUNT(*) as aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE statusid= " + statusid
                                    + " AND DATE_FORMAT(registerdate, '%Y-%m-%d')"
                                    + " BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%01-01')"
                                    + " AND DATE_FORMAT('" + endDate + "', '%Y-%12-%31')"
                                    + " GROUP BY statusid, resolved;");
                        }
                    } else {
                        if (statusid == 3) {
                            result = statement.executeQuery(
                                    " SELECT registerdate, statusid, COUNT(*) as aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE resolved = " + resolved
                                    + " AND DATE_FORMAT(registerdate, '%Y-%m-%d')"
                                    + " BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%01-01')"
                                    + " AND DATE_FORMAT('" + endDate + "', '%Y-%12-%31')"
                                    + " GROUP BY statusid;");
                        } else {
                            result = statement.executeQuery(
                                    " SELECT registerdate, statusid, COUNT(*) as aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE statusid= " + statusid + " AND resolved = " + resolved
                                    + " AND DATE_FORMAT(registerdate, '%Y-%m-%d')"
                                    + " BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%01-01')"
                                    + " AND DATE_FORMAT('" + endDate + "', '%Y-%12-%31')"
                                    + " GROUP BY statusid;");
                        }

                    }
                    while (result.next()) {
                        if (result.getInt(2) == 1) {
                            if (result.getInt(4) == 0) {
                                this.lostLuggageUnresolved = result.getInt(3);
                            } else {
                                this.lostLuggageResolved = result.getInt(3);
                            }
                        } else if (result.getInt(2) == 2) {
                            if (result.getInt(4) == 0) {
                                this.foundLuggageUnresolved = result.getInt(3);
                            } else {
                                this.foundLuggageResolved = result.getInt(3);
                            }
                        }
                    }
                }
                break;
            }
            default: {
                this.lostLuggageResolved = 0;
                this.foundLuggageResolved = 0;
                this.foundLuggageUnresolved = 0;
                this.lostLuggageUnresolved = 0;
            }
        }
        this.totalLuggage = this.lostLuggageResolved + this.foundLuggageResolved
                + this.foundLuggageUnresolved + this.lostLuggageUnresolved;
    }

    /**
     * getDataPerEmployee(LocalDate startDate, LocalDate endDate, int interval,
     * User employee, int resolved, int type) get the data per employee from the
     * database to fill the piechart. By executing the queries the requested
     * data comes in the piechart.
     *
     * @param startDate is the LocalDate that resembles the starting date of the
     * data that needs to be pulled from the database.
     * @param endDate is the LocalDate that resembles the ending date of the
     * data that needs to be pulled from the database.
     * @param interval is the interval of the data that needs to be pulled from
     * the database as follow: 1 = day, 2 = month, 3 = year.
     * @param statusid is the type of luggage that needs to be pulled from the
     * database as follow: 1 = lost, 2 = found.
     * @param employee is the employee of who the data belongs to.
     * @param resolved is the resolved parameter that pulls data as follow: 0 =
     * unresolved, 1 = resolved, 2 = all.
     * @throws ClassNotFoundException when the class could not be found.
     * @throws SQLException when no connection with the Database could be
     * established.
     */
    public void getDataPerEmployee(LocalDate startDate, LocalDate endDate, int interval,
            User employee, int resolved, int statusid) throws ClassNotFoundException, SQLException {
        int employeeId = employee.getId();
        switch (interval) {
            case (1): {
                try (Connection db = new ConnectMysqlServer().dbConnect()) {
                    Statement statement = db.createStatement();
                    ResultSet result;
                    if (resolved == 2) {
                        if (statusid == 3) {
                            result = statement.executeQuery(
                                    " SELECT registerdate, statusid, COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE employeeid=" + employeeId
                                    + " AND DATE_FORMAT(registerdate, '%Y-%m-%d')"
                                    + " BETWEEN '" + startDate + "' AND '" + endDate + "'"
                                    + " GROUP BY statusid, resolved;");
                        } else {
                            result = statement.executeQuery(
                                    " SELECT registerdate, statusid, COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE statusid= " + statusid
                                    + " AND employeeid=" + employeeId
                                    + " AND DATE_FORMAT(registerdate, '%Y-%m-%d') "
                                    + " BETWEEN '" + startDate + "' AND '" + endDate + "'"
                                    + " GROUP BY statusid, resolved;");
                        }

                    } else {
                        if (statusid == 3) {
                            result = statement.executeQuery(
                                    " SELECT registerdate, statusid, COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE employeeid= " + employeeId
                                    + " AND resolved=" + resolved
                                    + " AND DATE_FORMAT(registerdate, '%Y-%m-%d')"
                                    + " BETWEEN '" + startDate + "' AND '" + endDate + "'"
                                    + " GROUP BY statusid;");
                        } else {
                            result = statement.executeQuery(
                                    " SELECT registerdate, statusid, COUNT(*) AS aantal, resolved"
                                    + " FROM luggage "
                                    + " WHERE statusid = " + statusid
                                    + " AND employeeid= " + employeeId
                                    + " AND resolved=" + resolved
                                    + " AND DATE_FORMAT(registerdate, '%Y-%m-%d')"
                                    + " BETWEEN '" + startDate + "' AND '" + endDate + "'"
                                    + " GROUP BY statusid;");
                        }
                    }
                    while (result.next()) {
                        if (result.getInt(2) == 1) {
                            if (result.getInt(4) == 0) {
                                this.lostLuggageUnresolved = result.getInt(3);
                            } else {
                                this.lostLuggageResolved = result.getInt(3);
                            }
                        } else if (result.getInt(2) == 2) {
                            if (result.getInt(4) == 0) {
                                this.foundLuggageUnresolved = result.getInt(3);
                            } else {
                                this.foundLuggageResolved = result.getInt(3);
                            }
                        }
                    }
                }
                break;
            }
            case (2): {
                try (Connection db = new ConnectMysqlServer().dbConnect()) {
                    Statement statement = db.createStatement();
                    ResultSet result;
                    if (resolved == 2) {
                        if (statusid == 3) {
                            result = statement.executeQuery(
                                    " SELECT registerdate, MONTH(registerdate)"
                                    + " AS month, statusid, COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE employeeId= " + employeeId
                                    + " AND DATE_FORMAT(registerdate, '%Y-%m-%d')"
                                    + " BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%m-01')"
                                    + " AND LAST_DAY('" + endDate + "')"
                                    + " GROUP BY statusid, resolved;");
                        } else {
                            result = statement.executeQuery(
                                    " SELECT registerdate, MONTH(registerdate)"
                                    + " AS month, statusid, COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE statusid= " + statusid
                                    + " AND employeeid= " + employeeId
                                    + " AND DATE_FORMAT(registerdate, '%Y-%m-%d')"
                                    + " BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%m-01')"
                                    + " AND LAST_DAY('" + endDate + "')"
                                    + " GROUP BY statusid, resolved;");
                        }
                    } else {
                        if (statusid == 3) {
                            result = statement.executeQuery(
                                    " SELECT registerdate, MONTH(registerdate)"
                                    + " AS month, statusid, COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE employeeid= " + employeeId
                                    + " AND resolved= " + resolved
                                    + " AND DATE_FORMAT(registerdate, '%Y-%m-%d')"
                                    + " BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%m-01')"
                                    + " AND LAST_DAY('" + endDate + "')"
                                    + " GROUP BY statusid;");
                        } else {
                            result = statement.executeQuery(
                                    " SELECT registerdate, MONTH(registerdate)"
                                    + " AS month, statusid, COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE statusid= " + statusid
                                    + " AND employeeid= " + employeeId
                                    + " AND resolved= " + resolved
                                    + " AND DATE_FORMAT(registerdate, '%Y-%m-%d')"
                                    + " BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%m-01')"
                                    + " AND LAST_DAY('" + endDate + "')"
                                    + " GROUP BY statusid;");
                        }
                    }
                    while (result.next()) {
                        if (result.getInt(3) == 1) {
                            if (result.getInt(5) == 0) {
                                this.lostLuggageUnresolved = result.getInt(4);
                            } else {
                                this.lostLuggageResolved = result.getInt(4);
                            }
                        } else if (result.getInt(3) == 2) {
                            if (result.getInt(5) == 0) {
                                this.foundLuggageUnresolved = result.getInt(4);
                            } else {
                                this.foundLuggageResolved = result.getInt(4);
                            }
                        }
                    }
                }
                break;
            }
            case (3): {
                try (Connection db = new ConnectMysqlServer().dbConnect()) {
                    Statement statement = db.createStatement();
                    ResultSet result;
                    if (resolved == 2) {
                        if (statusid == 3) {
                            result = statement.executeQuery(
                                    " SELECT registerdate, statusid, COUNT(*) as aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE employeeId= " + employeeId
                                    + " AND DATE_FORMAT(registerdate, '%Y-%m-%d')"
                                    + " BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%01-01')"
                                    + " AND DATE_FORMAT('" + endDate + "', '%Y-%12-%31')"
                                    + " GROUP BY statusid, resolved;");
                        } else {
                            result = statement.executeQuery(
                                    " SELECT registerdate, statusid, COUNT(*) as aantal, resolved "
                                    + " FROM luggage "
                                    + " WHERE statusid= " + statusid
                                    + " AND employeeId= " + employeeId
                                    + " AND DATE_FORMAT(registerdate, '%Y-%m-%d')"
                                    + " BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%01-01')"
                                    + " AND DATE_FORMAT('" + endDate + "', '%Y-%12-%31') "
                                    + " GROUP BY statusid, resolved;");
                        }
                    } else {
                        if (statusid == 3) {
                            result = statement.executeQuery(
                                    " SELECT registerdate, statusid, COUNT(*) as aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE employeeId= " + employeeId
                                    + " AND resolved= " + resolved
                                    + " AND DATE_FORMAT(registerdate, '%Y-%m-%d')"
                                    + " BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%01-01')"
                                    + " AND DATE_FORMAT('" + endDate + "', '%Y-%12-%31')"
                                    + " GROUP BY statusid;");
                        } else {
                            result = statement.executeQuery(
                                    " SELECT registerdate, statusid, COUNT(*) as aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE statusid= " + statusid
                                    + " AND employeeId= " + employeeId
                                    + " AND resolved = " + resolved
                                    + " AND DATE_FORMAT(registerdate, '%Y-%m-%d')"
                                    + " BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%01-01')"
                                    + " AND DATE_FORMAT('" + endDate + "', '%Y-%12-%31') "
                                    + " GROUP BY statusid;");
                        }

                    }
                    while (result.next()) {
                        if (result.getInt(2) == 1) {
                            if (result.getInt(4) == 0) {
                                this.lostLuggageUnresolved = result.getInt(3);
                            } else {
                                this.lostLuggageResolved = result.getInt(3);
                            }
                        } else if (result.getInt(2) == 2) {
                            if (result.getInt(4) == 0) {
                                this.foundLuggageUnresolved = result.getInt(3);
                            } else {
                                this.foundLuggageResolved = result.getInt(3);
                            }
                        }
                    }
                }
                break;
            }
            default: {
                this.lostLuggageResolved = 0;
                this.foundLuggageResolved = 0;
                this.foundLuggageUnresolved = 0;
                this.lostLuggageUnresolved = 0;
            }
        }
        this.totalLuggage = this.lostLuggageResolved + this.foundLuggageResolved
                + this.foundLuggageUnresolved + this.lostLuggageUnresolved;
    }

    /**
     * int getLostLuggageResolved() gets the data for the Lost Lugggage Resolved
     * for the Piechart.
     *
     * @return int lostLuggageResolved
     */
    public int getLostLuggageResolved() {
        return lostLuggageResolved;
    }

    /**
     * int getFoundLuggageResolved() gets the data for the Found Luggage
     * Resolved for the Piechart.
     *
     * @return int foudLuggageResolved
     */
    public int getFoundLuggageResolved() {
        return foundLuggageResolved;
    }

    /**
     * int getLostLuggageUnResolved() gets the data for the Lost Luggage
     * Unresolved for the Piechart.
     *
     * @return int lostLuggageUnresolved
     */
    public int getLostLuggageUnResolved() {
        return lostLuggageUnresolved;
    }

    /**
     * int getFoundLuggageUnResolved() gets the data for the Found Luggage
     * Unresolved for the Piechart.
     *
     * @return int foundLuggageUnresolved
     */
    public int getFoundLuggageUnResolved() {
        return foundLuggageUnresolved;
    }

    /**
     * int getTotalLuggage() gets the data for the Total Luggage for the
     * Piechart.
     *
     * @return int totalLuggage
     */
    public int getTotalLuggage() {
        return totalLuggage;
    }

}
