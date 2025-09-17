package simu.model;

import java.util.*;

/**
 * Ultra-comprehensive real-world data including Top 100 Companies, 50 Countries, 40 Research Centers
 * Enhanced for 2025 with complete global coverage for realistic PESTEL simulation
 */
public class UltraComprehensiveRealWorldData {
    
    /**
     * Top 100 Global Companies by Revenue and Strategic Importance (2025)
     */
    public static final CompanyData[] TOP_COMPANIES = {
        // Technology Giants (Top 20)
        new CompanyData("Apple", 391000, "Technology", "USA", "California", "Consumer electronics and software innovation leader"),
        new CompanyData("Microsoft", 365000, "Technology", "USA", "Washington", "Cloud computing and enterprise software dominance"),
        new CompanyData("Alphabet", 350000, "Technology", "USA", "California", "Search, advertising, and AI technology leadership"),
        new CompanyData("Amazon", 638000, "E-commerce, Cloud", "USA", "Washington", "E-commerce and cloud infrastructure global leader"),
        new CompanyData("Meta", 134000, "Technology", "USA", "California", "Social media and virtual reality platforms"),
        new CompanyData("Tesla", 127000, "Automotive, Energy", "USA", "Texas", "Electric vehicles and sustainable energy innovation"),
        new CompanyData("NVIDIA", 118000, "Technology", "USA", "California", "AI chips and computing platform leader"),
        new CompanyData("Samsung Electronics", 279000, "Technology", "South Korea", "Seoul", "Global semiconductor and electronics leader"),
        new CompanyData("TSMC", 89000, "Technology", "Taiwan", "Hsinchu", "World's most advanced semiconductor manufacturer"),
        new CompanyData("Sony", 98000, "Technology, Entertainment", "Japan", "Tokyo", "Electronics and entertainment content leader"),
        new CompanyData("Intel", 87000, "Technology", "USA", "California", "Semiconductor design and manufacturing leader"),
        new CompanyData("IBM", 72000, "Technology", "USA", "New York", "Enterprise technology and AI services"),
        new CompanyData("Oracle", 65000, "Technology", "USA", "Texas", "Enterprise database and cloud solutions"),
        new CompanyData("Salesforce", 45000, "Technology", "USA", "California", "Customer relationship management platform"),
        new CompanyData("Adobe", 35000, "Technology", "USA", "California", "Creative software and digital marketing solutions"),
        new CompanyData("Netflix", 32000, "Technology, Media", "USA", "California", "Global streaming and content platform"),
        new CompanyData("PayPal", 29000, "Technology, Financial", "USA", "California", "Digital payments and financial services"),
        new CompanyData("Uber", 28000, "Technology, Transportation", "USA", "California", "Ride-sharing and delivery platform"),
        new CompanyData("Spotify", 15000, "Technology, Media", "Sweden", "Stockholm", "Global music streaming platform"),
        new CompanyData("Zoom", 8000, "Technology", "USA", "California", "Video communications platform"),
        
        // Retail and E-commerce (Top 15)
        new CompanyData("Walmart", 681000, "Retail", "USA", "Arkansas", "World's largest retailer and supply chain leader"),
        new CompanyData("Costco", 249000, "Retail", "USA", "Washington", "Membership-based warehouse club leader"),
        new CompanyData("Home Depot", 157000, "Retail", "USA", "Georgia", "Home improvement retail leader"),
        new CompanyData("Target", 109000, "Retail", "USA", "Minnesota", "General merchandise retail chain"),
        new CompanyData("Lowe's", 97000, "Retail", "USA", "North Carolina", "Home improvement retail chain"),
        new CompanyData("Best Buy", 51000, "Retail", "USA", "Minnesota", "Consumer electronics retail leader"),
        new CompanyData("Alibaba", 145000, "E-commerce", "China", "Hangzhou", "China's largest e-commerce platform"),
        new CompanyData("JD.com", 149000, "E-commerce", "China", "Beijing", "China's second-largest e-commerce platform"),
        new CompanyData("Shopify", 8000, "E-commerce", "Canada", "Ottawa", "E-commerce platform for businesses"),
        new CompanyData("eBay", 10000, "E-commerce", "USA", "California", "Online marketplace platform"),
        new CompanyData("Rakuten", 18000, "E-commerce", "Japan", "Tokyo", "Japanese e-commerce and internet services"),
        new CompanyData("MercadoLibre", 12000, "E-commerce", "Argentina", "Buenos Aires", "Latin American e-commerce leader"),
        new CompanyData("Sea Limited", 16000, "E-commerce, Gaming", "Singapore", "Singapore", "Southeast Asian digital platform"),
        new CompanyData("Flipkart", 23000, "E-commerce", "India", "Bangalore", "Indian e-commerce platform"),
        new CompanyData("Pinduoduo", 21000, "E-commerce", "China", "Shanghai", "Chinese group buying platform"),
        
        // Energy and Utilities (Top 15)
        new CompanyData("Saudi Aramco", 480000, "Energy", "Saudi Arabia", "Dhahran", "World's largest oil company"),
        new CompanyData("China National Petroleum", 413000, "Energy", "China", "Beijing", "China's largest oil and gas company"),
        new CompanyData("Sinopec Group", 407000, "Energy, Chemicals", "China", "Beijing", "Integrated oil, gas, and chemicals"),
        new CompanyData("State Grid Corporation", 548000, "Utilities", "China", "Beijing", "World's largest electric utility"),
        new CompanyData("ExxonMobil", 413000, "Energy", "USA", "Texas", "Integrated oil and gas company"),
        new CompanyData("Shell", 381000, "Energy", "Netherlands", "The Hague", "Global energy and petrochemicals"),
        new CompanyData("Chevron", 235000, "Energy", "USA", "California", "Integrated energy company"),
        new CompanyData("TotalEnergies", 263000, "Energy", "France", "Paris", "Multi-energy company with renewables focus"),
        new CompanyData("BP", 164000, "Energy", "United Kingdom", "London", "Oil, gas, and renewable energy"),
        new CompanyData("ConocoPhillips", 75000, "Energy", "USA", "Texas", "Independent oil and gas company"),
        new CompanyData("Petrobras", 124000, "Energy", "Brazil", "Rio de Janeiro", "Brazilian oil and gas giant"),
        new CompanyData("Gazprom", 156000, "Energy", "Russia", "Moscow", "Russian natural gas giant"),
        new CompanyData("Rosneft", 181000, "Energy", "Russia", "Moscow", "Russian oil company"),
        new CompanyData("Eni", 93000, "Energy", "Italy", "Rome", "Italian multinational oil and gas"),
        new CompanyData("Equinor", 89000, "Energy", "Norway", "Stavanger", "Norwegian energy company"),
        
        // Healthcare and Pharmaceuticals (Top 15)
        new CompanyData("UnitedHealth Group", 400000, "Healthcare", "USA", "Minnesota", "Health insurance and healthcare services"),
        new CompanyData("CVS Health", 373000, "Healthcare", "USA", "Rhode Island", "Pharmacy and healthcare services"),
        new CompanyData("Johnson & Johnson", 100000, "Healthcare", "USA", "New Jersey", "Pharmaceutical and medical devices"),
        new CompanyData("Pfizer", 58000, "Pharmaceuticals", "USA", "New York", "Biopharmaceutical research and development"),
        new CompanyData("Roche", 71000, "Pharmaceuticals", "Switzerland", "Basel", "Pharmaceuticals and diagnostics"),
        new CompanyData("Novartis", 51000, "Pharmaceuticals", "Switzerland", "Basel", "Innovative medicines and generics"),
        new CompanyData("Merck", 62000, "Pharmaceuticals", "USA", "New Jersey", "Healthcare innovation and life sciences"),
        new CompanyData("AbbVie", 67000, "Pharmaceuticals", "USA", "Illinois", "Research-based biopharmaceutical company"),
        new CompanyData("Bristol Myers Squibb", 46000, "Pharmaceuticals", "USA", "New York", "Biopharmaceutical company"),
        new CompanyData("AstraZeneca", 44000, "Pharmaceuticals", "United Kingdom", "Cambridge", "Global biopharmaceutical company"),
        new CompanyData("GlaxoSmithKline", 41000, "Pharmaceuticals", "United Kingdom", "London", "Global healthcare company"),
        new CompanyData("Sanofi", 44000, "Pharmaceuticals", "France", "Paris", "Global healthcare leader"),
        new CompanyData("Gilead Sciences", 27000, "Pharmaceuticals", "USA", "California", "Biopharmaceutical company"),
        new CompanyData("Moderna", 19000, "Pharmaceuticals", "USA", "Massachusetts", "mRNA therapeutics and vaccines"),
        new CompanyData("BioNTech", 2000, "Pharmaceuticals", "Germany", "Mainz", "Biotechnology company"),
        
        // Financial Services (Top 15)
        new CompanyData("JPMorgan Chase", 158000, "Financial Services", "USA", "New York", "Global investment banking leader"),
        new CompanyData("Bank of America", 119000, "Financial Services", "USA", "North Carolina", "Consumer and commercial banking"),
        new CompanyData("Wells Fargo", 87000, "Financial Services", "USA", "California", "Diversified financial services"),
        new CompanyData("Goldman Sachs", 59000, "Financial Services", "USA", "New York", "Investment banking and asset management"),
        new CompanyData("Morgan Stanley", 64000, "Financial Services", "USA", "New York", "Investment banking and wealth management"),
        new CompanyData("Citigroup", 75000, "Financial Services", "USA", "New York", "Global banking and financial services"),
        new CompanyData("American Express", 64000, "Financial Services", "USA", "New York", "Global financial services company"),
        new CompanyData("Visa", 32000, "Financial Services", "USA", "California", "Global payments technology company"),
        new CompanyData("Mastercard", 25000, "Financial Services", "USA", "New York", "Global payment processing technology"),
        new CompanyData("BlackRock", 21000, "Financial Services", "USA", "New York", "Investment management corporation"),
        new CompanyData("Charles Schwab", 84000, "Financial Services", "USA", "Texas", "Financial services company"),
        new CompanyData("HSBC", 133000, "Financial Services", "United Kingdom", "London", "British multinational banking"),
        new CompanyData("Barclays", 83000, "Financial Services", "United Kingdom", "London", "British multinational bank"),
        new CompanyData("Deutsche Bank", 75000, "Financial Services", "Germany", "Frankfurt", "German multinational bank"),
        new CompanyData("Credit Suisse", 52000, "Financial Services", "Switzerland", "Zurich", "Global financial services"),
        
        // Automotive (Top 10)
        new CompanyData("Volkswagen Group", 295000, "Automotive", "Germany", "Wolfsburg", "Global automotive manufacturing leader"),
        new CompanyData("Toyota Motor", 274000, "Automotive", "Japan", "Toyota City", "Automotive innovation and manufacturing"),
        new CompanyData("Mercedes-Benz Group", 186000, "Automotive", "Germany", "Stuttgart", "Luxury automotive and mobility"),
        new CompanyData("BMW Group", 142000, "Automotive", "Germany", "Munich", "Premium automotive and mobility services"),
        new CompanyData("General Motors", 138000, "Automotive", "USA", "Michigan", "Automotive design and manufacturing"),
        new CompanyData("Ford Motor", 156000, "Automotive", "USA", "Michigan", "Global automotive company"),
        new CompanyData("Stellantis", 300000, "Automotive", "Netherlands", "Amsterdam", "Multinational automotive corporation"),
        new CompanyData("Honda Motor", 137000, "Automotive", "Japan", "Tokyo", "Automotive and motorcycle manufacturer"),
        new CompanyData("Nissan Motor", 134000, "Automotive", "Japan", "Yokohama", "Automotive manufacturer"),
        new CompanyData("Hyundai Motor", 105000, "Automotive", "South Korea", "Seoul", "Global automotive company"),
        
        // Additional Strategic Companies (Top 10)
        new CompanyData("Procter & Gamble", 84000, "Consumer Goods", "USA", "Ohio", "Consumer products and brands"),
        new CompanyData("Coca-Cola", 46000, "Beverages", "USA", "Georgia", "Global beverage company"),
        new CompanyData("PepsiCo", 91000, "Food & Beverages", "USA", "New York", "Food, snack, and beverage company"),
        new CompanyData("Unilever", 61000, "Consumer Goods", "United Kingdom", "London", "Consumer goods multinational"),
        new CompanyData("Nestlé", 103000, "Food & Beverages", "Switzerland", "Vevey", "Food and drink processing conglomerate"),
        new CompanyData("L'Oréal", 39000, "Consumer Goods", "France", "Paris", "Personal care and cosmetics"),
        new CompanyData("Nike", 51000, "Consumer Goods", "USA", "Oregon", "Athletic footwear and apparel"),
        new CompanyData("Adidas", 22000, "Consumer Goods", "Germany", "Bavaria", "Sporting goods manufacturer"),
        new CompanyData("LVMH", 86000, "Luxury Goods", "France", "Paris", "Luxury goods conglomerate"),
        new CompanyData("Berkshire Hathaway", 371000, "Conglomerate", "USA", "Nebraska", "Diversified investment holding company")
    };
    
