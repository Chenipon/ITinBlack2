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
    
    @FXML private Label lblUsername, lblErrorMessage;
    @FXML private TextField firstName, lastName, address, phone, email;
    @FXML private MenuButton ddwnGender;
    
    private final Screen SCREEN = new Screen();
    private static User currentUser;
    
    public static void getUser(User user) {
        currentUser = user;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(currentUser.getUsername());
    }
    
    @FXML
    private void ddwnGenderMaleEvent(ActionEvent event) {
        ddwnGender.setText("Male");
        ddwnGender.setPrefWidth(200);
    }
    
    @FXML
    private void ddwnGenderFemaleEvent(ActionEvent event) {
        ddwnGender.setText("Female");
        ddwnGender.setPrefWidth(200);
    }
    
    @FXML
    private void btnAddCustomerEvent(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
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
                customer.insertCustomer(customer);
                CustomerOverviewController.getUser(currentUser);
                ((Node) event.getSource()).getScene().getWindow().hide();
                SCREEN.change("CustomerOverview", "Customer Overview");
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
    
    @FXML
    private void btnBackToOverviewEvent(ActionEvent event) throws IOException {
        CustomerOverviewController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("CustomerOverview", "Customer Overview");
    }
    
    //-- DO NOT TOUCH ANY CODE BELOW THIS COMMENT, DEFAULT BUTTON EVENTS --
    @FXML
    private void btnLuggageEvent(ActionEvent event) throws IOException {
        LuggageOverviewController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("LuggageOverview", "Luggage Overview");
    }
    
    @FXML
    private void btnCustomerEvent(ActionEvent event) throws IOException {
        CustomerOverviewController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("CustomerOverview", "Customer Overview");
    }
    
    @FXML
    private void btnLogoutEvent(ActionEvent event) {
        System.out.println("Log out");
    }
    
}
