package Fys.Controllers;

import Fys.Models.Role;
import Fys.Models.User;
import Fys.Tools.Password;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

/**
 * FXML Controller class. This class controls the Edit Account screen including
 * it's features. The Edit Account screen is responsible for editing existing
 * Users that have been added to the application.
 *
 * @author Daan Befort, IS106-2
 */
public class AccountEditController implements Initializable {

    private static Screen screen;
    private static User currentUser;
    private static User editUser;

    @FXML
    private Label lblUsername, lblError;
    @FXML
    private TextField txtUsername, txtPassword, txtFirstName, txtLastName;
    @FXML
    private ComboBox comboRoles;
    @FXML
    private CheckBox chckActive;

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
     * void setEditUser(User user) sets the User object that is being edited in
     * this screen. This is a different User object then the current logged in
     * User.
     *
     * @param user is the user that is being edited.
     */
    public static void setEditUser(User user) {
        editUser = user;
    }

    /**
     * void initialize(URL url, ResourceBundel rb) is the automatic
     * initialization of the Controller when it's being fired due to a load of
     * it's connected scene.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(currentUser.getUsername());

        if (editUser != null) {
            txtUsername.setText(editUser.getUsername());
            txtFirstName.setText(editUser.getFirstname());
            txtLastName.setText(editUser.getLastname());
            if (editUser.isActive()) {
                chckActive.selectedProperty().set(true);
            } else {
                chckActive.selectedProperty().set(false);
            }
            try {
                comboRoles.setItems(new Role().getRoles());
                comboRoles.setConverter(new StringConverter<Role>() {
                    @Override
                    public String toString(Role role) {
                        return role.getRoleName();
                    }

                    @Override
                    public Role fromString(String string) {
                        return null;
                    }
                });
                comboRoles.getSelectionModel().select(editUser.getRole());
            } catch (Exception ex) {
                Logger.getLogger(AccountEditController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * void btnSaveChangesEvent(ActionEvent event) saves the changes made to
     * editUser and returns to the Account Overview scene.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws Exception when no connection with the Database could be
     * established.
     */
    @FXML
    private void btnSaveChangesEvent(ActionEvent event) throws Exception {
        txtUsername.setStyle("-fx-border-width: 0px;");
        Role newRole = (Role) comboRoles.getSelectionModel().getSelectedItem();
        User checkUser = new User().getUserByUsername(txtUsername.getText());
        if (txtUsername.getText().equals("") || txtFirstName.getText().equals("")
                || txtLastName.getText().equals("")) {
            lblError.setText("The highlighted fields can't be empty");
            txtUsername.setStyle("-fx-text-box-border: red;");
            txtFirstName.setStyle("-fx-text-box-border: red;");
            txtLastName.setStyle("-fx-text-box-border: red;");
        } else {
            if (editUser.getUsername().equals(txtUsername.getText())) {
                editUser.setFirstname(txtFirstName.getText());
                editUser.setLastname(txtLastName.getText());
                if (chckActive.selectedProperty().getValue()) {
                    editUser.setActive(true);
                } else {
                    editUser.setActive(false);
                }
                editUser.setRoleId(newRole.getId());
                if (!txtPassword.getText().equals("")) {
                    editUser.setPassword(Password.getSaltedHash(txtPassword.getText()));
                }
                editUser.updateUser(editUser);
                AccountOverviewController.setUser(currentUser);
                AccountOverviewController.setScreen(screen);
                screen.change("AccountOverview");
            } else {
                if (checkUser.getId() == 0) {
                    editUser.setUsername(txtUsername.getText());
                    editUser.setFirstname(txtFirstName.getText());
                    editUser.setLastname(txtLastName.getText());
                    editUser.setActive(chckActive.selectedProperty().getValue());
                    editUser.setRoleId(newRole.getId());
                    if (!txtPassword.getText().equals("")) {
                        editUser.setPassword(Password.getSaltedHash(txtPassword.getText()));
                    }
                    editUser.updateUser(editUser);
                    AccountOverviewController.setUser(currentUser);
                    AccountOverviewController.setScreen(screen);
                    screen.change("AccountOverview");
                } else {
                    lblError.setText("Username already exists!");
                    txtUsername.setStyle("-fx-text-box-border: red;");
                }
            }
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
    private void btnLogoutEvent(ActionEvent event) throws IOException {
        LoginController.setScreen(screen);
        ((Node) event.getSource()).getScene().getWindow().hide();
        screen.change("Login");
    }
}
