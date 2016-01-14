package Fys.Controllers;

import Fys.Models.Luggage;
import Fys.Models.User;
import Fys.Tools.Screen;
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
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import Fys.Views.ViewModels.LuggageTableView;
import java.sql.SQLException;
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
public class LuggageOverviewController implements Initializable {
    private static Screen screen;
    private static User currentUser;
    
    @FXML private Label lblUsername, lblErrorMessage;
    @FXML private TableView tblLuggage;
    @FXML private TableColumn colType, colBrand, colMaterial, colColor, 
            colComment, colStatus, colResolved, colAction;
    @FXML private MenuButton ddwnLuggageType, ddwnResolved;
    @FXML private TextField lblSearch;
    
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
        colType.setCellValueFactory(new PropertyValueFactory<LuggageTableView, String>("type"));
        colBrand.setCellValueFactory(new PropertyValueFactory<LuggageTableView, String>("brand"));
        colMaterial.setCellValueFactory(new PropertyValueFactory<LuggageTableView, String>("material"));
        colColor.setCellValueFactory(new PropertyValueFactory<LuggageTableView, String>("color"));
        colComment.setCellValueFactory(new PropertyValueFactory<LuggageTableView, String>("comment"));
        colStatus.setCellValueFactory(new PropertyValueFactory<LuggageTableView, String>("status"));
        colResolved.setCellValueFactory(new PropertyValueFactory<LuggageTableView, String>("resolved"));
        colAction.setCellValueFactory(new PropertyValueFactory<LuggageTableView, String>("action"));
        Callback<TableColumn<LuggageTableView, String>, TableCell<LuggageTableView,
                String>> printColumnCellFactory
                = new Callback<TableColumn<LuggageTableView, String>,
                        TableCell<LuggageTableView, String>>() {
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
                                    LuggageTableView item = (LuggageTableView) 
                                            tblLuggage.getSelectionModel().getSelectedItem();
                                    if (item != null) {
                                        try {
                                            Luggage editLuggage = 
                                                    new Luggage().getLuggageById(item.getId());

                                            LuggageEditController.setUser(currentUser);
                                            LuggageEditController.setLuggage(editLuggage);
                                            LuggageEditController.setScreen(screen);
                                            screen.change("LuggageEdit");
                                        } catch (ClassNotFoundException | SQLException | IOException ex) {
                                            Logger.getLogger(AccountOverviewController.
                                                    class.getName()).log(Level.SEVERE, null, ex);
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
            tblLuggage.setItems(getLuggageList());
        } catch (Exception ex) {
            Logger.getLogger(LuggageOverviewController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * ObservableList<LuggageTableView> getLuggageList() returns an ObservableList
     * that can be used in the TableView.
     *
     * @return an ObservableList containing the information that is added into
     * the TableView.
     * @throws Exception when no connection with the Database could be
     * established.
     */
    public ObservableList<LuggageTableView> getLuggageList() throws Exception{
        ObservableList<LuggageTableView> luggageList= 
                new LuggageTableView().getLuggageList();
        return luggageList;
    }

    /**
     * void ddwnLuggageLostEvent(ActionEvent event) fills in the MenuButton
     * with the text "Lost" and sets the size of the MenuButton to always
     * keep it the same. 
     *
     * @param event The event that is being fired by clicking the button.
     */
    @FXML
    private void ddwnLuggageLostEvent(ActionEvent event) {
        ddwnLuggageType.setText("Lost");
        ddwnLuggageType.setPrefWidth(110);
    }

    /**
     * void ddwnLuggageFoundEvent(ActionEvent event) fills in the MenuButton
     * with the text "Found" and sets the size of the MenuButton to always
     * keep it the same. 
     *
     * @param event The event that is being fired by clicking the button.
     */
    @FXML
    private void ddwnLuggageFoundEvent(ActionEvent event) {
        ddwnLuggageType.setText("Found");
        ddwnLuggageType.setPrefWidth(110);
    }
    
    /**
     * void ddwnLuggageAllEvent(ActionEvent event) fills in the MenuButton
     * with the text "All" and sets the size of the MenuButton to always
     * keep it the same. 
     *
     * @param event The event that is being fired by clicking the button.
     */
    @FXML
    private void ddwnLuggageAllEvent(ActionEvent event) {
        ddwnLuggageType.setText("All");
        ddwnLuggageType.setPrefWidth(110);
    }
    
    /**
     * void ddwnResolvedFalseEvent(ActionEvent event) fills in the MenuButton
     * with the text "False" and sets the size of the MenuButton to always
     * keep it the same. 
     *
     * @param event The event that is being fired by clicking the button.
     */
    @FXML
    private void ddwnResolvedFalseEvent(ActionEvent event) {
        ddwnResolved.setText("False");
        ddwnResolved.setPrefWidth(85);
    }

    /**
     * void ddwnResolvedTrueEvent(ActionEvent event) fills in the MenuButton
     * with the text "True" and sets the size of the MenuButton to always
     * keep it the same. 
     *
     * @param event The event that is being fired by clicking the button.
     */
    @FXML
    private void ddwnResolvedTrueEvent(ActionEvent event) {
        ddwnResolved.setText("True");
        ddwnResolved.setPrefWidth(85);
    }
    
    /**
     * void ddwnResolvedAllEvent(ActionEvent event) fills in the MenuButton
     * with the text "All" and sets the size of the MenuButton to always
     * keep it the same. 
     *
     * @param event The event that is being fired by clicking the button.
     */
    @FXML
    private void ddwnResolvedAllEvent(ActionEvent event) {
        ddwnResolved.setText("All");
        ddwnResolved.setPrefWidth(85);
    }

    /**
     * void btnSearchLuggageEvent(ActionEvent event) fills the ObservableList of
     * the TableView with a new one using a search term to filter the results.
     * The SQL query is being filtered here.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws Exception when no connection with the Database could be
     * established.
     */
    @FXML
    private void btnSearchLuggageEvent(ActionEvent event) throws Exception {
        if (!ddwnLuggageType.getText().equals("Luggage Type")) {
            if (!ddwnResolved.getText().equals("Resolved")) {
                lblErrorMessage.setText("");
                ObservableList<LuggageTableView> luggageList = new LuggageTableView()
                        .getLuggageList(lblSearch.getText(), ddwnLuggageType.getText(),
                                ddwnResolved.getText());
                tblLuggage.setItems(luggageList);
            } else {
                lblErrorMessage.setText("Please select a value from \"Resolved\"");
            }
        } else {
            lblErrorMessage.setText("Select a luggage type");
        }
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
     * void btnAddLuggageEvent(ActionEvent event) changes the scene of the Stage
     * to the Luggage Add scene. This scene is being used to add new users into
     * the database so that they can be used to log into the application.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws IOException when the FXML file could not be loaded.
     */
    @FXML
    private void btnAddLuggageEvent(ActionEvent event) throws IOException {
        LuggageAddController.setUser(currentUser);
        LuggageAddController.setScreen(screen);
        screen.change("LuggageAdd");
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
