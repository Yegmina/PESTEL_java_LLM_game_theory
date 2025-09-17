package simu.model;

import java.util.Random;

/**
 * Company Agent represents companies in the forecasting system.
 * Companies have market conditions, financial resources, and strategic priorities
 * that influence their decision-making and interactions with the global state.
 */
public class CompanyAgent extends Agent {
    private double marketShare;
    private double financialResources;
    private String industrySector;
    private double innovationLevel;
    private double riskTolerance;
    
    /**
     * Initialize a new company agent
     * @param agentId Unique identifier for the company
     * @param globalState Reference to the global state
     */
    public CompanyAgent(String agentId, GlobalState globalState) {
        super(agentId, AgentType.COMPANY, globalState);
    }
    
    @Override
    protected void initializeAgentState() {
        // Initialize company-specific factors
        marketShare = random.nextDouble() * 0.1; // 0-10% market share
        financialResources = random.nextDouble() * 1000000; // 0-1M resources
        industrySector = getRandomIndustrySector();
        innovationLevel = random.nextDouble();
        riskTolerance = random.nextDouble();
        
        // Set internal factors
        setInternalFactor("market_share", marketShare);
        setInternalFactor("financial_resources", financialResources);
        setInternalFactor("innovation_level", innovationLevel);
        setInternalFactor("risk_tolerance", riskTolerance);
        
        // Set local data
        setLocalData("industry_sector", industrySector);
        setLocalData("company_size", getRandomCompanySize());
        setLocalData("market_position", getRandomMarketPosition());
    }
    
    @Override
    public AgentDecision makeDecision(double currentTime) {
        lastDecisionTime = currentTime;
        decisionCount++;
        
        // Analyze current conditions
        double economicGrowth = globalState.getIndicator("economic_growth_rate");
        double resourceAvailability = globalState.getIndicator("resource_availability");
        double technologyAdvancement = globalState.getIndicator("technology_advancement");
        
        // Decision logic based on company characteristics and global state
        String decisionType;
        double confidence;
        double expectedImpact;
        
        if (economicGrowth > 0.05 && financialResources > 500000) {
            // Good economic conditions and sufficient resources - expand
            decisionType = "expand";
            confidence = 0.8;
            expectedImpact = economicGrowth * innovationLevel * 0.5;
        } else if (economicGrowth < -0.02 || resourceAvailability < 0.3) {
            // Poor conditions - contract or divest
            decisionType = "contract";
            confidence = 0.7;
            expectedImpact = -Math.abs(economicGrowth) * 0.3;
        } else if (technologyAdvancement > 0.7 && innovationLevel > 0.6) {
            // High tech advancement and innovation capability - invest in R&D
            decisionType = "invest_rd";
            confidence = 0.75;
            expectedImpact = technologyAdvancement * innovationLevel * 0.4;
        } else {
            // Maintain current position
            decisionType = "maintain";
            confidence = 0.6;
            expectedImpact = 0.1;
        }
        
        AgentDecision decision = new AgentDecision(decisionType, agentId, currentTime);
        decision.setConfidence(confidence);
        decision.setExpectedImpact(expectedImpact);
        
        // Add decision-specific parameters
        decision.setParameter("market_share", marketShare);
        decision.setParameter("financial_resources", financialResources);
        decision.setParameter("innovation_level", innovationLevel);
        decision.setMetadata("industry_sector", industrySector);
        decision.setMetadata("decision_reason", getDecisionReason(decisionType));
        
        return decision;
    }
    
    @Override
    public void updateFromGlobalState(GlobalState globalState) {
        // Update internal factors based on global state changes
        double economicGrowth = globalState.getIndicator("economic_growth_rate");
        double resourceAvailability = globalState.getIndicator("resource_availability");
        
        // Adjust market share based on economic conditions
        marketShare = Math.max(0.0, marketShare + economicGrowth * 0.01);
        setInternalFactor("market_share", marketShare);
        
        // Adjust financial resources based on economic growth
        financialResources = Math.max(0.0, financialResources * (1 + economicGrowth));
        setInternalFactor("financial_resources", financialResources);
        
        // Update risk tolerance based on global stability
        double stability = globalState.calculateStabilityIndex();
        riskTolerance = Math.max(0.0, Math.min(1.0, riskTolerance + (stability - 0.5) * 0.1));
        setInternalFactor("risk_tolerance", riskTolerance);
    }
    
