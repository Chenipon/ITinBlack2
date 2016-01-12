package Fys.Controllers;

import Fys.Models.User;
import Fys.Tools.Screen;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;

/**
 * FXML Controller class. This class controls the Main screen including it's
 * features. The "Main Controller" is the default template, and will only be
 * displayed in case a screen could not be loaded.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class MainController implements Initializable {

    private static User currentUser;
    private static Screen screen;

    @FXML private Label lblUsername;

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
     * void setUser(User user) sets the user for the Controller. This is the
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
     * void btnLogoutEvent(ActionEvent event) logs the current User out of the
     * application and displays the Login screen.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws Exception when the FXML file could not be loaded.
     */
    @FXML
    private void btnLogoutEvent(ActionEvent event) throws Exception {
        LoginController.setScreen(screen);
        ((Node) event.getSource()).getScene().getWindow().hide();
        screen.change("Login");
    }

}
