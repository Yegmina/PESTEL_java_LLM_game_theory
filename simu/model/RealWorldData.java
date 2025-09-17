package simu.model;

import java.util.*;

/**
 * Real-world data for companies, countries, and research institutions
 * Based on 2025 global rankings and influence
 */
public class RealWorldData {
    
    /**
     * Top 10 Global Companies by Revenue (2025)
     */
    public static final CompanyData[] TOP_COMPANIES = {
        new CompanyData("Walmart", 681000, "Retail", "USA", "Arkansas", "Consumer goods distribution and retail services"),
        new CompanyData("Amazon", 638000, "E-commerce, Cloud Computing", "USA", "Washington", "E-commerce platform and cloud infrastructure"),
        new CompanyData("State Grid Corporation", 548000, "Utilities", "China", "Beijing", "Electric power transmission and distribution"),
        new CompanyData("Saudi Aramco", 480000, "Energy", "Saudi Arabia", "Dhahran", "Oil and gas exploration and production"),
        new CompanyData("China National Petroleum", 413000, "Energy", "China", "Beijing", "Integrated oil and gas operations"),
        new CompanyData("Sinopec Group", 407000, "Energy, Chemicals", "China", "Beijing", "Petroleum refining and petrochemicals"),
        new CompanyData("UnitedHealth Group", 400000, "Healthcare", "USA", "Minnesota", "Health insurance and healthcare services"),
        new CompanyData("Apple", 391000, "Technology", "USA", "California", "Consumer electronics and software"),
        new CompanyData("CVS Health", 373000, "Healthcare", "USA", "Rhode Island", "Pharmacy and healthcare services"),
        new CompanyData("Berkshire Hathaway", 371000, "Conglomerate", "USA", "Nebraska", "Diversified investment holding company")
    };
    
    /**
     * Top Influential Countries by Global Impact
     */
    public static final CountryData[] TOP_COUNTRIES = {
        new CountryData("United States", "North America", "Developed", "Federal Republic", 333000000, 26854599, "USD", "English"),
        new CountryData("China", "Asia", "Developed", "Socialist Republic", 1412000000, 17734063, "CNY", "Mandarin"),
        new CountryData("Germany", "Europe", "Developed", "Federal Republic", 83200000, 4259935, "EUR", "German"),
        new CountryData("Japan", "Asia", "Developed", "Constitutional Monarchy", 125800000, 4940878, "JPY", "Japanese"),
        new CountryData("United Kingdom", "Europe", "Developed", "Constitutional Monarchy", 67500000, 3131378, "GBP", "English"),
        new CountryData("India", "Asia", "Emerging", "Federal Republic", 1380000000, 3385090, "INR", "Hindi, English"),
        new CountryData("France", "Europe", "Developed", "Republic", 67750000, 2937473, "EUR", "French"),
        new CountryData("Canada", "North America", "Developed", "Federal Parliamentary", 38000000, 1988336, "CAD", "English, French"),
        new CountryData("South Korea", "Asia", "Developed", "Republic", 51780000, 1810972, "KRW", "Korean"),
        new CountryData("Brazil", "South America", "Emerging", "Federal Republic", 215000000, 1608981, "BRL", "Portuguese")
    };
    
    /**
     * Top Research Institutions and Centers
     */
    public static final ResearchData[] TOP_RESEARCH_CENTERS = {
        new ResearchData("MIT", "USA", "Technology, Engineering, AI", "University", "Cambridge, MA", "Leading technological research and innovation"),
        new ResearchData("Stanford University", "USA", "AI, Computer Science, Medicine", "University", "Stanford, CA", "Silicon Valley innovation hub"),
        new ResearchData("Chinese Academy of Sciences", "China", "Multidisciplinary Science", "Government", "Beijing", "China's premier research institution"),
        new ResearchData("Max Planck Society", "Germany", "Basic Science, Physics", "Independent", "Munich", "Fundamental research across sciences"),
        new ResearchData("Harvard University", "USA", "Medicine, Life Sciences, Policy", "University", "Cambridge, MA", "Medical and policy research leadership"),
        new ResearchData("CERN", "Switzerland", "Particle Physics", "International", "Geneva", "European particle physics research"),
        new ResearchData("Oxford University", "UK", "Medicine, AI, Climate", "University", "Oxford", "Centuries of research excellence"),
        new ResearchData("RIKEN", "Japan", "Physical Sciences, Biology", "Government", "Saitama", "Japan's largest research institute"),
        new ResearchData("CNRS", "France", "Multidisciplinary Research", "Government", "Paris", "France's national research organization"),
        new ResearchData("ETH Zurich", "Switzerland", "Technology, Engineering", "University", "Zurich", "European tech research leader")
    };
    
