package simu.model;

import java.util.List;

/**
 * Real-world company implementation with actual company data and behavior
 */
public class RealWorldCompany extends PESTELAgent {
    private ComprehensiveRealWorldData.CompanyData companyData;
    private double marketInfluence;
    private double innovationIndex;
    private double sustainabilityScore;
    
    public RealWorldCompany(ComprehensiveRealWorldData.CompanyData companyData) {
        super(companyData.name, AgentType.COMPANY);
        this.companyData = companyData;
        this.marketInfluence = calculateMarketInfluence();
        this.innovationIndex = calculateInnovationIndex();
        this.sustainabilityScore = Math.random() * 0.5 + 0.3; // 0.3-0.8
        
        // Re-initialize PESTEL with company data now available
        initializeLocalPESTEL();
    }
    
    @Override
    protected void initializeLocalPESTEL() {
        // Company-specific PESTEL based on real data
        localPESTEL.setPolitical("regulatory_compliance", 
            String.format("%s maintains compliance with %s regulations and international standards", 
                         companyData.name, companyData.country));
        localPESTEL.setPolitical("government_relations", 
            String.format("Strategic partnerships with %s government and regulatory bodies", companyData.country));
        
        localPESTEL.setEconomic("market_position", 
            String.format("Global revenue of $%.0fB in %s sector, headquartered in %s", 
                         companyData.revenue / 1000, companyData.industry, companyData.headquarters));
        localPESTEL.setEconomic("financial_performance", 
            String.format("Strong financial position with market influence score of %.2f", marketInfluence));
        
        localPESTEL.setSocial("brand_reputation", 
            String.format("%s brand recognition globally with focus on %s", 
                         companyData.name, companyData.description));
        localPESTEL.setSocial("workforce", 
            String.format("Large global workforce with operations spanning multiple continents"));
        
        localPESTEL.setTechnological("innovation_capability", 
            String.format("Innovation index: %.2f, leading in %s technology development", 
                         innovationIndex, companyData.industry));
        localPESTEL.setTechnological("digital_transformation", 
            String.format("Advanced digital capabilities in %s sector", companyData.industry));
        
        localPESTEL.setEnvironmental("sustainability_initiatives", 
            String.format("Sustainability score: %.2f, implementing green practices in %s operations", 
                         sustainabilityScore, companyData.industry));
        localPESTEL.setEnvironmental("carbon_footprint", 
            String.format("Working towards carbon neutrality with industry-specific environmental programs"));
        
        localPESTEL.setLegal("intellectual_property", 
            String.format("Extensive IP portfolio protecting %s innovations and technologies", companyData.industry));
        localPESTEL.setLegal("compliance_framework", 
            String.format("Comprehensive legal compliance across %s operations globally", companyData.country));
    }
    
    @Override
    public AgentDecision makeDecision(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions) {
        // Real-world company decision logic based on actual company characteristics
        String decision = generateRealWorldDecision(globalPESTEL, currentDay, recentAgentActions);
        
        if (decision != null && !decision.equals("no_action")) {
            return new AgentDecision(agentId, currentDay, decision, "corporate_strategy", 
                                   calculateDecisionConfidence());
        }
        
        return null;
    }
    
    private String generateRealWorldDecision(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions) {
        // Industry-specific decision making
        switch (companyData.industry.toLowerCase()) {
            case "technology":
                return generateTechDecision(globalPESTEL, currentDay);
            case "retail":
                return generateRetailDecision(globalPESTEL, currentDay);
            case "energy":
                return generateEnergyDecision(globalPESTEL, currentDay);
            case "healthcare":
                return generateHealthcareDecision(globalPESTEL, currentDay);
            case "e-commerce, cloud computing":
                return generateCloudDecision(globalPESTEL, currentDay);
            default:
                return generateGenericDecision(globalPESTEL, currentDay);
        }
    }
    
    private String generateTechDecision(PESTELState globalPESTEL, int currentDay) {
        if (companyData.name.equals("Apple")) {
            if (currentDay % 90 == 0) {
                return "Launch next-generation iPhone with advanced AI capabilities and sustainability features";
            } else if (Math.random() < 0.3) {
                return "Invest $2B in quantum computing research and development partnerships";
            }
        }
        return Math.random() < 0.4 ? "Accelerate AI research and development initiatives" : "no_action";
    }
    
    private String generateRetailDecision(PESTELState globalPESTEL, int currentDay) {
        if (companyData.name.equals("Walmart")) {
            if (currentDay % 30 == 0) {
                return "Expand sustainable supply chain initiatives and renewable energy adoption";
            } else if (Math.random() < 0.25) {
                return "Launch advanced e-commerce platform with AI-powered customer personalization";
            }
        }
        return Math.random() < 0.3 ? "Implement advanced logistics optimization systems" : "no_action";
    }
    
