package app.controller;

import app.model.SimulationModel;
import app.model.SimulationUpdate;
import app.model.TemporalVectorManager;
import app.model.Vector3DRenderer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import simu.model.EnhancedFutureScenarioManager;

import java.util.Map;
import java.util.List;

public class MainViewController {

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    @FXML
    private Slider speedSlider;

    @FXML
    private Button dbButton;

    @FXML
    private Button randomButton;

    @FXML
    private ListView<String> eventsListView;

    @FXML
    private Canvas vectorCanvas;

    @FXML
    private VBox pestelStateBox;

    @FXML
    private TextField startDayField;

    @FXML
    private TextField endDayField;

    @FXML
    private Button updateTimeframeButton;

    private SimulationModel simulationModel;
    private ObservableList<String> eventMessages = FXCollections.observableArrayList();
    private TemporalVectorManager temporalVectorManager;
    private Vector3DRenderer vector3DRenderer;
    private SimulationUpdate latestUpdate;

    @FXML
    void startSimulation() {
        System.out.println("Start Simulation button clicked");
        simulationModel.start();
        
        // Listen for updates from the simulation task
        simulationModel.getSimulationTask().valueProperty().addListener((obs, oldUpdate, newUpdate) -> {
            if (newUpdate != null) {
                Platform.runLater(() -> updateUI(newUpdate));
            }
        });
        
        startButton.setDisable(true);
        stopButton.setDisable(false);
    }

    @FXML
    void stopSimulation() {
        System.out.println("Stop Simulation button clicked");
        simulationModel.stop();
        startButton.setDisable(false);
        stopButton.setDisable(true);
    }

    @FXML
    void setDbMode() {
        System.out.println("DB Mode selected");
        // Logic to switch to DB-based LLM responses
    }

    @FXML
    void setRandomMode() {
        System.out.println("Random Mode selected");
        // Logic to switch to clever random responses
    }

    @FXML
    void updateTimeframe() {
        try {
            int startDay = Integer.parseInt(startDayField.getText());
            int endDay = Integer.parseInt(endDayField.getText());
            render3DVisualization(startDay, endDay);
            System.out.println("Timeframe updated: " + startDay + " to " + endDay);
        } catch (NumberFormatException e) {
            System.out.println("Invalid timeframe values");
        }
    }

    @FXML
    public void initialize() {
        System.out.println("MainViewController initialized");
        simulationModel = new SimulationModel();
        temporalVectorManager = new TemporalVectorManager();
        vector3DRenderer = new Vector3DRenderer(vectorCanvas);
        eventsListView.setItems(eventMessages);
        stopButton.setDisable(true);

        // Bind the speed slider to the simulation model
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            simulationModel.setSimulationSpeed(newVal.doubleValue());
        });
        
        // Add 3D interaction controls
        setup3DControls();
    }
    
    private void setup3DControls() {
        // Mouse drag for rotation
        vectorCanvas.setOnMouseDragged(event -> {
            double deltaX = event.getX() - vectorCanvas.getWidth() / 2;
            double deltaY = event.getY() - vectorCanvas.getHeight() / 2;
            
            double rotationX = deltaY / 100.0;
            double rotationY = deltaX / 100.0;
            
            vector3DRenderer.setRotation(rotationX, rotationY);
            
            // Re-render with new rotation using current timeframe
            reRenderWithCurrentTimeframe();
        });
        
        // Mouse wheel for zoom
        vectorCanvas.setOnScroll(event -> {
            double zoomDelta = event.getDeltaY() > 0 ? 1.1 : 0.9;
            vector3DRenderer.setZoom(vector3DRenderer.getCurrentZoom() * zoomDelta);
            
            // Re-render with new zoom using current timeframe
            reRenderWithCurrentTimeframe();
        });
    }
    
    private void reRenderWithCurrentTimeframe() {
        if (latestUpdate != null) {
            int startDay = 0;
            int endDay = latestUpdate.getCurrentDay();
            try {
                startDay = Integer.parseInt(startDayField.getText());
                endDay = Integer.parseInt(endDayField.getText());
            } catch (NumberFormatException e) {
                // Use defaults if parsing fails
            }
            render3DVisualization(startDay, endDay);
        }
    }
    
    private void updateUI(SimulationUpdate update) {
        // Store the latest update for use in other methods
        this.latestUpdate = update;
        
        // 1. Update Recent Events List
        if (eventMessages.size() > 200) { // Keep the list from getting too long
            eventMessages.remove(0);
        }
        eventMessages.add(update.getLatestEvent());
        eventsListView.scrollTo(eventMessages.size() - 1);
        
        // 2. Update PESTEL State Box
        pestelStateBox.getChildren().clear();
        Map<String, Map<String, String>> allFactors = update.getPestelState().getAllFactors();
        
        for (Map.Entry<String, Map<String, String>> categoryEntry : allFactors.entrySet()) {
            VBox categoryContent = new VBox(5.0);
            for (Map.Entry<String, String> factorEntry : categoryEntry.getValue().entrySet()) {
                Label factorLabel = new Label(factorEntry.getKey() + ": " + factorEntry.getValue());
                factorLabel.setWrapText(true);
                factorLabel.getStyleClass().add("pestel-factor-label");
                // Explicitly set text color to ensure visibility
                factorLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 14px;");
                categoryContent.getChildren().add(factorLabel);
            }
            TitledPane titledPane = new TitledPane(categoryEntry.getKey().toUpperCase(), categoryContent);
            titledPane.setAnimated(false); // Optional: for performance
            // Explicitly style the titled pane header for maximum visibility
            titledPane.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 16px; -fx-font-weight: bold;");
            pestelStateBox.getChildren().add(titledPane);
        }
        
        // 3. Update 3D Vector Visualization
        update3DVectorVisualization(update);
    }
    
    private void update3DVectorVisualization(SimulationUpdate update) {
        // Initialize scenarios if not done yet
        if (temporalVectorManager.getMaxDays() == 0) {
            temporalVectorManager.initializeScenarios(update.getFutureScenarios());
        }
        
        // Add current day to temporal vectors
        temporalVectorManager.addDay(update.getCurrentDay(), update.getFutureScenarios(), update.getPestelState());
        
        // Render the 3D visualization with current timeframe
        int startDay = 0;
        int endDay = update.getCurrentDay();
        try {
            startDay = Integer.parseInt(startDayField.getText());
            endDay = Integer.parseInt(endDayField.getText());
        } catch (NumberFormatException e) {
            // Use defaults if parsing fails
        }
        render3DVisualization(startDay, endDay);
    }
    
    private void render3DVisualization(int startDay, int endDay) {
        // Get the 3D paths for the specified timeframe
        Map<String, List<double[]>> paths = temporalVectorManager.get3DPaths(startDay, endDay);
        
        // Get current probabilities from the latest update
        Map<String, Double> probabilities = new java.util.HashMap<>();
        if (latestUpdate != null) {
            for (EnhancedFutureScenarioManager.FutureScenario scenario : latestUpdate.getFutureScenarios()) {
                probabilities.put(scenario.getName(), scenario.getProbability());
            }
        } else {
            // Default probabilities if no update yet
            for (String scenario : paths.keySet()) {
                probabilities.put(scenario, 0.1);
            }
        }
        
        // Render with the 3D renderer
        vector3DRenderer.render(paths, probabilities, startDay, endDay);
    }
}
