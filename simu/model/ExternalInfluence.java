package simu.model;

import java.util.Random;

/**
 * External Influence represents stateless agents that affect the global state
 * without being influenced by other system components. These include climate events,
 * natural disasters, economic shocks, and other external factors.
 */
public class ExternalInfluence {
    private String influenceType;
    private double probability;
    private double magnitude;
    private double duration;
    private Random random;
    
    /**
     * Types of external influences
     */
    public enum InfluenceType {
        CLIMATE_CHANGE, METEORITE, EARTHQUAKE, VOLCANO, 
        POPULATION_CHANGE, SOLAR_WIND, ECONOMIC_SHOCK,
        TECHNOLOGICAL_BREAKTHROUGH, PANDEMIC, CONFLICT
    }
    
    /**
     * Initialize a new external influence
     * @param influenceType Type of external influence
     * @param probability Probability of occurrence (0-1)
     * @param magnitude Magnitude of impact (-1 to 1)
     * @param duration Duration of the influence
     */
    public ExternalInfluence(String influenceType, double probability, double magnitude, double duration) {
        this.influenceType = influenceType;
        this.probability = Math.max(0.0, Math.min(1.0, probability));
        this.magnitude = Math.max(-1.0, Math.min(1.0, magnitude));
        this.duration = Math.max(0.0, duration);
        this.random = new Random();
    }
    
    /**
     * Check if this external influence should occur at the current time
     * @param currentTime Current simulation time
     * @return True if the influence should occur
     */
    public boolean shouldOccur(double currentTime) {
        // Simple probability check - can be enhanced with time-based patterns
        return random.nextDouble() < probability;
    }
    
    /**
     * Apply the external influence to the global state
     * @param globalState The global state to influence
     * @param currentTime Current simulation time
     */
    public void applyInfluence(GlobalState globalState, double currentTime) {
        if (shouldOccur(currentTime)) {
            globalState.applyExternalInfluence(influenceType, magnitude);
            
            // Log the influence application
            simu.framework.Trace.out(simu.framework.Trace.Level.INFO, 
                "External influence applied: " + influenceType + 
                " with magnitude " + String.format("%.2f", magnitude) + 
                " at time " + String.format("%.2f", currentTime));
        }
    }
    
    /**
     * Get the influence type
     * @return Influence type
     */
    public String getInfluenceType() {
        return influenceType;
    }
    
    /**
     * Get the probability of occurrence
     * @return Probability (0-1)
     */
    public double getProbability() {
        return probability;
    }
    
    /**
     * Set the probability of occurrence
     * @param probability New probability (0-1)
     */
    public void setProbability(double probability) {
        this.probability = Math.max(0.0, Math.min(1.0, probability));
    }
    
    /**
     * Get the magnitude of impact
     * @return Magnitude (-1 to 1)
     */
    public double getMagnitude() {
        return magnitude;
    }
    
    /**
     * Set the magnitude of impact
     * @param magnitude New magnitude (-1 to 1)
     */
    public void setMagnitude(double magnitude) {
        this.magnitude = Math.max(-1.0, Math.min(1.0, magnitude));
    }
    
    /**
     * Get the duration of the influence
     * @return Duration
     */
    public double getDuration() {
        return duration;
    }
    
    /**
     * Set the duration of the influence
     * @param duration New duration
     */
    public void setDuration(double duration) {
        this.duration = Math.max(0.0, duration);
    }
    
    /**
     * Create a random external influence
     * @return Random external influence
     */
    public static ExternalInfluence createRandomInfluence() {
        Random random = new Random();
        InfluenceType[] types = InfluenceType.values();
        InfluenceType randomType = types[random.nextInt(types.length)];
        
        double probability = random.nextDouble() * 0.1; // 0-10% probability
        double magnitude = (random.nextDouble() - 0.5) * 2; // -1 to 1 magnitude
        double duration = random.nextDouble() * 10; // 0-10 time units duration
        
        return new ExternalInfluence(randomType.name().toLowerCase(), probability, magnitude, duration);
    }
    
    /**
     * Create a predefined set of common external influences
     * @return Array of common external influences
     */
    public static ExternalInfluence[] createCommonInfluences() {
        return new ExternalInfluence[] {
            new ExternalInfluence("climate_change", 0.05, 0.1, 12.0), // Gradual climate change
            new ExternalInfluence("economic_shock", 0.02, -0.3, 6.0), // Economic recession
            new ExternalInfluence("technological_breakthrough", 0.01, 0.4, 24.0), // Major tech breakthrough
            new ExternalInfluence("pandemic", 0.005, -0.2, 18.0), // Global pandemic
            new ExternalInfluence("conflict", 0.03, -0.15, 8.0), // Regional conflict
            new ExternalInfluence("natural_disaster", 0.01, -0.1, 3.0), // Natural disaster
            new ExternalInfluence("population_change", 0.02, 0.05, 36.0), // Demographic shift
            new ExternalInfluence("resource_discovery", 0.005, 0.2, 12.0) // New resource discovery
        };
    }
    
    /**
     * Calculate the expected impact of this influence
     * @return Expected impact value
     */
    public double calculateExpectedImpact() {
        return probability * magnitude * duration;
    }
    
    /**
     * Get a summary of the external influence
     * @return String representation of the influence
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("External Influence: ").append(influenceType).append("\n");
        sb.append("  Probability: ").append(String.format("%.2f%%", probability * 100)).append("\n");
        sb.append("  Magnitude: ").append(String.format("%.2f", magnitude)).append("\n");
        sb.append("  Duration: ").append(String.format("%.2f", duration)).append("\n");
        sb.append("  Expected Impact: ").append(String.format("%.2f", calculateExpectedImpact())).append("\n");
        return sb.toString();
    }
}
