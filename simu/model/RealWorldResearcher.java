package simu.model;

import java.util.List;

/**
 * Real-world research institution with actual research data and academic behavior
 */
public class RealWorldResearcher extends PESTELAgent {
    private ComprehensiveRealWorldData.ResearchData researchData;
    private double researchImpact;
    private double globalRanking;
    private double fundingLevel;
    private double collaborationNetwork;
    
    public RealWorldResearcher(ComprehensiveRealWorldData.ResearchData researchData) {
        super(researchData.name, AgentType.RESEARCHER);
        this.researchData = researchData;
        this.researchImpact = calculateResearchImpact();
        this.globalRanking = calculateGlobalRanking();
        this.fundingLevel = calculateFundingLevel();
        this.collaborationNetwork = calculateCollaborationNetwork();
        
        // Re-initialize PESTEL with research data now available
        initializeLocalPESTEL();
    }
    
    @Override
    protected void initializeLocalPESTEL() {
        // Research institution-specific PESTEL
        localPESTEL.setPolitical("research_policy", 
            String.format("%s operates under %s research governance with strong academic freedom", 
                         researchData.name, researchData.country));
        localPESTEL.setPolitical("funding_policy", 
            String.format("Access to %s research funding with global ranking: %.2f", 
                         researchData.type, globalRanking));
        
        localPESTEL.setEconomic("research_budget", 
            String.format("Annual research budget reflecting funding level: %.2f in %s", 
                         fundingLevel, researchData.fields));
        localPESTEL.setEconomic("commercialization", 
            String.format("Technology transfer and commercialization programs for %s research", 
                         researchData.fields));
        
        localPESTEL.setSocial("academic_community", 
            String.format("Leading position in global %s research community", researchData.fields));
        localPESTEL.setSocial("student_body", 
            String.format("Diverse international student and researcher population in %s", researchData.location));
        
        localPESTEL.setTechnological("research_infrastructure", 
            String.format("World-class research facilities for %s with impact score: %.2f", 
                         researchData.fields, researchImpact));
        localPESTEL.setTechnological("innovation_pipeline", 
            String.format("Active innovation pipeline in %s with strong collaboration network: %.2f", 
                         researchData.fields, collaborationNetwork));
        
        localPESTEL.setEnvironmental("sustainable_research", 
            String.format("Commitment to sustainable research practices and environmental responsibility"));
        localPESTEL.setEnvironmental("climate_research", 
            String.format("Active climate research programs contributing to global sustainability goals"));
        
        localPESTEL.setLegal("research_ethics", 
            String.format("Strict research ethics framework and institutional review processes"));
        localPESTEL.setLegal("intellectual_property", 
            String.format("Comprehensive IP management and technology transfer policies"));
    }
    
    @Override
    public AgentDecision makeDecision(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions) {
        String decision = generateResearchDecision(globalPESTEL, currentDay, recentAgentActions);
        
        if (decision != null && !decision.equals("no_action")) {
            return new AgentDecision(agentId, currentDay, decision, "research_strategy", 
                                   calculateDecisionConfidence());
        }
        
        return null;
    }
    
    private String generateResearchDecision(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions) {
        // Institution-specific research decisions
        switch (researchData.name) {
            case "MIT":
                return generateMITDecision(globalPESTEL, currentDay, recentAgentActions);
            case "Stanford University":
                return generateStanfordDecision(globalPESTEL, currentDay, recentAgentActions);
            case "Chinese Academy of Sciences":
                return generateCASDecision(globalPESTEL, currentDay, recentAgentActions);
            case "Max Planck Society":
                return generateMaxPlanckDecision(globalPESTEL, currentDay, recentAgentActions);
            case "Harvard University":
                return generateHarvardDecision(globalPESTEL, currentDay, recentAgentActions);
            default:
                return generateGenericResearchDecision(globalPESTEL, currentDay, recentAgentActions);
        }
    }
    
    private String generateMITDecision(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions) {
        if (currentDay % 60 == 0) {
            return "Launch breakthrough quantum computing initiative with $500M investment and industry partnerships";
        } else if (hasAIRelatedActivity(recentAgentActions)) {
            return "Establish new AI safety research center with focus on responsible AI development";
        } else if (Math.random() < 0.4) {
            return "Create interdisciplinary climate technology lab combining engineering and policy research";
        }
        return Math.random() < 0.3 ? "Launch startup incubator program for deep tech companies" : "no_action";
    }
    
    private String generateStanfordDecision(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions) {
        if (currentDay % 45 == 0) {
            return "Establish Human-Centered AI Institute with $300M funding from Silicon Valley partners";
        } else if (Math.random() < 0.35) {
            return "Launch precision medicine initiative combining AI, genomics, and clinical research";
        }
        return Math.random() < 0.25 ? "Create sustainable technology accelerator for clean energy startups" : "no_action";
    }
    
    private String generateCASDecision(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions) {
        if (currentDay % 90 == 0) {
            return "Launch comprehensive space exploration program with lunar research station development";
        } else if (Math.random() < 0.4) {
            return "Establish Belt and Road research collaboration network for sustainable development";
        }
        return Math.random() < 0.3 ? "Invest in advanced materials research for next-generation manufacturing" : "no_action";
    }
    
