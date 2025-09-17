package simu.model;

import java.util.Random;

/**
 * Researcher Agent represents researchers in the forecasting system.
 * Researchers have research interests, funding availability, and collaboration networks
 * that influence their decision-making and interactions with the global state.
 */
public class ResearcherAgent extends Agent {
    private String researchField;
    private double researchExpertise;
    private double fundingAvailability;
    private double collaborationNetwork;
    private double publicationRate;
    private double innovationPotential;
    
    /**
     * Initialize a new researcher agent
     * @param agentId Unique identifier for the researcher
     * @param globalState Reference to the global state
     */
    public ResearcherAgent(String agentId, GlobalState globalState) {
        super(agentId, AgentType.RESEARCHER, globalState);
    }
    
    @Override
    protected void initializeAgentState() {
        // Initialize researcher-specific factors
        researchField = getRandomResearchField();
        researchExpertise = random.nextDouble();
        fundingAvailability = random.nextDouble();
        collaborationNetwork = random.nextDouble();
        publicationRate = random.nextDouble();
        innovationPotential = random.nextDouble();
        
        // Set internal factors
        setInternalFactor("research_expertise", researchExpertise);
        setInternalFactor("funding_availability", fundingAvailability);
        setInternalFactor("collaboration_network", collaborationNetwork);
        setInternalFactor("publication_rate", publicationRate);
        setInternalFactor("innovation_potential", innovationPotential);
        
        // Set local data
        setLocalData("research_field", researchField);
        setLocalData("institution_type", getRandomInstitutionType());
        setLocalData("career_stage", getRandomCareerStage());
    }
    
    @Override
    public AgentDecision makeDecision(double currentTime) {
        lastDecisionTime = currentTime;
        decisionCount++;
        
        // Analyze current conditions
        double technologyAdvancement = globalState.getIndicator("technology_advancement");
        double economicGrowth = globalState.getIndicator("economic_growth_rate");
        double resourceAvailability = globalState.getIndicator("resource_availability");
        double globalTemp = globalState.getIndicator("global_temperature");
        
        // Decision logic based on researcher characteristics and global state
        String decisionType;
        double confidence;
        double expectedImpact;
        
        if (fundingAvailability > 0.7 && researchExpertise > 0.6) {
            // Good funding and expertise - pursue breakthrough research
            decisionType = "breakthrough_research";
            confidence = 0.8;
            expectedImpact = researchExpertise * innovationPotential * 0.4;
        } else if (technologyAdvancement > 0.8 && innovationPotential > 0.7) {
            // High tech advancement and innovation potential - focus on applications
            decisionType = "applied_research";
            confidence = 0.75;
            expectedImpact = technologyAdvancement * innovationPotential * 0.3;
        } else if (globalTemp > 20 || resourceAvailability < 0.4) {
            // Environmental challenges - focus on sustainability research
            decisionType = "sustainability_research";
            confidence = 0.7;
            expectedImpact = -Math.abs(globalTemp - 15) * 0.2;
        } else if (collaborationNetwork > 0.6 && economicGrowth > 0.03) {
            // Good collaboration network and economic growth - collaborative research
            decisionType = "collaborative_research";
            confidence = 0.8;
            expectedImpact = collaborationNetwork * economicGrowth * 0.3;
        } else {
            // Standard research activities
            decisionType = "standard_research";
            confidence = 0.6;
            expectedImpact = researchExpertise * 0.1;
        }
        
        AgentDecision decision = new AgentDecision(decisionType, agentId, currentTime);
        decision.setConfidence(confidence);
        decision.setExpectedImpact(expectedImpact);
        
        // Add decision-specific parameters
        decision.setParameter("research_expertise", researchExpertise);
        decision.setParameter("funding_availability", fundingAvailability);
        decision.setParameter("innovation_potential", innovationPotential);
        decision.setParameter("collaboration_network", collaborationNetwork);
        decision.setMetadata("research_field", researchField);
        decision.setMetadata("decision_reason", getDecisionReason(decisionType));
        
        return decision;
    }
    
    @Override
    public void updateFromGlobalState(GlobalState globalState) {
        // Update internal factors based on global state changes
        double economicGrowth = globalState.getIndicator("economic_growth_rate");
        double technologyAdvancement = globalState.getIndicator("technology_advancement");
        double globalPeace = globalState.getIndicator("world_peace_index");
        
        // Adjust funding availability based on economic growth
        fundingAvailability = Math.max(0.0, Math.min(1.0, 
            fundingAvailability + economicGrowth * 0.2));
        setInternalFactor("funding_availability", fundingAvailability);
        
        // Adjust research expertise based on technology advancement
        researchExpertise = Math.min(1.0, researchExpertise + technologyAdvancement * 0.05);
        setInternalFactor("research_expertise", researchExpertise);
        
        // Adjust collaboration network based on global peace
        collaborationNetwork = Math.max(0.0, Math.min(1.0, 
            collaborationNetwork + (globalPeace - 0.5) * 0.1));
        setInternalFactor("collaboration_network", collaborationNetwork);
    }
    
