package simu.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the result of AI recommendation generation performed by the Gemini AI service.
 * Contains structured strategic recommendations for Metropolia University based on
 * analysis and prediction results.
 */
public class AIRecommendationResult {
    private String immediateActions;
    private String mediumTermStrategies;
    private String longTermVision;
    private String riskMitigation;
    private String opportunityCapitalization;
    private Map<String, Integer> priorityScores;
    private Map<String, Double> implementationConfidence;
    private long timestamp;
    
    /**
     * Create a new AI recommendation result
     * @param immediateActions Immediate action items (next 6 months)
     * @param mediumTermStrategies Medium-term strategies (1-3 years)
     * @param longTermVision Long-term vision adjustments (3-5 years)
     * @param riskMitigation Risk mitigation strategies
     * @param opportunityCapitalization Opportunity capitalization plans
     */
    public AIRecommendationResult(String immediateActions, String mediumTermStrategies, 
                                 String longTermVision, String riskMitigation, 
                                 String opportunityCapitalization) {
        this.immediateActions = immediateActions;
        this.mediumTermStrategies = mediumTermStrategies;
        this.longTermVision = longTermVision;
        this.riskMitigation = riskMitigation;
        this.opportunityCapitalization = opportunityCapitalization;
        this.priorityScores = new HashMap<>();
        this.implementationConfidence = new HashMap<>();
        this.timestamp = System.currentTimeMillis();
        
        // Initialize default priority scores and confidence levels
        initializeDefaults();
    }
    
    /**
     * Initialize default priority scores and confidence levels
     */
    private void initializeDefaults() {
        // Priority scores (1-5, 5 being highest priority)
        priorityScores.put("immediate_actions", 5);
        priorityScores.put("risk_mitigation", 4);
        priorityScores.put("medium_term_strategies", 3);
        priorityScores.put("opportunity_capitalization", 3);
        priorityScores.put("long_term_vision", 2);
        
        // Implementation confidence (0-1)
        implementationConfidence.put("immediate_actions", 0.8);
        implementationConfidence.put("risk_mitigation", 0.75);
        implementationConfidence.put("medium_term_strategies", 0.7);
        implementationConfidence.put("opportunity_capitalization", 0.65);
        implementationConfidence.put("long_term_vision", 0.6);
    }
    
    /**
     * Get immediate action items
     * @return Immediate actions
     */
    public String getImmediateActions() {
        return immediateActions;
    }
    
    /**
     * Set immediate action items
     * @param immediateActions New immediate actions
     */
    public void setImmediateActions(String immediateActions) {
        this.immediateActions = immediateActions;
    }
    
    /**
     * Get medium-term strategies
     * @return Medium-term strategies
     */
    public String getMediumTermStrategies() {
        return mediumTermStrategies;
    }
    
    /**
     * Set medium-term strategies
     * @param mediumTermStrategies New medium-term strategies
     */
    public void setMediumTermStrategies(String mediumTermStrategies) {
        this.mediumTermStrategies = mediumTermStrategies;
    }
    
    /**
     * Get long-term vision adjustments
     * @return Long-term vision
     */
    public String getLongTermVision() {
        return longTermVision;
    }
    
    /**
     * Set long-term vision adjustments
     * @param longTermVision New long-term vision
     */
    public void setLongTermVision(String longTermVision) {
        this.longTermVision = longTermVision;
    }
    
    /**
     * Get risk mitigation strategies
     * @return Risk mitigation strategies
     */
    public String getRiskMitigation() {
        return riskMitigation;
    }
    
    /**
     * Set risk mitigation strategies
     * @param riskMitigation New risk mitigation strategies
     */
    public void setRiskMitigation(String riskMitigation) {
        this.riskMitigation = riskMitigation;
    }
    
    /**
     * Get opportunity capitalization plans
     * @return Opportunity capitalization plans
     */
    public String getOpportunityCapitalization() {
        return opportunityCapitalization;
    }
    
    /**
     * Set opportunity capitalization plans
     * @param opportunityCapitalization New opportunity capitalization plans
     */
    public void setOpportunityCapitalization(String opportunityCapitalization) {
        this.opportunityCapitalization = opportunityCapitalization;
    }
    
    /**
     * Get priority score for a specific recommendation category
     * @param category The category
     * @return Priority score (1-5)
     */
    public int getPriorityScore(String category) {
        return priorityScores.getOrDefault(category, 3);
    }
    
