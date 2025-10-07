package app.model;

import simu.model.EnhancedFutureScenarioManager;
import simu.model.PESTELState;
import java.util.*;

/**
 * Manages temporal progression of high-dimensional vectors for alternative futures
 * Creates day0→day1→day2... vector chains for each scenario
 */
public class TemporalVectorManager {
    private final Map<String, List<HighDimensionalVector>> temporalVectors;
    private final Map<String, List<double[]>> compressed3DPaths;
    private final int vectorDimensions;
    
    public TemporalVectorManager() {
        this.temporalVectors = new HashMap<>();
        this.compressed3DPaths = new HashMap<>();
        this.vectorDimensions = 1500; // 1500-dimensional vectors
    }
    
    public void initializeScenarios(List<EnhancedFutureScenarioManager.FutureScenario> scenarios) {
        for (EnhancedFutureScenarioManager.FutureScenario scenario : scenarios) {
            String name = scenario.getName();
            
            // Initialize with day 0 vector
            HighDimensionalVector day0Vector = new HighDimensionalVector(name, vectorDimensions);
            day0Vector.evolve(scenario.getProbability(), 0);
            
            temporalVectors.put(name, new ArrayList<>(Arrays.asList(day0Vector)));
            
            // Initialize 3D compressed path
            compressed3DPaths.put(name, new ArrayList<>(Arrays.asList(day0Vector.compressTo3D())));
        }
    }
    
    public void addDay(int day, List<EnhancedFutureScenarioManager.FutureScenario> scenarios, PESTELState pestelState) {
        for (EnhancedFutureScenarioManager.FutureScenario scenario : scenarios) {
            String name = scenario.getName();
            
            // Get the previous day's vector
            List<HighDimensionalVector> vectors = temporalVectors.get(name);
            List<double[]> path3D = compressed3DPaths.get(name);
            
            if (vectors != null && !vectors.isEmpty()) {
                // Create new vector based on previous day + evolution
                HighDimensionalVector previousVector = vectors.get(vectors.size() - 1);
                HighDimensionalVector newVector = new HighDimensionalVector(name, vectorDimensions);
                
                // Copy previous state and evolve
                double[] prevDims = previousVector.getDimensions();
                double[] newDims = newVector.getDimensions();
                System.arraycopy(prevDims, 0, newDims, 0, prevDims.length);
                
                // Evolve based on current probability and PESTEL state
                newVector.evolve(scenario.getProbability(), day);
                applyPESTELInfluence(newVector, pestelState);
                
                vectors.add(newVector);
                path3D.add(newVector.compressTo3D());
            }
        }
    }
    
    private void applyPESTELInfluence(HighDimensionalVector vector, PESTELState pestelState) {
        // Apply PESTEL state influence to the high-dimensional vector
        // This creates realistic evolution based on current global conditions
        
        double[] dimensions = vector.getDimensions();
        
        // Political influence (dimensions 1200-1499)
        if (pestelState.getPolitical("stability").contains("stable")) {
            for (int i = 1200; i < 1500; i++) {
                dimensions[i] = Math.min(1.0, dimensions[i] + 0.01);
            }
        }
        
        // Economic influence (dimensions 0-299)
        if (pestelState.getEconomic("growth").contains("growth")) {
            for (int i = 0; i < 300; i++) {
                dimensions[i] = Math.min(1.0, dimensions[i] + 0.01);
            }
        }
        
        // Environmental influence (dimensions 900-1199)
        if (pestelState.getEnvironmental("climate_change").contains("renewable")) {
            for (int i = 900; i < 1200; i++) {
                dimensions[i] = Math.min(1.0, dimensions[i] + 0.01);
            }
        }
    }
    
    public Map<String, List<double[]>> get3DPaths(int startDay, int endDay) {
        Map<String, List<double[]>> filteredPaths = new HashMap<>();
        
        for (Map.Entry<String, List<double[]>> entry : compressed3DPaths.entrySet()) {
            String scenario = entry.getKey();
            List<double[]> fullPath = entry.getValue();
            
            // Extract the requested timeframe
            List<double[]> timeframePath = new ArrayList<>();
            for (int day = startDay; day <= endDay && day < fullPath.size(); day++) {
                timeframePath.add(fullPath.get(day));
            }
            
            filteredPaths.put(scenario, timeframePath);
        }
        
        return filteredPaths;
    }
    
    public Map<String, Double> getCurrentProbabilities(List<EnhancedFutureScenarioManager.FutureScenario> scenarios) {
        Map<String, Double> probabilities = new HashMap<>();
        for (EnhancedFutureScenarioManager.FutureScenario scenario : scenarios) {
            probabilities.put(scenario.getName(), scenario.getProbability());
        }
        return probabilities;
    }
    
    public int getMaxDays() {
        return temporalVectors.values().stream()
                .mapToInt(List::size)
                .max()
                .orElse(0);
    }
}
