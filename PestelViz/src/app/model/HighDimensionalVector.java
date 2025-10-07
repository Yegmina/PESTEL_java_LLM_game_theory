package app.model;

import java.util.Random;

/**
 * Represents a high-dimensional vector (1500+ dimensions) for alternative futures
 * Each dimension represents different aspects: economic, social, technological, etc.
 */
public class HighDimensionalVector {
    private final double[] dimensions;
    private final String scenarioName;
    private final Random random;
    
    public HighDimensionalVector(String scenarioName, int dimensionCount) {
        this.scenarioName = scenarioName;
        this.dimensions = new double[dimensionCount];
        this.random = new Random(scenarioName.hashCode()); // Deterministic but different per scenario
        
        // Initialize with scenario-specific base values
        initializeScenarioBase();
    }
    
    private void initializeScenarioBase() {
        // Create scenario-specific patterns in high-dimensional space
        String name = scenarioName.toLowerCase();
        
        // Economic dimensions (0-299)
        if (name.contains("ai") || name.contains("quantum") || name.contains("digital")) {
            fillRange(0, 299, 0.8, 1.0); // High tech = high economic innovation
        } else if (name.contains("green") || name.contains("climate")) {
            fillRange(0, 299, 0.6, 0.8); // Moderate economic, high environmental
        } else {
            fillRange(0, 299, 0.3, 0.6); // Standard economic baseline
        }
        
        // Social dimensions (300-599)
        if (name.contains("democratic") || name.contains("education")) {
            fillRange(300, 599, 0.7, 1.0); // High social development
        } else if (name.contains("resource") || name.contains("crisis")) {
            fillRange(300, 599, 0.1, 0.4); // Low social stability
        } else {
            fillRange(300, 599, 0.4, 0.7); // Moderate social
        }
        
        // Technological dimensions (600-899)
        if (name.contains("ai") || name.contains("quantum") || name.contains("biotech")) {
            fillRange(600, 899, 0.9, 1.0); // Very high tech
        } else if (name.contains("digital")) {
            fillRange(600, 899, 0.7, 0.9); // High digital tech
        } else {
            fillRange(600, 899, 0.2, 0.5); // Lower tech
        }
        
        // Environmental dimensions (900-1199)
        if (name.contains("green") || name.contains("climate")) {
            fillRange(900, 1199, 0.8, 1.0); // High environmental focus
        } else if (name.contains("resource") || name.contains("crisis")) {
            fillRange(900, 1199, 0.1, 0.3); // Environmental crisis
        } else {
            fillRange(900, 1199, 0.4, 0.7); // Moderate environmental
        }
        
        // Political dimensions (1200-1499)
        if (name.contains("democratic") || name.contains("multipolar")) {
            fillRange(1200, 1499, 0.6, 0.9); // High political complexity
        } else if (name.contains("regional") || name.contains("bloc")) {
            fillRange(1200, 1499, 0.4, 0.7); // Regional politics
        } else {
            fillRange(1200, 1499, 0.3, 0.6); // Standard politics
        }
    }
    
    private void fillRange(int start, int end, double min, double max) {
        for (int i = start; i <= end && i < dimensions.length; i++) {
            dimensions[i] = min + random.nextDouble() * (max - min);
        }
    }
    
    public void evolve(double probability, int day) {
        // Evolve the vector based on probability and time
        double evolutionFactor = probability * 0.1 + (day * 0.001);
        
        for (int i = 0; i < dimensions.length; i++) {
            // Add some noise and evolution
            double noise = (random.nextGaussian() * 0.01) * evolutionFactor;
            dimensions[i] = Math.max(0, Math.min(1, dimensions[i] + noise));
        }
    }
    
    public double[] getDimensions() {
        return dimensions.clone();
    }
    
    public String getScenarioName() {
        return scenarioName;
    }
    
    public double[] compressTo3D() {
        // Compress 1500+ dimensions to 3D using PCA-like approach
        double x = 0, y = 0, z = 0;
        
        // X-axis: Economic + Technological emphasis
        for (int i = 0; i < 600; i++) {
            x += dimensions[i] * (i < 300 ? 1.0 : 0.8);
        }
        x = x / 540.0; // Normalize
        
        // Y-axis: Social + Environmental emphasis  
        for (int i = 300; i < 1200; i++) {
            y += dimensions[i] * (i < 600 ? 0.8 : 1.0);
        }
        y = y / 720.0; // Normalize
        
        // Z-axis: Political + Temporal complexity
        for (int i = 1200; i < dimensions.length; i++) {
            z += dimensions[i];
        }
        z = z / 300.0; // Normalize
        
        return new double[]{x * 100, y * 100, z * 100}; // Scale to 0-100 range
    }
}
