package test;

import simu.framework.Trace;
import simu.framework.Trace.Level;
import simu.model.*;

import java.util.*;

/**
 * Simple demonstration of the AI Agent-Based Forecasting System.
 * This demo runs a basic simulation without user interaction to showcase
 * the system's capabilities.
 */
public class SimpleForecastingDemo {
    
    public static void main(String[] args) {
        // Set trace level for output
        Trace.setTraceLevel(Level.INFO);
        
        System.out.println("=== AI Agent-Based Forecasting System Demo ===");
        System.out.println("Metropolia University of Applied Sciences");
        System.out.println("Strategic Planning Simulation");
        System.out.println();
        
        // Create and run simulation
        runDemoSimulation();
        
        System.out.println("\n=== Demo Completed ===");
        System.out.println("This demonstrates the core functionality of the forecasting system.");
        System.out.println("For full interactive features, run ForecastingSimulator.java");
    }
    
    /**
     * Run a demonstration simulation
     */
    private static void runDemoSimulation() {
        System.out.println("Initializing simulation with default parameters...");
        
        // Create forecasting engine with default parameters
        ForecastingEngine engine = new ForecastingEngine(3, 2, 3); // 3 companies, 2 countries, 3 researchers
        engine.setSimulationTime(12.0); // 12 months simulation
        
        System.out.println("Starting simulation...");
        System.out.println("Configuration:");
        System.out.println("  - 3 Company agents");
        System.out.println("  - 2 Country agents");
        System.out.println("  - 3 Researcher agents");
        System.out.println("  - 12 months duration");
        System.out.println();
        
        // Run the simulation
        long startTime = System.currentTimeMillis();
        engine.run();
        long endTime = System.currentTimeMillis();
        
        System.out.println("\nSimulation completed in " + (endTime - startTime) + " ms");
        
        // Display key results
        displayDemoResults(engine);
    }
    
    /**
     * Display demonstration results
     * @param engine The forecasting engine
     */
    private static void displayDemoResults(ForecastingEngine engine) {
        System.out.println("\n=== DEMO RESULTS ===");
        
        // Global state summary
        GlobalState globalState = engine.getGlobalState();
        System.out.println("Final Global State:");
        System.out.println("  Temperature: " + String.format("%.2f°C", globalState.getIndicator("global_temperature")));
        System.out.println("  Peace Index: " + String.format("%.2f", globalState.getIndicator("world_peace_index")));
        System.out.println("  Economic Growth: " + String.format("%.2f%%", globalState.getIndicator("economic_growth_rate") * 100));
        System.out.println("  Resource Availability: " + String.format("%.2f", globalState.getIndicator("resource_availability")));
        System.out.println("  Technology Level: " + String.format("%.2f", globalState.getIndicator("technology_advancement")));
        System.out.println("  Stability Index: " + String.format("%.2f", globalState.calculateStabilityIndex()));
        
        // Agent statistics
        System.out.println("\nAgent Activity:");
        int totalDecisions = 0;
        for (Agent agent : engine.getAgents()) {
            System.out.println("  " + agent.getAgentId() + ": " + agent.getDecisionCount() + " decisions, " +
                             "influence: " + String.format("%.2f", agent.calculateInfluenceStrength()));
            totalDecisions += agent.getDecisionCount();
        }
        System.out.println("  Total decisions made: " + totalDecisions);
        
        // Scenario results
        ScenarioGenerator scenarioGen = engine.getScenarioGenerator();
        System.out.println("\nScenario Generation:");
        System.out.println("  Total scenarios: " + scenarioGen.getAllScenarios().size());
        
        Map<String, ScenarioGenerator.ScenarioCluster> clusters = scenarioGen.getAllClusters();
        System.out.println("  Scenario clusters: " + clusters.size());
        for (ScenarioGenerator.ScenarioCluster cluster : clusters.values()) {
            System.out.println("    " + cluster.getClusterName() + ": " + 
                             cluster.getScenarioIds().size() + " scenarios");
        }
        
        // Strategic insights
        System.out.println("\nStrategic Insights:");
        double stability = globalState.calculateStabilityIndex();
        if (stability > 0.7) {
            System.out.println("  ✓ High system stability - favorable conditions for growth");
        } else if (stability < 0.3) {
            System.out.println("  ⚠ Low system stability - focus on risk mitigation");
        } else {
            System.out.println("  → Moderate system stability - maintain flexible strategies");
        }
        
        double economicGrowth = globalState.getIndicator("economic_growth_rate");
        if (economicGrowth > 0.05) {
            System.out.println("  ✓ Strong economic growth - invest in expansion");
        } else if (economicGrowth < -0.02) {
            System.out.println("  ⚠ Economic challenges - focus on efficiency");
        } else {
            System.out.println("  → Moderate economic growth - balanced approach recommended");
        }
        
        double technologyLevel = globalState.getIndicator("technology_advancement");
        if (technologyLevel > 0.7) {
            System.out.println("  ✓ High technology advancement - leverage for competitive advantage");
        } else if (technologyLevel < 0.3) {
            System.out.println("  ⚠ Technology gap - invest in digital transformation");
        } else {
            System.out.println("  → Moderate technology level - continue innovation efforts");
        }
        
        System.out.println("\n=== Key Features Demonstrated ===");
        System.out.println("✓ ABC Phase Simulation Framework (Time, Scheduled Events, Conditional Events)");
        System.out.println("✓ Multi-Agent System with Companies, Countries, and Researchers");
        System.out.println("✓ Global State Management with External Influences");
        System.out.println("✓ Scenario Generation and Clustering");
        System.out.println("✓ Agent Decision Making and Collaboration");
        System.out.println("✓ Strategic Insights and Recommendations");
        System.out.println("✓ Probability-based Future Scenario Modeling");
    }
}
