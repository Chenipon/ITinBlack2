package Fys.Controllers;

import Fys.Models.Customer;
import Fys.Views.ViewModels.LogCustomerTabelView;
import Fys.Views.ViewModels.LuggageTabelView;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class. This class controls the Edit Luggage screen including 
 * it's features.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class LogCustomerController implements Initializable {
    private static Customer customer;
    private static Stage stage;
    
    @FXML private TableView tblCustomerLog;
    @FXML private TableColumn colDate, colEmployee, colLogEntry;
    
    public static void setCustomer(Customer editCustomer) {
        customer = editCustomer;
    }
    
    public static void setStage(Stage newStage) {
        stage = newStage;
    }
    
    /** initialize(URL url, ResourceBundle rb) executes before the FXML gets
     * loaded and initialized. This method is used to initialize all text and
     * information before being displayed.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colDate.setCellValueFactory(new PropertyValueFactory<LuggageTabelView, String>("registerdate"));
        colEmployee.setCellValueFactory(new PropertyValueFactory<LuggageTabelView, String>("employee"));
        colLogEntry.setCellValueFactory(new PropertyValueFactory<LuggageTabelView, String>("change"));
        try {
            tblCustomerLog.setItems(getLogList());
        } catch (Exception ex) {
            Logger.getLogger(LogCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ObservableList<LogCustomerTabelView> getLogList() throws Exception{
        ObservableList<LogCustomerTabelView> logList= new LogCustomerTabelView().getLogList(customer);
        return logList;
    }
    
    @FXML
    public void btnCloseWindowEvent(ActionEvent event) {
        stage.close();
    }
    
}
