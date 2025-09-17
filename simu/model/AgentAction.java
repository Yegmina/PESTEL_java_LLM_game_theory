package simu.model;

/**
 * Represents an action taken by an agent on a specific day
 */
public class AgentAction {
    private String agentId;
    private int day;
    private String actionDescription;
    private String actionType;
    private double timestamp;
    
    public AgentAction(String agentId, int day, String actionDescription, String actionType) {
        this.agentId = agentId;
        this.day = day;
        this.actionDescription = actionDescription;
        this.actionType = actionType;
        this.timestamp = System.currentTimeMillis();
    }
    
    public String getAgentId() {
        return agentId;
    }
    
    public int getDay() {
        return day;
    }
    
    public String getActionDescription() {
        return actionDescription;
    }
    
    public String getActionType() {
        return actionType;
    }
    
    public double getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return String.format("Day %d: %s - %s (%s)", day, agentId, actionDescription, actionType);
    }
}
