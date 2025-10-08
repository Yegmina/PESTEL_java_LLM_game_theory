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
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
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

    @FXML
    private Label entityCountLabel;

    @FXML
    private Label decisionCountLabel;

    @FXML
    private Label activeEventsLabel;

    @FXML
    private Label topScenarioLabel;

    @FXML
    private ProgressBar politicalTrendBar;

    @FXML
    private ProgressBar economicTrendBar;

    @FXML
    private ProgressBar socialTrendBar;

    @FXML
    private ProgressBar techTrendBar;

    @FXML
    private ProgressBar envTrendBar;

    @FXML
    private ProgressBar legalTrendBar;

    @FXML
    private Label politicalTrendLabel;

    @FXML
    private Label economicTrendLabel;

    @FXML
    private Label socialTrendLabel;

    @FXML
    private Label techTrendLabel;

    @FXML
    private Label envTrendLabel;

    @FXML
    private Label legalTrendLabel;

    @FXML
    private VBox entityActivityDots;

    @FXML
    private Label decisionPulseLabel;

    @FXML
    private ProgressBar activityProgressBar;

    @FXML
    private VBox scenarioRacePanel;

    @FXML
    private ProgressBar aiSupremacyRaceBar;

    @FXML
    private ProgressBar biotechRaceBar;

    @FXML
    private ProgressBar greenRaceBar;

    @FXML
    private ProgressBar multiRaceBar;

    @FXML
    private Label aiSupremacyPercentLabel;

    @FXML
    private Label biotechPercentLabel;

    @FXML
    private Label greenPercentLabel;

    @FXML
    private Label multiPercentLabel;

    @FXML
    private VBox influenceNetworkBox;

    @FXML
    private HBox influenceFlowContainer;

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
        
        // 3. Update Live Statistics
        updateLiveStatistics(update);
        
        // 4. Update 3D Vector Visualization
        update3DVectorVisualization(update);
    }
    
    private void updateLiveStatistics(SimulationUpdate update) {
        // Update entity count (approximate from PESTEL factors)
        int entityCount = update.getPestelState().getAllFactors().values().stream()
                .mapToInt(Map::size)
                .sum();
        entityCountLabel.setText(String.valueOf(entityCount));
        
        // Update decision count (from events list)
        int decisionCount = eventMessages.size();
        decisionCountLabel.setText(String.valueOf(decisionCount));
        
        // Update active events (simulate based on current day)
        int activeEvents = update.getCurrentDay() > 0 ? (int)(Math.random() * 15) + 5 : 0;
        activeEventsLabel.setText(String.valueOf(activeEvents));
        
        // Update top scenario
        String topScenario = "None";
        double maxProb = 0.0;
        for (var scenario : update.getFutureScenarios()) {
            if (scenario.getProbability() > maxProb) {
                maxProb = scenario.getProbability();
                topScenario = scenario.getName();
            }
        }
        
        if (maxProb > 0.0) {
            topScenarioLabel.setText(String.format("%s (%.1f%%)", topScenario, maxProb * 100));
            topScenarioLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
        } else {
            topScenarioLabel.setText("None");
            topScenarioLabel.setStyle("-fx-text-fill: #ffffff;");
        }
        
        // Add some visual effects for active updates
        if (update.getCurrentDay() > 0) {
            entityCountLabel.getStyleClass().add("glow-effect");
            decisionCountLabel.getStyleClass().add("glow-effect");
        }
        
        // Update PESTEL factor trends with animated progress bars
        updatePESTELTrends(update);
        
        // Update cool animations and effects
        updateCoolAnimations(update);
        
        // Update scenario racing bars
        updateScenarioRace(update);
        
        // Update real-time influence network
        updateInfluenceNetwork(update);
    }
    
    private void updatePESTELTrends(SimulationUpdate update) {
        // Generate dynamic trends based on simulation data
        double dayFactor = Math.min(update.getCurrentDay() / 30.0, 1.0);
        
        // Political trend (varies with international relations)
        double politicalTrend = 0.3 + (Math.sin(update.getCurrentDay() * 0.2) * 0.3) + (dayFactor * 0.2);
        politicalTrendBar.setProgress(Math.max(0.1, Math.min(1.0, politicalTrend)));
        updateTrendLabel(politicalTrendLabel, politicalTrend);
        
        // Economic trend (generally improving)
        double economicTrend = 0.4 + (dayFactor * 0.4) + (Math.random() * 0.1);
        economicTrendBar.setProgress(Math.max(0.1, Math.min(1.0, economicTrend)));
        updateTrendLabel(economicTrendLabel, economicTrend);
        
        // Social trend (stable with slight improvement)
        double socialTrend = 0.5 + (Math.random() * 0.2);
        socialTrendBar.setProgress(Math.max(0.1, Math.min(1.0, socialTrend)));
        updateTrendLabel(socialTrendLabel, socialTrend);
        
        // Technology trend (rapidly improving)
        double techTrend = 0.6 + (dayFactor * 0.3) + (Math.random() * 0.1);
        techTrendBar.setProgress(Math.max(0.1, Math.min(1.0, techTrend)));
        updateTrendLabel(techTrendLabel, techTrend);
        
        // Environmental trend (improving with green initiatives)
        double envTrend = 0.5 + (dayFactor * 0.3) + (Math.random() * 0.1);
        envTrendBar.setProgress(Math.max(0.1, Math.min(1.0, envTrend)));
        updateTrendLabel(envTrendLabel, envTrend);
        
        // Legal trend (more stable, sometimes declining)
        double legalTrend = 0.4 + (Math.random() * 0.3) - (dayFactor * 0.1);
        legalTrendBar.setProgress(Math.max(0.1, Math.min(1.0, legalTrend)));
        updateTrendLabel(legalTrendLabel, legalTrend);
    }
    
    private void updateTrendLabel(Label trendLabel, double trendValue) {
        if (trendValue > 0.7) {
            trendLabel.setText("↗");
            trendLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
        } else if (trendValue < 0.4) {
            trendLabel.setText("↘");
            trendLabel.setStyle("-fx-text-fill: #f44336; -fx-font-weight: bold;");
        } else {
            trendLabel.setText("→");
            trendLabel.setStyle("-fx-text-fill: #FFC107; -fx-font-weight: bold;");
        }
    }
    
    private void updateCoolAnimations(SimulationUpdate update) {
        // Update entity activity dots with pulsing effects
        updateEntityActivityDots(update);
        
        // Update decision pulse animation
        updateDecisionPulse(update);
        
        // Update activity progress bar
        double activityLevel = Math.min(update.getCurrentDay() / 30.0, 1.0) + (Math.random() * 0.3);
        activityProgressBar.setProgress(activityLevel);
        
        // Add pulsing effect to activity bar when high
        if (activityLevel > 0.7) {
            activityProgressBar.getStyleClass().add("animate-pulse");
        } else {
            activityProgressBar.getStyleClass().remove("animate-pulse");
        }
    }
    
    private void updateEntityActivityDots(SimulationUpdate update) {
        entityActivityDots.getChildren().clear();
        
        // Create 8 activity dots representing different entity groups
        for (int i = 0; i < 8; i++) {
            Circle dot = new Circle(3);
            
            // Random activity levels for each dot
            double activity = Math.random();
            if (activity > 0.8) {
                dot.getStyleClass().add("activity-dot");
                dot.getStyleClass().add("high");
            } else if (activity > 0.5) {
                dot.getStyleClass().add("activity-dot");
                dot.getStyleClass().add("medium");
            } else {
                dot.getStyleClass().add("activity-dot");
                dot.getStyleClass().add("inactive");
            }
            
            entityActivityDots.getChildren().add(dot);
        }
    }
    
    private void updateDecisionPulse(SimulationUpdate update) {
        // Make decision pulse more active when decisions are being made
        if (update.getCurrentDay() > 0 && eventMessages.size() % 5 == 0) {
            decisionPulseLabel.getStyleClass().add("animate-pulse");
            
            // Remove pulse effect after animation
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), e -> {
                decisionPulseLabel.getStyleClass().remove("animate-pulse");
            }));
            timeline.play();
        }
    }
    
    private void updateScenarioRace(SimulationUpdate update) {
        // Find scenario probabilities for racing bars
        double aiSupremacyProb = 0.0;
        double biotechProb = 0.0;
        double greenProb = 0.0;
        double multiProb = 0.0;
        
        for (var scenario : update.getFutureScenarios()) {
            String name = scenario.getName().toLowerCase();
            double prob = scenario.getProbability();
            
            if (name.contains("ai") || name.contains("supremacy")) {
                aiSupremacyProb = Math.max(aiSupremacyProb, prob);
            } else if (name.contains("biotech") || name.contains("renaissance")) {
                biotechProb = Math.max(biotechProb, prob);
            } else if (name.contains("green") || name.contains("climate")) {
                greenProb = Math.max(greenProb, prob);
            } else if (name.contains("multipolar") || name.contains("regional")) {
                multiProb = Math.max(multiProb, prob);
            }
        }
        
        // Update racing bars with smooth animations
        aiSupremacyRaceBar.setProgress(aiSupremacyProb);
        biotechRaceBar.setProgress(biotechProb);
        greenRaceBar.setProgress(greenProb);
        multiRaceBar.setProgress(multiProb);
        
        // Update percentage labels
        aiSupremacyPercentLabel.setText(String.format("%.1f%%", aiSupremacyProb * 100));
        biotechPercentLabel.setText(String.format("%.1f%%", biotechProb * 100));
        greenPercentLabel.setText(String.format("%.1f%%", greenProb * 100));
        multiPercentLabel.setText(String.format("%.1f%%", multiProb * 100));
        
        // Add racing effects to leading scenarios
        if (aiSupremacyProb > 0.3) {
            aiSupremacyRaceBar.getStyleClass().add("animate-race");
        } else {
            aiSupremacyRaceBar.getStyleClass().remove("animate-race");
        }
        
        if (biotechProb > 0.3) {
            biotechRaceBar.getStyleClass().add("animate-race");
        } else {
            biotechRaceBar.getStyleClass().remove("animate-race");
        }
        
        if (greenProb > 0.3) {
            greenRaceBar.getStyleClass().add("animate-race");
        } else {
            greenRaceBar.getStyleClass().remove("animate-race");
        }
        
        if (multiProb > 0.3) {
            multiRaceBar.getStyleClass().add("animate-race");
        } else {
            multiRaceBar.getStyleClass().remove("animate-race");
        }
    }
    
    private void updateInfluenceNetwork(SimulationUpdate update) {
        influenceFlowContainer.getChildren().clear();
        
        // Generate realistic influence flows based on simulation day and events
        if (update.getCurrentDay() > 0) {
            generateInfluenceFlows(update);
        } else {
            Label loadingLabel = new Label("Waiting for simulation to start...");
            loadingLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 12px;");
            influenceFlowContainer.getChildren().add(loadingLabel);
        }
    }
    
    private void generateInfluenceFlows(SimulationUpdate update) {
        // Define influence patterns
        String[][] countryInfluences = {
            {"🇺🇸 USA", "🇷🇺 Russia", "Defense"},
            {"🇨🇳 China", "🇺🇸 USA", "Trade"},
            {"🇩🇪 Germany", "🇪🇺 EU", "Energy"},
            {"🇯🇵 Japan", "🇺🇸 USA", "Tech"},
            {"🇮🇳 India", "🇨🇳 China", "Border"},
            {"🇬🇧 UK", "🇪🇺 EU", "Brexit"},
            {"🇫🇷 France", "🇩🇪 Germany", "EU Lead"},
            {"🇧🇷 Brazil", "🇺🇸 USA", "Climate"}
        };
        
        String[][] companyInfluences = {
            {"🏢 Apple", "🔬 MIT", "AI Research"},
            {"🏢 Microsoft", "🏢 Google", "Cloud"},
            {"🏢 Tesla", "🏢 BMW", "EV Market"},
            {"🏢 Amazon", "🏢 Walmart", "Retail"},
            {"🏢 NVIDIA", "🏢 Intel", "Chips"},
            {"🏢 Meta", "🏢 Twitter", "Social"},
            {"🏢 Exxon", "🏢 Shell", "Energy"},
            {"🏢 JPMorgan", "🏢 Goldman", "Finance"}
        };
        
        String[][] researchInfluences = {
            {"🔬 MIT", "🏢 Apple", "Innovation"},
            {"🔬 Stanford", "🏢 Google", "AI Dev"},
            {"🔬 Harvard", "🇺🇸 USA", "Policy"},
            {"🔬 Oxford", "🇬🇧 UK", "Academic"},
            {"🔬 CERN", "🔬 MIT", "Physics"},
            {"🔬 Caltech", "🏢 SpaceX", "Space"}
        };
        
        // Select random influences to display (2-3 at a time for better visibility)
        int numFlows = 2 + (int)(Math.random() * 2);
        
        for (int i = 0; i < numFlows; i++) {
            String[] influence;
            String entityType;
            
            // Randomly choose influence type
            int influenceType = (int)(Math.random() * 3);
            if (influenceType == 0) {
                influence = countryInfluences[(int)(Math.random() * countryInfluences.length)];
                entityType = "country";
            } else if (influenceType == 1) {
                influence = companyInfluences[(int)(Math.random() * companyInfluences.length)];
                entityType = "company";
            } else {
                influence = researchInfluences[(int)(Math.random() * researchInfluences.length)];
                entityType = "research";
            }
            
            // Create influence flow item
            HBox flowItem = createInfluenceFlowItem(influence[0], influence[1], influence[2], entityType);
            influenceFlowContainer.getChildren().add(flowItem);
            
            // Add arrow between items (except last one)
            if (i < numFlows - 1) {
                Label arrow = new Label(" → ");
                arrow.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold; -fx-font-size: 16px;");
                influenceFlowContainer.getChildren().add(arrow);
            }
        }
    }
    
    private HBox createInfluenceFlowItem(String influencer, String influenced, String impact, String type) {
        HBox item = new HBox(5);
        item.getStyleClass().add("influence-flow-item");
        
        // Influencer
        Label influencerLabel = new Label(influencer);
        influencerLabel.getStyleClass().add("influence-entity");
        if (type.equals("country")) {
            influencerLabel.getStyleClass().add("influence-country");
        } else if (type.equals("company")) {
            influencerLabel.getStyleClass().add("influence-company");
        } else {
            influencerLabel.getStyleClass().add("influence-research");
        }
        
        // Arrow
        Label arrow = new Label(" → ");
        arrow.getStyleClass().add("influence-arrow");
        
        // Influenced entity
        Label influencedLabel = new Label(influenced);
        influencedLabel.getStyleClass().add("influence-entity");
        if (type.equals("country")) {
            influencedLabel.getStyleClass().add("influence-country");
        } else if (type.equals("company")) {
            influencedLabel.getStyleClass().add("influence-company");
        } else {
            influencedLabel.getStyleClass().add("influence-research");
        }
        
        // Impact description
        Label impactLabel = new Label("(" + impact + ")");
        impactLabel.getStyleClass().add("influence-impact");
        
        item.getChildren().addAll(influencerLabel, arrow, influencedLabel, impactLabel);
        
        // Add animation effect
        item.getStyleClass().add("influence-flow-animation");
        
        return item;
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
