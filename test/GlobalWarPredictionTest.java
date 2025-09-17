package test;

import simu.framework.Trace;
import simu.framework.Trace.Level;
import simu.model.*;

import java.util.*;

/**
 * Global War Prediction Test - Specialized test for modeling global conflict scenarios
 * and their impact on the forecasting system. This test focuses on:
 * - High conflict probability scenarios
 * - Economic and social disruption modeling
 * - Resource scarcity and allocation
 * - Technology development under conflict conditions
 * - Post-conflict recovery scenarios
 */
public class GlobalWarPredictionTest {
    
    public static void main(String[] args) {
        // Set trace level for detailed output
        Trace.setTraceLevel(Level.INFO);
        
        System.out.println("=== GLOBAL WAR PREDICTION TEST ===");
        System.out.println("Metropolia University of Applied Sciences");
        System.out.println("Strategic Planning - Conflict Scenario Analysis");
        System.out.println();
        
        // Run different war scenario tests
        runHighConflictScenario();
        runResourceWarScenario();
        runTechnologyWarScenario();
        runPostConflictRecoveryScenario();
        
        System.out.println("\n=== GLOBAL WAR PREDICTION TEST COMPLETED ===");
        System.out.println("All conflict scenarios have been analyzed.");
    }
    
    /**
     * Test high conflict probability scenario
     */
    private static void runHighConflictScenario() {
        System.out.println("--- HIGH CONFLICT PROBABILITY SCENARIO ---");
        
        // Create engine with conflict-focused configuration
        ForecastingEngine engine = new ForecastingEngine(4, 6, 2); // More countries, fewer researchers
        engine.setSimulationTime(24.0); // 24 months simulation
        
        // Initialize with high conflict conditions
        GlobalState globalState = engine.getGlobalState();
        globalState.updateIndicator("world_peace_index", 0.2); // Very low peace index
        globalState.updateIndicator("economic_growth_rate", -0.05); // Economic recession
        globalState.updateIndicator("resource_availability", 0.3); // Resource scarcity
        globalState.updateStatus("political_status", "unstable");
        globalState.updateStatus("economic_status", "recession");
        
        System.out.println("Initial Conflict Conditions:");
        System.out.println("  Peace Index: " + String.format("%.2f", globalState.getIndicator("world_peace_index")));
        System.out.println("  Economic Growth: " + String.format("%.2f%%", globalState.getIndicator("economic_growth_rate") * 100));
        System.out.println("  Resource Availability: " + String.format("%.2f", globalState.getIndicator("resource_availability")));
        System.out.println("  Political Status: " + globalState.getStatus("political_status"));
        System.out.println();
        
        // Add high-conflict external influences
        List<ExternalInfluence> conflictInfluences = createConflictInfluences();
        for (ExternalInfluence influence : conflictInfluences) {
            // Apply multiple conflict events
            for (int i = 0; i < 5; i++) {
                influence.applyInfluence(globalState, i * 2.0);
            }
        }
        
        System.out.println("Running high conflict simulation...");
        long startTime = System.currentTimeMillis();
        engine.run();
        long endTime = System.currentTimeMillis();
        
        // Analyze results
        analyzeConflictResults(engine, "High Conflict Scenario", endTime - startTime);
    }
    
    /**
     * Test resource war scenario
     */
    private static void runResourceWarScenario() {
        System.out.println("\n--- RESOURCE WAR SCENARIO ---");
        
        ForecastingEngine engine = new ForecastingEngine(6, 4, 3);
        engine.setSimulationTime(18.0);
        
        GlobalState globalState = engine.getGlobalState();
        // Set resource scarcity conditions
        globalState.updateIndicator("resource_availability", 0.1); // Extreme scarcity
        globalState.updateIndicator("economic_growth_rate", -0.08); // Deep recession
        globalState.updateIndicator("world_peace_index", 0.3); // Low peace
        globalState.updateStatus("economic_status", "crisis");
        
        System.out.println("Resource War Conditions:");
        System.out.println("  Resource Availability: " + String.format("%.2f", globalState.getIndicator("resource_availability")));
        System.out.println("  Economic Growth: " + String.format("%.2f%%", globalState.getIndicator("economic_growth_rate") * 100));
        System.out.println("  Peace Index: " + String.format("%.2f", globalState.getIndicator("world_peace_index")));
        System.out.println();
        
        // Apply resource conflict influences
        List<ExternalInfluence> resourceConflicts = createResourceConflictInfluences();
        for (ExternalInfluence influence : resourceConflicts) {
            for (int i = 0; i < 3; i++) {
                influence.applyInfluence(globalState, i * 3.0);
            }
        }
        
        System.out.println("Running resource war simulation...");
        long startTime = System.currentTimeMillis();
        engine.run();
        long endTime = System.currentTimeMillis();
        
        analyzeConflictResults(engine, "Resource War Scenario", endTime - startTime);
    }
    
