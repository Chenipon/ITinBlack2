package Fys.Controllers;

import static Fys.Controllers.LuggageEditController.choosePrinter;
import Fys.Models.Customer;
import Fys.Models.User;
import Fys.Tools.LogTools;
import Fys.Tools.Screen;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;

/**
 * FXML Controller class. This class controls the Customer Edit screen including it's
 * features.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class CustomerEditController implements Initializable {

    @FXML private Label lblUsername, lblErrorMessage, lblRegisterDate, lblRegisterEmployee;
    @FXML private TextField firstName, lastName, phone, address, email;
    @FXML private MenuButton ddwnGender;
    
    private final Screen SCREEN = new Screen();
    private static User currentUser;
    private static Customer editCustomer;
    
    public static void getUser(User user) {
        currentUser = user;
    }
    
    public static void setCustomer(Customer customer)   {
        editCustomer = customer;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        firstName.setText(editCustomer.getFirstName());
        lastName.setText(editCustomer.getLastName());
        ddwnGender.setText(editCustomer.getGender());
        phone.setText(editCustomer.getPhone());
        address.setText(editCustomer.getAddress());
        email.setText(editCustomer.getEmail());
        lblUsername.setText(currentUser.getUsername());
        try {
            lblRegisterDate.setText((new LogTools().getCustomerRegisterDate(editCustomer.getId())).toString());
            lblRegisterEmployee.setText((new LogTools().getCustomerRegisterEmployee(editCustomer.getEmployeeId())));
        } catch (SQLException | ParseException | ClassNotFoundException ex) {
            Logger.getLogger(CustomerEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void ddwnGenderMaleEvent(ActionEvent event) {
        ddwnGender.setText("Male");
        ddwnGender.setPrefWidth(200);
    }
    
    @FXML
    private void ddwnGenderFemaleEvent(ActionEvent event) {
        ddwnGender.setText("Female");
        ddwnGender.setPrefWidth(200);
    }
    
    @FXML
    private void btnSaveChangesEvent(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
        if (!(firstName.getText().equals("") || lastName.getText().equals("") || 
                phone.getText().equals(""))) {
            lblErrorMessage.setText("");
            firstName.setStyle("-fx-border-width: 0px;");
            lastName.setStyle("-fx-border-width: 0px;");
            phone.setStyle("-fx-border-width: 0px;");
            editCustomer.setFirstName(firstName.getText());
            editCustomer.setLastName(lastName.getText());
            editCustomer.setGender(ddwnGender.getText());
            editCustomer.setPhone(phone.getText());
            editCustomer.setAddress(address.getText());
            editCustomer.setEmail(email.getText());
            editCustomer.updateCustomer(editCustomer);
            CustomerOverviewController.getUser(currentUser);
            ((Node) event.getSource()).getScene().getWindow().hide();
            SCREEN.change("CustomerOverview", "Customer Overview");
        } else {
            lblErrorMessage.setText("The highlighted fields can't be empty");
            firstName.setStyle("-fx-text-box-border: red;");
            lastName.setStyle("-fx-text-box-border: red;");
            phone.setStyle("-fx-text-box-border: red;");
        }
    }
    
    @FXML
    private void btnBackToOverviewEvent(ActionEvent event) throws IOException {
        CustomerOverviewController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("CustomerOverview", "Customer Overview");
    }
    
    @FXML
    private void btnPrintProofEvent(ActionEvent event) throws Exception {
        try{
            Document document = new Document();
        String fileName = "/temporaryPrintFileCustomer.pdf";
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
        document.add(new Paragraph("Proof of registration: Customer", fontbold));
        document.add(new Paragraph("Name: " + editCustomer.getFirstName() + " " + editCustomer.getLastName()));
        document.add(new Paragraph("Gender: " + editCustomer.getGender()));
        document.add(new Paragraph("Phone: " + editCustomer.getPhone()));
        document.add(new Paragraph("Address: " + editCustomer.getAddress()));
        document.add(new Paragraph("Email: " + editCustomer.getEmail()));
        document.add(new Paragraph("Register date: " + editCustomer.getRegisterDate()));
        
        
        // step 5
        document.close();
        
        DocFlavor docFlavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        Doc printedDocument = new SimpleDoc(new FileInputStream(fileLocation.getAbsolutePath() + fileName), docFlavor, null);  
        DocPrintJob printJob = choosePrinter().createPrintJob();
        
        printJob.print(printedDocument, new HashPrintRequestAttributeSet());
        } catch(Exception ex) {
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
    private void btnLuggageEvent(ActionEvent event) throws Exception {
        LuggageOverviewController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("LuggageOverview", "Luggage Overview");
    }
    
    @FXML
    private void btnCustomerEvent(ActionEvent event) throws Exception {
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
