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
import Fys.Views.ViewModels.LuggageTabelView;
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
    @FXML private TableColumn colType, colBrand, colMaterial, colColor, colComment, colStatus, colResolved, colAction;
    @FXML private MenuButton ddwnLuggageType, ddwnResolved;
    @FXML private TextField lblSearch;
    
    public static void setScreen(Screen newScreen) {
        screen = newScreen;
    }
    
    public static void setUser(User user) {
        currentUser = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(currentUser.getUsername());
        colType.setCellValueFactory(new PropertyValueFactory<LuggageTabelView, String>("type"));
        colBrand.setCellValueFactory(new PropertyValueFactory<LuggageTabelView, String>("brand"));
        colMaterial.setCellValueFactory(new PropertyValueFactory<LuggageTabelView, String>("material"));
        colColor.setCellValueFactory(new PropertyValueFactory<LuggageTabelView, String>("color"));
        colComment.setCellValueFactory(new PropertyValueFactory<LuggageTabelView, String>("comment"));
        colStatus.setCellValueFactory(new PropertyValueFactory<LuggageTabelView, String>("status"));
        colResolved.setCellValueFactory(new PropertyValueFactory<LuggageTabelView, String>("resolved"));
        colAction.setCellValueFactory(new PropertyValueFactory<LuggageTabelView, String>("action"));
        Callback<TableColumn<LuggageTabelView, String>, TableCell<LuggageTabelView, String>> printColumnCellFactory
                = new Callback<TableColumn<LuggageTabelView, String>, TableCell<LuggageTabelView, String>>() {

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
                                            LuggageTabelView item = (LuggageTabelView) tblLuggage.getSelectionModel().getSelectedItem();
                                            if (item != null) {
                                                try {
                                                    Luggage editLuggage = new Luggage().getLuggageById(item.getId());
                                                    
                                                    LuggageEditController.setUser(currentUser);
                                                    LuggageEditController.setLuggage(editLuggage);
                                                    LuggageEditController.setScreen(screen);
                                                    screen.change("LuggageEdit");
                                                } catch (ClassNotFoundException | SQLException | IOException ex) {
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
            tblLuggage.setItems(getLuggageList());
        } catch (Exception ex) {
            Logger.getLogger(LuggageOverviewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ObservableList<LuggageTabelView> getLuggageList() throws Exception{
        ObservableList<LuggageTabelView> luggageList= new LuggageTabelView().getLuggageList();
        return luggageList;
    }

    @FXML
    private void ddwnLuggageLostEvent(ActionEvent event) {
        ddwnLuggageType.setText("Lost");
        ddwnLuggageType.setPrefWidth(110);
    }

    @FXML
    private void ddwnLuggageFoundEvent(ActionEvent event) {
        ddwnLuggageType.setText("Found");
        ddwnLuggageType.setPrefWidth(110);
    }
    
    @FXML
    private void ddwnLuggageAllEvent(ActionEvent event) {
        ddwnLuggageType.setText("All");
        ddwnLuggageType.setPrefWidth(110);
    }
    
    @FXML
    private void ddwnResolvedFalseEvent(ActionEvent event) {
        ddwnResolved.setText("False");
        ddwnResolved.setPrefWidth(85);
    }

    @FXML
    private void ddwnResolvedTrueEvent(ActionEvent event) {
        ddwnResolved.setText("True");
        ddwnResolved.setPrefWidth(85);
    }
    
    @FXML
    private void ddwnResolvedAllEvent(ActionEvent event) {
        ddwnResolved.setText("All");
        ddwnResolved.setPrefWidth(85);
    }

    @FXML
    private void btnSearchLuggageEvent(ActionEvent event) throws Exception {
        if (!ddwnLuggageType.getText().equals("Luggage Type")) {
            if (!ddwnResolved.getText().equals("Resolved")) {
                lblErrorMessage.setText("");
                ObservableList<LuggageTabelView> luggageList = new LuggageTabelView()
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

    @FXML
    private void btnCustomerEvent(ActionEvent event) throws IOException {
        CustomerOverviewController.setUser(currentUser);
        CustomerOverviewController.setScreen(screen);
        screen.change("CustomerOverview");
    }

    @FXML
    private void btnAddLuggageEvent(ActionEvent event) throws IOException {
        LuggageAddController.setUser(currentUser);
        LuggageAddController.setScreen(screen);
        screen.change("LuggageAdd");
    }

    @FXML
    private void btnLogoutEvent(ActionEvent event) throws IOException {
        LoginController.setScreen(screen);
        ((Node) event.getSource()).getScene().getWindow().hide();
        screen.change("Login");
    }

}
