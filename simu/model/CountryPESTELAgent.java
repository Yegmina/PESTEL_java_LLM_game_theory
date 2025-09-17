package simu.model;

import java.util.List;

/**
 * Country agent with PESTEL-based decision making
 */
public class CountryPESTELAgent extends PESTELAgent {
    private String region;
    private String developmentLevel;
    private String governmentType;
    
    public CountryPESTELAgent(String agentId) {
        super(agentId, AgentType.COUNTRY);
        
        // Initialize country-specific attributes
        String[] regions = {"North America", "Europe", "Asia", "South America", "Africa", "Oceania"};
        String[] levels = {"developing", "emerging", "developed", "advanced"};
        String[] govTypes = {"democracy", "republic", "monarchy", "federation"};
        
        this.region = regions[(int)(Math.random() * regions.length)];
        this.developmentLevel = levels[(int)(Math.random() * levels.length)];
        this.governmentType = govTypes[(int)(Math.random() * govTypes.length)];
    }
    
    @Override
    protected void initializeLocalPESTEL() {
        // Country-specific PESTEL factors
        localPESTEL.setPolitical("domestic_policy", "Stable domestic policies focused on economic development");
        localPESTEL.setPolitical("international_relations", "Maintaining diplomatic relationships with key partners");
        localPESTEL.setPolitical("governance", "Democratic governance with regular elections and peaceful transitions");
        
        localPESTEL.setEconomic("national_budget", "Balanced budget with moderate deficit, focus on infrastructure");
        localPESTEL.setEconomic("trade_balance", "Positive trade relationships with key partners, moderate surplus");
        localPESTEL.setEconomic("currency_stability", "Stable currency with controlled inflation rates");
        
        localPESTEL.setSocial("education_system", "Well-developed education system with high literacy rates");
        localPESTEL.setSocial("healthcare_system", "Universal healthcare coverage with good health outcomes");
        localPESTEL.setSocial("social_cohesion", "Strong social fabric with moderate inequality levels");
        
        localPESTEL.setTechnological("digital_infrastructure", "Advanced broadband coverage and digital services");
        localPESTEL.setTechnological("research_investment", "Significant public investment in research and development");
        
        localPESTEL.setEnvironmental("climate_policy", "Committed to carbon neutrality with renewable energy transition");
        localPESTEL.setEnvironmental("natural_resources", "Sustainable management of natural resources");
        
        localPESTEL.setLegal("rule_of_law", "Strong legal institutions with transparent judicial system");
        localPESTEL.setLegal("regulatory_framework", "Comprehensive regulatory framework supporting business and citizens");
    }
    
    @Override
    public AgentDecision makeDecision(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions) {
        String context = getCurrentContext(globalPESTEL, currentDay, recentAgentActions);
        
        // Country decision logic based on PESTEL analysis
        String decision = analyzeAndDecide(globalPESTEL, currentDay, recentAgentActions);
        
        if (decision != null && !decision.equals("no_action")) {
            return new AgentDecision(agentId, currentDay, decision, "policy_decision", 0.8);
        }
        
        return null; // No action taken
    }
    
    private String analyzeAndDecide(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions) {
        // Analyze global conditions for policy decisions
        String globalEconomicGrowth = globalPESTEL.getEconomic("growth");
        String globalPoliticalStability = globalPESTEL.getPolitical("stability");
        String globalClimateChange = globalPESTEL.getEnvironmental("climate_change");
        
        // React to other countries' actions
        for (AgentAction action : recentAgentActions) {
            if (action.getAgentId().startsWith("Country_") && !action.getAgentId().equals(agentId)) {
                if (action.getActionDescription().contains("trade agreement")) {
                    return "Negotiate bilateral trade agreement to strengthen economic ties";
                }
                if (action.getActionDescription().contains("defense")) {
                    return "Review national security strategy and defense capabilities";
                }
            }
        }
        
        // Policy decisions based on global conditions
        if (globalEconomicGrowth.contains("recession") || globalEconomicGrowth.contains("decline")) {
            return "Implement economic stimulus package to support domestic growth";
        }
        
        if (globalPoliticalStability.contains("unstable") || globalPoliticalStability.contains("conflict")) {
            return "Strengthen diplomatic initiatives and peacekeeping efforts";
        }
        
        if (globalClimateChange.contains("urgent") || globalClimateChange.contains("crisis")) {
            return "Accelerate renewable energy transition and climate adaptation measures";
        }
        
        // Regular policy reviews and initiatives
        if (currentDay % 90 == 0) { // Quarterly policy review
            return "Conduct quarterly policy review and adjust national strategy";
        }
        
        if (currentDay % 30 == 15) { // Monthly international engagement
            return "Engage in international cooperation initiatives and diplomatic meetings";
        }
        
        // Random policy initiatives
        if (Math.random() < 0.25) { // 25% chance of taking action
            String[] policies = {
                "Launch national infrastructure modernization program",
                "Implement education reform to improve digital literacy",
                "Establish new environmental protection regulations",
                "Create innovation hubs to support technology startups",
                "Strengthen healthcare system resilience and capacity",
                "Develop strategic partnerships with neighboring countries"
            };
            return policies[(int)(Math.random() * policies.length)];
        }
        
        return "no_action";
    }
    
    @Override
    public void updateFromPESTELChanges(List<PESTELChange> changes) {
        for (PESTELChange change : changes) {
            // Update local PESTEL based on global changes
            switch (change.getCategory().toLowerCase()) {
                case "political":
                case "p":
                    localPESTEL.setPolitical("international_relations", 
                        "International relations adjusted due to: " + change.getNewValue());
                    break;
                case "economic":
                case "e":
                    localPESTEL.setEconomic("trade_balance",
                        "Trade strategy updated due to: " + change.getNewValue());
                    break;
                case "environmental":
                case "env":
                    localPESTEL.setEnvironmental("climate_policy",
                        "Climate policy adjusted due to: " + change.getNewValue());
                    break;
                case "social":
                case "s":
                    localPESTEL.setSocial("social_cohesion",
                        "Social policies updated due to: " + change.getNewValue());
                    break;
            }
        }
    }
    
    @Override
    public String getAgentDescription() {
        return String.format("Country %s - Region: %s, Development: %s, Government: %s", 
                           agentId, region, developmentLevel, governmentType);
    }
    
    public String getRegion() {
        return region;
    }
    
    public String getDevelopmentLevel() {
        return developmentLevel;
    }
    
    public String getGovernmentType() {
        return governmentType;
    }
}
