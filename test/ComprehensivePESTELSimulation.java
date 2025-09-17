package test;

import simu.framework.Trace;
import simu.framework.Trace.Level;
import simu.model.*;

/**
 * Comprehensive PESTEL Simulation with all top companies, countries, and research centers
 * Demonstrates complete real-world strategic planning system for Metropolia University
 */
public class ComprehensivePESTELSimulation {
    
    public static void main(String[] args) {
        Trace.setTraceLevel(Level.INFO);
        
        System.out.println("=== COMPREHENSIVE REAL-WORLD PESTEL SIMULATION ===");
        System.out.println("Metropolia University Strategic Planning System");
        System.out.println("Complete Global Coverage: Top 50 Companies, 30 Countries, 25 Research Centers");
        System.out.println("Powered by Local Qwen3-Next-80B-A3B-Thinking Model");
        System.out.println();
        
        // Display comprehensive entities
        displayComprehensiveEntities();
        
        // Run comprehensive simulation
        runComprehensiveSimulation();
        
        System.out.println("\n=== COMPREHENSIVE SIMULATION COMPLETED ===");
    }
    
    private static void displayComprehensiveEntities() {
        System.out.println("🌍 COMPREHENSIVE REAL-WORLD ENTITIES:");
        
        System.out.println("\n🏢 TOP 50 GLOBAL COMPANIES:");
        System.out.println("   Technology: Apple ($391B), Microsoft ($365B), NVIDIA ($118B), Samsung ($279B)");
        System.out.println("   Retail: Walmart ($681B), Amazon ($638B), Costco ($249B), Home Depot ($157B)");
        System.out.println("   Energy: Saudi Aramco ($480B), ExxonMobil ($413B), Shell ($381B), Chevron ($235B)");
        System.out.println("   Healthcare: UnitedHealth ($400B), J&J ($100B), Pfizer ($58B), Roche ($71B)");
        System.out.println("   Financial: JPMorgan ($158B), Bank of America ($119B), Goldman Sachs ($59B)");
        System.out.println("   Automotive: Volkswagen ($295B), Toyota ($274B), Tesla ($127B), Mercedes ($186B)");
        System.out.println("   + 26 more global industry leaders...");
        
        System.out.println("\n🏛️ TOP 30 INFLUENTIAL COUNTRIES:");
        System.out.println("   Superpowers: USA ($26.9T GDP), China ($17.7T GDP), Japan ($4.9T GDP)");
        System.out.println("   European Powers: Germany ($4.3T), UK ($3.1T), France ($2.9T), Italy ($2.1T)");
        System.out.println("   Asian Tigers: South Korea ($1.8T), Singapore ($397B), Taiwan ($791B)");
        System.out.println("   Emerging Giants: India ($3.4T), Brazil ($1.6T), Russia ($2.2T)");
        System.out.println("   Regional Leaders: Canada ($2.0T), Australia ($1.6T), Saudi Arabia ($834B)");
        System.out.println("   + 15 more influential nations...");
        
        System.out.println("\n🔬 TOP 25 RESEARCH INSTITUTIONS:");
        System.out.println("   US Elite: MIT, Stanford, Harvard, Caltech, Princeton, Yale");
        System.out.println("   International: Oxford, Cambridge, ETH Zurich, University of Toronto");
        System.out.println("   Asian Leaders: Chinese Academy of Sciences, Tsinghua, University of Tokyo, RIKEN");
        System.out.println("   European Excellence: Max Planck Society, CERN, CNRS, Karolinska Institute");
        System.out.println("   + 11 more world-class research centers...");
        
        System.out.println("\n🤝 INTERNATIONAL ORGANIZATIONS:");
        System.out.println("   Economic: EU (27 members), G7, G20, BRICS, ASEAN");
        System.out.println("   Security: NATO, Indo-Pacific partnerships");
        System.out.println("   Regional: Nordic Council, USMCA");
        
        System.out.println("\n🔮 12 ALTERNATIVE FUTURE SCENARIOS:");
        for (ComprehensiveRealWorldData.FutureScenario scenario : ComprehensiveRealWorldData.ALTERNATIVE_FUTURES) {
            System.out.println(String.format("   • %s (%.0f%% base probability)", 
                scenario.name, scenario.probability * 100));
        }
        System.out.println();
    }
    
