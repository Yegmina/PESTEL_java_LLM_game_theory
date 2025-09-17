package test;

import simu.framework.Trace;
import simu.framework.Trace.Level;
import simu.model.*;

import java.util.*;

/**
 * Test runner for the PESTEL-based simulation system
 */
public class PESTELSimulationTest {
    
    public static void main(String[] args) {
        // Set trace level for detailed output
        Trace.setTraceLevel(Level.INFO);
        
        System.out.println("=== PESTEL-BASED AI SIMULATION TEST ===");
        System.out.println("Metropolia University of Applied Sciences");
        System.out.println("Strategic Planning with AI-Driven PESTEL Analysis");
        System.out.println();
        
        // Run different test scenarios
        runBasicPESTELTest();
        runExtendedPESTELTest();
        
        System.out.println("\n=== PESTEL SIMULATION TESTS COMPLETED ===");
    }
    
    /**
     * Basic PESTEL test - short duration
     */
    private static void runBasicPESTELTest() {
        System.out.println("=== BASIC PESTEL TEST (7 DAYS) ===");
        
        // Create simulation with moderate number of agents
        PESTELSimulationEngine engine = new PESTELSimulationEngine(3, 2, 2, 7);
        engine.setSimulationTime(7.0); // 7 days
        
        System.out.println("Configuration:");
        System.out.println("  - 3 Company agents");
        System.out.println("  - 2 Country agents");
        System.out.println("  - 2 Researcher agents");
        System.out.println("  - 7 days simulation");
        System.out.println("  - AI-driven decision making: " + (engine.isAIEnabled() ? "Enabled" : "Disabled"));
        System.out.println();
        
        long startTime = System.currentTimeMillis();
        engine.run();
        long endTime = System.currentTimeMillis();
        
        System.out.println("\nBasic PESTEL test completed in " + (endTime - startTime) + " ms");
        
        // Display key results
        displayPESTELResults(engine, "Basic Test");
    }
    
    /**
     * Extended PESTEL test - longer duration
     */
    private static void runExtendedPESTELTest() {
        System.out.println("\n=== EXTENDED PESTEL TEST (30 DAYS) ===");
        
        // Create simulation with more agents and longer duration
        PESTELSimulationEngine engine = new PESTELSimulationEngine(4, 3, 3, 30);
        engine.setSimulationTime(30.0); // 30 days
        
        System.out.println("Configuration:");
        System.out.println("  - 4 Company agents");
        System.out.println("  - 3 Country agents");
        System.out.println("  - 3 Researcher agents");
        System.out.println("  - 30 days simulation");
        System.out.println("  - AI-driven decision making: " + (engine.isAIEnabled() ? "Enabled" : "Disabled"));
        System.out.println();
        
        long startTime = System.currentTimeMillis();
        engine.run();
        long endTime = System.currentTimeMillis();
        
        System.out.println("\nExtended PESTEL test completed in " + (endTime - startTime) + " ms");
        
        // Display key results
        displayPESTELResults(engine, "Extended Test");
    }
    
    /**
     * Display PESTEL simulation results
     */
    private static void displayPESTELResults(PESTELSimulationEngine engine, String testName) {
        System.out.println("\n=== " + testName.toUpperCase() + " RESULTS ===");
        
        // Global PESTEL state summary
        PESTELState globalState = engine.getGlobalPESTEL();
        System.out.println("\nFinal Global PESTEL State Summary:");
        displayPESTELSummary(globalState);
        
        // Agent activity summary
        System.out.println("\nAgent Activity Summary:");
        List<PESTELAgent> agents = engine.getAgents();
        int totalDecisions = 0;
        
        Map<String, Integer> agentTypeDecisions = new HashMap<>();
        for (PESTELAgent agent : agents) {
            int decisions = agent.getDecisionCount();
            totalDecisions += decisions;
            
            String type = agent.getAgentType().toString();
            agentTypeDecisions.merge(type, decisions, Integer::sum);
            
            System.out.println("  " + agent.getAgentId() + " (" + type + "): " + 
                             decisions + " decisions");
        }
        
        System.out.println("\nDecisions by Agent Type:");
        agentTypeDecisions.forEach((type, count) -> 
            System.out.println("  " + type + ": " + count + " decisions"));
        
        System.out.println("  Total Decisions: " + totalDecisions);
        System.out.println("  Average per Agent: " + 
                         String.format("%.2f", (double) totalDecisions / agents.size()));
        
        // Recent actions summary
        System.out.println("\nRecent Actions Summary:");
        List<AgentAction> recentActions = engine.getRecentActions();
        System.out.println("  Total Actions Recorded: " + recentActions.size());
        
        if (!recentActions.isEmpty()) {
            System.out.println("  Last 5 Actions:");
            recentActions.stream()
                .skip(Math.max(0, recentActions.size() - 5))
                .forEach(action -> System.out.println("    " + action.toString()));
        }
        
        // PESTEL changes summary
        System.out.println("\nPESTEL Changes Summary:");
        List<PESTELChange> changes = engine.getRecentChanges();
        System.out.println("  Total Changes: " + changes.size());
        
        Map<String, Integer> categoryChanges = new HashMap<>();
        for (PESTELChange change : changes) {
            categoryChanges.merge(change.getCategory().toUpperCase(), 1, Integer::sum);
        }
        
        if (!categoryChanges.isEmpty()) {
            System.out.println("  Changes by Category:");
            categoryChanges.forEach((category, count) -> 
                System.out.println("    " + category + ": " + count + " changes"));
        }
        
        // Strategic insights
        System.out.println("\nStrategic Insights:");
        generateStrategicInsights(engine);
    }
    