    /**
     * Test technology war scenario
     */
    private static void runTechnologyWarScenario() {
        System.out.println("\n--- TECHNOLOGY WAR SCENARIO ---");
        
        ForecastingEngine engine = new ForecastingEngine(5, 3, 6); // More researchers
        engine.setSimulationTime(30.0);
        
        GlobalState globalState = engine.getGlobalState();
        // Set technology conflict conditions
        globalState.updateIndicator("technology_advancement", 0.9); // High tech level
        globalState.updateIndicator("world_peace_index", 0.4); // Moderate conflict
        globalState.updateIndicator("economic_growth_rate", 0.02); // Slow growth
        globalState.updateStatus("biotech_status", "advanced");
        
        System.out.println("Technology War Conditions:");
        System.out.println("  Technology Level: " + String.format("%.2f", globalState.getIndicator("technology_advancement")));
        System.out.println("  Peace Index: " + String.format("%.2f", globalState.getIndicator("world_peace_index")));
        System.out.println("  Economic Growth: " + String.format("%.2f%%", globalState.getIndicator("economic_growth_rate") * 100));
        System.out.println();
        
        // Apply technology conflict influences
        List<ExternalInfluence> techConflicts = createTechnologyConflictInfluences();
        for (ExternalInfluence influence : techConflicts) {
            for (int i = 0; i < 4; i++) {
                influence.applyInfluence(globalState, i * 2.5);
            }
        }
        
        System.out.println("Running technology war simulation...");
        long startTime = System.currentTimeMillis();
        engine.run();
        long endTime = System.currentTimeMillis();
        
        analyzeConflictResults(engine, "Technology War Scenario", endTime - startTime);
    }
    
    /**
     * Test post-conflict recovery scenario
     */
    private static void runPostConflictRecoveryScenario() {
        System.out.println("\n--- POST-CONFLICT RECOVERY SCENARIO ---");
        
        ForecastingEngine engine = new ForecastingEngine(4, 5, 4);
        engine.setSimulationTime(36.0); // Longer recovery period
        
        GlobalState globalState = engine.getGlobalState();
        // Set post-conflict conditions
        globalState.updateIndicator("world_peace_index", 0.6); // Improving peace
        globalState.updateIndicator("economic_growth_rate", 0.01); // Slow recovery
        globalState.updateIndicator("resource_availability", 0.4); // Recovering resources
        globalState.updateIndicator("technology_advancement", 0.3); // Technology setback
        globalState.updateStatus("political_status", "recovering");
        globalState.updateStatus("economic_status", "recovery");
        
        System.out.println("Post-Conflict Recovery Conditions:");
        System.out.println("  Peace Index: " + String.format("%.2f", globalState.getIndicator("world_peace_index")));
        System.out.println("  Economic Growth: " + String.format("%.2f%%", globalState.getIndicator("economic_growth_rate") * 100));
        System.out.println("  Resource Availability: " + String.format("%.2f", globalState.getIndicator("resource_availability")));
        System.out.println("  Technology Level: " + String.format("%.2f", globalState.getIndicator("technology_advancement")));
        System.out.println();
        
        // Apply recovery influences
        List<ExternalInfluence> recoveryInfluences = createRecoveryInfluences();
        for (ExternalInfluence influence : recoveryInfluences) {
            for (int i = 0; i < 6; i++) {
                influence.applyInfluence(globalState, i * 2.0);
            }
        }
        
        System.out.println("Running post-conflict recovery simulation...");
        long startTime = System.currentTimeMillis();
        engine.run();
        long endTime = System.currentTimeMillis();
        
        analyzeConflictResults(engine, "Post-Conflict Recovery Scenario", endTime - startTime);
    }
    
