package simu.model;

import java.util.Random;

/**
 * Country Agent represents countries in the forecasting system.
 * Countries have policy frameworks, economic conditions, and demographic trends
 * that influence their decision-making and interactions with the global state.
 */
public class CountryAgent extends Agent {
    private double gdp;
    private double population;
    private String region;
    private double politicalStability;
    private double educationLevel;
    private double infrastructureQuality;
    
    /**
     * Initialize a new country agent
     * @param agentId Unique identifier for the country
     * @param globalState Reference to the global state
     */
    public CountryAgent(String agentId, GlobalState globalState) {
        super(agentId, AgentType.COUNTRY, globalState);
    }
    
    @Override
    protected void initializeAgentState() {
        // Initialize country-specific factors
        gdp = random.nextDouble() * 5000000000000L; // 0-5T GDP
        population = random.nextDouble() * 1000000000; // 0-1B population
        region = getRandomRegion();
        politicalStability = random.nextDouble();
        educationLevel = random.nextDouble();
        infrastructureQuality = random.nextDouble();
        
        // Set internal factors
        setInternalFactor("gdp", gdp);
        setInternalFactor("population", population);
        setInternalFactor("political_stability", politicalStability);
        setInternalFactor("education_level", educationLevel);
        setInternalFactor("infrastructure_quality", infrastructureQuality);
        
        // Set local data
        setLocalData("region", region);
        setLocalData("development_level", getRandomDevelopmentLevel());
        setLocalData("government_type", getRandomGovernmentType());
    }
    
    @Override
    public AgentDecision makeDecision(double currentTime) {
        lastDecisionTime = currentTime;
        decisionCount++;
        
        // Analyze current conditions
        double globalPeace = globalState.getIndicator("world_peace_index");
        double economicGrowth = globalState.getIndicator("economic_growth_rate");
        double resourceAvailability = globalState.getIndicator("resource_availability");
        double globalTemp = globalState.getIndicator("global_temperature");
        
        // Decision logic based on country characteristics and global state
        String decisionType;
        double confidence;
        double expectedImpact;
        
        if (globalPeace > 0.8 && politicalStability > 0.7) {
            // Stable conditions - invest in development
            decisionType = "invest_development";
            confidence = 0.8;
            expectedImpact = educationLevel * infrastructureQuality * 0.3;
        } else if (globalPeace < 0.4 || politicalStability < 0.3) {
            // Unstable conditions - focus on security
            decisionType = "focus_security";
            confidence = 0.7;
            expectedImpact = -Math.abs(globalPeace - 0.5) * 0.2;
        } else if (globalTemp > 20 && resourceAvailability < 0.5) {
            // Climate and resource challenges - adapt
            decisionType = "climate_adaptation";
            confidence = 0.75;
            expectedImpact = -Math.abs(globalTemp - 15) * 0.1;
        } else if (economicGrowth > 0.05 && educationLevel > 0.6) {
            // Good conditions - promote innovation
            decisionType = "promote_innovation";
            confidence = 0.8;
            expectedImpact = educationLevel * economicGrowth * 0.4;
        } else {
            // Maintain current policies
            decisionType = "maintain_policies";
            confidence = 0.6;
            expectedImpact = 0.05;
        }
        
        AgentDecision decision = new AgentDecision(decisionType, agentId, currentTime);
        decision.setConfidence(confidence);
        decision.setExpectedImpact(expectedImpact);
        
        // Add decision-specific parameters
        decision.setParameter("gdp", gdp);
        decision.setParameter("population", population);
        decision.setParameter("political_stability", politicalStability);
        decision.setParameter("education_level", educationLevel);
        decision.setMetadata("region", region);
        decision.setMetadata("decision_reason", getDecisionReason(decisionType));
        
        return decision;
    }
    
    @Override
    public void updateFromGlobalState(GlobalState globalState) {
        // Update internal factors based on global state changes
        double globalPeace = globalState.getIndicator("world_peace_index");
        double economicGrowth = globalState.getIndicator("economic_growth_rate");
        double globalTemp = globalState.getIndicator("global_temperature");
        
        // Adjust political stability based on global peace
        politicalStability = Math.max(0.0, Math.min(1.0, 
            politicalStability + (globalPeace - 0.5) * 0.1));
        setInternalFactor("political_stability", politicalStability);
        
        // Adjust GDP based on global economic growth
        gdp = Math.max(0.0, gdp * (1 + economicGrowth * 0.5));
        setInternalFactor("gdp", gdp);
        
        // Adjust infrastructure based on climate changes
        if (globalTemp > 18) {
            // Climate stress affects infrastructure
            infrastructureQuality = Math.max(0.0, infrastructureQuality - 0.01);
            setInternalFactor("infrastructure_quality", infrastructureQuality);
        }
    }
    
