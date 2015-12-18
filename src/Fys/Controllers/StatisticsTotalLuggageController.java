package Fys.Controllers;

import Fys.Models.User;
import Fys.Tools.ChartTools;
import Fys.Tools.Screen;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
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
 * @author Jeffrey van der Lingen, Daan Befort, IS106-2
 */
public class StatisticsTotalLuggageController implements Initializable {

    private static Screen screen;
    private static User currentUser;

    @FXML private Label lblUsername, lblErrorMessage;
    @FXML private MenuButton ddwnLuggageType, ddwnInterval;
    @FXML private BarChart<String, Number> barChart;
    @FXML private DatePicker startDate, endDate;

    public static void setScreen(Screen newScreen) {
        screen = newScreen;
    }

    public static void setUser(User user) {
        currentUser = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(currentUser.getUsername());
    }

    @FXML
    private void btnPrintStatisticsEvent(ActionEvent event) throws ClassNotFoundException, SQLException {
        //TODO
    }

    @FXML
    private void btnFilterEvent(ActionEvent event) throws ClassNotFoundException, SQLException {
        barChart.getData().clear();
        if (startDate.getValue() != null || endDate.getValue() != null) {
            ChartTools chartTools = new ChartTools();
            LocalDate start = startDate.getValue();
            LocalDate end = endDate.getValue();
            if (end.isAfter(start)) {
                int interval;
                switch (ddwnInterval.getText()) {
                    case ("Day"): {
                        interval = 1;
                        break;
                    }
                    case ("Month"): {
                        interval = 2;
                        break;
                    }
                    case ("Year"): {
                        interval = 3;
                        break;
                    }
                    default: {
                        interval = 0;
                    }
                }
                switch (ddwnLuggageType.getText()) {
                    case ("Lost"): {
                        lblErrorMessage.setText("");
                        final CategoryAxis xAxis = new CategoryAxis();
                        final NumberAxis yAxis = new NumberAxis();
                        barChart.setTitle("Total Luggage reported as \"Lost\"");
                        xAxis.setLabel("Date");
                        yAxis.setLabel("Amount");

                        XYChart.Series lostLuggage = new XYChart.Series();
                        lostLuggage.setName("Lost Luggage");
                        ObservableList<ChartTools> lostLuggageList = chartTools.getLostOrFoundLuggage(start, end, interval, 1);
                        for (int i = 0; i < lostLuggageList.size(); i++) {
                            lostLuggage.getData().add(new XYChart.Data(lostLuggageList.get(i).getDate(), lostLuggageList.get(i).getAmount()));
                        }
                        barChart.getData().addAll(lostLuggage);
                        break;
                    }
                    case ("Found"): {
                        lblErrorMessage.setText("");
                        final CategoryAxis xAxis = new CategoryAxis();
                        final NumberAxis yAxis = new NumberAxis();
                        barChart.setTitle("Total Luggage reported as \"Found\"");
                        xAxis.setLabel("Date");
                        yAxis.setLabel("Amount");

                        XYChart.Series foundLuggage = new XYChart.Series();
                        foundLuggage.setName("Found Luggage");
                        ObservableList<ChartTools> foundLuggageList = chartTools.getLostOrFoundLuggage(start, end, interval, 2);
                        for (int i = 0; i < foundLuggageList.size(); i++) {
                            foundLuggage.getData().add(new XYChart.Data(foundLuggageList.get(i).getDate(), foundLuggageList.get(i).getAmount()));
                        }
                        barChart.getData().addAll(foundLuggage);
                        break;
                    }
                    case ("Connected"): {
                        lblErrorMessage.setText("");
                        final CategoryAxis xAxis = new CategoryAxis();
                        final NumberAxis yAxis = new NumberAxis();
                        barChart.setTitle("Total Luggage reported as \"Connected\"");
                        xAxis.setLabel("Date");
                        yAxis.setLabel("Amount");

                        XYChart.Series connectedLuggage = new XYChart.Series();
                        connectedLuggage.setName("Connected Luggage");
                        ObservableList<ChartTools> connectedLuggageList = chartTools.getConnectedLuggage(start, end, interval);
                        for (int i = 0; i < connectedLuggageList.size(); i++) {
                            connectedLuggage.getData().add(new XYChart.Data(connectedLuggageList.get(i).getDate(), connectedLuggageList.get(i).getAmount()));
                        }
                        barChart.getData().addAll(connectedLuggage);
                        break;
                    }
                    case ("All"): {
                        lblErrorMessage.setText("");
                        final CategoryAxis xAxis = new CategoryAxis();
                        final NumberAxis yAxis = new NumberAxis();
                        barChart.setTitle("Total Luggage reported");
                        xAxis.setLabel("Date");
                        yAxis.setLabel("Amount");

                        XYChart.Series lostLuggage = new XYChart.Series();
                        lostLuggage.setName("Lost Luggage");
                        ObservableList<ChartTools> lostLuggageList = chartTools.getLostOrFoundLuggage(start, end, interval, 1);
                        for (int i = 0; i < lostLuggageList.size(); i++) {
                            lostLuggage.getData().add(new XYChart.Data(lostLuggageList.get(i).getDate(), lostLuggageList.get(i).getAmount()));
                        }

                        XYChart.Series foundLuggage = new XYChart.Series();
                        foundLuggage.setName("Found Luggage");
                        ObservableList<ChartTools> foundLuggageList = chartTools.getLostOrFoundLuggage(start, end, interval, 2);
                        for (int i = 0; i < foundLuggageList.size(); i++) {
                            foundLuggage.getData().add(new XYChart.Data(foundLuggageList.get(i).getDate(), foundLuggageList.get(i).getAmount()));
                        }

                        XYChart.Series connectedLuggage = new XYChart.Series();
                        connectedLuggage.setName("Connected Luggage");
                        ObservableList<ChartTools> connectedLuggageList = chartTools.getConnectedLuggage(start, end, interval);
                        for (int i = 0; i < connectedLuggageList.size(); i++) {
                            connectedLuggage.getData().add(new XYChart.Data(connectedLuggageList.get(i).getDate(), connectedLuggageList.get(i).getAmount()));
                        }

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

    @FXML
    private void ddwnIntervalDayEvent(ActionEvent event) {
        ddwnInterval.setText("Day");
        ddwnInterval.setPrefWidth(110);
    }

    @FXML
    private void ddwnIntervalMonthEvent(ActionEvent event) {
        ddwnInterval.setText("Month");
        ddwnInterval.setPrefWidth(110);
    }

    @FXML
    private void ddwnIntervalYearEvent(ActionEvent event) {
        ddwnInterval.setText("Year");
        ddwnInterval.setPrefWidth(110);
    }

    @FXML
    private void btnLuggagePerEmployeeEvent(ActionEvent event) throws IOException {
        StatisticsLuggagePerEmployeeController.setUser(currentUser);
        StatisticsLuggagePerEmployeeController.setScreen(screen);
        screen.change("StatisticsLuggagePerEmployee");
    }

    @FXML
    private void btnLogoutEvent(ActionEvent event) throws IOException {
        LoginController.setScreen(screen);
        ((Node) event.getSource()).getScene().getWindow().hide();
        screen.change("Login");
    }

}
