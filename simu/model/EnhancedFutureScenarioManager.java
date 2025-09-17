package simu.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Enhanced Alternative Future Scenario Manager with comprehensive analysis
 * Tracks 12 different future scenarios and their evolution based on real-world actions
 */
public class EnhancedFutureScenarioManager {
    private List<FutureScenario> scenarios;
    private Map<String, Double> scenarioProbabilities;
    private Map<String, List<String>> scenarioTriggers;
    private FutureScenario currentDominantScenario;
    private int daysSinceLastUpdate;
    private List<ScenarioTransition> transitions;
    
    /**
     * Enhanced Future Scenario with detailed tracking
     */
    public static class FutureScenario {
        private String name;
        private String description;
        private double probability;
        private double baseProbability;
        private String implications;
        private List<String> keyIndicators;
        private List<String> supportingActions;
        private double momentum;
        private String dominantSector;
        
        public FutureScenario(ComprehensiveRealWorldData.FutureScenario data) {
            this.name = data.name;
            this.description = data.description;
            this.probability = data.probability;
            this.baseProbability = data.probability;
            this.implications = data.implications;
            this.keyIndicators = new ArrayList<>();
            this.supportingActions = new ArrayList<>();
            this.momentum = 0.0;
            this.dominantSector = extractDominantSector(data.name);
            
            initializeKeyIndicators();
        }
        
        private void initializeKeyIndicators() {
            // Set key indicators based on scenario type
            switch (name.toLowerCase()) {
                case "ai supremacy":
                    keyIndicators.addAll(Arrays.asList("AI research funding", "Tech company valuations", "AI regulation", "Automation adoption"));
                    break;
                case "green transition triumph":
                    keyIndicators.addAll(Arrays.asList("Renewable energy adoption", "Carbon pricing", "Green investment", "Climate policies"));
                    break;
                case "multipolar world order":
                    keyIndicators.addAll(Arrays.asList("Trade bloc formation", "Currency diversification", "Military spending", "Diplomatic initiatives"));
                    break;
                case "digital economy supremacy":
                    keyIndicators.addAll(Arrays.asList("Digital payments", "Remote work adoption", "Virtual reality usage", "Cryptocurrency adoption"));
                    break;
                default:
                    keyIndicators.addAll(Arrays.asList("Economic indicators", "Political stability", "Technology adoption", "Social changes"));
            }
        }
        
        private String extractDominantSector(String scenarioName) {
            if (scenarioName.toLowerCase().contains("ai") || scenarioName.toLowerCase().contains("quantum")) return "Technology";
            if (scenarioName.toLowerCase().contains("green") || scenarioName.toLowerCase().contains("climate")) return "Environment";
            if (scenarioName.toLowerCase().contains("multipolar") || scenarioName.toLowerCase().contains("bloc")) return "Geopolitics";
            if (scenarioName.toLowerCase().contains("digital") || scenarioName.toLowerCase().contains("virtual")) return "Digital";
            if (scenarioName.toLowerCase().contains("biotech") || scenarioName.toLowerCase().contains("aging")) return "Healthcare";
            return "Mixed";
        }
        
        public void updateMomentum(List<AgentAction> recentActions, PESTELState globalPESTEL) {
            double newMomentum = 0.0;
            
            // Calculate momentum based on relevant actions
            for (AgentAction action : recentActions) {
                if (isRelevantAction(action)) {
                    newMomentum += 0.1;
                    if (!supportingActions.contains(action.getActionDescription())) {
                        supportingActions.add(action.getActionDescription());
                        // Keep only last 10 actions
                        if (supportingActions.size() > 10) {
                            supportingActions.remove(0);
                        }
                    }
                }
            }
            
            // Add PESTEL alignment bonus
            newMomentum += calculatePESTELAlignment(globalPESTEL) * 0.2;
            
            // Apply momentum decay
            this.momentum = this.momentum * 0.9 + newMomentum * 0.1;
        }
        
