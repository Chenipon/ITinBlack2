package Fys.Controllers;

import Fys.Models.User;
import Fys.Tools.ChartTools;
import Fys.Tools.Screen;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;

/**
 * FXML Controller class. This class controls the Account Overview screen
 * including it's features.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class StatisticsTotalLuggageController implements Initializable {

    @FXML private Label lblUsername, lblErrorMessage;
    @FXML private MenuButton ddwnLuggageType;
    @FXML private BarChart<String, Number> barChart;
    @FXML private DatePicker startDate, endDate;

    private final Screen SCREEN = new Screen();
    private static User currentUser;
    
    public static void getUser(User user) {
        currentUser = user;
    }
    
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
                switch (ddwnLuggageType.getText()) {
                    case ("Lost"): {
                        lblErrorMessage.setText("");
                        barChart.getData().clear();
                        ChartTools chartTools = new ChartTools();
                        final CategoryAxis xAxis = new CategoryAxis();
                        final NumberAxis yAxis = new NumberAxis();
                        barChart.setTitle("Total Luggage reported as \"Lost\"");
                        xAxis.setLabel("Date");
                        yAxis.setLabel("Amount");
                        
                        XYChart.Series lostLuggage = new XYChart.Series();
                        lostLuggage.setName("Lost Luggage");
                        LocalDate iterateDate = start;
                        while(!(iterateDate.equals(end))) {
                            lostLuggage.getData().add(new XYChart.Data(iterateDate.toString(), chartTools.getLostLuggage(iterateDate)));
                            iterateDate = iterateDate.plusDays(1);
                        }
                        lostLuggage.getData().add(new XYChart.Data(iterateDate.toString(), chartTools.getLostLuggage(iterateDate)));
                        barChart.getData().addAll(lostLuggage);
                        break;
                    }
                    case ("Found"): {
                        lblErrorMessage.setText("");
                        barChart.getData().clear();
                        ChartTools chartTools = new ChartTools();
                        final CategoryAxis xAxis = new CategoryAxis();
                        final NumberAxis yAxis = new NumberAxis();
                        barChart.setTitle("Total Luggage reported as \"Found\"");
                        xAxis.setLabel("Date");
                        yAxis.setLabel("Amount");
                        
                        XYChart.Series foundLuggage = new XYChart.Series();
                        foundLuggage.setName("Found Luggage");
                        LocalDate iterateDate = start;
                        while(!(iterateDate.equals(end))) {
                            foundLuggage.getData().add(new XYChart.Data(iterateDate.toString(), chartTools.getFoundLuggage(iterateDate)));
                            iterateDate = iterateDate.plusDays(1);
                        }
                        foundLuggage.getData().add(new XYChart.Data(iterateDate.toString(), chartTools.getFoundLuggage(iterateDate)));
                        barChart.getData().addAll(foundLuggage);
                        break;
                    }
                    case ("Connected"): {
                        lblErrorMessage.setText("");
                        barChart.getData().clear();
                        ChartTools chartTools = new ChartTools();
                        final CategoryAxis xAxis = new CategoryAxis();
                        final NumberAxis yAxis = new NumberAxis();
                        barChart.setTitle("Total Luggage reported as \"Connected\"");
                        xAxis.setLabel("Date");
                        yAxis.setLabel("Amount");
                        
                        XYChart.Series connectedLuggage = new XYChart.Series();
                        connectedLuggage.setName("Connected Luggage");
                        LocalDate iterateDate = start;
                        while(!(iterateDate.equals(end))) {
                            connectedLuggage.getData().add(new XYChart.Data(iterateDate.toString(), chartTools.getConnectedLuggage(iterateDate)));
                            iterateDate = iterateDate.plusDays(1);
                        }
                        connectedLuggage.getData().add(new XYChart.Data(iterateDate.toString(), chartTools.getConnectedLuggage(iterateDate)));
                        barChart.getData().addAll(connectedLuggage);
                        break;
                    }
                    case ("All"): {
                        lblErrorMessage.setText("");
                        barChart.getData().clear();
                        ChartTools chartTools = new ChartTools();
                        final CategoryAxis xAxis = new CategoryAxis();
                        final NumberAxis yAxis = new NumberAxis();
                        barChart.setTitle("Total Luggage reported");
                        xAxis.setLabel("Date");
                        yAxis.setLabel("Amount");
                        
                        XYChart.Series lostLuggage = new XYChart.Series();
                        lostLuggage.setName("Lost Luggage");
                        LocalDate iterateDate = start;
                        while(!(iterateDate.equals(end))) {
                            lostLuggage.getData().add(new XYChart.Data(iterateDate.toString(), chartTools.getLostLuggage(iterateDate)));
                            iterateDate = iterateDate.plusDays(1);
                        }
                        lostLuggage.getData().add(new XYChart.Data(iterateDate.toString(), chartTools.getLostLuggage(iterateDate)));
                        
                        XYChart.Series foundLuggage = new XYChart.Series();
                        foundLuggage.setName("Found Luggage");
                        iterateDate = start;
                        while(!(iterateDate.equals(end))) {
                            foundLuggage.getData().add(new XYChart.Data(iterateDate.toString(), chartTools.getFoundLuggage(iterateDate)));
                            iterateDate = iterateDate.plusDays(1);
                        }
                        foundLuggage.getData().add(new XYChart.Data(iterateDate.toString(), chartTools.getFoundLuggage(iterateDate)));
                        
                        XYChart.Series connectedLuggage = new XYChart.Series();
                        connectedLuggage.setName("Connected Luggage");
                        iterateDate = start;
                        while(!(iterateDate.equals(end))) {
                            connectedLuggage.getData().add(new XYChart.Data(iterateDate.toString(), chartTools.getConnectedLuggage(iterateDate)));
                            iterateDate = iterateDate.plusDays(1);
                        }
                        connectedLuggage.getData().add(new XYChart.Data(iterateDate.toString(), chartTools.getConnectedLuggage(iterateDate)));
                        
                        barChart.getData().addAll(lostLuggage, foundLuggage, connectedLuggage);
                        break;    
                    }
                    default: {
                        lblErrorMessage.setText("Please select a luggage type");
                        break;
                    }
                }
            } else {
                lblErrorMessage.setText("Start Date can't be after End Date");
            }
        } else {
            lblErrorMessage.setText("Please specify date(s)");
        }
    }

    @FXML
    private void ddwnLostLuggageEvent(ActionEvent event) {
        ddwnLuggageType.setText("Lost");
        ddwnLuggageType.setPrefWidth(95);
    }

    @FXML
    private void ddwnFoundLuggageEvent(ActionEvent event) {
        ddwnLuggageType.setText("Found");
        ddwnLuggageType.setPrefWidth(95);
    }

    @FXML
    private void ddwnConnectedLuggageEvent(ActionEvent event) {
        ddwnLuggageType.setText("Connected");
        ddwnLuggageType.setPrefWidth(95);
    }

    @FXML
    private void ddwnAllLuggageEvent(ActionEvent event) {
        ddwnLuggageType.setText("All");
        ddwnLuggageType.setPrefWidth(95);
    }

    //-- DO NOT TOUCH ANY CODE BELOW THIS COMMENT. THESE ARE THE MENU BUTTONS. --
    @FXML
    private void btnLuggagePerEmployeeEvent(ActionEvent event) throws IOException {
        StatisticsLuggagePerEmployeeController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("StatisticsLuggagePerEmployee", "Statistics - Employee");
    }

    @FXML
    private void btnLogoutEvent(ActionEvent event) {
        System.out.println("Log out");
    }

}
