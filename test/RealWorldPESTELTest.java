package test;

import simu.framework.Trace;
import simu.framework.Trace.Level;
import simu.model.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Real-World PESTEL Test using actual global companies, countries, and research institutions
 * with alternative future scenarios and AI-driven decision making
 */
public class RealWorldPESTELTest {
    
    public static void main(String[] args) {
        Trace.setTraceLevel(Level.INFO);
        
        System.out.println("=== REAL-WORLD PESTEL AI SIMULATION ===");
        System.out.println("Metropolia University Strategic Planning");
        System.out.println("Using Top Global Companies, Countries & Research Centers");
        System.out.println("Powered by Qwen3-Next-80B-A3B-Thinking Model");
        System.out.println();
        
        // Run different real-world scenarios
        runRealWorldScenario();
        runAlternativeFuturesAnalysis();
        
        System.out.println("\n=== REAL-WORLD PESTEL SIMULATION COMPLETED ===");
    }
    
    /**
     * Run main real-world scenario simulation
     */
    private static void runRealWorldScenario() {
        System.out.println("=== REAL-WORLD SCENARIO (30 DAYS) ===");
        System.out.println();
        
        // Display real entities being simulated
        displayRealWorldEntities();
        
        // Create and run simulation
        RealWorldPESTELEngine engine = new RealWorldPESTELEngine(30);
        engine.setSimulationTime(30.0);
        
        System.out.println("🚀 Starting Real-World PESTEL Simulation...");
        System.out.println("Duration: 30 days");
        System.out.println("AI Integration: " + (engine.isAIEnabled() ? "Qwen3-Next-80B-A3B-Thinking Enabled" : "Advanced Fallback Mode"));
        System.out.println();
        
        long startTime = System.currentTimeMillis();
        engine.run();
        long endTime = System.currentTimeMillis();
        
        System.out.println("\n✅ Real-World simulation completed in " + (endTime - startTime) + " ms");
        
        // Display comprehensive results
        displayComprehensiveResults(engine);
    }
    
    /**
     * Display real-world entities in the simulation
     */
    private static void displayRealWorldEntities() {
        System.out.println("📊 REAL-WORLD ENTITIES IN SIMULATION:");
        
        System.out.println("\n🏢 TOP GLOBAL COMPANIES:");
        for (RealWorldData.CompanyData company : RealWorldData.TOP_COMPANIES) {
            System.out.println(String.format("  • %s - $%.0fB revenue (%s, %s)", 
                company.name, company.revenue / 1000, company.industry, company.country));
        }
        
        System.out.println("\n🏛️ INFLUENTIAL COUNTRIES:");
        for (RealWorldData.CountryData country : RealWorldData.TOP_COUNTRIES) {
            System.out.println(String.format("  • %s - %.0fM population, $%.0fB GDP (%s)", 
                country.name, country.population / 1000000.0, country.gdp / 1000.0, country.region));
        }
        
        System.out.println("\n🔬 TOP RESEARCH INSTITUTIONS:");
        for (RealWorldData.ResearchData research : RealWorldData.TOP_RESEARCH_CENTERS) {
            System.out.println(String.format("  • %s - %s (%s, %s)", 
                research.name, research.fields, research.location, research.country));
        }
        
        System.out.println("\n🤝 COUNTRY UNIONS & ALLIANCES:");
        for (RealWorldData.CountryUnion union : RealWorldData.COUNTRY_UNIONS) {
            System.out.println(String.format("  • %s - %s (Founded: %d, HQ: %s)", 
                union.name, union.type, union.foundedYear, union.headquarters));
        }
        
        System.out.println();
    }
    
    /**
     * Run alternative futures analysis
     */
    private static void runAlternativeFuturesAnalysis() {
        System.out.println("\n=== ALTERNATIVE FUTURES ANALYSIS ===");
        
        System.out.println("🔮 FUTURE SCENARIOS BEING TRACKED:");
        for (RealWorldData.FutureScenario scenario : RealWorldData.ALTERNATIVE_FUTURES) {
            System.out.println(String.format("  • %s (%.0f%% base probability)", 
                scenario.name, scenario.probability * 100));
            System.out.println(String.format("    %s", scenario.description));
            System.out.println(String.format("    Impact: %s", scenario.implications));
            System.out.println();
        }
        
        System.out.println("📈 These scenarios are dynamically updated based on:");
        System.out.println("  • Real-world agent decisions and actions");
        System.out.println("  • Global PESTEL factor changes");
        System.out.println("  • Inter-agent collaboration patterns");
        System.out.println("  • Country union strategic initiatives");
        System.out.println();
    }
    
