package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import simu.framework.Trace;

import java.io.File;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Correctly locate the FXML file relative to the execution directory (PestelViz)
        URL fxmlUrl = new File("resources/MainView.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(fxmlUrl);
        
        primaryStage.setTitle("PESTEL Simulation Visualizer");
        
        Scene scene = new Scene(root, 1400, 900);
        scene.getStylesheets().add(new File("resources/style.css").toURI().toURL().toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Initialize the simulation's logging system before anything else
        Trace.setTraceLevel(Trace.Level.INFO);
        
        // This is needed to allow JavaFX to run on systems that might not have it fully integrated
        System.setProperty("javafx.graphics", "com.sun.javafx.sg.prism.sw.SWPipeline");
        launch(args);
    }
}
