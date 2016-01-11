package Fys.Tools;

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

    private int lostLuggage;
    private int foundLuggage;
    private int connectedLuggage;

    /**
     *
     */
    public PieChartData() {
        this.lostLuggage = 0;
        this.foundLuggage = 0;
        this.connectedLuggage = 0;
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
                            this.lostLuggage = result.getInt(3);
                        } else if (result.getInt(2) == 2) {
                            this.foundLuggage = result.getInt(3);
                        }
                    }
                }
                try (Connection db = new ConnectMysqlServer().dbConnect()) {
                    Statement statement = db.createStatement();
                    ResultSet result = statement.executeQuery("SELECT connectiondate, COUNT(*) AS aantal "
                            + "FROM connection "
                            + "WHERE DATE_FORMAT(connectiondate, '%Y-%m-%d') BETWEEN '" + startDate + "' AND '" + endDate + "'");
                    while (result.next()) {
                        this.connectedLuggage = result.getInt(2);
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
                            this.lostLuggage = result.getInt(4);
                        } else if (result.getInt(3) == 2) {
                            this.foundLuggage = result.getInt(4);
                        }
                    }
                }
                try (Connection db = new ConnectMysqlServer().dbConnect()) {
                    Statement statement = db.createStatement();
                    ResultSet result = statement.executeQuery("SELECT connectiondate, MONTH(connectiondate) AS month, COUNT(*) AS aantal "
                            + "FROM connection "
                            + "WHERE DATE_FORMAT(connectiondate, '%Y-%m-01') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%m-01') AND LAST_DAY('" + endDate + "')");
                    while (result.next()) {
                        this.connectedLuggage = result.getInt(2);
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
                            this.lostLuggage = result.getInt(3);
                        } else if (result.getInt(2) == 2) {
                            this.foundLuggage = result.getInt(3);
                        }
                    }
                }
                try (Connection db = new ConnectMysqlServer().dbConnect()) {
                    Statement statement = db.createStatement();
                    ResultSet result = statement.executeQuery("SELECT connectiondate, COUNT(*) as aantal "
                            + "FROM connection "
                            + "WHERE DATE_FORMAT(connectiondate, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startDate + "', '%Y-%01-01') AND DATE_FORMAT('" + endDate + "', '%Y-%12-%31');");
                    while (result.next()) {
                        this.connectedLuggage = result.getInt(2);
                    }
                }
                break;
            }
            default: {
                this.lostLuggage = 0;
                this.foundLuggage = 0;
                this.connectedLuggage = 0;
            }
        }

    }

    /**
     *
     * @return
     */
    public int getLostLuggage() {
        return lostLuggage;
    }

    /**
     *
     * @return
     */
    public int getFoundLuggage() {
        return foundLuggage;
    }

    /**
     *
     * @return
     */
    public int getConnectedLuggage() {
        return connectedLuggage;
    }

}
