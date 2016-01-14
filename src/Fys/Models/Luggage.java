package Fys.Models;

import Fys.Tools.ConnectMysqlServer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * public class Luggage contains the attributes, constructor and methods of the
 * object Luggage. This object holds the data of luggage that gets registered or
 * edited in the application.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class Luggage {

    private int id;
    private String type;
    private String brand;
    private String material;
    private String color;
    private String comment;
    private int statusId;
    private String registerDate;
    private int employeeId;
    private Status status;
    private boolean resolved;
    private String resolveDate;

    /**
     * Constructor Luggage() creates an empty Luggage object, which stores the
     * data of Luggage type, Luggage brand, Luggage material, Luggages color,
     * Luggage comment, Luggage statusId, Luggage registerDate and Luggage
     * Status.
     */
    public Luggage() {
        this.type = "";
        this.brand = "";
        this.material = "";
        this.color = "";
        this.comment = "";
        this.statusId = 0;
        this.registerDate = null;
        this.employeeId = 0;
        this.status = new Status();
        this.resolved = false;
        this.resolveDate = null;
    }

    /**
     * Constructor Luggage(String type, String brand, String material, String
     * color, String comment) creates a Luggage object based of all the
     * TextField input in the Luggage Add screens. statusId, registerDate and
     * Status have to be added seperately using the methodes that Luggage()
     * provides.
     *
     * @param type String type of Luggage
     * @param brand String brand of Luggage
     * @param material String material of Luggage
     * @param color String color of Luggage
     * @param comment String comment(s) of Luggage
     */
    public Luggage(String type, String brand, String material, String color,
            String comment) {
        this.type = type;
        this.brand = brand;
        this.material = material;
        this.color = color;
        this.comment = comment;
        this.statusId = 0;
        this.registerDate = null;
        this.employeeId = 0;
        this.status = new Status();
        this.resolved = false;
        this.resolveDate = null;
    }

    /**
     * public int getId() gets the id of a piece of Luggage.
     *
     * @return Gets int id of Luggage Object. This is the Primary Key in the
     * Database.
     */
    public int getId() {
        return id;
    }

    /**
     * public void setId(int id) sets the id of a piece of Luggage. This method
     * should not be used as it can cause issues in the database as this id
     * resembles the Primary Key in the database.
     *
     * @param id Sets int id to Luggage Object "id". This is the Primary Key in
     * the Database.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * public String getType() gets the type of a piece of Luggage.
     *
     * @return Gets String type of Luggage Object
     */
    public String getType() {
        return type;
    }

    /**
     * public void setType(String type) sets the type of a piece of Luggage.
     *
     * @param type Sets String type to Luggage Object "type"
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * public String getBrand() gets the brand of a piece of Luggage.
     *
     * @return Gets String brand of Luggage Object
     */
    public String getBrand() {
        return brand;
    }

    /**
     * public void setType(String brand) sets the brand of a piece of Luggage.
     *
     * @param brand Sets String brand to Luggage Object "brand"
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * public String getMaterial() gets the material of a piece of Luggage.
     *
     * @return Gets String material of Luggage Object
     */
    public String getMaterial() {
        return material;
    }

    /**
     * public void setMaterial(String material) sets the material of a piece of
     * Luggage.
     *
     * @param material Sets String material to Luggage Object "material"
     */
    public void setMaterial(String material) {
        this.material = material;
    }

    /**
     * public String getColor() gets the color of a piece of Luggage.
     *
     * @return Gets String color of Luggage Object
     */
    public String getColor() {
        return color;
    }

    /**
     * public void setMaterial(String color) sets the color of a piece of
     * Luggage.
     *
     * @param color Sets String color to Luggage Object "color"
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * public String getComment() gets the comment of a piece of Luggage.
     *
     * @return Gets String comment of Luggage Object
     */
    public String getComment() {
        return comment;
    }

    /**
     * public void setComment(String comment) sets the comment of a piece of
     * Luggage.
     *
     * @param comment Sets String comment to Luggage Object "comment"
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * public String getStatusId() gets the statusId of a piece of Luggage.
     * Statusid = 1 is lost, statusid = 2 is found.
     *
     * @return Gets int statusId of Luggage Object. This is the status of a
     * piece of Luggage.
     */
    public int getStatusId() {
        return statusId;
    }

    /**
     * public String setStatusId(int statusId) sets the statusId of a piece of
     * Luggage. Statusid = 1 is lost, statusid = 2 is found. Other ids are not
     * allowed.
     *
     * @param statusId Sets int statusId to Luggage Object "statusId". This is
     * the status of a piece of Luggage.
     */
    public void setStatusId(int statusId) {
        if (statusId == 1 || statusId == 2) {
            this.statusId = statusId;
        } else {
            throw new IllegalArgumentException("statusId can only be 1 or 2");
        }
    }

    /**
     * public String getRegisterDate() gets the register date of a piece of
     * Luggage. This is th date that the Luggage was added to the database.
     *
     * @return Gets String registerDate of Luggage Object. This is a String, and
     * not a Date object as MySQL and Java use 2 different types of Date
     * formatting. To format a Java Date to a MySQL Date, see the method
     * convertJavaDateToSqlDate(Date date).
     */
    public String getRegisterDate() {
        return registerDate;
    }

    /**
     * public String getRegisterDate() sets the register date of a piece of
     * Luggage. This is the date that the Luggage was added to the database.
     *
     * @param registerDate Sets String registerDate of Luggage Object. This is a
     * String, and not a Date object as MySQL and Java use 2 different types of
     * Date formatting. To format a Java Date to a MySQL Date, see the method
     * convertJavaDateToSqlDate(Date date).
     */
    public void setRegisterDate(String registerDate) {
        /* 
         * For a strange reason, MySQL adds ".0" after the date. 
         * This is being manually removed here by using a String splitter 
         */
        if (registerDate.contains(".")) {
            String[] split = registerDate.split("\\.");
            registerDate = split[0];
        }
        this.registerDate = registerDate;
    }

    /**
     * public int getEmployeeId() gets the employeeId of a Luggage object. This
     * is the employee that registered this piece of luggage.
     *
     * @return Gets int employeeid of Luggage Object. This is the employee id of
     * a piece of Luggage.
     */
    public int getEmployeeId() {
        return employeeId;
    }

    /**
     * public void setEmployeeId(int employeeId) sets the employeeId of a
     * Luggage object. This is the employee that registered this piece of
     * luggage.
     *
     * @param employeeId Sets int employeeId to Luggage Object "employeeId".
     * This is the employee id of a piece of Luggage.
     */
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * public Status getStatus() gets the Status of a Luggage object.
     *
     * @return Status Gets a Status object which holds the statusId and
     * statusName.
     */
    public Status getStatus() {
        return this.status;
    }

    /**
     * public Status getStatus() sets the Status of a Luggage object.
     *
     * @param status Sets Status status to Luggage Object "status" which holds
     * the statusId and statusName.
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * public boolean isResolved() gets the resolved state of this Luggage
     * object. This is the boolean that indicates if the Luggage has been
     * finished or not.
     *
     * @return true if the Luggage has been resolved, false otherwise.
     */
    public boolean isResolved() {
        return resolved;
    }

    /**
     * public void setResolved(boolean resolved) sets the resolved state of this
     * Luggage object. Thsi is the boolean that indicates if the Luggage has
     * been finished or not.
     *
     * @param resolved is the boolean that indicates whether or not a piece of
     * Luggage has been resolved.
     */
    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    /**
     * public String getResolveDate() gets the resolveDate. This is the date
     * that the resolved box has been checked.
     *
     * @return the resolveDate as a String.
     */
    public String getResolveDate() {
        return resolveDate;
    }

    /**
     * public void setResolveDate(String resolveDate) sets the resolveDate. This
     * is the date that the resolved box has been checked.
     *
     * @param resolveDate
     */
    public void setResolveDate(String resolveDate) {
        this.resolveDate = resolveDate;
    }

    /**
     * public void insertLuggage(Luggage luggage) This method inserts a Luggage
     * object into the database.
     *
     * @param luggage Luggage object that needs to be imported into the Database
     * @throws ClassNotFoundException when the jdbc could not be found.
     * @throws SQLException when an SQL exception error occurs.
     */
    public void insertLuggage(Luggage luggage) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            String query = ("INSERT INTO luggage (type,brand,material,color,comments,"
                    + "registerdate,statusid,employeeid,resolved,resolvedate) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement preparedStatement = (PreparedStatement) db.prepareStatement(query);
            preparedStatement.setString(1, luggage.type);
            preparedStatement.setString(2, luggage.brand);
            preparedStatement.setString(3, luggage.material);
            preparedStatement.setString(4, luggage.color);
            preparedStatement.setString(5, luggage.comment);
            preparedStatement.setString(6, luggage.registerDate);
            preparedStatement.setInt(7, luggage.statusId);
            preparedStatement.setInt(8, luggage.employeeId);
            preparedStatement.setBoolean(9, luggage.resolved);
            preparedStatement.setString(10, luggage.resolveDate);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * public void updateLuggage(Luggage luggage) This method updates already
     * registered luggage, which can be used in the events of changing of
     * luggage data.
     *
     * @param luggage Luggage luggage is the luggage that needs to be updated
     * @throws ClassNotFoundException when the jdbc could not be found.
     * @throws SQLException when an SQL exception error occurs.
     */
    public void updateLuggage(Luggage luggage) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            String query = ("UPDATE luggage SET type = ?,brand = ?,material = ?,color = ?,"
                    + "comments = ?,statusid = ?,resolved = ?,resolvedate = ? WHERE id="
                    + luggage.getId());
            PreparedStatement preparedStatement = (PreparedStatement) db.prepareStatement(query);
            preparedStatement.setString(1, luggage.type);
            preparedStatement.setString(2, luggage.brand);
            preparedStatement.setString(3, luggage.material);
            preparedStatement.setString(4, luggage.color);
            preparedStatement.setString(5, luggage.comment);
            preparedStatement.setInt(6, luggage.statusId);
            preparedStatement.setBoolean(7, resolved);
            preparedStatement.setString(8, resolveDate);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * public Luggage getLuggageById(int id) gets a piece of Luggage by it's id
     * from the database.
     *
     * @param id int id is the Primary Key of a Luggage object layed out in the
     * Database.
     * @return Luggage object made out of the data from a row with the specified
     * Primary Key.
     * @throws ClassNotFoundException when the jdbc could not be found.
     * @throws SQLException when an SQL exception error occurs.
     */
    public Luggage getLuggageById(int id) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM luggage WHERE id=" + id);
            while (result.next()) {
                this.id = result.getInt(1);
                this.type = result.getString(2);
                this.brand = result.getString(3);
                this.material = result.getString(4);
                this.color = result.getString(5);
                this.comment = result.getString(6);
                this.setRegisterDate(result.getString(7)); //.0 fix applied here.
                this.statusId = result.getInt(8);
                this.employeeId = result.getInt(10);
                this.status = new Status().getStatusById(this.statusId);
                this.resolved = result.getBoolean(10);
                this.resolveDate = result.getString(11);
            }
        }
        return this;
    }

    /**
     * public boolean checkIfLuggageIsConnected(Luggage luggage) checks if there
     * is a connection valid in the database containing a given piece of
     * Luggage.
     *
     * @param luggage The piece of Luggage that needs to be checked.
     * @return true if a connection has been found, false otherwise.
     * @throws ClassNotFoundException when the jdbc could not be found.
     * @throws SQLException when an SQL exception error occurs.
     */
    public boolean checkIfLuggageIsConnected(Luggage luggage)
            throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT id FROM connection"
                    + " WHERE luggageid=" + id);
            while (result.next()) {
                return true;
            }
            return false;
        }
    }

}
