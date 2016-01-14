package Fys.Controllers;

import Fys.Models.Customer;
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
import javafx.scene.control.TextField;

/**
 * FXML Controller class. This class controls the Add Customer screen including it's
 * features.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class CustomerAddController implements Initializable {
    private static Screen screen;
    private static User currentUser;
    
    @FXML private Label lblUsername, lblErrorMessage;
    @FXML private TextField firstName, lastName, address, phone, email;
    @FXML private MenuButton ddwnGender;
    
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
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(currentUser.getUsername());
    }
    
    /**
     * void ddwnGenderMaleEvent(ActionEvent event) fills in the MenuButton
     * with the text "Male" and sets the size of the MenuButton to always
     * keep it the same. 
     *
     * @param event The event that is being fired by clicking the button.
     */
    @FXML
    private void ddwnGenderMaleEvent(ActionEvent event) {
        ddwnGender.setText("Male");
        ddwnGender.setPrefWidth(200);
    }
    
    /**
     * void ddwnGenderMaleEvent(ActionEvent event) fills in the MenuButton
     * with the text "Female" and sets the size of the MenuButton to always
     * keep it the same. 
     *
     * @param event The event that is being fired by clicking the button.
     */
    @FXML
    private void ddwnGenderFemaleEvent(ActionEvent event) {
        ddwnGender.setText("Female");
        ddwnGender.setPrefWidth(200);
    }
    
    /**
     * Void btnAddCustomerEvent(ActionEvent event) changes the scene of the Stage
     * to the Customer Add scene. This scene is being used to add new customers into
     * the database so that they can be notified when their luggage is found.
     * 
     * @param event The event that is being fired by clicking the button.
     * @throws ClassNotFoundException when the class could not be found.
     * @throws SQLException when no connection with the Database could be
     * established.
     * @throws IOException when the FXML file could not be loaded.
     */
    @FXML
    private void btnAddCustomerEvent(ActionEvent event) 
            throws ClassNotFoundException, SQLException, IOException {
        if (!(firstName.getText().equals("") || lastName.getText().equals("") || 
                phone.getText().equals("") || address.getText().equals(""))) {
            if (!ddwnGender.getText().equals("Select...")) {
                lblErrorMessage.setText("");
                firstName.setStyle("-fx-border-width: 0px;");
                lastName.setStyle("-fx-border-width: 0px;");
                phone.setStyle("-fx-border-width: 0px;");
                address.setStyle("-fx-border-width: 0px;");
                
                Customer customer = new Customer();
                customer.setFirstName(firstName.getText());
                customer.setLastName(lastName.getText());
                customer.setGender(ddwnGender.getText());
                customer.setPhone(phone.getText());
                customer.setAddress(address.getText());
                customer.setEmail(email.getText());
                customer.setRegisterDate(new DateConverter().getCurrentDateInSqlFormat());
                customer.setEmployeeId(currentUser.getId());
                customer.insertCustomer(customer);
                
                CustomerOverviewController.setUser(currentUser);
                CustomerOverviewController.setScreen(screen);
                screen.change("CustomerOverview");
            } else {
                lblErrorMessage.setText("Please select a gender");
            }
        } else {
            lblErrorMessage.setText("Please fill out the required fields");
            firstName.setStyle("-fx-text-box-border: red;");
            lastName.setStyle("-fx-text-box-border: red;");
            phone.setStyle("-fx-text-box-border: red;");
            address.setStyle("-fx-text-box-border: red;");
        }
    }
    
    /**
     * void btnBackToOverviewEvent(ActionEvent event) returns the user back to
     * the Customer Overview screen. This is the button next to the "Save
     * Changes" button.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws IOException when the FXML file could not be loaded.
     */
    @FXML
    private void btnBackToOverviewEvent(ActionEvent event) throws IOException {
        CustomerOverviewController.setUser(currentUser);
        CustomerOverviewController.setScreen(screen);
        screen.change("CustomerOverview");
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
