package simu.model;

import java.util.List;

/**
 * Company agent with PESTEL-based decision making
 */
public class CompanyPESTELAgent extends PESTELAgent {
    private String industry;
    private String companySize;
    private String marketPosition;
    
    public CompanyPESTELAgent(String agentId) {
        super(agentId, AgentType.COMPANY);
        
        // Initialize company-specific attributes
        String[] industries = {"technology", "manufacturing", "services", "finance", "healthcare", "energy"};
        String[] sizes = {"startup", "small", "medium", "large", "enterprise"};
        String[] positions = {"leader", "challenger", "follower", "niche"};
        
        this.industry = industries[(int)(Math.random() * industries.length)];
        this.companySize = sizes[(int)(Math.random() * sizes.length)];
        this.marketPosition = positions[(int)(Math.random() * positions.length)];
    }
    
    @Override
    protected void initializeLocalPESTEL() {
        // Company-specific PESTEL factors
        localPESTEL.setPolitical("regulatory_compliance", "Full compliance with current business regulations");
        localPESTEL.setPolitical("government_relations", "Standard corporate-government relationship");
        
        localPESTEL.setEconomic("market_conditions", "Operating in competitive market with moderate growth");
        localPESTEL.setEconomic("financial_health", "Stable revenue streams, moderate profitability");
        localPESTEL.setEconomic("cost_structure", "Balanced operational costs, seeking efficiency improvements");
        
        localPESTEL.setSocial("customer_base", "Diverse customer demographics, brand loyalty moderate");
        localPESTEL.setSocial("employee_satisfaction", "Good workplace culture, competitive compensation");
        
        localPESTEL.setTechnological("innovation_capability", "Moderate R&D investment, following industry trends");
        localPESTEL.setTechnological("digital_transformation", "Ongoing digitalization efforts, cloud adoption");
        
        localPESTEL.setEnvironmental("sustainability_practices", "Basic environmental compliance, some green initiatives");
        localPESTEL.setEnvironmental("carbon_footprint", "Standard industry carbon footprint, reduction plans in development");
        
        localPESTEL.setLegal("contract_management", "Standard business contracts, legal compliance maintained");
        localPESTEL.setLegal("ip_protection", "Basic intellectual property protection measures");
    }
    
    @Override
    public AgentDecision makeDecision(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions) {
        // Company decision logic based on PESTEL analysis
        String context = getCurrentContext(globalPESTEL, currentDay, recentAgentActions);
        
        // Simple decision logic - in real implementation, this would use AI
        String decision = analyzeAndDecide(globalPESTEL, currentDay);
        
        if (decision != null && !decision.equals("no_action")) {
            return new AgentDecision(agentId, currentDay, decision, "business_decision", 0.7);
        }
        
        return null; // No action taken
    }
    
    private String analyzeAndDecide(PESTELState globalPESTEL, int currentDay) {
        // Analyze global economic conditions
        String economicGrowth = globalPESTEL.getEconomic("growth");
        String technologicalInnovation = globalPESTEL.getTechnological("innovation");
        
        // Decision logic based on conditions
        if (economicGrowth.contains("expansion") || economicGrowth.contains("growth")) {
            if (companySize.equals("startup") || companySize.equals("small")) {
                return "Expand operations to capture market growth opportunities";
            } else {
                return "Increase R&D investment to maintain competitive advantage";
            }
        }
        
        if (technologicalInnovation.contains("high") || technologicalInnovation.contains("strong")) {
            return "Invest in digital transformation and new technology adoption";
        }
        
        if (currentDay % 30 == 0) { // Monthly strategic review
            return "Conduct monthly business review and adjust strategy based on market conditions";
        }
        
        if (Math.random() < 0.3) { // 30% chance of taking action
            String[] actions = {
                "Form strategic partnership with industry leader",
                "Launch new product line based on market research",
                "Implement cost reduction program to improve margins",
                "Expand into new geographic market",
                "Acquire smaller competitor to increase market share"
            };
            return actions[(int)(Math.random() * actions.length)];
        }
        
        return "no_action";
    }
    
    @Override
    public void updateFromPESTELChanges(List<PESTELChange> changes) {
        for (PESTELChange change : changes) {
            // Update local PESTEL based on global changes
            switch (change.getCategory().toLowerCase()) {
                case "economic":
                case "e":
                    if (change.getFactor().equals("growth")) {
                        localPESTEL.setEconomic("market_conditions", 
                            "Market conditions adjusted due to: " + change.getNewValue());
                    }
                    break;
                case "technological":
                case "t":
                    if (change.getFactor().equals("innovation")) {
                        localPESTEL.setTechnological("innovation_capability",
                            "Innovation strategy updated due to: " + change.getNewValue());
                    }
                    break;
                case "political":
                case "p":
                    localPESTEL.setPolitical("regulatory_compliance",
                        "Compliance strategy adjusted due to: " + change.getNewValue());
                    break;
            }
        }
    }
    
    @Override
    public String getAgentDescription() {
        return String.format("Company %s - Industry: %s, Size: %s, Position: %s", 
                           agentId, industry, companySize, marketPosition);
    }
    
    public String getIndustry() {
        return industry;
    }
    
    public String getCompanySize() {
        return companySize;
    }
    
    public String getMarketPosition() {
        return marketPosition;
    }
}
