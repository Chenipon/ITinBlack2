package Fys;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Daan
 */
public class Startup extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Fys/Views/Login.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("Fys/Content/Css/stylesheet.css");
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
