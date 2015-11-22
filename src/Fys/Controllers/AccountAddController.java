package Fys.Controllers;

import Fys.Models.User;
import Fys.Tools.Screen;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;

/**
 * FXML Controller class. This class controls the Add Account screen including it's
 * features.
 *
 * @author Daan Befort, Jeffrey van der Lingen, IS106-2
 */
public class AccountAddController implements Initializable {
    
    private final Screen SCREEN = new Screen();
    private static User currentUser;
    
    public static void getUser(User user)   {
        currentUser = user;
    }
    
    @FXML private Label lblUsername;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(currentUser.getUsername());
    }
    
    @FXML
    private void btnAddAccountEvent(ActionEvent event) {
        
    }
    
    @FXML
    private void btnBackToOverviewEvent(ActionEvent event) throws IOException {
        AccountOverviewController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("AccountOverview", "Account Overview");
    }
    
    @FXML
    private void btnAccountEvent(ActionEvent event) throws IOException {
        AccountOverviewController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("AccountOverview", "Account Overview");
    }    
    
    @FXML
    private void btnLogoutEvent(ActionEvent event) {
        System.out.println("Log out");
    }
    
}
