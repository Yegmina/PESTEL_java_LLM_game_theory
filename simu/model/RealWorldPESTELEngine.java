package simu.model;

import simu.framework.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Enhanced PESTEL simulation engine using real-world companies, countries, and research institutions
 * with alternative future scenarios and object-oriented country unions
 */
public class RealWorldPESTELEngine extends Engine {
    private PESTELState globalPESTEL;
    private List<RealWorldCompany> companies;
    private List<RealWorldCountry> countries;
    private List<RealWorldResearcher> researchers;
    private List<CountryUnion> countryUnions;
    private LocalQwenAIService aiService;
    private EnhancedFutureScenarioManager enhancedFutureManager;
    private List<AgentAction> recentActions;
    private List<PESTELChange> recentChanges;
    private int currentDay;
    private int simulationDays;
    private boolean aiEnabled;
    
    /**
     * Country Union class for managing alliances and regional cooperation
     */
    public static class CountryUnion {
        private String name;
        private String type;
        private List<RealWorldCountry> memberCountries;
        private String headquarters;
        private int foundedYear;
        private PESTELState unionPESTEL;
        private double collectiveInfluence;
        
        public CountryUnion(String name, String type, String headquarters, int foundedYear) {
            this.name = name;
            this.type = type;
            this.headquarters = headquarters;
            this.foundedYear = foundedYear;
            this.memberCountries = new ArrayList<>();
            this.unionPESTEL = new PESTELState();
            this.collectiveInfluence = 0.0;
            initializeUnionPESTEL();
        }
        
        private void initializeUnionPESTEL() {
            unionPESTEL.setPolitical("union_governance", 
                String.format("%s provides %s framework for member cooperation", name, type));
            unionPESTEL.setEconomic("collective_economy", 
                String.format("Combined economic power of %s member states", name));
            unionPESTEL.setSocial("cultural_exchange", 
                String.format("Promotes cultural and educational exchange among %s members", name));
        }
        
        public void addMember(RealWorldCountry country) {
            memberCountries.add(country);
            updateCollectiveInfluence();
        }
        
        private void updateCollectiveInfluence() {
            collectiveInfluence = memberCountries.stream()
                    .mapToDouble(RealWorldCountry::getGeopoliticalInfluence)
                    .sum();
        }
        
        public String makeUnionDecision(int currentDay, PESTELState globalPESTEL) {
            if (currentDay % 180 == 0) { // Semi-annual union decisions
                return String.format("%s announces new strategic initiative for enhanced cooperation among member states", name);
            } else if (Math.random() < 0.1) {
                return String.format("%s coordinates response to global challenges affecting member states", name);
            }
            return null;
        }
        
        // Getters
        public String getName() { return name; }
        public List<RealWorldCountry> getMemberCountries() { return new ArrayList<>(memberCountries); }
        public double getCollectiveInfluence() { return collectiveInfluence; }
        public PESTELState getUnionPESTEL() { return unionPESTEL; }
    }
    
    /**
     * Alternative Future Manager for scenario generation
     */
    public static class AlternativeFutureManager {
        private List<FutureScenario> scenarios;
        private FutureScenario currentDominantScenario;
        private Map<String, Double> scenarioProbabilities;
        
        public AlternativeFutureManager() {
            this.scenarios = new ArrayList<>();
            this.scenarioProbabilities = new HashMap<>();
            initializeScenarios();
        }
        
        private void initializeScenarios() {
            for (RealWorldData.FutureScenario scenarioData : RealWorldData.ALTERNATIVE_FUTURES) {
                FutureScenario scenario = new FutureScenario(scenarioData);
                scenarios.add(scenario);
                scenarioProbabilities.put(scenario.getName(), scenario.getProbability());
            }
            updateDominantScenario();
        }
        
        public void updateScenarioProbabilities(List<AgentAction> recentActions, PESTELState globalPESTEL) {
            // Update probabilities based on recent actions and global state
            for (FutureScenario scenario : scenarios) {
                double newProbability = calculateUpdatedProbability(scenario, recentActions, globalPESTEL);
                scenarioProbabilities.put(scenario.getName(), newProbability);
                scenario.setProbability(newProbability);
            }
            
            // Normalize probabilities
            normalizeProbabilities();
            updateDominantScenario();
        }
        
