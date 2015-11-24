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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class. This class controls the Login screen including it's
 * features.
 *
 * @author Daan Befort, Jeffrey van der Lingen, IS106-2
 */
public class LoginController implements Initializable {
    
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    @FXML
    private void btnLoginAction(ActionEvent event) throws Exception {
        if (!txtUsername.getText().equals("") && !txtPassword.getText().equals("")) {
            User user = new User().getUserByUsername(txtUsername.getText());
            if (user.getId() != 0) {
                if (Password.check(txtPassword.getText(), user.getPassword())) {
                    ((Node) event.getSource()).getScene().getWindow().hide();
                    loadScreen(user);
                } else {
                    lblError.setText("Invalid password");
                }
            } else {
                lblError.setText("Invalid username");
            }
        } else {
            txtUsername.setStyle("-fx-text-box-border: red;");
            txtPassword.setStyle("-fx-text-box-border: red;");
            lblError.setText("Username and/or password is not filled");
        }
    }

    public Stage loadScreen(User user) throws Exception {
        Screen screen = new Screen();
        switch (user.getRoleId()) {
            case (1): {
                AccountOverviewController.getUser(user);
                return screen.change("AccountOverview", "Account Overview");
            }
            case (2): {
                StatisticsTotalLuggageController.currentUser = user;
                return screen.change("StatisticsTotalLuggage", "Statistics - Total");
            }
            case (3): {
                LuggageOverviewController.getUser(user);
                return screen.change("LuggageOverview", "Luggage Overview");
            }
            default: {
                MainController.currentUser = user;
                return screen.change("Main", "Error");
            }
        }
    }

}
