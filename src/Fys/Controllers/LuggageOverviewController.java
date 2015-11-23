package Fys.Controllers;

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

/**
 * FXML Controller class. This class controls the Luggage Overview screen
 * including it's features.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class LuggageOverviewController implements Initializable {

    @FXML private Label lblUsername, lblErrorMessage;
    @FXML private TableView tblLuggage;
    @FXML private TableColumn colType, colBrand, colMaterial, colColor, colComment, colStatus;
    @FXML private MenuButton ddwnLuggageType;
    @FXML private TextField lblSearch;
    
    private final Screen SCREEN = new Screen();
    private static User currentUser;
    
    public static void getUser(User user) {
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
    private void ddwnLuggageConnectedEvent(ActionEvent event) {
        ddwnLuggageType.setText("Connected");
        ddwnLuggageType.setPrefWidth(110);
    }
    
    @FXML
    private void ddwnLuggageAllEvent(ActionEvent event) {
        ddwnLuggageType.setText("All");
        ddwnLuggageType.setPrefWidth(110);
    }

    @FXML
    private void btnSearchLuggageEvent(ActionEvent event) throws Exception {
        if (!ddwnLuggageType.getText().equals("Luggage Type")) {
            lblErrorMessage.setText("");
            ObservableList<LuggageTabelView> luggageList = new LuggageTabelView().getLuggageList(lblSearch.getText(), ddwnLuggageType.getText());
            tblLuggage.setItems(luggageList);
        } else {
            lblErrorMessage.setText("Select a luggage type");
        }
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
    private void btnLogoutEvent(ActionEvent event) {
        System.out.println("Log out");
    }

}
