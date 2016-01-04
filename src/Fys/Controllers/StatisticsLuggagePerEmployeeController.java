package Fys.Controllers;

import Fys.Models.Role;
import Fys.Models.User;
import Fys.Tools.Screen;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.util.StringConverter;

/**
 * FXML Controller class. This class controls the Statistics Per Employee screen 
 * including it's features.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class StatisticsLuggagePerEmployeeController implements Initializable {
    private static Screen screen;
    private static User currentUser;
    
    @FXML private Label lblUsername;
    @FXML private DatePicker startDate, endDate;
    @FXML private MenuButton ddwnLuggageType;
    @FXML private ComboBox comboEmployee;
    
    public static void setScreen(Screen newScreen) {
        screen = newScreen;
    }
    
    public static void setUser(User user) {
        currentUser = user;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(currentUser.getUsername());
        
        try {
                comboEmployee.setItems(new User().getUsers());
                comboEmployee.setConverter(new StringConverter<User>() {
                    @Override
                    public String toString(User user) {
                        return user.getUsername();
                    }

                    @Override
                    public User fromString(String string) {
                        return null;
                    }
                });
                
            } catch (Exception ex) {
                Logger.getLogger(StatisticsLuggagePerEmployeeController.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        ddwnLuggageType.setText("Lost");
        ddwnLuggageType.setPrefWidth(95);
    }
    
    @FXML
    private void ddwnFoundLuggageEvent(ActionEvent event) {
        ddwnLuggageType.setText("Found");
        ddwnLuggageType.setPrefWidth(95);
    }
    
    @FXML
    private void ddwnConnectedLuggageEvent(ActionEvent event) {
        ddwnLuggageType.setText("Connected");
        ddwnLuggageType.setPrefWidth(95);
    }
    
    @FXML
    private void btnTotalLuggageEvent(ActionEvent event) throws IOException {
        StatisticsTotalLuggageController.setUser(currentUser);
        StatisticsTotalLuggageController.setScreen(screen);
        screen.change("StatisticsTotalLuggage");
    }
    
    @FXML
    private void btnLogoutEvent(ActionEvent event) throws IOException {
        LoginController.setScreen(screen);
        ((Node) event.getSource()).getScene().getWindow().hide();
        screen.change("Login");
    }
    
}
