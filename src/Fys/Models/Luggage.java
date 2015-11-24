package Fys.Models;

import Fys.Tools.ConnectMysqlServer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * public class Luggage contains the attributes, constructor and methods of
 * the object Luggage. This object holds the data of luggage that gets registered
 * or edited in the application.
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
    private Status status;

    /**
     * public Luggage() creates an empty Luggage object, which stores the data
     * of Luggage type, Luggage brand, Luggage material, Luggages color, Luggage comment,
     * Luggage statusId, Luggage registerDate and Luggage Status.
     */
    public Luggage() {
        this.type = "";
        this.brand = "";
        this.material = "";
        this.color = "";
        this.comment = "";
        this.statusId = 0;
        this.registerDate = null;
        this.status = new Status();
    }
    
    /**
     * public Luggage(String type, String brand, String material, String color, String comment) 
     * creates a Luggage object based of all the TextField input in the Luggage Add screens. 
     * statusId, registerDate and Status have to be added seperately using the methodes 
     * that Luggage() provides.
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
        this.status = new Status();
    }

    /**
     * public int getId()
     * @return Gets int id of Luggage Object. This is the Primary Key in the Database.
     */
    public int getId() {
        return id;
    }

    /**
     * public void setId(int id)
     * @param id Sets int id to Luggage Object "id". This is the Primary Key in the Database.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * public String getType()
     * @return Gets String type of Luggage Object
     */
    public String getType() {
        return type;
    }

    /**
     * public void setType(String type)
     * @param type Sets String type to Luggage Object "type"
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * public String getBrand()
     * @return Gets String brand of Luggage Object
     */
    public String getBrand() {
        return brand;
    }

    /**
     * public void setType(String brand)
     * @param brand Sets String brand to Luggage Object "brand"
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    /**
     * public String getMaterial()
     * @return Gets String material of Luggage Object
     */
    public String getMaterial() {
        return material;
    }

    /**
     * public void setMaterial(String material)
     * @param material Sets String material to Luggage Object "material"
     */
    public void setMaterial(String material) {
        this.material = material;
    }

    /**
     * public String getColor()
     * @return Gets String color of Luggage Object
     */
    public String getColor() {
        return color;
    }

    /**
     * public void setMaterial(String color)
     * @param color Sets String color to Luggage Object "color"
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * public String getComment()
     * @return Gets String comment of Luggage Object
     */
    public String getComment() {
        return comment;
    }

    /**
     * public void setMaterial(String comment)
     * @param comment Sets String comment to Luggage Object "comment"
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * public String getStatusId()
     * @return Gets int statusId of Luggage Object. This is the status of a piece of Luggage.
     */
    public int getStatusId() {
        return statusId;
    }

    /**
     * public String setStatusId(int statusId)
     * @param statusId Sets int statusId to Luggage Object "statusId". This is the status
     * of a piece of Luggage.
     */
    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    /**
     * public String getRegisterDate()
     * @return Gets String registerDate of Luggage Object. This is a String, and not
     * a Date object as MySQL and Java use 2 different types of Date formatting. To
     * format a Java Date to a MySQL Date, see the method convertJavaDateToSqlDate(Date date).
     */
    public String getRegisterDate() {
        return registerDate;
    }

    /**
     * public String getRegisterDate()
     * @param registerDate Sets String registerDate of Luggage Object. This is a String, and not
     * a Date object as MySQL and Java use 2 different types of Date formatting. To
     * format a Java Date to a MySQL Date, see the method convertJavaDateToSqlDate(Date date).
     */
    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    /**
     * public Status getStatus()
     * @return Status Gets a Status object which holds the statusId and statusName.
     */
    public Status getStatus() {
        return this.status;
    }

    /**
     * public Status getStatus()
     * @param status Sets Status status to Luggage Object "status" which holds 
     * the statusId and statusName.
     */
    public void setStatus(Status status) {
        this.status = status;
    }
    
    /**
     * public void insertLuggage(Luggage luggage)
     * This method inserts a Luggage object into a connected Database using the
     * ConnectMysqlServer class.
     * @param luggage Luggage object that needs to be imported into the Database
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void insertLuggage(Luggage luggage) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            String query = ("INSERT INTO luggage (type,brand,material,color,comments,registerdate,statusid) VALUES (?,?,?,?,?,?,?)");
            PreparedStatement preparedStatement = (PreparedStatement) db.prepareStatement(query);
            preparedStatement.setString(1, luggage.type);
            preparedStatement.setString(2, luggage.brand);
            preparedStatement.setString(3, luggage.material);
            preparedStatement.setString(4, luggage.color);
            preparedStatement.setString(5, luggage.comment);
            preparedStatement.setString(6, luggage.registerDate);
            preparedStatement.setInt(7, luggage.statusId);
            preparedStatement.executeUpdate();
        }
    }
    
    public void updateLuggage(Luggage luggage) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            String query = ("UPDATE luggage SET type = ?,brand = ?,material = ?,color = ?,comments = ?,statusid = ? WHERE id=" + luggage.getId());
            PreparedStatement preparedStatement = (PreparedStatement) db.prepareStatement(query);
            preparedStatement.setString(1, luggage.type);
            preparedStatement.setString(2, luggage.brand);
            preparedStatement.setString(3, luggage.material);
            preparedStatement.setString(4, luggage.color);
            preparedStatement.setString(5, luggage.comment);
            preparedStatement.setInt(6, luggage.statusId);
            preparedStatement.executeUpdate();
        }
    }
    
    /**
     * public int getStatusIdByName(String statusName)
     * @param statusName String of a status name of a piece of luggage.
     * @return the id of a status name, as defined in the Database "luggagestatus".
     * Returns 0 when the status name is not found in the Database.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public int getStatusIdByName(String statusName) throws ClassNotFoundException, SQLException {
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM luggagestatus WHERE statusname='" + statusName + "'");
            while (result.next()) {
                return result.getInt(1);
            }
            return 0;
        }
    }

    /**
     * public Luggage getLuggageById(int id)
     * @param id int id is the Primary Key of a Luggage object layed out in the Database.
     * @return Luggage object made out of the data from a row with the specified Primary Key.
     * @throws ClassNotFoundException
     * @throws SQLException
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
                this.registerDate = result.getString(7);
                this.statusId = result.getInt(8);
                this.status = new Status().getStatusById(this.statusId);
            }
        }
        return this;
    }

}
