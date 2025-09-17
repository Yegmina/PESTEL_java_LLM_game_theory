package test;

import simu.framework.Trace;
import simu.framework.Trace.Level;
import simu.model.*;

import java.util.*;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

/**
 * Main simulator for the AI Agent-Based Forecasting System for Metropolia University.
 * This simulator provides a command-line interface for running the forecasting simulation
 * with configurable parameters and real-time AI analysis using the Gemini API.
 */
public class ForecastingSimulator {
    private ForecastingEngine engine;
    private GeminiAIService aiService;
    private Scanner scanner;
    private boolean aiEnabled;
    
    /**
     * Initialize the forecasting simulator
     */
    public ForecastingSimulator() {
        this.scanner = new Scanner(System.in);
        this.aiEnabled = false;
        initializeAIService();
    }
    
    /**
     * Initialize the AI service with API key
     */
    private void initializeAIService() {
        String apiKey = System.getProperty("GEMINI_API_KEY");
        if (apiKey != null && !apiKey.isEmpty()) {
            this.aiService = new GeminiAIService(apiKey);
            this.aiEnabled = true;
            System.out.println("✓ Gemini AI service initialized successfully");
        } else {
            System.out.println("⚠ Gemini AI service not available (API key not provided)");
            System.out.println("  Set GEMINI_API_KEY system property to enable AI features");
        }
    }
    
    /**
     * Main entry point for the simulator
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Set trace level for detailed output
        Trace.setTraceLevel(Level.INFO);
        
        ForecastingSimulator simulator = new ForecastingSimulator();
        simulator.run();
    }
    
    /**
     * Run the main simulation loop
     */
    public void run() {
        System.out.println("=== AI Agent-Based Forecasting System ===");
        System.out.println("Metropolia University of Applied Sciences");
        System.out.println("Strategic Planning Simulation");
        System.out.println();
        
        // Display menu and get user configuration
        SimulationConfig config = displayMenuAndGetConfig();
        
        // Initialize and run simulation
        runSimulation(config);
        
        // Display results and AI analysis
        displayResults();
        
        scanner.close();
    }
    
    /**
     * Display menu and get simulation configuration from user
     * @return Simulation configuration
     */
    private SimulationConfig displayMenuAndGetConfig() {
        System.out.println("=== Simulation Configuration ===");
        
        // Get agent counts
        int companies = getIntInput("Number of Company agents (default: 5): ", 5);
        int countries = getIntInput("Number of Country agents (default: 3): ", 3);
        int researchers = getIntInput("Number of Researcher agents (default: 4): ", 4);
        
        // Get simulation duration
        double simulationTime = getDoubleInput("Simulation duration in months (default: 24): ", 24.0);
        
        // Get AI analysis frequency
        boolean enableAI = false;
        if (aiEnabled) {
            enableAI = getBooleanInput("Enable AI analysis during simulation? (y/n, default: y): ", true);
        }
        
        // Get output verbosity
        String verbosity = getStringInput("Output verbosity (minimal/normal/detailed, default: normal): ", "normal");
        setTraceLevel(verbosity);
        
        return new SimulationConfig(companies, countries, researchers, simulationTime, enableAI);
    }
    
    /**
     * Run the simulation with the given configuration
     * @param config Simulation configuration
     */
    private void runSimulation(SimulationConfig config) {
        System.out.println("\n=== Starting Simulation ===");
        System.out.println("Configuration:");
        System.out.println("  Companies: " + config.companies);
        System.out.println("  Countries: " + config.countries);
        System.out.println("  Researchers: " + config.researchers);
        System.out.println("  Duration: " + config.simulationTime + " months");
        System.out.println("  AI Analysis: " + (config.enableAI ? "Enabled" : "Disabled"));
        System.out.println();
        
        // Create and configure engine
        engine = new ForecastingEngine(config.companies, config.countries, config.researchers);
        engine.setSimulationTime(config.simulationTime);
        
        // Run simulation
        long startTime = System.currentTimeMillis();
        engine.run();
        long endTime = System.currentTimeMillis();
        
        System.out.println("\n=== Simulation Completed ===");
        System.out.println("Execution time: " + (endTime - startTime) + " ms");
    }
    