    /**
     * Set priority score for a specific recommendation category
     * @param category The category
     * @param priority Priority score (1-5)
     */
    public void setPriorityScore(String category, int priority) {
        priorityScores.put(category, Math.max(1, Math.min(5, priority)));
    }
    
    /**
     * Get implementation confidence for a specific category
     * @param category The category
     * @return Implementation confidence (0-1)
     */
    public double getImplementationConfidence(String category) {
        return implementationConfidence.getOrDefault(category, 0.5);
    }
    
    /**
     * Set implementation confidence for a specific category
     * @param category The category
     * @param confidence Implementation confidence (0-1)
     */
    public void setImplementationConfidence(String category, double confidence) {
        implementationConfidence.put(category, Math.max(0.0, Math.min(1.0, confidence)));
    }
    
    /**
     * Get all priority scores
     * @return Map of priority scores
     */
    public Map<String, Integer> getAllPriorityScores() {
        return new HashMap<>(priorityScores);
    }
    
    /**
     * Get all implementation confidence scores
     * @return Map of confidence scores
     */
    public Map<String, Double> getAllImplementationConfidences() {
        return new HashMap<>(implementationConfidence);
    }
    
    /**
     * Get the timestamp when these recommendations were generated
     * @return Timestamp in milliseconds
     */
    public long getTimestamp() {
        return timestamp;
    }
    
    /**
     * Calculate overall recommendation quality score
     * @return Quality score (0-1)
     */
    public double getOverallQualityScore() {
        double avgConfidence = implementationConfidence.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.5);
        
        double avgPriority = priorityScores.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(3.0) / 5.0;
        
        return (avgConfidence + avgPriority) / 2.0;
    }
    
    /**
     * Get the highest priority recommendation category
     * @return Category with highest priority
     */
    public String getHighestPriorityCategory() {
        return priorityScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("immediate_actions");
    }
    
    /**
     * Check if recommendations include urgent actions
     * @return True if urgent actions are recommended
     */
    public boolean includesUrgentActions() {
        return immediateActions.toLowerCase().contains("urgent") || 
               immediateActions.toLowerCase().contains("immediate") ||
               immediateActions.toLowerCase().contains("critical") ||
               riskMitigation.toLowerCase().contains("urgent");
    }
    
    /**
     * Check if recommendations include innovation opportunities
     * @return True if innovation opportunities are identified
     */
    public boolean includesInnovationOpportunities() {
        return opportunityCapitalization.toLowerCase().contains("innovation") || 
               opportunityCapitalization.toLowerCase().contains("technology") ||
               opportunityCapitalization.toLowerCase().contains("digital") ||
               mediumTermStrategies.toLowerCase().contains("innovation");
    }
    
    /**
     * Get the recommended implementation timeline
     * @return Implementation timeline description
     */
    public String getImplementationTimeline() {
        StringBuilder timeline = new StringBuilder();
        
        if (includesUrgentActions()) {
            timeline.append("Immediate (0-3 months): Critical actions required\n");
        }
        
        timeline.append("Short-term (3-6 months): ").append(immediateActions.length() > 50 ? 
            immediateActions.substring(0, 50) + "..." : immediateActions).append("\n");
        
        timeline.append("Medium-term (1-3 years): ").append(mediumTermStrategies.length() > 50 ? 
            mediumTermStrategies.substring(0, 50) + "..." : mediumTermStrategies).append("\n");
        
        timeline.append("Long-term (3-5 years): ").append(longTermVision.length() > 50 ? 
            longTermVision.substring(0, 50) + "..." : longTermVision);
        
        return timeline.toString();
    }
    
    /**
     * Get a summary of the recommendation result
     * @return String summary
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AI Recommendation Result:\n");
        sb.append("  Overall Quality Score: ").append(String.format("%.2f", getOverallQualityScore())).append("\n");
        sb.append("  Highest Priority: ").append(getHighestPriorityCategory()).append("\n");
        sb.append("  Urgent Actions: ").append(includesUrgentActions() ? "Yes" : "No").append("\n");
        sb.append("  Innovation Opportunities: ").append(includesInnovationOpportunities() ? "Yes" : "No").append("\n");
        sb.append("  Implementation Timeline:\n");
        
        String[] timelineLines = getImplementationTimeline().split("\n");
        for (String line : timelineLines) {
            sb.append("    ").append(line).append("\n");
        }
        
        sb.append("  Timestamp: ").append(timestamp).append("\n");
        return sb.toString();
    }
}
