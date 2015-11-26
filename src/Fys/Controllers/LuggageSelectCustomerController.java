package Fys.Controllers;

import Fys.Models.Connection;
import Fys.Models.Customer;
import Fys.Models.Luggage;
import Fys.Models.User;
import Fys.Tools.Screen;
import Fys.Views.ViewModels.CustomerTabelView;
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
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.util.Callback;

/**
 * FXML Controller class. This class controls the Luggage Overview screen
 * including it's features.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class LuggageSelectCustomerController implements Initializable {

    @FXML private Label lblUsername, lblType, lblBrand, lblMaterial, lblColor;
    @FXML private TableView tblCustomers;
    @FXML private TableColumn colFirstName, colLastName, colGender, colPhone, colAddress, colEmail, colAction;
    @FXML private TextField lblSearch;
    
    private final Screen SCREEN = new Screen();
    private static User currentUser;
    private static Luggage selectedLuggage;
    
    public static void getUser(User user) {
        currentUser = user;
    }
    
    public static void setLuggage(Luggage luggage) {
        selectedLuggage = luggage;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(currentUser.getUsername());
        lblType.setText(selectedLuggage.getType());
        lblBrand.setText(selectedLuggage.getBrand());
        lblMaterial.setText(selectedLuggage.getMaterial());
        lblColor.setText(selectedLuggage.getColor());
        colFirstName.setCellValueFactory(new PropertyValueFactory<CustomerTabelView, String>("firstname"));
        colLastName.setCellValueFactory(new PropertyValueFactory<CustomerTabelView, String>("lastname"));
        colGender.setCellValueFactory(new PropertyValueFactory<CustomerTabelView, String>("gender"));
        colPhone.setCellValueFactory(new PropertyValueFactory<CustomerTabelView, String>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<CustomerTabelView, String>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<CustomerTabelView, String>("email"));
        colAction.setCellValueFactory(new PropertyValueFactory<CustomerTabelView, String>("action"));
        Callback<TableColumn<CustomerTabelView, String>, TableCell<CustomerTabelView, String>> printColumnCellFactory
                = new Callback<TableColumn<CustomerTabelView, String>, TableCell<CustomerTabelView, String>>() {

                    @Override
                    public TableCell call(final TableColumn param) {
                        final TableCell cell = new TableCell() {

                            @Override
                            public void updateItem(Object item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setText(null);
                                    setGraphic(null);
                                } else {
                                    final Button btnPrint = new Button("Select");
                                    btnPrint.setOnAction(new EventHandler<ActionEvent>() {

                                        @Override
                                        public void handle(ActionEvent event) {
                                            param.getTableView().getSelectionModel().select(getIndex());
                                            CustomerTabelView item = (CustomerTabelView) tblCustomers.getSelectionModel().getSelectedItem();
                                            if (item != null) {
                                                try {
                                                    Customer selectedCustomer = new Customer().getCustomerById(item.getId());
                                                    Connection connection = new Connection();
                                                    connection.setCustomerId(selectedCustomer.getId());
                                                    connection.setLuggageId(selectedLuggage.getId());
                                                    selectedLuggage.setStatusId(3);
                                                    LuggageEditController.setConnection(connection);
                                                    LuggageEditController.getUser(currentUser);
                                                    LuggageEditController.setCustomer(selectedCustomer);
                                                    LuggageEditController.setLuggage(selectedLuggage);
                                                    ((Node) event.getSource()).getScene().getWindow().hide();
                                                    SCREEN.change("LuggageEdit", "Edit Luggage");
                                                } catch (Exception ex) {
                                                    Logger.getLogger(CustomerOverviewController.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            }
                                        }
                                    });
                                    setGraphic(btnPrint);
                                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                                }
                            }
                        };
                        return cell;
                    }
                };
        colAction.setCellFactory(printColumnCellFactory);

        try {
            tblCustomers.setItems(getCustomerList());
        } catch (Exception ex) {
            Logger.getLogger(CustomerOverviewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ObservableList<CustomerTabelView> getCustomerList() throws Exception {
        ObservableList<CustomerTabelView> customerList = new CustomerTabelView().getCustomerList();
        return customerList;
    }

    @FXML
    private void btnSearchCustomerEvent(ActionEvent event) throws Exception {
        ObservableList<CustomerTabelView> customerList = new CustomerTabelView().getCustomerList(lblSearch.getText());
        tblCustomers.setItems(customerList);
    }
    
    @FXML
    private void btnBackToEditEvent(ActionEvent event) throws IOException {
        LuggageEditController.getUser(currentUser);
        LuggageEditController.setLuggage(selectedLuggage);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("LuggageEdit", "Edit Luggage");
    }

    //-- DO NOT TOUCH THESE BUTTONS BELOW, THEY ARE THE DEFAULT MENU ITEMS --
    @FXML
    private void btnCustomerEvent(ActionEvent event) throws IOException {
        CustomerOverviewController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("CustomerOverview", "Customer Overview");
    }

    @FXML
    private void btnAddLuggageEvent(ActionEvent event) throws IOException {
        LuggageAddController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("LuggageAdd", "Add Luggage");
    }

    @FXML
    private void btnLogoutEvent(ActionEvent event) throws IOException {
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("Login", "Login");
    }

}