    /**
     * Create conflict-focused external influences
     */
    private static List<ExternalInfluence> createConflictInfluences() {
        List<ExternalInfluence> influences = new ArrayList<>();
        
        // High probability conflict events
        influences.add(new ExternalInfluence("conflict", 0.3, -0.2, 6.0)); // Major conflict
        influences.add(new ExternalInfluence("economic_shock", 0.2, -0.3, 8.0)); // Economic crisis
        influences.add(new ExternalInfluence("resource_discovery", 0.1, 0.1, 4.0)); // Rare resource discovery
        influences.add(new ExternalInfluence("population_change", 0.15, -0.1, 12.0)); // Population displacement
        influences.add(new ExternalInfluence("natural_disaster", 0.1, -0.15, 3.0)); // War-related disasters
        
        return influences;
    }
    
    /**
     * Create resource conflict influences
     */
    private static List<ExternalInfluence> createResourceConflictInfluences() {
        List<ExternalInfluence> influences = new ArrayList<>();
        
        influences.add(new ExternalInfluence("economic_shock", 0.4, -0.4, 10.0)); // Resource crisis
        influences.add(new ExternalInfluence("conflict", 0.25, -0.15, 8.0)); // Resource wars
        influences.add(new ExternalInfluence("climate_change", 0.2, -0.1, 15.0)); // Climate stress
        influences.add(new ExternalInfluence("population_change", 0.3, -0.2, 18.0)); // Mass migration
        
        return influences;
    }
    
    /**
     * Create technology conflict influences
     */
    private static List<ExternalInfluence> createTechnologyConflictInfluences() {
        List<ExternalInfluence> influences = new ArrayList<>();
        
        influences.add(new ExternalInfluence("technological_breakthrough", 0.2, 0.3, 12.0)); // Tech advances
        influences.add(new ExternalInfluence("conflict", 0.2, -0.1, 6.0)); // Cyber conflicts
        influences.add(new ExternalInfluence("economic_shock", 0.15, -0.2, 8.0)); // Tech market crashes
        influences.add(new ExternalInfluence("pandemic", 0.1, -0.1, 10.0)); // Biotech threats
        
        return influences;
    }
    
    /**
     * Create recovery-focused influences
     */
    private static List<ExternalInfluence> createRecoveryInfluences() {
        List<ExternalInfluence> influences = new ArrayList<>();
        
        influences.add(new ExternalInfluence("economic_shock", 0.1, 0.2, 12.0)); // Economic recovery
        influences.add(new ExternalInfluence("technological_breakthrough", 0.15, 0.2, 15.0)); // Rebuilding tech
        influences.add(new ExternalInfluence("resource_discovery", 0.2, 0.15, 8.0)); // New resources
        influences.add(new ExternalInfluence("population_change", 0.1, 0.1, 20.0)); // Population recovery
        influences.add(new ExternalInfluence("conflict", 0.05, -0.05, 4.0)); // Occasional setbacks
        
        return influences;
    }
    
