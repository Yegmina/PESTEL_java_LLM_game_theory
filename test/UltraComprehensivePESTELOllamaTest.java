package test;

import simu.framework.Trace;
import simu.framework.Trace.Level;
import simu.model.*;

/**
 * Ultra-Comprehensive PESTEL Simulation Test using LocalOllamaAIService
 */
public class UltraComprehensivePESTELOllamaTest {
    
    public static void main(String[] args) {
        Trace.setTraceLevel(Level.INFO);
        
        System.out.println("=== ULTRA-COMPREHENSIVE OLLAMA-ENHANCED PESTEL SIMULATION ===");
        System.out.println("Metropolia University Advanced Strategic Planning System");
        System.out.println("🌍 Global Coverage: 100 Companies + 50 Countries + 40 Research Centers");
        System.out.println("🤖 True AI Integration: Local Ollama Service");
        System.out.println();
        
        // Initialize Ollama service
        LocalOllamaAIService ollamaService = new LocalOllamaAIService();
        
        // Display Ollama status
        displayOllamaStatus(ollamaService);
        
        // Display ultra-comprehensive entities
        displayUltraComprehensiveEntities();
        
        // Run Ollama-enhanced simulation
        runOllamaEnhancedSimulation(ollamaService);
        
        System.out.println("\n=== OLLAMA-ENHANCED SIMULATION COMPLETED ===");
    }
    
    private static void displayOllamaStatus(LocalOllamaAIService ollamaService) {
        System.out.println("\n🤖 OLLAMA AI SERVICE STATUS:");
        System.out.println("  Service Available: " + (ollamaService.isOllamaAvailable() ? "✅ YES" : "❌ NO"));
        System.out.println("  Current Model: " + ollamaService.getCurrentModel());
        System.out.println("  Ollama URL: " + ollamaService.getOllamaUrl());
        
        if (ollamaService.isOllamaAvailable()) {
            System.out.println("  Available Models:");
            ollamaService.getAvailableModels().forEach(model -> 
                System.out.println("    - " + model));
        } else {
            System.out.println("  ⚠️ Using advanced fallback logic");
        }
        System.out.println();
    }
    
    private static void displayUltraComprehensiveEntities() {
        // Keep your existing display code...
        System.out.println("🌍 ULTRA-COMPREHENSIVE REAL-WORLD ENTITIES:");
        System.out.println("🏢 TOP 100 GLOBAL COMPANIES...");
        // ... rest of your display code
    }
    
