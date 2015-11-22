/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Fys;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import Fys.Tools.ConnectMysqlServer;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author John
 */
public class Test extends Application {

    @Override
    public void start(Stage stage) throws ClassNotFoundException, SQLException {
        stage.setTitle("Line Chart Sample");
        //define the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of luggage");
        //creating the chart
        final LineChart<Number, Number> LineChart = new LineChart<>(xAxis, yAxis);
        LineChart.setTitle("Found Luggage");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Found Luggage");
        series.getData().add(new XYChart.Data(1,getFoundLuggage()));
        series.getData().add(new XYChart.Data(2, 50));
        //set scene
        Scene scene = new Scene(LineChart, 800, 600);
        LineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();
    }

    //create class for getting found luggage

    public int getFoundLuggage() throws ClassNotFoundException, SQLException {
        XYChart.Series series = new XYChart.Series();
        int totalFound = 0;
        try (Connection db = new ConnectMysqlServer().dbConnect()) {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM luggage WHERE statusid=1");
            while (result.next()) {
                totalFound++;
                return result.getInt(1);
            }
        }
        return totalFound;
        
    }

    public static void main(String[] args) {
        launch(args);
    }

}