    private String generateEnergyDecision(PESTELState globalPESTEL, int currentDay) {
        String climateStatus = globalPESTEL.getEnvironmental("climate_change");
        if (climateStatus.contains("urgent") || climateStatus.contains("transition")) {
            if (companyData.name.equals("Saudi Aramco")) {
                return "Invest $10B in blue hydrogen production and carbon capture technologies";
            } else {
                return "Accelerate renewable energy portfolio development and green technology investments";
            }
        }
        return Math.random() < 0.2 ? "Optimize energy production efficiency and reduce emissions" : "no_action";
    }
    
    private String generateHealthcareDecision(PESTELState globalPESTEL, int currentDay) {
        if (companyData.name.equals("UnitedHealth Group")) {
            if (currentDay % 60 == 0) {
                return "Launch AI-powered preventive healthcare platform for early disease detection";
            }
        }
        return Math.random() < 0.35 ? "Expand telemedicine capabilities and digital health services" : "no_action";
    }
    
    private String generateCloudDecision(PESTELState globalPESTEL, int currentDay) {
        if (companyData.name.equals("Amazon")) {
            if (currentDay % 45 == 0) {
                return "Launch AWS quantum computing services and expand global data center network";
            } else if (Math.random() < 0.4) {
                return "Invest in sustainable packaging solutions and carbon-neutral delivery systems";
            }
        }
        return Math.random() < 0.3 ? "Expand cloud infrastructure and AI service offerings" : "no_action";
    }
    
    private String generateGenericDecision(PESTELState globalPESTEL, int currentDay) {
        if (Math.random() < 0.25) {
            String[] decisions = {
                String.format("Form strategic alliance with leading %s companies for market expansion", companyData.industry),
                String.format("Launch sustainability initiative targeting 50%% carbon reduction by 2030"),
                String.format("Invest in workforce development and digital skills training programs"),
                String.format("Expand operations in emerging markets with focus on %s", companyData.country),
                String.format("Implement advanced data analytics for %s optimization", companyData.industry)
            };
            return decisions[(int)(Math.random() * decisions.length)];
        }
        return "no_action";
    }
    
    @Override
    public void updateFromPESTELChanges(List<PESTELChange> changes) {
        for (PESTELChange change : changes) {
            // Update based on industry relevance
            if (isRelevantChange(change)) {
                updateLocalPESTELFromChange(change);
            }
        }
    }
    
    private boolean isRelevantChange(PESTELChange change) {
        // Check if change is relevant to this company's industry or region
        return change.getNewValue().toLowerCase().contains(companyData.industry.toLowerCase()) ||
               change.getNewValue().toLowerCase().contains(companyData.country.toLowerCase()) ||
               change.getCategory().equals("economic") || change.getCategory().equals("technological");
    }
    
    private void updateLocalPESTELFromChange(PESTELChange change) {
        String impact = String.format("Adjusted strategy due to %s change: %s", 
                                     change.getCategory(), change.getNewValue());
        localPESTEL.updateFactor(change.getCategory(), "strategic_response", impact);
    }
    
    @Override
    public String getAgentDescription() {
        return String.format("%s - Revenue: $%.0fB, Industry: %s, HQ: %s, %s", 
                           companyData.name, companyData.revenue / 1000, 
                           companyData.industry, companyData.headquarters, companyData.country);
    }
    
    private double calculateMarketInfluence() {
        // Based on revenue and industry type
        double baseInfluence = Math.min(1.0, companyData.revenue / 1000000.0); // Normalize by trillion
        
        // Industry multipliers
        if (companyData.industry.toLowerCase().contains("technology")) {
            baseInfluence *= 1.3;
        } else if (companyData.industry.toLowerCase().contains("energy")) {
            baseInfluence *= 1.2;
        } else if (companyData.industry.toLowerCase().contains("healthcare")) {
            baseInfluence *= 1.1;
        }
        
        return Math.min(1.0, baseInfluence);
    }
    
    private double calculateInnovationIndex() {
        // Based on industry and company characteristics
        if (companyData.industry.toLowerCase().contains("technology") || 
            companyData.industry.toLowerCase().contains("cloud")) {
            return 0.8 + Math.random() * 0.2; // 0.8-1.0
        } else if (companyData.industry.toLowerCase().contains("healthcare")) {
            return 0.6 + Math.random() * 0.3; // 0.6-0.9
        } else {
            return 0.3 + Math.random() * 0.4; // 0.3-0.7
        }
    }
    
    private double calculateDecisionConfidence() {
        // Higher confidence for larger, more established companies
        double confidence = 0.5 + (marketInfluence * 0.3) + (innovationIndex * 0.2);
        return Math.min(0.95, confidence);
    }
    
    // Getters
    public ComprehensiveRealWorldData.CompanyData getCompanyData() {
        return companyData;
    }
    
    public double getMarketInfluence() {
        return marketInfluence;
    }
    
    public double getInnovationIndex() {
        return innovationIndex;
    }
    
    public double getSustainabilityScore() {
        return sustainabilityScore;
    }
}
