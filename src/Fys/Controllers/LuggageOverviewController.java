package Fys.Controllers;

import Fys.Models.User;
import Fys.Views.ViewModels.AccountTabelView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import Fys.Views.ViewModels.LuggageTabelView;

/**
 * FXML Controller class. This class controls the Luggage Overview screen
 * including it's features.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class LuggageOverviewController implements Initializable {

    public static User currentUser;

    @FXML private Label lblUsername, lblErrorMessage;
    @FXML private TableView tblLuggage;
    @FXML private TableColumn colType, colBrand, colMaterial, colColor, colComment, colStatus;
    @FXML private MenuButton ddwnLuggageType;
    @FXML private TextField lblSearch;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(currentUser.getUsername());
        colType.setCellValueFactory(new PropertyValueFactory<AccountTabelView, String>("type"));
        colBrand.setCellValueFactory(new PropertyValueFactory<AccountTabelView, String>("brand"));
        colMaterial.setCellValueFactory(new PropertyValueFactory<AccountTabelView, String>("material"));
        colColor.setCellValueFactory(new PropertyValueFactory<AccountTabelView, String>("color"));
        colComment.setCellValueFactory(new PropertyValueFactory<AccountTabelView, String>("comment"));
        colStatus.setCellValueFactory(new PropertyValueFactory<AccountTabelView, String>("status"));
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
        CustomerOverviewController.currentUser = currentUser;
        ((Node) event.getSource()).getScene().getWindow().hide();
        Parent parent = FXMLLoader.load(getClass().getResource("/Fys/Views/CustomerOverview.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        scene.getStylesheets().add("/Fys/Content/Css/stylesheet.css");
        stage.setScene(scene);
        stage.setTitle("Customer Overview");
        stage.show();
    }

    @FXML
    private void btnAddLuggageEvent(ActionEvent event) throws IOException {
        LuggageAddController.currentUser = currentUser;
        ((Node) event.getSource()).getScene().getWindow().hide();
        Parent parent = FXMLLoader.load(getClass().getResource("/Fys/Views/LuggageAdd.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        scene.getStylesheets().add("/Fys/Content/Css/stylesheet.css");
        stage.setScene(scene);
        stage.setTitle("Add Luggage");
        stage.show();
    }

    @FXML
    private void btnLogoutEvent(ActionEvent event) {
        System.out.println("Log out");
    }

}
