package simu.model;

import simu.framework.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Main PESTEL-based simulation engine that implements the day-by-day AI-driven decision making
 */
public class PESTELSimulationEngine extends Engine {
    private PESTELState globalPESTEL;
    private List<PESTELAgent> agents;
    private PESTELAIService aiService;
    private List<AgentAction> recentActions;
    private List<PESTELChange> recentChanges;
    private int currentDay;
    private int simulationDays;
    private boolean aiEnabled;
    
    private static final String[] PESTEL_CATEGORIES = {"political", "economic", "social", "technological", "environmental", "legal"};
    
    public PESTELSimulationEngine(int companies, int countries, int researchers, int simulationDays) {
        this.globalPESTEL = new PESTELState();
        this.agents = new ArrayList<>();
        this.recentActions = new ArrayList<>();
        this.recentChanges = new ArrayList<>();
        this.currentDay = 1;
        this.simulationDays = simulationDays;
        this.aiEnabled = false;
        
        // Initialize AI service
        String apiKey = System.getProperty("GEMINI_API_KEY");
        if (apiKey != null && !apiKey.isEmpty()) {
            this.aiService = new PESTELAIService(apiKey);
            this.aiEnabled = true;
            Trace.out(Trace.Level.INFO, "✓ PESTEL AI service initialized successfully");
        } else {
            Trace.out(Trace.Level.WAR, "⚠ PESTEL AI service not available (API key not provided)");
        }
        
        // Create agents
        createAgents(companies, countries, researchers);
    }
    
    private void createAgents(int companies, int countries, int researchers) {
        // Create company agents
        for (int i = 1; i <= companies; i++) {
            agents.add(new CompanyPESTELAgent("Company_" + i));
        }
        
        // Create country agents
        for (int i = 1; i <= countries; i++) {
            agents.add(new CountryPESTELAgent("Country_" + i));
        }
        
        // Create researcher agents
        for (int i = 1; i <= researchers; i++) {
            agents.add(new ResearcherPESTELAgent("Researcher_" + i));
        }
        
        Trace.out(Trace.Level.INFO, "Created " + agents.size() + " PESTEL agents:");
        Trace.out(Trace.Level.INFO, "  - " + companies + " companies");
        Trace.out(Trace.Level.INFO, "  - " + countries + " countries");
        Trace.out(Trace.Level.INFO, "  - " + researchers + " researchers");
    }
    
    @Override
    protected void initialize() {
        Trace.out(Trace.Level.INFO, "=== INITIALIZING PESTEL SIMULATION ===");
        Trace.out(Trace.Level.INFO, "Simulation Duration: " + simulationDays + " days");
        Trace.out(Trace.Level.INFO, "AI-Driven Decisions: " + (aiEnabled ? "Enabled" : "Disabled"));
        
        Trace.out(Trace.Level.INFO, "\n=== INITIAL GLOBAL PESTEL STATE ===");
        Trace.out(Trace.Level.INFO, globalPESTEL.toString());
        
        // Schedule first day event
        eventList.add(new Event(ForecastingEventType.TIME_STEP_ADVANCE, 1.0));
    }
    
    @Override
    protected void runEvent(Event event) {
        ForecastingEventType eventType = (ForecastingEventType) event.getType();
        
        switch (eventType) {
            case TIME_STEP_ADVANCE:
                processDailySimulation();
                break;
            default:
                Trace.out(Trace.Level.WAR, "Unknown event type: " + eventType);
        }
    }
    
    @Override
    protected void tryCEvents() {
        // No conditional events in this implementation
    }
    
    @Override
    protected void results() {
        Trace.out(Trace.Level.INFO, "\n=== PESTEL SIMULATION RESULTS ===");
        Trace.out(Trace.Level.INFO, "Simulation completed after " + currentDay + " days");
        
        // Display final global PESTEL state
        Trace.out(Trace.Level.INFO, "\n=== FINAL GLOBAL PESTEL STATE ===");
        Trace.out(Trace.Level.INFO, globalPESTEL.toString());
        
        // Display agent statistics
        displayAgentStatistics();
        
        // Display recent changes summary
        displayChangesSummary();
    }
    
    /**
     * Process one day of simulation
     */
    private void processDailySimulation() {
        Trace.out(Trace.Level.INFO, "\n========== DAY " + currentDay + " ==========");
        
        // Process each agent
        for (PESTELAgent agent : agents) {
            processAgentDay(agent);
        }
        
        // Clean up old actions (keep last 10 days)
        cleanupOldActions();
        
        // Schedule next day if simulation not complete
        currentDay++;
        if (currentDay <= simulationDays) {
            eventList.add(new Event(ForecastingEventType.TIME_STEP_ADVANCE, currentDay));
        }
    }
    
