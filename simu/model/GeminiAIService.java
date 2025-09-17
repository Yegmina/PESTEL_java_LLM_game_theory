package simu.model;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Gemini AI Service provides integration with Google's Gemini API for Gemma 3 model usage.
 * This service uses the Gemma 2 9B Instruct model (gemma-2-9b-it) to enable the forecasting 
 * system to leverage advanced AI capabilities for analysis, prediction, and recommendation generation.
 */
public class GeminiAIService {
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemma-2-9b-it:generateContent";
    private static final String API_KEY_PROPERTY = "GEMINI_API_KEY";
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(30);
    private static final int MAX_RETRIES = 3;
    
    private final HttpClient httpClient;
    private final String apiKey;
    private final Map<String, Object> requestCache;
    
    /**
     * Initialize the Gemini AI Service
     * @param apiKey The Gemini API key
     */
    public GeminiAIService(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(REQUEST_TIMEOUT)
                .build();
        this.requestCache = new HashMap<>();
    }
    
    /**
     * Initialize the Gemini AI Service with API key from system property
     */
    public GeminiAIService() {
        this(System.getProperty(API_KEY_PROPERTY, ""));
    }
    
    /**
     * Analyze the current global state and generate insights
     * @param globalState Current global state
     * @return AI analysis results
     */
    public CompletableFuture<AIAnalysisResult> analyzeGlobalState(GlobalState globalState) {
        String prompt = buildGlobalStateAnalysisPrompt(globalState);
        return generateContent(prompt, "global_state_analysis")
                .thenApply(response -> parseGlobalStateAnalysis(response, globalState));
    }
    
    /**
     * Generate scenario predictions based on current conditions
     * @param scenarios List of current scenarios
     * @param globalState Current global state
     * @return AI prediction results
     */
    public CompletableFuture<AIPredictionResult> predictScenarios(
            java.util.List<ScenarioGenerator.Scenario> scenarios, GlobalState globalState) {
        String prompt = buildScenarioPredictionPrompt(scenarios, globalState);
        return generateContent(prompt, "scenario_prediction")
                .thenApply(response -> parseScenarioPrediction(response, scenarios));
    }
    
    /**
     * Generate strategic recommendations based on analysis
     * @param analysisResult AI analysis result
     * @param predictionResult AI prediction result
     * @return Strategic recommendations
     */
    public CompletableFuture<AIRecommendationResult> generateRecommendations(
            AIAnalysisResult analysisResult, AIPredictionResult predictionResult) {
        String prompt = buildRecommendationPrompt(analysisResult, predictionResult);
        return generateContent(prompt, "strategic_recommendations")
                .thenApply(response -> parseRecommendations(response));
    }
    
