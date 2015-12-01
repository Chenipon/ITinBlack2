package Fys.Controllers;

import Fys.Models.Customer;
import Fys.Models.User;
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
 * FXML Controller class. This class controls the Customer Edit screen including it's
 * features.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class CustomerEditController implements Initializable {

    @FXML private Label lblUsername, lblErrorMessage, lblRegisterDate, lblRegisterEmployee;
    @FXML private TextField firstName, lastName, phone, address, email;
    @FXML private MenuButton ddwnGender;
    
    private final Screen SCREEN = new Screen();
    private static User currentUser;
    private static Customer editCustomer;
    
    public static void getUser(User user) {
        currentUser = user;
    }
    
    public static void setCustomer(Customer customer)   {
        editCustomer = customer;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        firstName.setText(editCustomer.getFirstName());
        lastName.setText(editCustomer.getLastName());
        ddwnGender.setText(editCustomer.getGender());
        phone.setText(editCustomer.getPhone());
        address.setText(editCustomer.getAddress());
        email.setText(editCustomer.getEmail());
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
    private void btnSaveChangesEvent(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
        if (firstName.getText().equals("") || lastName.getText().equals("") || 
                phone.getText().equals("") || address.getText().equals("") || 
                email.getText().equals("")) {
            lblErrorMessage.setText("");
            editCustomer.setFirstName(firstName.getText());
            editCustomer.setLastName(lastName.getText());
            editCustomer.setGender(ddwnGender.getText());
            editCustomer.setPhone(phone.getText());
            editCustomer.setAddress(address.getText());
            editCustomer.setEmail(email.getText());
            editCustomer.updateCustomer(editCustomer);
            CustomerOverviewController.getUser(currentUser);
            ((Node) event.getSource()).getScene().getWindow().hide();
            SCREEN.change("CustomerOverview", "Customer Overview");
        } else {
            lblErrorMessage.setText("The highlighted fields can't be empty");
            
        }
    }
    
    @FXML
    private void btnBackToOverviewEvent(ActionEvent event) throws IOException {
        CustomerOverviewController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("CustomerOverview", "Customer Overview");
    }
    
    @FXML
    private void btnPrintProofEvent(ActionEvent event) {
        System.out.println("Print Proof of Registration");
    }
    
    @FXML
    private void btnLuggageEvent(ActionEvent event) throws Exception {
        LuggageOverviewController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("LuggageOverview", "Luggage Overview");
    }
    
    @FXML
    private void btnCustomerEvent(ActionEvent event) throws Exception {
        CustomerOverviewController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("CustomerOverview", "Customer Overview");
    }
    
    @FXML
    private void btnLogoutEvent(ActionEvent event) throws IOException {
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("Login", "Login");
    }
    
}
