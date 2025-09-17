package simu.model;

import java.util.*;

/**
 * Scenario Generator creates alternative future outcomes based on agent interactions,
 * state transitions, and external influences. It implements the hierarchical state
 * transition model for scenario generation and clustering.
 */
public class ScenarioGenerator {
    private List<Scenario> scenarios;
    private Map<String, ScenarioCluster> clusters;
    private Random random;
    private int scenarioIdCounter;
    
    /**
     * Represents a single future scenario
     */
    public static class Scenario {
        private int scenarioId;
        private String scenarioName;
        private Map<String, Double> stateValues;
        private double probability;
        private double timeStep;
        private String parentScenarioId;
        private List<String> childScenarioIds;
        private Map<String, String> metadata;
        
        public Scenario(int scenarioId, String scenarioName, double timeStep) {
            this.scenarioId = scenarioId;
            this.scenarioName = scenarioName;
            this.timeStep = timeStep;
            this.stateValues = new HashMap<>();
            this.childScenarioIds = new ArrayList<>();
            this.metadata = new HashMap<>();
            this.probability = 0.0;
            this.parentScenarioId = null;
        }
        
        // Getters and setters
        public int getScenarioId() { return scenarioId; }
        public String getScenarioName() { return scenarioName; }
        public void setScenarioName(String scenarioName) { this.scenarioName = scenarioName; }
        public Map<String, Double> getStateValues() { return new HashMap<>(stateValues); }
        public void setStateValue(String key, double value) { stateValues.put(key, value); }
        public double getProbability() { return probability; }
        public void setProbability(double probability) { this.probability = probability; }
        public double getTimeStep() { return timeStep; }
        public String getParentScenarioId() { return parentScenarioId; }
        public void setParentScenarioId(String parentScenarioId) { this.parentScenarioId = parentScenarioId; }
        public List<String> getChildScenarioIds() { return new ArrayList<>(childScenarioIds); }
        public void addChildScenarioId(String childId) { childScenarioIds.add(childId); }
        public Map<String, String> getMetadata() { return new HashMap<>(metadata); }
        public void setMetadata(String key, String value) { metadata.put(key, value); }
    }
    
    /**
     * Represents a cluster of related scenarios
     */
    public static class ScenarioCluster {
        private String clusterId;
        private String clusterName;
        private List<Integer> scenarioIds;
        private String clusterType; // "optimistic", "challenging", "outlier"
        private double averageProbability;
        private Map<String, Double> averageStateValues;
        
        public ScenarioCluster(String clusterId, String clusterName, String clusterType) {
            this.clusterId = clusterId;
            this.clusterName = clusterName;
            this.clusterType = clusterType;
            this.scenarioIds = new ArrayList<>();
            this.averageStateValues = new HashMap<>();
            this.averageProbability = 0.0;
        }
        
        // Getters and setters
        public String getClusterId() { return clusterId; }
        public String getClusterName() { return clusterName; }
        public String getClusterType() { return clusterType; }
        public List<Integer> getScenarioIds() { return new ArrayList<>(scenarioIds); }
        public void addScenarioId(int scenarioId) { scenarioIds.add(scenarioId); }
        public double getAverageProbability() { return averageProbability; }
        public void setAverageProbability(double averageProbability) { this.averageProbability = averageProbability; }
        public Map<String, Double> getAverageStateValues() { return new HashMap<>(averageStateValues); }
        public void setAverageStateValue(String key, double value) { averageStateValues.put(key, value); }
    }
    
    /**
     * Initialize the scenario generator
     */
    public ScenarioGenerator() {
        this.scenarios = new ArrayList<>();
        this.clusters = new HashMap<>();
        this.random = new Random();
        this.scenarioIdCounter = 1;
    }
    
