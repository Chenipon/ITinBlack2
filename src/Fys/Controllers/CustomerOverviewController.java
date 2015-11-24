package Fys.Controllers;

import Fys.Models.Customer;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * FXML Controller class. This class controls the Customer Overview screen
 * including it's features.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class CustomerOverviewController implements Initializable {

    @FXML private Label lblUsername, lblErrorMessage;
    @FXML private TextField lblSearch;
    @FXML private TableColumn colFirstName, colLastName, colGender, colPhone, colAddress, colEmail, colAction;
    @FXML private TableView tblCustomers;

    private final Screen SCREEN = new Screen();
    private static User currentUser;
    private static Customer updatedCustomer;

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
                                    final Button btnPrint = new Button("Edit");
                                    btnPrint.setOnAction(new EventHandler<ActionEvent>() {

                                        @Override
                                        public void handle(ActionEvent event) {
                                            param.getTableView().getSelectionModel().select(getIndex());
                                            CustomerTabelView item = (CustomerTabelView) tblCustomers.getSelectionModel().getSelectedItem();
                                            if (item != null) {
                                                try {
                                                    Customer editCustomer = new Customer().getCustomerById(item.getId());
                                                    CustomerEditController.getUser(currentUser);
                                                    CustomerEditController.setCustomer(editCustomer);
                                                    ((Node) event.getSource()).getScene().getWindow().hide();
                                                    SCREEN.change("CustomerEdit", "Edit Customer");
                                                } catch (Exception ex) {
                                                    Logger.getLogger(AccountOverviewController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AccountOverviewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ObservableList<CustomerTabelView> getCustomerList() throws Exception {
        ObservableList<CustomerTabelView> customerList = new CustomerTabelView().getCustomerList();
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
    private void btnLogoutEvent(ActionEvent event) throws IOException {
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("Login", "Login");
    }

}