    /**
     * Top 50 Most Influential Countries by Global Impact (2025)
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
        new CountryData("Switzerland", "Europe", "Developed", "Federal Republic", 8700000, 812867, "CHF", "German, French, Italian"),
        new CountryData("Belgium", "Europe", "Developed", "Federal Monarchy", 11500000, 529607, "EUR", "Dutch, French, German"),
        new CountryData("Austria", "Europe", "Developed", "Federal Republic", 9000000, 479819, "EUR", "German"),
        new CountryData("Ireland", "Europe", "Developed", "Republic", 5000000, 498561, "EUR", "English, Irish"),
        new CountryData("Denmark", "Europe", "Developed", "Constitutional Monarchy", 5800000, 395130, "DKK", "Danish"),
        
        // Asia-Pacific Powers
        new CountryData("South Korea", "Asia", "Developed", "Republic", 51780000, 1810972, "KRW", "Korean"),
        new CountryData("Australia", "Oceania", "Developed", "Federal Parliamentary", 25700000, 1552667, "AUD", "English"),
        new CountryData("Taiwan", "Asia", "Developed", "Republic", 23400000, 790728, "TWD", "Mandarin"),
        new CountryData("Singapore", "Asia", "Developed", "Parliamentary Republic", 5900000, 397000, "SGD", "English, Mandarin"),
        new CountryData("Thailand", "Asia", "Emerging", "Constitutional Monarchy", 69800000, 543500, "THB", "Thai"),
        new CountryData("Malaysia", "Asia", "Emerging", "Federal Constitutional Monarchy", 32400000, 432956, "MYR", "Malay"),
        new CountryData("Indonesia", "Asia", "Emerging", "Republic", 273500000, 1319100, "IDR", "Indonesian"),
        new CountryData("Philippines", "Asia", "Emerging", "Republic", 109000000, 394086, "PHP", "Filipino, English"),
        new CountryData("Vietnam", "Asia", "Emerging", "Socialist Republic", 97300000, 408802, "VND", "Vietnamese"),
        new CountryData("Bangladesh", "Asia", "Emerging", "Parliamentary Republic", 165000000, 460751, "BDT", "Bengali"),
        
        // Americas
        new CountryData("Canada", "North America", "Developed", "Federal Parliamentary", 38000000, 1988336, "CAD", "English, French"),
        new CountryData("Brazil", "South America", "Emerging", "Federal Republic", 215000000, 1608981, "BRL", "Portuguese"),
        new CountryData("Mexico", "North America", "Emerging", "Federal Republic", 128900000, 1293000, "MXN", "Spanish"),
        new CountryData("Argentina", "South America", "Emerging", "Federal Republic", 45400000, 487200, "ARS", "Spanish"),
        new CountryData("Chile", "South America", "Developed", "Republic", 19100000, 317000, "CLP", "Spanish"),
        new CountryData("Colombia", "South America", "Emerging", "Republic", 50900000, 314465, "COP", "Spanish"),
        new CountryData("Peru", "South America", "Emerging", "Republic", 33000000, 223249, "PEN", "Spanish"),
        new CountryData("Uruguay", "South America", "Developed", "Republic", 3500000, 62909, "UYU", "Spanish"),
        
        // Middle East & Africa
        new CountryData("Saudi Arabia", "Middle East", "Developed", "Absolute Monarchy", 34800000, 833540, "SAR", "Arabic"),
        new CountryData("United Arab Emirates", "Middle East", "Developed", "Federal Monarchy", 9900000, 507540, "AED", "Arabic"),
        new CountryData("Israel", "Middle East", "Developed", "Parliamentary Republic", 9400000, 481600, "ILS", "Hebrew"),
        new CountryData("Turkey", "Middle East", "Emerging", "Republic", 84300000, 819055, "TRY", "Turkish"),
        new CountryData("Iran", "Middle East", "Emerging", "Islamic Republic", 84000000, 231289, "IRR", "Persian"),
        new CountryData("Egypt", "Africa", "Emerging", "Republic", 104000000, 469440, "EGP", "Arabic"),
        new CountryData("South Africa", "Africa", "Emerging", "Republic", 59300000, 419000, "ZAR", "Multiple"),
        new CountryData("Nigeria", "Africa", "Emerging", "Federal Republic", 218500000, 440800, "NGN", "English"),
        new CountryData("Kenya", "Africa", "Emerging", "Republic", 54000000, 115000, "KES", "English, Swahili"),
        new CountryData("Morocco", "Africa", "Emerging", "Constitutional Monarchy", 37500000, 134000, "MAD", "Arabic"),
        
        // Eastern Europe & Russia
        new CountryData("Russia", "Europe/Asia", "Emerging", "Federal Republic", 146000000, 2240000, "RUB", "Russian"),
        new CountryData("Poland", "Europe", "Developed", "Republic", 38000000, 679000, "PLN", "Polish"),
        new CountryData("Czech Republic", "Europe", "Developed", "Parliamentary Republic", 10700000, 290000, "CZK", "Czech"),
        new CountryData("Hungary", "Europe", "Developed", "Parliamentary Republic", 9700000, 181000, "HUF", "Hungarian"),
        new CountryData("Romania", "Europe", "Developed", "Republic", 19100000, 250077, "RON", "Romanian"),
        new CountryData("Ukraine", "Europe", "Emerging", "Republic", 44100000, 200086, "UAH", "Ukrainian"),
        
        // Nordic Countries
        new CountryData("Sweden", "Europe", "Developed", "Constitutional Monarchy", 10400000, 635000, "SEK", "Swedish"),
        new CountryData("Norway", "Europe", "Developed", "Constitutional Monarchy", 5400000, 482000, "NOK", "Norwegian"),
        new CountryData("Finland", "Europe", "Developed", "Republic", 5500000, 297000, "EUR", "Finnish"),
        new CountryData("Iceland", "Europe", "Developed", "Parliamentary Republic", 370000, 25600, "ISK", "Icelandic"),
        
        // Additional Strategic Countries
        new CountryData("New Zealand", "Oceania", "Developed", "Parliamentary Democracy", 5100000, 249886, "NZD", "English")
    };
    
    /**
     * Top 40 Global Research Institutions and Centers (2025)
     */
    public static final ResearchData[] TOP_RESEARCH_CENTERS = {
        // US Elite Universities (Top 15)
        new ResearchData("MIT", "USA", "Technology, Engineering, AI, Quantum Computing", "University", "Cambridge, MA", "Leading technological research and innovation hub"),
        new ResearchData("Stanford University", "USA", "AI, Computer Science, Medicine, Entrepreneurship", "University", "Stanford, CA", "Silicon Valley innovation epicenter"),
        new ResearchData("Harvard University", "USA", "Medicine, Life Sciences, Policy, Business", "University", "Cambridge, MA", "Medical and policy research leadership"),
        new ResearchData("Caltech", "USA", "Physics, Engineering, Space Science", "University", "Pasadena, CA", "Small but elite science and engineering research"),
        new ResearchData("Princeton University", "USA", "Physics, Mathematics, Economics, AI", "University", "Princeton, NJ", "Theoretical research excellence"),
        new ResearchData("Yale University", "USA", "Medicine, Law, Social Sciences", "University", "New Haven, CT", "Medical and social science research"),
        new ResearchData("University of Chicago", "USA", "Economics, Physics, Medicine", "University", "Chicago, IL", "Nobel Prize winning research institution"),
        new ResearchData("Columbia University", "USA", "Medicine, Journalism, International Affairs", "University", "New York, NY", "Urban research and global studies"),
        new ResearchData("UC Berkeley", "USA", "Computer Science, Engineering, Physics", "University", "Berkeley, CA", "Public research university excellence"),
        new ResearchData("UCLA", "USA", "Medicine, Engineering, Life Sciences", "University", "Los Angeles, CA", "Leading public research university"),
        new ResearchData("University of Pennsylvania", "USA", "Medicine, Business, Engineering", "University", "Philadelphia, PA", "Ivy League research powerhouse"),
        new ResearchData("Johns Hopkins University", "USA", "Medicine, Public Health, Engineering", "University", "Baltimore, MD", "Medical research leader"),
        new ResearchData("Cornell University", "USA", "Agriculture, Engineering, Veterinary Medicine", "University", "Ithaca, NY", "Research university with broad expertise"),
        new ResearchData("Duke University", "USA", "Medicine, Engineering, Public Policy", "University", "Durham, NC", "Leading research university"),
        new ResearchData("Northwestern University", "USA", "Medicine, Engineering, Business", "University", "Evanston, IL", "Research and innovation leader"),
        
        // International Elite Universities (Top 10)
        new ResearchData("University of Oxford", "UK", "Medicine, AI, Climate Science, History", "University", "Oxford", "Centuries of research excellence and tradition"),
        new ResearchData("University of Cambridge", "UK", "Physics, Engineering, Medicine, AI", "University", "Cambridge", "Scientific research and innovation leadership"),
        new ResearchData("Imperial College London", "UK", "Engineering, Medicine, Natural Sciences", "University", "London", "Science, technology, and medicine focus"),
        new ResearchData("UCL", "UK", "Medicine, Engineering, Social Sciences", "University", "London", "London's global university"),
        new ResearchData("King's College London", "UK", "Medicine, Law, Social Sciences", "University", "London", "Research excellence in health and humanities"),
        new ResearchData("ETH Zurich", "Switzerland", "Technology, Engineering, Natural Sciences", "University", "Zurich", "European technology research leader"),
        new ResearchData("EPFL", "Switzerland", "Technology, Engineering, Life Sciences", "University", "Lausanne", "Swiss technology institute"),
        new ResearchData("University of Toronto", "Canada", "AI, Medicine, Engineering", "University", "Toronto", "Leading AI research and innovation"),
        new ResearchData("McGill University", "Canada", "Medicine, Engineering, Natural Sciences", "University", "Montreal", "Canadian research excellence"),
        new ResearchData("University of Melbourne", "Australia", "Medicine, Engineering, Sciences", "University", "Melbourne", "Leading Australian research university"),
        
        // Asian Research Powerhouses (Top 10)
        new ResearchData("Chinese Academy of Sciences", "China", "Multidisciplinary Science, Space, AI", "Government", "Beijing", "China's premier research institution"),
        new ResearchData("Tsinghua University", "China", "Engineering, Technology, AI, Policy", "University", "Beijing", "China's top technical university"),
        new ResearchData("Peking University", "China", "Natural Sciences, Medicine, Social Sciences", "University", "Beijing", "Leading comprehensive research university"),
        new ResearchData("University of Tokyo", "Japan", "Natural Sciences, Engineering, Medicine", "University", "Tokyo", "Japan's premier research university"),
        new ResearchData("RIKEN", "Japan", "Physical Sciences, Biology, AI", "Government", "Saitama", "Japan's largest research institute"),
        new ResearchData("Seoul National University", "South Korea", "Engineering, Natural Sciences, Medicine", "University", "Seoul", "South Korea's top research university"),
        new ResearchData("National University of Singapore", "Singapore", "Engineering, Medicine, AI", "University", "Singapore", "Leading Asian research university"),
        new ResearchData("Nanyang Technological University", "Singapore", "Engineering, Technology, Business", "University", "Singapore", "Technology-focused research university"),
        new ResearchData("Indian Institute of Science", "India", "Science, Engineering, Technology", "University", "Bangalore", "India's premier research institute"),
        new ResearchData("Indian Institute of Technology Delhi", "India", "Engineering, Technology, Sciences", "University", "New Delhi", "Leading technical institute"),
        
        // European Research Excellence (Top 5)
        new ResearchData("Max Planck Society", "Germany", "Basic Science, Physics, Chemistry", "Independent", "Munich", "Fundamental research across sciences"),
        new ResearchData("CERN", "Switzerland", "Particle Physics, Computing", "International", "Geneva", "European particle physics research center"),
        new ResearchData("CNRS", "France", "Multidisciplinary Research, Physics", "Government", "Paris", "France's national research organization"),
        new ResearchData("Sorbonne University", "France", "Medicine, Sciences, Humanities", "University", "Paris", "Leading French research university"),
        new ResearchData("Technical University of Munich", "Germany", "Engineering, Technology, Natural Sciences", "University", "Munich", "German technical research excellence")
    };
    
