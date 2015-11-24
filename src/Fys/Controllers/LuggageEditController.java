package Fys.Controllers;

import Fys.Models.Luggage;
import Fys.Models.Status;
import Fys.Models.User;
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

/**
 * FXML Controller class. This class controls the Edit Luggage screen including 
 * it's features.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class LuggageEditController implements Initializable {

    @FXML private Label lblUsername, lblErrorMessage;
    @FXML private TextField type, brand, material, color;
    @FXML private TextArea comments;
    @FXML private MenuButton ddwnStatus;
    @FXML private Button btnSelectCustomer;
    
    private final Screen SCREEN = new Screen();
    private static User currentUser;
    private static Luggage editLuggage;
    
    public static void getUser(User user) {
        currentUser = user;
    }
    
    public static void setLuggage(Luggage luggage) {
        editLuggage = luggage;
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
            ddwnStatus.setText(new Status().getStatusById(editLuggage.getStatusId()).getStatusName());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LuggageEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (ddwnStatus.getText().equals("Connected")) {
            btnSelectCustomer.setVisible(true);
        } else {
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
    private void btnSelectCustomerEvent(ActionEvent event) {

    }
    
    @FXML
    private void btnSaveChangesEvent(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
        if (!(type.getText().equals("") || brand.getText().equals("") || 
                material.getText().equals("") || color.getText().equals(""))) {
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
