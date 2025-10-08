package simu.model;

import java.util.HashMap;
import java.util.Map;

/**
 * PESTEL State represents the six key factors that influence strategic planning:
 * Political, Economic, Social, Technological, Environmental, and Legal
 */
public class PESTELState {
    private Map<String, String> political;
    private Map<String, String> economic;
    private Map<String, String> social;
    private Map<String, String> technological;
    private Map<String, String> environmental;
    private Map<String, String> legal;
    private double lastUpdateTime;
    
    public PESTELState() {
        this.political = new HashMap<>();
        this.economic = new HashMap<>();
        this.social = new HashMap<>();
        this.technological = new HashMap<>();
        this.environmental = new HashMap<>();
        this.legal = new HashMap<>();
        this.lastUpdateTime = 0.0;
        
        initializeDefaults();
    }
    
    /**
     * Initialize with default PESTEL values
     */
    private void initializeDefaults() {
        // Political factors
        political.put("policies_laws", "Moderate regulatory environment with standard business policies");
        political.put("tax_labour", "Corporate tax rate 25%, flexible labor laws");
        political.put("trade", "Open trade policies with EU partnerships");
        political.put("stability", "Stable democratic government");
        political.put("corruption", "Low corruption index, transparent institutions");
        
        // Economic factors
        economic.put("growth", "GDP growth 2.5% annually, steady economic expansion");
        economic.put("inflation", "Inflation rate 2.1%, within target range");
        economic.put("interest_rates", "Central bank rate 1.5%, accommodative monetary policy");
        economic.put("employment", "Unemployment 6.2%, recovering job market");
        economic.put("income", "Median household income €45,000, moderate purchasing power");
        
        // Social factors
        social.put("population", "Population 5.5M, aging demographics");
        social.put("ageing", "25% population over 65, increasing healthcare needs");
        social.put("career_views", "Strong emphasis on work-life balance, remote work acceptance");
        social.put("lifestyle", "Health-conscious, environmentally aware population");
        social.put("cultural_barriers", "Multicultural society, language diversity challenges");
        
        // Technological factors
        technological.put("technology_incentives", "Government R&D tax credits, innovation grants");
        technological.put("innovation", "High digitalization rate, strong tech sector");
        technological.put("automation", "Moderate automation adoption, focus on human-AI collaboration");
        technological.put("rd_activity", "3.2% GDP invested in R&D, strong university-industry partnerships");
        
        // Environmental factors
        environmental.put("climate_change", "Carbon neutral by 2035 goal, renewable energy transition");
        environmental.put("ethical", "Strong environmental regulations, circular economy initiatives");
        environmental.put("recycling_disposal", "85% recycling rate, advanced waste management");
        environmental.put("sustainability", "Sustainable development focus, green technology investments");
        
        // Legal factors
        legal.put("antitrust", "Strong competition laws, fair market practices");
        legal.put("labour", "Progressive labor laws, strong worker protections");
        legal.put("copyright", "Robust IP protection, digital rights enforcement");
        legal.put("data_protection", "GDPR compliance, strict privacy regulations");
        legal.put("health_safety", "Comprehensive health and safety standards");
    }
    
    // Political getters/setters
    public String getPolitical(String key) {
        return political.getOrDefault(key, "Not defined");
    }
    
    public void setPolitical(String key, String value) {
        political.put(key, value);
        updateTime();
    }
    
    public Map<String, String> getAllPolitical() {
        return new HashMap<>(political);
    }
    
    // Economic getters/setters
    public String getEconomic(String key) {
        return economic.getOrDefault(key, "Not defined");
    }
    
    public void setEconomic(String key, String value) {
        economic.put(key, value);
        updateTime();
    }
    
    public Map<String, String> getAllEconomic() {
        return new HashMap<>(economic);
    }
    
    // Social getters/setters
    public String getSocial(String key) {
        return social.getOrDefault(key, "Not defined");
    }
    
    public void setSocial(String key, String value) {
        social.put(key, value);
        updateTime();
    }
    