    /**
     * Enhanced Alternative Future Scenarios (20 scenarios for comprehensive analysis)
     */
    public static final FutureScenario[] ALTERNATIVE_FUTURES = {
        // Technology-Driven Futures
        new FutureScenario("AI Supremacy", "Artificial Intelligence becomes the dominant force reshaping all sectors", 0.18,
            "AI companies like NVIDIA, Google, and OpenAI control global decision-making, education becomes AI-centric"),
        new FutureScenario("Quantum Revolution", "Quantum computing breakthrough transforms computing and cryptography", 0.12,
            "IBM, Google, and research institutions lead quantum advantage, cybersecurity revolutionized"),
        new FutureScenario("Biotech Renaissance", "Biotechnology advances cure aging and enhance human capabilities", 0.10,
            "Pharmaceutical companies and medical research institutions drive human enhancement"),
        new FutureScenario("Metaverse Dominance", "Virtual and augmented reality become primary interaction modes", 0.08,
            "Meta, Apple, and gaming companies create persistent virtual economies"),
        new FutureScenario("Space Economy Boom", "Space commercialization drives new economic sectors", 0.06,
            "SpaceX, Blue Origin, and space agencies create orbital manufacturing and tourism"),
        
        // Environmental Futures
        new FutureScenario("Green Transition Triumph", "Renewable energy and sustainability become economic drivers", 0.20,
            "Tesla, renewable energy companies, and Nordic countries lead carbon-neutral transformation"),
        new FutureScenario("Climate Crisis Response", "Urgent climate action reshapes global priorities and governance", 0.15,
            "Emergency climate measures dominate policy, massive green infrastructure investments"),
        new FutureScenario("Circular Economy Revolution", "Waste elimination and resource recycling transform production", 0.09,
            "Companies adopt zero-waste models, recycling becomes primary resource source"),
        
        // Geopolitical Futures
        new FutureScenario("Multipolar World Order", "Multiple power centers compete for global influence", 0.16,
            "US-China rivalry, EU autonomy, emerging middle powers reshape international relations"),
        new FutureScenario("Regional Bloc Dominance", "World divides into competing regional economic and political blocs", 0.13,
            "EU, ASEAN, USMCA become primary governance units, reduced global cooperation"),
        new FutureScenario("Democratic Renaissance", "Renewed focus on democratic values and institutions globally", 0.11,
            "Democratic countries strengthen alliances, authoritarian systems face pressure"),
        new FutureScenario("Cyber Warfare Era", "Digital conflicts become primary form of international competition", 0.07,
            "Nation-states and corporations engage in constant cyber operations"),
        
        // Economic Futures
        new FutureScenario("Digital Economy Supremacy", "Virtual and digital interactions dominate economic activity", 0.14,
            "Meta, Microsoft, gaming companies lead virtual economy transformation"),
        new FutureScenario("Resource Scarcity Wars", "Competition for rare earth metals and water intensifies conflicts", 0.10,
            "Mining companies, water tech firms, and resource-rich countries gain power"),
        new FutureScenario("Post-Capitalism Society", "New economic models replace traditional market capitalism", 0.05,
            "Universal basic income, cooperative ownership, and sharing economy dominate"),
        
        // Social Futures
        new FutureScenario("Aging Society Adaptation", "Demographic transition drives innovation in eldercare and automation", 0.13,
            "Japan, Germany lead aging solutions, robotics and healthcare AI advance rapidly"),
        new FutureScenario("Global Education Revolution", "Personalized AI education transforms human development", 0.17,
            "Universities partner with AI companies, traditional education models disrupted"),
        new FutureScenario("Post-Work Society", "Automation eliminates most traditional employment", 0.08,
            "Universal basic income, creative economy, and leisure-based society emerge"),
        
        // Health Futures
        new FutureScenario("Longevity Breakthrough", "Life extension technologies create centuries-long lifespans", 0.04,
            "Pharmaceutical companies achieve aging reversal, society restructures for extended life"),
        new FutureScenario("Pandemic Preparedness Era", "Global health security becomes primary policy focus", 0.09,
            "WHO, health agencies, and pharmaceutical companies create rapid response systems")
    };
    
