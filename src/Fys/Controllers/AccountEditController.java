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
 * it's features.
 *
 * @author Daan Befort, IS106-2
 */
public class AccountEditController implements Initializable {

    private final Screen SCREEN = new Screen();
    private static User currentUser;
    private static User editUser;

    public static void getUser(User user) {
        currentUser = user;
    }

    public static void setEditUser(User user) {
        editUser = user;
    }

    @FXML private Label lblUsername;
    @FXML private Label lblError;
    @FXML private TextField txtUsername;
    @FXML private TextField txtPassword;
    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private ComboBox comboRoles;
    @FXML private CheckBox chckActive;

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
                    public String toString(Role object) {
                        return object.getRoleName();
                    }

                    @Override
                    public Role fromString(String string) {
                        // TODO Auto-generated method stub
                        return null;
                    }
                });
                comboRoles.getSelectionModel().select(editUser.getRole());
            } catch (Exception ex) {
                Logger.getLogger(AccountEditController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void btnSaveChangesEvent(ActionEvent event) throws Exception {
        txtUsername.setStyle("-fx-border-width: 0px;");
        Role newRole = (Role) comboRoles.getSelectionModel().getSelectedItem();
        User checkUser = new User().getUserByUsername(txtUsername.getText());
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
            AccountOverviewController.getUser(currentUser);
            ((Node) event.getSource()).getScene().getWindow().hide();
            SCREEN.change("AccountOverview", "Account Overview");
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
                AccountOverviewController.getUser(currentUser);
                ((Node) event.getSource()).getScene().getWindow().hide();
                SCREEN.change("AccountOverview", "Account Overview");
            } else {
                lblError.setText("Username already exists!");
                txtUsername.setStyle("-fx-text-box-border: red;");
            }
        }

    }

    @FXML
    private void btnBackToOverviewEvent(ActionEvent event) throws IOException {
        AccountOverviewController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("AccountOverview", "Account Overview");
    }

    @FXML
    private void boxDisableAccountEvent(ActionEvent event) {

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
