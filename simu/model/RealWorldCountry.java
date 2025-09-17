package simu.model;

import java.util.List;
import java.util.ArrayList;

/**
 * Real-world country implementation with actual country data and geopolitical behavior
 */
public class RealWorldCountry extends PESTELAgent {
    private ComprehensiveRealWorldData.CountryData countryData;
    private double geopoliticalInfluence;
    private double economicPower;
    private double militaryStrength;
    private List<String> allianceMembers;
    private ComprehensiveRealWorldData.CountryUnion countryUnion;
    
    public RealWorldCountry(ComprehensiveRealWorldData.CountryData countryData) {
        super(countryData.name, AgentType.COUNTRY);
        this.countryData = countryData;
        this.geopoliticalInfluence = calculateGeopoliticalInfluence();
        this.economicPower = calculateEconomicPower();
        this.militaryStrength = calculateMilitaryStrength();
        this.allianceMembers = new ArrayList<>();
        this.countryUnion = findCountryUnion();
        
        // Re-initialize PESTEL with country data now available
        initializeLocalPESTEL();
    }
    
    @Override
    protected void initializeLocalPESTEL() {
        // Country-specific PESTEL based on real data
        localPESTEL.setPolitical("government_system", 
            String.format("%s operates as a %s with stable democratic institutions", 
                         countryData.name, countryData.governmentType));
        localPESTEL.setPolitical("international_relations", 
            String.format("Active member of %s alliance, maintaining diplomatic relations globally", 
                         countryUnion != null ? countryUnion.name : "various international"));
        localPESTEL.setPolitical("policy_framework", 
            String.format("Comprehensive policy framework supporting %s development priorities", 
                         countryData.developmentLevel));
        
        localPESTEL.setEconomic("national_economy", 
            String.format("GDP: $%.0fB, Population: %.0fM, Currency: %s", 
                         countryData.gdp / 1000.0, countryData.population / 1000000.0, countryData.currency));
        localPESTEL.setEconomic("trade_relations", 
            String.format("Major trading partner in %s region with economic power index: %.2f", 
                         countryData.region, economicPower));
        localPESTEL.setEconomic("fiscal_policy", 
            String.format("Stable fiscal policy supporting %s economic objectives", countryData.developmentLevel));
        
        localPESTEL.setSocial("demographics", 
            String.format("Population: %.0fM, Primary language: %s, %s society", 
                         countryData.population / 1000000.0, countryData.language, countryData.developmentLevel));
        localPESTEL.setSocial("cultural_influence", 
            String.format("Significant cultural influence in %s region through %s heritage", 
                         countryData.region, countryData.language));
        
        localPESTEL.setTechnological("innovation_ecosystem", 
            String.format("Advanced innovation ecosystem with geopolitical influence: %.2f", geopoliticalInfluence));
        localPESTEL.setTechnological("digital_infrastructure", 
            String.format("Modern digital infrastructure supporting %s technological development", 
                         countryData.developmentLevel));
        
        localPESTEL.setEnvironmental("climate_commitments", 
            String.format("%s committed to international climate agreements and sustainable development", 
                         countryData.name));
        localPESTEL.setEnvironmental("resource_management", 
            String.format("Strategic resource management policies for %s region", countryData.region));
        
        localPESTEL.setLegal("legal_system", 
            String.format("Robust legal system based on %s constitutional framework", countryData.governmentType));
        localPESTEL.setLegal("international_law", 
            String.format("Active participant in international legal frameworks and treaties"));
    }
    
    @Override
    public AgentDecision makeDecision(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions) {
        String decision = generateGeopoliticalDecision(globalPESTEL, currentDay, recentAgentActions);
        
        if (decision != null && !decision.equals("no_action")) {
            return new AgentDecision(agentId, currentDay, decision, "geopolitical_strategy", 
                                   calculateDecisionConfidence());
        }
        
        return null;
    }
    
    private String generateGeopoliticalDecision(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions) {
        // Country-specific decision making based on real geopolitical positions
        switch (countryData.name) {
            case "United States":
                return generateUSDecision(globalPESTEL, currentDay, recentAgentActions);
            case "China":
                return generateChinaDecision(globalPESTEL, currentDay, recentAgentActions);
            case "Germany":
                return generateGermanyDecision(globalPESTEL, currentDay, recentAgentActions);
            case "Japan":
                return generateJapanDecision(globalPESTEL, currentDay, recentAgentActions);
            case "United Kingdom":
                return generateUKDecision(globalPESTEL, currentDay, recentAgentActions);
            default:
                return generateGenericCountryDecision(globalPESTEL, currentDay, recentAgentActions);
        }
    }
    
    private String generateUSDecision(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions) {
        if (currentDay % 120 == 0) {
            return "Launch $100B infrastructure modernization program focusing on clean energy and digital connectivity";
        } else if (hasRecentConflict(recentAgentActions)) {
            return "Strengthen NATO alliance partnerships and increase defense cooperation with allies";
        } else if (Math.random() < 0.3) {
            return "Announce new AI leadership initiative with $50B investment in quantum computing research";
        }
        return Math.random() < 0.25 ? "Implement comprehensive immigration reform and workforce development programs" : "no_action";
    }
    
    private String generateChinaDecision(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions) {
        if (currentDay % 100 == 0) {
            return "Launch Belt and Road Initiative 2.0 with focus on green infrastructure and digital connectivity";
        } else if (globalPESTEL.getTechnological("innovation").contains("AI")) {
            return "Invest $80B in artificial intelligence research and semiconductor manufacturing capabilities";
        } else if (Math.random() < 0.35) {
            return "Strengthen BRICS cooperation and expand yuan-based international trade agreements";
        }
        return Math.random() < 0.2 ? "Implement carbon neutrality roadmap with massive renewable energy expansion" : "no_action";
    }
    