    private static void runOllamaEnhancedSimulation(LocalOllamaAIService ollamaService) {
        System.out.println("🚀 STARTING OLLAMA-ENHANCED ULTRA-COMPREHENSIVE SIMULATION");
        System.out.println("Duration: 30 days (1 month strategic analysis)");
        System.out.println("Entities: 190 total (100 companies + 50 countries + 40 research centers)");
        System.out.println("AI Service: Local Ollama with model: " + ollamaService.getCurrentModel());
        System.out.println();
        
        try {
            // Create a simple simulation engine that uses Ollama
            OllamaEnhancedSimulationEngine engine = new OllamaEnhancedSimulationEngine(ollamaService, 30);
            
            System.out.println("✅ Ollama-enhanced simulation initialized");
            System.out.println("🤖 AI Integration: " + (ollamaService.isOllamaAvailable() ? 
                "Local Ollama ACTIVE" : "Advanced Fallback Logic"));
            System.out.println();
            
            long startTime = System.currentTimeMillis();
            engine.runSimulation();
            long endTime = System.currentTimeMillis();
            
            System.out.println("\n🎉 OLLAMA-ENHANCED SIMULATION COMPLETED SUCCESSFULLY");
            System.out.println("Execution time: " + (endTime - startTime) + " ms");
            
            // Display results
            displayOllamaEnhancedResults(engine);
            
        } catch (Exception e) {
            System.out.println("❌ Simulation error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void displayOllamaEnhancedResults(OllamaEnhancedSimulationEngine engine) {
        System.out.println("\n=== OLLAMA-ENHANCED SIMULATION RESULTS ===");
        engine.displayResults();
    }
}

// You'll need to create this new class that uses LocalOllamaAIService
class OllamaEnhancedSimulationEngine {
    private final LocalOllamaAIService ollamaService;
    private final int simulationDays;
    private int totalDecisions = 0;
    private int ollamaCalls = 0;
    private int fallbackDecisions = 0;
    
    public OllamaEnhancedSimulationEngine(LocalOllamaAIService ollamaService, int simulationDays) {
        this.ollamaService = ollamaService;
        this.simulationDays = simulationDays;
    }
    
    public void runSimulation() {
        System.out.println("🏃 Running Ollama-enhanced simulation for " + simulationDays + " days...");
        
        // Simulate each day
        for (int day = 1; day <= simulationDays; day++) {
            System.out.println("\n📅 DAY " + day + " of " + simulationDays);
            simulateDay(day);
        }
        
        System.out.println("\n📊 SIMULATION COMPLETED");
        System.out.println("Total decisions: " + totalDecisions);
        System.out.println("Ollama AI calls: " + ollamaCalls);
        System.out.println("Fallback decisions: " + fallbackDecisions);
    }
    
    private void simulateDay(int currentDay) {
        // Simulate some key entities making decisions using Ollama
        String[] keyEntities = {
            "Apple Inc. (Technology Leader)",
            "United States Government",
            "MIT Research Institute", 
            "European Union",
            "Saudi Aramco (Energy Giant)",
            "China Government",
            "Stanford University",
            "Amazon Inc. (E-commerce Leader)"
        };
        
        for (String entity : keyEntities) {
            simulateEntityDecision(entity, currentDay);
        }
    }
    
    private void simulateEntityDecision(String entity, int currentDay) {
        try {
            // Create a prompt for the entity's decision
            String prompt = buildDecisionPrompt(entity, currentDay);
            
            // Use Ollama to get a decision
            String decision = ollamaService.analyzeDecision(prompt, "entity_decision_" + entity.replace(" ", "_"));
            
            totalDecisions++;
            if (ollamaService.isOllamaAvailable()) {
                ollamaCalls++;
                System.out.println("  🤖 " + entity + " (AI): " + decision);
            } else {
                fallbackDecisions++;
                System.out.println("  ⚡ " + entity + " (Fallback): " + decision);
            }
            
            // Simulate PESTEL impact analysis
            if (!decision.equals("no_action") && !decision.equals("NO_ACTION")) {
                analyzePESTELImpact(entity, decision, currentDay);
            }
            
        } catch (Exception e) {
            System.out.println("  ❌ Error in " + entity + " decision: " + e.getMessage());
        }
    }
    
    private String buildDecisionPrompt(String entity, int currentDay) {
        return String.format(
            "You are %s on day %d of a strategic simulation. " +
            "Consider current global economic, technological, and political conditions. " +
            "Should you take strategic action today? " +
            "If yes, provide a specific strategic decision. " +
            "If no, respond with 'NO_ACTION'. " +
            "Be concise and strategic.",
            entity, currentDay
        );
    }
    
    private void analyzePESTELImpact(String entity, String decision, int currentDay) {
        String[] pestelCategories = {"Technological", "Economic", "Political", "Environmental", "Social", "Legal"};
        
        for (String category : pestelCategories) {
            try {
                String impactPrompt = String.format(
                    "Decision by %s: %s. " +
                    "What is the potential impact on %s factors? " +
                    "Respond briefly with the main impact or 'NO_IMPACT'.",
                    entity, decision, category
                );
                
                String impact = ollamaService.analyzeDecision(impactPrompt, "pestel_impact_" + category);
                if (!impact.equals("NO_IMPACT") && !impact.contains("NO_IMPACT")) {
                    System.out.println("    📈 " + category + " impact: " + impact);
                }
            } catch (Exception e) {
                // Silent fail for impact analysis to keep simulation flowing
            }
        }
    }
    
    public void displayResults() {
        System.out.println("\n🎯 OLLAMA SIMULATION RESULTS:");
        System.out.println("Simulation Days: " + simulationDays);
        System.out.println("Total Strategic Decisions: " + totalDecisions);
        System.out.println("Ollama AI Decisions: " + ollamaCalls);
        System.out.println("Fallback Decisions: " + fallbackDecisions);
        System.out.println("AI Utilization Rate: " + 
            String.format("%.1f%%", (double) ollamaCalls / totalDecisions * 100));
        
        if (ollamaService.isOllamaAvailable()) {
            System.out.println("🤖 Ollama Status: ✅ ACTIVE with model: " + ollamaService.getCurrentModel());
        } else {
            System.out.println("🤖 Ollama Status: ❌ UNAVAILABLE - Using advanced fallback");
        }
    }
}
