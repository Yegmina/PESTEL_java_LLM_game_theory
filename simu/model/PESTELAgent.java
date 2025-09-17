package simu.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for PESTEL-based agents that make decisions and affect PESTEL factors
 */
public abstract class PESTELAgent {
    protected String agentId;
    protected AgentType agentType;
    protected PESTELState localPESTEL;
    protected double lastDecisionTime;
    protected int decisionCount;
    protected List<String> recentActions;
    
    public enum AgentType {
        COMPANY, COUNTRY, RESEARCHER
    }
    
    public PESTELAgent(String agentId, AgentType agentType) {
        this.agentId = agentId;
        this.agentType = agentType;
        this.localPESTEL = new PESTELState();
        this.lastDecisionTime = 0.0;
        this.decisionCount = 0;
        this.recentActions = new ArrayList<>();
        
        initializeLocalPESTEL();
    }
    
    /**
     * Initialize agent-specific PESTEL state
     */
    protected abstract void initializeLocalPESTEL();
    
    /**
     * Make a decision based on current global and local PESTEL state
     * @param globalPESTEL Current global PESTEL state
     * @param currentDay Current simulation day
     * @param recentAgentActions Actions taken by other agents recently
     * @return Decision made by the agent, or null if no action
     */
    public abstract AgentDecision makeDecision(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions);
    
    /**
     * Update local PESTEL state based on external changes
     * @param changes List of PESTEL changes that might affect this agent
     */
    public abstract void updateFromPESTELChanges(List<PESTELChange> changes);
    
    /**
     * Get agent type-specific description for AI prompts
     */
    public abstract String getAgentDescription();
    
    /**
     * Get current context for AI decision making
     */
    public String getCurrentContext(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions) {
        StringBuilder context = new StringBuilder();
        context.append("Agent: ").append(agentId).append(" (").append(agentType).append(")\n");
        context.append("Current Day: ").append(currentDay).append("\n");
        context.append("Decisions Made: ").append(decisionCount).append("\n");
        context.append("Last Decision: Day ").append((int)lastDecisionTime).append("\n\n");
        
        context.append("=== GLOBAL PESTEL STATE ===\n");
        context.append(globalPESTEL.toString()).append("\n\n");
        
        context.append("=== LOCAL PESTEL STATE ===\n");
        context.append(localPESTEL.toString()).append("\n\n");
        
        if (!recentAgentActions.isEmpty()) {
            context.append("=== RECENT AGENT ACTIONS ===\n");
            for (AgentAction action : recentAgentActions) {
                context.append("Day ").append(action.getDay()).append(": ")
                       .append(action.getAgentId()).append(" - ")
                       .append(action.getActionDescription()).append("\n");
            }
            context.append("\n");
        }
        
        if (!recentActions.isEmpty()) {
            context.append("=== MY RECENT ACTIONS ===\n");
            for (String action : recentActions) {
                context.append("- ").append(action).append("\n");
            }
        }
        
        return context.toString();
    }
    
    /**
     * Record an action taken by this agent
     */
    public void recordAction(String action) {
        recentActions.add(action);
        // Keep only last 5 actions
        if (recentActions.size() > 5) {
            recentActions.remove(0);
        }
        decisionCount++;
        lastDecisionTime = simu.framework.Clock.getInstance().getClock();
    }
    
    // Getters
    public String getAgentId() {
        return agentId;
    }
    
    public AgentType getAgentType() {
        return agentType;
    }
    
    public PESTELState getLocalPESTEL() {
        return localPESTEL;
    }
    
    public double getLastDecisionTime() {
        return lastDecisionTime;
    }
    
    public int getDecisionCount() {
        return decisionCount;
    }
    
    public List<String> getRecentActions() {
        return new ArrayList<>(recentActions);
    }
    
    @Override
    public String toString() {
        return String.format("Agent %s (%s) - Decisions: %d, Last: Day %.0f", 
                           agentId, agentType, decisionCount, lastDecisionTime);
    }
}
