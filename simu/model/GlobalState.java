package simu.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Global State represents the centralized information repository that aggregates
 * key indicators affecting all agents in the forecasting system.
 * This class maintains global factors such as economic indicators, climate data,
 * peace index, and resource availability.
 */
public class GlobalState {
    private Map<String, Double> indicators;
    private Map<String, String> statusIndicators;
    private double lastUpdateTime;
    private int updateCount;
    
    /**
     * Initialize the global state with default values
     */
    public GlobalState() {
        indicators = new ConcurrentHashMap<>();
        statusIndicators = new ConcurrentHashMap<>();
        lastUpdateTime = 0.0;
        updateCount = 0;
        
        // Initialize with baseline values
        initializeDefaultValues();
    }
    
    /**
     * Initialize default global state values
     */
    private void initializeDefaultValues() {
        // Economic indicators
        indicators.put("global_temperature", 15.0); // Celsius
        indicators.put("world_peace_index", 0.7);   // 0-1 scale
        indicators.put("economic_growth_rate", 0.03); // 3% annual growth
        indicators.put("resource_availability", 0.8); // 0-1 scale
        indicators.put("population_growth_rate", 0.01); // 1% annual growth
        indicators.put("technology_advancement", 0.5); // 0-1 scale
        
        // Status indicators
        statusIndicators.put("biotech_status", "moderate");
        statusIndicators.put("climate_status", "stable");
        statusIndicators.put("economic_status", "growing");
        statusIndicators.put("political_status", "stable");
    }
    
    /**
     * Update a numerical indicator
     * @param key The indicator name
     * @param value The new value
     */
    public void updateIndicator(String key, double value) {
        indicators.put(key, value);
        lastUpdateTime = simu.framework.Clock.getInstance().getClock();
        updateCount++;
    }
    
    /**
     * Update a status indicator
     * @param key The status name
     * @param value The new status
     */
    public void updateStatus(String key, String value) {
        statusIndicators.put(key, value);
        lastUpdateTime = simu.framework.Clock.getInstance().getClock();
        updateCount++;
    }
    
    /**
     * Get a numerical indicator value
     * @param key The indicator name
     * @return The indicator value, or 0.0 if not found
     */
    public double getIndicator(String key) {
        return indicators.getOrDefault(key, 0.0);
    }
    
    /**
     * Get a status indicator value
     * @param key The status name
     * @return The status value, or "unknown" if not found
     */
    public String getStatus(String key) {
        return statusIndicators.getOrDefault(key, "unknown");
    }
    
    /**
     * Apply external influence to the global state
     * @param influenceType The type of external influence
     * @param magnitude The magnitude of the influence
     */
    public void applyExternalInfluence(String influenceType, double magnitude) {
        switch (influenceType.toLowerCase()) {
            case "climate_change":
                double currentTemp = getIndicator("global_temperature");
                updateIndicator("global_temperature", currentTemp + magnitude * 0.1);
                updateStatus("climate_status", magnitude > 0 ? "warming" : "cooling");
                break;
                
            case "economic_shock":
                double currentGrowth = getIndicator("economic_growth_rate");
                updateIndicator("economic_growth_rate", currentGrowth + magnitude);
                updateStatus("economic_status", magnitude > 0 ? "booming" : "recession");
                break;
                
            case "technological_breakthrough":
                double currentTech = getIndicator("technology_advancement");
                updateIndicator("technology_advancement", Math.min(1.0, currentTech + magnitude));
                updateStatus("biotech_status", "advanced");
                break;
                
            case "conflict":
                double currentPeace = getIndicator("world_peace_index");
                updateIndicator("world_peace_index", Math.max(0.0, currentPeace - magnitude));
                updateStatus("political_status", "unstable");
                break;
                
            default:
                // Generic influence on resource availability
                double currentResources = getIndicator("resource_availability");
                updateIndicator("resource_availability", Math.max(0.0, Math.min(1.0, currentResources + magnitude)));
        }
    }
    
    /**
     * Get all indicators as a map
     * @return Copy of the indicators map
     */
    public Map<String, Double> getAllIndicators() {
        return new HashMap<>(indicators);
    }
    
    /**
     * Get all status indicators as a map
     * @return Copy of the status indicators map
     */
    public Map<String, String> getAllStatusIndicators() {
        return new HashMap<>(statusIndicators);
    }
    
    /**
     * Get the time of the last update
     * @return Last update time
     */
    public double getLastUpdateTime() {
        return lastUpdateTime;
    }
    
    /**
     * Get the number of updates performed
     * @return Update count
     */
    public int getUpdateCount() {
        return updateCount;
    }
    
    /**
     * Calculate the overall system stability index
     * @return Stability index between 0 and 1
     */
    public double calculateStabilityIndex() {
        double peaceIndex = getIndicator("world_peace_index");
        double economicStability = Math.abs(getIndicator("economic_growth_rate")) < 0.1 ? 1.0 : 0.5;
        double resourceStability = getIndicator("resource_availability");
        
        return (peaceIndex + economicStability + resourceStability) / 3.0;
    }
    
    /**
     * Get a summary of the current global state
     * @return String representation of the global state
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Global State Summary:\n");
        sb.append("  Temperature: ").append(String.format("%.2f°C", getIndicator("global_temperature"))).append("\n");
        sb.append("  Peace Index: ").append(String.format("%.2f", getIndicator("world_peace_index"))).append("\n");
        sb.append("  Economic Growth: ").append(String.format("%.2f%%", getIndicator("economic_growth_rate") * 100)).append("\n");
        sb.append("  Resource Availability: ").append(String.format("%.2f", getIndicator("resource_availability"))).append("\n");
        sb.append("  Stability Index: ").append(String.format("%.2f", calculateStabilityIndex())).append("\n");
        sb.append("  Last Update: ").append(String.format("%.2f", lastUpdateTime)).append("\n");
        return sb.toString();
    }
}
