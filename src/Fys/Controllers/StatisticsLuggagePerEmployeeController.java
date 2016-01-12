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
import java.text.DecimalFormat;
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
 * @author Jeffrey van der Lingen, Daan Befort, IS106-2
 */
public class StatisticsLuggagePerEmployeeController implements Initializable {

    private static Screen screen;
    private static User currentUser;

    @FXML private Label lblUsername, lblErrorMessage;
    @FXML private DatePicker startDate, endDate;
    @FXML private MenuButton ddwnLuggageType, ddwnInterval, ddwnChartType, ddwnResolved;
    @FXML private ComboBox comboEmployee;
    @FXML private AnchorPane charts;
    @FXML private BarChart<String, Number> barChart;
    @FXML private AreaChart<String, Number> areaChart;
    @FXML private LineChart<String, Number> lineChart;
    @FXML private ScatterChart<String, Number> scatterChart;
    @FXML private PieChart pieChart;
    @FXML private Button btnSaveStatistics;

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
     * void setUser(User user) sets the user for the Controller. This is the
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
        btnSaveStatistics.setDisable(true);

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

    /**
     * void btnSaveStatisticsEvent(ActionEvent event) is the event used for
     * saving a graph to a PDF file.
     *
     * @param event The event that is being fired by clicking the button.
     */
    @FXML
    private void btnSaveStatisticsEvent(ActionEvent event) {
        try {
            /* Create new FileChooser */
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("Statistics - Luggage per Employee");
            fileChooser.getExtensionFilters().add(new FileChooser
                    .ExtensionFilter("PDF file (*.pdf)", "*.pdf"));

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
    private void btnFilterEvent(ActionEvent event) throws ClassNotFoundException, SQLException {
        if (ddwnCheckFilled(startDate.getValue(), endDate.getValue())) {
            User employee = (User) comboEmployee.getSelectionModel().getSelectedItem();
            LocalDate start = startDate.getValue();
            LocalDate end = endDate.getValue();
            btnSaveStatistics.setDisable(false); //Enable the "Save Statistics" button
            int interval, graphType, resolved, type;
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
            switch (ddwnResolved.getText()) {
                case ("Unresolved"): {
                    resolved = 0;
                    break;
                }
                case ("Resolved"): {
                    resolved = 1;
                    break;
                }
                case ("All"): {
                    resolved = 2;
                    break;
                }
                default: {
                    resolved = 0;
                    break;
                }
            }
            switch (ddwnLuggageType.getText()) {
                case ("Lost"): {
                    type = 1;
                    break;
                }
                case ("Found"): {
                    type = 2;
                    break;
                }
                case ("All"): {
                    type = 3;
                    break;
                }
                default: {
                    type = 0;
                    break;
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
                fillPieChart(start, end, interval, employee, resolved, type);
            } else {
                if (ddwnLuggageType.getText().equals("All")) {
                    fillGraphAllTypes(graphType, start, end, interval, employee, resolved);
                } else {
                    fillGraphSingleType(ddwnLuggageType.getText(), graphType, start, end, interval, employee, resolved);
                }
            }

        }
    }

    /**
     * boolean ddwnCheckFilled(LocalDate start, LocalDate end) checks if the
     * dropdown-boxes and the date-boxes are filled correctly and returns this
     * as a boolean.
     *
     * @param start is the LocalDate start date of the graph selection.
     * @param end is the LocalDate end dat of the graph selection.
     * @return true if all data is correctly filled, false otherwise.
     */
    private boolean ddwnCheckFilled(LocalDate start, LocalDate end) {
        if (startDate.getValue() == null || endDate.getValue() == null) {
            lblErrorMessage.setText("Please specify date(s)");
            return false;
        }
        if (end.isBefore(start)) {
            lblErrorMessage.setText("Start Date can't be after End Date");
            return false;
        }
        if (ddwnInterval.getText().equals("Select")) {
            lblErrorMessage.setText("Please select an interval");
            return false;
        }
        if (ddwnLuggageType.getText().equals("Select")) {
            lblErrorMessage.setText("Please select a type of luggage");
            return false;
        }
        if (ddwnResolved.getText().equals("Select")) {
            lblErrorMessage.setText("Please select a resolved type");
            return false;
        }
        if (ddwnChartType.getText().equals("Select")) {
            lblErrorMessage.setText("Please select a type of graphs");
            return false;
        }
        if (comboEmployee.getSelectionModel().isEmpty()) {
            lblErrorMessage.setText("Please specify an employee");
            return false;
        }
        return true;
    }

    private void fillPieChart(LocalDate start, LocalDate end, int interval, User employee, int resolved, int type) throws ClassNotFoundException, SQLException {
        PieChartData pieChartData = new PieChartData();
        pieChartData.getDataPerEmployee(start, end, interval, employee, resolved, type);
        DecimalFormat df = new DecimalFormat("#.0");
        if (pieChartData.getFoundLuggageResolved() != 0) {
            pieChart.getData().add(new PieChart.Data("Resolved Found - " 
                    + df.format(((double)pieChartData.getFoundLuggageResolved() / pieChartData.getTotalLuggage()) * 100) + "%", pieChartData.getFoundLuggageResolved()));
        }
        if (pieChartData.getFoundLuggageUnResolved() != 0) {
            pieChart.getData().add(new PieChart.Data("Unresolved Found - " 
                    + df.format(((double)pieChartData.getFoundLuggageUnResolved() / pieChartData.getTotalLuggage()) * 100) + "%", pieChartData.getFoundLuggageUnResolved()));
        }
        if (pieChartData.getLostLuggageResolved() != 0) {
            pieChart.getData().add(new PieChart.Data("Resolved Lost - " 
                    + df.format(((double)pieChartData.getLostLuggageResolved() / pieChartData.getTotalLuggage()) * 100) + "%", pieChartData.getLostLuggageResolved()));
        }
        if (pieChartData.getLostLuggageUnResolved() != 0) {
            pieChart.getData().add(new PieChart.Data("Unresolved Lost - " 
                    + df.format(((double)pieChartData.getLostLuggageUnResolved() / pieChartData.getTotalLuggage()) * 100) + "%", pieChartData.getLostLuggageUnResolved()));
        }
        pieChart.setTitle("Total Luggage Reported");
    }

    private void fillGraphSingleType(String luggageType, int graphType, 
            LocalDate start, LocalDate end, int interval, User employee, int resolved) 
            throws SQLException, ClassNotFoundException {
        ObservableList<ChartTools> observableList;
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        XYChart.Series series = new XYChart.Series();

        xAxis.setLabel("Date");
        yAxis.setLabel("Amount");

        switch (luggageType) {
            case ("Lost"): {
                observableList = new ChartTools().getLostOrFoundLuggagePerEmployee(start, 
                        end, interval, 1, employee, resolved);
                for (int i = 0; i < observableList.size(); i++) {
                    series.getData().add(new XYChart.Data(observableList.get(i).getDate(), 
                            observableList.get(i).getAmount()));
                }
                break;
            }
            case ("Found"): {
                observableList = new ChartTools().getLostOrFoundLuggagePerEmployee(start, 
                        end, interval, 2, employee, resolved);
                for (int i = 0; i < observableList.size(); i++) {
                    series.getData().add(new XYChart.Data(observableList.get(i).getDate(), 
                            observableList.get(i).getAmount()));
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

    private void fillGraphAllTypes(int graphType, LocalDate start, LocalDate end, int interval, User employee, int resolved) throws SQLException, ClassNotFoundException {
        /* Lost luggage */
        XYChart.Series lostLuggage = new XYChart.Series();
        lostLuggage.setName("Lost Luggage");
        ObservableList<ChartTools> lostLuggageList = new ChartTools().getLostOrFoundLuggagePerEmployee(start, end, interval, 1, employee, resolved);
        for (int i = 0; i < lostLuggageList.size(); i++) {
            lostLuggage.getData().add(new XYChart.Data(lostLuggageList.get(i).getDate(), lostLuggageList.get(i).getAmount()));
        }

        /* Found luggage */
        XYChart.Series foundLuggage = new XYChart.Series();
        foundLuggage.setName("Found Luggage");
        ObservableList<ChartTools> foundLuggageList = new ChartTools().getLostOrFoundLuggagePerEmployee(start, end, interval, 2, employee, resolved);
        for (int i = 0; i < foundLuggageList.size(); i++) {
            foundLuggage.getData().add(new XYChart.Data(foundLuggageList.get(i).getDate(), foundLuggageList.get(i).getAmount()));
        }

        switch (graphType) {
            case (1): {
                barChart.setTitle("All Total Luggage reported");
                barChart.getData().addAll(lostLuggage, foundLuggage);
                break;
            }
            case (2): {
                areaChart.setTitle("All Total Luggage reported");
                areaChart.getData().addAll(lostLuggage, foundLuggage);
                break;
            }
            case (3): {
                lineChart.setTitle("All Total Luggage reported");
                lineChart.getData().addAll(lostLuggage, foundLuggage);
            }
            case (4): {
                scatterChart.setTitle("All Total Luggage reported");
                scatterChart.getData().addAll(lostLuggage, foundLuggage);
            }
            default: {
                break;
            }
        }
    }
    
    /* 
     * The next couple of methods are all for the setting of text and correct 
     * width of the selection dropdown-boxes and all work the same 
     */
    @FXML
    private void ddwnLostLuggageEvent(ActionEvent event) {
        ddwnLuggageType.setText("Lost");
        ddwnLuggageType.setPrefWidth(70);
    }

    @FXML
    private void ddwnFoundLuggageEvent(ActionEvent event) {
        ddwnLuggageType.setText("Found");
        ddwnLuggageType.setPrefWidth(70);
    }

    @FXML
    private void ddwnAllLuggageEvent(ActionEvent event) {
        ddwnLuggageType.setText("All");
        ddwnLuggageType.setPrefWidth(70);
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

    @FXML
    private void ddwnResolvedEvent(ActionEvent event) {
        ddwnResolved.setText("Resolved");
        ddwnResolved.setPrefWidth(95);
    }

    @FXML
    private void ddwnUnresolvedEvent(ActionEvent event) {
        ddwnResolved.setText("Unresolved");
        ddwnResolved.setPrefWidth(95);
    }

    @FXML
    private void ddwnResolvedAllEvent(ActionEvent event) {
        ddwnResolved.setText("All");
        ddwnResolved.setPrefWidth(95);
    }

    /**
     * void btnTotalLuggageEvent(ActionEvent event) changes the screen to
     * the Total Luggage Statistics screen.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws IOException when the FXML file could not be loaded.
     */
    @FXML
    private void btnTotalLuggageEvent(ActionEvent event) throws IOException {
        StatisticsTotalLuggageController.setUser(currentUser);
        StatisticsTotalLuggageController.setScreen(screen);
        screen.change("StatisticsTotalLuggage");
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