    private static void runComprehensiveSimulation() {
        System.out.println("🚀 STARTING COMPREHENSIVE SIMULATION");
        System.out.println("Duration: 21 days (3 weeks strategic analysis)");
        System.out.println("Entities: 105 total (50 companies + 30 countries + 25 research centers)");
        System.out.println();
        
        try {
            // Create comprehensive simulation
            RealWorldPESTELEngine engine = new RealWorldPESTELEngine(21);
            engine.setSimulationTime(21.0);
            
            System.out.println("✅ Comprehensive simulation initialized");
            System.out.println("AI Integration: " + (engine.isAIEnabled() ? 
                "Local Qwen3-Next-80B-A3B-Thinking" : "Advanced Fallback Logic"));
            System.out.println();
            
            long startTime = System.currentTimeMillis();
            engine.run();
            long endTime = System.currentTimeMillis();
            
            System.out.println("\n🎉 COMPREHENSIVE SIMULATION COMPLETED SUCCESSFULLY");
            System.out.println("Execution time: " + (endTime - startTime) + " ms");
            
            // Display comprehensive results
            displayComprehensiveResults(engine);
            
        } catch (Exception e) {
            System.out.println("❌ Simulation error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void displayComprehensiveResults(RealWorldPESTELEngine engine) {
        System.out.println("\n=== COMPREHENSIVE SIMULATION RESULTS ===");
        
        // Get comprehensive scenario analysis
        EnhancedFutureScenarioManager.ScenarioAnalysis analysis = 
            engine.getEnhancedFutureManager().generateScenarioAnalysis();
        
        // Display top scenarios with detailed analysis
        System.out.println("\n🏆 FINAL ALTERNATIVE FUTURES RANKING:");
        List<EnhancedFutureScenarioManager.FutureScenario> topScenarios = 
            engine.getEnhancedFutureManager().getTopScenarios();
        
        for (int i = 0; i < topScenarios.size(); i++) {
            EnhancedFutureScenarioManager.FutureScenario scenario = topScenarios.get(i);
            String medal = i == 0 ? "🥇" : i == 1 ? "🥈" : "🥉";
            System.out.println(String.format("  %s %s: %.1f%% probability (%.2f momentum)", 
                medal, scenario.getName(), scenario.getProbability() * 100, scenario.getMomentum()));
            System.out.println(String.format("     Sector: %s", scenario.getDominantSector()));
            System.out.println(String.format("     Description: %s", scenario.getDescription()));
            
            if (!scenario.getSupportingActions().isEmpty()) {
                System.out.println("     Recent Supporting Actions:");
                scenario.getSupportingActions().stream().limit(3).forEach(action -> 
                    System.out.println("       - " + action));
            }
            System.out.println();
        }
        
        // Display sector analysis
        System.out.println("📊 SECTOR PROBABILITY ANALYSIS:");
        Map<String, Double> sectorProbs = analysis.getSectorProbabilities();
        sectorProbs.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .forEach(entry -> System.out.println(String.format("  %s: %.1f%% combined probability", 
                entry.getKey(), entry.getValue() * 100)));
        
        // Display key trends
        System.out.println("\n📈 KEY STRATEGIC TRENDS:");
        for (String trend : analysis.getKeyTrends()) {
            System.out.println("  • " + trend);
        }
        
        // Display entity performance
        displayEntityPerformance(engine);
        
        // Display strategic recommendations
        System.out.println("\n🎯 STRATEGIC RECOMMENDATIONS FOR METROPOLIA UNIVERSITY:");
        for (String recommendation : analysis.getStrategicRecommendations()) {
            System.out.println("  • " + recommendation);
        }
        
        // Display simulation statistics
        displaySimulationStatistics(engine);
    }
    
    private static void displayEntityPerformance(RealWorldPESTELEngine engine) {
        System.out.println("\n📊 TOP PERFORMING ENTITIES:");
        
        // Most active companies by sector
        Map<String, List<RealWorldCompany>> companiesBySector = engine.getCompanies().stream()
            .collect(java.util.stream.Collectors.groupingBy(c -> extractMainSector(c.getCompanyData().industry)));
        
        System.out.println("\n🏢 MOST ACTIVE COMPANIES BY SECTOR:");
        companiesBySector.forEach((sector, companies) -> {
            RealWorldCompany mostActive = companies.stream()
                .max((c1, c2) -> Integer.compare(c1.getDecisionCount(), c2.getDecisionCount()))
                .orElse(null);
            if (mostActive != null && mostActive.getDecisionCount() > 0) {
                System.out.println(String.format("  %s: %s (%d decisions, $%.0fB revenue)", 
                    sector, mostActive.getCompanyData().name, mostActive.getDecisionCount(), 
                    mostActive.getCompanyData().revenue / 1000));
            }
        });
        
        // Most influential countries by region
        Map<String, List<RealWorldCountry>> countriesByRegion = engine.getCountries().stream()
            .collect(java.util.stream.Collectors.groupingBy(c -> c.getCountryData().region));
        
        System.out.println("\n🏛️ MOST ACTIVE COUNTRIES BY REGION:");
        countriesByRegion.forEach((region, countries) -> {
            RealWorldCountry mostActive = countries.stream()
                .max((c1, c2) -> Integer.compare(c1.getDecisionCount(), c2.getDecisionCount()))
                .orElse(null);
            if (mostActive != null && mostActive.getDecisionCount() > 0) {
                System.out.println(String.format("  %s: %s (%d policies, $%.0fB GDP)", 
                    region, mostActive.getCountryData().name, mostActive.getDecisionCount(), 
                    mostActive.getCountryData().gdp / 1000.0));
            }
        });
        
        // Most productive research institutions
        System.out.println("\n🔬 MOST PRODUCTIVE RESEARCH INSTITUTIONS:");
        engine.getResearchers().stream()
            .filter(r -> r.getDecisionCount() > 0)
            .sorted((r1, r2) -> Integer.compare(r2.getDecisionCount(), r1.getDecisionCount()))
            .limit(5)
            .forEach(researcher -> System.out.println(String.format("  %s: %d initiatives (Impact: %.2f)", 
                researcher.getResearchData().name, researcher.getDecisionCount(), researcher.getResearchImpact())));
    }
    
    private static void displaySimulationStatistics(RealWorldPESTELEngine engine) {
        System.out.println("\n📈 SIMULATION STATISTICS:");
        
        int totalDecisions = engine.getCompanies().stream().mapToInt(PESTELAgent::getDecisionCount).sum() +
                           engine.getCountries().stream().mapToInt(PESTELAgent::getDecisionCount).sum() +
                           engine.getResearchers().stream().mapToInt(PESTELAgent::getDecisionCount).sum();
        
        int totalEntities = engine.getCompanies().size() + engine.getCountries().size() + engine.getResearchers().size();
        
        System.out.println("  Total Entities: " + totalEntities);
        System.out.println("  Total Strategic Decisions: " + totalDecisions);
        System.out.println("  Average Decisions per Entity: " + String.format("%.2f", (double) totalDecisions / totalEntities));
        System.out.println("  PESTEL Changes: " + engine.getRecentChanges().size());
        System.out.println("  Agent Interactions: " + engine.getRecentActions().size());
        
        // Decision distribution
        System.out.println("\n📊 DECISION DISTRIBUTION:");
        System.out.println("  Companies: " + engine.getCompanies().stream().mapToInt(PESTELAgent::getDecisionCount).sum() + " decisions");
        System.out.println("  Countries: " + engine.getCountries().stream().mapToInt(PESTELAgent::getDecisionCount).sum() + " decisions");
        System.out.println("  Research: " + engine.getResearchers().stream().mapToInt(PESTELAgent::getDecisionCount).sum() + " decisions");
    }
    
    private static String extractMainSector(String industry) {
        String industryLower = industry.toLowerCase();
        if (industryLower.contains("technology") || industryLower.contains("software")) return "Technology";
        if (industryLower.contains("energy") || industryLower.contains("oil")) return "Energy";
        if (industryLower.contains("healthcare") || industryLower.contains("pharmaceutical")) return "Healthcare";
        if (industryLower.contains("financial") || industryLower.contains("bank")) return "Financial";
        if (industryLower.contains("automotive")) return "Automotive";
        if (industryLower.contains("retail")) return "Retail";
        if (industryLower.contains("telecommunications")) return "Telecom";
        if (industryLower.contains("aerospace")) return "Aerospace";
        return "Other";
    }
}
