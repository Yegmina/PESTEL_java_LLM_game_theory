package simu.model;

import java.util.*;

/**
 * Comprehensive real-world data including top 50 companies, 30 countries, and 25 research institutions
 * Updated for 2025 with complete global coverage for realistic PESTEL simulation
 */
public class ComprehensiveRealWorldData {
    
    /**
     * Top 50 Global Companies by Revenue and Influence (2025)
     */
    public static final CompanyData[] TOP_COMPANIES = {
        // Top 10 by Revenue
        new CompanyData("Walmart", 681000, "Retail", "USA", "Arkansas", "Global retail and supply chain leadership"),
        new CompanyData("Amazon", 638000, "E-commerce, Cloud Computing", "USA", "Washington", "E-commerce and cloud infrastructure dominance"),
        new CompanyData("State Grid Corporation", 548000, "Utilities", "China", "Beijing", "World's largest electric utility company"),
        new CompanyData("Saudi Aramco", 480000, "Energy", "Saudi Arabia", "Dhahran", "Global oil and gas production leader"),
        new CompanyData("China National Petroleum", 413000, "Energy", "China", "Beijing", "Integrated oil and gas operations"),
        new CompanyData("Sinopec Group", 407000, "Energy, Chemicals", "China", "Beijing", "Petroleum refining and petrochemicals"),
        new CompanyData("UnitedHealth Group", 400000, "Healthcare", "USA", "Minnesota", "Health insurance and healthcare services"),
        new CompanyData("Apple", 391000, "Technology", "USA", "California", "Consumer electronics and software innovation"),
        new CompanyData("CVS Health", 373000, "Healthcare", "USA", "Rhode Island", "Pharmacy and healthcare services"),
        new CompanyData("Berkshire Hathaway", 371000, "Conglomerate", "USA", "Nebraska", "Diversified investment holding company"),
        
        // Technology Giants
        new CompanyData("Microsoft", 365000, "Technology", "USA", "Washington", "Cloud computing and software services"),
        new CompanyData("Alphabet", 350000, "Technology", "USA", "California", "Search, advertising, and AI technologies"),
        new CompanyData("Meta", 134000, "Technology", "USA", "California", "Social media and virtual reality platforms"),
        new CompanyData("Tesla", 127000, "Automotive, Energy", "USA", "Texas", "Electric vehicles and sustainable energy"),
        new CompanyData("NVIDIA", 118000, "Technology", "USA", "California", "AI chips and computing platforms"),
        new CompanyData("Samsung Electronics", 279000, "Technology", "South Korea", "Seoul", "Semiconductors and consumer electronics"),
        new CompanyData("TSMC", 89000, "Technology", "Taiwan", "Hsinchu", "Advanced semiconductor manufacturing"),
        new CompanyData("Sony", 98000, "Technology, Entertainment", "Japan", "Tokyo", "Electronics and entertainment content"),
        new CompanyData("Intel", 87000, "Technology", "USA", "California", "Semiconductor design and manufacturing"),
        new CompanyData("IBM", 72000, "Technology", "USA", "New York", "Enterprise technology and AI services"),
        
        // Financial Services
        new CompanyData("JPMorgan Chase", 158000, "Financial Services", "USA", "New York", "Global investment banking and financial services"),
        new CompanyData("Bank of America", 119000, "Financial Services", "USA", "North Carolina", "Consumer and commercial banking"),
        new CompanyData("Wells Fargo", 87000, "Financial Services", "USA", "California", "Diversified financial services"),
        new CompanyData("Goldman Sachs", 59000, "Financial Services", "USA", "New York", "Investment banking and asset management"),
        new CompanyData("Morgan Stanley", 64000, "Financial Services", "USA", "New York", "Investment banking and wealth management"),
        
        // Global Corporations
        new CompanyData("Volkswagen Group", 295000, "Automotive", "Germany", "Wolfsburg", "Global automotive manufacturing"),
        new CompanyData("Toyota Motor", 274000, "Automotive", "Japan", "Toyota City", "Automotive innovation and manufacturing"),
        new CompanyData("Mercedes-Benz Group", 186000, "Automotive", "Germany", "Stuttgart", "Luxury automotive and mobility"),
        new CompanyData("BMW Group", 142000, "Automotive", "Germany", "Munich", "Premium automotive and mobility services"),
        new CompanyData("General Motors", 138000, "Automotive", "USA", "Michigan", "Automotive design and manufacturing"),
        
        // Energy and Resources
        new CompanyData("ExxonMobil", 413000, "Energy", "USA", "Texas", "Oil and gas exploration and production"),
        new CompanyData("Chevron", 235000, "Energy", "USA", "California", "Integrated energy company"),
        new CompanyData("Shell", 381000, "Energy", "Netherlands", "The Hague", "Global energy and petrochemicals"),
        new CompanyData("BP", 164000, "Energy", "United Kingdom", "London", "Oil, gas, and renewable energy"),
        new CompanyData("TotalEnergies", 263000, "Energy", "France", "Paris", "Multi-energy company"),
        
        // Retail and Consumer
        new CompanyData("Costco", 249000, "Retail", "USA", "Washington", "Membership-based warehouse club"),
        new CompanyData("Home Depot", 157000, "Retail", "USA", "Georgia", "Home improvement retail"),
        new CompanyData("Procter & Gamble", 84000, "Consumer Goods", "USA", "Ohio", "Consumer products and brands"),
        new CompanyData("Coca-Cola", 46000, "Beverages", "USA", "Georgia", "Global beverage company"),
        new CompanyData("PepsiCo", 91000, "Food & Beverages", "USA", "New York", "Food, snack, and beverage company"),
        
        // Healthcare and Pharmaceuticals
        new CompanyData("Johnson & Johnson", 100000, "Healthcare", "USA", "New Jersey", "Pharmaceutical and medical devices"),
        new CompanyData("Pfizer", 58000, "Pharmaceuticals", "USA", "New York", "Biopharmaceutical research and development"),
        new CompanyData("Roche", 71000, "Pharmaceuticals", "Switzerland", "Basel", "Pharmaceuticals and diagnostics"),
        new CompanyData("Novartis", 51000, "Pharmaceuticals", "Switzerland", "Basel", "Innovative medicines and generics"),
        new CompanyData("Merck", 62000, "Pharmaceuticals", "USA", "New Jersey", "Healthcare innovation and life sciences"),
        
        // Telecommunications
        new CompanyData("Verizon", 134000, "Telecommunications", "USA", "New York", "Wireless and broadband communications"),
        new CompanyData("AT&T", 120000, "Telecommunications", "USA", "Texas", "Communications and media services"),
        new CompanyData("China Mobile", 118000, "Telecommunications", "China", "Beijing", "Mobile telecommunications services"),
        new CompanyData("Vodafone", 52000, "Telecommunications", "United Kingdom", "London", "Global mobile and fixed communications"),
        
        // Aerospace and Defense
        new CompanyData("Boeing", 66000, "Aerospace", "USA", "Illinois", "Aerospace and defense systems"),
        new CompanyData("Airbus", 67000, "Aerospace", "France", "Toulouse", "Commercial aircraft and space systems"),
        new CompanyData("Lockheed Martin", 67000, "Defense", "USA", "Maryland", "Aerospace, defense, and technology"),
        new CompanyData("Raytheon Technologies", 64000, "Defense", "USA", "Massachusetts", "Aerospace and defense technologies")
    };
    
