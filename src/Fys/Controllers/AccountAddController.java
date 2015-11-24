package Fys.Controllers;

import Fys.Models.Role;
import Fys.Models.User;
import Fys.Tools.DateConverter;
import Fys.Tools.Password;
import Fys.Tools.Screen;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;

/**
 * FXML Controller class. This class controls the Add Account screen including
 * it's features.
 *
 * @author Daan Befort, Jeffrey van der Lingen, IS106-2
 */
public class AccountAddController implements Initializable {

    private final Screen SCREEN = new Screen();
    private static User currentUser;

    public static void getUser(User user) {
        currentUser = user;
    }

    @FXML
    private Label lblUsername, lblErrorMessage;
    @FXML
    private MenuButton ddwnUserRole;
    @FXML
    private TextField username, password, firstName, lastName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(currentUser.getUsername());
    }

    @FXML
    private void ddwnRoleAdminEvent(ActionEvent event) {
        ddwnUserRole.setText("Administrator");
        ddwnUserRole.setPrefWidth(200);
    }

    @FXML
    private void ddwnRoleManagerEvent(ActionEvent event) {
        ddwnUserRole.setText("Manager");
        ddwnUserRole.setPrefWidth(200);
    }

    @FXML
    private void ddwnRoleEmployeeEvent(ActionEvent event) {
        ddwnUserRole.setText("Employee");
        ddwnUserRole.setPrefWidth(200);
    }

    @FXML
    private void btnAddAccountEvent(ActionEvent event) throws Exception {
        if (!(username.getText().equals("") || password.getText().equals("")
                || firstName.getText().equals("") || lastName.getText().equals(""))) {
            if (!(ddwnUserRole.getText().equals("User Role"))) {
                lblErrorMessage.setText("");
                username.setStyle("-fx-border-width: 0px;");
                password.setStyle("-fx-border-width: 0px;");
                firstName.setStyle("-fx-border-width: 0px;");
                lastName.setStyle("-fx-border-width: 0px;");
                
                DateConverter dateConverter = new DateConverter();
                User user = new User();
                Role role = new Role().getRoleByName(ddwnUserRole.getText());
                user.setUsername(username.getText());
                user.setPassword(Password.getSaltedHash(password.getText()));
                user.setFirstname(firstName.getText());
                user.setLastname(lastName.getText());
                user.setRoleId(role.getId());
                user.setRole(role);
                user.setRegisterDate(dateConverter.getCurrentDateInSqlFormat());
                user.setActive(true);
                User checkUserExist = new User().getUserByUsername(user.getUsername());
                if (checkUserExist.getId() == 0) {
                    user.insertUser(user);
                    ((Node) event.getSource()).getScene().getWindow().hide();
                    SCREEN.change("AccountOverview", "Account Overview");
                } else {
                    lblErrorMessage.setText("Username already exist!");
                }
            } else {
                username.setStyle("-fx-text-box-border: red;");
                lblErrorMessage.setText("Select a User Role");
            }
        } else {
            lblErrorMessage.setText("Not all fields have been filled out.");
            username.setStyle("-fx-text-box-border: red;");
            password.setStyle("-fx-text-box-border: red;");
            firstName.setStyle("-fx-text-box-border: red;");
            lastName.setStyle("-fx-text-box-border: red;");
        }
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
    private void btnLogoutEvent(ActionEvent event) throws Exception{
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("Login", "Login");
    }

}
