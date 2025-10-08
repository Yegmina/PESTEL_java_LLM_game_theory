package app.model;

import java.util.HashMap;
import java.util.Map;
import simu.model.EnhancedFutureScenarioManager;

public class VectorEmbedder {

    // Simple keyword-based mapping for vector dimensions
    private static final Map<String, double[]> keywordVectors = new HashMap<>();

    static {
        // Dimension 1: Technological vs. Economic
        // Dimension 2: Social vs. Environmental
        keywordVectors.put("ai", new double[]{0.8, 0.2});
        keywordVectors.put("quantum", new double[]{0.9, 0.1});
        keywordVectors.put("biotech", new double[]{0.6, 0.7});
        keywordVectors.put("green", new double[]{-0.4, 0.9});
        keywordVectors.put("climate", new double[]{-0.5, 0.8});
        keywordVectors.put("democratic", new double[]{-0.2, -0.6});
        keywordVectors.put("economy", new double[]{0.5, -0.5});
        keywordVectors.put("resource", new double[]{-0.7, -0.8});
    }

    public static double[] getVector(EnhancedFutureScenarioManager.FutureScenario scenario) {
        double[] vector = new double[2]; // 2D vector for our chart
        String description = scenario.getName().toLowerCase() + " " + scenario.getDescription().toLowerCase();

        for (Map.Entry<String, double[]> entry : keywordVectors.entrySet()) {
            if (description.contains(entry.getKey())) {
                vector[0] += entry.getValue()[0];
                vector[1] += entry.getValue()[1];
            }
        }

        // Normalize the vector
        double magnitude = Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1]);
        if (magnitude > 0) {
            vector[0] /= magnitude;
            vector[1] /= magnitude;
        }

        return vector;
    }
}