    /**
     * Top 30 Most Influential Countries by Global Impact (2025)
     */
    public static final CountryData[] TOP_COUNTRIES = {
        // Global Superpowers
        new CountryData("United States", "North America", "Developed", "Federal Republic", 333000000, 26854599, "USD", "English"),
        new CountryData("China", "Asia", "Developed", "Socialist Republic", 1412000000, 17734063, "CNY", "Mandarin"),
        new CountryData("Japan", "Asia", "Developed", "Constitutional Monarchy", 125800000, 4940878, "JPY", "Japanese"),
        new CountryData("Germany", "Europe", "Developed", "Federal Republic", 83200000, 4259935, "EUR", "German"),
        new CountryData("India", "Asia", "Emerging", "Federal Republic", 1380000000, 3385090, "INR", "Hindi, English"),
        
        // Major European Powers
        new CountryData("United Kingdom", "Europe", "Developed", "Constitutional Monarchy", 67500000, 3131378, "GBP", "English"),
        new CountryData("France", "Europe", "Developed", "Republic", 67750000, 2937473, "EUR", "French"),
        new CountryData("Italy", "Europe", "Developed", "Republic", 59500000, 2107703, "EUR", "Italian"),
        new CountryData("Spain", "Europe", "Developed", "Constitutional Monarchy", 47400000, 1397870, "EUR", "Spanish"),
        new CountryData("Netherlands", "Europe", "Developed", "Constitutional Monarchy", 17400000, 909070, "EUR", "Dutch"),
        
        // Asia-Pacific Powers
        new CountryData("South Korea", "Asia", "Developed", "Republic", 51780000, 1810972, "KRW", "Korean"),
        new CountryData("Australia", "Oceania", "Developed", "Federal Parliamentary", 25700000, 1552667, "AUD", "English"),
        new CountryData("Taiwan", "Asia", "Developed", "Republic", 23400000, 790728, "TWD", "Mandarin"),
        new CountryData("Singapore", "Asia", "Developed", "Parliamentary Republic", 5900000, 397000, "SGD", "English, Mandarin"),
        new CountryData("Thailand", "Asia", "Emerging", "Constitutional Monarchy", 69800000, 543500, "THB", "Thai"),
        
        // Americas
        new CountryData("Canada", "North America", "Developed", "Federal Parliamentary", 38000000, 1988336, "CAD", "English, French"),
        new CountryData("Brazil", "South America", "Emerging", "Federal Republic", 215000000, 1608981, "BRL", "Portuguese"),
        new CountryData("Mexico", "North America", "Emerging", "Federal Republic", 128900000, 1293000, "MXN", "Spanish"),
        new CountryData("Argentina", "South America", "Emerging", "Federal Republic", 45400000, 487200, "ARS", "Spanish"),
        new CountryData("Chile", "South America", "Developed", "Republic", 19100000, 317000, "CLP", "Spanish"),
        
        // Middle East & Africa
        new CountryData("Saudi Arabia", "Middle East", "Developed", "Absolute Monarchy", 34800000, 833540, "SAR", "Arabic"),
        new CountryData("United Arab Emirates", "Middle East", "Developed", "Federal Monarchy", 9900000, 507540, "AED", "Arabic"),
        new CountryData("Israel", "Middle East", "Developed", "Parliamentary Republic", 9400000, 481600, "ILS", "Hebrew"),
        new CountryData("South Africa", "Africa", "Emerging", "Republic", 59300000, 419000, "ZAR", "Multiple"),
        new CountryData("Nigeria", "Africa", "Emerging", "Federal Republic", 218500000, 440800, "NGN", "English"),
        
        // Eastern Europe & Russia
        new CountryData("Russia", "Europe/Asia", "Emerging", "Federal Republic", 146000000, 2240000, "RUB", "Russian"),
        new CountryData("Poland", "Europe", "Developed", "Republic", 38000000, 679000, "PLN", "Polish"),
        new CountryData("Czech Republic", "Europe", "Developed", "Parliamentary Republic", 10700000, 290000, "CZK", "Czech"),
        
        // Nordic Countries
        new CountryData("Sweden", "Europe", "Developed", "Constitutional Monarchy", 10400000, 635000, "SEK", "Swedish"),
        new CountryData("Norway", "Europe", "Developed", "Constitutional Monarchy", 5400000, 482000, "NOK", "Norwegian"),
        new CountryData("Finland", "Europe", "Developed", "Republic", 5500000, 297000, "EUR", "Finnish")
    };
    