    @Override
    public void influenceGlobalState(GlobalState globalState, AgentDecision decision) {
        // Apply country influence to global state based on decision
        double influenceStrength = calculateInfluenceStrength();
        double impact = decision.getExpectedImpact() * influenceStrength;
        
        switch (decision.getDecisionType()) {
            case "invest_development":
                // Development investment increases global economic growth
                double currentGrowth = globalState.getIndicator("economic_growth_rate");
                globalState.updateIndicator("economic_growth_rate", currentGrowth + impact * 0.005);
                break;
                
            case "promote_innovation":
                // Innovation promotion increases technology advancement
                double currentTech = globalState.getIndicator("technology_advancement");
                globalState.updateIndicator("technology_advancement", 
                    Math.min(1.0, currentTech + impact * 0.01));
                break;
                
            case "focus_security":
                // Security focus may decrease global peace index
                double currentPeace = globalState.getIndicator("world_peace_index");
                globalState.updateIndicator("world_peace_index", 
                    Math.max(0.0, currentPeace - impact * 0.01));
                break;
                
            case "climate_adaptation":
                // Climate adaptation may improve resource management
                double currentResources = globalState.getIndicator("resource_availability");
                globalState.updateIndicator("resource_availability", 
                    Math.min(1.0, currentResources + impact * 0.01));
                break;
        }
    }
    
    @Override
    public void collaborate(Agent otherAgent, double currentTime) {
        if (otherAgent instanceof CountryAgent) {
            // Country-to-country collaboration
            CountryAgent otherCountry = (CountryAgent) otherAgent;
            
            // Share knowledge and resources
            double collaborationBenefit = (educationLevel + otherCountry.educationLevel) / 2;
            educationLevel = Math.min(1.0, educationLevel + collaborationBenefit * 0.05);
            setInternalFactor("education_level", educationLevel);
            
            // Update local data
            setLocalData("collaboration_partner", otherCountry.getAgentId());
            setLocalData("last_collaboration", String.valueOf(currentTime));
        } else if (otherAgent instanceof ResearcherAgent) {
            // Country-researcher collaboration
            ResearcherAgent researcher = (ResearcherAgent) otherAgent;
            
            // Increase education level through research collaboration
            double researchBenefit = researcher.getInternalFactor("research_expertise") * 0.1;
            educationLevel = Math.min(1.0, educationLevel + researchBenefit);
            setInternalFactor("education_level", educationLevel);
        }
    }
    
    @Override
    public double calculateInfluenceStrength() {
        // Country influence based on GDP, population, and political stability
        double gdpInfluence = Math.log10(gdp + 1) / 12; // Log scale for GDP
        double populationInfluence = Math.log10(population + 1) / 9; // Log scale for population
        double stabilityInfluence = politicalStability;
        
        return (gdpInfluence + populationInfluence + stabilityInfluence) / 3.0;
    }
    
    private String getRandomRegion() {
        String[] regions = {"North America", "Europe", "Asia", "South America", "Africa", "Oceania"};
        return regions[random.nextInt(regions.length)];
    }
    
    private String getRandomDevelopmentLevel() {
        String[] levels = {"developing", "emerging", "developed", "advanced"};
        return levels[random.nextInt(levels.length)];
    }
    
    private String getRandomGovernmentType() {
        String[] types = {"democracy", "republic", "monarchy", "federation", "confederation"};
        return types[random.nextInt(types.length)];
    }
    
    private String getDecisionReason(String decisionType) {
        switch (decisionType) {
            case "invest_development": return "Stable conditions allow for development investment";
            case "focus_security": return "Unstable conditions require security focus";
            case "climate_adaptation": return "Climate and resource challenges require adaptation";
            case "promote_innovation": return "Good conditions support innovation promotion";
            case "maintain_policies": return "Stable conditions, maintaining current policies";
            default: return "Standard policy decision";
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("  GDP: $").append(String.format("%.0f", gdp)).append("\n");
        sb.append("  Population: ").append(String.format("%.0f", population)).append("\n");
        sb.append("  Region: ").append(region).append("\n");
        sb.append("  Political Stability: ").append(String.format("%.2f", politicalStability)).append("\n");
        sb.append("  Education Level: ").append(String.format("%.2f", educationLevel)).append("\n");
        return sb.toString();
    }
}
