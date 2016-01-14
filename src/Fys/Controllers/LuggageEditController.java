package Fys.Controllers;

import Fys.Models.Connection;
import Fys.Models.Customer;
import Fys.Models.Luggage;
import Fys.Models.Status;
import Fys.Models.User;
import Fys.Tools.DateConverter;
import Fys.Tools.LogTools;
import Fys.Tools.Screen;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class. This class controls the Edit Luggage screen including
 * it's features.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class LuggageEditController implements Initializable {

    private static Screen screen;
    private static User currentUser;
    private static Luggage editLuggage;
    private static Customer connectedCustomer;
    private static Connection connection;

    @FXML private Label lblUsername, lblErrorMessage, lblFirstName, lblLastName,
            lblGender, lblPhone, lblAddress, lblEmail, lblRegisterDate, lblRegisterEmployee;
    @FXML private TextField type, brand, material, color;
    @FXML private TextArea comments;
    @FXML private MenuButton ddwnStatus;
    @FXML private AnchorPane paneCustomer;
    @FXML private CheckBox chckResolved;
    @FXML private Button btnRemoveCustomer;

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
     * void setLuggage(Luggage luggage)  sets the luggage for the Controller.
     * This sets the luggage information that is needed for this controller.
     *
     * @param luggage is the user that needs to be set in this class.
     */
    public static void setLuggage(Luggage luggage) {
        editLuggage = luggage;
    }

    /**
     * void setCustomer(Customer customer)  sets the customer for the Controller.
     * This sets the customer information that is needed for this controller.
     *
     * @param customer is the user that needs to be set in this class.
     */
    public static void setCustomer(Customer customer) {
        connectedCustomer = customer;
    }

    /**
     * initialize(URL url, ResourceBundle rb) executes before the FXML gets
     * loaded and initialized. This method is used to initialize all text and
     * information before being displayed.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /* Initialize an empty Connection */
        connection = null;
        
        /* Initialize the Textfields */
        setLuggageData();

        try {
            /* Initialize the labels of the luggage history and the statusid */
            ddwnStatus.setText(new Status().getStatusById(editLuggage.getStatusId()).getStatusName());
            lblRegisterDate.setText(new LogTools().getLuggageRegisterDate(editLuggage.getId()).toString());
            lblRegisterEmployee.setText((new LogTools().getLuggageRegisterEmployee(editLuggage.getEmployeeId())));
            /* Initialize the connection */
            /* if there is a Connection of Luggage and Customer registered in the database,
             * initialize this Connection.
             */
            if (editLuggage.checkIfLuggageIsConnected(editLuggage)) {
                connection = new Connection().getConnectionByLuggageId(editLuggage.getId());
            }
            /* if there is a Connection of Luggage and Customer, and the Customer has
             * not yet been initialized, initialize the Customer from the Connection
             * and display the information of this Customer. This can happen when
             * a Connection has been registered on the selected Luggage, but no
             * Customer has been initialized yet.
             */
            if (editLuggage.checkIfLuggageIsConnected(editLuggage) && connectedCustomer == null) {
                connectedCustomer = new Customer().getCustomerById(connection.getCustomerId());
                setLabelText();
                /* else if the Customer has been initialized, display the text from this 
                 * initialized Customer.
                 */
            } else if (connectedCustomer != null) {
                setLabelText();
            }
        } catch (ClassNotFoundException | SQLException | ParseException ex) {
            Logger.getLogger(LuggageEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
        /* Enable the right buttons and panes on the scene based off their data. */
        if (connectedCustomer != null) {
            paneCustomer.setVisible(true);
            btnRemoveCustomer.setVisible(true);
        } else {
            paneCustomer.setVisible(false);
            btnRemoveCustomer.setVisible(false);
        }
    }

    /**
     * setLabelText() fills the labels displaying the Customer information, when
     * a connectedCustomer has been found.
     */
    public void setLabelText() {
        lblFirstName.setText(connectedCustomer.getFirstName());
        lblLastName.setText(connectedCustomer.getLastName());
        lblGender.setText(connectedCustomer.getGender());
        lblPhone.setText(connectedCustomer.getPhone());
        lblAddress.setText(connectedCustomer.getAddress());
        lblEmail.setText(connectedCustomer.getEmail());
    }
    
    /**
     * setLuggageData() fills the textfields displaying the Luggage information 
     * so that these fields can be edited. Also sets the checkbox displaying 
     * whether or not the luggage has been resolved.
     */
    public void setLuggageData() {
        /* Initialize textfields */
        lblUsername.setText(currentUser.getUsername());
        type.setText(editLuggage.getType());
        brand.setText(editLuggage.getBrand());
        material.setText(editLuggage.getMaterial());
        color.setText(editLuggage.getColor());
        comments.setText(editLuggage.getComment());
        /* Initialize checkbox */
        if (editLuggage.isResolved()) {
            chckResolved.selectedProperty().set(true);
        } else {
            chckResolved.selectedProperty().set(false);
        }
    }

    /**
     * ddwnStatusLostEvent() replaces the text in the Status dropdown with
     * "Lost" and sets the width to the default one of 200. Also disables the
     * "Select Customer" button if visible.
     */
    @FXML
    private void ddwnStatusLostEvent() {
        ddwnStatus.setText("Lost");
        ddwnStatus.setPrefWidth(200);
    }

    /**
     * ddwnStatusFoundEvent() replaces the text in the Status dropdown with
     * "Found" and sets the width to the default one of 200. Also disables the
     * "Select Customer" button if visible.
     */
    @FXML
    private void ddwnStatusFoundEvent() {
        ddwnStatus.setText("Found");
        ddwnStatus.setPrefWidth(200);
    }
    
    /**
     * btnRemoveCustomerEvent() removes the connected customer from the selected customer.
     * 
     * @throws ClassNotFoundException when the class could not be found.
     * @throws SQLException when no connection with the Database could be
     * established.
     */
    @FXML
    private void btnRemoveCustomerEvent() throws ClassNotFoundException, SQLException {
        if (editLuggage.checkIfLuggageIsConnected(editLuggage)) {
            connection.deleteConnection(connection);
        }
        connection = null;
        connectedCustomer = null;
        paneCustomer.setVisible(false);
        btnRemoveCustomer.setVisible(false);
    }

    /**
     * void btnSelectCustomerEvent(ActionEvent event) changes the scene of the Stage
     * to the Select Customer scene. This scene is being used to connect a customer to the selected luggage.
     * 
     * @param event The event that is being fired by clicking the button.
     * @throws IOException when the FXML file could not be loaded.
     */
    @FXML
    private void btnSelectCustomerEvent(ActionEvent event) throws IOException {
        LuggageSelectCustomerController.setUser(currentUser);
        LuggageSelectCustomerController.setLuggage(editLuggage);
        LuggageSelectCustomerController.setScreen(screen);
        screen.change("LuggageSelectCustomer");
    }

    /**
     * void btnSaveChangesEvent(ActionEvent event) saves the changes made to
     * editCustomer and returns to the Customer Overview scene.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws ClassNotFoundException when the class could not be found.
     * @throws SQLException when no connection with the Database could be
     * established.
     * @throws IOException when the FXML file could not be loaded.
     */
    @FXML
    private void btnSaveChangesEvent(ActionEvent event) throws IOException,
            ClassNotFoundException, SQLException {
        if (!(type.getText().equals("") || brand.getText().equals("")
                || material.getText().equals("") || color.getText().equals(""))) {
            if (chckResolved.selectedProperty().get() && connectedCustomer == null) {
                lblErrorMessage.setText("Please select a customer to connect to the luggage before saving");
                return;
            }
            
            if (editLuggage.checkIfLuggageIsConnected(editLuggage)
                    && (connectedCustomer.getId() != connection.getCustomerId())) {
                connection.setCustomerId(connectedCustomer.getId());
                connection.updateConnection(connection);
                //Customer has been changed
            } else if (connectedCustomer != null && connection == null) {
                connection = new Connection();
                connection.setCustomerId(connectedCustomer.getId());
                connection.setLuggageId(editLuggage.getId());
                connection.insertConnection(connection);
                //No connection exists yet
            }

            lblErrorMessage.setText("");
            type.setStyle("-fx-border-width: 0px;");
            brand.setStyle("-fx-border-width: 0px;");
            material.setStyle("-fx-border-width: 0px;");
            color.setStyle("-fx-border-width: 0px;");

            LogTools logTools = new LogTools();
            editLuggage.setType(type.getText());
            if (logTools.checkLuggageChanged(editLuggage, "type")) {
                logTools.logLuggageChanged(editLuggage, currentUser, "type");
            }
            editLuggage.setBrand(brand.getText());
            if (logTools.checkLuggageChanged(editLuggage, "brand")) {
                logTools.logLuggageChanged(editLuggage, currentUser, "brand");
            }
            editLuggage.setMaterial(material.getText());
            if (logTools.checkLuggageChanged(editLuggage, "material")) {
                logTools.logLuggageChanged(editLuggage, currentUser, "material");
            }
            editLuggage.setColor(color.getText());
            if (logTools.checkLuggageChanged(editLuggage, "color")) {
                logTools.logLuggageChanged(editLuggage, currentUser, "color");
            }
            editLuggage.setComment(comments.getText());
            if (logTools.checkLuggageChanged(editLuggage, "comment")) {
                logTools.logLuggageChanged(editLuggage, currentUser, "comment");
            }
            editLuggage.setStatusId(new Status().getStatusByName(ddwnStatus.getText()).getId());
            if (logTools.checkLuggageChanged(editLuggage, "status")) {
                logTools.logLuggageChanged(editLuggage, currentUser, "status");
            }
            if (chckResolved.selectedProperty().getValue()) {
                editLuggage.setResolved(true);
            } else {
                editLuggage.setResolved(false);
            }
            if (logTools.checkLuggageChanged(editLuggage, "resolved")) {
                logTools.logLuggageChanged(editLuggage, currentUser, "resolved");
            }
            
            if ((chckResolved.selectedProperty().get() != 
                    new Luggage().getLuggageById(editLuggage.getId()).isResolved())) {
                if (chckResolved.selectedProperty().get()) {
                    editLuggage.setResolveDate(
                            new DateConverter().getCurrentDateInSqlFormat());
                } else {
                    editLuggage.setResolveDate(null);
                }
            }
            
            editLuggage.updateLuggage(editLuggage);
            
            connectedCustomer = null;
            LuggageOverviewController.setUser(currentUser);
            LuggageOverviewController.setScreen(screen);
            screen.change("LuggageOverview");
        } else {
            lblErrorMessage.setText("Highlighted fields cannot be empty");
            type.setStyle("-fx-text-box-border: red;");
            brand.setStyle("-fx-text-box-border: red;");
            material.setStyle("-fx-text-box-border: red;");
            color.setStyle("-fx-text-box-border: red;");
        }
    }

    /**
     * void btnPrintProofEvent(ActionEvent event) opens the file explorer to save the proof as pdf.
     * The saved document can be printed from the computer location.
     *
     * @param event The event that is being fired by clicking the button.
     */
    @FXML
    private void btnPrintProofEvent(ActionEvent event) {
        try {
            /* Create new FileChooser */
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("Proof of Registration - luggage" + editLuggage.getId());
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF file (*.pdf)", "*.pdf"));
            
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
            Font fontbold = FontFactory.getFont("Arial", 18, Font.BOLD);
            
            /* Write to the Document */
            document.open();
            corendonLogo.scalePercent(20f);
            document.add(corendonLogo);
            document.add(new Paragraph("Proof of registration: Luggage", fontbold));
            document.add(new Paragraph("Type: " + editLuggage.getType()));
            document.add(new Paragraph("Brand: " + editLuggage.getBrand()));
            document.add(new Paragraph("Color: " + editLuggage.getColor()));
            document.add(new Paragraph("Material: " + editLuggage.getMaterial()));
            document.add(new Paragraph("Status: " + editLuggage.getStatus().getStatusName()));
            document.add(new Paragraph("Register date: " + editLuggage.getRegisterDate()));
            
            if (connectedCustomer.getId() != 0) {
                document.add(new Paragraph(" "));
                document.add(new Paragraph("Connected customer", fontbold));
                document.add(new Paragraph("Name: " + connectedCustomer.getFullName()));
                document.add(new Paragraph("Address: " + connectedCustomer.getAddress()));
                document.add(new Paragraph("Email: " + connectedCustomer.getEmail()));
                document.add(new Paragraph("Telephone number: " + connectedCustomer.getPhone()));
            }
            document.close();
            
            /* Display success of file save */
            lblErrorMessage.setText("Successfully saved the Proof of Registration document");
        } catch (IOException | DocumentException ex) {
            Logger.getLogger(LuggageEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * void btnShowHistoryEvent(ActionEvent event) changes the scene of the Stage
     * to the history log scene. This scene is being used to add new customers into
     * the database so that they can be notified when their luggage is found.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws IOException when the FXML file could not be loaded.
     */
    @FXML
    private void btnShowHistoryEvent(ActionEvent event) throws IOException {
        LogLuggageController.setLuggage(editLuggage);
        Stage logStage = new Stage();
        LogLuggageController.setStage(logStage);
        Scene logScene = new Scene(FXMLLoader.load(getClass().getResource("/Fys/Views/LogLuggage.fxml")));
        logScene.getStylesheets().add("/Fys/Content/Css/stylesheet.css");
        logStage.setTitle("Corendon Lost Luggage System");
        logStage.getIcons().add(new javafx.scene.image.Image("/Fys/Content/Image/corendonicon.png"));
        logStage.setScene(logScene);
        logStage.show();
    }

    /**
     * void btnBackToOverviewEvent(ActionEvent event) returns the user back to
     * the Luggage Overview screen. This is the button next to the "Save
     * Changes" button.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws IOException when the FXML file could not be loaded.
     */
    @FXML
    private void btnBackToOverviewEvent(ActionEvent event) throws IOException {
        connectedCustomer = null;
        LuggageOverviewController.setUser(currentUser);
        LuggageOverviewController.setScreen(screen);
        screen.change("LuggageOverview");
    }

    /**
     * void btnLuggageEvent(ActionEvent event) is the button on the left of the
     * screen inside the red bar that returns to the Luggage Overview scene.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws IOException when the FXML file could not be loaded.
     */
    @FXML
    private void btnLuggageEvent(ActionEvent event) throws IOException {
        connectedCustomer = null;
        LuggageOverviewController.setUser(currentUser);
        LuggageOverviewController.setScreen(screen);
        screen.change("LuggageOverview");
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
        connectedCustomer = null;
        CustomerOverviewController.setUser(currentUser);
        CustomerOverviewController.setScreen(screen);
        screen.change("CustomerOverview");
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
