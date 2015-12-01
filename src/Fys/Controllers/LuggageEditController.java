package Fys.Controllers;

import Fys.Models.Connection;
import Fys.Models.Customer;
import Fys.Models.Luggage;
import Fys.Models.Status;
import Fys.Models.User;
import Fys.Tools.DateConverter;
import Fys.Tools.Screen;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.Path;
import com.itextpdf.tool.xml.html.ParaGraph;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;

/**
 * FXML Controller class. This class controls the Edit Luggage screen including 
 * it's features.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class LuggageEditController implements Initializable {

    @FXML private Label lblUsername, lblErrorMessage, lblFirstName, lblLastName, 
            lblGender, lblPhone, lblAddress, lblEmail;
    @FXML private TextField type, brand, material, color;
    @FXML private TextArea comments;
    @FXML private MenuButton ddwnStatus;
    @FXML private Button btnSelectCustomer;
    @FXML private AnchorPane paneCustomer;
    
    private final Screen SCREEN = new Screen();
    private static User currentUser;
    private static Luggage editLuggage;
    private static Customer connectedCustomer;
    private static Connection connection;
    
    public static void getUser(User user) {
        currentUser = user;
    }
    
    public static void setLuggage(Luggage luggage) {
        editLuggage = luggage;
    }
    
    public static void setCustomer(Customer customer) {
        connectedCustomer = customer;
    }
    
    /** initialize(URL url, ResourceBundle rb) executes before the FXML gets
     * loaded and initialized. This method is used to initialize all text and
     * information before being displayed.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(currentUser.getUsername());
        type.setText(editLuggage.getType());
        brand.setText(editLuggage.getBrand());
        material.setText(editLuggage.getMaterial());
        color.setText(editLuggage.getColor());
        comments.setText(editLuggage.getComment());
        
        try { 
            ddwnStatus.setText(new Status().getStatusById(editLuggage.getStatusId()).getStatusName());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LuggageEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
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
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LuggageEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Enable the right buttons and panes on the scene based off their data.
        if (ddwnStatus.getText().equals("Connected")) {
            btnSelectCustomer.setVisible(true);
            if (connectedCustomer != null) {
                paneCustomer.setVisible(true);
            }
        } else {
            paneCustomer.setVisible(false);
            btnSelectCustomer.setVisible(false);
        }
    } 
    
    /** setLabelText() fills the labels displaying the Customer information, when
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
    
    /** ddwnStatusLostEvent() replaces the text in the Status
     * dropdown with "Lost" and sets the width to the default one of 200. Also
     * disables the "Select Customer" button if visable.
     */
    @FXML
    private void ddwnStatusLostEvent() {
        ddwnStatus.setText("Lost");
        btnSelectCustomer.setVisible(false);
        ddwnStatus.setPrefWidth(200);
    }
    
    /** ddwnStatusFoundEvent() replaces the text in the Status
     * dropdown with "Found" and sets the width to the default one of 200. Also
     * disables the "Select Customer" button if visable.
     */    
    @FXML
    private void ddwnStatusFoundEvent() {
        ddwnStatus.setText("Found");
        btnSelectCustomer.setVisible(false);
        ddwnStatus.setPrefWidth(200);
    }

    /** ddwnStatusConnectedEvent() replaces the text in the Status
     * dropdown with "Lost" and sets the width to the default one of 200. Also
     * enables the "Select Customer" button.
     */    
    @FXML
    private void ddwnStatusConnectedEvent() {
        ddwnStatus.setText("Connected");
        btnSelectCustomer.setVisible(true);
        ddwnStatus.setPrefWidth(200);
    }
    
    /** btnSelectCustomerEvent(ActionEvent event) switches screen to the 
     * LuggageSelectCustomer FXML screen. Also sends the Luggage and User objects
     * to the controller of the LuggageSelectCustomer FXML screen.
     */     
    @FXML
    private void btnSelectCustomerEvent(ActionEvent event) throws IOException {
        LuggageSelectCustomerController.getUser(currentUser);
        LuggageSelectCustomerController.setLuggage(editLuggage);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("LuggageSelectCustomer", "Select Customer");
    }
    
    @FXML
    private void btnSaveChangesEvent(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
        if (!(type.getText().equals("") || brand.getText().equals("") || 
                material.getText().equals("") || color.getText().equals(""))) {
            if (ddwnStatus.getText().equals("Connected") && connectedCustomer == null) {
                lblErrorMessage.setText("Please select a customer to connect to the luggage before saving");
                return;
            }
            if (ddwnStatus.getText().equals("Connected")) {
                if (editLuggage.checkIfLuggageIsConnected(editLuggage) && (connectedCustomer.getId() != connection.getCustomerId())) {
                    connection.setCustomerId(connectedCustomer.getId());
                    connection.setConnectionDate(new DateConverter().getCurrentDateInSqlFormat());
                    connection.updateConnection(connection);
                } else if (editLuggage.checkIfLuggageIsConnected(editLuggage) && connectedCustomer.getId() == connection.getCustomerId()) {
                    System.out.println("USER HAS NOT BEEN CHANGED");
                } else {
                    connection = new Connection();
                    connection.setCustomerId(connectedCustomer.getId());
                    connection.setLuggageId(editLuggage.getId());
                    connection.setConnectionDate(new DateConverter().getCurrentDateInSqlFormat());
                    connection.insertConnection(connection);
                }
            } else if (editLuggage.checkIfLuggageIsConnected(editLuggage)) {
                connectedCustomer = null;
                connection.deleteConnection(connection);
            }
            lblErrorMessage.setText("");
            type.setStyle("-fx-border-width: 0px;");
            brand.setStyle("-fx-border-width: 0px;");
            material.setStyle("-fx-border-width: 0px;");
            color.setStyle("-fx-border-width: 0px;");
            
            editLuggage.setType(type.getText());
            editLuggage.setBrand(brand.getText());
            editLuggage.setMaterial(material.getText());
            editLuggage.setColor(color.getText());
            editLuggage.setComment(comments.getText());
            editLuggage.setStatusId(new Status().getStatusByName(ddwnStatus.getText()).getId());
            editLuggage.updateLuggage(editLuggage);
            
            connectedCustomer = null;
            LuggageOverviewController.getUser(currentUser);
            ((Node) event.getSource()).getScene().getWindow().hide();
            SCREEN.change("LuggageOverview", "Luggage Overview");
        } else {
            lblErrorMessage.setText("Highlighted fields cannot be empty");
            type.setStyle("-fx-text-box-border: red;");
            brand.setStyle("-fx-text-box-border: red;");
            material.setStyle("-fx-text-box-border: red;");
            color.setStyle("-fx-text-box-border: red;");
        }
    }
    
    @FXML
    private void btnPrintProofEvent(ActionEvent event) throws DocumentException, Exception {
        try{
            Document document = new Document();
        String fileName = "/temporaryPrintFileLuggage.pdf";
        File fileLocation = new File(new File("temp").getAbsolutePath());
        Image corendonLogo = Image.getInstance("src/Fys/Content/Image/corendonlogo.jpg");
        Font fontbold = FontFactory.getFont("Arial", 18, Font.BOLD);
        if (!fileLocation.exists()) {
            fileLocation.mkdir();
            System.out.println("directory temp created");
        }
        
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileLocation.getAbsolutePath() + fileName));
        document.open();
        // step 4
        corendonLogo.scalePercent(20f);
        document.add(corendonLogo);
        document.add(new Paragraph("Proof of registration: Luggage", fontbold));
        document.add(new Paragraph("Type: " + editLuggage.getType()));
        document.add(new Paragraph("Brand: " + editLuggage.getBrand()));
        document.add(new Paragraph("Color: " + editLuggage.getColor()));
        document.add(new Paragraph("Material: " + editLuggage.getMaterial()));
        document.add(new Paragraph("Status: " + editLuggage.getStatus().getStatusName()));
        document.add(new Paragraph("Register date: " + editLuggage.getRegisterDate()));
        
        
        // step 5
        document.close();
        
        DocFlavor docFlavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        Doc printedDocument = new SimpleDoc(new FileInputStream(fileLocation.getAbsolutePath() + fileName), docFlavor, null);  
        DocPrintJob printJob = choosePrinter().createPrintJob();
        
        printJob.print(printedDocument, new HashPrintRequestAttributeSet());
        } catch(Exception ex){
            System.out.println(ex.toString());
            lblErrorMessage.setText("Somthing went wrong, your request is not printed.");
        }
    }
    public static PrintService choosePrinter(){
        PrinterJob printJob = PrinterJob.getPrinterJob();
        if(printJob.printDialog()) {
        return printJob.getPrintService();          
        }
        else {
            return null;
        }
    }
    
    @FXML
    private void btnBackToOverviewEvent(ActionEvent event) throws IOException {
        connectedCustomer = null;
        LuggageOverviewController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("LuggageOverview", "Luggage Overview");
    }
    
    @FXML
    private void btnLuggageEvent(ActionEvent event) throws IOException {
        connectedCustomer = null;
        LuggageOverviewController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("LuggageOverview", "Luggage Overview");
    }
    
    @FXML
    private void btnCustomerEvent(ActionEvent event) throws IOException {
        connectedCustomer = null;
        CustomerOverviewController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("CustomerOverview", "Customer Overview");
    }
    
    @FXML
    private void btnLogoutEvent(ActionEvent event) throws IOException {
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("Login", "Login");
    }   
    
}
