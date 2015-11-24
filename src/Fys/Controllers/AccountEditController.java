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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class. This class controls the Edit Account screen including it's
 * features.
 *
 * @author Daan Befort, Jeffrey van der Lingen, IS106-2
 */
public class AccountEditController implements Initializable {
    
    private final Screen SCREEN = new Screen();
    private static User currentUser;
    private static User editUser;
    
    public static void getUser(User user)   {
        currentUser = user;
    }
    
    public static void setEditUser(User user)   {
        editUser = user;
    }
    
    @FXML private Label lblUsername;
    @FXML private TextField txtUsername;
    @FXML private TextField txtPassword;
    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private ComboBox comboRole;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(currentUser.getUsername());
        
        if (editUser != null) {
            txtUsername.setText(editUser.getUsername());
            txtFirstName.setText(editUser.getFirstname());
            txtLastName.setText(editUser.getLastname());
        }
    }
    
    @FXML
    private void btnSaveChangesEvent(ActionEvent event) {
        System.out.println("Changes Saved");
    }
    
    @FXML
    private void btnBackToOverviewEvent(ActionEvent event) throws IOException {
        AccountOverviewController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("AccountOverview", "Account Overview");
    }
    
    @FXML
    private void boxDisableAccountEvent(ActionEvent event) {
        System.out.println("Box checked");
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
