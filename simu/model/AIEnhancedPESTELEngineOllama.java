package simu.model;

import simu.framework.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AI-Enhanced PESTEL Simulation Engine with True AI Integration
 * Features 100 companies, 50 countries, 40 research centers, 30-day simulation
 * Real AI decision making and comprehensive agent-to-agent interactions
 */
public class AIEnhancedPESTELEngineOllama extends Engine {
    private PESTELState globalPESTEL;
    private List<RealWorldCompany> companies;
    private List<RealWorldCountry> countries;
    private List<RealWorldResearcher> researchers;
    private List<String> countryUnionNames;
    private LocalOllamaAIService aiService;
    private EnhancedFutureScenarioManager enhancedFutureManager;
    private List<AgentAction> recentActions;
    private List<PESTELChange> recentChanges;
    private Map<String, List<String>> agentConnections; // Agent influence network
    private int currentDay;
    private int simulationDays;
    private boolean aiEnabled;
    private Random random;
    
    // Enhanced tracking
    private Map<String, Double> agentInfluenceScores;
    private Map<String, List<AgentDecision>> dailyDecisions;
    private List<CrossAgentInteraction> crossAgentInteractions;
    
    public AIEnhancedPESTELEngineOllama(int simulationDays) {
        super();
        this.simulationDays = simulationDays;
        this.currentDay = 0;
        this.random = new Random();
        
        // Initialize collections
        this.companies = new ArrayList<>();
        this.countries = new ArrayList<>();
        this.researchers = new ArrayList<>();
        this.countryUnionNames = new ArrayList<>();
        this.recentActions = new ArrayList<>();
        this.recentChanges = new ArrayList<>();
        this.agentConnections = new HashMap<>();
        this.agentInfluenceScores = new HashMap<>();
        this.dailyDecisions = new HashMap<>();
        this.crossAgentInteractions = new ArrayList<>();
        
        // Initialize global PESTEL state with enhanced variables
        this.globalPESTEL = new PESTELState();
        initializeEnhancedGlobalPESTEL();
        
        // Initialize AI service
        initializeAIService();
        
        // Initialize comprehensive real-world entities
        initializeUltraComprehensiveEntities();
        
        // Build agent influence network
        buildAgentInfluenceNetwork();
        
        // Initialize enhanced future scenarios
        this.enhancedFutureManager = new EnhancedFutureScenarioManager();
        
        Trace.out(Trace.Level.INFO, "=== AI-ENHANCED PESTEL SIMULATION INITIALIZATION ===");
        Trace.out(Trace.Level.INFO, "Duration: " + simulationDays + " days");
	Trace.out(Trace.Level.INFO, "AI Model: " + (aiEnabled ? aiService.getCurrentModel() : "Advanced Fallback Logic"));
        
        displayInitialConfiguration();
    }
    
	private void initializeAIService() {
	    try {
		this.aiService = new LocalOllamaAIService();
		this.aiEnabled = aiService.isOllamaAvailable();
		if (aiEnabled) {
		    Trace.out(Trace.Level.INFO, "✅ Ollama service connected successfully with model: " + aiService.getCurrentModel());
		} else {
		    Trace.out(Trace.Level.WAR, "⚠️ Ollama service not available - using advanced fallback logic");
		    Trace.out(Trace.Level.INFO, "💡 To enable AI: Install and run Ollama, then pull a model like 'gemma3:4b'");
		}
	    } catch (Exception e) {
		this.aiEnabled = false;
		Trace.out(Trace.Level.WAR, "⚠️ Ollama service unavailable, using advanced fallback logic: " + e.getMessage());
	    }
	}    
    private void initializeEnhancedGlobalPESTEL() {
        // Initialize with enhanced PESTEL variables
        Map<String, String[]> variables = UltraComprehensiveRealWorldData.ENHANCED_PESTEL_VARIABLES;
        
        for (Map.Entry<String, String[]> category : variables.entrySet()) {
            String categoryName = category.getKey();
            for (String variable : category.getValue()) {
                String initialValue = generateInitialPESTELValue(categoryName, variable);
                globalPESTEL.updateFactor(categoryName, variable, initialValue);
            }
        }
        
        // globalPESTEL.setLastUpdate(0.0); // Not needed
    }
    
    private String generateInitialPESTELValue(String category, String variable) {
        // Generate realistic initial values for each PESTEL variable
        switch (category) {
            case "political":
                return generatePoliticalValue(variable);
            case "economic":
                return generateEconomicValue(variable);
            case "social":
                return generateSocialValue(variable);
            case "technological":
                return generateTechnologicalValue(variable);
            case "environmental":
                return generateEnvironmentalValue(variable);
            case "legal":
                return generateLegalValue(variable);
            default:
                return "Not defined";
        }
    }
    
    private String generatePoliticalValue(String variable) {
        switch (variable) {
            case "tax_labour": return "Corporate tax rate 25%, flexible labor laws";
            case "trade": return "Open trade policies with moderate protectionism";
            case "policies_laws": return "Stable regulatory environment with periodic updates";
            case "corruption": return "Low-medium corruption index, improving transparency";
            case "stability": return "Stable democratic institutions with periodic tensions";
            case "international_relations": return "Multipolar cooperation with strategic competition";
            case "regulatory_environment": return "Balanced regulation supporting innovation and protection";
            case "government_spending": return "Moderate fiscal policy with infrastructure focus";
            case "political_risk": return "Low-medium political risk with election cycles";
            case "diplomatic_relations": return "Active multilateral engagement and alliance building";
            case "sovereignty_issues": return "Strong national sovereignty with international cooperation";
            default: return "Moderate political stability";
        }
    }
    