    private String generateGermanyDecision(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions) {
        if (currentDay % 90 == 0) {
            return "Lead European Union green transition with €75B renewable energy investment program";
        } else if (hasEconomicStress(globalPESTEL)) {
            return "Strengthen EU economic integration and launch European digital sovereignty initiative";
        } else if (Math.random() < 0.4) {
            return "Expand Industry 4.0 programs and establish European AI research consortium";
        }
        return Math.random() < 0.3 ? "Implement comprehensive circular economy policies across EU member states" : "no_action";
    }
    
    private String generateJapanDecision(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions) {
        if (currentDay % 80 == 0) {
            return "Launch Society 5.0 initiative with $40B investment in robotics and aging society solutions";
        } else if (Math.random() < 0.3) {
            return "Strengthen Indo-Pacific security partnerships and expand clean energy cooperation";
        }
        return Math.random() < 0.25 ? "Implement advanced disaster resilience systems using AI and IoT technologies" : "no_action";
    }
    
    private String generateUKDecision(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions) {
        if (currentDay % 75 == 0) {
            return "Launch Global Britain strategy with £30B investment in fintech and green technology";
        } else if (Math.random() < 0.35) {
            return "Strengthen Commonwealth partnerships and establish new trade agreements with emerging economies";
        }
        return Math.random() < 0.2 ? "Implement net-zero strategy with focus on offshore wind and nuclear energy" : "no_action";
    }
    
    private String generateGenericCountryDecision(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions) {
        if (Math.random() < 0.3) {
            String[] decisions = {
                String.format("Strengthen %s regional cooperation and expand trade partnerships", countryData.region),
                String.format("Launch national digitalization program with focus on %s development", countryData.developmentLevel),
                String.format("Implement sustainable development goals aligned with %s priorities", countryData.region),
                String.format("Expand international cooperation in education and research initiatives"),
                String.format("Strengthen climate resilience and environmental protection measures")
            };
            return decisions[(int)(Math.random() * decisions.length)];
        }
        return "no_action";
    }
    
    private boolean hasRecentConflict(List<AgentAction> recentActions) {
        return recentActions.stream().anyMatch(action -> 
            action.getActionDescription().toLowerCase().contains("conflict") ||
            action.getActionDescription().toLowerCase().contains("security") ||
            action.getActionDescription().toLowerCase().contains("defense"));
    }
    
    private boolean hasEconomicStress(PESTELState globalPESTEL) {
        String economicGrowth = globalPESTEL.getEconomic("growth");
        return economicGrowth.toLowerCase().contains("recession") || 
               economicGrowth.toLowerCase().contains("decline") ||
               economicGrowth.toLowerCase().contains("crisis");
    }
    
    @Override
    public void updateFromPESTELChanges(List<PESTELChange> changes) {
        for (PESTELChange change : changes) {
            if (isGeopoliticallyRelevant(change)) {
                updateGeopoliticalResponse(change);
            }
        }
    }
    
    private boolean isGeopoliticallyRelevant(PESTELChange change) {
        return change.getNewValue().toLowerCase().contains(countryData.region.toLowerCase()) ||
               change.getNewValue().toLowerCase().contains(countryData.name.toLowerCase()) ||
               change.getCategory().equals("political") || change.getCategory().equals("economic");
    }
    
    private void updateGeopoliticalResponse(PESTELChange change) {
        String response = String.format("%s adjusts %s policy in response to %s developments", 
                                       countryData.name, change.getCategory(), change.getNewValue());
        localPESTEL.updateFactor(change.getCategory(), "policy_response", response);
    }
    
    @Override
    public String getAgentDescription() {
        return String.format("%s - Population: %.0fM, GDP: $%.0fB, Region: %s, Government: %s", 
                           countryData.name, countryData.population / 1000000.0, 
                           countryData.gdp / 1000.0, countryData.region, countryData.governmentType);
    }
    
    private double calculateGeopoliticalInfluence() {
        // Based on GDP, population, and regional position
        double gdpFactor = Math.min(1.0, countryData.gdp / 10000000.0); // Normalize by 10T
        double popFactor = Math.min(1.0, countryData.population / 1000000000.0); // Normalize by 1B
        double devFactor = countryData.developmentLevel.equals("Developed") ? 1.0 : 0.7;
        
        return (gdpFactor * 0.4 + popFactor * 0.3 + devFactor * 0.3);
    }
    
    private double calculateEconomicPower() {
        return Math.min(1.0, countryData.gdp / 15000000.0); // Normalize by 15T
    }
    
    private double calculateMilitaryStrength() {
        // Estimated based on GDP and geopolitical position
        return geopoliticalInfluence * 0.8 + Math.random() * 0.2;
    }
    
    private ComprehensiveRealWorldData.CountryUnion findCountryUnion() {
        for (ComprehensiveRealWorldData.CountryUnion union : ComprehensiveRealWorldData.COUNTRY_UNIONS) {
            if (union.memberCountries.contains(countryData.name)) {
                return union;
            }
        }
        return null;
    }
    
    private double calculateDecisionConfidence() {
        return 0.6 + (geopoliticalInfluence * 0.3) + (economicPower * 0.1);
    }
    
    // Getters
    public ComprehensiveRealWorldData.CountryData getCountryData() {
        return countryData;
    }
    
    public double getGeopoliticalInfluence() {
        return geopoliticalInfluence;
    }
    
    public double getEconomicPower() {
        return economicPower;
    }
    
    public ComprehensiveRealWorldData.CountryUnion getCountryUnion() {
        return countryUnion;
    }
    
    public List<String> getAllianceMembers() {
        return new ArrayList<>(allianceMembers);
    }
}
