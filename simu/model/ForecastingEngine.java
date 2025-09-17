package simu.model;

import simu.framework.*;
import java.util.*;
import java.util.Random;

/**
 * Main forecasting engine that implements the AI agent-based forecasting system
 * using the ABC phase simulation framework. This engine coordinates agent interactions,
 * global state management, external influences, and scenario generation.
 */
public class ForecastingEngine extends Engine {
    private GlobalState globalState;
    private List<Agent> agents;
    private List<ExternalInfluence> externalInfluences;
    private ScenarioGenerator scenarioGenerator;
    private List<AgentDecision> currentDecisions;
    private Map<String, Integer> agentCounts;
    private double timeStepSize;
    private int currentTimeStep;
    private Random random;
    
    // Configuration parameters
    private static final int DEFAULT_COMPANIES = 5;
    private static final int DEFAULT_COUNTRIES = 3;
    private static final int DEFAULT_RESEARCHERS = 4;
    private static final double DEFAULT_TIME_STEP = 1.0; // 1 month per time step
    
    /**
     * Initialize the forecasting engine
     */
    public ForecastingEngine() {
        this.globalState = new GlobalState();
        this.agents = new ArrayList<>();
        this.externalInfluences = new ArrayList<>();
        this.scenarioGenerator = new ScenarioGenerator();
        this.currentDecisions = new ArrayList<>();
        this.agentCounts = new HashMap<>();
        this.timeStepSize = DEFAULT_TIME_STEP;
        this.currentTimeStep = 0;
        this.random = new Random();
        
        // Set default agent counts
        agentCounts.put("companies", DEFAULT_COMPANIES);
        agentCounts.put("countries", DEFAULT_COUNTRIES);
        agentCounts.put("researchers", DEFAULT_RESEARCHERS);
    }
    
    /**
     * Initialize the forecasting engine with custom agent counts
     * @param companies Number of company agents
     * @param countries Number of country agents
     * @param researchers Number of researcher agents
     */
    public ForecastingEngine(int companies, int countries, int researchers) {
        this();
        agentCounts.put("companies", companies);
        agentCounts.put("countries", countries);
        agentCounts.put("researchers", researchers);
    }
    
    @Override
    protected void initialize() {
        Trace.out(Trace.Level.INFO, "Initializing AI Agent-Based Forecasting System");
        
        // Create agents
        createAgents();
        
        // Create external influences
        createExternalInfluences();
        
        // Generate initial scenarios
        List<ScenarioGenerator.Scenario> initialScenarios = 
            scenarioGenerator.generateInitialScenarios(globalState, currentTimeStep);
        
        // Schedule initial events
        scheduleInitialEvents();
        
        Trace.out(Trace.Level.INFO, "System initialized with " + agents.size() + " agents");
        Trace.out(Trace.Level.INFO, "Global state: " + globalState.toString());
    }
    
    @Override
    protected void runEvent(Event event) {
        // B-phase: Execute scheduled events
        ForecastingEventType eventType = (ForecastingEventType) event.getType();
        double currentTime = event.getTime();
        
        Trace.out(Trace.Level.INFO, "Executing event: " + eventType + " at time " + currentTime);
        
        switch (eventType) {
            case AGENT_DECISION:
                handleAgentDecisionEvent(currentTime);
                break;
                
            case GLOBAL_STATE_UPDATE:
                handleGlobalStateUpdateEvent(currentTime);
                break;
                
            case EXTERNAL_INFLUENCE:
                handleExternalInfluenceEvent(currentTime);
                break;
                
            case SCENARIO_GENERATION:
                handleScenarioGenerationEvent(currentTime);
                break;
                
            case TIME_STEP_ADVANCE:
                handleTimeStepAdvanceEvent(currentTime);
                break;
                
            case AI_ANALYSIS:
                handleAIAnalysisEvent(currentTime);
                break;
                
            default:
                Trace.out(Trace.Level.WAR, "Unknown event type: " + eventType);
        }
    }
    
    @Override
    protected void tryCEvents() {
        // C-phase: Execute conditional events
        double currentTime = Clock.getInstance().getClock();
        
        // Check for agent collaboration opportunities
        checkAgentCollaborations(currentTime);
        
        // Check for scenario transitions
        checkScenarioTransitions(currentTime);
        
        // Check for external influence triggers
        checkExternalInfluenceTriggers(currentTime);
    }
    