    /**
     * Country Unions and Alliances
     */
    public static final CountryUnion[] COUNTRY_UNIONS = {
        new CountryUnion("European Union", "Economic and Political Union", 
            Arrays.asList("Germany", "France", "United Kingdom"), "Brussels", 1957),
        new CountryUnion("USMCA", "Trade Agreement", 
            Arrays.asList("United States", "Canada"), "Washington DC", 2020),
        new CountryUnion("ASEAN", "Economic Association", 
            Arrays.asList("Japan", "South Korea"), "Jakarta", 1967),
        new CountryUnion("BRICS", "Economic Cooperation", 
            Arrays.asList("China", "India", "Brazil"), "Shanghai", 2009)
    };
    
    /**
     * Alternative Future Scenarios
     */
    public static final FutureScenario[] ALTERNATIVE_FUTURES = {
        new FutureScenario("AI Dominance", "Technology companies lead global transformation", 0.25,
            "Rapid AI advancement reshapes all industries and governance"),
        new FutureScenario("Green Transition", "Environmental priorities drive policy and economy", 0.30,
            "Climate action becomes the primary driver of global decisions"),
        new FutureScenario("Geopolitical Fragmentation", "Regional blocs compete for influence", 0.20,
            "World divides into competing technological and economic spheres"),
        new FutureScenario("Resource Scarcity", "Competition for limited resources intensifies", 0.15,
            "Water, energy, and rare earth materials become primary conflict drivers"),
        new FutureScenario("Digital Society", "Virtual interactions become primary social mode", 0.10,
            "Physical and digital worlds merge into new social paradigms")
    };
    
    // Data classes
    public static class CompanyData {
        public final String name;
        public final double revenue; // in millions USD
        public final String industry;
        public final String country;
        public final String headquarters;
        public final String description;
        
        public CompanyData(String name, double revenue, String industry, String country, 
                          String headquarters, String description) {
            this.name = name;
            this.revenue = revenue;
            this.industry = industry;
            this.country = country;
            this.headquarters = headquarters;
            this.description = description;
        }
    }
    
    public static class CountryData {
        public final String name;
        public final String region;
        public final String developmentLevel;
        public final String governmentType;
        public final long population;
        public final long gdp; // in millions USD
        public final String currency;
        public final String language;
        
        public CountryData(String name, String region, String developmentLevel, String governmentType,
                          long population, long gdp, String currency, String language) {
            this.name = name;
            this.region = region;
            this.developmentLevel = developmentLevel;
            this.governmentType = governmentType;
            this.population = population;
            this.gdp = gdp;
            this.currency = currency;
            this.language = language;
        }
    }
    
    public static class ResearchData {
        public final String name;
        public final String country;
        public final String fields;
        public final String type;
        public final String location;
        public final String description;
        
        public ResearchData(String name, String country, String researchFields, String type,
                           String location, String description) {
            this.name = name;
            this.country = country;
            this.fields = researchFields;
            this.type = type;
            this.location = location;
            this.description = description;
        }
    }
    
    public static class CountryUnion {
        public final String name;
        public final String type;
        public final List<String> memberCountries;
        public final String headquarters;
        public final int foundedYear;
        
        public CountryUnion(String name, String type, List<String> memberCountries, 
                           String headquarters, int foundedYear) {
            this.name = name;
            this.type = type;
            this.memberCountries = new ArrayList<>(memberCountries);
            this.headquarters = headquarters;
            this.foundedYear = foundedYear;
        }
    }
    
    public static class FutureScenario {
        public final String name;
        public final String description;
        public final double probability;
        public final String implications;
        
        public FutureScenario(String name, String description, double probability, String implications) {
            this.name = name;
            this.description = description;
            this.probability = probability;
            this.implications = implications;
        }
    }
}
