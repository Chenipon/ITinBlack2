package Fys.Controllers;

import Fys.Models.Connection;
import Fys.Models.Customer;
import Fys.Models.Luggage;
import Fys.Models.Status;
import Fys.Models.User;
import Fys.Tools.DateConverter;
import Fys.Tools.Screen;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class. This class controls the Edit Luggage screen including 
 * it's features.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class LuggageEditController implements Initializable {

    @FXML private Label lblUsername, lblErrorMessage, lblFirstName, lblLastName, lblGender, lblPhone, lblAddress, lblEmail;
    @FXML private TextField type, brand, material, color;
    @FXML private TextArea comments;
    @FXML private MenuButton ddwnStatus;
    @FXML private Button btnSelectCustomer;
    @FXML private AnchorPane paneCustomer;
    
    private final Screen SCREEN = new Screen();
    private static User currentUser;
    private static Luggage editLuggage;
    private static Customer connectedCustomer;
    private static Connection connection;
    
    public static void getUser(User user) {
        currentUser = user;
    }
    
    public static void setLuggage(Luggage luggage) {
        editLuggage = luggage;
    }
    
    public static void setCustomer(Customer customer) {
        connectedCustomer = customer;
    }
    
    public static void setConnection(Connection newConnection) {
        connection = newConnection;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(currentUser.getUsername());
        type.setText(editLuggage.getType());
        brand.setText(editLuggage.getBrand());
        material.setText(editLuggage.getMaterial());
        color.setText(editLuggage.getColor());
        comments.setText(editLuggage.getComment());
        
        try {
            if(editLuggage.checkIfLuggageIsConnected(editLuggage)) {
                connection = new Connection().getConnectionByLuggageId(editLuggage.getId());
                connectedCustomer = new Customer().getCustomerById(connection.getCustomerId());
                lblFirstName.setText(connectedCustomer.getFirstName());
                lblLastName.setText(connectedCustomer.getLastName());
                lblGender.setText(connectedCustomer.getGender());
                lblPhone.setText(connectedCustomer.getPhone());
                lblAddress.setText(connectedCustomer.getAddress());
                lblEmail.setText(connectedCustomer.getEmail());
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LuggageEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try { 
            ddwnStatus.setText(new Status().getStatusById(editLuggage.getStatusId()).getStatusName());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LuggageEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (ddwnStatus.getText().equals("Connected")) {
            btnSelectCustomer.setVisible(true);
            if (connectedCustomer != null) {
                paneCustomer.setVisible(true);
            }
        } else {
            paneCustomer.setVisible(false);
            btnSelectCustomer.setVisible(false);
        }
        
    } 
    
    @FXML
    private void ddwnStatusLostEvent(ActionEvent event) {
        ddwnStatus.setText("Lost");
        btnSelectCustomer.setVisible(false);
        ddwnStatus.setPrefWidth(200);
    }
    
    @FXML
    private void ddwnStatusFoundEvent(ActionEvent event) {
        ddwnStatus.setText("Found");
        btnSelectCustomer.setVisible(false);
        ddwnStatus.setPrefWidth(200);
    }
    
    @FXML
    private void ddwnStatusConnectedEvent(ActionEvent event) {
        ddwnStatus.setText("Connected");
        btnSelectCustomer.setVisible(true);
        ddwnStatus.setPrefWidth(200);
    }
    
    @FXML
    private void btnSelectCustomerEvent(ActionEvent event) throws IOException {
        LuggageSelectCustomerController.getUser(currentUser);
        LuggageSelectCustomerController.setLuggage(editLuggage);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("LuggageSelectCustomer", "Select Customer");
    }
    
    @FXML
    private void btnSaveChangesEvent(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
        if (!(type.getText().equals("") || brand.getText().equals("") || 
                material.getText().equals("") || color.getText().equals(""))) {
            if (ddwnStatus.getText().equals("Connected") && connectedCustomer == null) {
                lblErrorMessage.setText("Please select a customer to connect to the luggage before saving");
                return;
            }
            if (ddwnStatus.getText().equals("Connected")) {
                connection.setConnectionDate(new DateConverter().getCurrentDateInSqlFormat());
                connection.insertConnection(connection);
            } else {
                System.out.println("Flush Connection with ID: " + connection.getId());
                connection.deleteConnection(connection);
            }
            lblErrorMessage.setText("");
            type.setStyle("-fx-border-width: 0px;");
            brand.setStyle("-fx-border-width: 0px;");
            material.setStyle("-fx-border-width: 0px;");
            color.setStyle("-fx-border-width: 0px;");
            
            editLuggage.setType(type.getText());
            editLuggage.setBrand(brand.getText());
            editLuggage.setMaterial(material.getText());
            editLuggage.setColor(color.getText());
            editLuggage.setComment(comments.getText());
            editLuggage.setStatusId(new Status().getStatusByName(ddwnStatus.getText()).getId());
            editLuggage.updateLuggage(editLuggage);
        
            LuggageOverviewController.getUser(currentUser);
            ((Node) event.getSource()).getScene().getWindow().hide();
            SCREEN.change("LuggageOverview", "Luggage Overview");
        } else {
            lblErrorMessage.setText("Highlighted fields cannot be empty");
            type.setStyle("-fx-text-box-border: red;");
            brand.setStyle("-fx-text-box-border: red;");
            material.setStyle("-fx-text-box-border: red;");
            color.setStyle("-fx-text-box-border: red;");
        }
    }
    
    @FXML
    private void btnPrintProofEvent(ActionEvent event) {
        System.out.println("Print Proof of Registration");
    }
    
    @FXML
    private void btnBackToOverviewEvent(ActionEvent event) throws IOException {
        LuggageOverviewController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("LuggageOverview", "Luggage Overview");
    }
    
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
    private void btnLogoutEvent(ActionEvent event) throws IOException {
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("Login", "Login");
    }   
    
}