    private String generateEconomicValue(String variable) {
        switch (variable) {
            case "income": return "Median household income €52,000, growing middle class";
            case "interest_rates": return "Central bank rate 2.5%, moderate monetary policy";
            case "growth": return "GDP growth 3.1% annually, steady expansion";
            case "employment": return "Unemployment 5.8%, recovering job market";
            case "inflation": return "Inflation rate 2.8%, within acceptable range";
            case "market_dynamics": return "Dynamic markets with technological disruption";
            case "currency_stability": return "Stable currency with moderate volatility";
            case "trade_balance": return "Balanced trade with slight export surplus";
            case "investment_climate": return "Favorable investment climate with regulatory clarity";
            case "economic_inequality": return "Moderate inequality with social mobility programs";
            case "debt_levels": return "Manageable debt levels with fiscal discipline";
            default: return "Stable economic conditions";
        }
    }
    
    private String generateSocialValue(String variable) {
        switch (variable) {
            case "ageing": return "28% population over 65, increasing healthcare needs";
            case "career_views": return "Strong work-life balance emphasis, flexible careers";
            case "cultural_barriers": return "Multicultural society with integration challenges";
            case "population": return "Population 5.8M, stable demographics with immigration";
            case "lifestyle": return "Health-conscious, environmentally aware, digitally connected";
            case "social_development": return "High human development index, quality education";
            case "education_levels": return "85% tertiary education, lifelong learning focus";
            case "health_consciousness": return "High health awareness, preventive healthcare";
            case "social_mobility": return "Good social mobility with merit-based advancement";
            case "demographic_trends": return "Aging population, declining birth rates, urbanization";
            case "cultural_values": return "Progressive values, environmental consciousness, equality";
            default: return "Progressive social development";
        }
    }
    
    private String generateTechnologicalValue(String variable) {
        switch (variable) {
            case "innovation": return "High innovation index, strong startup ecosystem";
            case "automation": return "Advanced automation adoption, human-AI collaboration";
            case "technology_incentives": return "Government R&D tax credits, innovation grants";
            case "rd_activity": return "3.8% GDP invested in R&D, university-industry partnerships";
            case "innovation_ecosystem": return "Thriving innovation ecosystem with global connections";
            case "digital_infrastructure": return "Advanced 5G/6G infrastructure, fiber optic networks";
            case "ai_adoption": return "Rapid AI adoption across sectors, ethical AI frameworks";
            case "cybersecurity_maturity": return "Advanced cybersecurity capabilities, threat resilience";
            case "tech_talent_availability": return "Strong tech talent pool, international recruitment";
            case "research_collaboration": return "Extensive international research collaboration";
            default: return "Advanced technological development";
        }
    }
    
    private String generateEnvironmentalValue(String variable) {
        switch (variable) {
            case "climate_change": return "Carbon neutral by 2030 goal, renewable transition";
            case "recycling_disposal": return "90% recycling rate, circular economy principles";
            case "ethical": return "Strong environmental ethics, corporate sustainability";
            case "sustainability": return "Comprehensive sustainability focus, green innovation";
            case "climate_leadership": return "Global climate leadership, international cooperation";
            case "resource_availability": return "Sustainable resource management, efficiency focus";
            case "pollution_levels": return "Low pollution levels, strict environmental standards";
            case "biodiversity_status": return "Protected biodiversity, ecosystem restoration";
            case "renewable_energy_adoption": return "75% renewable energy, grid modernization";
            case "environmental_regulations": return "Comprehensive environmental regulations, enforcement";
            default: return "Strong environmental performance";
        }
    }
    
    private String generateLegalValue(String variable) {
        switch (variable) {
            case "data_protection": return "GDPR+ compliance, advanced privacy protection";
            case "antitrust": return "Strong competition laws, tech platform regulation";
            case "health_safety": return "Comprehensive health and safety standards";
            case "copyright": return "Robust IP protection, digital rights enforcement";
            case "labour": return "Progressive labor laws, worker protection, flexibility";
            case "regulatory_framework": return "Adaptive regulatory framework, innovation-friendly";
            case "intellectual_property": return "Strong IP protection, innovation incentives";
            case "contract_enforcement": return "Reliable contract enforcement, legal certainty";
            case "judicial_independence": return "Independent judiciary, rule of law";
            case "legal_transparency": return "Transparent legal processes, access to justice";
            case "compliance_requirements": return "Clear compliance requirements, business support";
            default: return "Strong legal framework";
        }
    }
    
    private void initializeUltraComprehensiveEntities() {
        // Create ultra-comprehensive companies (100 companies)
        for (UltraComprehensiveRealWorldData.CompanyData companyData : UltraComprehensiveRealWorldData.TOP_COMPANIES) {
            companies.add(new RealWorldCompany(companyData));
        }
        
        // Create ultra-comprehensive countries (50 countries)
        for (UltraComprehensiveRealWorldData.CountryData countryData : UltraComprehensiveRealWorldData.TOP_COUNTRIES) {
            countries.add(new RealWorldCountry(countryData));
        }
        
        // Create ultra-comprehensive research institutions (40 institutions)
        for (UltraComprehensiveRealWorldData.ResearchData researchData : UltraComprehensiveRealWorldData.TOP_RESEARCH_CENTERS) {
            researchers.add(new RealWorldResearcher(researchData));
        }
        
        // Create enhanced country unions (using existing data)
        createEnhancedCountryUnions();
        
        Trace.out(Trace.Level.INFO, "Initialized Ultra-Comprehensive Real-World Entities:");
        Trace.out(Trace.Level.INFO, "  - " + companies.size() + " global companies (Top 100)");
        Trace.out(Trace.Level.INFO, "  - " + countries.size() + " influential countries (Top 50)");
        Trace.out(Trace.Level.INFO, "  - " + researchers.size() + " research institutions (Top 40)");
        Trace.out(Trace.Level.INFO, "  - " + countryUnionNames.size() + " international organizations");
    }
    
    private void createEnhancedCountryUnions() {
        // Use existing comprehensive country unions data
        for (ComprehensiveRealWorldData.CountryUnion unionData : ComprehensiveRealWorldData.COUNTRY_UNIONS) {
            countryUnionNames.add(unionData.name);
        }
    }
    