    /**
     * Generate initial scenarios from the current global state
     * @param globalState Current global state
     * @param timeStep Current time step
     * @return List of generated scenarios
     */
    public List<Scenario> generateInitialScenarios(GlobalState globalState, double timeStep) {
        List<Scenario> initialScenarios = new ArrayList<>();
        
        // Create three initial scenarios representing different strategic directions
        String[] scenarioNames = {"Conservative Growth", "Moderate Expansion", "Aggressive Innovation"};
        
        for (int i = 0; i < 3; i++) {
            Scenario scenario = new Scenario(scenarioIdCounter++, scenarioNames[i], timeStep);
            
            // Set state values based on global state with variations
            Map<String, Double> globalIndicators = globalState.getAllIndicators();
            for (Map.Entry<String, Double> entry : globalIndicators.entrySet()) {
                double baseValue = entry.getValue();
                double variation = (random.nextDouble() - 0.5) * 0.2; // ±10% variation
                scenario.setStateValue(entry.getKey(), baseValue + variation);
            }
            
            // Set scenario-specific characteristics
            switch (i) {
                case 0: // Conservative Growth
                    scenario.setStateValue("economic_growth_rate", 
                        scenario.getStateValues().get("economic_growth_rate") * 0.8);
                    scenario.setMetadata("strategy", "conservative");
                    scenario.setMetadata("risk_level", "low");
                    break;
                case 1: // Moderate Expansion
                    scenario.setMetadata("strategy", "moderate");
                    scenario.setMetadata("risk_level", "medium");
                    break;
                case 2: // Aggressive Innovation
                    scenario.setStateValue("technology_advancement", 
                        Math.min(1.0, scenario.getStateValues().get("technology_advancement") * 1.2));
                    scenario.setMetadata("strategy", "aggressive");
                    scenario.setMetadata("risk_level", "high");
                    break;
            }
            
            initialScenarios.add(scenario);
            scenarios.add(scenario);
        }
        
        return initialScenarios;
    }
    
    /**
     * Generate next-level scenarios from parent scenarios
     * @param parentScenarios Parent scenarios to branch from
     * @param timeStep Current time step
     * @return List of generated child scenarios
     */
    public List<Scenario> generateChildScenarios(List<Scenario> parentScenarios, double timeStep) {
        List<Scenario> childScenarios = new ArrayList<>();
        
        for (Scenario parent : parentScenarios) {
            // Each parent generates 2 child scenarios
            for (int i = 0; i < 2; i++) {
                Scenario child = new Scenario(scenarioIdCounter++, 
                    parent.getScenarioName() + " - Variant " + (i + 1), timeStep);
                child.setParentScenarioId(String.valueOf(parent.getScenarioId()));
                parent.addChildScenarioId(String.valueOf(child.getScenarioId()));
                
                // Inherit and modify parent state values
                Map<String, Double> parentValues = parent.getStateValues();
                for (Map.Entry<String, Double> entry : parentValues.entrySet()) {
                    double parentValue = entry.getValue();
                    double evolution = (random.nextDouble() - 0.5) * 0.3; // ±15% evolution
                    child.setStateValue(entry.getKey(), parentValue + evolution);
                }
                
                // Add time-based evolution
                child.setStateValue("economic_growth_rate", 
                    child.getStateValues().get("economic_growth_rate") + (random.nextDouble() - 0.5) * 0.05);
                child.setStateValue("technology_advancement", 
                    Math.min(1.0, child.getStateValues().get("technology_advancement") + random.nextDouble() * 0.1));
                
                childScenarios.add(child);
                scenarios.add(child);
            }
        }
        
        return childScenarios;
    }
    
    /**
     * Calculate probabilities for scenarios based on agent interactions and external influences
     * @param agentDecisions List of agent decisions
     * @param externalInfluences List of external influences
     */
    public void calculateScenarioProbabilities(List<AgentDecision> agentDecisions, 
                                             List<ExternalInfluence> externalInfluences) {
        // Reset all probabilities
        for (Scenario scenario : scenarios) {
            scenario.setProbability(0.0);
        }
        
        // Calculate base probabilities based on scenario characteristics
        for (Scenario scenario : scenarios) {
            double baseProbability = 1.0 / scenarios.size(); // Equal base probability
            
            // Adjust based on agent decisions
            for (AgentDecision decision : agentDecisions) {
                double decisionImpact = decision.getExpectedImpact() * decision.getConfidence();
                
                // Match decision type with scenario characteristics
                if (scenario.getMetadata().get("strategy").equals("conservative") && 
                    decision.getDecisionType().contains("maintain")) {
                    baseProbability += decisionImpact * 0.1;
                } else if (scenario.getMetadata().get("strategy").equals("aggressive") && 
                          decision.getDecisionType().contains("invest")) {
                    baseProbability += decisionImpact * 0.1;
                }
            }
            
            // Adjust based on external influences
            for (ExternalInfluence influence : externalInfluences) {
                double influenceImpact = influence.calculateExpectedImpact();
                
                if (influence.getInfluenceType().equals("economic_shock") && influenceImpact < 0) {
                    // Economic shocks favor conservative scenarios
                    if (scenario.getMetadata().get("strategy").equals("conservative")) {
                        baseProbability += Math.abs(influenceImpact) * 0.1;
                    }
                } else if (influence.getInfluenceType().equals("technological_breakthrough") && influenceImpact > 0) {
                    // Tech breakthroughs favor aggressive scenarios
                    if (scenario.getMetadata().get("strategy").equals("aggressive")) {
                        baseProbability += influenceImpact * 0.1;
                    }
                }
            }
            
            scenario.setProbability(Math.max(0.0, baseProbability));
        }
        
        // Normalize probabilities
        normalizeProbabilities();
    }
    