        public boolean isRelevantAction(AgentAction action) {
            String actionLower = action.getActionDescription().toLowerCase();
            String nameLower = name.toLowerCase();
            
            // Specific relevance checks
            if (nameLower.contains("ai") && (actionLower.contains("ai") || actionLower.contains("artificial intelligence"))) return true;
            if (nameLower.contains("green") && (actionLower.contains("climate") || actionLower.contains("sustainable") || actionLower.contains("renewable"))) return true;
            if (nameLower.contains("quantum") && actionLower.contains("quantum")) return true;
            if (nameLower.contains("biotech") && (actionLower.contains("biotech") || actionLower.contains("medicine") || actionLower.contains("health"))) return true;
            if (nameLower.contains("digital") && (actionLower.contains("digital") || actionLower.contains("virtual") || actionLower.contains("online"))) return true;
            if (nameLower.contains("multipolar") && (actionLower.contains("alliance") || actionLower.contains("cooperation") || actionLower.contains("trade"))) return true;
            
            return false;
        }
        
        public double calculatePESTELAlignment(PESTELState globalPESTEL) {
            double alignment = 0.0;
            
            switch (dominantSector) {
                case "Technology":
                    if (globalPESTEL.getTechnological("innovation").contains("AI") || 
                        globalPESTEL.getTechnological("innovation").contains("advanced")) {
                        alignment += 0.4;
                    }
                    break;
                case "Environment":
                    if (globalPESTEL.getEnvironmental("climate_change").contains("transition") || 
                        globalPESTEL.getEnvironmental("sustainability").contains("progress")) {
                        alignment += 0.4;
                    }
                    break;
                case "Geopolitics":
                    if (globalPESTEL.getPolitical("international_relations").contains("cooperation") || 
                        globalPESTEL.getPolitical("stability").contains("alliance")) {
                        alignment += 0.3;
                    }
                    break;
            }
            
            return Math.min(1.0, alignment);
        }
        
        // Getters and setters
        public String getName() { return name; }
        public String getDescription() { return description; }
        public double getProbability() { return probability; }
        public void setProbability(double probability) { this.probability = Math.max(0.01, Math.min(0.8, probability)); }
        public double getBaseProbability() { return baseProbability; }
        public String getImplications() { return implications; }
        public List<String> getKeyIndicators() { return new ArrayList<>(keyIndicators); }
        public List<String> getSupportingActions() { return new ArrayList<>(supportingActions); }
        public double getMomentum() { return momentum; }
        public String getDominantSector() { return dominantSector; }
    }
    
    /**
     * Scenario Transition tracking
     */
    public static class ScenarioTransition {
        private String fromScenario;
        private String toScenario;
        private int day;
        private String trigger;
        private double probabilityShift;
        
        public ScenarioTransition(String fromScenario, String toScenario, int day, String trigger, double probabilityShift) {
            this.fromScenario = fromScenario;
            this.toScenario = toScenario;
            this.day = day;
            this.trigger = trigger;
            this.probabilityShift = probabilityShift;
        }
        
        // Getters
        public String getFromScenario() { return fromScenario; }
        public String getToScenario() { return toScenario; }
        public int getDay() { return day; }
        public String getTrigger() { return trigger; }
        public double getProbabilityShift() { return probabilityShift; }
        
        @Override
        public String toString() {
            return String.format("Day %d: %s → %s (%.2f%% shift) due to %s", 
                day, fromScenario, toScenario, probabilityShift * 100, trigger);
        }
    }
    
    public EnhancedFutureScenarioManager() {
        this.scenarios = new ArrayList<>();
        this.scenarioProbabilities = new HashMap<>();
        this.scenarioTriggers = new HashMap<>();
        this.transitions = new ArrayList<>();
        this.daysSinceLastUpdate = 0;
        
        initializeScenarios();
    }
    
    private void initializeScenarios() {
        for (ComprehensiveRealWorldData.FutureScenario scenarioData : ComprehensiveRealWorldData.ALTERNATIVE_FUTURES) {
            FutureScenario scenario = new FutureScenario(scenarioData);
            scenarios.add(scenario);
            scenarioProbabilities.put(scenario.getName(), scenario.getProbability());
            scenarioTriggers.put(scenario.getName(), new ArrayList<>());
        }
        updateDominantScenario();
    }
    
