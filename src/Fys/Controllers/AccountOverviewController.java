package Fys.Controllers;

import Fys.Models.User;
import Fys.Tools.Screen;
import Fys.Views.ViewModels.AccountTabelView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * FXML Controller class. This class controls the Account Overview screen
 * including it's features.
 *
 * @author Daan Befort, Jeffrey van der Lingen, IS106-2
 */
public class AccountOverviewController implements Initializable {

    @FXML private Label lblUsername;
    @FXML private TableView tblUsers;
    @FXML private TableColumn columnUsername;
    @FXML private TableColumn columnFirstname;
    @FXML private TableColumn columnLastname;
    @FXML private TableColumn columnRole;
    @FXML private TableColumn columnActive;
    @FXML private TableColumn columnAction;
    @FXML private TextField lblSearch;

    private final Screen SCREEN = new Screen();
    private static User currentUser;

    public static void getUser(User user) {
        currentUser = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(currentUser.getUsername());
        columnUsername.setCellValueFactory(new PropertyValueFactory<AccountTabelView, String>("username"));
        columnFirstname.setCellValueFactory(new PropertyValueFactory<AccountTabelView, String>("firstname"));
        columnLastname.setCellValueFactory(new PropertyValueFactory<AccountTabelView, String>("lastname"));
        columnRole.setCellValueFactory(new PropertyValueFactory<AccountTabelView, String>("role"));
        columnActive.setCellValueFactory(new PropertyValueFactory<AccountTabelView, String>("active"));
        columnAction.setCellValueFactory(new PropertyValueFactory<AccountTabelView, String>("username")); 
        Callback<TableColumn<AccountTabelView, String>, TableCell<AccountTabelView, String>> printColumnCellFactory = //
                new Callback<TableColumn<AccountTabelView, String>, TableCell<AccountTabelView, String>>() {

            @Override
            public TableCell call(final TableColumn param) {
                final TableCell cell = new TableCell() {

                    @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            final Button btnPrint = new Button("Edit");
                            btnPrint.setOnAction(new EventHandler<ActionEvent>() {

                                @Override
                                public void handle(ActionEvent event) {
                                    param.getTableView().getSelectionModel().select(getIndex());
                                    AccountTabelView item = (AccountTabelView)tblUsers.getSelectionModel().getSelectedItem();
                                    if (item != null) {
                                        try {
                                            User editUser = new User().getUserById(item.getId());
                                            AccountEditController.getUser(currentUser);
                                            AccountEditController.setEditUser(editUser);
                                            ((Node) event.getSource()).getScene().getWindow().hide();
                                            SCREEN.change("AccountEdit", "Add Account");
                                        } catch (Exception ex) {
                                            Logger.getLogger(AccountOverviewController.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }
                            });
                            setGraphic(btnPrint);
                            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                        }
                    }
                };
                return cell;
            }
        };
        columnAction.setCellFactory(printColumnCellFactory);
        
        try {
            tblUsers.setItems(getUserList());
        } catch (Exception ex) {
            Logger.getLogger(AccountOverviewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ObservableList<AccountTabelView> getUserList() throws Exception{
        ObservableList<AccountTabelView> userList= new AccountTabelView().getAccountList();
        return userList;
    }
    
    @FXML
    private void btnAddAccountEvent(ActionEvent event) throws IOException {
        AccountAddController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("AccountAdd", "Add Account");
    }

    @FXML
    private void btnSearchAccountEvent(ActionEvent event) throws Exception {
        if (lblSearch.getText().equals("")) {
            tblUsers.setItems(getUserList());
        } else {
            ObservableList<AccountTabelView> userList = new AccountTabelView().getAccountList(lblSearch.getText());
            tblUsers.setItems(userList);
        }
    }

    @FXML
    private void btnLogoutEvent(ActionEvent event) throws Exception{
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("Login", "Login");
    }

}
