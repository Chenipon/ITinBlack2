package Fys.Controllers;

import Fys.Models.User;
import Fys.Tools.Screen;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class. This class controls the Statistics Per Employee screen 
 * including it's features.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class StatisticsLuggagePerEmployeeController implements Initializable {
    
    @FXML private Label lblUsername;
    @FXML private TextField startDate;
    @FXML private TextField endDate;
    @FXML private MenuButton ddwnLuggageType;
    @FXML private MenuButton ddwnSelectEmployee; //This needs to contain data from the DB as ddwn.
    
    private final Screen SCREEN = new Screen();
    private static User currentUser;
    
    public static void getUser(User user) {
        currentUser = user;
    }
    
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
        System.out.println("Filter results");
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
    private void btnTotalLuggageEvent(ActionEvent event) throws IOException {
        StatisticsTotalLuggageController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("StatisticsTotalLuggage", "Statistics - Total");
    }
    
    @FXML
    private void btnLogoutEvent(ActionEvent event) {
        System.out.println("Log out");
    }
    
}
