package simu.model;

/**
 * Represents a change to a PESTEL factor
 */
public class PESTELChange {
    private String category; // P, E, S, T, E, L
    private String factor;   // specific factor within category
    private String oldValue;
    private String newValue;
    private String reason;   // why the change occurred
    private String sourceAgentId; // which agent caused this change
    private int day;
    
    public PESTELChange(String category, String factor, String oldValue, String newValue, 
                       String reason, String sourceAgentId, int day) {
        this.category = category;
        this.factor = factor;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.reason = reason;
        this.sourceAgentId = sourceAgentId;
        this.day = day;
    }
    
    public String getCategory() {
        return category;
    }
    
    public String getFactor() {
        return factor;
    }
    
    public String getOldValue() {
        return oldValue;
    }
    
    public String getNewValue() {
        return newValue;
    }
    
    public String getReason() {
        return reason;
    }
    
    public String getSourceAgentId() {
        return sourceAgentId;
    }
    
    public int getDay() {
        return day;
    }
    
    @Override
    public String toString() {
        return String.format("Day %d: %s changed %s.%s from '%s' to '%s' (Reason: %s)", 
                           day, sourceAgentId, category, factor, oldValue, newValue, reason);
    }
}
