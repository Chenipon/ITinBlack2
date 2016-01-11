package Fys.Controllers;

import Fys.Models.User;
import Fys.Tools.ChartTools;
import Fys.Tools.PieChartData;
import Fys.Tools.Screen;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * FXML Controller class. This class controls the Statistics Per Employee screen
 * including it's features.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class StatisticsLuggagePerEmployeeController implements Initializable {

    private static Screen screen;
    private static User currentUser;

    @FXML private Label lblUsername, lblErrorMessage;
    @FXML private DatePicker startDate, endDate;
    @FXML private MenuButton ddwnLuggageType, ddwnInterval, ddwnChartType;
    @FXML private ComboBox comboEmployee;
    @FXML private Button btnPrintStatistics;
    @FXML private AnchorPane charts;
    @FXML private BarChart<String, Number> barChart;
    @FXML private AreaChart<String, Number> areaChart;
    @FXML private LineChart<String, Number> lineChart;
    @FXML private PieChart pieChart;
    @FXML private ScatterChart<String, Number> scatterChart;

    /**
     * 
     * @param newScreen 
     */
    public static void setScreen(Screen newScreen) {
        screen = newScreen;
    }

    /**
     *
     * @param user
     */
    public static void setUser(User user) {
        currentUser = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(currentUser.getUsername());
        btnPrintStatistics.setDisable(true);

        try {
            comboEmployee.setItems(new User().getEmployees());
            comboEmployee.setConverter(new StringConverter<User>() {
                @Override
                public String toString(User user) {
                    return user.getUsername();
                }

                @Override
                public User fromString(String string) {
                    return null;
                }
            });

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(StatisticsLuggagePerEmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnFilterEvent(ActionEvent event) throws ClassNotFoundException, SQLException {
        if (startDate.getValue() != null || endDate.getValue() != null || !comboEmployee.getSelectionModel().isEmpty()) {
            User employee = (User) comboEmployee.getSelectionModel().getSelectedItem();
            LocalDate start = startDate.getValue();
            LocalDate end = endDate.getValue();
            if (end.isAfter(start)) {
                if (!ddwnInterval.getText().equals("Select Interval")) {
                    if (!ddwnLuggageType.getText().equals("Select Type")) {
                        if (!ddwnChartType.getText().equals("Select Graph Type")) {
                            btnPrintStatistics.setDisable(false); //Enable the "Save Statistics" button
                            int interval, graphType;
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
                            switch (ddwnChartType.getText()) {
                                case ("Bar Chart"): {
                                    graphType = 1;
                                    barChart.setVisible(true);
                                    areaChart.setVisible(false);
                                    lineChart.setVisible(false);
                                    scatterChart.setVisible(false);
                                    pieChart.setVisible(false);
                                    break;
                                }
                                case ("Area Chart"): {
                                    graphType = 2;
                                    areaChart.setVisible(true);
                                    barChart.setVisible(false);
                                    lineChart.setVisible(false);
                                    scatterChart.setVisible(false);
                                    pieChart.setVisible(false);
                                    break;
                                }
                                case ("Line Chart"): {
                                    graphType = 3;
                                    lineChart.setVisible(true);
                                    areaChart.setVisible(false);
                                    barChart.setVisible(false);
                                    scatterChart.setVisible(false);
                                    pieChart.setVisible(false);
                                    break;
                                }
                                case ("Scatter Chart"): {
                                    graphType = 4;
                                    scatterChart.setVisible(true);
                                    lineChart.setVisible(false);
                                    areaChart.setVisible(false);
                                    barChart.setVisible(false);
                                    pieChart.setVisible(false);
                                    break;
                                }
                                case ("Pie Chart"): {
                                    graphType = 5;
                                    pieChart.setVisible(true);
                                    scatterChart.setVisible(false);
                                    lineChart.setVisible(false);
                                    areaChart.setVisible(false);
                                    barChart.setVisible(false);
                                    break;
                                }
                                default: {
                                    graphType = 0;
                                }
                            }
                            
                            pieChart.getData().clear();
                            areaChart.getData().clear();
                            barChart.getData().clear();
                            lineChart.getData().clear();
                            scatterChart.getData().clear();
                            lblErrorMessage.setText("");
                            if (graphType == 5) {
                                fillPieChart(start, end, interval);
                            } else {
                                if (ddwnLuggageType.getText().equals("All")) {
                                    fillGraphAllTypes(graphType, start, end, interval, employee);
                                } else {
                                    fillGraphSingleType(ddwnLuggageType.getText(), graphType, start, end, interval, employee);
                                }
                            }
                        } else {
                            lblErrorMessage.setText("Please select a type of graphs");
                        }
                    } else {
                        lblErrorMessage.setText("Please select a type of luggage");
                    }
                } else {
                    lblErrorMessage.setText("Please select an interval");
                }
            } else {
                lblErrorMessage.setText("Start Date can't be after End Date");
            }
        } else {
            lblErrorMessage.setText("Please specify date(s) and/or employee");
        }
    }
    
    @FXML
    private void btnPrintStatisticsEvent(ActionEvent event) {
        try {
            /* Create new FileChooser */
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("Statistics - Luggage per Employee");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF file (*.pdf)", "*.pdf"));

            /* Create a new File by selecting a save directory */
            File pdfFile = fileChooser.showSaveDialog(new Stage());

            /* Check if a directory has been selected. If not, return out of the method */
            if (pdfFile == null) {
                return;
            }

            /* Create new Document */
            Document document = new Document();

            /* Create the FileOutputStream */
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));

            /* Initialize the image and the bold font */
            Image corendonLogo = Image.getInstance("src/Fys/Content/Image/corendonlogo.jpg");
            corendonLogo.scalePercent(20f);
            Font fontbold = FontFactory.getFont("Arial", 18, Font.BOLD);

            /* Create a snapshot of the graph */
            WritableImage writableImage = charts.snapshot(new SnapshotParameters(), null);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(bufferedImage, null);
            image.scalePercent(((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin()) / image.getWidth()) * 100);

            /* Write to the Document */
            document.open();
            document.add(corendonLogo);
            document.add(new Paragraph("Statistics - Luggage per Employee", fontbold));
            document.add(image);
            document.close();

            /* Display success of file save */
            lblErrorMessage.setText("Successfully saved this graph to a PDF file");
        } catch (IOException | DocumentException ex) {
            Logger.getLogger(LuggageEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void ddwnLostLuggageEvent(ActionEvent event) {
        ddwnLuggageType.setText("Lost");
        ddwnLuggageType.setPrefWidth(100);
    }

    @FXML
    private void ddwnFoundLuggageEvent(ActionEvent event) {
        ddwnLuggageType.setText("Found");
        ddwnLuggageType.setPrefWidth(100);
    }

    @FXML
    private void ddwnConnectedLuggageEvent(ActionEvent event) {
        ddwnLuggageType.setText("Connected");
        ddwnLuggageType.setPrefWidth(100);
    }
    
    @FXML
    private void ddwnAllLuggageEvent(ActionEvent event) {
        ddwnLuggageType.setText("All");
        ddwnLuggageType.setPrefWidth(100);
    }
    
    @FXML
    private void ddwnIntervalDayEvent(ActionEvent event) {
        ddwnInterval.setText("Day");
        ddwnInterval.setPrefWidth(75);
    }

    @FXML
    private void ddwnIntervalMonthEvent(ActionEvent event) {
        ddwnInterval.setText("Month");
        ddwnInterval.setPrefWidth(75);
    }

    @FXML
    private void ddwnIntervalYearEvent(ActionEvent event) {
        ddwnInterval.setText("Year");
        ddwnInterval.setPrefWidth(75);
    }
    
    @FXML
    private void ddwnChartTypeBarChartEvent(ActionEvent event) {
        ddwnChartType.setText("Bar Chart");
        ddwnChartType.setPrefWidth(115);
    }

    @FXML
    private void ddwnChartTypeAreaChartEvent(ActionEvent event) {
        ddwnChartType.setText("Area Chart");
        ddwnChartType.setPrefWidth(115);
    }
    
    @FXML
    private void ddwnChartTypeLineChartEvent(ActionEvent event) {
        ddwnChartType.setText("Line Chart");
        ddwnChartType.setPrefWidth(115);
    }
    
    @FXML
    private void ddwnChartTypeScatterChartEvent(ActionEvent event) {
        ddwnChartType.setText("Scatter Chart");
        ddwnChartType.setPrefWidth(115);
    }
    
    @FXML
    private void ddwnChartTypePieChartEvent(ActionEvent event) {
        ddwnChartType.setText("Pie Chart");
        ddwnChartType.setPrefWidth(115);
    }

    private void fillPieChart(LocalDate start, LocalDate end, int interval) throws ClassNotFoundException, SQLException {
        PieChartData pieChartData = new PieChartData();
        pieChartData.getData(start, end, interval);
        if (pieChartData.getLostLuggage() != 0) {
            pieChart.getData().add(new PieChart.Data("Lost", pieChartData.getLostLuggage()));
        }
        if (pieChartData.getFoundLuggage() != 0) {
            pieChart.getData().add(new PieChart.Data("Found", pieChartData.getFoundLuggage()));
        }
        if (pieChartData.getConnectedLuggage() != 0) {
            pieChart.getData().add(new PieChart.Data("Connected", pieChartData.getConnectedLuggage()));
        }
        pieChart.setTitle("Total Luggage Reported");
    }

    private void fillGraphSingleType(String luggageType, int graphType, LocalDate start, LocalDate end, int interval, User employee) throws SQLException, ClassNotFoundException {
        ObservableList<ChartTools> observableList;
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        XYChart.Series series = new XYChart.Series();
        xAxis.setLabel("Date");
        yAxis.setLabel("Amount");

        switch (luggageType) {
            case ("Lost"): {
                observableList = new ChartTools().getLostOrFoundLuggagePerEmployee(start, end, interval, 1, employee);
                for (int i = 0; i < observableList.size(); i++) {
                    series.getData().add(new XYChart.Data(observableList.get(i).getDate(), observableList.get(i).getAmount()));
                }
                break;
            }
            case ("Found"): {
                observableList = new ChartTools().getLostOrFoundLuggagePerEmployee(start, end, interval, 2, employee);
                for (int i = 0; i < observableList.size(); i++) {
                    series.getData().add(new XYChart.Data(observableList.get(i).getDate(), observableList.get(i).getAmount()));
                }
                break;
            }
            case ("Connected"): {
                observableList = new ChartTools().getConnectedLuggagePerEmployee(start, end, interval, employee);
                for (int i = 0; i < observableList.size(); i++) {
                    series.getData().add(new XYChart.Data(observableList.get(i).getDate(), observableList.get(i).getAmount()));
                }
                break;
            }
            default: {
                break;
            }
        }
        series.setName(luggageType + " Luggage");

        switch (graphType) {
            case (1): {
                barChart.setTitle("Total Luggage reported as \"" + luggageType + "\"");
                barChart.getData().add(series);
                break;
            }
            case (2): {
                areaChart.setTitle("Total Luggage reported as \"" + luggageType + "\"");
                areaChart.getData().add(series);
                break;
            }
            case (3): {
                lineChart.setTitle("Total Luggage reported as \"" + luggageType + "\"");
                lineChart.getData().add(series);
            }
            case (4): {
                scatterChart.setTitle("Total Luggage reported as \"" + luggageType + "\"");
                scatterChart.getData().add(series);
            }
            default: {
                break;
            }
        }

    }

    private void fillGraphAllTypes(int graphType, LocalDate start, LocalDate end, int interval, User employee) throws SQLException, ClassNotFoundException {
        /* Lost luggage */
        XYChart.Series lostLuggage = new XYChart.Series();
        lostLuggage.setName("Lost Luggage");
        ObservableList<ChartTools> lostLuggageList = new ChartTools().getLostOrFoundLuggagePerEmployee(start, end, interval, 1, employee);
        for (int i = 0; i < lostLuggageList.size(); i++) {
            lostLuggage.getData().add(new XYChart.Data(lostLuggageList.get(i).getDate(), lostLuggageList.get(i).getAmount()));
        }

        /* Found luggage */
        XYChart.Series foundLuggage = new XYChart.Series();
        foundLuggage.setName("Found Luggage");
        ObservableList<ChartTools> foundLuggageList = new ChartTools().getLostOrFoundLuggage(start, end, interval, 2);
        for (int i = 0; i < foundLuggageList.size(); i++) {
            foundLuggage.getData().add(new XYChart.Data(foundLuggageList.get(i).getDate(), foundLuggageList.get(i).getAmount()));
        }

        /* Connected luggage */
        XYChart.Series connectedLuggage = new XYChart.Series();
        connectedLuggage.setName("Connected Luggage");
        ObservableList<ChartTools> connectedLuggageList = new ChartTools().getConnectedLuggagePerEmployee(start, end, interval, employee);
        for (int i = 0; i < connectedLuggageList.size(); i++) {
            connectedLuggage.getData().add(new XYChart.Data(connectedLuggageList.get(i).getDate(), connectedLuggageList.get(i).getAmount()));
        }

        switch (graphType) {
            case (1): {
                barChart.setTitle("All Total Luggage reported");
                barChart.getData().addAll(lostLuggage, foundLuggage, connectedLuggage);
                break;
            }
            case (2): {
                areaChart.setTitle("All Total Luggage reported");
                areaChart.getData().addAll(lostLuggage, foundLuggage, connectedLuggage);
                break;
            }
            case (3): {
                lineChart.setTitle("All Total Luggage reported");
                lineChart.getData().addAll(lostLuggage, foundLuggage, connectedLuggage);
            }
            case (4): {
                scatterChart.setTitle("All Total Luggage reported");
                scatterChart.getData().addAll(lostLuggage, foundLuggage, connectedLuggage);
            }
            default: {
                break;
            }
        }
    }

    @FXML
    private void btnTotalLuggageEvent(ActionEvent event) throws IOException {
        StatisticsTotalLuggageController.setUser(currentUser);
        StatisticsTotalLuggageController.setScreen(screen);
        screen.change("StatisticsTotalLuggage");
    }

    @FXML
    private void btnLogoutEvent(ActionEvent event) throws IOException {
        LoginController.setScreen(screen);
        ((Node) event.getSource()).getScene().getWindow().hide();
        screen.change("Login");
    }

}
