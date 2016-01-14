package Fys.Controllers;

import Fys.Tools.Password;
import Fys.Models.User;
import Fys.Tools.Screen;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class. This class controls the Login screen including it's
 * features.
 *
 * @author Daan Befort, Jeffrey van der Lingen, IS106-2
 */
public class LoginController implements Initializable {

    private static Screen screen;
    
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;
    @FXML private Button btnLogin;
    
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
     * void initialize(URL url, ResourceBundel rb) is the automatic
     * initialization of the Controller when it's being fired due to a load of
     * it's connected scene.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    /**
     * void btnLoginAction(ActionEvent event) compares the parameters with the 
     * database rows to validate the user. With this the user is granted access
     * to the application.
     * 
     * @param event The event that is being fired by clicking the button.
     * @throws Exception when no connection with the Database could be
     * established.
     */
    @FXML
    private void btnLoginAction(ActionEvent event) throws Exception {
        try {
            if (!txtUsername.getText().equals("") && !txtPassword.getText().equals("")) {
                User user = new User().getUserByUsername(txtUsername.getText());
                if (user.getId() != 0) {
                    if (Password.check(txtPassword.getText(), user.getPassword())) {
                        if (!user.isActive()) {
                            lblError.setText("This account has been disabled");
                        } else {
                            ((Node) event.getSource()).getScene().getWindow().hide();
                            loadScreen(user);
                        }
                    } else {
                        lblError.setText("Invalid username or password");
                    }
                } else {
                    lblError.setText("Invalid username or password");
                }
            } else {
                txtUsername.setStyle("-fx-text-box-border: red;");
                txtPassword.setStyle("-fx-text-box-border: red;");
                lblError.setText("Username and/or password is not filled");
            }
        } catch (Exception e) {
            lblError.setText("No connection with the database could be established");
            btnLogin.setDisable(true);
        }
    }

    /**
     * void loadScreen(User user) decides what role to assign the user who is logging into the application.
     * With the role a specific corresponding scene is set.
     * 
     * @param user is the user that needs to be set in this class.
     * @throws Exception when no connection with the Database could be
     * established.
     */
    public void loadScreen(User user) throws Exception {
        switch (user.getRoleId()) {
            case (1): {
                AccountOverviewController.setUser(user);
                AccountOverviewController.setScreen(screen);
                screen.change("AccountOverview");
                break;
            }
            case (2): {
                StatisticsTotalLuggageController.setUser(user);
                StatisticsTotalLuggageController.setScreen(screen);
                screen.change("StatisticsTotalLuggage");
                break;
            }
            case (3): {
                LuggageOverviewController.setUser(user);
                LuggageOverviewController.setScreen(screen);
                screen.change("LuggageOverview");
                break;
            }
            default: {
                MainController.setUser(user);
                MainController.setScreen(screen);
                screen.change("Main");
                break;
            }
        }
    }

}
