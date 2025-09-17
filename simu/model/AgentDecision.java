package simu.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a decision made by an agent in the forecasting system.
 * Contains information about the decision type, parameters, and expected impact.
 */
public class AgentDecision {
    private String agentId;
    private int day;
    private String description;
    private String decisionType;
    private double confidence;
    private Map<String, Double> parameters;
    private Map<String, String> metadata;
    private double expectedImpact;
    private double decisionTime;
    
    /**
     * Create a new agent decision
     * @param agentId ID of the agent making the decision
     * @param day Day when the decision was made
     * @param description Description of the decision
     * @param decisionType Type of decision made
     * @param confidence Confidence level (0-1)
     */
    public AgentDecision(String agentId, int day, String description, String decisionType, double confidence) {
        this.agentId = agentId;
        this.day = day;
        this.description = description;
        this.decisionType = decisionType;
        this.confidence = confidence;
        this.parameters = new HashMap<>();
        this.metadata = new HashMap<>();
        this.expectedImpact = 0.0; // Default impact
        this.decisionTime = simu.framework.Clock.getInstance().getClock();
    }
    
    /**
     * Legacy constructor for compatibility
     */
    public AgentDecision(String decisionType, String agentId, double decisionTime) {
        this.decisionType = decisionType;
        this.agentId = agentId;
        this.decisionTime = decisionTime;
        this.day = (int) decisionTime;
        this.description = decisionType;
        this.confidence = 0.5;
        this.parameters = new HashMap<>();
        this.metadata = new HashMap<>();
        this.expectedImpact = 0.0;
    }
    
    /**
     * Get the decision description
     * @return Decision description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Get the decision type
     * @return Decision type
     */
    public String getDecisionType() {
        return decisionType;
    }
    
    /**
     * Get the day when decision was made
     * @return Day
     */
    public int getDay() {
        return day;
    }
    
    /**
     * Set the decision type
     * @param decisionType New decision type
     */
    public void setDecisionType(String decisionType) {
        this.decisionType = decisionType;
    }
    
    /**
     * Get a parameter value
     * @param key Parameter name
     * @return Parameter value, or 0.0 if not found
     */
    public double getParameter(String key) {
        return parameters.getOrDefault(key, 0.0);
    }
    
    /**
     * Set a parameter value
     * @param key Parameter name
     * @param value Parameter value
     */
    public void setParameter(String key, double value) {
        parameters.put(key, value);
    }
    
    /**
     * Get metadata value
     * @param key Metadata key
     * @return Metadata value, or "unknown" if not found
     */
    public String getMetadata(String key) {
        return metadata.getOrDefault(key, "unknown");
    }
    
    /**
     * Set metadata value
     * @param key Metadata key
     * @param value Metadata value
     */
    public void setMetadata(String key, String value) {
        metadata.put(key, value);
    }
    
    /**
     * Get decision confidence level
     * @return Confidence between 0 and 1
     */
    public double getConfidence() {
        return confidence;
    }
    
    /**
     * Set decision confidence level
     * @param confidence Confidence between 0 and 1
     */
    public void setConfidence(double confidence) {
        this.confidence = Math.max(0.0, Math.min(1.0, confidence));
    }
    
    /**
     * Get expected impact of the decision
     * @return Expected impact value
     */
    public double getExpectedImpact() {
        return expectedImpact;
    }
    
    /**
     * Set expected impact of the decision
     * @param expectedImpact Expected impact value
     */
    public void setExpectedImpact(double expectedImpact) {
        this.expectedImpact = expectedImpact;
    }
    
    /**
     * Get the time when the decision was made
     * @return Decision time
     */
    public double getDecisionTime() {
        return decisionTime;
    }
    
    /**
     * Get the ID of the agent that made the decision
     * @return Agent ID
     */
    public String getAgentId() {
        return agentId;
    }
    
    /**
     * Get all parameters as a map
     * @return Copy of parameters map
     */
    public Map<String, Double> getAllParameters() {
        return new HashMap<>(parameters);
    }
    
    /**
     * Get all metadata as a map
     * @return Copy of metadata map
     */
    public Map<String, String> getAllMetadata() {
        return new HashMap<>(metadata);
    }
    
    /**
     * Calculate the overall decision strength
     * @return Decision strength based on confidence and impact
     */
    public double calculateDecisionStrength() {
        return confidence * Math.abs(expectedImpact);
    }
    
    /**
     * Check if this decision conflicts with another decision
     * @param other The other decision to compare with
     * @return True if there's a conflict
     */
    public boolean conflictsWith(AgentDecision other) {
        // Simple conflict detection based on decision types
        if (decisionType.equals(other.decisionType)) {
            // Same type decisions might conflict if they have opposite impacts
            return Math.signum(expectedImpact) != Math.signum(other.expectedImpact);
        }
        
        // Check for specific conflicting decision types
        return (decisionType.equals("invest") && other.decisionType.equals("divest")) ||
               (decisionType.equals("expand") && other.decisionType.equals("contract")) ||
               (decisionType.equals("collaborate") && other.decisionType.equals("compete"));
    }
    
    /**
     * Get a summary of the decision
     * @return String representation of the decision
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Decision: ").append(decisionType).append("\n");
        sb.append("  Agent: ").append(agentId).append("\n");
        sb.append("  Time: ").append(String.format("%.2f", decisionTime)).append("\n");
        sb.append("  Confidence: ").append(String.format("%.2f", confidence)).append("\n");
        sb.append("  Expected Impact: ").append(String.format("%.2f", expectedImpact)).append("\n");
        sb.append("  Strength: ").append(String.format("%.2f", calculateDecisionStrength())).append("\n");
        return sb.toString();
    }
}