    /**
     * Display simulation results and AI analysis
     */
    private void displayResults() {
        System.out.println("\n=== SIMULATION RESULTS ===");
        
        // Display basic results
        displayBasicResults();
        
        // Display AI analysis if enabled
        if (aiEnabled) {
            displayAIAnalysis();
        }
        
        // Display strategic insights
        displayStrategicInsights();
        
        // Ask if user wants to run another simulation
        if (getBooleanInput("\nRun another simulation? (y/n): ", false)) {
            run();
        }
    }
    
    /**
     * Display basic simulation results
     */
    private void displayBasicResults() {
        System.out.println("\n--- Basic Results ---");
        
        // Global state summary
        GlobalState globalState = engine.getGlobalState();
        System.out.println("Final Global State:");
        System.out.println(globalState.toString());
        
        // Agent statistics
        System.out.println("\nAgent Statistics:");
        int totalDecisions = 0;
        for (Agent agent : engine.getAgents()) {
            totalDecisions += agent.getDecisionCount();
            System.out.println("  " + agent.getAgentId() + ": " + agent.getDecisionCount() + " decisions");
        }
        System.out.println("  Total decisions: " + totalDecisions);
        
        // Scenario results
        ScenarioGenerator scenarioGen = engine.getScenarioGenerator();
        System.out.println("\nScenario Results:");
        System.out.println(scenarioGen.getSummary());
    }
    
    /**
     * Display AI analysis results
     */
    private void displayAIAnalysis() {
        System.out.println("\n--- AI Analysis Results ---");
        
        try {
            // Perform AI analysis
            GlobalState globalState = engine.getGlobalState();
            CompletableFuture<AIAnalysisResult> analysisFuture = aiService.analyzeGlobalState(globalState);
            
            System.out.println("Performing AI analysis...");
            AIAnalysisResult analysisResult = analysisFuture.get();
            
            System.out.println("\nAI Analysis:");
            System.out.println(analysisResult.toString());
            System.out.println("\nDetailed Analysis:");
            System.out.println("Summary: " + analysisResult.getSummary());
            System.out.println("Key Risks: " + analysisResult.getKeyRisks());
            System.out.println("Key Opportunities: " + analysisResult.getKeyOpportunities());
            System.out.println("Trend Analysis: " + analysisResult.getTrendAnalysis());
            
            // Scenario predictions
            ScenarioGenerator scenarioGen = engine.getScenarioGenerator();
            CompletableFuture<AIPredictionResult> predictionFuture = 
                aiService.predictScenarios(scenarioGen.getAllScenarios(), globalState);
            
            System.out.println("\nPerforming scenario predictions...");
            AIPredictionResult predictionResult = predictionFuture.get();
            
            System.out.println("\nAI Predictions:");
            System.out.println(predictionResult.toString());
            System.out.println("\nDetailed Predictions:");
            System.out.println("Summary: " + predictionResult.getSummary());
            System.out.println("Most Likely Scenarios: " + predictionResult.getMostLikelyScenarios());
            System.out.println("Timeline: " + predictionResult.getTimelinePredictions());
            
            // Strategic recommendations
            CompletableFuture<AIRecommendationResult> recommendationFuture = 
                aiService.generateRecommendations(analysisResult, predictionResult);
            
            System.out.println("\nGenerating strategic recommendations...");
            AIRecommendationResult recommendationResult = recommendationFuture.get();
            
            System.out.println("\nAI Recommendations:");
            System.out.println(recommendationResult.toString());
            System.out.println("\nDetailed Recommendations:");
            System.out.println("Immediate Actions: " + recommendationResult.getImmediateActions());
            System.out.println("Medium-term Strategies: " + recommendationResult.getMediumTermStrategies());
            System.out.println("Long-term Vision: " + recommendationResult.getLongTermVision());
            System.out.println("Risk Mitigation: " + recommendationResult.getRiskMitigation());
            System.out.println("Opportunities: " + recommendationResult.getOpportunityCapitalization());
            
        } catch (Exception e) {
            System.out.println("AI analysis failed: " + e.getMessage());
            System.out.println("Continuing with basic results...");
        }
    }
    
