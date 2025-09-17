package simu.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Abstract base class for all agents in the forecasting system.
 * Agents represent different stakeholder groups (Companies, Countries, Researchers)
 * with their own internal states, decision-making capabilities, and interaction protocols.
 */
public abstract class Agent {
    protected String agentId;
    protected AgentType agentType;
    protected Map<String, Double> internalFactors;
    protected Map<String, String> localData;
    protected double lastDecisionTime;
    protected int decisionCount;
    protected Random random;
    protected GlobalState globalState;
    
    /**
     * Types of agents in the system
     */
    public enum AgentType {
        COMPANY, COUNTRY, RESEARCHER
    }
    
    /**
     * Initialize a new agent
     * @param agentId Unique identifier for the agent
     * @param agentType Type of the agent
     * @param globalState Reference to the global state
     */
    public Agent(String agentId, AgentType agentType, GlobalState globalState) {
        this.agentId = agentId;
        this.agentType = agentType;
        this.globalState = globalState;
        this.internalFactors = new HashMap<>();
        this.localData = new HashMap<>();
        this.lastDecisionTime = 0.0;
        this.decisionCount = 0;
        this.random = new Random();
        
        initializeAgentState();
    }
    
    /**
     * Initialize agent-specific state and factors
     */
    protected abstract void initializeAgentState();
    
    /**
     * Make a decision based on current internal state and global state
     * @param currentTime Current simulation time
     * @return Decision made by the agent
     */
    public abstract AgentDecision makeDecision(double currentTime);
    
    /**
     * Update internal factors based on global state changes
     * @param globalState The current global state
     */
    public abstract void updateFromGlobalState(GlobalState globalState);
    
    /**
     * Apply influence to the global state based on agent actions
     * @param globalState The global state to influence
     * @param decision The decision made by the agent
     */
    public abstract void influenceGlobalState(GlobalState globalState, AgentDecision decision);
    
    /**
     * Collaborate with another agent
     * @param otherAgent The agent to collaborate with
     * @param currentTime Current simulation time
     */
    public abstract void collaborate(Agent otherAgent, double currentTime);
    
    /**
     * Get the agent's unique identifier
     * @return Agent ID
     */
    public String getAgentId() {
        return agentId;
    }
    
    /**
     * Get the agent's type
     * @return Agent type
     */
    public AgentType getAgentType() {
        return agentType;
    }
    
    /**
     * Get an internal factor value
     * @param key The factor name
     * @return The factor value, or 0.0 if not found
     */
    public double getInternalFactor(String key) {
        return internalFactors.getOrDefault(key, 0.0);
    }
    
    /**
     * Set an internal factor value
     * @param key The factor name
     * @param value The factor value
     */
    public void setInternalFactor(String key, double value) {
        internalFactors.put(key, value);
    }
    
    /**
     * Get local data value
     * @param key The data name
     * @return The data value, or "unknown" if not found
     */
    public String getLocalData(String key) {
        return localData.getOrDefault(key, "unknown");
    }
    
    /**
     * Set local data value
     * @param key The data name
     * @param value The data value
     */
    public void setLocalData(String key, String value) {
        localData.put(key, value);
    }
    
    /**
     * Get the time of the last decision
     * @return Last decision time
     */
    public double getLastDecisionTime() {
        return lastDecisionTime;
    }
    
    /**
     * Get the number of decisions made
     * @return Decision count
     */
    public int getDecisionCount() {
        return decisionCount;
    }
    
    /**
     * Calculate agent's influence strength based on internal factors
     * @return Influence strength between 0 and 1
     */
    public double calculateInfluenceStrength() {
        // Base influence calculation - can be overridden by subclasses
        double baseInfluence = 0.5;
        
        // Adjust based on internal factors
        for (double factor : internalFactors.values()) {
            baseInfluence += factor * 0.1;
        }
        
        return Math.max(0.0, Math.min(1.0, baseInfluence));
    }
    
    /**
     * Get a summary of the agent's current state
     * @return String representation of the agent state
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Agent ").append(agentId).append(" (").append(agentType).append(")\n");
        sb.append("  Influence Strength: ").append(String.format("%.2f", calculateInfluenceStrength())).append("\n");
        sb.append("  Decisions Made: ").append(decisionCount).append("\n");
        sb.append("  Last Decision: ").append(String.format("%.2f", lastDecisionTime)).append("\n");
        return sb.toString();
    }
}