    /**
     * Cluster scenarios into meaningful groups
     * @return Map of cluster ID to ScenarioCluster
     */
    public Map<String, ScenarioCluster> clusterScenarios() {
        clusters.clear();
        
        // Create clusters based on scenario characteristics
        ScenarioCluster optimisticCluster = new ScenarioCluster("cluster1", "Optimistic Scenarios", "optimistic");
        ScenarioCluster challengingCluster = new ScenarioCluster("cluster2", "Challenging Scenarios", "challenging");
        ScenarioCluster outlierCluster = new ScenarioCluster("cluster3", "Outlier Scenarios", "outlier");
        
        for (Scenario scenario : scenarios) {
            double economicGrowth = scenario.getStateValues().getOrDefault("economic_growth_rate", 0.0);
            double peaceIndex = scenario.getStateValues().getOrDefault("world_peace_index", 0.0);
            double resourceAvailability = scenario.getStateValues().getOrDefault("resource_availability", 0.0);
            
            // Cluster based on combined indicators
            double overallScore = (economicGrowth + peaceIndex + resourceAvailability) / 3.0;
            
            if (overallScore > 0.6) {
                optimisticCluster.addScenarioId(scenario.getScenarioId());
            } else if (overallScore < 0.3) {
                challengingCluster.addScenarioId(scenario.getScenarioId());
            } else {
                outlierCluster.addScenarioId(scenario.getScenarioId());
            }
        }
        
        // Calculate cluster statistics
        calculateClusterStatistics(optimisticCluster);
        calculateClusterStatistics(challengingCluster);
        calculateClusterStatistics(outlierCluster);
        
        clusters.put("cluster1", optimisticCluster);
        clusters.put("cluster2", challengingCluster);
        clusters.put("cluster3", outlierCluster);
        
        return new HashMap<>(clusters);
    }
    
    /**
     * Calculate statistics for a cluster
     * @param cluster The cluster to calculate statistics for
     */
    private void calculateClusterStatistics(ScenarioCluster cluster) {
        if (cluster.getScenarioIds().isEmpty()) {
            return;
        }
        
        double totalProbability = 0.0;
        Map<String, Double> totalStateValues = new HashMap<>();
        
        for (int scenarioId : cluster.getScenarioIds()) {
            Scenario scenario = getScenarioById(scenarioId);
            if (scenario != null) {
                totalProbability += scenario.getProbability();
                
                for (Map.Entry<String, Double> entry : scenario.getStateValues().entrySet()) {
                    totalStateValues.merge(entry.getKey(), entry.getValue(), Double::sum);
                }
            }
        }
        
        cluster.setAverageProbability(totalProbability / cluster.getScenarioIds().size());
        
        for (Map.Entry<String, Double> entry : totalStateValues.entrySet()) {
            cluster.setAverageStateValue(entry.getKey(), entry.getValue() / cluster.getScenarioIds().size());
        }
    }
    
    /**
     * Get a scenario by ID
     * @param scenarioId The scenario ID
     * @return The scenario, or null if not found
     */
    private Scenario getScenarioById(int scenarioId) {
        return scenarios.stream()
                .filter(s -> s.getScenarioId() == scenarioId)
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Normalize scenario probabilities to sum to 1.0
     */
    private void normalizeProbabilities() {
        double totalProbability = scenarios.stream()
                .mapToDouble(Scenario::getProbability)
                .sum();
        
        if (totalProbability > 0) {
            for (Scenario scenario : scenarios) {
                scenario.setProbability(scenario.getProbability() / totalProbability);
            }
        }
    }
    
    /**
     * Get all scenarios
     * @return List of all scenarios
     */
    public List<Scenario> getAllScenarios() {
        return new ArrayList<>(scenarios);
    }
    
    /**
     * Get all clusters
     * @return Map of all clusters
     */
    public Map<String, ScenarioCluster> getAllClusters() {
        return new HashMap<>(clusters);
    }
    
    /**
     * Get a summary of the scenario generation results
     * @return String summary
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Scenario Generation Summary:\n");
        sb.append("  Total Scenarios: ").append(scenarios.size()).append("\n");
        sb.append("  Total Clusters: ").append(clusters.size()).append("\n");
        
        for (ScenarioCluster cluster : clusters.values()) {
            sb.append("  ").append(cluster.getClusterName()).append(": ")
              .append(cluster.getScenarioIds().size()).append(" scenarios, ")
              .append(String.format("%.2f%%", cluster.getAverageProbability() * 100))
              .append(" average probability\n");
        }
        
        return sb.toString();
    }
}
