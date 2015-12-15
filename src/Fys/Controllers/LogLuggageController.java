package Fys.Controllers;

import Fys.Models.Luggage;
import Fys.Views.ViewModels.LogLuggageTabelView;
import Fys.Views.ViewModels.LuggageTabelView;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class. This class controls the Edit Luggage screen including 
 * it's features.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class LogLuggageController implements Initializable {
    private static Luggage luggage;
    
    @FXML private TableView tblLuggageLog;
    @FXML private TableColumn colDate, colEmployee, colLogEntry;
    
    public static void setLuggage(Luggage editLuggage) {
        luggage = editLuggage;
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
            tblLuggageLog.setItems(getLogList());
        } catch (Exception ex) {
            Logger.getLogger(LuggageOverviewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ObservableList<LogLuggageTabelView> getLogList() throws Exception{
        ObservableList<LogLuggageTabelView> logList= new LogLuggageTabelView().getLogList(luggage);
        return logList;
    }
    
}