        private double calculateUpdatedProbability(FutureScenario scenario, List<AgentAction> recentActions, PESTELState globalPESTEL) {
            double baseProbability = scenario.getBaseProbability();
            double adjustment = 0.0;
            
            // Adjust based on relevant actions
            for (AgentAction action : recentActions) {
                if (scenario.isRelevantAction(action)) {
                    adjustment += 0.01; // Small positive adjustment
                }
            }
            
            // Adjust based on global PESTEL alignment
            adjustment += scenario.calculatePESTELAlignment(globalPESTEL) * 0.05;
            
            return Math.max(0.01, Math.min(0.8, baseProbability + adjustment));
        }
        
        private void normalizeProbabilities() {
            double total = scenarioProbabilities.values().stream().mapToDouble(Double::doubleValue).sum();
            if (total > 0) {
                scenarioProbabilities.replaceAll((k, v) -> v / total);
            }
        }
        
        private void updateDominantScenario() {
            currentDominantScenario = scenarios.stream()
                    .max(Comparator.comparing(FutureScenario::getProbability))
                    .orElse(scenarios.get(0));
        }
        
        public FutureScenario getCurrentDominantScenario() {
            return currentDominantScenario;
        }
        
        public List<FutureScenario> getAllScenarios() {
            return new ArrayList<>(scenarios);
        }
    }
    
    /**
     * Future Scenario implementation
     */
    public static class FutureScenario {
        private String name;
        private String description;
        private double probability;
        private double baseProbability;
        private String implications;
        
        public FutureScenario(RealWorldData.FutureScenario scenarioData) {
            this.name = scenarioData.name;
            this.description = scenarioData.description;
            this.probability = scenarioData.probability;
            this.baseProbability = scenarioData.probability;
            this.implications = scenarioData.implications;
        }
        
        public boolean isRelevantAction(AgentAction action) {
            String actionLower = action.getActionDescription().toLowerCase();
            String nameLower = name.toLowerCase();
            
            if (nameLower.contains("ai") && actionLower.contains("ai")) return true;
            if (nameLower.contains("green") && actionLower.contains("climate")) return true;
            if (nameLower.contains("geopolitical") && actionLower.contains("alliance")) return true;
            if (nameLower.contains("resource") && actionLower.contains("resource")) return true;
            if (nameLower.contains("digital") && actionLower.contains("digital")) return true;
            
            return false;
        }
        
        public double calculatePESTELAlignment(PESTELState globalPESTEL) {
            double alignment = 0.0;
            
            // Check alignment with global PESTEL factors
            if (name.contains("AI") && globalPESTEL.getTechnological("innovation").contains("AI")) {
                alignment += 0.3;
            }
            if (name.contains("Green") && globalPESTEL.getEnvironmental("climate_change").contains("transition")) {
                alignment += 0.3;
            }
            if (name.contains("Geopolitical") && globalPESTEL.getPolitical("stability").contains("unstable")) {
                alignment += 0.2;
            }
            
            return Math.min(1.0, alignment);
        }
        
        // Getters and setters
        public String getName() { return name; }
        public String getDescription() { return description; }
        public double getProbability() { return probability; }
        public void setProbability(double probability) { this.probability = probability; }
        public double getBaseProbability() { return baseProbability; }
        public String getImplications() { return implications; }
    }
    
    public RealWorldPESTELEngine(int simulationDays) {
        this.globalPESTEL = new PESTELState();
        this.companies = new ArrayList<>();
        this.countries = new ArrayList<>();
        this.researchers = new ArrayList<>();
        this.countryUnions = new ArrayList<>();
        this.recentActions = new ArrayList<>();
        this.recentChanges = new ArrayList<>();
        this.currentDay = 1;
        this.simulationDays = simulationDays;
        this.aiEnabled = false;
        
        // Initialize Local Qwen3-Next AI service
        this.aiService = new LocalQwenAIService();
        this.aiEnabled = aiService.isModelAvailable();
        
        if (aiEnabled) {
            Trace.out(Trace.Level.INFO, "✅ Local Qwen3-Next-80B-A3B-Thinking model initialized");
        } else {
            Trace.out(Trace.Level.WAR, "⚠️ Local Qwen3-Next model not available - using advanced fallback logic");
            Trace.out(Trace.Level.INFO, "💡 To enable local AI: Run 'python setup_local_qwen.py' first");
        }
        
        // Initialize real-world entities
        initializeRealWorldEntities();
        
        // Initialize enhanced future scenarios
        this.enhancedFutureManager = new EnhancedFutureScenarioManager();
    }
    
