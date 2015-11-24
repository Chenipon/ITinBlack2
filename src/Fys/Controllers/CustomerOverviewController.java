package Fys.Controllers;

import Fys.Models.User;
import Fys.Tools.Screen;
import Fys.Views.ViewModels.CustomerTabelView;
import Fys.Views.ViewModels.LuggageTabelView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class. This class controls the Customer Overview screen 
 * including it's features.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class CustomerOverviewController implements Initializable {
    
    @FXML private Label lblUsername, lblErrorMessage;
    @FXML private TextField lblSearch;
    @FXML private TableColumn colFirstName, colLastName, colGender, colPhone, colAddress, colEmail;
    @FXML private TableView tblCustomers;    
    
    private final Screen SCREEN = new Screen();
    private static User currentUser;
    
    public static void getUser(User user) {
        currentUser = user;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(currentUser.getUsername());
        colFirstName.setCellValueFactory(new PropertyValueFactory<CustomerTabelView, String>("firstname"));
        colLastName.setCellValueFactory(new PropertyValueFactory<CustomerTabelView, String>("lastname"));
        colGender.setCellValueFactory(new PropertyValueFactory<CustomerTabelView, String>("gender"));
        colPhone.setCellValueFactory(new PropertyValueFactory<CustomerTabelView, String>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<CustomerTabelView, String>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<CustomerTabelView, String>("email"));
        try {
            tblCustomers.setItems(getCustomerList());
        } catch (Exception ex) {
            Logger.getLogger(CustomerOverviewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ObservableList<CustomerTabelView> getCustomerList() throws Exception{
        ObservableList<CustomerTabelView> customerList= new CustomerTabelView().getCustomerList();
        return customerList;
    }
    
    @FXML
    private void btnLuggageEvent(ActionEvent event) throws IOException {
        LuggageOverviewController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("LuggageOverview", "Luggage Overview");
    }
    
    @FXML
    private void btnAddCustomerEvent(ActionEvent event) throws IOException {
        CustomerAddController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("CustomerAdd", "Add Customer");
    }
    
    @FXML
    private void btnSearchCustomerEvent(ActionEvent event) throws Exception {
        ObservableList<CustomerTabelView> customerList = new CustomerTabelView().getCustomerList(lblSearch.getText());
        tblCustomers.setItems(customerList);
    }
    
    @FXML
    private void btnLogoutEvent(ActionEvent event) {
        System.out.println("Log out");
    }
    
}