    /**
     * Display strategic insights
     */
    private void displayStrategicInsights() {
        System.out.println("\n--- Strategic Insights ---");
        
        GlobalState globalState = engine.getGlobalState();
        ScenarioGenerator scenarioGen = engine.getScenarioGenerator();
        
        // Calculate key metrics
        double stability = globalState.calculateStabilityIndex();
        double economicGrowth = globalState.getIndicator("economic_growth_rate");
        double technologyLevel = globalState.getIndicator("technology_advancement");
        
        System.out.println("Key Strategic Metrics:");
        System.out.println("  System Stability: " + String.format("%.2f", stability) + 
                         (stability > 0.7 ? " (High)" : stability < 0.3 ? " (Low)" : " (Medium)"));
        System.out.println("  Economic Growth: " + String.format("%.2f%%", economicGrowth * 100) + 
                         (economicGrowth > 0.05 ? " (Strong)" : economicGrowth < -0.02 ? " (Weak)" : " (Moderate)"));
        System.out.println("  Technology Level: " + String.format("%.2f", technologyLevel) + 
                         (technologyLevel > 0.7 ? " (Advanced)" : technologyLevel < 0.3 ? " (Basic)" : " (Intermediate)"));
        
        // Scenario insights
        Map<String, ScenarioGenerator.ScenarioCluster> clusters = scenarioGen.getAllClusters();
        System.out.println("\nScenario Insights:");
        for (ScenarioGenerator.ScenarioCluster cluster : clusters.values()) {
            System.out.println("  " + cluster.getClusterName() + ": " + 
                             cluster.getScenarioIds().size() + " scenarios, " +
                             String.format("%.1f%%", cluster.getAverageProbability() * 100) + " average probability");
        }
        
        // Strategic recommendations
        System.out.println("\nStrategic Recommendations:");
        if (stability > 0.7) {
            System.out.println("  • System shows high stability - focus on growth and innovation");
        } else if (stability < 0.3) {
            System.out.println("  • System shows low stability - prioritize risk mitigation and adaptation");
        } else {
            System.out.println("  • System shows moderate stability - maintain flexible strategies");
        }
        
        if (economicGrowth > 0.05) {
            System.out.println("  • Strong economic growth - invest in expansion and development");
        } else if (economicGrowth < -0.02) {
            System.out.println("  • Economic challenges - focus on efficiency and cost management");
        }
        
        if (technologyLevel > 0.7) {
            System.out.println("  • High technology advancement - leverage for competitive advantage");
        } else if (technologyLevel < 0.3) {
            System.out.println("  • Technology gap identified - invest in digital transformation");
        }
    }
    
    /**
     * Get integer input from user
     * @param prompt Input prompt
     * @param defaultValue Default value
     * @return User input or default value
     */
    private int getIntInput(String prompt, int defaultValue) {
        System.out.print(prompt);
        try {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return defaultValue;
            }
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input, using default value: " + defaultValue);
            return defaultValue;
        }
    }
    
    /**
     * Get double input from user
     * @param prompt Input prompt
     * @param defaultValue Default value
     * @return User input or default value
     */
    private double getDoubleInput(String prompt, double defaultValue) {
        System.out.print(prompt);
        try {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return defaultValue;
            }
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input, using default value: " + defaultValue);
            return defaultValue;
        }
    }
    
    /**
     * Get boolean input from user
     * @param prompt Input prompt
     * @param defaultValue Default value
     * @return User input or default value
     */
    private boolean getBooleanInput(String prompt, boolean defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim().toLowerCase();
        if (input.isEmpty()) {
            return defaultValue;
        }
        return input.startsWith("y") || input.startsWith("t") || input.equals("1");
    }
    
    /**
     * Get string input from user
     * @param prompt Input prompt
     * @param defaultValue Default value
     * @return User input or default value
     */
    private String getStringInput(String prompt, String defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? defaultValue : input;
    }
    
    /**
     * Set trace level based on verbosity setting
     * @param verbosity Verbosity level
     */
    private void setTraceLevel(String verbosity) {
        switch (verbosity.toLowerCase()) {
            case "minimal":
                Trace.setTraceLevel(Level.ERR);
                break;
            case "detailed":
                Trace.setTraceLevel(Level.INFO);
                break;
            default:
                Trace.setTraceLevel(Level.INFO);
                break;
        }
    }
    
    /**
     * Configuration class for simulation parameters
     */
    private static class SimulationConfig {
        final int companies;
        final int countries;
        final int researchers;
        final double simulationTime;
        final boolean enableAI;
        
        SimulationConfig(int companies, int countries, int researchers, 
                        double simulationTime, boolean enableAI) {
            this.companies = companies;
            this.countries = countries;
            this.researchers = researchers;
            this.simulationTime = simulationTime;
            this.enableAI = enableAI;
        }
    }
}
