package simu.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the result of AI prediction analysis performed by the Gemini AI service.
 * Contains structured predictions about future scenarios and their likelihoods.
 */
public class AIPredictionResult {
    private String summary;
    private String scenarioTransitions;
    private String keyFactors;
    private String timelinePredictions;
    private List<ScenarioGenerator.Scenario> scenarios;
    private Map<String, Double> predictionConfidences;
    private long timestamp;
    
    /**
     * Create a new AI prediction result
     * @param summary Most likely scenario outcomes
     * @param scenarioTransitions Scenario transition probabilities
     * @param keyFactors Key factors driving scenario evolution
     * @param timelinePredictions Timeline predictions for major changes
     * @param scenarios The scenarios that were analyzed
     */
    public AIPredictionResult(String summary, String scenarioTransitions, String keyFactors, 
                             String timelinePredictions, List<ScenarioGenerator.Scenario> scenarios) {
        this.summary = summary;
        this.scenarioTransitions = scenarioTransitions;
        this.keyFactors = keyFactors;
        this.timelinePredictions = timelinePredictions;
        this.scenarios = scenarios;
        this.predictionConfidences = new HashMap<>();
        this.timestamp = System.currentTimeMillis();
        
        // Initialize default confidence scores
        initializeConfidenceScores();
    }
    
    /**
     * Initialize default confidence scores for different prediction aspects
     */
    private void initializeConfidenceScores() {
        predictionConfidences.put("scenario_outcomes", 0.7);
        predictionConfidences.put("transition_probabilities", 0.65);
        predictionConfidences.put("factor_analysis", 0.75);
        predictionConfidences.put("timeline_accuracy", 0.6);
    }
    
    /**
     * Get the prediction summary
     * @return Prediction summary
     */
    public String getSummary() {
        return summary;
    }
    
    /**
     * Set the prediction summary
     * @param summary New summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    /**
     * Get scenario transition information
     * @return Scenario transitions
     */
    public String getScenarioTransitions() {
        return scenarioTransitions;
    }
    
    /**
     * Set scenario transitions
     * @param scenarioTransitions New scenario transitions
     */
    public void setScenarioTransitions(String scenarioTransitions) {
        this.scenarioTransitions = scenarioTransitions;
    }
    
    /**
     * Get key factors analysis
     * @return Key factors
     */
    public String getKeyFactors() {
        return keyFactors;
    }
    
    /**
     * Set key factors
     * @param keyFactors New key factors
     */
    public void setKeyFactors(String keyFactors) {
        this.keyFactors = keyFactors;
    }
    
    /**
     * Get timeline predictions
     * @return Timeline predictions
     */
    public String getTimelinePredictions() {
        return timelinePredictions;
    }
    
    /**
     * Set timeline predictions
     * @param timelinePredictions New timeline predictions
     */
    public void setTimelinePredictions(String timelinePredictions) {
        this.timelinePredictions = timelinePredictions;
    }
    
    /**
     * Get the scenarios that were analyzed
     * @return List of scenarios
     */
    public List<ScenarioGenerator.Scenario> getScenarios() {
        return scenarios;
    }
    
    /**
     * Get confidence score for a specific prediction aspect
     * @param aspect The aspect to get confidence for
     * @return Confidence score (0-1)
     */
    public double getPredictionConfidence(String aspect) {
        return predictionConfidences.getOrDefault(aspect, 0.5);
    }
    
    /**
     * Set confidence score for a specific prediction aspect
     * @param aspect The aspect
     * @param confidence Confidence score (0-1)
     */
    public void setPredictionConfidence(String aspect, double confidence) {
        predictionConfidences.put(aspect, Math.max(0.0, Math.min(1.0, confidence)));
    }
    
    /**
     * Get all prediction confidence scores
     * @return Map of confidence scores
     */
    public Map<String, Double> getAllPredictionConfidences() {
        return new HashMap<>(predictionConfidences);
    }
    