    private void buildAgentInfluenceNetwork() {
        // Build comprehensive agent-to-agent influence network
        for (RealWorldCompany company : companies) {
            List<String> influences = new ArrayList<>();
            
            // Companies influence other companies in same industry
            companies.stream()
                .filter(c -> !c.equals(company) && 
                           c.getCompanyData().industry.equals(company.getCompanyData().industry))
                .limit(3)
                .forEach(c -> influences.add(c.getAgentId()));
            
            // Companies influence countries where they operate
            countries.stream()
                .filter(country -> country.getCountryData().name.equals(company.getCompanyData().country))
                .forEach(country -> influences.add(country.getAgentId()));
            
            // Tech companies influence research institutions
            if (company.getCompanyData().industry.toLowerCase().contains("technology")) {
                researchers.stream()
                    .filter(r -> r.getResearchData().fields.toLowerCase().contains("technology") ||
                               r.getResearchData().fields.toLowerCase().contains("ai"))
                    .limit(2)
                    .forEach(r -> influences.add(r.getAgentId()));
            }
            
            agentConnections.put(company.getAgentId(), influences);
        }
        
        // Build country influence networks
        for (RealWorldCountry country : countries) {
            List<String> influences = new ArrayList<>();
            
            // Countries influence companies based in their territory
            companies.stream()
                .filter(c -> c.getCompanyData().country.equals(country.getCountryData().name))
                .forEach(c -> influences.add(c.getAgentId()));
            
            // Countries influence research institutions in their territory
            researchers.stream()
                .filter(r -> r.getResearchData().country.equals(country.getCountryData().name))
                .forEach(r -> influences.add(r.getAgentId()));
            
            // Regional influence between countries
            countries.stream()
                .filter(c -> !c.equals(country) && 
                           c.getCountryData().region.equals(country.getCountryData().region))
                .limit(3)
                .forEach(c -> influences.add(c.getAgentId()));
            
            agentConnections.put(country.getAgentId(), influences);
        }
        
        // Build research institution networks
        for (RealWorldResearcher researcher : researchers) {
            List<String> influences = new ArrayList<>();
            
            // Research institutions influence companies in related fields
            companies.stream()
                .filter(c -> isRelatedField(researcher.getResearchData().fields, c.getCompanyData().industry))
                .limit(2)
                .forEach(c -> influences.add(c.getAgentId()));
            
            // Research institutions influence their host countries
            countries.stream()
                .filter(country -> country.getCountryData().name.equals(researcher.getResearchData().country))
                .forEach(country -> influences.add(country.getAgentId()));
            
            agentConnections.put(researcher.getAgentId(), influences);
        }
        
        Trace.out(Trace.Level.INFO, "Built agent influence network with " + 
                 agentConnections.values().stream().mapToInt(List::size).sum() + " connections");
    }
    
    private boolean isRelatedField(String researchFields, String industry) {
        String fieldsLower = researchFields.toLowerCase();
        String industryLower = industry.toLowerCase();
        
        if (fieldsLower.contains("technology") && industryLower.contains("technology")) return true;
        if (fieldsLower.contains("medicine") && industryLower.contains("healthcare")) return true;
        if (fieldsLower.contains("engineering") && industryLower.contains("automotive")) return true;
        if (fieldsLower.contains("ai") && industryLower.contains("technology")) return true;
        if (fieldsLower.contains("economics") && industryLower.contains("financial")) return true;
        
        return false;
    }
    
    private RealWorldCountry findCountryByName(String name) {
        return countries.stream()
                .filter(country -> country.getCountryData().name.equals(name))
                .findFirst()
                .orElse(null);
    }
    