    /**
     * Update scenario probabilities based on recent actions and global state
     */
    public void updateScenarioProbabilities(List<AgentAction> recentActions, PESTELState globalPESTEL, int currentDay) {
        FutureScenario previousDominant = currentDominantScenario;
        
        // Update each scenario
        for (FutureScenario scenario : scenarios) {
            scenario.updateMomentum(recentActions, globalPESTEL);
            
            double newProbability = calculateUpdatedProbability(scenario, recentActions, globalPESTEL);
            double oldProbability = scenario.getProbability();
            
            scenario.setProbability(newProbability);
            scenarioProbabilities.put(scenario.getName(), newProbability);
            
            // Track significant changes
            if (Math.abs(newProbability - oldProbability) > 0.05) {
                String trigger = findMainTrigger(scenario, recentActions);
                ScenarioTransition transition = new ScenarioTransition(
                    previousDominant.getName(), scenario.getName(), currentDay, trigger, 
                    newProbability - oldProbability);
                transitions.add(transition);
            }
        }
        
        // Normalize probabilities
        normalizeProbabilities();
        updateDominantScenario();
        
        daysSinceLastUpdate = 0;
    }
    
    private double calculateUpdatedProbability(FutureScenario scenario, List<AgentAction> recentActions, PESTELState globalPESTEL) {
        double baseProbability = scenario.getBaseProbability();
        double adjustment = 0.0;
        
        // Momentum-based adjustment
        adjustment += scenario.getMomentum() * 0.1;
        
        // Recent action relevance
        long relevantActions = recentActions.stream()
            .filter(scenario::isRelevantAction)
            .count();
        adjustment += relevantActions * 0.02;
        
        // PESTEL alignment bonus
        adjustment += scenario.calculatePESTELAlignment(globalPESTEL) * 0.05;
        
        // Time-based decay (scenarios become more likely over time if supported)
        if (scenario.getMomentum() > 0.1) {
            adjustment += daysSinceLastUpdate * 0.01;
        }
        
        return Math.max(0.01, Math.min(0.6, baseProbability + adjustment));
    }
    
    private String findMainTrigger(FutureScenario scenario, List<AgentAction> recentActions) {
        return recentActions.stream()
            .filter(scenario::isRelevantAction)
            .map(action -> action.getAgentId() + ": " + action.getActionDescription())
            .collect(Collectors.joining("; "));
    }
    
    private void normalizeProbabilities() {
        double total = scenarios.stream().mapToDouble(FutureScenario::getProbability).sum();
        if (total > 0) {
            for (FutureScenario scenario : scenarios) {
                double normalized = scenario.getProbability() / total;
                scenario.setProbability(normalized);
                scenarioProbabilities.put(scenario.getName(), normalized);
            }
        }
    }
    
    private void updateDominantScenario() {
        currentDominantScenario = scenarios.stream()
            .max(Comparator.comparing(FutureScenario::getProbability))
            .orElse(scenarios.get(0));
    }
    
    /**
     * Generate comprehensive scenario analysis
     */
    public ScenarioAnalysis generateScenarioAnalysis() {
        return new ScenarioAnalysis(scenarios, transitions, currentDominantScenario);
    }
    
    /**
     * Get top 3 most likely scenarios
     */
    public List<FutureScenario> getTopScenarios() {
        return scenarios.stream()
            .sorted((s1, s2) -> Double.compare(s2.getProbability(), s1.getProbability()))
            .limit(3)
            .collect(Collectors.toList());
    }
    
    /**
     * Get scenarios by sector
     */
    public Map<String, List<FutureScenario>> getScenariosBySector() {
        return scenarios.stream()
            .collect(Collectors.groupingBy(FutureScenario::getDominantSector));
    }
    