    /**
     * Analyze conflict scenario results
     */
    private static void analyzeConflictResults(ForecastingEngine engine, String scenarioName, long executionTime) {
        System.out.println("\n=== " + scenarioName.toUpperCase() + " RESULTS ===");
        
        GlobalState globalState = engine.getGlobalState();
        ScenarioGenerator scenarioGen = engine.getScenarioGenerator();
        
        // Final state analysis
        System.out.println("Final Global State:");
        System.out.println("  Peace Index: " + String.format("%.2f", globalState.getIndicator("world_peace_index")) + 
                         getPeaceIndexAssessment(globalState.getIndicator("world_peace_index")));
        System.out.println("  Economic Growth: " + String.format("%.2f%%", globalState.getIndicator("economic_growth_rate") * 100) + 
                         getEconomicAssessment(globalState.getIndicator("economic_growth_rate")));
        System.out.println("  Resource Availability: " + String.format("%.2f", globalState.getIndicator("resource_availability")) + 
                         getResourceAssessment(globalState.getIndicator("resource_availability")));
        System.out.println("  Technology Level: " + String.format("%.2f", globalState.getIndicator("technology_advancement")) + 
                         getTechnologyAssessment(globalState.getIndicator("technology_advancement")));
        System.out.println("  Stability Index: " + String.format("%.2f", globalState.calculateStabilityIndex()) + 
                         getStabilityAssessment(globalState.calculateStabilityIndex()));
        
        // Agent impact analysis
        System.out.println("\nAgent Impact Analysis:");
        int totalDecisions = 0;
        int conflictDecisions = 0;
        int recoveryDecisions = 0;
        
        for (Agent agent : engine.getAgents()) {
            totalDecisions += agent.getDecisionCount();
            
            // Analyze decision patterns based on agent type
            if (agent instanceof CountryAgent) {
                CountryAgent country = (CountryAgent) agent;
                if (country.getInternalFactor("political_stability") < 0.4) {
                    conflictDecisions++;
                } else if (country.getInternalFactor("political_stability") > 0.7) {
                    recoveryDecisions++;
                }
            }
        }
        
        System.out.println("  Total Decisions: " + totalDecisions);
        System.out.println("  Conflict-Related Decisions: " + conflictDecisions);
        System.out.println("  Recovery-Related Decisions: " + recoveryDecisions);
        System.out.println("  Decision Ratio (Conflict/Recovery): " + 
                         String.format("%.2f", (double) conflictDecisions / Math.max(1, recoveryDecisions)));
        
        // Scenario analysis
        System.out.println("\nScenario Analysis:");
        List<ScenarioGenerator.Scenario> scenarios = scenarioGen.getAllScenarios();
        Map<String, ScenarioGenerator.ScenarioCluster> clusters = scenarioGen.getAllClusters();
        
        System.out.println("  Total Scenarios: " + scenarios.size());
        System.out.println("  Scenario Clusters: " + clusters.size());
        
        for (ScenarioGenerator.ScenarioCluster cluster : clusters.values()) {
            System.out.println("    " + cluster.getClusterName() + ": " + 
                             cluster.getScenarioIds().size() + " scenarios, " +
                             String.format("%.1f%%", cluster.getAverageProbability() * 100) + " avg probability");
        }
        
        // Conflict risk assessment
        System.out.println("\nConflict Risk Assessment:");
        double conflictRisk = calculateConflictRisk(globalState, scenarios);
        System.out.println("  Overall Conflict Risk: " + String.format("%.1f%%", conflictRisk * 100) + 
                         getRiskAssessment(conflictRisk));
        
        // Strategic recommendations
        System.out.println("\nStrategic Recommendations:");
        generateConflictRecommendations(globalState, conflictRisk, scenarioName);
        
        System.out.println("  Execution Time: " + executionTime + " ms");
    }
    
    /**
     * Calculate overall conflict risk based on global state and scenarios
     */
    private static double calculateConflictRisk(GlobalState globalState, List<ScenarioGenerator.Scenario> scenarios) {
        double peaceIndex = globalState.getIndicator("world_peace_index");
        double economicGrowth = globalState.getIndicator("economic_growth_rate");
        double resourceAvailability = globalState.getIndicator("resource_availability");
        double stability = globalState.calculateStabilityIndex();
        
        // Weighted risk calculation
        double risk = 0.0;
        risk += (1.0 - peaceIndex) * 0.4; // Peace index is most important
        risk += Math.max(0, -economicGrowth) * 0.3; // Economic decline increases risk
        risk += (1.0 - resourceAvailability) * 0.2; // Resource scarcity increases risk
        risk += (1.0 - stability) * 0.1; // Low stability increases risk
        
        return Math.min(1.0, risk);
    }
    
