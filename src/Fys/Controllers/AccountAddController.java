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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class. This class controls the Add Account screen including
 * it's features. This class is the Controller of the AccountAdd.fxml file and
 * contains everything regarding adding a new User to the application.
 *
 * @author Daan Befort, Jeffrey van der Lingen, IS106-2
 */
public class AccountAddController implements Initializable {

    private static Screen screen;
    private static User currentUser;

    @FXML private Label lblUsername, lblErrorMessage;
    @FXML private MenuButton ddwnUserRole;
    @FXML private TextField username, firstName, lastName;
    @FXML private PasswordField password;

    /**
     * void setScreen(Screen newScreen) sets the Screen element for the
     * Controller. This element contains the Stage object, used for switching
     * scenes.
     *
     * @param newScreen is the new Screen object that needs to be set in this
     * class.
     */
    public static void setScreen(Screen newScreen) {
        screen = newScreen;
    }

    /**
     * void setUser(User user) sets the user for the Controller. Ths is the
     * curent User that is used to log into the application, and is being used
     * for tracking of actions.
     *
     * @param user is the user that needs to be set in this class.
     */
    public static void setUser(User user) {
        currentUser = user;
    }

    /**
     * void initialize(URL url, ResourceBundel rb) is the automatic
     * initialization of the Controller when it's being fired due to a load of
     * it's connected scene.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(currentUser.getUsername());
    }

    /**
     * void ddwnRoleAdminEvent(ActionEvent event) fills in the MenuButton with
     * the text "Administrator" and sets the size of the MenuButton to always
     * keep it the same.
     *
     * @param event The event that is being fired by clicking the button.
     */
    @FXML
    private void ddwnRoleAdminEvent(ActionEvent event) {
        ddwnUserRole.setText("Administrator");
        ddwnUserRole.setPrefWidth(200);
    }

    /**
     * void ddwnRoleManagerEvent(ActionEvent event) fills in the MenuButton with
     * the text "Manager" and sets the size of the MenuButton to always keep it
     * the same.
     *
     * @param event The event that is being fired by clicking the button.
     */
    @FXML
    private void ddwnRoleManagerEvent(ActionEvent event) {
        ddwnUserRole.setText("Manager");
        ddwnUserRole.setPrefWidth(200);
    }

    /**
     * void ddwnRoleEmployeeEvent(ActionEvent event) fills in the MenuButton
     * with the text "Employee" and sets the size of the MenuButton to always
     * keep it the same.
     *
     * @param event The event that is being fired by clicking the button.
     */
    @FXML
    private void ddwnRoleEmployeeEvent(ActionEvent event) {
        ddwnUserRole.setText("Employee");
        ddwnUserRole.setPrefWidth(200);
    }

    /**
     * void btnAddAccountEvent(ActionEvent event) adds a new User into the
     * Database that can be used to log into the application. The data is being
     * collected from user input boxes.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws Exception when no connection with the Database could be
     * established.
     */
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
                    AccountOverviewController.setScreen(screen);
                    AccountOverviewController.setUser(currentUser);
                    screen.change("AccountOverview");
                } else {
                    username.setStyle("-fx-text-box-border: red;");
                    lblErrorMessage.setText("Username already exist!");
                }
            } else {
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

    /**
     * void btnBackToOverviewEvent(ActionEvent event) returns the user back to
     * the Account Overview screen. This is the button next to the "Save
     * Changes" button.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws IOException when the FXML file could not be loaded.
     */
    @FXML
    private void btnBackToOverviewEvent(ActionEvent event) throws IOException {
        AccountOverviewController.setUser(currentUser);
        AccountOverviewController.setScreen(screen);
        screen.change("AccountOverview");
    }

    /**
     * void btnAccountEvent(ActionEvent event) is the button on the left of the
     * screen inside the red bar that returns to the Account Overview scene.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws IOException when the FXML file could not be loaded.
     */
    @FXML
    private void btnAccountEvent(ActionEvent event) throws IOException {
        AccountOverviewController.setUser(currentUser);
        AccountOverviewController.setScreen(screen);
        screen.change("AccountOverview");
    }

    /**
     * void btnLogoutEvent(ActionEvent event) logs the current User out of the
     * application and displays the Login screen.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws IOException when the FXML file could not be loaded.
     */
    @FXML
    private void btnLogoutEvent(ActionEvent event) throws Exception {
        LoginController.setScreen(screen);
        ((Node) event.getSource()).getScene().getWindow().hide();
        screen.change("Login");
    }

}