    // Data classes remain the same but enhanced
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
    
    /**
     * Enhanced PESTEL Variables - Core + Extended
     */
    public static final Map<String, String[]> ENHANCED_PESTEL_VARIABLES = Map.of(
        "political", new String[]{
            "tax_labour", "trade", "policies_laws", "corruption", "stability", 
            "international_relations", "regulatory_environment", "government_spending",
            "political_risk", "diplomatic_relations", "sovereignty_issues"
        },
        "economic", new String[]{
            "income", "interest_rates", "growth", "employment", "inflation",
            "market_dynamics", "currency_stability", "trade_balance", 
            "investment_climate", "economic_inequality", "debt_levels"
        },
        "social", new String[]{
            "ageing", "career_views", "cultural_barriers", "population", "lifestyle",
            "social_development", "education_levels", "health_consciousness",
            "social_mobility", "demographic_trends", "cultural_values"
        },
        "technological", new String[]{
            "innovation", "automation", "technology_incentives", "rd_activity",
            "innovation_ecosystem", "digital_infrastructure", "ai_adoption",
            "cybersecurity_maturity", "tech_talent_availability", "research_collaboration"
        },
        "environmental", new String[]{
            "climate_change", "recycling_disposal", "ethical", "sustainability",
            "climate_leadership", "resource_availability", "pollution_levels",
            "biodiversity_status", "renewable_energy_adoption", "environmental_regulations"
        },
        "legal", new String[]{
            "data_protection", "antitrust", "health_safety", "copyright", "labour",
            "regulatory_framework", "intellectual_property", "contract_enforcement",
            "judicial_independence", "legal_transparency", "compliance_requirements"
        }
    );
}