    @Override
    protected void results() {
        Trace.out(Trace.Level.INFO, "=== FORECASTING SIMULATION RESULTS ===");
        Trace.out(Trace.Level.INFO, "Simulation completed at time: " + Clock.getInstance().getClock());
        Trace.out(Trace.Level.INFO, "Total time steps: " + currentTimeStep);
        
        // Display final global state
        Trace.out(Trace.Level.INFO, "\nFinal Global State:");
        Trace.out(Trace.Level.INFO, globalState.toString());
        
        // Display agent statistics
        displayAgentStatistics();
        
        // Display scenario results
        displayScenarioResults();
        
        // Display cluster analysis
        displayClusterAnalysis();
        
        // Display strategic recommendations
        displayStrategicRecommendations();
    }
    
    /**
     * Create all agents in the system
     */
    private void createAgents() {
        // Create company agents
        for (int i = 0; i < agentCounts.get("companies"); i++) {
            String agentId = "Company_" + (i + 1);
            agents.add(new CompanyAgent(agentId, globalState));
        }
        
        // Create country agents
        for (int i = 0; i < agentCounts.get("countries"); i++) {
            String agentId = "Country_" + (i + 1);
            agents.add(new CountryAgent(agentId, globalState));
        }
        
        // Create researcher agents
        for (int i = 0; i < agentCounts.get("researchers"); i++) {
            String agentId = "Researcher_" + (i + 1);
            agents.add(new ResearcherAgent(agentId, globalState));
        }
        
        Trace.out(Trace.Level.INFO, "Created " + agents.size() + " agents:");
        Trace.out(Trace.Level.INFO, "  - " + agentCounts.get("companies") + " companies");
        Trace.out(Trace.Level.INFO, "  - " + agentCounts.get("countries") + " countries");
        Trace.out(Trace.Level.INFO, "  - " + agentCounts.get("researchers") + " researchers");
    }
    
    /**
     * Create external influences
     */
    private void createExternalInfluences() {
        externalInfluences.addAll(Arrays.asList(ExternalInfluence.createCommonInfluences()));
        Trace.out(Trace.Level.INFO, "Created " + externalInfluences.size() + " external influences");
    }
    
    /**
     * Schedule initial events
     */
    private void scheduleInitialEvents() {
        // Schedule first agent decision event
        eventList.add(new Event(ForecastingEventType.AGENT_DECISION, timeStepSize));
        
        // Schedule first global state update
        eventList.add(new Event(ForecastingEventType.GLOBAL_STATE_UPDATE, timeStepSize * 0.5));
        
        // Schedule first external influence check
        eventList.add(new Event(ForecastingEventType.EXTERNAL_INFLUENCE, timeStepSize * 0.3));
        
        // Schedule first scenario generation
        eventList.add(new Event(ForecastingEventType.SCENARIO_GENERATION, timeStepSize * 2));
        
        // Schedule first AI analysis
        eventList.add(new Event(ForecastingEventType.AI_ANALYSIS, timeStepSize * 1.5));
    }
    
    /**
     * Handle agent decision events
     */
    private void handleAgentDecisionEvent(double currentTime) {
        currentDecisions.clear();
        
        // Collect decisions from all agents
        for (Agent agent : agents) {
            AgentDecision decision = agent.makeDecision(currentTime);
            currentDecisions.add(decision);
            
            // Apply agent influence to global state
            agent.influenceGlobalState(globalState, decision);
            
            Trace.out(Trace.Level.INFO, "Agent " + agent.getAgentId() + " made decision: " + 
                     decision.getDecisionType() + " (impact: " + 
                     String.format("%.2f", decision.getExpectedImpact()) + ")");
        }
        
        // Schedule next agent decision event
        eventList.add(new Event(ForecastingEventType.AGENT_DECISION, currentTime + timeStepSize));
    }
    
    /**
     * Handle global state update events
     */
    private void handleGlobalStateUpdateEvent(double currentTime) {
        // Update agents based on global state changes
        for (Agent agent : agents) {
            agent.updateFromGlobalState(globalState);
        }
        
        Trace.out(Trace.Level.INFO, "Global state updated at time " + currentTime);
        Trace.out(Trace.Level.INFO, "Stability index: " + 
                 String.format("%.2f", globalState.calculateStabilityIndex()));
        
        // Schedule next global state update
        eventList.add(new Event(ForecastingEventType.GLOBAL_STATE_UPDATE, currentTime + timeStepSize));
    }
    
