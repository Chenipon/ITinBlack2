package Fys.Controllers;

import Fys.Models.User;
import Fys.Tools.ChartTools;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
    @FXML private Button btnPrintStatistics;

    public static void setScreen(Screen newScreen) {
        screen = newScreen;
    }

    public static void setUser(User user) {
        currentUser = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(currentUser.getUsername());
        btnPrintStatistics.setDisable(true); //Disable the "Save Statistics" button; No graph yet.
    }

    @FXML
    private void btnPrintStatisticsEvent(ActionEvent event) throws ClassNotFoundException, SQLException {
        try {
            /* Create new FileChooser */
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("Statistics - Total Luggage");
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
            WritableImage writableImage = barChart.snapshot(new SnapshotParameters(), null);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(bufferedImage, null);
            image.scalePercent(((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin()) / image.getWidth()) * 100);

            /* Write to the Document */
            document.open();
            document.add(corendonLogo);
            document.add(new Paragraph("Statistics - Total Luggage", fontbold));
            document.add(image);
            document.close();

            /* Display success of file save */
            lblErrorMessage.setText("Successfully saved the Proof of Registration document");
        } catch (IOException | DocumentException ex) {
            Logger.getLogger(LuggageEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnFilterEvent(ActionEvent event) throws ClassNotFoundException, SQLException {
        barChart.getData().clear();
        if (startDate.getValue() != null || endDate.getValue() != null) {
            ChartTools chartTools = new ChartTools();
            LocalDate start = startDate.getValue();
            LocalDate end = endDate.getValue();
            if (end.isAfter(start)) {
                btnPrintStatistics.setDisable(false); //Enable the "Save Statistics" button
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

                        /* Lost luggage */
                        XYChart.Series lostLuggage = new XYChart.Series();
                        lostLuggage.setName("Lost Luggage");
                        ObservableList<ChartTools> lostLuggageList = chartTools.getLostOrFoundLuggage(start, end, interval, 1);
                        for (int i = 0; i < lostLuggageList.size(); i++) {
                            lostLuggage.getData().add(new XYChart.Data(lostLuggageList.get(i).getDate(), lostLuggageList.get(i).getAmount()));
                        }

                        /* Found luggage */
                        XYChart.Series foundLuggage = new XYChart.Series();
                        foundLuggage.setName("Found Luggage");
                        ObservableList<ChartTools> foundLuggageList = chartTools.getLostOrFoundLuggage(start, end, interval, 2);
                        for (int i = 0; i < foundLuggageList.size(); i++) {
                            foundLuggage.getData().add(new XYChart.Data(foundLuggageList.get(i).getDate(), foundLuggageList.get(i).getAmount()));
                        }

                        /* Connected luggage */
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