    @Override
    public void influenceGlobalState(GlobalState globalState, AgentDecision decision) {
        // Apply researcher influence to global state based on decision
        double influenceStrength = calculateInfluenceStrength();
        double impact = decision.getExpectedImpact() * influenceStrength;
        
        switch (decision.getDecisionType()) {
            case "breakthrough_research":
                // Breakthrough research increases technology advancement
                double currentTech = globalState.getIndicator("technology_advancement");
                globalState.updateIndicator("technology_advancement", 
                    Math.min(1.0, currentTech + impact * 0.03));
                break;
                
            case "applied_research":
                // Applied research increases economic growth
                double currentGrowth = globalState.getIndicator("economic_growth_rate");
                globalState.updateIndicator("economic_growth_rate", currentGrowth + impact * 0.01);
                break;
                
            case "sustainability_research":
                // Sustainability research improves resource availability
                double currentResources = globalState.getIndicator("resource_availability");
                globalState.updateIndicator("resource_availability", 
                    Math.min(1.0, currentResources + impact * 0.02));
                break;
                
            case "collaborative_research":
                // Collaborative research improves global cooperation
                double currentPeace = globalState.getIndicator("world_peace_index");
                globalState.updateIndicator("world_peace_index", 
                    Math.min(1.0, currentPeace + impact * 0.01));
                break;
        }
    }
    
    @Override
    public void collaborate(Agent otherAgent, double currentTime) {
        if (otherAgent instanceof ResearcherAgent) {
            // Researcher-to-researcher collaboration
            ResearcherAgent otherResearcher = (ResearcherAgent) otherAgent;
            
            // Share expertise and increase collaboration network
            double expertiseBenefit = (researchExpertise + otherResearcher.researchExpertise) / 2;
            researchExpertise = Math.min(1.0, researchExpertise + expertiseBenefit * 0.1);
            setInternalFactor("research_expertise", researchExpertise);
            
            // Increase collaboration network
            collaborationNetwork = Math.min(1.0, collaborationNetwork + 0.1);
            setInternalFactor("collaboration_network", collaborationNetwork);
            
            // Update local data
            setLocalData("collaboration_partner", otherResearcher.getAgentId());
            setLocalData("last_collaboration", String.valueOf(currentTime));
        } else if (otherAgent instanceof CompanyAgent) {
            // Researcher-company collaboration
            CompanyAgent company = (CompanyAgent) otherAgent;
            
            // Increase innovation potential through industry collaboration
            double industryBenefit = company.getInternalFactor("innovation_level") * 0.15;
            innovationPotential = Math.min(1.0, innovationPotential + industryBenefit);
            setInternalFactor("innovation_potential", innovationPotential);
            
            // Increase funding availability
            fundingAvailability = Math.min(1.0, fundingAvailability + 0.1);
            setInternalFactor("funding_availability", fundingAvailability);
        }
    }
    
    @Override
    public double calculateInfluenceStrength() {
        // Researcher influence based on expertise, funding, and collaboration network
        double expertiseInfluence = researchExpertise;
        double fundingInfluence = fundingAvailability;
        double networkInfluence = collaborationNetwork;
        double innovationInfluence = innovationPotential;
        
        return (expertiseInfluence + fundingInfluence + networkInfluence + innovationInfluence) / 4.0;
    }
    
    private String getRandomResearchField() {
        String[] fields = {"artificial_intelligence", "biotechnology", "climate_science", 
                          "renewable_energy", "materials_science", "quantum_computing", 
                          "neuroscience", "nanotechnology", "space_science", "data_science"};
        return fields[random.nextInt(fields.length)];
    }
    
    private String getRandomInstitutionType() {
        String[] types = {"university", "research_institute", "government_lab", "private_lab", "startup"};
        return types[random.nextInt(types.length)];
    }
    
    private String getRandomCareerStage() {
        String[] stages = {"graduate_student", "postdoc", "assistant_professor", 
                          "associate_professor", "full_professor", "research_director"};
        return stages[random.nextInt(stages.length)];
    }
    
    private String getDecisionReason(String decisionType) {
        switch (decisionType) {
            case "breakthrough_research": return "Good funding and expertise enable breakthrough research";
            case "applied_research": return "High technology advancement supports applied research";
            case "sustainability_research": return "Environmental challenges require sustainability focus";
            case "collaborative_research": return "Strong collaboration network enables joint research";
            case "standard_research": return "Standard research activities based on current conditions";
            default: return "Standard research decision";
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("  Research Field: ").append(researchField).append("\n");
        sb.append("  Expertise: ").append(String.format("%.2f", researchExpertise)).append("\n");
        sb.append("  Funding: ").append(String.format("%.2f", fundingAvailability)).append("\n");
        sb.append("  Collaboration Network: ").append(String.format("%.2f", collaborationNetwork)).append("\n");
        sb.append("  Innovation Potential: ").append(String.format("%.2f", innovationPotential)).append("\n");
        return sb.toString();
    }
}