    /**
     * Comprehensive Scenario Analysis
     */
    public static class ScenarioAnalysis {
        private List<FutureScenario> allScenarios;
        private List<ScenarioTransition> transitions;
        private FutureScenario dominantScenario;
        private Map<String, Double> sectorProbabilities;
        private List<String> keyTrends;
        private List<String> strategicRecommendations;
        
        public ScenarioAnalysis(List<FutureScenario> scenarios, List<ScenarioTransition> transitions, FutureScenario dominant) {
            this.allScenarios = new ArrayList<>(scenarios);
            this.transitions = new ArrayList<>(transitions);
            this.dominantScenario = dominant;
            this.sectorProbabilities = new HashMap<>();
            this.keyTrends = new ArrayList<>();
            this.strategicRecommendations = new ArrayList<>();
            
            analyzeScenarios();
        }
        
        private void analyzeScenarios() {
            // Calculate sector probabilities
            Map<String, Double> sectorTotals = new HashMap<>();
            for (FutureScenario scenario : allScenarios) {
                sectorTotals.merge(scenario.getDominantSector(), scenario.getProbability(), Double::sum);
            }
            this.sectorProbabilities = sectorTotals;
            
            // Identify key trends
            identifyKeyTrends();
            
            // Generate strategic recommendations
            generateStrategicRecommendations();
        }
        
        private void identifyKeyTrends() {
            // Analyze momentum and probability changes
            List<FutureScenario> highMomentum = allScenarios.stream()
                .filter(s -> s.getMomentum() > 0.2)
                .sorted((s1, s2) -> Double.compare(s2.getMomentum(), s1.getMomentum()))
                .collect(Collectors.toList());
            
            for (FutureScenario scenario : highMomentum) {
                keyTrends.add(String.format("%s gaining momentum (%.1f%% probability, %.2f momentum)", 
                    scenario.getName(), scenario.getProbability() * 100, scenario.getMomentum()));
            }
            
            // Analyze recent transitions
            if (!transitions.isEmpty()) {
                ScenarioTransition lastTransition = transitions.get(transitions.size() - 1);
                keyTrends.add(String.format("Recent shift: %s triggered by %s", 
                    lastTransition.getToScenario(), lastTransition.getTrigger()));
            }
            
            // Sector analysis
            String dominantSector = sectorProbabilities.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Mixed");
            
            keyTrends.add(String.format("%s sector scenarios dominate with %.1f%% combined probability", 
                dominantSector, sectorProbabilities.get(dominantSector) * 100));
        }
        
        private void generateStrategicRecommendations() {
            // Recommendations based on dominant scenario
            switch (dominantScenario.getDominantSector()) {
                case "Technology":
                    strategicRecommendations.addAll(Arrays.asList(
                        "Invest heavily in AI and quantum computing education programs",
                        "Partner with tech giants (Apple, Microsoft, NVIDIA) for curriculum development",
                        "Establish technology ethics and governance research center",
                        "Create AI-human collaboration training programs"
                    ));
                    break;
                    
                case "Environment":
                    strategicRecommendations.addAll(Arrays.asList(
                        "Develop comprehensive sustainability and climate science programs",
                        "Partner with renewable energy companies and environmental organizations",
                        "Create carbon-neutral campus and operations",
                        "Establish climate adaptation and resilience research center"
                    ));
                    break;
                    
                case "Geopolitics":
                    strategicRecommendations.addAll(Arrays.asList(
                        "Strengthen international partnerships and exchange programs",
                        "Develop crisis management and diplomatic studies programs",
                        "Create conflict resolution and peacebuilding research center",
                        "Enhance multicultural competency and language programs"
                    ));
                    break;
                    
                case "Digital":
                    strategicRecommendations.addAll(Arrays.asList(
                        "Expand online and hybrid learning capabilities",
                        "Develop virtual reality and metaverse educational experiences",
                        "Create digital transformation and cybersecurity programs",
                        "Establish digital society research and policy center"
                    ));
                    break;
                    
                case "Healthcare":
                    strategicRecommendations.addAll(Arrays.asList(
                        "Develop aging society and healthcare innovation programs",
                        "Partner with pharmaceutical and biotech companies",
                        "Create personalized medicine and genomics research center",
                        "Establish global health and pandemic preparedness programs"
                    ));
                    break;
                    
                default:
                    strategicRecommendations.addAll(Arrays.asList(
                        "Maintain flexible and adaptive strategic planning approach",
                        "Diversify partnerships across multiple sectors and regions",
                        "Strengthen scenario planning and futures research capabilities",
                        "Create interdisciplinary programs addressing complex global challenges"
                    ));
            }
            
            // Add cross-cutting recommendations
            strategicRecommendations.addAll(Arrays.asList(
                "Enhance data analytics and strategic intelligence capabilities",
                "Develop global competency and cultural intelligence programs",
                "Create innovation ecosystems and entrepreneurship support",
                "Establish continuous learning and adaptation frameworks"
            ));
        }
        