    private void initializeRealWorldEntities() {
        // Create comprehensive real companies (Top 50)
        for (UltraComprehensiveRealWorldData.CompanyData companyData : UltraComprehensiveRealWorldData.TOP_COMPANIES) {
            companies.add(new RealWorldCompany(companyData));
        }
        
        // Create comprehensive real countries (Top 30)
        for (UltraComprehensiveRealWorldData.CountryData countryData : UltraComprehensiveRealWorldData.TOP_COUNTRIES) {
            countries.add(new RealWorldCountry(countryData));
        }
        
        // Create comprehensive research institutions (Top 25)
        for (UltraComprehensiveRealWorldData.ResearchData researchData : UltraComprehensiveRealWorldData.TOP_RESEARCH_CENTERS) {
            researchers.add(new RealWorldResearcher(researchData));
        }
        
        // Create enhanced country unions
        createComprehensiveCountryUnions();
        
        Trace.out(Trace.Level.INFO, "Initialized Comprehensive Real-World Entities:");
        Trace.out(Trace.Level.INFO, "  - " + companies.size() + " global companies (Top 50)");
        Trace.out(Trace.Level.INFO, "  - " + countries.size() + " influential countries (Top 30)");
        Trace.out(Trace.Level.INFO, "  - " + researchers.size() + " research institutions (Top 25)");
        Trace.out(Trace.Level.INFO, "  - " + countryUnions.size() + " international organizations");
    }
    
    private void createComprehensiveCountryUnions() {
        for (ComprehensiveRealWorldData.CountryUnion unionData : ComprehensiveRealWorldData.COUNTRY_UNIONS) {
            CountryUnion union = new CountryUnion(unionData.name, unionData.type, 
                                                 unionData.headquarters, unionData.foundedYear);
            
            // Add member countries
            for (String memberName : unionData.memberCountries) {
                RealWorldCountry country = findCountryByName(memberName);
                if (country != null) {
                    union.addMember(country);
                }
            }
            
            countryUnions.add(union);
        }
    }
    