    /**
     * Top 25 Global Research Institutions and Centers (2025)
     */
    public static final ResearchData[] TOP_RESEARCH_CENTERS = {
        // US Elite Universities
        new ResearchData("MIT", "USA", "Technology, Engineering, AI, Quantum Computing", "University", "Cambridge, MA", "Leading technological research and innovation hub"),
        new ResearchData("Stanford University", "USA", "AI, Computer Science, Medicine, Entrepreneurship", "University", "Stanford, CA", "Silicon Valley innovation epicenter"),
        new ResearchData("Harvard University", "USA", "Medicine, Life Sciences, Policy, Business", "University", "Cambridge, MA", "Medical and policy research leadership"),
        new ResearchData("Caltech", "USA", "Physics, Engineering, Space Science", "University", "Pasadena, CA", "Small but elite science and engineering research"),
        new ResearchData("Princeton University", "USA", "Physics, Mathematics, Economics, AI", "University", "Princeton, NJ", "Theoretical research excellence"),
        new ResearchData("Yale University", "USA", "Medicine, Law, Social Sciences", "University", "New Haven, CT", "Medical and social science research"),
        new ResearchData("University of Chicago", "USA", "Economics, Physics, Medicine", "University", "Chicago, IL", "Nobel Prize winning research institution"),
        new ResearchData("Columbia University", "USA", "Medicine, Journalism, International Affairs", "University", "New York, NY", "Urban research and global studies"),
        
        // International Elite Universities
        new ResearchData("University of Oxford", "UK", "Medicine, AI, Climate Science, History", "University", "Oxford", "Centuries of research excellence and tradition"),
        new ResearchData("University of Cambridge", "UK", "Physics, Engineering, Medicine, AI", "University", "Cambridge", "Scientific research and innovation leadership"),
        new ResearchData("Imperial College London", "UK", "Engineering, Medicine, Natural Sciences", "University", "London", "Science, technology, and medicine focus"),
        new ResearchData("ETH Zurich", "Switzerland", "Technology, Engineering, Natural Sciences", "University", "Zurich", "European technology research leader"),
        new ResearchData("University of Toronto", "Canada", "AI, Medicine, Engineering", "University", "Toronto", "Leading AI research and innovation"),
        
        // Asian Research Powerhouses
        new ResearchData("Chinese Academy of Sciences", "China", "Multidisciplinary Science, Space, AI", "Government", "Beijing", "China's premier research institution"),
        new ResearchData("Tsinghua University", "China", "Engineering, Technology, AI, Policy", "University", "Beijing", "China's top technical university"),
        new ResearchData("Peking University", "China", "Natural Sciences, Medicine, Social Sciences", "University", "Beijing", "Leading comprehensive research university"),
        new ResearchData("University of Tokyo", "Japan", "Natural Sciences, Engineering, Medicine", "University", "Tokyo", "Japan's premier research university"),
        new ResearchData("RIKEN", "Japan", "Physical Sciences, Biology, AI", "Government", "Saitama", "Japan's largest research institute"),
        new ResearchData("Seoul National University", "South Korea", "Engineering, Natural Sciences, Medicine", "University", "Seoul", "South Korea's top research university"),
        new ResearchData("National University of Singapore", "Singapore", "Engineering, Medicine, AI", "University", "Singapore", "Leading Asian research university"),
        
        // European Research Excellence
        new ResearchData("Max Planck Society", "Germany", "Basic Science, Physics, Chemistry", "Independent", "Munich", "Fundamental research across sciences"),
        new ResearchData("CERN", "Switzerland", "Particle Physics, Computing", "International", "Geneva", "European particle physics research center"),
        new ResearchData("CNRS", "France", "Multidisciplinary Research, Physics", "Government", "Paris", "France's national research organization"),
        new ResearchData("Sorbonne University", "France", "Medicine, Sciences, Humanities", "University", "Paris", "Leading French research university"),
        new ResearchData("Technical University of Munich", "Germany", "Engineering, Technology, Natural Sciences", "University", "Munich", "German technical research excellence"),
        new ResearchData("Karolinska Institute", "Sweden", "Medicine, Life Sciences", "University", "Stockholm", "Medical research and Nobel Prize awards")
    };
    