    private String generateMaxPlanckDecision(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions) {
        if (currentDay % 70 == 0) {
            return "Launch European quantum research consortium with €200M EU funding";
        } else if (Math.random() < 0.3) {
            return "Establish fundamental physics research program exploring dark matter and quantum gravity";
        }
        return Math.random() < 0.2 ? "Create international fellowship program for young physicists" : "no_action";
    }
    
    private String generateHarvardDecision(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions) {
        if (currentDay % 50 == 0) {
            return "Launch global health initiative with $400M investment in pandemic preparedness research";
        } else if (Math.random() < 0.35) {
            return "Establish public policy research center focusing on AI governance and digital democracy";
        }
        return Math.random() < 0.25 ? "Create interdisciplinary aging research institute with biotech industry partnerships" : "no_action";
    }
    
    private String generateGenericResearchDecision(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions) {
        if (Math.random() < 0.3) {
            String[] decisions = {
                String.format("Expand %s research capabilities with new international partnerships", researchData.fields),
                String.format("Launch graduate fellowship program in %s for emerging researchers", researchData.fields),
                String.format("Establish research collaboration with %s institutions globally", researchData.country),
                String.format("Create innovation hub for %s technology transfer and startups", researchData.fields),
                String.format("Implement open science initiatives for %s research data sharing", researchData.fields)
            };
            return decisions[(int)(Math.random() * decisions.length)];
        }
        return "no_action";
    }
    
    private boolean hasAIRelatedActivity(List<AgentAction> recentActions) {
        return recentActions.stream().anyMatch(action -> 
            action.getActionDescription().toLowerCase().contains("ai") ||
            action.getActionDescription().toLowerCase().contains("artificial intelligence") ||
            action.getActionDescription().toLowerCase().contains("machine learning"));
    }
    
    @Override
    public void updateFromPESTELChanges(List<PESTELChange> changes) {
        for (PESTELChange change : changes) {
            if (isResearchRelevant(change)) {
                updateResearchStrategy(change);
            }
        }
    }
    
    private boolean isResearchRelevant(PESTELChange change) {
        return change.getNewValue().toLowerCase().contains(researchData.fields.toLowerCase()) ||
               change.getNewValue().toLowerCase().contains("research") ||
               change.getNewValue().toLowerCase().contains("innovation") ||
               change.getCategory().equals("technological");
    }
    
    private void updateResearchStrategy(PESTELChange change) {
        String strategy = String.format("Research strategy adapted to leverage %s developments in %s", 
                                       change.getNewValue(), change.getCategory());
        localPESTEL.updateFactor(change.getCategory(), "research_adaptation", strategy);
    }
    
    @Override
    public String getAgentDescription() {
        return String.format("%s (%s) - Fields: %s, Location: %s, Type: %s", 
                           researchData.name, researchData.country, researchData.fields, 
                           researchData.location, researchData.type);
    }
    
    private double calculateResearchImpact() {
        // Based on institution prestige and research fields
        if (researchData.name.contains("MIT") || researchData.name.contains("Stanford") || 
            researchData.name.contains("Harvard")) {
            return 0.9 + Math.random() * 0.1; // 0.9-1.0
        } else if (researchData.name.contains("Chinese Academy") || researchData.name.contains("Max Planck")) {
            return 0.8 + Math.random() * 0.2; // 0.8-1.0
        } else {
            return 0.6 + Math.random() * 0.3; // 0.6-0.9
        }
    }
    
    private double calculateGlobalRanking() {
        // Inverse relationship - lower number = higher ranking
        if (researchData.name.contains("MIT")) return 0.95;
        if (researchData.name.contains("Stanford")) return 0.93;
        if (researchData.name.contains("Harvard")) return 0.91;
        if (researchData.name.contains("Chinese Academy")) return 0.89;
        if (researchData.name.contains("Max Planck")) return 0.87;
        return 0.7 + Math.random() * 0.2;
    }
    
    private double calculateFundingLevel() {
        // Based on country and institution type
        if (researchData.country.equals("USA") && researchData.type.equals("University")) {
            return 0.8 + Math.random() * 0.2;
        } else if (researchData.type.equals("Government")) {
            return 0.7 + Math.random() * 0.3;
        } else {
            return 0.5 + Math.random() * 0.4;
        }
    }
    
    private double calculateCollaborationNetwork() {
        return globalRanking * 0.7 + Math.random() * 0.3;
    }
    
    private double calculateDecisionConfidence() {
        return 0.5 + (researchImpact * 0.3) + (globalRanking * 0.2);
    }
    
    // Getters
    public ComprehensiveRealWorldData.ResearchData getResearchData() {
        return researchData;
    }
    
    public double getResearchImpact() {
        return researchImpact;
    }
    
    public double getGlobalRanking() {
        return globalRanking;
    }
    
    public double getFundingLevel() {
        return fundingLevel;
    }
    
    public double getCollaborationNetwork() {
        return collaborationNetwork;
    }
}
