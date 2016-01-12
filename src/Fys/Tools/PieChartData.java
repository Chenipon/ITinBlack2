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
    private int lostLuggageUnResolved;
    private int foundLuggageUnResolved;

    /**
     *
     */
    public PieChartData() {
        this.lostLuggageResolved = 0;
        this.foundLuggageResolved = 0;
        this.foundLuggageUnResolved = 0;
        this.foundLuggageUnResolved = 0;
    }

    /**
     *
     * @param startDate
     * @param endDate
     * @param interval
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void getData(LocalDate startDate, LocalDate endDate, int interval) throws ClassNotFoundException, SQLException {
        switch (interval) {
            case (1): {
                try (Connection db = new ConnectMysqlServer().dbConnect()) {
                    Statement statement = db.createStatement();
                    ResultSet result = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) AS aantal "
                            + "FROM luggage "
                            + "WHERE DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN '" + startDate + "' AND '" + endDate + "'"
                            + "GROUP BY statusid;");
                    while (result.next()) {
                        if (result.getInt(2) == 1) {
                            this.lostLuggageResolved = result.getInt(3);
                        } else if (result.getInt(2) == 2) {
                            this.foundLuggageResolved = result.getInt(3);
                        }
                    }
                }
                break;
            }
            case (2): {
                try (Connection db = new ConnectMysqlServer().dbConnect()) {
                    Statement statement = db.createStatement();
                    ResultSet result = statement.executeQuery("SELECT registerdate, MONTH(registerdate) AS month, statusid, COUNT(*) AS aantal "
                            + "FROM luggage "
                            + "WHERE DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%m-01') AND LAST_DAY('" + endDate + "') "
                            + "GROUP BY statusid;");
                    while (result.next()) {
                        if (result.getInt(3) == 1) {
                            this.lostLuggageResolved = result.getInt(4);
                        } else if (result.getInt(3) == 2) {
                            this.foundLuggageResolved = result.getInt(4);
                        }
                    }
                }
                break;
            }
            case (3): {
                try (Connection db = new ConnectMysqlServer().dbConnect()) {
                    Statement statement = db.createStatement();
                    ResultSet result = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) as aantal "
                            + "FROM luggage "
                            + "WHERE DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%01-01') AND DATE_FORMAT('" + endDate + "', '%Y-%12-%31') "
                            + "GROUP BY statusid;");
                    while (result.next()) {
                        if (result.getInt(2) == 1) {
                            this.lostLuggageResolved = result.getInt(3);
                        } else if (result.getInt(2) == 2) {
                            this.foundLuggageResolved = result.getInt(3);
                        }
                    }
                }
                break;
            }
            default: {
                this.lostLuggageResolved = 0;
                this.foundLuggageResolved = 0;
                this.foundLuggageUnResolved = 0;
                this.lostLuggageUnResolved = 0;
            }
        }

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
                            result = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) AS aantal, resolved" +
                                " FROM luggage" +
                                " WHERE employeeid="+employeeId+" AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN '"+startDate+"' AND '"+endDate+"'" +
                                " GROUP BY statusid, resolved;");
                        } else {
                            result = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) AS aantal, resolved" +
                                " FROM luggage" +
                                " WHERE statusid = "+type+" AND employeeid="+employeeId+" AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN '"+startDate+"' AND '"+endDate+"'" +
                                " GROUP BY statusid, resolved;");
                        }
                        
                    } else{
                        if (type == 3) {
                            result = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) AS aantal, resolved "
                            + "FROM luggage "
                            + "WHERE employeeid= "+employeeId+" AND resolved="+resolved+" AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN '" + startDate + "' AND '" + endDate + "'"
                            + "GROUP BY statusid;");
                        } else {
                            result = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) AS aantal, resolved "
                            + "FROM luggage "
                            + "WHERE statusid = "+type+" AND employeeid= "+employeeId+" AND resolved="+resolved+" AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN '" + startDate + "' AND '" + endDate + "'"
                            + "GROUP BY statusid;");
                        }
                    }
                    while (result.next()) {
                        if (result.getInt(2) == 1) {
                            if (result.getInt(4) == 0) {
                                this.lostLuggageUnResolved = result.getInt(3);
                            } else{
                                this.lostLuggageResolved = result.getInt(3);
                            }
                        } else if (result.getInt(2) == 2) {
                            if (result.getInt(4) == 0) {
                                this.foundLuggageUnResolved = result.getInt(3);
                            } else{
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
                            result = statement.executeQuery("SELECT registerdate, MONTH(registerdate) AS month, statusid, COUNT(*) AS aantal, resolved" +
                                " FROM luggage" +
                                " WHERE employeeId ="+employeeId+" AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('"+startDate+"', '%Y-%m-01') AND LAST_DAY('"+endDate+"')" +
                                " GROUP BY statusid, resolved;");
                        } else {
                            result = statement.executeQuery("SELECT registerdate, MONTH(registerdate) AS month, statusid, COUNT(*) AS aantal, resolved" +
                                " FROM luggage" +
                                " WHERE statusid= "+type+" AND employeeid ="+employeeId+" AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('"+startDate+"', '%Y-%m-01') AND LAST_DAY('"+endDate+"')" +
                                " GROUP BY statusid, resolved;");
                        }
                    } else{
                        if (type == 3) {
                            result = statement.executeQuery("SELECT registerdate, MONTH(registerdate) AS month, statusid, COUNT(*) AS aantal, resolved"
                            + " FROM luggage"
                            + " WHERE employeeid = "+employeeId+" AND resolved= "+resolved+" AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%m-01') AND LAST_DAY('" + endDate + "')"
                            + " GROUP BY statusid;");
                        } else {
                            result = statement.executeQuery("SELECT registerdate, MONTH(registerdate) AS month, statusid, COUNT(*) AS aantal, resolved"
                            + " FROM luggage"
                            + " WHERE statusid= "+type+" AND employeeid = "+employeeId+" AND resolved= "+resolved+" AND DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%m-01') AND LAST_DAY('" + endDate + "')"
                            + " GROUP BY statusid;");
                        }
                    }
                    while (result.next()) {
                        if (result.getInt(2) == 1) {
                            if (result.getInt(4) == 0) {
                                this.lostLuggageUnResolved = result.getInt(3);
                            } else{
                                this.lostLuggageResolved = result.getInt(3);
                            }
                        } else if (result.getInt(2) == 2) {
                            if (result.getInt(4) == 0) {
                                this.foundLuggageUnResolved = result.getInt(3);
                            } else{
                                this.foundLuggageResolved = result.getInt(3);
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
                            result  = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) as aantal, resolved "
                            + "FROM luggage "
                            + "WHERE employeeId= "+employeeId+" DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%01-01') AND DATE_FORMAT('" + endDate + "', '%Y-%12-%31') "
                            + "GROUP BY statusid, resolved;");
                        } else {
                            result  = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) as aantal, resolved "
                            + "FROM luggage "
                            + "WHERE statusid = "+type+" AND employeeId= "+employeeId+" DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%01-01') AND DATE_FORMAT('" + endDate + "', '%Y-%12-%31') "
                            + "GROUP BY statusid, resolved;");
                        }
                    } else {
                        if (type == 3) {
                            result = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) as aantal, resolved "
                            + "FROM luggage "
                            + "WHERE employeeId= "+employeeId+" AND resolved = "+resolved+" DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%01-01') AND DATE_FORMAT('" + endDate + "', '%Y-%12-%31') "
                            + "GROUP BY statusid;");
                        } else {
                            result = statement.executeQuery("SELECT registerdate, statusid, COUNT(*) as aantal, resolved "
                            + "FROM luggage "
                            + "WHERE statusid ="+type+" AND employeeId= "+employeeId+" AND resolved = "+resolved+" DATE_FORMAT(registerdate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%01-01') AND DATE_FORMAT('" + endDate + "', '%Y-%12-%31') "
                            + "GROUP BY statusid;");
                        }
                        
                    }
                    while (result.next()) {
                        if (result.getInt(2) == 1) {
                            if (result.getInt(4) == 0) {
                                this.lostLuggageUnResolved = result.getInt(3);
                            } else{
                                this.lostLuggageResolved = result.getInt(3);
                            }
                        } else if (result.getInt(2) == 2) {
                            if (result.getInt(4) == 0) {
                                this.foundLuggageUnResolved = result.getInt(3);
                            } else{
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
                this.foundLuggageUnResolved = 0;
                this.lostLuggageUnResolved = 0;
            }
        }

    }

    public int getLostLuggageResolved() {
        return lostLuggageResolved;
    }

    public int getFoundLuggageResolved() {
        return foundLuggageResolved;
    }

    public int getLostLuggageUnResolved() {
        return lostLuggageUnResolved;
    }

    public int getFoundLuggageUnResolved() {
        return foundLuggageUnResolved;
    }
    
    
    
    

}
