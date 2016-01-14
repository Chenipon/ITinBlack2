package Fys.Controllers;

import Fys.Models.User;
import Fys.Tools.Screen;
import Fys.Views.ViewModels.AccountTabelView;
import java.io.IOException;
import java.net.URL;
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

    private static Screen screen;
    private static User currentUser;

    @FXML
    private Label lblUsername;
    @FXML
    private TableView tblUsers;
    @FXML
    private TableColumn columnUsername, columnFirstname, columnLastname,
            columnRole, columnActive, columnAction;
    @FXML
    private TextField lblSearch;

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
        columnUsername.setCellValueFactory(
                new PropertyValueFactory<AccountTabelView, String>("username"));
        columnFirstname.setCellValueFactory(
                new PropertyValueFactory<AccountTabelView, String>("firstname"));
        columnLastname.setCellValueFactory(
                new PropertyValueFactory<AccountTabelView, String>("lastname"));
        columnRole.setCellValueFactory(
                new PropertyValueFactory<AccountTabelView, String>("role"));
        columnActive.setCellValueFactory(
                new PropertyValueFactory<AccountTabelView, String>("active"));
        columnAction.setCellValueFactory(
                new PropertyValueFactory<AccountTabelView, String>("username"));
        Callback<TableColumn<AccountTabelView, String>, 
                TableCell<AccountTabelView, String>> printColumnCellFactory
                = new Callback<TableColumn<AccountTabelView, String>,
                        TableCell<AccountTabelView, String>>() {
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
                                            param.getTableView().getSelectionModel()
                                            .select(getIndex());
                                            AccountTabelView item = 
                                                    (AccountTabelView) tblUsers
                                            .getSelectionModel().getSelectedItem();
                                            if (item != null) {
                                                try {
                                                    User editUser = new User()
                                                    .getUserById(item.getId());
                                                    AccountEditController
                                                    .setUser(currentUser);
                                                    AccountEditController
                                                    .setScreen(screen);
                                                    AccountEditController
                                                    .setEditUser(editUser);
                                                    screen.change("AccountEdit");
                                                } catch (Exception ex) {
                                                    Logger.getLogger(
                                                            AccountOverviewController.class
                                                            .getName()).log(Level.SEVERE,
                                                            null, ex);
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
            Logger.getLogger(AccountOverviewController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    /**
     * ObservableList<AccountTabelView> getUserList() returns an ObservableList
     * that can be used in the TableView.
     *
     * @return an ObservableList containing the information that is added into
     * the TableView.
     * @throws Exception when no connection with the Database could be
     * established.
     */
    public ObservableList<AccountTabelView> getUserList() throws Exception {
        return new AccountTabelView().getAccountList();
    }

    /**
     * void btnAddAccountEvent(ActionEvent event) changes the scene of the Stage
     * to the Account Add scene. This scene is being used to add new users into
     * the database so that they can be used to log into the application.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws IOException when the FXML file could not be loaded.
     */
    @FXML
    private void btnAddAccountEvent(ActionEvent event) throws IOException {
        AccountAddController.setUser(currentUser);
        AccountAddController.setScreen(screen);
        screen.change("AccountAdd");
    }

    /**
     * void btnSearchAccountEvent(ActionEvent event) fills the ObservableList of
     * the TableView with a new one using a search term to filter the results.
     * The SQL query is being filtered here.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws Exception when no connection with the Database could be
     * established.
     */
    @FXML
    private void btnSearchAccountEvent(ActionEvent event) throws Exception {
        if (lblSearch.getText().equals("")) {
            tblUsers.setItems(getUserList());
        } else {
            ObservableList<AccountTabelView> userList = new AccountTabelView()
                    .getAccountList(lblSearch.getText());
            tblUsers.setItems(userList);
        }
    }

    /**
     * void btnLogoutEvent(ActionEvent event) logs the current User out of the
     * application and displays the Login screen.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws IOException when the FXML file could not be loaded.
     */
    @FXML
    private void btnLogoutEvent(ActionEvent event) throws Exception {
        LoginController.setScreen(screen);
        ((Node) event.getSource()).getScene().getWindow().hide();
        screen.change("Login");
    }

}