    @Override
    public void influenceGlobalState(GlobalState globalState, AgentDecision decision) {
        // Apply company influence to global state based on decision
        double influenceStrength = calculateInfluenceStrength();
        double impact = decision.getExpectedImpact() * influenceStrength;
        
        switch (decision.getDecisionType()) {
            case "expand":
                // Expansion increases economic growth
                double currentGrowth = globalState.getIndicator("economic_growth_rate");
                globalState.updateIndicator("economic_growth_rate", currentGrowth + impact * 0.01);
                break;
                
            case "invest_rd":
                // R&D investment increases technology advancement
                double currentTech = globalState.getIndicator("technology_advancement");
                globalState.updateIndicator("technology_advancement", 
                    Math.min(1.0, currentTech + impact * 0.02));
                break;
                
            case "contract":
                // Contraction decreases economic growth
                double currentGrowth2 = globalState.getIndicator("economic_growth_rate");
                globalState.updateIndicator("economic_growth_rate", currentGrowth2 - impact * 0.01);
                break;
        }
    }
    
    @Override
    public void collaborate(Agent otherAgent, double currentTime) {
        if (otherAgent instanceof CompanyAgent) {
            // Company-to-company collaboration
            CompanyAgent otherCompany = (CompanyAgent) otherAgent;
            
            // Share innovation and resources
            double collaborationBenefit = (innovationLevel + otherCompany.innovationLevel) / 2;
            innovationLevel = Math.min(1.0, innovationLevel + collaborationBenefit * 0.1);
            setInternalFactor("innovation_level", innovationLevel);
            
            // Update local data
            setLocalData("collaboration_partner", otherCompany.getAgentId());
            setLocalData("last_collaboration", String.valueOf(currentTime));
        } else if (otherAgent instanceof ResearcherAgent) {
            // Company-researcher collaboration
            ResearcherAgent researcher = (ResearcherAgent) otherAgent;
            
            // Increase innovation through research collaboration
            double researchBenefit = researcher.getInternalFactor("research_expertise") * 0.2;
            innovationLevel = Math.min(1.0, innovationLevel + researchBenefit);
            setInternalFactor("innovation_level", innovationLevel);
        }
    }
    
    @Override
    public double calculateInfluenceStrength() {
        // Company influence based on market share, financial resources, and innovation
        double marketInfluence = marketShare * 10; // Scale up market share
        double financialInfluence = Math.log10(financialResources + 1) / 6; // Log scale
        double innovationInfluence = innovationLevel;
        
        return (marketInfluence + financialInfluence + innovationInfluence) / 3.0;
    }
    
    private String getRandomIndustrySector() {
        String[] sectors = {"technology", "manufacturing", "services", "finance", "healthcare", "energy"};
        return sectors[random.nextInt(sectors.length)];
    }
    
    private String getRandomCompanySize() {
        String[] sizes = {"startup", "small", "medium", "large", "enterprise"};
        return sizes[random.nextInt(sizes.length)];
    }
    
    private String getRandomMarketPosition() {
        String[] positions = {"leader", "challenger", "follower", "niche"};
        return positions[random.nextInt(positions.length)];
    }
    
    private String getDecisionReason(String decisionType) {
        switch (decisionType) {
            case "expand": return "Favorable economic conditions and sufficient resources";
            case "contract": return "Poor economic conditions or resource scarcity";
            case "invest_rd": return "High technology advancement and innovation capability";
            case "maintain": return "Stable conditions, maintaining current position";
            default: return "Standard business decision";
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("  Market Share: ").append(String.format("%.2f%%", marketShare * 100)).append("\n");
        sb.append("  Financial Resources: $").append(String.format("%.0f", financialResources)).append("\n");
        sb.append("  Industry: ").append(industrySector).append("\n");
        sb.append("  Innovation Level: ").append(String.format("%.2f", innovationLevel)).append("\n");
        return sb.toString();
    }
}