    /**
     * Display condensed PESTEL state summary
     */
    private static void displayPESTELSummary(PESTELState state) {
        System.out.println("  [POLITICAL]");
        System.out.println("    Stability: " + state.getPolitical("stability"));
        System.out.println("    Policies: " + truncateText(state.getPolitical("policies_laws"), 60));
        
        System.out.println("  [ECONOMIC]");
        System.out.println("    Growth: " + state.getEconomic("growth"));
        System.out.println("    Employment: " + truncateText(state.getEconomic("employment"), 60));
        
        System.out.println("  [SOCIAL]");
        System.out.println("    Population: " + truncateText(state.getSocial("population"), 60));
        System.out.println("    Lifestyle: " + truncateText(state.getSocial("lifestyle"), 60));
        
        System.out.println("  [TECHNOLOGICAL]");
        System.out.println("    Innovation: " + truncateText(state.getTechnological("innovation"), 60));
        System.out.println("    R&D: " + truncateText(state.getTechnological("rd_activity"), 60));
        
        System.out.println("  [ENVIRONMENTAL]");
        System.out.println("    Climate: " + truncateText(state.getEnvironmental("climate_change"), 60));
        System.out.println("    Sustainability: " + truncateText(state.getEnvironmental("sustainability"), 60));
        
        System.out.println("  [LEGAL]");
        System.out.println("    Data Protection: " + truncateText(state.getLegal("data_protection"), 60));
        System.out.println("    Health & Safety: " + truncateText(state.getLegal("health_safety"), 60));
    }
    
    /**
     * Generate strategic insights based on simulation results
     */
    private static void generateStrategicInsights(PESTELSimulationEngine engine) {
        List<PESTELAgent> agents = engine.getAgents();
        List<PESTELChange> changes = engine.getRecentChanges();
        
        // Analyze agent activity levels
        double avgDecisions = agents.stream()
                .mapToInt(PESTELAgent::getDecisionCount)
                .average()
                .orElse(0.0);
        
        if (avgDecisions > 5) {
            System.out.println("  ✓ High agent activity - dynamic environment with frequent decisions");
        } else if (avgDecisions > 2) {
            System.out.println("  → Moderate agent activity - balanced decision-making environment");
        } else {
            System.out.println("  ⚠ Low agent activity - stable but potentially stagnant environment");
        }
        
        // Analyze PESTEL change patterns
        Map<String, Long> categoryFrequency = changes.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    PESTELChange::getCategory,
                    java.util.stream.Collectors.counting()
                ));
        
        if (!categoryFrequency.isEmpty()) {
            String mostChanged = categoryFrequency.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("none");
            
            System.out.println("  📊 Most dynamic PESTEL category: " + mostChanged.toUpperCase());
        }
        
        // AI system performance
        if (engine.isAIEnabled()) {
            System.out.println("  🤖 AI-driven decision making active - enhanced strategic analysis");
        } else {
            System.out.println("  💡 Fallback decision logic used - consider enabling AI for better insights");
        }
        
        // Recommendations
        System.out.println("  📋 Recommendations:");
        System.out.println("    • Monitor " + (categoryFrequency.isEmpty() ? "all" : 
            categoryFrequency.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("key")) + " PESTEL factors closely");
        System.out.println("    • Encourage inter-agent collaboration for better outcomes");
        System.out.println("    • Regular PESTEL analysis updates recommended");
    }
    
    /**
     * Truncate text to specified length
     */
    private static String truncateText(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }
}