    private RealWorldCountry findCountryByName(String name) {
        return countries.stream()
                .filter(country -> country.getCountryData().name.equals(name))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    protected void initialize() {
        Trace.out(Trace.Level.INFO, "=== REAL-WORLD PESTEL SIMULATION INITIALIZATION ===");
        Trace.out(Trace.Level.INFO, "Duration: " + simulationDays + " days");
        Trace.out(Trace.Level.INFO, "AI Model: " + (aiEnabled ? "Qwen3-Next-80B-A3B-Thinking" : "Advanced Fallback Logic"));
        
        Trace.out(Trace.Level.INFO, "\n=== INITIAL GLOBAL PESTEL STATE ===");
        Trace.out(Trace.Level.INFO, globalPESTEL.toString());
        
        Trace.out(Trace.Level.INFO, "\n=== COMPREHENSIVE ALTERNATIVE FUTURE SCENARIOS ===");
        for (EnhancedFutureScenarioManager.FutureScenario scenario : enhancedFutureManager.getAllScenarios()) {
            Trace.out(Trace.Level.INFO, String.format("  %s: %.1f%% - %s", 
                scenario.getName(), scenario.getProbability() * 100, scenario.getDescription()));
        }
        
        // Schedule first day
        eventList.add(new Event(ForecastingEventType.TIME_STEP_ADVANCE, 1.0));
    }
    
    @Override
    protected void runEvent(Event event) {
        ForecastingEventType eventType = (ForecastingEventType) event.getType();
        
        switch (eventType) {
            case TIME_STEP_ADVANCE:
                processDailyRealWorldSimulation();
                break;
            default:
                Trace.out(Trace.Level.WAR, "Unknown event type: " + eventType);
        }
    }
    
    @Override
    protected void tryCEvents() {
        // Process country union decisions
        processCountryUnionDecisions();
        
        // Update alternative future scenarios
        updateAlternativeFutures();
    }
    
    @Override
    protected void results() {
        Trace.out(Trace.Level.INFO, "\n=== REAL-WORLD PESTEL SIMULATION RESULTS ===");
        Trace.out(Trace.Level.INFO, "Simulation completed after " + (currentDay - 1) + " days");
        
        // Display final global state
        Trace.out(Trace.Level.INFO, "\n=== FINAL GLOBAL PESTEL STATE ===");
        Trace.out(Trace.Level.INFO, globalPESTEL.toString());
        
        // Display comprehensive alternative futures analysis
        displayComprehensiveAlternativeFuturesResults();
        
        // Display entity statistics
        displayEntityStatistics();
        
        // Display strategic recommendations
        displayStrategicRecommendations();
    }
    
    /**
     * Process one day of real-world simulation
     */
    private void processDailyRealWorldSimulation() {
        Trace.out(Trace.Level.INFO, "\n========== DAY " + currentDay + " ==========");
        
        // Process companies first (market influence)
        Trace.out(Trace.Level.INFO, "\n--- GLOBAL COMPANIES ---");
        for (RealWorldCompany company : companies) {
            processRealWorldAgent(company);
        }
        
        // Process countries (policy influence)
        Trace.out(Trace.Level.INFO, "\n--- WORLD COUNTRIES ---");
        for (RealWorldCountry country : countries) {
            processRealWorldAgent(country);
        }
        
        // Process researchers (innovation influence)
        Trace.out(Trace.Level.INFO, "\n--- RESEARCH INSTITUTIONS ---");
        for (RealWorldResearcher researcher : researchers) {
            processRealWorldAgent(researcher);
        }
        
        // Clean up old data
        cleanupOldData();
        
        // Schedule next day
        if (currentDay < simulationDays) {
            currentDay++;
            eventList.add(new Event(ForecastingEventType.TIME_STEP_ADVANCE, currentDay));
        }
    }
    
    /**
     * Process real-world agent decision making
     */
    private void processRealWorldAgent(PESTELAgent agent) {
        try {
            // Get AI decision or fallback
            AgentDecision decision = agent.makeDecision(globalPESTEL, currentDay, recentActions);
            
            if (decision == null) {
                Trace.out(Trace.Level.INFO, agent.getAgentId() + ": No action taken");
                return;
            }
            
            Trace.out(Trace.Level.INFO, agent.getAgentId() + " decides: " + decision.getDescription());
            
            // Record action
            AgentAction action = new AgentAction(agent.getAgentId(), currentDay, 
                                               decision.getDescription(), decision.getDecisionType());
            recentActions.add(action);
            
            // Process PESTEL impacts for each category
            processPESTELImpactsForAllCategories(decision.getDescription(), agent.getAgentId());
            
            // Update affected agents
            updateAffectedRealWorldAgents(decision.getDescription(), agent);
            
        } catch (Exception e) {
            Trace.out(Trace.Level.WAR, "Error processing " + agent.getAgentId() + ": " + e.getMessage());
        }
    }
    
    /**
     * Process PESTEL impacts across all 6 categories
     */
    private void processPESTELImpactsForAllCategories(String decision, String agentId) {
        String[] categories = {"political", "economic", "social", "technological", "environmental", "legal"};
        
        for (String category : categories) {
            String impact = analyzePESTELImpact(decision, category, agentId);
            if (!impact.equals("NO_IMPACT")) {
                applyPESTELChange(impact, category, agentId);
            }
        }
    }
    
    /**
     * Analyze PESTEL impact (AI or fallback)
     */
    private String analyzePESTELImpact(String decision, String category, String agentId) {
        // Enhanced fallback logic for real-world entities
        return generateEnhancedFallbackImpact(decision, category, agentId);
    }
    
    private String generateEnhancedFallbackImpact(String decision, String category, String agentId) {
        String decisionLower = decision.toLowerCase();
        
        switch (category.toLowerCase()) {
            case "political":
                if (agentId.contains("United States") || agentId.contains("China")) {
                    if (decisionLower.contains("alliance") || decisionLower.contains("cooperation")) {
                        return String.format("FACTOR:international_relations|NEW_VALUE:%s enhances diplomatic cooperation through strategic partnerships|REASON:Major power diplomatic initiative", agentId);
                    }
                }
                break;
                
            case "economic":
                if (agentId.contains("Walmart") || agentId.contains("Amazon")) {
                    if (decisionLower.contains("invest") || decisionLower.contains("expand")) {
                        return String.format("FACTOR:market_dynamics|NEW_VALUE:%s drives global market transformation through strategic expansion|REASON:Major corporate investment", agentId);
                    }
                }
                break;
                
            case "technological":
                if (agentId.contains("Apple") || agentId.contains("MIT") || agentId.contains("Stanford")) {
                    if (decisionLower.contains("ai") || decisionLower.contains("quantum") || decisionLower.contains("research")) {
                        return String.format("FACTOR:innovation_ecosystem|NEW_VALUE:%s advances global innovation through breakthrough research and development|REASON:Technology leadership", agentId);
                    }
                }
                break;
                
            case "environmental":
                if (decisionLower.contains("green") || decisionLower.contains("climate") || decisionLower.contains("sustainable")) {
                    return String.format("FACTOR:climate_leadership|NEW_VALUE:%s demonstrates environmental leadership through comprehensive sustainability initiatives|REASON:Climate action commitment", agentId);
                }
                break;
                
            case "social":
                if (decisionLower.contains("education") || decisionLower.contains("health") || decisionLower.contains("workforce")) {
                    return String.format("FACTOR:social_development|NEW_VALUE:%s promotes social progress through education and workforce development|REASON:Social investment", agentId);
                }
                break;
                
            case "legal":
                if (decisionLower.contains("regulation") || decisionLower.contains("policy") || decisionLower.contains("governance")) {
                    return String.format("FACTOR:regulatory_framework|NEW_VALUE:%s strengthens regulatory framework through policy innovation|REASON:Governance improvement", agentId);
                }
                break;
        }
        
        return "NO_IMPACT";
    }
    
    /**
     * Apply PESTEL change to global state (avoid duplicate changes)
     */
    private void applyPESTELChange(String impact, String category, String agentId) {
        if (impact.startsWith("FACTOR:")) {
            String[] parts = impact.split("\\|");
            if (parts.length >= 2) {
                String factor = parts[0].substring(7);
                String newValue = parts[1].substring(10);
                String reason = parts.length > 2 ? parts[2].substring(7) : "Strategic decision";
                
                String oldValue = globalPESTEL.getFactor(category, factor);
                
                // Only apply change if it's actually different
                if (!oldValue.equals(newValue)) {
                    globalPESTEL.updateFactor(category, factor, newValue);
                    
                    PESTELChange change = new PESTELChange(category, factor, oldValue, newValue, reason, agentId, currentDay);
                    recentChanges.add(change);
                    
                    Trace.out(Trace.Level.INFO, "  🔄 " + change.toString());
                } else {
                    Trace.out(Trace.Level.INFO, "  ⚪ No change needed for " + category + "." + factor);
                }
            }
        }
    }
    
    /**
     * Update affected real-world agents
     */
    private void updateAffectedRealWorldAgents(String decision, PESTELAgent sourceAgent) {
        // Determine affected agents based on decision type and global influence
        List<PESTELAgent> affectedAgents = determineAffectedAgents(decision, sourceAgent);
        
        for (PESTELAgent agent : affectedAgents) {
            List<PESTELChange> relevantChanges = getRecentChangesForAgent(agent);
            agent.updateFromPESTELChanges(relevantChanges);
            Trace.out(Trace.Level.INFO, "    📡 Updated " + agent.getAgentId());
        }
    }
    
    private List<PESTELAgent> determineAffectedAgents(String decision, PESTELAgent sourceAgent) {
        List<PESTELAgent> affected = new ArrayList<>();
        String decisionLower = decision.toLowerCase();
        
        // Limit affected agents to prevent excessive updates
        int maxAffected = 3;
        
        // Industry/sector-based effects
        if (decisionLower.contains("technology") || decisionLower.contains("ai") || decisionLower.contains("quantum")) {
            // Tech decisions affect tech companies and AI research institutions
            companies.stream()
                .filter(c -> c.getCompanyData().industry.toLowerCase().contains("technology"))
                .limit(1)
                .forEach(affected::add);
            researchers.stream()
                .filter(r -> r.getResearchData().fields.toLowerCase().contains("ai") || 
                           r.getResearchData().name.contains("MIT") || 
                           r.getResearchData().name.contains("Stanford"))
                .limit(2)
                .forEach(affected::add);
        }
        
        if (decisionLower.contains("trade") || decisionLower.contains("alliance") || decisionLower.contains("cooperation")) {
            // Political/economic decisions affect major economies
            countries.stream()
                .filter(c -> c.getEconomicPower() > 0.7)
                .limit(2)
                .forEach(affected::add);
        }
        
        if (decisionLower.contains("sustainable") || decisionLower.contains("climate") || decisionLower.contains("green")) {
            // Environmental decisions affect key players
            companies.stream()
                .filter(c -> c.getCompanyData().industry.toLowerCase().contains("energy"))
                .limit(1)
                .forEach(affected::add);
        }
        
        // Remove source agent and limit total
        affected.removeIf(agent -> agent.getAgentId().equals(sourceAgent.getAgentId()));
        
        return affected.stream().limit(maxAffected).collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Process country union decisions
     */
    private void processCountryUnionDecisions() {
        for (CountryUnion union : countryUnions) {
            String unionDecision = union.makeUnionDecision(currentDay, globalPESTEL);
            if (unionDecision != null) {
                Trace.out(Trace.Level.INFO, "🤝 " + unionDecision);
                
                // Union decisions affect all member countries
                for (RealWorldCountry member : union.getMemberCountries()) {
                    AgentAction unionAction = new AgentAction(union.getName(), currentDay, unionDecision, "union_decision");
                    recentActions.add(unionAction);
                }
            }
        }
    }
    
    /**
     * Update enhanced alternative future scenarios
     */
    private void updateAlternativeFutures() {
        if (currentDay % 7 == 0) { // Weekly scenario updates
            enhancedFutureManager.updateScenarioProbabilities(recentActions, globalPESTEL, currentDay);
            
            EnhancedFutureScenarioManager.FutureScenario dominant = enhancedFutureManager.getCurrentDominantScenario();
            Trace.out(Trace.Level.INFO, String.format("🔮 Dominant Future: %s (%.1f%% probability, %.2f momentum)", 
                dominant.getName(), dominant.getProbability() * 100, dominant.getMomentum()));
        }
    }
    
    private List<PESTELChange> getRecentChangesForAgent(PESTELAgent agent) {
        return recentChanges.stream()
                .filter(change -> change.getDay() >= currentDay - 3)
                .collect(java.util.stream.Collectors.toList());
    }
    
    private void cleanupOldData() {
        recentActions.removeIf(action -> action.getDay() < currentDay - 7);
        recentChanges.removeIf(change -> change.getDay() < currentDay - 7);
    }
    
    private void displayComprehensiveAlternativeFuturesResults() {
        Trace.out(Trace.Level.INFO, "\n=== COMPREHENSIVE ALTERNATIVE FUTURES ANALYSIS ===");
        
        // Generate and display full scenario analysis
        EnhancedFutureScenarioManager.ScenarioAnalysis analysis = enhancedFutureManager.generateScenarioAnalysis();
        Trace.out(Trace.Level.INFO, analysis.generateSummaryReport());
    }
    
    private void displayEntityStatistics() {
        Trace.out(Trace.Level.INFO, "\n=== REAL-WORLD ENTITY STATISTICS ===");
        
        // Company statistics
        Trace.out(Trace.Level.INFO, "\n[GLOBAL COMPANIES]");
        int totalCompanyDecisions = 0;
        for (RealWorldCompany company : companies) {
            totalCompanyDecisions += company.getDecisionCount();
            Trace.out(Trace.Level.INFO, String.format("  %s: %d decisions, Market Influence: %.2f", 
                company.getCompanyData().name, company.getDecisionCount(), company.getMarketInfluence()));
        }
        
        // Country statistics
        Trace.out(Trace.Level.INFO, "\n[WORLD COUNTRIES]");
        int totalCountryDecisions = 0;
        for (RealWorldCountry country : countries) {
            totalCountryDecisions += country.getDecisionCount();
            Trace.out(Trace.Level.INFO, String.format("  %s: %d decisions, Geopolitical Influence: %.2f", 
                country.getCountryData().name, country.getDecisionCount(), country.getGeopoliticalInfluence()));
        }
        
        // Research statistics
        Trace.out(Trace.Level.INFO, "\n[RESEARCH INSTITUTIONS]");
        int totalResearchDecisions = 0;
        for (RealWorldResearcher researcher : researchers) {
            totalResearchDecisions += researcher.getDecisionCount();
            Trace.out(Trace.Level.INFO, String.format("  %s: %d decisions, Research Impact: %.2f", 
                researcher.getResearchData().name, researcher.getDecisionCount(), researcher.getResearchImpact()));
        }
        
        Trace.out(Trace.Level.INFO, String.format("\nTotal Decisions: %d (Companies: %d, Countries: %d, Research: %d)", 
            totalCompanyDecisions + totalCountryDecisions + totalResearchDecisions,
            totalCompanyDecisions, totalCountryDecisions, totalResearchDecisions));
    }
    
    private void displayStrategicRecommendations() {
        Trace.out(Trace.Level.INFO, "\n=== STRATEGIC RECOMMENDATIONS FOR METROPOLIA UNIVERSITY ===");
        
        EnhancedFutureScenarioManager.FutureScenario dominant = enhancedFutureManager.getCurrentDominantScenario();
        
        switch (dominant.getName()) {
            case "AI Dominance":
                Trace.out(Trace.Level.INFO, "🤖 AI DOMINANCE SCENARIO - Recommendations:");
                Trace.out(Trace.Level.INFO, "  • Invest heavily in AI education and research programs");
                Trace.out(Trace.Level.INFO, "  • Partner with tech giants (Apple, Amazon) for AI curriculum development");
                Trace.out(Trace.Level.INFO, "  • Establish AI ethics and governance research center");
                break;
                
            case "Green Transition":
                Trace.out(Trace.Level.INFO, "🌱 GREEN TRANSITION SCENARIO - Recommendations:");
                Trace.out(Trace.Level.INFO, "  • Develop comprehensive sustainability programs");
                Trace.out(Trace.Level.INFO, "  • Partner with energy companies for clean tech research");
                Trace.out(Trace.Level.INFO, "  • Create carbon-neutral campus initiatives");
                break;
                
            case "Geopolitical Fragmentation":
                Trace.out(Trace.Level.INFO, "🌍 GEOPOLITICAL FRAGMENTATION SCENARIO - Recommendations:");
                Trace.out(Trace.Level.INFO, "  • Strengthen international partnerships and exchange programs");
                Trace.out(Trace.Level.INFO, "  • Develop crisis management and resilience capabilities");
                Trace.out(Trace.Level.INFO, "  • Focus on diplomatic and conflict resolution education");
                break;
                
            default:
                Trace.out(Trace.Level.INFO, "📊 MIXED SCENARIO OUTCOMES - Recommendations:");
                Trace.out(Trace.Level.INFO, "  • Maintain flexible strategic planning approach");
                Trace.out(Trace.Level.INFO, "  • Diversify partnerships across multiple sectors");
                Trace.out(Trace.Level.INFO, "  • Strengthen adaptive capacity and scenario planning");
        }
    }
    
    // Getters
    public PESTELState getGlobalPESTEL() { return globalPESTEL; }
    public List<RealWorldCompany> getCompanies() { return new ArrayList<>(companies); }
    public List<RealWorldCountry> getCountries() { return new ArrayList<>(countries); }
    public List<RealWorldResearcher> getResearchers() { return new ArrayList<>(researchers); }
    public List<CountryUnion> getCountryUnions() { return new ArrayList<>(countryUnions); }
    public EnhancedFutureScenarioManager getEnhancedFutureManager() { return enhancedFutureManager; }
    public List<PESTELChange> getRecentChanges() { return new ArrayList<>(recentChanges); }
    public List<AgentAction> getRecentActions() { return new ArrayList<>(recentActions); }
    public boolean isAIEnabled() { return aiEnabled; }
}
