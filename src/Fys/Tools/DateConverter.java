package Fys.Tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * This class contains 3 dateconverters that can be used to convert between the
 * dates of java.sql.Date and java.util.Date.
 * @author Jeffrey van der Lingen, IS106-2
 */
public class DateConverter {

    /**
     * public String getCurrentDateInSqlFormat()
     *
     * @return String of the CURRENT Java Date formatted in MySQL DateTime.
     */
    public String getCurrentDateInSqlFormat() {
        SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sqlFormat.format(new java.util.Date());
    }

    /**
     * public String convertJavaDataToSqlDate(java.util.Date date)
     *
     * @param date Date in java.util.Date format.
     * @return String of the parameter java.util.Date formatted in MySQL
     * DateTime.
     */
    public String convertJavaDateToSqlDate(java.util.Date date) {
        SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sqlFormat.format(date);
    }

    /**
     * public java.util.Date convertSqlDateToJavaDate(String sqlDate)
     *
     * @param sqlDate String of MySQL formatted Date.
     * @return java.util.Date Object of a MySQL Date in a String format.
     * @throws ParseException
     */
    public java.util.Date convertSqlDateToJavaDate(String sqlDate) throws ParseException {
        SimpleDateFormat javaFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return javaFormat.parse(sqlDate);
    }
}