    /**
     * Enhanced Country Unions and International Organizations
     */
    public static final CountryUnion[] COUNTRY_UNIONS = {
        new CountryUnion("European Union", "Economic and Political Union", 
            Arrays.asList("Germany", "France", "Italy", "Spain", "Netherlands", "Poland"), "Brussels", 1957),
        new CountryUnion("USMCA", "Trade Agreement", 
            Arrays.asList("United States", "Canada", "Mexico"), "Washington DC", 2020),
        new CountryUnion("ASEAN", "Economic Association", 
            Arrays.asList("Singapore", "Thailand"), "Jakarta", 1967),
        new CountryUnion("BRICS", "Economic Cooperation", 
            Arrays.asList("China", "India", "Brazil", "Russia", "South Africa"), "Shanghai", 2009),
        new CountryUnion("G7", "Economic Forum", 
            Arrays.asList("United States", "Japan", "Germany", "United Kingdom", "France", "Italy", "Canada"), "Various", 1975),
        new CountryUnion("G20", "Economic Cooperation", 
            Arrays.asList("United States", "China", "Japan", "Germany", "India", "United Kingdom", "France", "Brazil", "South Korea", "Australia"), "Various", 1999),
        new CountryUnion("NATO", "Security Alliance", 
            Arrays.asList("United States", "United Kingdom", "Germany", "France", "Canada", "Poland"), "Brussels", 1949),
        new CountryUnion("Nordic Council", "Regional Cooperation", 
            Arrays.asList("Sweden", "Norway", "Finland"), "Copenhagen", 1952)
    };
    
