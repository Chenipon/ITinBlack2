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
 */
public class LuggageTabelView {

    private int id;
    private String type;
    private String brand;
    private String material;
    private String color;
    private String comment;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

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
