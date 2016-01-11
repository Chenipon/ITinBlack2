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
    private String resolved;

    /**
     * This method grabs the id of the Luggage, used to create the table.
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * This method sets the id of the Luggage, used to create the table.
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * This method gets the type of the Luggage, used to create the table.
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * This method sets the type of the Luggage, used to create the table.
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * This method gets the brand of the Luggage, used to create the table.
     *
     * @return
     */
    public String getBrand() {
        return brand;
    }

    /**
     * This method sets the brand of the Luggage, used to create the table.
     *
     * @param brand
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * This method gets the material of the Luggage, used to create the table.
     *
     * @return
     */
    public String getMaterial() {
        return material;
    }

    /**
     * This method sets the material of the Luggage, used to create the table.
     *
     * @param material
     */
    public void setMaterial(String material) {
        this.material = material;
    }

    /**
     * This method gets the color of the Luggage, used to create the table.
     *
     * @return
     */
    public String getColor() {
        return color;
    }

    /**
     * This method sets the color of the Luggage, used to create the table.
     *
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * This method gets the comment linked to the Luggage, used to create the
     * table.
     *
     * @return
     */
    public String getComment() {
        return comment;
    }

    /**
     * This method sets the comment linked to the Luggage, used to create the
     * table.
     *
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * This method gets the status linked to the Luggage, used to create the
     * table.
     *
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method sets the status linked to the Luggage, used to create the
     * table.
     *
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * This method gets the resolved status linked to the Luggage, used to
     * create the table.
     *
     * @return
     */
    public String getResolved() {
        return resolved;
    }

    /**
     * This method sets the resolved status linked to the Luggage, used to
     * create the table.
     *
     * @param resolved
     */
    public void setResolved(String resolved) {
        this.resolved = resolved;
    }

    /**
     * This method gets the list of Luggage, and the details After that has been
     * done, the method generates a table containing those details.
     *
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
                if (result.getBoolean(10)) {
                    foundLuggage.setResolved("True");
                } else {
                    foundLuggage.setResolved("False");
                }
                luggage.add(foundLuggage);
            }
        }
        return luggage;
    }

    /**
     * This method gets the list of Luggage, and the details After that has been
     * done, the method generates a table containing those details, this method
     * reacts to the user's search terms .
     *
     * @param resolved
     * @param searchTerm
     * @param status
     * @return
     * @throws Exception
     */
    public ObservableList<LuggageTabelView> getLuggageList(String searchTerm, String status, String resolved) throws Exception {
        ObservableList<LuggageTabelView> luggage = FXCollections.observableArrayList();
        int resolvedInt = 0;
        if (resolved.equals("True")) {
            resolvedInt = 1;
        }
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Status statusId = new Status().getStatusByName(status);
            Statement statement = db.createStatement();
            ResultSet result;
            if (statusId.getId() == 0) {
                if (resolved.equals("All")) {
                    result = statement.executeQuery("SELECT * FROM luggage WHERE type LIKE '%"
                            + searchTerm + "%' OR brand LIKE '%" + searchTerm + "%' OR material LIKE '%"
                            + searchTerm + "%' OR color LIKE '%" + searchTerm + "%' OR comments LIKE '%"
                            + searchTerm + "%'");
                } else {
                    result = statement.executeQuery("SELECT * FROM luggage WHERE (type LIKE '%"
                            + searchTerm + "%' OR brand LIKE '%" + searchTerm + "%' OR material LIKE '%"
                            + searchTerm + "%' OR color LIKE '%" + searchTerm + "%' OR comments LIKE '%"
                            + searchTerm + "%') AND resolved=" + resolvedInt);
                }
            } else if (resolved.equals("All")) {
                result = statement.executeQuery("SELECT * FROM luggage WHERE (type LIKE '%"
                        + searchTerm + "%' OR brand LIKE '%" + searchTerm + "%' OR material LIKE '%"
                        + searchTerm + "%' OR color LIKE '%" + searchTerm + "%' OR comments LIKE '%"
                        + searchTerm + "%') AND statusid=" + statusId.getId());
            } else if (searchTerm.equals("")) {
                if (resolved.equals("All")) {
                    result = statement.executeQuery("SELECT * FROM luggage WHERE statusid="
                            + statusId.getId());
                } else {
                    result = statement.executeQuery("SELECT * FROM luggage WHERE statusid="
                            + statusId.getId() + " AND resolved=" + resolvedInt);
                }
            } else {
                result = statement.executeQuery("SELECT * FROM luggage WHERE (type LIKE '%"
                        + searchTerm + "%' OR brand LIKE '%" + searchTerm + "%' OR material LIKE '%"
                        + searchTerm + "%' OR color LIKE '%" + searchTerm + "%' OR comments LIKE '%"
                        + searchTerm + "%') AND statusid=" + statusId.getId() + " AND resolved=" + resolvedInt);
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
                if (result.getBoolean(10)) {
                    foundLuggage.setResolved("True");
                } else {
                    foundLuggage.setResolved("False");
                }
                luggage.add(foundLuggage);
            }
        }
        return luggage;
    }
}