    /**
     * Display comprehensive simulation results
     */
    private static void displayComprehensiveResults(RealWorldPESTELEngine engine) {
        System.out.println("\n=== COMPREHENSIVE SIMULATION RESULTS ===");
        
        // Alternative futures outcome
        System.out.println("\n🔮 ALTERNATIVE FUTURES OUTCOME:");
        RealWorldPESTELEngine.AlternativeFutureManager futureManager = engine.getFutureManager();
        List<RealWorldPESTELEngine.FutureScenario> scenarios = futureManager.getAllScenarios();
        
        scenarios.sort((s1, s2) -> Double.compare(s2.getProbability(), s1.getProbability()));
        
        for (int i = 0; i < scenarios.size(); i++) {
            RealWorldPESTELEngine.FutureScenario scenario = scenarios.get(i);
            String indicator = i == 0 ? "🥇" : i == 1 ? "🥈" : i == 2 ? "🥉" : "📊";
            System.out.println(String.format("  %s %s: %.1f%% probability", 
                indicator, scenario.getName(), scenario.getProbability() * 100));
        }
        
        RealWorldPESTELEngine.FutureScenario dominant = futureManager.getCurrentDominantScenario();
        System.out.println(String.format("\n🎯 DOMINANT FUTURE: %s", dominant.getName()));
        System.out.println(String.format("   Probability: %.1f%%", dominant.getProbability() * 100));
        System.out.println(String.format("   Description: %s", dominant.getDescription()));
        
        // Entity performance analysis
        System.out.println("\n📊 ENTITY PERFORMANCE ANALYSIS:");
        
        // Most active companies
        System.out.println("\n🏢 MOST ACTIVE COMPANIES:");
        engine.getCompanies().stream()
            .sorted((c1, c2) -> Integer.compare(c2.getDecisionCount(), c1.getDecisionCount()))
            .limit(5)
            .forEach(company -> System.out.println(String.format("  • %s: %d strategic decisions", 
                company.getCompanyData().name, company.getDecisionCount())));
        
        // Most influential countries
        System.out.println("\n🏛️ MOST INFLUENTIAL COUNTRIES:");
        engine.getCountries().stream()
            .sorted((c1, c2) -> Double.compare(c2.getGeopoliticalInfluence(), c1.getGeopoliticalInfluence()))
            .limit(5)
            .forEach(country -> System.out.println(String.format("  • %s: Influence %.2f, %d policy decisions", 
                country.getCountryData().name, country.getGeopoliticalInfluence(), country.getDecisionCount())));
        
        // Most productive research institutions
        System.out.println("\n🔬 MOST PRODUCTIVE RESEARCH INSTITUTIONS:");
        engine.getResearchers().stream()
            .sorted((r1, r2) -> Integer.compare(r2.getDecisionCount(), r1.getDecisionCount()))
            .limit(5)
            .forEach(researcher -> System.out.println(String.format("  • %s: %d research initiatives, Impact %.2f", 
                researcher.getResearchData().name, researcher.getDecisionCount(), researcher.getResearchImpact())));
        
        // Country union effectiveness
        System.out.println("\n🤝 COUNTRY UNION EFFECTIVENESS:");
        for (RealWorldPESTELEngine.CountryUnion union : engine.getCountryUnions()) {
            System.out.println(String.format("  • %s: %d members, Collective Influence: %.2f", 
                union.getName(), union.getMemberCountries().size(), union.getCollectiveInfluence()));
        }
        
        // PESTEL change analysis
        System.out.println("\n📈 PESTEL CHANGES ANALYSIS:");
        Map<String, Integer> categoryChanges = new HashMap<>();
        for (PESTELChange change : engine.getRecentChanges()) {
            categoryChanges.merge(change.getCategory().toUpperCase(), 1, Integer::sum);
        }
        
        categoryChanges.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .forEach(entry -> System.out.println(String.format("  • %s: %d changes", 
                entry.getKey(), entry.getValue())));
    }
    
    /**
     * Display system capabilities
     */
    static {
        System.out.println("\n=== SYSTEM CAPABILITIES OVERVIEW ===");
        System.out.println("✅ Real-world entity modeling with actual data");
        System.out.println("✅ AI-driven decision making using Qwen3-Next model");
        System.out.println("✅ Comprehensive PESTEL analysis (6 categories)");
        System.out.println("✅ Dynamic alternative future scenarios");
        System.out.println("✅ Object-oriented country unions and alliances");
        System.out.println("✅ Strategic recommendations for university planning");
        System.out.println("✅ Fallback operation when AI unavailable");
        System.out.println();
    }
}
