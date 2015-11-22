package Fys.Tools;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * This class handles quick changing of screens without having to define this
 * method over and over again at every button.
 * @author Daan Befort, IS106-2
 */
public class Screen {

    /**
     * public Stage change(String viewName, String stageTitle)
     * @param fxmlName String viewName is the title of the FXML File.
     * @param stageTitle String stageTitle is the title of the stage on top of the screen.
     * @return Stage stage, which opens the stage in a new window.
     * @throws IOException
     */
    public Stage change(String fxmlName, String stageTitle) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fys/Views/" + fxmlName + ".fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene((Pane) loader.load()));
        stage.getScene().getStylesheets().add("/Fys/Content/Css/stylesheet.css");
        stage.setTitle(stageTitle);

        stage.show();

        return stage;
    }
}
