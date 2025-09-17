package simu.model;

import java.util.List;

/**
 * Researcher agent with specialized PESTEL factors for academic/research context
 */
public class ResearcherPESTELAgent extends PESTELAgent {
    private String researchField;
    private String institutionType;
    private String careerStage;
    
    public ResearcherPESTELAgent(String agentId) {
        super(agentId, AgentType.RESEARCHER);
        
        // Initialize researcher-specific attributes
        String[] fields = {"artificial_intelligence", "biotechnology", "climate_science", 
                          "renewable_energy", "materials_science", "quantum_computing", 
                          "neuroscience", "nanotechnology", "space_science", "data_science"};
        String[] institutions = {"university", "research_institute", "government_lab", "private_lab"};
        String[] stages = {"graduate_student", "postdoc", "assistant_professor", 
                          "associate_professor", "full_professor", "research_director"};
        
        this.researchField = fields[(int)(Math.random() * fields.length)];
        this.institutionType = institutions[(int)(Math.random() * institutions.length)];
        this.careerStage = stages[(int)(Math.random() * stages.length)];
    }
    
    @Override
    protected void initializeLocalPESTEL() {
        // Researcher-specific PESTEL factors (adapted for academic context)
        
        // Political factors (funding and policy)
        localPESTEL.setPolitical("research_funding_policy", "Stable government research funding with competitive grants");
        localPESTEL.setPolitical("academic_freedom", "Strong protection of academic freedom and research independence");
        localPESTEL.setPolitical("international_collaboration", "Open policies for international research collaboration");
        
        // Economic factors (funding and resources)
        localPESTEL.setEconomic("funding_availability", "Moderate funding availability from multiple sources");
        localPESTEL.setEconomic("research_costs", "Standard research costs with access to shared facilities");
        localPESTEL.setEconomic("career_prospects", "Competitive but viable career progression opportunities");
        
        // Social factors (collaboration and impact)
        localPESTEL.setSocial("research_community", "Active participation in global research community");
        localPESTEL.setSocial("public_engagement", "Regular engagement with public and media on research topics");
        localPESTEL.setSocial("student_mentoring", "Committed to training next generation of researchers");
        
        // Technological factors (tools and methods)
        localPESTEL.setTechnological("research_infrastructure", "Access to state-of-the-art research equipment and computing");
        localPESTEL.setTechnological("methodological_advances", "Keeping up with latest methodological developments");
        localPESTEL.setTechnological("data_access", "Good access to research data and databases");
        
        // Environmental factors (sustainability and ethics)
        localPESTEL.setEnvironmental("research_ethics", "Strong commitment to ethical research practices");
        localPESTEL.setEnvironmental("sustainability", "Incorporating sustainability principles in research");
        localPESTEL.setEnvironmental("environmental_impact", "Minimizing environmental impact of research activities");
        
        // Legal factors (IP and compliance)
        localPESTEL.setLegal("intellectual_property", "Clear understanding of IP rights and patent processes");
        localPESTEL.setLegal("research_compliance", "Full compliance with research regulations and ethics boards");
        localPESTEL.setLegal("publication_rights", "Understanding of publication rights and open access policies");
    }
    
    @Override
    public AgentDecision makeDecision(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions) {
        String context = getCurrentContext(globalPESTEL, currentDay, recentAgentActions);
        
        // Researcher decision logic based on PESTEL analysis
        String decision = analyzeAndDecide(globalPESTEL, currentDay, recentAgentActions);
        
        if (decision != null && !decision.equals("no_action")) {
            return new AgentDecision(agentId, currentDay, decision, "research_decision", 0.75);
        }
        
        return null; // No action taken
    }
    
    private String analyzeAndDecide(PESTELState globalPESTEL, int currentDay, List<AgentAction> recentAgentActions) {
        // Analyze global conditions for research decisions
        String globalTechInnovation = globalPESTEL.getTechnological("innovation");
        String globalFunding = globalPESTEL.getEconomic("growth");
        String globalClimate = globalPESTEL.getEnvironmental("climate_change");
        
        // React to other agents' actions
        for (AgentAction action : recentAgentActions) {
            if (action.getAgentId().startsWith("Company_") && 
                action.getActionDescription().contains("R&D")) {
                return "Propose industry-academia collaboration to leverage mutual expertise";
            }
            if (action.getAgentId().startsWith("Country_") && 
                action.getActionDescription().contains("innovation")) {
                return "Apply for government innovation grants and policy advisory roles";
            }
        }
        
        // Research decisions based on field and global conditions
        if (researchField.equals("climate_science") && globalClimate.contains("urgent")) {
            return "Launch urgent climate research initiative with international partners";
        }
        
        if (researchField.equals("artificial_intelligence") && globalTechInnovation.contains("high")) {
            return "Develop new AI methodology and publish breakthrough research";
        }
        
        if (researchField.equals("biotechnology") && globalFunding.contains("growth")) {
            return "Initiate biotechnology research project with potential commercial applications";
        }
        
        // Regular research activities
        if (currentDay % 60 == 0) { // Every 2 months - major research milestone
            return "Complete major research milestone and prepare publications";
        }
        
        if (currentDay % 30 == 10) { // Monthly collaboration
            return "Engage in research collaboration and knowledge sharing activities";
        }
        
        if (currentDay % 7 == 0) { // Weekly research progress
            if (Math.random() < 0.4) { // 40% chance
                String[] activities = {
                    "Conduct breakthrough experiment yielding significant results",
                    "Present research findings at international conference",
                    "Submit grant proposal for innovative research project",
                    "Establish new research partnership with leading institution",
                    "Publish high-impact paper in prestigious journal",
                    "Mentor graduate students and advance their research projects"
                };
                return activities[(int)(Math.random() * activities.length)];
            }
        }
        
        return "no_action";
    }
    
    @Override
    public void updateFromPESTELChanges(List<PESTELChange> changes) {
        for (PESTELChange change : changes) {
            // Update local PESTEL based on global changes
            switch (change.getCategory().toLowerCase()) {
                case "technological":
                case "t":
                    localPESTEL.setTechnological("methodological_advances",
                        "Research methods updated due to: " + change.getNewValue());
                    break;
                case "economic":
                case "e":
                    localPESTEL.setEconomic("funding_availability",
                        "Funding strategy adjusted due to: " + change.getNewValue());
                    break;
                case "political":
                case "p":
                    localPESTEL.setPolitical("research_funding_policy",
                        "Research policy adapted due to: " + change.getNewValue());
                    break;
                case "environmental":
                case "env":
                    if (researchField.contains("climate") || researchField.contains("environment")) {
                        localPESTEL.setEnvironmental("research_ethics",
                            "Research focus intensified due to: " + change.getNewValue());
                    }
                    break;
            }
        }
    }
    
    @Override
    public String getAgentDescription() {
        return String.format("Researcher %s - Field: %s, Institution: %s, Stage: %s", 
                           agentId, researchField, institutionType, careerStage);
    }
    
    public String getResearchField() {
        return researchField;
    }
    
    public String getInstitutionType() {
        return institutionType;
    }
    
    public String getCareerStage() {
        return careerStage;
    }
}
