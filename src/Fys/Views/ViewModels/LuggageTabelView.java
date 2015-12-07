package Fys.Views.ViewModels;

import Fys.Models.Status;
import Fys.Tools.ConnectMysqlServer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Jeffrey van der Lingen, IS106-2
 * @author Javadoc: John Ghatas, IS106-2
 */
public class LuggageTabelView {

    private int id;
    private String type;
    private String brand;
    private String material;
    private String color;
    private String comment;
    private String status;

    /**
     * This method grabs the id of the Luggage, used to create the table.
     * @see getId()
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * This method sets the id of the Luggage, used to create the table.
     * @see setId()
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * This method gets the type of the Luggage, used to create the table.
     * @see getType()
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * This method sets the type of the Luggage, used to create the table.
     * @see setType()
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * This method gets the brand of the Luggage, used to create the table.
     * @see getBrand()
     * @return
     */
    public String getBrand() {
        return brand;
    }

    /**
     * This method sets the brand of the Luggage, used to create the table.
     * @see setBrand()
     * @param brand
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * This method gets the material of the Luggage, used to create the table.
     * @see getMaterial()
     * @return
     */
    public String getMaterial() {
        return material;
    }

    /**
     * This method sets the material of the Luggage, used to create the table.
     * @see setMaterial()
     * @param material
     */
    public void setMaterial(String material) {
        this.material = material;
    }

    /**
     * This method gets the color of the Luggage, used to create the table.
     * @see getColor()
     * @return
     */
    public String getColor() {
        return color;
    }

    /**
     * This method sets the color of the Luggage, used to create the table.
     * @see setColor()
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * This method gets the comment linked to the Luggage, used to create the table.
     * @see getComment()
     * @return
     */
    public String getComment() {
        return comment;
    }

    /**
     * This method sets the comment linked to the Luggage, used to create the table.
     * @see setComment()
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    /**
     * This method gets the status linked to the Luggage, used to create the table.
     * @see getStatus()
     * @return
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * This method sets the status linked to the Luggage, used to create the table.
     * @see setStatus()
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * This method gets the list of Luggage, and the details
     * After that has been done, the method generates a table containing those details.
     * @see getLuggageList()
     * @return
     * @throws Exception
     */
    public ObservableList<LuggageTabelView> getLuggageList() throws Exception {
        ObservableList<LuggageTabelView> luggage = FXCollections.observableArrayList();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM luggage");
            while (result.next()) {
                LuggageTabelView foundLuggage = new LuggageTabelView();
                foundLuggage.setId(result.getInt(1));
                foundLuggage.setType(result.getString(2));
                foundLuggage.setBrand(result.getString(3));
                foundLuggage.setMaterial(result.getString(4));
                foundLuggage.setColor(result.getString(5));
                foundLuggage.setComment(result.getString(6));
                foundLuggage.setStatus(new Status().getStatusById(result.getInt(8)).getStatusName());
                luggage.add(foundLuggage);
            }
        }
        return luggage;
    }

    /**
     * This method gets the list of Luggage, and the details
     * After that has been done, the method generates a table containing those details, this method reacts to the user's search terms .
     * @see getLuggageList(String searchTerm, String status)
     * @param searchTerm
     * @param status
     * @return
     * @throws Exception
     */
    public ObservableList<LuggageTabelView> getLuggageList(String searchTerm, String status) throws Exception {
        ObservableList<LuggageTabelView> luggage = FXCollections.observableArrayList();
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Status statusId = new Status().getStatusByName(status);
            Statement statement = db.createStatement();
            ResultSet result;
            if (statusId.getId() == 0) {
                result = statement.executeQuery("SELECT * FROM luggage WHERE type LIKE '%"
                        + searchTerm + "%' OR brand LIKE '%" + searchTerm + "%' OR material LIKE '%"
                        + searchTerm + "%' OR color LIKE '%" + searchTerm + "%' OR comments LIKE '%"
                        + searchTerm + "%'");
            } else if (searchTerm.equals("")) {
                result = statement.executeQuery("SELECT * FROM luggage WHERE statusid=" + statusId.getId());
            } else {
                result = statement.executeQuery("SELECT * FROM luggage WHERE (type LIKE '%"
                        + searchTerm + "%' OR brand LIKE '%" + searchTerm + "%' OR material LIKE '%"
                        + searchTerm + "%' OR color LIKE '%" + searchTerm + "%' OR comments LIKE '%"
                        + searchTerm + "%') AND statusid=" + statusId.getId());
            }
            while (result.next()) {
                LuggageTabelView foundLuggage = new LuggageTabelView();
                foundLuggage.setId(result.getInt(1));
                foundLuggage.setType(result.getString(2));
                foundLuggage.setBrand(result.getString(3));
                foundLuggage.setMaterial(result.getString(4));
                foundLuggage.setColor(result.getString(5));
                foundLuggage.setComment(result.getString(6));
                foundLuggage.setStatus(new Status().getStatusById(result.getInt(8)).getStatusName());
                luggage.add(foundLuggage);
            }
        }
        return luggage;
    }
}