    /**
     * Handle external influence events
     */
    private void handleExternalInfluenceEvent(double currentTime) {
        for (ExternalInfluence influence : externalInfluences) {
            influence.applyInfluence(globalState, currentTime);
        }
        
        // Schedule next external influence check
        eventList.add(new Event(ForecastingEventType.EXTERNAL_INFLUENCE, currentTime + timeStepSize * 0.5));
    }
    
    /**
     * Handle scenario generation events
     */
    private void handleScenarioGenerationEvent(double currentTime) {
        // Calculate scenario probabilities based on current decisions and influences
        scenarioGenerator.calculateScenarioProbabilities(currentDecisions, externalInfluences);
        
        // Cluster scenarios
        scenarioGenerator.clusterScenarios();
        
        Trace.out(Trace.Level.INFO, "Scenarios generated and clustered at time " + currentTime);
        
        // Schedule next scenario generation
        eventList.add(new Event(ForecastingEventType.SCENARIO_GENERATION, currentTime + timeStepSize * 3));
    }
    
    /**
     * Handle time step advance events
     */
    private void handleTimeStepAdvanceEvent(double currentTime) {
        currentTimeStep++;
        Trace.out(Trace.Level.INFO, "Advanced to time step " + currentTimeStep + " at time " + currentTime);
        
        // Schedule next time step advance
        eventList.add(new Event(ForecastingEventType.TIME_STEP_ADVANCE, currentTime + timeStepSize));
    }
    
    /**
     * Handle AI analysis events
     */
    private void handleAIAnalysisEvent(double currentTime) {
        // Perform AI analysis of current state and generate insights
        performAIAnalysis(currentTime);
        
        // Schedule next AI analysis
        eventList.add(new Event(ForecastingEventType.AI_ANALYSIS, currentTime + timeStepSize * 2));
    }
    
    /**
     * Check for agent collaboration opportunities
     */
    private void checkAgentCollaborations(double currentTime) {
        // Randomly select agents for potential collaboration
        if (agents.size() >= 2 && random.nextDouble() < 0.3) { // 30% chance
            Agent agent1 = agents.get(random.nextInt(agents.size()));
            Agent agent2 = agents.get(random.nextInt(agents.size()));
            
            if (agent1 != agent2) {
                agent1.collaborate(agent2, currentTime);
                Trace.out(Trace.Level.INFO, "Collaboration between " + agent1.getAgentId() + 
                         " and " + agent2.getAgentId() + " at time " + currentTime);
            }
        }
    }
    
    /**
     * Check for scenario transitions
     */
    private void checkScenarioTransitions(double currentTime) {
        // Check if conditions are met for scenario transitions
        double stability = globalState.calculateStabilityIndex();
        
        if (stability < 0.3 || stability > 0.8) {
            // High instability or high stability - potential for scenario transitions
            Trace.out(Trace.Level.INFO, "Scenario transition conditions met at time " + currentTime + 
                     " (stability: " + String.format("%.2f", stability) + ")");
        }
    }
    
    /**
     * Check for external influence triggers
     */
    private void checkExternalInfluenceTriggers(double currentTime) {
        // Additional checks for external influences based on current conditions
        double globalTemp = globalState.getIndicator("global_temperature");
        
        if (globalTemp > 20) {
            // High temperature - increased chance of climate-related events
            if (random.nextDouble() < 0.1) { // 10% chance
                Trace.out(Trace.Level.INFO, "Climate stress detected at time " + currentTime);
            }
        }
    }
    
    /**
     * Perform AI analysis of current state
     */
    private void performAIAnalysis(double currentTime) {
        // Simulate AI analysis using Gemma 3 model capabilities
        double stability = globalState.calculateStabilityIndex();
        double economicGrowth = globalState.getIndicator("economic_growth_rate");
        double technologyAdvancement = globalState.getIndicator("technology_advancement");
        
        Trace.out(Trace.Level.INFO, "AI Analysis at time " + currentTime + ":");
        Trace.out(Trace.Level.INFO, "  System Stability: " + String.format("%.2f", stability));
        Trace.out(Trace.Level.INFO, "  Economic Growth: " + String.format("%.2f%%", economicGrowth * 100));
        Trace.out(Trace.Level.INFO, "  Technology Level: " + String.format("%.2f", technologyAdvancement));
        
        // Generate AI insights
        if (stability > 0.7) {
            Trace.out(Trace.Level.INFO, "  AI Insight: System shows high stability - good conditions for growth");
        } else if (stability < 0.3) {
            Trace.out(Trace.Level.INFO, "  AI Insight: System shows low stability - adaptation strategies recommended");
        } else {
            Trace.out(Trace.Level.INFO, "  AI Insight: System shows moderate stability - monitor key indicators");
        }
    }
    
