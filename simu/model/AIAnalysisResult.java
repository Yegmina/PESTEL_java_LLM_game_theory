package simu.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the result of AI analysis performed by the Gemini AI service.
 * Contains structured insights about the global state and system conditions.
 */
public class AIAnalysisResult {
    private String summary;
    private String keyRisks;
    private String keyOpportunities;
    private String trendAnalysis;
    private GlobalState globalState;
    private Map<String, Double> confidenceScores;
    private long timestamp;
    
    /**
     * Create a new AI analysis result
     * @param summary Overall system health assessment
     * @param keyRisks Key risk factors identified
     * @param keyOpportunities Key opportunities identified
     * @param trendAnalysis Trend analysis and predictions
     * @param globalState The global state that was analyzed
     */
    public AIAnalysisResult(String summary, String keyRisks, String keyOpportunities, 
                           String trendAnalysis, GlobalState globalState) {
        this.summary = summary;
        this.keyRisks = keyRisks;
        this.keyOpportunities = keyOpportunities;
        this.trendAnalysis = trendAnalysis;
        this.globalState = globalState;
        this.confidenceScores = new HashMap<>();
        this.timestamp = System.currentTimeMillis();
        
        // Initialize default confidence scores
        initializeConfidenceScores();
    }
    
    /**
     * Initialize default confidence scores for different aspects
     */
    private void initializeConfidenceScores() {
        confidenceScores.put("overall_analysis", 0.8);
        confidenceScores.put("risk_assessment", 0.75);
        confidenceScores.put("opportunity_identification", 0.7);
        confidenceScores.put("trend_prediction", 0.65);
    }
    
    /**
     * Get the overall summary
     * @return Analysis summary
     */
    public String getSummary() {
        return summary;
    }
    
    /**
     * Set the overall summary
     * @param summary New summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    /**
     * Get key risks identified
     * @return Key risks
     */
    public String getKeyRisks() {
        return keyRisks;
    }
    
    /**
     * Set key risks
     * @param keyRisks New key risks
     */
    public void setKeyRisks(String keyRisks) {
        this.keyRisks = keyRisks;
    }
    
    /**
     * Get key opportunities identified
     * @return Key opportunities
     */
    public String getKeyOpportunities() {
        return keyOpportunities;
    }
    
    /**
     * Set key opportunities
     * @param keyOpportunities New key opportunities
     */
    public void setKeyOpportunities(String keyOpportunities) {
        this.keyOpportunities = keyOpportunities;
    }
    
    /**
     * Get trend analysis
     * @return Trend analysis
     */
    public String getTrendAnalysis() {
        return trendAnalysis;
    }
    
    /**
     * Set trend analysis
     * @param trendAnalysis New trend analysis
     */
    public void setTrendAnalysis(String trendAnalysis) {
        this.trendAnalysis = trendAnalysis;
    }
    
    /**
     * Get the global state that was analyzed
     * @return Global state
     */
    public GlobalState getGlobalState() {
        return globalState;
    }
    
    /**
     * Get confidence score for a specific aspect
     * @param aspect The aspect to get confidence for
     * @return Confidence score (0-1)
     */
    public double getConfidenceScore(String aspect) {
        return confidenceScores.getOrDefault(aspect, 0.5);
    }
    
    /**
     * Set confidence score for a specific aspect
     * @param aspect The aspect
     * @param confidence Confidence score (0-1)
     */
    public void setConfidenceScore(String aspect, double confidence) {
        confidenceScores.put(aspect, Math.max(0.0, Math.min(1.0, confidence)));
    }
    
    /**
     * Get all confidence scores
     * @return Map of confidence scores
     */
    public Map<String, Double> getAllConfidenceScores() {
        return new HashMap<>(confidenceScores);
    }
    
    /**
     * Get the timestamp when this analysis was performed
     * @return Timestamp in milliseconds
     */
    public long getTimestamp() {
        return timestamp;
    }
    
    /**
     * Calculate overall confidence in the analysis
     * @return Overall confidence score
     */
    public double getOverallConfidence() {
        return confidenceScores.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.5);
    }
    
    /**
     * Check if the analysis indicates high risk
     * @return True if high risk is indicated
     */
    public boolean indicatesHighRisk() {
        return keyRisks.toLowerCase().contains("high risk") || 
               keyRisks.toLowerCase().contains("critical") ||
               keyRisks.toLowerCase().contains("urgent");
    }
    
    /**
     * Check if the analysis indicates high opportunity
     * @return True if high opportunity is indicated
     */
    public boolean indicatesHighOpportunity() {
        return keyOpportunities.toLowerCase().contains("significant") || 
               keyOpportunities.toLowerCase().contains("major") ||
               keyOpportunities.toLowerCase().contains("breakthrough");
    }
    
    /**
     * Get a priority level for this analysis
     * @return Priority level (1-5, 5 being highest)
     */
    public int getPriorityLevel() {
        int priority = 3; // Default medium priority
        
        if (indicatesHighRisk()) {
            priority = Math.max(priority, 4);
        }
        if (indicatesHighOpportunity()) {
            priority = Math.max(priority, 4);
        }
        if (getOverallConfidence() < 0.6) {
            priority = Math.max(priority, 4); // Low confidence increases priority for review
        }
        
        return priority;
    }
    
    /**
     * Get a summary of the analysis result
     * @return String summary
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AI Analysis Result:\n");
        sb.append("  Summary: ").append(summary.length() > 100 ? 
            summary.substring(0, 100) + "..." : summary).append("\n");
        sb.append("  Priority Level: ").append(getPriorityLevel()).append("\n");
        sb.append("  Overall Confidence: ").append(String.format("%.2f", getOverallConfidence())).append("\n");
        sb.append("  High Risk: ").append(indicatesHighRisk() ? "Yes" : "No").append("\n");
        sb.append("  High Opportunity: ").append(indicatesHighOpportunity() ? "Yes" : "No").append("\n");
        sb.append("  Timestamp: ").append(timestamp).append("\n");
        return sb.toString();
    }
}
