package Fys.Tools;

import Fys.Models.User;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
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
     *
     */
    public PieChartData() {
        this.lostLuggageResolved = 0;
        this.foundLuggageResolved = 0;
        this.foundLuggageUnresolved = 0;
        this.foundLuggageUnresolved = 0;
        this.totalLuggage = 0;
    }

    /**
     *
     * @param startDate
     * @param endDate
     * @param interval
     * @param resolved
     * @param type
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void getData(LocalDate startDate, LocalDate endDate, int interval, int resolved, int type) throws ClassNotFoundException, SQLException {
        switch (interval) {
            case (1): {
                try (Connection db = new ConnectMysqlServer().dbConnect()) {
                    Statement statement = db.createStatement();
                    ResultSet result;
                    if (resolved == 2) {
                        if (type == 3) {
                            result = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN '" + startDate + "' AND '" + endDate + "'"
                                    + " GROUP BY statusid, resolved;");
                        } else {
                            result = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE statusid = " + type + " AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN '" + startDate + "' AND '" + endDate + "'"
                                    + " GROUP BY statusid, resolved;");
                        }

                    } else {
                        if (type == 3) {
                            result = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) AS aantal, resolved "
                                    + "FROM luggage "
                                    + "WHERE resolved=" + resolved + " AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN '" + startDate + "' AND '" + endDate + "'"
                                    + "GROUP BY statusid;");
                        } else {
                            result = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) AS aantal, resolved "
                                    + "FROM luggage "
                                    + "WHERE statusid = " + type + " AND resolved=" + resolved + " AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN '" + startDate + "' AND '" + endDate + "'"
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
                        if (type == 3) {
                            result = statement.executeQuery("SELECT registerdate, MONTH(registerdate) AS month, statusid, COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%m-01') AND LAST_DAY('" + endDate + "')"
                                    + " GROUP BY statusid, resolved;");
                        } else {
                            result = statement.executeQuery("SELECT registerdate, MONTH(registerdate) AS month, statusid, COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE statusid= " + type + " AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%m-01') AND LAST_DAY('" + endDate + "')"
                                    + " GROUP BY statusid, resolved;");
                        }
                    } else {
                        if (type == 3) {
                            result = statement.executeQuery("SELECT registerdate, MONTH(registerdate) AS month, statusid, COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE resolved= " + resolved + " AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%m-01') AND LAST_DAY('" + endDate + "')"
                                    + " GROUP BY statusid;");
                        } else {
                            result = statement.executeQuery("SELECT registerdate, MONTH(registerdate) AS month, statusid, COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE statusid= " + type + " AND resolved= " + resolved + " AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%m-01') AND LAST_DAY('" + endDate + "')"
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
                        if (type == 3) {
                            result = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) as aantal, resolved "
                                    + "FROM luggage "
                                    + "WHERE DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%01-01') AND DATE_FORMAT('" + endDate + "', '%Y-%12-%31') "
                                    + "GROUP BY statusid, resolved;");
                        } else {
                            result = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) as aantal, resolved "
                                    + "FROM luggage "
                                    + "WHERE statusid = " + type + " AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%01-01') AND DATE_FORMAT('" + endDate + "', '%Y-%12-%31') "
                                    + "GROUP BY statusid, resolved;");
                        }
                    } else {
                        if (type == 3) {
                            result = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) as aantal, resolved "
                                    + "FROM luggage "
                                    + "WHERE resolved = " + resolved + " AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%01-01') AND DATE_FORMAT('" + endDate + "', '%Y-%12-%31') "
                                    + "GROUP BY statusid;");
                        } else {
                            result = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) as aantal, resolved "
                                    + "FROM luggage "
                                    + "WHERE statusid =" + type + " AND resolved = " + resolved + " AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%01-01') AND DATE_FORMAT('" + endDate + "', '%Y-%12-%31') "
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

    public void getDataPerEmployee(LocalDate startDate, LocalDate endDate, int interval, User employee, int resolved, int type) throws ClassNotFoundException, SQLException {
        int employeeId = employee.getId();
        switch (interval) {
            case (1): {
                try (Connection db = new ConnectMysqlServer().dbConnect()) {
                    Statement statement = db.createStatement();
                    ResultSet result;
                    if (resolved == 2) {
                        if (type == 3) {
                            result = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE employeeid=" + employeeId + " AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN '" + startDate + "' AND '" + endDate + "'"
                                    + " GROUP BY statusid, resolved;");
                        } else {
                            result = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE statusid = " + type + " AND employeeid=" + employeeId + " AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN '" + startDate + "' AND '" + endDate + "'"
                                    + " GROUP BY statusid, resolved;");
                        }

                    } else {
                        if (type == 3) {
                            result = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) AS aantal, resolved "
                                    + "FROM luggage "
                                    + "WHERE employeeid= " + employeeId + " AND resolved=" + resolved + " AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN '" + startDate + "' AND '" + endDate + "'"
                                    + "GROUP BY statusid;");
                        } else {
                            result = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) AS aantal, resolved "
                                    + "FROM luggage "
                                    + "WHERE statusid = " + type + " AND employeeid= " + employeeId + " AND resolved=" + resolved + " AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN '" + startDate + "' AND '" + endDate + "'"
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
                        if (type == 3) {
                            result = statement.executeQuery("SELECT registerdate, MONTH(registerdate) AS month, statusid, COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE employeeId =" + employeeId + " AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%m-01') AND LAST_DAY('" + endDate + "')"
                                    + " GROUP BY statusid, resolved;");
                        } else {
                            result = statement.executeQuery("SELECT registerdate, MONTH(registerdate) AS month, statusid, COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE statusid= " + type + " AND employeeid =" + employeeId + " AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%m-01') AND LAST_DAY('" + endDate + "')"
                                    + " GROUP BY statusid, resolved;");
                        }
                    } else {
                        if (type == 3) {
                            result = statement.executeQuery("SELECT registerdate, MONTH(registerdate) AS month, statusid, COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE employeeid = " + employeeId + " AND resolved= " + resolved + " AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%m-01') AND LAST_DAY('" + endDate + "')"
                                    + " GROUP BY statusid;");
                        } else {
                            result = statement.executeQuery("SELECT registerdate, MONTH(registerdate) AS month, statusid, COUNT(*) AS aantal, resolved"
                                    + " FROM luggage"
                                    + " WHERE statusid= " + type + " AND employeeid = " + employeeId + " AND resolved= " + resolved + " AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%m-01') AND LAST_DAY('" + endDate + "')"
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
                        if (type == 3) {
                            result = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) as aantal, resolved "
                                    + "FROM luggage "
                                    + "WHERE employeeId= " + employeeId + " AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%01-01') AND DATE_FORMAT('" + endDate + "', '%Y-%12-%31') "
                                    + "GROUP BY statusid, resolved;");
                        } else {
                            result = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) as aantal, resolved "
                                    + "FROM luggage "
                                    + "WHERE statusid = " + type + " AND employeeId= " + employeeId + " AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%01-01') AND DATE_FORMAT('" + endDate + "', '%Y-%12-%31') "
                                    + "GROUP BY statusid, resolved;");
                        }
                    } else {
                        if (type == 3) {
                            result = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) as aantal, resolved "
                                    + "FROM luggage "
                                    + "WHERE employeeId= " + employeeId + " AND resolved = " + resolved + " AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%01-01') AND DATE_FORMAT('" + endDate + "', '%Y-%12-%31') "
                                    + "GROUP BY statusid;");
                        } else {
                            result = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) as aantal, resolved "
                                    + "FROM luggage "
                                    + "WHERE statusid =" + type + " AND employeeId= " + employeeId + " AND resolved = " + resolved + " AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%01-01') AND DATE_FORMAT('" + endDate + "', '%Y-%12-%31') "
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

    public int getLostLuggageResolved() {
        return lostLuggageResolved;
    }

    public int getFoundLuggageResolved() {
        return foundLuggageResolved;
    }

    public int getLostLuggageUnResolved() {
        return lostLuggageUnresolved;
    }

    public int getFoundLuggageUnResolved() {
        return foundLuggageUnresolved;
    }

    public int getTotalLuggage() {
        return totalLuggage;
    }

}