        // Getters
        public List<FutureScenario> getAllScenarios() { return new ArrayList<>(allScenarios); }
        public List<ScenarioTransition> getTransitions() { return new ArrayList<>(transitions); }
        public FutureScenario getDominantScenario() { return dominantScenario; }
        public Map<String, Double> getSectorProbabilities() { return new HashMap<>(sectorProbabilities); }
        public List<String> getKeyTrends() { return new ArrayList<>(keyTrends); }
        public List<String> getStrategicRecommendations() { return new ArrayList<>(strategicRecommendations); }
        
        /**
         * Generate comprehensive summary report
         */
        public String generateSummaryReport() {
            StringBuilder report = new StringBuilder();
            
            report.append("=== ALTERNATIVE FUTURES ANALYSIS SUMMARY ===\n\n");
            
            // Top scenarios
            report.append("🏆 TOP 3 MOST LIKELY FUTURES:\n");
            List<FutureScenario> top3 = allScenarios.stream()
                .sorted((s1, s2) -> Double.compare(s2.getProbability(), s1.getProbability()))
                .limit(3)
                .collect(Collectors.toList());
            
            for (int i = 0; i < top3.size(); i++) {
                FutureScenario scenario = top3.get(i);
                String medal = i == 0 ? "🥇" : i == 1 ? "🥈" : "🥉";
                report.append(String.format("  %s %s: %.1f%% probability\n", 
                    medal, scenario.getName(), scenario.getProbability() * 100));
                report.append(String.format("     %s\n", scenario.getDescription()));
                report.append(String.format("     Momentum: %.2f, Sector: %s\n\n", 
                    scenario.getMomentum(), scenario.getDominantSector()));
            }
            
            // Sector analysis
            report.append("📊 PROBABILITY BY SECTOR:\n");
            sectorProbabilities.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(entry -> report.append(String.format("  %s: %.1f%%\n", 
                    entry.getKey(), entry.getValue() * 100)));
            
            // Key trends
            report.append("\n📈 KEY TRENDS IDENTIFIED:\n");
            for (String trend : keyTrends) {
                report.append("  • ").append(trend).append("\n");
            }
            
            // Recent transitions
            if (!transitions.isEmpty()) {
                report.append("\n🔄 RECENT SCENARIO TRANSITIONS:\n");
                transitions.stream()
                    .skip(Math.max(0, transitions.size() - 5))
                    .forEach(transition -> report.append("  • ").append(transition.toString()).append("\n"));
            }
            
            // Strategic recommendations
            report.append("\n🎯 STRATEGIC RECOMMENDATIONS FOR METROPOLIA:\n");
            for (String recommendation : strategicRecommendations) {
                report.append("  • ").append(recommendation).append("\n");
            }
            
            return report.toString();
        }
    }
    
    // Getters
    public List<FutureScenario> getAllScenarios() { return new ArrayList<>(scenarios); }
    public FutureScenario getCurrentDominantScenario() { return currentDominantScenario; }
    public List<ScenarioTransition> getTransitions() { return new ArrayList<>(transitions); }
    public Map<String, Double> getScenarioProbabilities() { return new HashMap<>(scenarioProbabilities); }
}