    /**
     * Process one agent's decision for the day
     */
    private void processAgentDay(PESTELAgent agent) {
        Trace.out(Trace.Level.INFO, "\n--- Processing " + agent.getAgentId() + " ---");
        
        try {
            // Step 1: Ask AI if agent wants to take action
            String decision = askAgentForDecision(agent);
            
            if (decision.equals("no_action")) {
                Trace.out(Trace.Level.INFO, agent.getAgentId() + ": No action taken");
                return;
            }
            
            Trace.out(Trace.Level.INFO, agent.getAgentId() + " decides: " + decision);
            
            // Record the action
            AgentAction action = new AgentAction(agent.getAgentId(), currentDay, decision, "decision");
            recentActions.add(action);
            agent.recordAction(decision);
            
            // Step 2: Process PESTEL impacts
            processPESTELImpacts(decision, agent.getAgentId());
            
            // Step 3: Find and update affected agents
            updateAffectedAgents(decision, agent);
            
        } catch (Exception e) {
            Trace.out(Trace.Level.WAR, "Error processing " + agent.getAgentId() + ": " + e.getMessage());
        }
    }
    
    /**
     * Ask agent for decision (AI or fallback)
     */
    private String askAgentForDecision(PESTELAgent agent) {
        if (aiEnabled) {
            try {
                CompletableFuture<String> future = aiService.askAgentDecision(agent, globalPESTEL, currentDay, recentActions);
                String result = future.get();
                return result != null ? result : "no_action";
            } catch (Exception e) {
                Trace.out(Trace.Level.WAR, "AI decision failed for " + agent.getAgentId() + ", using fallback");
            }
        }
        
        // Fallback: use agent's built-in decision logic
        AgentDecision decision = agent.makeDecision(globalPESTEL, currentDay, recentActions);
        return decision != null ? decision.getDescription() : "no_action";
    }
    
    /**
     * Process how a decision affects each PESTEL category
     */
    private void processPESTELImpacts(String decision, String agentId) {
        for (String category : PESTEL_CATEGORIES) {
            try {
                String impact = getPESTELImpact(decision, category);
                
                if (!impact.equals("NO_IMPACT")) {
                    applyPESTELChange(impact, category, agentId);
                }
            } catch (Exception e) {
                Trace.out(Trace.Level.WAR, "Error processing " + category + " impact: " + e.getMessage());
            }
        }
    }
    
    /**
     * Get PESTEL impact from AI or fallback
     */
    private String getPESTELImpact(String decision, String category) {
        if (aiEnabled) {
            try {
                CompletableFuture<String> future = aiService.askPESTELImpact(decision, category, globalPESTEL);
                return future.get();
            } catch (Exception e) {
                Trace.out(Trace.Level.WAR, "AI PESTEL impact failed for " + category + ", using fallback");
            }
        }
        
        // Fallback: simple impact logic
        return generateFallbackImpact(decision, category);
    }
    
    /**
     * Apply a PESTEL change to global state
     */
    private void applyPESTELChange(String impact, String category, String agentId) {
        if (impact.startsWith("FACTOR:")) {
            // Parse structured impact
            String[] parts = impact.split("\\|");
            String factor = parts[0].substring(7); // Remove "FACTOR:"
            String newValue = parts[1].substring(10); // Remove "NEW_VALUE:"
            String reason = parts.length > 2 ? parts[2].substring(7) : ""; // Remove "REASON:"
            
            // Get old value
            String oldValue = globalPESTEL.getFactor(category, factor);
            
            // Apply change
            globalPESTEL.updateFactor(category, factor, newValue);
            
            // Record change
            PESTELChange change = new PESTELChange(category, factor, oldValue, newValue, reason, agentId, currentDay);
            recentChanges.add(change);
            
            Trace.out(Trace.Level.INFO, "  PESTEL Change: " + change.toString());
        }
    }
    
    /**
     * Update agents affected by a decision
     */
    private void updateAffectedAgents(String decision, PESTELAgent sourceAgent) {
        List<String> affectedAgentIds = getAffectedAgents(decision);
        
        for (String agentId : affectedAgentIds) {
            if (!agentId.equals(sourceAgent.getAgentId())) {
                PESTELAgent affectedAgent = findAgentById(agentId);
                if (affectedAgent != null) {
                    // Update affected agent with recent changes
                    List<PESTELChange> relevantChanges = getRecentChangesForAgent(affectedAgent);
                    affectedAgent.updateFromPESTELChanges(relevantChanges);
                    
                    Trace.out(Trace.Level.INFO, "  Updated " + agentId + " with PESTEL changes");
                }
            }
        }
    }
    
    /**
     * Get list of affected agents from AI or fallback
     */
    private List<String> getAffectedAgents(String decision) {
        if (aiEnabled) {
            try {
                CompletableFuture<List<String>> future = aiService.askAffectedAgents(decision, agents);
                return future.get();
            } catch (Exception e) {
                Trace.out(Trace.Level.WAR, "AI affected agents failed, using fallback");
            }
        }
        
        // Fallback: simple logic
        return generateFallbackAffectedAgents(decision);
    }
    
