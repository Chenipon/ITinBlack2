package Fys.Controllers;

import Fys.Models.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class. This class controls the Account Overview screen 
 * including it's features.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class StatisticsTotalLuggageController implements Initializable {
    
    public static User currentUser;
    
    @FXML private Label lblUsername;
    @FXML private TextField startDate, endDate;
    @FXML private MenuButton ddwnLuggageType;
    @FXML private BarChart<String, Number> barChart;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(currentUser.getUsername());
    }
    
    @FXML
    private void btnPrintStatisticsEvent(ActionEvent event) {
        System.out.println("Print Statistics");
    }
    
    @FXML
    private void btnFilterEvent(ActionEvent event) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        barChart.setTitle("Total Luggage Lost");
        xAxis.setLabel("Date");
        yAxis.setLabel("Amount");
        
        XYChart.Series luggageLost = new XYChart.Series();
        luggageLost.setName("Total Luggage Lost");
        luggageLost.getData().add(new XYChart.Data("22-11-2015", 3));
        luggageLost.getData().add(new XYChart.Data("23-11-2015", 4));
        luggageLost.getData().add(new XYChart.Data("24-11-2015", 2));
        luggageLost.getData().add(new XYChart.Data("25-11-2015", 5));
        luggageLost.getData().add(new XYChart.Data("26-11-2015", 8));  
        
        XYChart.Series luggageFound = new XYChart.Series();
        luggageFound.setName("Total Luggage Found");
        luggageFound.getData().add(new XYChart.Data("22-11-2015", 1));
        luggageFound.getData().add(new XYChart.Data("23-11-2015", 2));
        luggageFound.getData().add(new XYChart.Data("24-11-2015", 2));
        luggageFound.getData().add(new XYChart.Data("25-11-2015", 3));
        luggageFound.getData().add(new XYChart.Data("26-11-2015", 1));  
        
        XYChart.Series luggageConnected = new XYChart.Series();
        luggageConnected.setName("Total Luggage Connected");
        luggageConnected.getData().add(new XYChart.Data("22-11-2015", 0));
        luggageConnected.getData().add(new XYChart.Data("23-11-2015", 0));
        luggageConnected.getData().add(new XYChart.Data("24-11-2015", 2));
        luggageConnected.getData().add(new XYChart.Data("25-11-2015", 1));
        luggageConnected.getData().add(new XYChart.Data("26-11-2015", 3));
        
        barChart.getData().addAll(luggageLost, luggageFound, luggageConnected);
    }
    
    @FXML
    private void ddwnLostLuggageEvent(ActionEvent event) {
        ddwnLuggageType.setText("Lost"); //Sets the text of the ddwn.
        ddwnLuggageType.setPrefWidth(95);
        System.out.println("Lost Luggage Selected");
    }
    
    @FXML
    private void ddwnFoundLuggageEvent(ActionEvent event) {
        ddwnLuggageType.setText("Found"); //Sets the text of the ddwn.
        ddwnLuggageType.setPrefWidth(95);
        System.out.println("Found Luggage Selected");
    }
    
    @FXML
    private void ddwnConnectedLuggageEvent(ActionEvent event) {
        ddwnLuggageType.setText("Connected"); //Sets the text of the ddwn.
        ddwnLuggageType.setPrefWidth(95);
        System.out.println("Connected Luggage Selected");
    }
    
    //-- DO NOT TOUCH ANY CODE BELOW THIS COMMENT. THESE ARE THE MENU BUTTONS. --
    @FXML
    private void btnLuggagePerEmployeeEvent(ActionEvent event) throws IOException {
        StatisticsLuggagePerEmployeeController.currentUser = currentUser;
        ((Node) event.getSource()).getScene().getWindow().hide();
        Parent parent = FXMLLoader.load(getClass().getResource("/Fys/Views/StatisticsLuggagePerEmployee.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        scene.getStylesheets().add("/Fys/Content/Css/stylesheet.css");
        stage.setScene(scene);
        stage.setTitle("Statistics - Employee");
        stage.show();
    }
    
    @FXML
    private void btnLogoutEvent(ActionEvent event) {
        System.out.println("Log out");
    }
    
}
