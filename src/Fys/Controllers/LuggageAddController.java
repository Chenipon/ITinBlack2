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
    
    public static void setScreen(Screen newScreen) {
        screen = newScreen;
    }
    
    public static void setUser(User user) {
        currentUser = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(currentUser.getUsername());
    }

    @FXML
    private void ddwnLuggageLostEvent(ActionEvent event) {
        ddwnLuggageStatus.setText("Lost");
        ddwnLuggageStatus.setPrefWidth(200);
    }

    @FXML
    private void ddwnLuggageFoundEvent(ActionEvent event) {
        ddwnLuggageStatus.setText("Found");
        ddwnLuggageStatus.setPrefWidth(200);
    }

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
                Luggage luggage = new Luggage(luggageType.getText(), luggageBrand.getText(),
                        luggageMaterial.getText(), luggageColor.getText(), luggageComments.getText());
                luggage.setStatusId(luggage.getStatusIdByName(ddwnLuggageStatus.getText()));
                luggage.setRegisterDate(dateConverter.getCurrentDateInSqlFormat());
                luggage.setStatus(new Status().getStatusByName(ddwnLuggageStatus.getText()));
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

    @FXML
    private void btnBackToOverviewEvent(ActionEvent event) throws IOException {
        LuggageOverviewController.setUser(currentUser);
        LuggageOverviewController.setScreen(screen);
        screen.change("LuggageOverview");
    }

    @FXML
    private void btnLuggageEvent(ActionEvent event) throws IOException {
        LuggageOverviewController.setUser(currentUser);
        LuggageOverviewController.setScreen(screen);
        screen.change("LuggageOverview");
    }

    @FXML
    private void btnCustomerEvent(ActionEvent event) throws IOException {
        CustomerOverviewController.setUser(currentUser);
        CustomerOverviewController.setScreen(screen);
        screen.change("CustomerOverview");
    }

    @FXML
    private void btnLogoutEvent(ActionEvent event) throws IOException {
        LoginController.setScreen(screen);
        ((Node) event.getSource()).getScene().getWindow().hide();
        screen.change("Login");
    }

}
