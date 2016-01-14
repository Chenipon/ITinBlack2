package Fys.Controllers;

import Fys.Models.Luggage;
import Fys.Models.Status;
import Fys.Models.User;
import Fys.Tools.DateConverter;
import Fys.Tools.Screen;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class. This class controls the Add Luggage screen including
 * it's features.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class LuggageAddController implements Initializable {
    private static Screen screen;
    private static User currentUser;
    
    @FXML private Label lblUsername, lblErrorMessage;
    @FXML private MenuButton ddwnLuggageStatus;
    @FXML private TextField luggageType, luggageBrand, luggageMaterial, luggageColor;
    @FXML private TextArea luggageComments;
    
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
     * void ddwnLuggageLostEvent(ActionEvent event) fills in the MenuButton
     * with the text "Lost" and sets the size of the MenuButton to always
     * keep it the same. 
     *
     * @param event The event that is being fired by clicking the button.
     */
    @FXML
    private void ddwnLuggageLostEvent(ActionEvent event) {
        ddwnLuggageStatus.setText("Lost");
        ddwnLuggageStatus.setPrefWidth(200);
    }

    /**
     * void ddwnLuggageFoundEvent(ActionEvent event) fills in the MenuButton
     * with the text "Found" and sets the size of the MenuButton to always
     * keep it the same. 
     *
     * @param event The event that is being fired by clicking the button.
     */
    @FXML
    private void ddwnLuggageFoundEvent(ActionEvent event) {
        ddwnLuggageStatus.setText("Found");
        ddwnLuggageStatus.setPrefWidth(200);
    }

    /**
     * Void btnAddLuggageEvent(ActionEvent event) changes the scene of the Stage
     * to the Luggage Add scene. This scene is being used to add new luggage into
     * the database.
     * 
     * @param event The event that is being fired by clicking the button.
     * @throws ClassNotFoundException when the class could not be found.
     * @throws SQLException when no connection with the Database could be
     * established.
     * @throws IOException when the FXML file could not be loaded.
     */
    @FXML
    private void btnAddLuggageEvent(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
        if (!(luggageType.getText().equals("")
                || luggageBrand.getText().equals("")
                || luggageMaterial.getText().equals("")
                || luggageColor.getText().equals(""))) {
            if (!ddwnLuggageStatus.getText().equals("Luggage Status")) {
                lblErrorMessage.setText("");
                luggageType.setStyle("-fx-border-width: 0px;");
                luggageBrand.setStyle("-fx-border-width: 0px;");
                luggageMaterial.setStyle("-fx-border-width: 0px;");
                luggageColor.setStyle("-fx-border-width: 0px;");

                DateConverter dateConverter = new DateConverter();
                Status status = new Status().getStatusByName(ddwnLuggageStatus.getText());
                Luggage luggage = new Luggage(luggageType.getText(), luggageBrand.getText(),
                        luggageMaterial.getText(), luggageColor.getText(), luggageComments.getText());
                luggage.setStatusId(status.getId());
                luggage.setRegisterDate(dateConverter.getCurrentDateInSqlFormat());
                luggage.setStatus(status);
                luggage.setEmployeeId(currentUser.getId());
                luggage.insertLuggage(luggage);
                
                LuggageOverviewController.setUser(currentUser);
                LuggageOverviewController.setScreen(screen);
                screen.change("LuggageOverview");
            } else {
                lblErrorMessage.setText("Select a Luggage Status");
            }
        } else {
            lblErrorMessage.setText("One or more fields have not been filled out.");
            luggageType.setStyle("-fx-text-box-border: red;");
            luggageBrand.setStyle("-fx-text-box-border: red;");
            luggageMaterial.setStyle("-fx-text-box-border: red;");
            luggageColor.setStyle("-fx-text-box-border: red;");
        }
    }

    /**
     * void btnBackToOverviewEvent(ActionEvent event) returns the user back to
     * the Luggage Overview screen. This is the button next to the "Save
     * Changes" button.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws IOException when the FXML file could not be loaded.
     */
    @FXML
    private void btnBackToOverviewEvent(ActionEvent event) throws IOException {
        LuggageOverviewController.setUser(currentUser);
        LuggageOverviewController.setScreen(screen);
        screen.change("LuggageOverview");
    }

    /**
     * void btnLuggageEvent(ActionEvent event) is the button on the left of the
     * screen inside the red bar that returns to the Luggage Overview scene.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws IOException when the FXML file could not be loaded.
     */
    @FXML
    private void btnLuggageEvent(ActionEvent event) throws IOException {
        LuggageOverviewController.setUser(currentUser);
        LuggageOverviewController.setScreen(screen);
        screen.change("LuggageOverview");
    }

    /**
     * void btnCustomerEvent(ActionEvent event) is the button on the left of the
     * screen inside the red bar that returns to the Customer Overview scene.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws IOException when the FXML file could not be loaded.
     */
    @FXML
    private void btnCustomerEvent(ActionEvent event) throws IOException {
        CustomerOverviewController.setUser(currentUser);
        CustomerOverviewController.setScreen(screen);
        screen.change("CustomerOverview");
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