    /**
     * Comprehensive Alternative Future Scenarios (2025-2035)
     */
    public static final FutureScenario[] ALTERNATIVE_FUTURES = {
        // Technology-Driven Futures
        new FutureScenario("AI Supremacy", "Artificial Intelligence becomes the dominant force reshaping all sectors", 0.22,
            "AI companies like NVIDIA, Google, and OpenAI control global decision-making, education becomes AI-centric"),
        new FutureScenario("Quantum Revolution", "Quantum computing breakthrough transforms computing and cryptography", 0.15,
            "IBM, Google, and research institutions lead quantum advantage, cybersecurity revolutionized"),
        new FutureScenario("Biotech Renaissance", "Biotechnology advances cure aging and enhance human capabilities", 0.12,
            "Pharmaceutical companies and medical research institutions drive human enhancement"),
        
        // Environmental Futures
        new FutureScenario("Green Transition Triumph", "Renewable energy and sustainability become economic drivers", 0.25,
            "Tesla, renewable energy companies, and Nordic countries lead carbon-neutral transformation"),
        new FutureScenario("Climate Crisis Response", "Urgent climate action reshapes global priorities and governance", 0.18,
            "Emergency climate measures dominate policy, massive green infrastructure investments"),
        
        // Geopolitical Futures
        new FutureScenario("Multipolar World Order", "Multiple power centers compete for global influence", 0.20,
            "US-China rivalry, EU autonomy, emerging middle powers reshape international relations"),
        new FutureScenario("Regional Bloc Dominance", "World divides into competing regional economic and political blocs", 0.16,
            "EU, ASEAN, USMCA become primary governance units, reduced global cooperation"),
        new FutureScenario("Democratic Renaissance", "Renewed focus on democratic values and institutions globally", 0.14,
            "Democratic countries strengthen alliances, authoritarian systems face pressure"),
        
        // Economic Futures
        new FutureScenario("Digital Economy Supremacy", "Virtual and digital interactions dominate economic activity", 0.19,
            "Meta, Microsoft, gaming companies lead virtual economy transformation"),
        new FutureScenario("Resource Scarcity Wars", "Competition for rare earth metals and water intensifies conflicts", 0.13,
            "Mining companies, water tech firms, and resource-rich countries gain power"),
        
        // Social Futures
        new FutureScenario("Aging Society Adaptation", "Demographic transition drives innovation in eldercare and automation", 0.17,
            "Japan, Germany lead aging solutions, robotics and healthcare AI advance rapidly"),
        new FutureScenario("Global Education Revolution", "Personalized AI education transforms human development", 0.21,
            "Universities partner with AI companies, traditional education models disrupted")
    };
    
    /**
     * Industry Sectors for Strategic Analysis
     */
    public static final String[] INDUSTRY_SECTORS = {
        "Technology", "Healthcare", "Energy", "Financial Services", "Automotive", 
        "Retail", "Telecommunications", "Aerospace", "Pharmaceuticals", "Entertainment",
        "Agriculture", "Mining", "Construction", "Transportation", "Education"
    };
    
    /**
     * Research Fields for Academic Analysis
     */
    public static final String[] RESEARCH_FIELDS = {
        "Artificial Intelligence", "Quantum Computing", "Biotechnology", "Climate Science",
        "Renewable Energy", "Materials Science", "Neuroscience", "Space Science",
        "Medicine", "Physics", "Chemistry", "Economics", "Political Science",
        "Environmental Science", "Data Science", "Robotics", "Nanotechnology"
    };
    
    // Data classes remain the same as RealWorldData
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