    /**
     * Display agent statistics
     */
    private void displayAgentStatistics() {
        Trace.out(Trace.Level.INFO, "\n=== AGENT STATISTICS ===");
        
        int totalDecisions = 0;
        for (Agent agent : agents) {
            totalDecisions += agent.getDecisionCount();
            Trace.out(Trace.Level.INFO, agent.toString());
        }
        
        Trace.out(Trace.Level.INFO, "Total decisions made: " + totalDecisions);
        Trace.out(Trace.Level.INFO, "Average decisions per agent: " + 
                 String.format("%.2f", (double) totalDecisions / agents.size()));
    }
    
    /**
     * Display scenario results
     */
    private void displayScenarioResults() {
        Trace.out(Trace.Level.INFO, "\n=== SCENARIO RESULTS ===");
        Trace.out(Trace.Level.INFO, scenarioGenerator.getSummary());
        
        List<ScenarioGenerator.Scenario> scenarios = scenarioGenerator.getAllScenarios();
        for (ScenarioGenerator.Scenario scenario : scenarios) {
            Trace.out(Trace.Level.INFO, "Scenario " + scenario.getScenarioId() + ": " + 
                     scenario.getScenarioName() + " (probability: " + 
                     String.format("%.2f%%", scenario.getProbability() * 100) + ")");
        }
    }
    
    /**
     * Display cluster analysis
     */
    private void displayClusterAnalysis() {
        Trace.out(Trace.Level.INFO, "\n=== CLUSTER ANALYSIS ===");
        
        Map<String, ScenarioGenerator.ScenarioCluster> clusters = scenarioGenerator.getAllClusters();
        for (ScenarioGenerator.ScenarioCluster cluster : clusters.values()) {
            Trace.out(Trace.Level.INFO, cluster.getClusterName() + " (" + cluster.getClusterType() + "):");
            Trace.out(Trace.Level.INFO, "  Scenarios: " + cluster.getScenarioIds().size());
            Trace.out(Trace.Level.INFO, "  Average Probability: " + 
                     String.format("%.2f%%", cluster.getAverageProbability() * 100));
        }
    }
    
    /**
     * Display strategic recommendations
     */
    private void displayStrategicRecommendations() {
        Trace.out(Trace.Level.INFO, "\n=== STRATEGIC RECOMMENDATIONS ===");
        
        Map<String, ScenarioGenerator.ScenarioCluster> clusters = scenarioGenerator.getAllClusters();
        ScenarioGenerator.ScenarioCluster optimisticCluster = clusters.get("cluster1");
        ScenarioGenerator.ScenarioCluster challengingCluster = clusters.get("cluster2");
        
        if (optimisticCluster != null && optimisticCluster.getAverageProbability() > 0.4) {
            Trace.out(Trace.Level.INFO, "RECOMMENDATION: Focus on growth strategies - optimistic scenarios are likely");
        } else if (challengingCluster != null && challengingCluster.getAverageProbability() > 0.4) {
            Trace.out(Trace.Level.INFO, "RECOMMENDATION: Prepare adaptation strategies - challenging scenarios are likely");
        } else {
            Trace.out(Trace.Level.INFO, "RECOMMENDATION: Maintain flexible strategies - mixed scenario outcomes expected");
        }
        
        double stability = globalState.calculateStabilityIndex();
        if (stability < 0.5) {
            Trace.out(Trace.Level.INFO, "RECOMMENDATION: Monitor system stability - current stability index is low");
        }
    }
    
    /**
     * Get the current global state
     * @return Current global state
     */
    public GlobalState getGlobalState() {
        return globalState;
    }
    
    /**
     * Get all agents
     * @return List of all agents
     */
    public List<Agent> getAgents() {
        return new ArrayList<>(agents);
    }
    
    /**
     * Get the scenario generator
     * @return Scenario generator
     */
    public ScenarioGenerator getScenarioGenerator() {
        return scenarioGenerator;
    }
}
