package Fys;

import Fys.Controllers.LoginController;
import Fys.Tools.Screen;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Daan
 */
public class Startup extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Corendon Lost Luggage System");
        stage.getIcons().add(new Image("/Fys/Content/Image/corendonicon.png"));
        
        Screen SCREEN = new Screen(stage);
        
        LoginController.setScreen(SCREEN);
        SCREEN.change("Login");
    }
    
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

}
