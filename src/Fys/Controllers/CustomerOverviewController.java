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
    private static Screen screen;
    private static User currentUser;
    
    @FXML private Label lblUsername, lblErrorMessage;
    @FXML private TextField lblSearch;
    @FXML private TableColumn colFirstName, colLastName, colGender, colPhone, colAddress, colEmail, colAction;
    @FXML private TableView tblCustomers;
    
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
                                                    CustomerEditController.setUser(currentUser);
                                                    CustomerEditController.setCustomer(editCustomer);
                                                    CustomerEditController.setScreen(screen);
                                                    screen.change("CustomerEdit");
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

    /**
     * ObservableList<CustomerTabelView> getCustomerList() returns an ObservableList
     * that can be used in the TableView.
     *
     * @return an ObservableList containing the information that is added into
     * the TableView.
     * @throws Exception when no connection with the Database could be
     * established.
     */
    public ObservableList<CustomerTabelView> getCustomerList() throws Exception {
        ObservableList<CustomerTabelView> customerList = new CustomerTabelView().getCustomerList();
        return customerList;
    }
    
    /**
     * void btnAccountEvent(ActionEvent event) is the button on the left of the
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
     * void btnAccountEvent(ActionEvent event) is the button on the left of the
     * screen inside the red bar that returns to the Customer Overview scene.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws IOException when the FXML file could not be loaded.
     */
    @FXML
    private void btnAddCustomerEvent(ActionEvent event) throws IOException {
        CustomerAddController.setUser(currentUser);
        CustomerAddController.setScreen(screen);
        screen.change("CustomerAdd");
    }
    
    /**
     * void btnSearchCustomerEvent(ActionEvent event) fills the ObservableList of
     * the TableView with a new one using a search term to filter the results.
     * The SQL query is being filtered here.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws Exception when no connection with the Database could be
     * established.
     */
    @FXML
    private void btnSearchCustomerEvent(ActionEvent event) throws Exception {
        ObservableList<CustomerTabelView> customerList = new CustomerTabelView().getCustomerList(lblSearch.getText());
        tblCustomers.setItems(customerList);
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