    /**
     * Get the timestamp when this prediction was made
     * @return Timestamp in milliseconds
     */
    public long getTimestamp() {
        return timestamp;
    }
    
    /**
     * Calculate overall confidence in the predictions
     * @return Overall confidence score
     */
    public double getOverallConfidence() {
        return predictionConfidences.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.5);
    }
    
    /**
     * Get the most likely scenarios based on AI analysis
     * @return String description of most likely scenarios
     */
    public String getMostLikelyScenarios() {
        if (scenarios == null || scenarios.isEmpty()) {
            return "No scenarios available for analysis";
        }
        
        // Find scenarios with highest probabilities
        ScenarioGenerator.Scenario mostLikely = scenarios.stream()
                .max((s1, s2) -> Double.compare(s1.getProbability(), s2.getProbability()))
                .orElse(null);
        
        if (mostLikely != null) {
            return String.format("Most likely: %s (%.2f%% probability)", 
                mostLikely.getScenarioName(), mostLikely.getProbability() * 100);
        }
        
        return "Unable to determine most likely scenarios";
    }
    
    /**
     * Check if predictions indicate significant changes
     * @return True if significant changes are predicted
     */
    public boolean indicatesSignificantChanges() {
        return timelinePredictions.toLowerCase().contains("significant") || 
               timelinePredictions.toLowerCase().contains("major") ||
               timelinePredictions.toLowerCase().contains("dramatic") ||
               keyFactors.toLowerCase().contains("critical");
    }
    
    /**
     * Check if predictions indicate stability
     * @return True if stability is predicted
     */
    public boolean indicatesStability() {
        return summary.toLowerCase().contains("stable") || 
               summary.toLowerCase().contains("gradual") ||
               summary.toLowerCase().contains("steady");
    }
    
    /**
     * Get a reliability rating for these predictions
     * @return Reliability rating (1-5, 5 being most reliable)
     */
    public int getReliabilityRating() {
        int rating = 3; // Default medium reliability
        
        double overallConfidence = getOverallConfidence();
        if (overallConfidence > 0.8) {
            rating = 5;
        } else if (overallConfidence > 0.7) {
            rating = 4;
        } else if (overallConfidence < 0.5) {
            rating = 2;
        }
        
        // Adjust based on scenario diversity
        if (scenarios != null && scenarios.size() > 3) {
            rating = Math.min(5, rating + 1);
        }
        
        return rating;
    }
    
    /**
     * Get prediction horizon (short, medium, long term)
     * @return Prediction horizon
     */
    public String getPredictionHorizon() {
        if (timelinePredictions.toLowerCase().contains("immediate") || 
            timelinePredictions.toLowerCase().contains("short-term")) {
            return "Short-term (0-6 months)";
        } else if (timelinePredictions.toLowerCase().contains("medium") || 
                  timelinePredictions.toLowerCase().contains("1-3 years")) {
            return "Medium-term (1-3 years)";
        } else if (timelinePredictions.toLowerCase().contains("long-term") || 
                  timelinePredictions.toLowerCase().contains("3-5 years")) {
            return "Long-term (3-5 years)";
        }
        
        return "Mixed horizon";
    }
    
    /**
     * Get a summary of the prediction result
     * @return String summary
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AI Prediction Result:\n");
        sb.append("  Summary: ").append(summary.length() > 100 ? 
            summary.substring(0, 100) + "..." : summary).append("\n");
        sb.append("  Reliability Rating: ").append(getReliabilityRating()).append("/5\n");
        sb.append("  Overall Confidence: ").append(String.format("%.2f", getOverallConfidence())).append("\n");
        sb.append("  Prediction Horizon: ").append(getPredictionHorizon()).append("\n");
        sb.append("  Significant Changes: ").append(indicatesSignificantChanges() ? "Yes" : "No").append("\n");
        sb.append("  Stability Indicated: ").append(indicatesStability() ? "Yes" : "No").append("\n");
        sb.append("  Timestamp: ").append(timestamp).append("\n");
        return sb.toString();
    }
}
