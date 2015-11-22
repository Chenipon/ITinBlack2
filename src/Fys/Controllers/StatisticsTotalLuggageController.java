package Fys.Controllers;

import Fys.Models.User;
import Fys.Tools.ChartTools;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class. This class controls the Account Overview screen
 * including it's features.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class StatisticsTotalLuggageController implements Initializable {

    public static User currentUser;

    @FXML
    private Label lblUsername, lblErrorMessage;
    @FXML
    private MenuButton ddwnLuggageType;
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private DatePicker startDate, endDate;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(currentUser.getUsername());
    }

    @FXML
    private void btnPrintStatisticsEvent(ActionEvent event) throws ClassNotFoundException, SQLException {

    }

    @FXML
    private void btnFilterEvent(ActionEvent event) throws ClassNotFoundException, SQLException {
        if (startDate.getValue() != null || endDate.getValue() != null) {
            LocalDate start = startDate.getValue();
            LocalDate end = endDate.getValue();
            if (end.isAfter(start)) {
                lblErrorMessage.setText("");
                ChartTools chartTools = new ChartTools();
                System.out.println(chartTools.getLostLuggage(start));
                //CONTINUE CHARTS IN HERE
            } else {
                lblErrorMessage.setText("Start Date can't be after End Date");
            }
        } else {
            lblErrorMessage.setText("Please specify date(s)");
        }
    }

    @FXML
    private void ddwnLostLuggageEvent(ActionEvent event) {
        ddwnLuggageType.setText("Lost"); //Sets the text of the ddwn.
        ddwnLuggageType.setPrefWidth(95);
        System.out.println("Lost Luggage Selected");
    }

    @FXML
    private void ddwnFoundLuggageEvent(ActionEvent event) {
        ddwnLuggageType.setText("Found"); //Sets the text of the ddwn.
        ddwnLuggageType.setPrefWidth(95);
        System.out.println("Found Luggage Selected");
    }

    @FXML
    private void ddwnConnectedLuggageEvent(ActionEvent event) {
        ddwnLuggageType.setText("Connected"); //Sets the text of the ddwn.
        ddwnLuggageType.setPrefWidth(95);
        System.out.println("Connected Luggage Selected");
    }

    //-- DO NOT TOUCH ANY CODE BELOW THIS COMMENT. THESE ARE THE MENU BUTTONS. --
    @FXML
    private void btnLuggagePerEmployeeEvent(ActionEvent event) throws IOException {
        StatisticsLuggagePerEmployeeController.currentUser = currentUser;
        ((Node) event.getSource()).getScene().getWindow().hide();
        Parent parent = FXMLLoader.load(getClass().getResource("/Fys/Views/StatisticsLuggagePerEmployee.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        scene.getStylesheets().add("/Fys/Content/Css/stylesheet.css");
        stage.setScene(scene);
        stage.setTitle("Statistics - Employee");
        stage.show();
    }

    @FXML
    private void btnLogoutEvent(ActionEvent event) {
        System.out.println("Log out");
    }

}