    /**
     * Generate fallback PESTEL impact
     */
    private String generateFallbackImpact(String decision, String category) {
        // Simple fallback logic based on keywords
        String lowerDecision = decision.toLowerCase();
        
        switch (category.toLowerCase()) {
            case "economic":
                if (lowerDecision.contains("invest") || lowerDecision.contains("expand")) {
                    return "FACTOR:growth|NEW_VALUE:Increased economic activity due to investment|REASON:Investment stimulates economic growth";
                }
                break;
            case "technological":
                if (lowerDecision.contains("research") || lowerDecision.contains("innovation") || lowerDecision.contains("technology")) {
                    return "FACTOR:innovation|NEW_VALUE:Enhanced innovation capabilities|REASON:Focus on research and technology development";
                }
                break;
            case "environmental":
                if (lowerDecision.contains("climate") || lowerDecision.contains("green") || lowerDecision.contains("sustainable")) {
                    return "FACTOR:sustainability|NEW_VALUE:Improved sustainability practices|REASON:Environmental initiatives implemented";
                }
                break;
        }
        
        return "NO_IMPACT";
    }
    
    /**
     * Generate fallback affected agents list
     */
    private List<String> generateFallbackAffectedAgents(String decision) {
        List<String> affected = new ArrayList<>();
        String lowerDecision = decision.toLowerCase();
        
        // Simple logic: economic decisions affect companies, policy decisions affect countries, etc.
        if (lowerDecision.contains("policy") || lowerDecision.contains("regulation")) {
            // Add some companies
            for (PESTELAgent agent : agents) {
                if (agent instanceof CompanyPESTELAgent && Math.random() < 0.3) {
                    affected.add(agent.getAgentId());
                }
            }
        }
        
        if (lowerDecision.contains("research") || lowerDecision.contains("innovation")) {
            // Add some researchers
            for (PESTELAgent agent : agents) {
                if (agent instanceof ResearcherPESTELAgent && Math.random() < 0.4) {
                    affected.add(agent.getAgentId());
                }
            }
        }
        
        return affected;
    }
    
    /**
     * Find agent by ID
     */
    private PESTELAgent findAgentById(String agentId) {
        return agents.stream()
                .filter(agent -> agent.getAgentId().equals(agentId))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Get recent changes relevant to an agent
     */
    private List<PESTELChange> getRecentChangesForAgent(PESTELAgent agent) {
        // Return last 5 changes that might affect this agent
        return recentChanges.stream()
                .filter(change -> change.getDay() >= currentDay - 5)
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Clean up old actions to prevent memory issues
     */
    private void cleanupOldActions() {
        recentActions.removeIf(action -> action.getDay() < currentDay - 10);
        recentChanges.removeIf(change -> change.getDay() < currentDay - 10);
    }
    
    /**
     * Display agent statistics
     */
    private void displayAgentStatistics() {
        Trace.out(Trace.Level.INFO, "\n=== AGENT STATISTICS ===");
        
        int totalDecisions = 0;
        for (PESTELAgent agent : agents) {
            totalDecisions += agent.getDecisionCount();
            Trace.out(Trace.Level.INFO, agent.toString());
        }
        
        Trace.out(Trace.Level.INFO, "Total decisions made: " + totalDecisions);
        Trace.out(Trace.Level.INFO, "Average decisions per agent: " + 
                 String.format("%.2f", (double) totalDecisions / agents.size()));
    }
    
    /**
     * Display summary of changes
     */
    private void displayChangesSummary() {
        Trace.out(Trace.Level.INFO, "\n=== CHANGES SUMMARY ===");
        Trace.out(Trace.Level.INFO, "Total PESTEL changes: " + recentChanges.size());
        
        Map<String, Integer> categoryChanges = new HashMap<>();
        for (PESTELChange change : recentChanges) {
            categoryChanges.merge(change.getCategory(), 1, Integer::sum);
        }
        
        categoryChanges.forEach((category, count) -> 
            Trace.out(Trace.Level.INFO, "  " + category.toUpperCase() + ": " + count + " changes"));
    }
    
    // Getters
    public PESTELState getGlobalPESTEL() {
        return globalPESTEL;
    }
    
    public List<PESTELAgent> getAgents() {
        return new ArrayList<>(agents);
    }
    
    public List<AgentAction> getRecentActions() {
        return new ArrayList<>(recentActions);
    }
    
    public List<PESTELChange> getRecentChanges() {
        return new ArrayList<>(recentChanges);
    }
    
    public int getCurrentDay() {
        return currentDay;
    }
    
    public boolean isAIEnabled() {
        return aiEnabled;
    }
}