    private void displayInitialConfiguration() {
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
    public void runEvent(Event event) {
        currentDay = (int) event.getTime();
        
        Trace.out(Trace.Level.INFO, "\n========== DAY " + currentDay + " ==========\n");
        
        // Process daily simulation
        processDailySimulation();
        
        // Schedule next day if within simulation time
        if (currentDay < simulationDays) {
            eventList.add(new Event(ForecastingEventType.TIME_STEP_ADVANCE, currentDay + 1.0));
        }
    }
    
    private void processDailySimulation() {
        dailyDecisions.clear();
        
        // Process all companies with AI decision making
        Trace.out(Trace.Level.INFO, "--- GLOBAL COMPANIES (AI-DRIVEN) ---");
        for (RealWorldCompany company : companies) {
            processAIEnhancedAgentDecision(company);
        }
        
        // Process all countries with AI decision making
        Trace.out(Trace.Level.INFO, "\n--- WORLD COUNTRIES (AI-DRIVEN) ---");
        for (RealWorldCountry country : countries) {
            processAIEnhancedAgentDecision(country);
        }
        
        // Process all research institutions with AI decision making
        Trace.out(Trace.Level.INFO, "\n--- RESEARCH INSTITUTIONS (AI-DRIVEN) ---");
        for (RealWorldResearcher researcher : researchers) {
            processAIEnhancedAgentDecision(researcher);
        }
        
        // Update alternative futures based on all decisions
        updateEnhancedAlternativeFutures();
        
        // Clean up old data
        cleanupOldData();
    }
    
    private void processAIEnhancedAgentDecision(PESTELAgent agent) {
        try {
            // Get AI-driven decision
            AgentDecision decision = getAIEnhancedDecision(agent);
            
            if (decision == null) {
                Trace.out(Trace.Level.INFO, agent.getAgentId() + ": No action taken");
                return;
            }
            
            Trace.out(Trace.Level.INFO, agent.getAgentId() + " decides: " + decision.getDescription());
            
            // Record the decision
            dailyDecisions.computeIfAbsent(agent.getAgentId(), k -> new ArrayList<>()).add(decision);
            
            // Create agent action
            AgentAction action = new AgentAction(agent.getAgentId(), currentDay, 
                                               decision.getDescription(), decision.getDecisionType());
            recentActions.add(action);
            
            // Process comprehensive PESTEL impacts
            processComprehensivePESTELImpacts(decision, agent);
            
            // Process cross-agent interactions
            processCrossAgentInteractions(decision, agent);
            
        } catch (Exception e) {
            Trace.out(Trace.Level.WAR, "Error processing " + agent.getAgentId() + ": " + e.getMessage());
        }
    }
    
    private AgentDecision getAIEnhancedDecision(PESTELAgent agent) {
        if (aiEnabled) {
            return getAIDecision(agent);
        } else {
            return getEnhancedFallbackDecision(agent);
        }
    }
    
    private AgentDecision getAIDecision(PESTELAgent agent) {
        try {
            // Build comprehensive context for AI
            String context = buildAIContext(agent);
            
            // Get AI decision
            String prompt = String.format(
                "You are %s on day %d of a 30-day global simulation. " +
                "Context: %s\n\n" +
                "Current global PESTEL state: %s\n\n" +
                "Your recent actions: %s\n\n" +
                "Should you take an action today? If yes, provide a specific, realistic strategic decision. " +
                "If no, respond with 'NO_ACTION'. " +
                "Format: ACTION_TYPE|DESCRIPTION|CONFIDENCE(0.0-1.0)",
                agent.getAgentId(), currentDay, context,
                getRelevantPESTELState(agent),
                getRecentActionsForAgent(agent)
            );
            
            String aiResponse = aiService.analyzeDecision(prompt, "DECISION_ANALYSIS");
            return parseAIDecision(aiResponse, agent);
            
        } catch (Exception e) {
            Trace.out(Trace.Level.WAR, "AI decision failed for " + agent.getAgentId() + ": " + e.getMessage());
            return getEnhancedFallbackDecision(agent);
        }
    }
    
    private String buildAIContext(PESTELAgent agent) {
        StringBuilder context = new StringBuilder();
        
        if (agent instanceof RealWorldCompany) {
            RealWorldCompany company = (RealWorldCompany) agent;
            context.append(String.format("Company: %s, Revenue: $%.0fB, Industry: %s, Country: %s. ",
                company.getCompanyData().name, company.getCompanyData().revenue / 1000,
                company.getCompanyData().industry, company.getCompanyData().country));
        } else if (agent instanceof RealWorldCountry) {
            RealWorldCountry country = (RealWorldCountry) agent;
            context.append(String.format("Country: %s, GDP: $%.0fB, Population: %dM, Region: %s. ",
                country.getCountryData().name, country.getCountryData().gdp / 1000,
                country.getCountryData().population / 1000000, country.getCountryData().region));
        } else if (agent instanceof RealWorldResearcher) {
            RealWorldResearcher researcher = (RealWorldResearcher) agent;
            context.append(String.format("Research Institution: %s, Fields: %s, Country: %s. ",
                researcher.getResearchData().name, researcher.getResearchData().fields,
                researcher.getResearchData().country));
        }
        
        // Add influence network context
        List<String> influences = agentConnections.get(agent.getAgentId());
        if (influences != null && !influences.isEmpty()) {
            context.append("Connected to: ").append(String.join(", ", influences.subList(0, Math.min(3, influences.size())))).append(". ");
        }
        
        return context.toString();
    }
    
    private String getRelevantPESTELState(PESTELAgent agent) {
        // Return relevant PESTEL factors based on agent type
        StringBuilder relevant = new StringBuilder();
        
        if (agent instanceof RealWorldCompany) {
            relevant.append("Economic: ").append(globalPESTEL.getEconomic("market_dynamics")).append(". ");
            relevant.append("Technological: ").append(globalPESTEL.getTechnological("innovation_ecosystem")).append(". ");
        } else if (agent instanceof RealWorldCountry) {
            relevant.append("Political: ").append(globalPESTEL.getPolitical("international_relations")).append(". ");
            relevant.append("Economic: ").append(globalPESTEL.getEconomic("growth")).append(". ");
        } else if (agent instanceof RealWorldResearcher) {
            relevant.append("Technological: ").append(globalPESTEL.getTechnological("rd_activity")).append(". ");
            relevant.append("Social: ").append(globalPESTEL.getSocial("education_levels")).append(". ");
        }
        
        return relevant.toString();
    }
    
    private String getRecentActionsForAgent(PESTELAgent agent) {
        return recentActions.stream()
            .filter(action -> action.getAgentId().equals(agent.getAgentId()))
            .limit(3)
            .map(AgentAction::getActionDescription)
            .collect(Collectors.joining("; "));
    }
    
    private AgentDecision parseAIDecision(String aiResponse, PESTELAgent agent) {
        if (aiResponse == null || aiResponse.trim().equals("NO_ACTION")) {
            return null;
        }
        
        try {
            String[] parts = aiResponse.split("\\|");
            if (parts.length >= 3) {
                String actionType = parts[0].trim();
                String description = parts[1].trim();
                double confidence = Double.parseDouble(parts[2].trim());
                
                return new AgentDecision(agent.getAgentId(), currentDay, description, actionType, confidence);
            }
        } catch (Exception e) {
            Trace.out(Trace.Level.WAR, "Failed to parse AI decision: " + aiResponse);
        }
        
        // Fallback to simple decision
        return new AgentDecision(agent.getAgentId(), currentDay, 
                               "Strategic decision based on current conditions", "STRATEGIC", 0.7);
    }
    
    private AgentDecision getEnhancedFallbackDecision(PESTELAgent agent) {
        // Enhanced fallback logic with realistic decision patterns
        double actionProbability = calculateActionProbability(agent);
        
        if (random.nextDouble() > actionProbability) {
            return null; // No action
        }
        
        String decision = generateRealisticDecision(agent);
        String decisionType = determineDecisionType(decision);
        double confidence = 0.6 + random.nextDouble() * 0.3;
        
        return new AgentDecision(agent.getAgentId(), currentDay, decision, decisionType, confidence);
    }
    
    private double calculateActionProbability(PESTELAgent agent) {
        double baseProbability = 0.3;
        
        // Increase probability based on agent type and current conditions
        if (agent instanceof RealWorldCompany) {
            RealWorldCompany company = (RealWorldCompany) agent;
            baseProbability += company.getMarketInfluence() * 0.2;
        } else if (agent instanceof RealWorldCountry) {
            RealWorldCountry country = (RealWorldCountry) agent;
            baseProbability += country.getGeopoliticalInfluence() * 0.15;
        } else if (agent instanceof RealWorldResearcher) {
            RealWorldResearcher researcher = (RealWorldResearcher) agent;
            baseProbability += researcher.getResearchImpact() * 0.1;
        }
        
        // Adjust based on recent activity
        long recentActions = this.recentActions.stream()
            .filter(action -> action.getAgentId().equals(agent.getAgentId()) && 
                            action.getDay() >= currentDay - 3)
            .count();
        
        if (recentActions > 2) {
            baseProbability *= 0.7; // Reduce probability if very active recently
        }
        
        return Math.min(0.8, baseProbability);
    }
    
    private String generateRealisticDecision(PESTELAgent agent) {
        List<String> decisions = new ArrayList<>();
        
        if (agent instanceof RealWorldCompany) {
            RealWorldCompany company = (RealWorldCompany) agent;
            String industry = company.getCompanyData().industry.toLowerCase();
            
            if (industry.contains("technology")) {
                decisions.addAll(Arrays.asList(
                    "Launch AI-powered product innovation initiative",
                    "Expand quantum computing research partnerships",
                    "Implement advanced cybersecurity infrastructure",
                    "Develop sustainable technology solutions"
                ));
            } else if (industry.contains("energy")) {
                decisions.addAll(Arrays.asList(
                    "Accelerate renewable energy portfolio expansion",
                    "Invest in carbon capture and storage technology",
                    "Develop green hydrogen production facilities",
                    "Launch energy efficiency optimization program"
                ));
            } else if (industry.contains("healthcare")) {
                decisions.addAll(Arrays.asList(
                    "Expand precision medicine research programs",
                    "Develop AI-assisted diagnostic tools",
                    "Launch global health access initiatives",
                    "Invest in biotechnology innovation"
                ));
            } else {
                decisions.addAll(Arrays.asList(
                    "Launch sustainability transformation initiative",
                    "Expand international market presence",
                    "Implement advanced analytics and automation",
                    "Develop strategic partnerships for growth"
                ));
            }
        } else if (agent instanceof RealWorldCountry) {
            RealWorldCountry country = (RealWorldCountry) agent;
            String region = country.getCountryData().region.toLowerCase();
            
            decisions.addAll(Arrays.asList(
                "Launch national digital transformation program",
                "Strengthen international cooperation agreements",
                "Implement comprehensive climate action plan",
                "Expand education and research investment",
                "Develop advanced infrastructure projects"
            ));
        } else if (agent instanceof RealWorldResearcher) {
            RealWorldResearcher researcher = (RealWorldResearcher) agent;
            String fields = researcher.getResearchData().fields.toLowerCase();
            
            if (fields.contains("ai") || fields.contains("technology")) {
                decisions.addAll(Arrays.asList(
                    "Establish AI ethics and safety research center",
                    "Launch quantum computing breakthrough program",
                    "Develop human-AI collaboration frameworks",
                    "Create technology transfer innovation hub"
                ));
            } else {
                decisions.addAll(Arrays.asList(
                    "Launch interdisciplinary research collaboration",
                    "Establish international research partnership",
                    "Develop innovation commercialization program",
                    "Create graduate fellowship excellence program"
                ));
            }
        }
        
        return decisions.get(random.nextInt(decisions.size()));
    }
    
    private String determineDecisionType(String decision) {
        String decisionLower = decision.toLowerCase();
        
        if (decisionLower.contains("research") || decisionLower.contains("innovation")) return "RESEARCH";
        if (decisionLower.contains("partnership") || decisionLower.contains("cooperation")) return "PARTNERSHIP";
        if (decisionLower.contains("investment") || decisionLower.contains("expand")) return "INVESTMENT";
        if (decisionLower.contains("launch") || decisionLower.contains("develop")) return "DEVELOPMENT";
        if (decisionLower.contains("sustainability") || decisionLower.contains("climate")) return "SUSTAINABILITY";
        
        return "STRATEGIC";
    }
    
    private void processComprehensivePESTELImpacts(AgentDecision decision, PESTELAgent agent) {
        // Process impacts across all enhanced PESTEL categories
        Map<String, String[]> variables = UltraComprehensiveRealWorldData.ENHANCED_PESTEL_VARIABLES;
        
        for (Map.Entry<String, String[]> category : variables.entrySet()) {
            String categoryName = category.getKey();
            String impact = analyzeEnhancedPESTELImpact(decision, categoryName, agent);
            
            if (!impact.equals("NO_IMPACT")) {
                applyEnhancedPESTELChange(impact, categoryName, agent.getAgentId());
            }
        }
    }
    
    private String analyzeEnhancedPESTELImpact(AgentDecision decision, String category, PESTELAgent agent) {
        if (aiEnabled) {
            return getAIPESTELAnalysis(decision, category, agent);
        } else {
            return getEnhancedFallbackPESTELImpact(decision, category, agent);
        }
    }
    
    private String getAIPESTELAnalysis(AgentDecision decision, String category, PESTELAgent agent) {
        try {
            String prompt = String.format(
                "Analyze the PESTEL impact of this decision in the %s category:\n" +
                "Agent: %s\n" +
                "Decision: %s\n" +
                "Current %s state: %s\n\n" +
                "How does this decision specifically affect the %s category? " +
                "Respond with a specific variable and new value in format: " +
                "VARIABLE:variable_name|VALUE:new_description|REASON:explanation " +
                "or NO_IMPACT if no significant impact.",
                category, agent.getAgentId(), decision.getDescription(),
                category, getCurrentCategoryState(category),
                category
            );
            
            return aiService.analyzeDecision(prompt, "PESTEL_ANALYSIS");
        } catch (Exception e) {
            return getEnhancedFallbackPESTELImpact(decision, category, agent);
        }
    }
    
    private String getCurrentCategoryState(String category) {
        // Get current state summary for the category
        StringBuilder state = new StringBuilder();
        String[] variables = UltraComprehensiveRealWorldData.ENHANCED_PESTEL_VARIABLES.get(category);
        
        if (variables != null) {
            for (String variable : Arrays.copyOf(variables, Math.min(3, variables.length))) {
                String value = globalPESTEL.getFactor(category, variable);
                state.append(variable).append(": ").append(value).append(". ");
            }
        }
        
        return state.toString();
    }
    
    private String getEnhancedFallbackPESTELImpact(AgentDecision decision, String category, PESTELAgent agent) {
        String decisionLower = decision.getDescription().toLowerCase();
        String agentType = agent.getClass().getSimpleName();
        
        // Enhanced fallback logic based on decision content and agent type
        switch (category) {
            case "political":
                return analyzeEnhancedPoliticalImpact(decisionLower, agentType, agent);
            case "economic":
                return analyzeEnhancedEconomicImpact(decisionLower, agentType, agent);
            case "social":
                return analyzeEnhancedSocialImpact(decisionLower, agentType, agent);
            case "technological":
                return analyzeEnhancedTechnologicalImpact(decisionLower, agentType, agent);
            case "environmental":
                return analyzeEnhancedEnvironmentalImpact(decisionLower, agentType, agent);
            case "legal":
                return analyzeEnhancedLegalImpact(decisionLower, agentType, agent);
            default:
                return "NO_IMPACT";
        }
    }
    
    private String analyzeEnhancedPoliticalImpact(String decision, String agentType, PESTELAgent agent) {
        if (agentType.equals("RealWorldCountry")) {
            if (decision.contains("cooperation") || decision.contains("alliance")) {
                return "VARIABLE:international_relations|VALUE:" + agent.getAgentId() + 
                       " strengthens diplomatic cooperation through strategic partnerships|REASON:International cooperation initiative";
            }
            if (decision.contains("regulation") || decision.contains("policy")) {
                return "VARIABLE:regulatory_environment|VALUE:" + agent.getAgentId() + 
                       " enhances regulatory framework for innovation|REASON:Policy reform initiative";
            }
        }
        return "NO_IMPACT";
    }
    
    private String analyzeEnhancedEconomicImpact(String decision, String agentType, PESTELAgent agent) {
        if (agentType.equals("RealWorldCompany")) {
            if (decision.contains("investment") || decision.contains("expansion")) {
                return "VARIABLE:investment_climate|VALUE:" + agent.getAgentId() + 
                       " drives economic growth through strategic investment|REASON:Major corporate investment";
            }
            if (decision.contains("market") || decision.contains("global")) {
                return "VARIABLE:market_dynamics|VALUE:" + agent.getAgentId() + 
                       " transforms market dynamics through innovation|REASON:Market expansion strategy";
            }
        }
        return "NO_IMPACT";
    }
    
    private String analyzeEnhancedSocialImpact(String decision, String agentType, PESTELAgent agent) {
        if (decision.contains("education") || decision.contains("training") || decision.contains("development")) {
            return "VARIABLE:social_development|VALUE:" + agent.getAgentId() + 
                   " promotes social progress through education and workforce development|REASON:Human development initiative";
        }
        if (decision.contains("health") || decision.contains("healthcare")) {
            return "VARIABLE:health_consciousness|VALUE:" + agent.getAgentId() + 
                   " advances public health through healthcare innovation|REASON:Health improvement initiative";
        }
        return "NO_IMPACT";
    }
    
    private String analyzeEnhancedTechnologicalImpact(String decision, String agentType, PESTELAgent agent) {
        if (decision.contains("ai") || decision.contains("technology") || decision.contains("innovation")) {
            return "VARIABLE:innovation_ecosystem|VALUE:" + agent.getAgentId() + 
                   " advances global innovation through breakthrough technology development|REASON:Technology leadership";
        }
        if (decision.contains("research") || decision.contains("development")) {
            return "VARIABLE:rd_activity|VALUE:" + agent.getAgentId() + 
                   " enhances R&D capabilities through strategic research investment|REASON:Research investment";
        }
        return "NO_IMPACT";
    }
    
    private String analyzeEnhancedEnvironmentalImpact(String decision, String agentType, PESTELAgent agent) {
        if (decision.contains("climate") || decision.contains("sustainability") || decision.contains("renewable")) {
            return "VARIABLE:climate_leadership|VALUE:" + agent.getAgentId() + 
                   " demonstrates environmental leadership through comprehensive sustainability initiatives|REASON:Climate action commitment";
        }
        if (decision.contains("green") || decision.contains("environment")) {
            return "VARIABLE:environmental_regulations|VALUE:" + agent.getAgentId() + 
                   " strengthens environmental standards through green initiatives|REASON:Environmental protection";
        }
        return "NO_IMPACT";
    }
    
    private String analyzeEnhancedLegalImpact(String decision, String agentType, PESTELAgent agent) {
        if (decision.contains("regulation") || decision.contains("compliance") || decision.contains("governance")) {
            return "VARIABLE:regulatory_framework|VALUE:" + agent.getAgentId() + 
                   " strengthens regulatory framework through governance innovation|REASON:Regulatory improvement";
        }
        if (decision.contains("intellectual property") || decision.contains("patent")) {
            return "VARIABLE:intellectual_property|VALUE:" + agent.getAgentId() + 
                   " enhances IP protection through innovation incentives|REASON:IP development";
        }
        return "NO_IMPACT";
    }
    
    private void applyEnhancedPESTELChange(String impact, String category, String agentId) {
        if (impact.startsWith("VARIABLE:")) {
            String[] parts = impact.split("\\|");
            if (parts.length >= 3) {
                String variable = parts[0].substring(9);
                String newValue = parts[1].substring(6);
                String reason = parts[2].substring(7);
                
                String oldValue = globalPESTEL.getFactor(category, variable);
                
                // Only apply change if it's actually different
                if (!oldValue.equals(newValue)) {
                    globalPESTEL.updateFactor(category, variable, newValue);
                    
                    PESTELChange change = new PESTELChange(category, variable, oldValue, newValue, reason, agentId, currentDay);
                    recentChanges.add(change);
                    
                    Trace.out(Trace.Level.INFO, "  🔄 " + change.toString());
                } else {
                    Trace.out(Trace.Level.INFO, "  ⚪ No change needed for " + category + "." + variable);
                }
            }
        }
    }
    
    private void processCrossAgentInteractions(AgentDecision decision, PESTELAgent sourceAgent) {
        List<String> influencedAgents = agentConnections.get(sourceAgent.getAgentId());
        if (influencedAgents == null || influencedAgents.isEmpty()) {
            return;
        }
        
        // Limit the number of affected agents to prevent excessive updates
        int maxAffected = Math.min(3, influencedAgents.size());
        List<String> affectedIds = influencedAgents.stream()
            .limit(maxAffected)
            .collect(Collectors.toList());
        
        if (!affectedIds.isEmpty()) {
            Trace.out(Trace.Level.INFO, "    📡 Cross-agent effects: " + String.join(", ", affectedIds));
            
            // Record cross-agent interaction
            CrossAgentInteraction interaction = new CrossAgentInteraction(
                sourceAgent.getAgentId(), affectedIds, decision.getDescription(), currentDay);
            crossAgentInteractions.add(interaction);
            
            // Update affected agents' local PESTEL states
            updateAffectedAgentsStates(affectedIds, decision, sourceAgent);
        }
    }
    
    private void updateAffectedAgentsStates(List<String> affectedIds, AgentDecision decision, PESTELAgent sourceAgent) {
        for (String affectedId : affectedIds) {
            PESTELAgent affectedAgent = findAgentById(affectedId);
            if (affectedAgent != null) {
                updateAgentLocalPESTEL(affectedAgent, decision, sourceAgent);
            }
        }
    }
    
    private PESTELAgent findAgentById(String agentId) {
        // Search in companies
        for (RealWorldCompany company : companies) {
            if (company.getAgentId().equals(agentId)) {
                return company;
            }
        }
        
        // Search in countries
        for (RealWorldCountry country : countries) {
            if (country.getAgentId().equals(agentId)) {
                return country;
            }
        }
        
        // Search in researchers
        for (RealWorldResearcher researcher : researchers) {
            if (researcher.getAgentId().equals(agentId)) {
                return researcher;
            }
        }
        
        return null;
    }
    
    private void updateAgentLocalPESTEL(PESTELAgent affectedAgent, AgentDecision decision, PESTELAgent sourceAgent) {
        // Update the affected agent's local PESTEL state based on the source decision
        PESTELState localState = affectedAgent.getLocalPESTEL();
        
        // Simple influence propagation - could be enhanced with AI
        String influence = "Influenced by " + sourceAgent.getAgentId() + ": " + decision.getDescription();
        
        // Update a relevant PESTEL factor in the affected agent
        if (decision.getDecisionType().equals("TECHNOLOGY") || decision.getDecisionType().equals("RESEARCH")) {
            localState.updateFactor("technological", "innovation_ecosystem", influence);
        } else if (decision.getDecisionType().equals("PARTNERSHIP")) {
            localState.updateFactor("political", "international_relations", influence);
        } else if (decision.getDecisionType().equals("SUSTAINABILITY")) {
            localState.updateFactor("environmental", "climate_leadership", influence);
        } else {
            localState.updateFactor("economic", "market_dynamics", influence);
        }
    }
    
    private void updateEnhancedAlternativeFutures() {
        if (currentDay % 7 == 0) { // Weekly scenario updates
            enhancedFutureManager.updateScenarioProbabilities(recentActions, globalPESTEL, currentDay);
            
            EnhancedFutureScenarioManager.FutureScenario dominant = enhancedFutureManager.getCurrentDominantScenario();
            Trace.out(Trace.Level.INFO, String.format("🔮 Dominant Future: %s (%.1f%% probability, %.2f momentum)", 
                dominant.getName(), dominant.getProbability() * 100, dominant.getMomentum()));
        }
    }
    
    private void cleanupOldData() {
        recentActions.removeIf(action -> action.getDay() < currentDay - 7);
        recentChanges.removeIf(change -> change.getDay() < currentDay - 7);
        crossAgentInteractions.removeIf(interaction -> interaction.getDay() < currentDay - 7);
    }
    
    @Override
    public void tryCEvents() {
        // Country union coordination
        if (currentDay % 3 == 0) {
            for (String unionName : countryUnionNames) {
                Trace.out(Trace.Level.INFO, "🤝 " + unionName + " coordinates response to global challenges affecting member states");
            }
        }
    }
    
    @Override
    protected void initialize() {
        // Initialization already done in constructor
    }
    
    @Override
    public void results() {
        displayComprehensiveResults();
    }
    
    public void displayComprehensiveResults() {
        Trace.out(Trace.Level.INFO, "\n=== AI-ENHANCED PESTEL SIMULATION RESULTS ===");
        Trace.out(Trace.Level.INFO, "Simulation completed after " + (currentDay - 1) + " days");
        
        // Display final global state
        Trace.out(Trace.Level.INFO, "\n=== FINAL GLOBAL PESTEL STATE ===");
        Trace.out(Trace.Level.INFO, globalPESTEL.toString());
        
        // Display comprehensive alternative futures analysis
        displayComprehensiveAlternativeFuturesResults();
        
        // Display cross-agent interaction statistics
        displayCrossAgentInteractionStats();
        
        // Display entity statistics
        displayEnhancedEntityStatistics();
        
        // Display strategic recommendations
        displayEnhancedStrategicRecommendations();
    }
    
    private void displayComprehensiveAlternativeFuturesResults() {
        Trace.out(Trace.Level.INFO, "\n=== COMPREHENSIVE ALTERNATIVE FUTURES ANALYSIS ===");
        
        // Generate and display full scenario analysis
        EnhancedFutureScenarioManager.ScenarioAnalysis analysis = enhancedFutureManager.generateScenarioAnalysis();
        Trace.out(Trace.Level.INFO, analysis.generateSummaryReport());
    }
    
    private void displayCrossAgentInteractionStats() {
        Trace.out(Trace.Level.INFO, "\n=== CROSS-AGENT INTERACTION STATISTICS ===");
        
        long totalInteractions = crossAgentInteractions.size();
        Trace.out(Trace.Level.INFO, "Total cross-agent interactions: " + totalInteractions);
        
        // Most influential agents
        Map<String, Long> influenceCount = crossAgentInteractions.stream()
            .collect(Collectors.groupingBy(CrossAgentInteraction::getSourceAgentId, Collectors.counting()));
        
        Trace.out(Trace.Level.INFO, "\n🌟 MOST INFLUENTIAL AGENTS:");
        influenceCount.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(5)
            .forEach(entry -> Trace.out(Trace.Level.INFO, 
                String.format("  %s: %d interactions", entry.getKey(), entry.getValue())));
    }
    
    private void displayEnhancedEntityStatistics() {
        Trace.out(Trace.Level.INFO, "\n=== ENHANCED ENTITY STATISTICS ===");
        
        // Company statistics
        Trace.out(Trace.Level.INFO, "\n[GLOBAL COMPANIES]");
        companies.stream()
            .sorted((c1, c2) -> Integer.compare(c2.getDecisionCount(), c1.getDecisionCount()))
            .limit(10)
            .forEach(company -> Trace.out(Trace.Level.INFO, 
                String.format("  %s: %d decisions, Market Influence: %.2f", 
                    company.getCompanyData().name, company.getDecisionCount(), company.getMarketInfluence())));
        
        // Country statistics
        Trace.out(Trace.Level.INFO, "\n[WORLD COUNTRIES]");
        countries.stream()
            .sorted((c1, c2) -> Integer.compare(c2.getDecisionCount(), c1.getDecisionCount()))
            .limit(10)
            .forEach(country -> Trace.out(Trace.Level.INFO, 
                String.format("  %s: %d policies, Geopolitical Influence: %.2f", 
                    country.getCountryData().name, country.getDecisionCount(), country.getGeopoliticalInfluence())));
        
        // Research statistics
        Trace.out(Trace.Level.INFO, "\n[RESEARCH INSTITUTIONS]");
        researchers.stream()
            .filter(r -> r.getDecisionCount() > 0)
            .sorted((r1, r2) -> Integer.compare(r2.getDecisionCount(), r1.getDecisionCount()))
            .limit(10)
            .forEach(researcher -> Trace.out(Trace.Level.INFO, 
                String.format("  %s: %d initiatives, Research Impact: %.2f", 
                    researcher.getResearchData().name, researcher.getDecisionCount(), researcher.getResearchImpact())));
        
        // Overall statistics
        int totalDecisions = companies.stream().mapToInt(PESTELAgent::getDecisionCount).sum() +
                           countries.stream().mapToInt(PESTELAgent::getDecisionCount).sum() +
                           researchers.stream().mapToInt(PESTELAgent::getDecisionCount).sum();
        
        int totalEntities = companies.size() + countries.size() + researchers.size();
        
        Trace.out(Trace.Level.INFO, String.format("\nTotal Decisions: %d (Companies: %d, Countries: %d, Research: %d)", 
            totalDecisions, 
            companies.stream().mapToInt(PESTELAgent::getDecisionCount).sum(),
            countries.stream().mapToInt(PESTELAgent::getDecisionCount).sum(),
            researchers.stream().mapToInt(PESTELAgent::getDecisionCount).sum()));
    }
    
    private void displayEnhancedStrategicRecommendations() {
        Trace.out(Trace.Level.INFO, "\n=== ENHANCED STRATEGIC RECOMMENDATIONS FOR METROPOLIA UNIVERSITY ===");
        
        EnhancedFutureScenarioManager.FutureScenario dominant = enhancedFutureManager.getCurrentDominantScenario();
        
        Trace.out(Trace.Level.INFO, "🎯 BASED ON DOMINANT SCENARIO: " + dominant.getName());
        
        // Get strategic recommendations from scenario analysis
        EnhancedFutureScenarioManager.ScenarioAnalysis analysis = enhancedFutureManager.generateScenarioAnalysis();
        for (String recommendation : analysis.getStrategicRecommendations()) {
            Trace.out(Trace.Level.INFO, "  • " + recommendation);
        }
    }
    
    // Cross-agent interaction class
    public static class CrossAgentInteraction {
        private String sourceAgentId;
        private List<String> affectedAgentIds;
        private String trigger;
        private int day;
        
        public CrossAgentInteraction(String sourceAgentId, List<String> affectedAgentIds, String trigger, int day) {
            this.sourceAgentId = sourceAgentId;
            this.affectedAgentIds = new ArrayList<>(affectedAgentIds);
            this.trigger = trigger;
            this.day = day;
        }
        
        public String getSourceAgentId() { return sourceAgentId; }
        public List<String> getAffectedAgentIds() { return new ArrayList<>(affectedAgentIds); }
        public String getTrigger() { return trigger; }
        public int getDay() { return day; }
    }
    
    // Getters
    public PESTELState getGlobalPESTEL() { return globalPESTEL; }
    public List<RealWorldCompany> getCompanies() { return new ArrayList<>(companies); }
    public List<RealWorldCountry> getCountries() { return new ArrayList<>(countries); }
    public List<RealWorldResearcher> getResearchers() { return new ArrayList<>(researchers); }
    public List<String> getCountryUnionNames() { return new ArrayList<>(countryUnionNames); }
    public EnhancedFutureScenarioManager getEnhancedFutureManager() { return enhancedFutureManager; }
    public List<PESTELChange> getRecentChanges() { return new ArrayList<>(recentChanges); }
    public List<AgentAction> getRecentActions() { return new ArrayList<>(recentActions); }
    public List<CrossAgentInteraction> getCrossAgentInteractions() { return new ArrayList<>(crossAgentInteractions); }
    public boolean isAIEnabled() { return aiEnabled; }
    public int getSimulationDays() { return simulationDays; }
}