    public Map<String, String> getAllSocial() {
        return new HashMap<>(social);
    }
    
    // Technological getters/setters
    public String getTechnological(String key) {
        return technological.getOrDefault(key, "Not defined");
    }
    
    public void setTechnological(String key, String value) {
        technological.put(key, value);
        updateTime();
    }
    
    public Map<String, String> getAllTechnological() {
        return new HashMap<>(technological);
    }
    
    // Environmental getters/setters
    public String getEnvironmental(String key) {
        return environmental.getOrDefault(key, "Not defined");
    }
    
    public void setEnvironmental(String key, String value) {
        environmental.put(key, value);
        updateTime();
    }
    
    public Map<String, String> getAllEnvironmental() {
        return new HashMap<>(environmental);
    }
    
    // Legal getters/setters
    public String getLegal(String key) {
        return legal.getOrDefault(key, "Not defined");
    }
    
    public void setLegal(String key, String value) {
        legal.put(key, value);
        updateTime();
    }
    
    public Map<String, String> getAllLegal() {
        return new HashMap<>(legal);
    }
    
    /**
     * Update a PESTEL factor by category and key
     */
    public void updateFactor(String category, String key, String value) {
        switch (category.toLowerCase()) {
            case "political":
            case "p":
                setPolitical(key, value);
                break;
            case "economic":
            case "e":
                setEconomic(key, value);
                break;
            case "social":
            case "s":
                setSocial(key, value);
                break;
            case "technological":
            case "t":
                setTechnological(key, value);
                break;
            case "environmental":
            case "env":
                setEnvironmental(key, value);
                break;
            case "legal":
            case "l":
                setLegal(key, value);
                break;
        }
    }
    
    /**
     * Get a PESTEL factor by category and key
     */
    public String getFactor(String category, String key) {
        switch (category.toLowerCase()) {
            case "political":
            case "p":
                return getPolitical(key);
            case "economic":
            case "e":
                return getEconomic(key);
            case "social":
            case "s":
                return getSocial(key);
            case "technological":
            case "t":
                return getTechnological(key);
            case "environmental":
            case "env":
                return getEnvironmental(key);
            case "legal":
            case "l":
                return getLegal(key);
            default:
                return "Invalid category";
        }
    }
    
    private void updateTime() {
        lastUpdateTime = simu.framework.Clock.getInstance().getClock();
    }
    
    public double getLastUpdateTime() {
        return lastUpdateTime;
    }
    
    /**
     * Get a comprehensive summary of all PESTEL factors
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== PESTEL STATE SUMMARY ===\n");
        
        sb.append("\n[POLITICAL]\n");
        political.forEach((k, v) -> sb.append("  ").append(k).append(": ").append(v).append("\n"));
        
        sb.append("\n[ECONOMIC]\n");
        economic.forEach((k, v) -> sb.append("  ").append(k).append(": ").append(v).append("\n"));
        
        sb.append("\n[SOCIAL]\n");
        social.forEach((k, v) -> sb.append("  ").append(k).append(": ").append(v).append("\n"));
        
        sb.append("\n[TECHNOLOGICAL]\n");
        technological.forEach((k, v) -> sb.append("  ").append(k).append(": ").append(v).append("\n"));
        
        sb.append("\n[ENVIRONMENTAL]\n");
        environmental.forEach((k, v) -> sb.append("  ").append(k).append(": ").append(v).append("\n"));
        
        sb.append("\n[LEGAL]\n");
        legal.forEach((k, v) -> sb.append("  ").append(k).append(": ").append(v).append("\n"));
        
        sb.append("\nLast Update: ").append(String.format("%.2f", lastUpdateTime));
        
        return sb.toString();
    }
    
    public Map<String, Map<String, String>> getAllFactors() {
        Map<String, Map<String, String>> factors = new HashMap<>();
        factors.put("Political", political);
        factors.put("Economic", economic);
        factors.put("Social", social);
        factors.put("Technological", technological);
        factors.put("Environmental", environmental);
        factors.put("Legal", legal);
        return factors;
    }
}