    /**
     * Generate strategic recommendations based on conflict analysis
     */
    private static void generateConflictRecommendations(GlobalState globalState, double conflictRisk, String scenarioName) {
        double peaceIndex = globalState.getIndicator("world_peace_index");
        double economicGrowth = globalState.getIndicator("economic_growth_rate");
        double resourceAvailability = globalState.getIndicator("resource_availability");
        
        if (conflictRisk > 0.7) {
            System.out.println("  🚨 HIGH CONFLICT RISK - Immediate action required:");
            System.out.println("    • Deploy diplomatic crisis management teams");
            System.out.println("    • Implement emergency resource allocation protocols");
            System.out.println("    • Activate conflict prevention measures");
        } else if (conflictRisk > 0.4) {
            System.out.println("  ⚠️  MODERATE CONFLICT RISK - Monitor closely:");
            System.out.println("    • Strengthen diplomatic relations");
            System.out.println("    • Prepare contingency plans");
            System.out.println("    • Increase resource stockpiling");
        } else {
            System.out.println("  ✅ LOW CONFLICT RISK - Maintain stability:");
            System.out.println("    • Continue current diplomatic efforts");
            System.out.println("    • Focus on economic development");
            System.out.println("    • Invest in long-term stability measures");
        }
        
        // Specific recommendations based on scenario
        if (scenarioName.contains("Resource")) {
            System.out.println("  📦 RESOURCE-SPECIFIC RECOMMENDATIONS:");
            System.out.println("    • Diversify resource supply chains");
            System.out.println("    • Invest in alternative energy sources");
            System.out.println("    • Develop resource-sharing agreements");
        } else if (scenarioName.contains("Technology")) {
            System.out.println("  🔬 TECHNOLOGY-SPECIFIC RECOMMENDATIONS:");
            System.out.println("    • Strengthen cybersecurity measures");
            System.out.println("    • Promote international tech cooperation");
            System.out.println("    • Regulate dual-use technologies");
        } else if (scenarioName.contains("Recovery")) {
            System.out.println("  🔄 RECOVERY-SPECIFIC RECOMMENDATIONS:");
            System.out.println("    • Focus on economic reconstruction");
            System.out.println("    • Rebuild international partnerships");
            System.out.println("    • Invest in social stability programs");
        }
    }
    
    // Assessment helper methods
    private static String getPeaceIndexAssessment(double peaceIndex) {
        if (peaceIndex > 0.8) return " (Very Peaceful)";
        if (peaceIndex > 0.6) return " (Peaceful)";
        if (peaceIndex > 0.4) return " (Moderate Tension)";
        if (peaceIndex > 0.2) return " (High Tension)";
        return " (Critical Conflict)";
    }
    
    private static String getEconomicAssessment(double growth) {
        if (growth > 0.05) return " (Strong Growth)";
        if (growth > 0.02) return " (Moderate Growth)";
        if (growth > -0.02) return " (Stagnant)";
        if (growth > -0.05) return " (Recession)";
        return " (Economic Crisis)";
    }
    
    private static String getResourceAssessment(double availability) {
        if (availability > 0.8) return " (Abundant)";
        if (availability > 0.6) return " (Adequate)";
        if (availability > 0.4) return " (Limited)";
        if (availability > 0.2) return " (Scarce)";
        return " (Critical Shortage)";
    }
    
    private static String getTechnologyAssessment(double techLevel) {
        if (techLevel > 0.8) return " (Advanced)";
        if (techLevel > 0.6) return " (Developed)";
        if (techLevel > 0.4) return " (Moderate)";
        if (techLevel > 0.2) return " (Basic)";
        return " (Underdeveloped)";
    }
    
    private static String getStabilityAssessment(double stability) {
        if (stability > 0.8) return " (Very Stable)";
        if (stability > 0.6) return " (Stable)";
        if (stability > 0.4) return " (Moderate)";
        if (stability > 0.2) return " (Unstable)";
        return " (Critical Instability)";
    }
    
    private static String getRiskAssessment(double risk) {
        if (risk > 0.7) return " (CRITICAL)";
        if (risk > 0.4) return " (HIGH)";
        if (risk > 0.2) return " (MODERATE)";
        return " (LOW)";
    }
}
