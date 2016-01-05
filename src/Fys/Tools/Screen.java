package Fys.Tools;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class handles quick changing of screens without having to define this
 * method over and over again at every screen switch event.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class Screen {

    private final Stage stage;

    /**
     * Constructor Screen(Stage stage) assigns Stage stage to the local
     * attribute stage, which is a final attribute.
     *
     * @param stage The stage that gets sent at application launch. This is
     * final and cannot be changed after the constuctor has been called in the
     * startup class.
     */
    public Screen(Stage stage) {
        this.stage = stage;
    }

    /**
     * Method change(String fxmlName) switches the screen to the screen that is
     * defined in the parameter. Remember to also send the "screen" element
     * between each class or this Method will give a NullPointerException!
     *
     * @param fxmlName is a String of the name of the FXML file. This is the
     * name only, and does NOT contain the .fxml extension in the String.
     * @throws IOException is thrown when the FXML file is not found.
     */
    public void change(String fxmlName) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/Fys/Views/"
                + fxmlName + ".fxml")));
        scene.getStylesheets().add("/Fys/Content/Css/stylesheet.css");
        if (fxmlName.equals("Login")) {
            stage.setResizable(false);
            stage.setMinWidth(616);
            stage.setMinHeight(438);
            stage.setWidth(616);
            stage.setHeight(438);
        } else {
            stage.setResizable(true);
            stage.setMinWidth(1016);
            stage.setMinHeight(738);
        }
        stage.setScene(scene);
        stage.show();
    }
}