    /**
     * Generate content using the Gemini API with retry mechanism
     * @param prompt The input prompt
     * @param requestType Type of request for caching
     * @return CompletableFuture with the response
     */
    private CompletableFuture<String> generateContent(String prompt, String requestType) {
        // Check cache first
        String cacheKey = requestType + "_" + prompt.hashCode();
        if (requestCache.containsKey(cacheKey)) {
            return CompletableFuture.completedFuture((String) requestCache.get(cacheKey));
        }
        
        return CompletableFuture.supplyAsync(() -> {
            for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
                try {
                    String response = makeGeminiRequest(prompt);
                    // Cache successful response
                    requestCache.put(cacheKey, response);
                    return response;
                } catch (Exception e) {
                    simu.framework.Trace.out(simu.framework.Trace.Level.WAR, 
                        "Gemini API request failed (attempt " + attempt + "/" + MAX_RETRIES + "): " + e.getMessage());
                    
                    if (attempt == MAX_RETRIES) {
                        // Return fallback response after all retries failed
                        return generateFallbackResponse(requestType);
                    }
                    
                    // Wait before retry
                    try {
                        TimeUnit.SECONDS.sleep(attempt * 2);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return generateFallbackResponse(requestType);
                    }
                }
            }
            return generateFallbackResponse(requestType);
        });
    }
    
    /**
     * Make actual HTTP request to Gemini API
     * @param prompt The input prompt
     * @return API response
     * @throws IOException If request fails
     * @throws InterruptedException If request is interrupted
     */
    private String makeGeminiRequest(String prompt) throws IOException, InterruptedException {
        if (apiKey.isEmpty()) {
            throw new IllegalStateException("Gemini API key not provided");
        }
        
        String requestBody = buildRequestBody(prompt);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GEMINI_API_URL + "?key=" + apiKey))
                .header("Content-Type", "application/json")
                .timeout(REQUEST_TIMEOUT)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        
        HttpResponse<String> response = httpClient.send(request, 
                HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new IOException("Gemini API request failed with status: " + response.statusCode() + 
                                ", body: " + response.body());
        }
        
        return response.body();
    }
    
    /**
     * Build the request body for Gemini API
     * @param prompt The input prompt
     * @return JSON request body
     */
    private String buildRequestBody(String prompt) {
        return String.format("""
            {
                "contents": [{
                    "parts": [{
                        "text": "%s"
                    }]
                }],
                "generationConfig": {
                    "temperature": 0.7,
                    "topK": 40,
                    "topP": 0.95,
                    "maxOutputTokens": 1024
                }
            }
            """, escapeJson(prompt));
    }
    
    /**
     * Escape JSON special characters
     * @param text Text to escape
     * @return Escaped text
     */
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    /**
     * Build prompt for global state analysis
     * @param globalState Current global state
     * @return Analysis prompt
     */
    private String buildGlobalStateAnalysisPrompt(GlobalState globalState) {
        return String.format("""
            As an AI analyst for Metropolia University's strategic planning system, analyze the following global state indicators:
            
            Global Temperature: %.2f°C
            World Peace Index: %.2f
            Economic Growth Rate: %.2f%%
            Resource Availability: %.2f
            Technology Advancement: %.2f
            Population Growth Rate: %.2f%%
            System Stability Index: %.2f
            
            Please provide:
            1. Overall system health assessment
            2. Key risk factors and opportunities
            3. Trend analysis and predictions
            4. Critical indicators to monitor
            
            Format your response as a structured analysis suitable for strategic decision-making.
            """, 
            globalState.getIndicator("global_temperature"),
            globalState.getIndicator("world_peace_index"),
            globalState.getIndicator("economic_growth_rate") * 100,
            globalState.getIndicator("resource_availability"),
            globalState.getIndicator("technology_advancement"),
            globalState.getIndicator("population_growth_rate") * 100,
            globalState.calculateStabilityIndex());
    }
    
    /**
     * Build prompt for scenario prediction
     * @param scenarios Current scenarios
     * @param globalState Current global state
     * @return Prediction prompt
     */
    private String buildScenarioPredictionPrompt(
            java.util.List<ScenarioGenerator.Scenario> scenarios, GlobalState globalState) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("As an AI forecaster, analyze these future scenarios for Metropolia University:\n\n");
        
        for (ScenarioGenerator.Scenario scenario : scenarios) {
            prompt.append(String.format("Scenario %d: %s (Probability: %.2f%%)\n", 
                scenario.getScenarioId(), scenario.getScenarioName(), scenario.getProbability() * 100));
        }
        
        prompt.append(String.format("""
            
            Current Global State:
            - Economic Growth: %.2f%%
            - Technology Level: %.2f
            - Stability Index: %.2f
            
            Please provide:
            1. Most likely scenario outcomes
            2. Scenario transition probabilities
            3. Key factors driving scenario evolution
            4. Timeline predictions for major changes
            
            Format as strategic forecasting analysis.
            """,
            globalState.getIndicator("economic_growth_rate") * 100,
            globalState.getIndicator("technology_advancement"),
            globalState.calculateStabilityIndex()));
        
        return prompt.toString();
    }
    
    /**
     * Build prompt for strategic recommendations
     * @param analysisResult AI analysis result
     * @param predictionResult AI prediction result
     * @return Recommendation prompt
     */
    private String buildRecommendationPrompt(AIAnalysisResult analysisResult, AIPredictionResult predictionResult) {
        return String.format("""
            Based on the following AI analysis and predictions, provide strategic recommendations for Metropolia University:
            
            Analysis Summary: %s
            Key Risks: %s
            Key Opportunities: %s
            
            Prediction Summary: %s
            Most Likely Scenarios: %s
            
            Please provide:
            1. Immediate action items (next 6 months)
            2. Medium-term strategies (1-3 years)
            3. Long-term vision adjustments (3-5 years)
            4. Risk mitigation strategies
            5. Opportunity capitalization plans
            
            Format as executive strategic recommendations.
            """,
            analysisResult.getSummary(),
            analysisResult.getKeyRisks(),
            analysisResult.getKeyOpportunities(),
            predictionResult.getSummary(),
            predictionResult.getMostLikelyScenarios());
    }
    
    /**
     * Parse global state analysis response
     * @param response API response
     * @param globalState Global state
     * @return Analysis result
     */
    private AIAnalysisResult parseGlobalStateAnalysis(String response, GlobalState globalState) {
        // Extract structured information from AI response
        String summary = extractSection(response, "Overall system health assessment");
        String risks = extractSection(response, "Key risk factors");
        String opportunities = extractSection(response, "opportunities");
        String trends = extractSection(response, "Trend analysis");
        
        return new AIAnalysisResult(summary, risks, opportunities, trends, globalState);
    }
    
    /**
     * Parse scenario prediction response
     * @param response API response
     * @param scenarios Current scenarios
     * @return Prediction result
     */
    private AIPredictionResult parseScenarioPrediction(String response, 
            java.util.List<ScenarioGenerator.Scenario> scenarios) {
        String summary = extractSection(response, "Most likely scenario outcomes");
        String transitions = extractSection(response, "Scenario transition probabilities");
        String factors = extractSection(response, "Key factors driving");
        String timeline = extractSection(response, "Timeline predictions");
        
        return new AIPredictionResult(summary, transitions, factors, timeline, scenarios);
    }
    
    /**
     * Parse recommendations response
     * @param response API response
     * @return Recommendation result
     */
    private AIRecommendationResult parseRecommendations(String response) {
        String immediateActions = extractSection(response, "Immediate action items");
        String mediumTerm = extractSection(response, "Medium-term strategies");
        String longTerm = extractSection(response, "Long-term vision");
        String riskMitigation = extractSection(response, "Risk mitigation");
        String opportunities = extractSection(response, "Opportunity capitalization");
        
        return new AIRecommendationResult(immediateActions, mediumTerm, longTerm, riskMitigation, opportunities);
    }
    
    /**
     * Extract a section from AI response
     * @param response Full response
     * @param sectionName Section to extract
     * @return Extracted section content
     */
    private String extractSection(String response, String sectionName) {
        // Simple text extraction - in production, use more sophisticated parsing
        String[] lines = response.split("\n");
        StringBuilder section = new StringBuilder();
        boolean inSection = false;
        
        for (String line : lines) {
            if (line.toLowerCase().contains(sectionName.toLowerCase())) {
                inSection = true;
                continue;
            }
            if (inSection && (line.trim().isEmpty() || line.matches("^\\d+\\..*"))) {
                break;
            }
            if (inSection) {
                section.append(line).append("\n");
            }
        }
        
        return section.toString().trim();
    }
    
    /**
     * Generate fallback response when API fails
     * @param requestType Type of request
     * @return Fallback response
     */
    private String generateFallbackResponse(String requestType) {
        switch (requestType) {
            case "global_state_analysis":
                return "System analysis unavailable - using default assessment. Monitor key indicators closely.";
            case "scenario_prediction":
                return "Scenario prediction unavailable - using probabilistic models. Review scenario probabilities.";
            case "strategic_recommendations":
                return "AI recommendations unavailable - use standard strategic planning processes.";
            default:
                return "AI service temporarily unavailable - using fallback analysis.";
        }
    }
    
    /**
     * Clear the request cache
     */
    public void clearCache() {
        requestCache.clear();
    }
    
    /**
     * Get cache size
     * @return Number of cached requests
     */
    public int getCacheSize() {
        return requestCache.size();
    }
}
