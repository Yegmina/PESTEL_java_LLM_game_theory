package simu.model;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * AI Service for PESTEL-based decision making using Gemma 3 model
 */
public class PESTELAIService {
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-lite:generateContent";
    private static final String API_KEY_PROPERTY = "GEMINI_API_KEY";
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(30);
    private static final int MAX_RETRIES = 3;
    
    private final HttpClient httpClient;
    private final String apiKey;
    private final Map<String, Object> requestCache;
    
    public PESTELAIService(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(REQUEST_TIMEOUT)
                .build();
        this.requestCache = new HashMap<>();
    }
    
    public PESTELAIService() {
        this(System.getProperty(API_KEY_PROPERTY, ""));
    }
    
    /**
     * Ask AI if agent wants to take action
     */
    public CompletableFuture<String> askAgentDecision(PESTELAgent agent, PESTELState globalPESTEL, 
                                                     int currentDay, List<AgentAction> recentActions) {
        String prompt = buildAgentDecisionPrompt(agent, globalPESTEL, currentDay, recentActions);
        return generateContent(prompt, "agent_decision")
                .thenApply(this::parseAgentDecisionResponse);
    }
    
    /**
     * Ask AI how a decision affects a specific PESTEL factor
     */
    public CompletableFuture<String> askPESTELImpact(String decision, String pestelCategory, 
                                                    PESTELState currentState) {
        String prompt = buildPESTELImpactPrompt(decision, pestelCategory, currentState);
        return generateContent(prompt, "pestel_impact")
                .thenApply(response -> parsePESTELImpactResponse(response, pestelCategory));
    }
    
    /**
     * Ask AI which agents are affected by a decision
     */
    public CompletableFuture<List<String>> askAffectedAgents(String decision, List<PESTELAgent> allAgents) {
        String prompt = buildAffectedAgentsPrompt(decision, allAgents);
        return generateContent(prompt, "affected_agents")
                .thenApply(this::parseAffectedAgentsResponse);
    }
    
    /**
     * Parse AI response to correct format if parsing fails
     */
    public CompletableFuture<String> parseToCorrectFormat(String response, String expectedFormat) {
        String prompt = buildParsingPrompt(response, expectedFormat);
        return generateContent(prompt, "format_correction");
    }
    
    private String buildAgentDecisionPrompt(PESTELAgent agent, PESTELState globalPESTEL, 
                                          int currentDay, List<AgentAction> recentActions) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are ").append(agent.getAgentDescription()).append(".\n\n");
        
        prompt.append("CURRENT SITUATION:\n");
        prompt.append("Day: ").append(currentDay).append("\n");
        prompt.append("Your decisions made so far: ").append(agent.getDecisionCount()).append("\n\n");
        
        prompt.append("GLOBAL PESTEL STATE:\n");
        prompt.append(globalPESTEL.toString()).append("\n\n");
        
        if (!recentActions.isEmpty()) {
            prompt.append("RECENT ACTIONS BY OTHER AGENTS:\n");
            for (AgentAction action : recentActions) {
                if (!action.getAgentId().equals(agent.getAgentId())) {
                    prompt.append("- Day ").append(action.getDay()).append(": ")
                           .append(action.getAgentId()).append(" - ")
                           .append(action.getActionDescription()).append("\n");
                }
            }
            prompt.append("\n");
        }
        
        prompt.append("YOUR LOCAL PESTEL STATE:\n");
        prompt.append(agent.getLocalPESTEL().toString()).append("\n\n");
        
        prompt.append("QUESTION: Do you want to take any action today?\n");
        prompt.append("Respond with EITHER:\n");
        prompt.append("- 'NO' if you don't want to take any action\n");
        prompt.append("- A specific action description if you want to act\n\n");
        prompt.append("If taking action, describe it clearly in one sentence.\n");
        prompt.append("Consider your role, current conditions, and recent events.\n");
        
        return prompt.toString();
    }
    
    private String buildPESTELImpactPrompt(String decision, String pestelCategory, PESTELState currentState) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("DECISION TAKEN: ").append(decision).append("\n\n");
        
        prompt.append("CURRENT ").append(pestelCategory.toUpperCase()).append(" STATE:\n");
        switch (pestelCategory.toLowerCase()) {
            case "political":
            case "p":
                currentState.getAllPolitical().forEach((k, v) -> 
                    prompt.append("- ").append(k).append(": ").append(v).append("\n"));
                break;
            case "economic":
            case "e":
                currentState.getAllEconomic().forEach((k, v) -> 
                    prompt.append("- ").append(k).append(": ").append(v).append("\n"));
                break;
            case "social":
            case "s":
                currentState.getAllSocial().forEach((k, v) -> 
                    prompt.append("- ").append(k).append(": ").append(v).append("\n"));
                break;
            case "technological":
            case "t":
                currentState.getAllTechnological().forEach((k, v) -> 
                    prompt.append("- ").append(k).append(": ").append(v).append("\n"));
                break;
            case "environmental":
            case "env":
                currentState.getAllEnvironmental().forEach((k, v) -> 
                    prompt.append("- ").append(k).append(": ").append(v).append("\n"));
                break;
            case "legal":
            case "l":
                currentState.getAllLegal().forEach((k, v) -> 
                    prompt.append("- ").append(k).append(": ").append(v).append("\n"));
                break;
        }
        
        prompt.append("\nQUESTION: How does this decision affect the ").append(pestelCategory.toUpperCase()).append(" factors?\n\n");
        prompt.append("Respond in this EXACT format:\n");
        prompt.append("FACTOR: [factor_name]\n");
        prompt.append("NEW_VALUE: [new description of this factor]\n");
        prompt.append("REASON: [why this change occurred]\n\n");
        prompt.append("If multiple factors are affected, list each one separately.\n");
        prompt.append("If no factors are affected, respond with: NO_IMPACT\n");
        
        return prompt.toString();
    }
    
    private String buildAffectedAgentsPrompt(String decision, List<PESTELAgent> allAgents) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("DECISION TAKEN: ").append(decision).append("\n\n");
        
        prompt.append("AVAILABLE AGENTS:\n");
        for (PESTELAgent agent : allAgents) {
            prompt.append("- ").append(agent.getAgentId()).append(": ").append(agent.getAgentDescription()).append("\n");
        }
        
        prompt.append("\nQUESTION: Which agents might be affected by this decision?\n\n");
        prompt.append("Respond with a comma-separated list of agent IDs.\n");
        prompt.append("Example: Company_1, Country_2, Researcher_1\n");
        prompt.append("If no agents are affected, respond with: NONE\n");
        
        return prompt.toString();
    }
    
    private String buildParsingPrompt(String response, String expectedFormat) {
        return String.format("""
            The following response needs to be reformatted:
            
            RESPONSE: %s
            
            EXPECTED FORMAT: %s
            
            Please reformat the response to match the expected format exactly.
            """, response, expectedFormat);
    }
    
    private CompletableFuture<String> generateContent(String prompt, String requestType) {
        String cacheKey = requestType + "_" + prompt.hashCode();
        if (requestCache.containsKey(cacheKey)) {
            return CompletableFuture.completedFuture((String) requestCache.get(cacheKey));
        }
        
        return CompletableFuture.supplyAsync(() -> {
            for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
                try {
                    String response = makeGeminiRequest(prompt);
                    requestCache.put(cacheKey, response);
                    return response;
                } catch (Exception e) {
                    simu.framework.Trace.out(simu.framework.Trace.Level.WAR, 
                        "Gemini API request failed (attempt " + attempt + "/" + MAX_RETRIES + "): " + e.getMessage());
                    
                    if (attempt == MAX_RETRIES) {
                        return generateFallbackResponse(requestType);
                    }
                    
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
            throw new IOException("Gemini API request failed with status: " + response.statusCode());
        }
        
        return extractTextFromResponse(response.body());
    }
    
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
    
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    private String extractTextFromResponse(String jsonResponse) {
        // Simple JSON parsing - extract text from response
        try {
            int textStart = jsonResponse.indexOf("\"text\":");
            if (textStart != -1) {
                textStart = jsonResponse.indexOf("\"", textStart + 7) + 1;
                int textEnd = jsonResponse.indexOf("\"", textStart);
                return jsonResponse.substring(textStart, textEnd)
                    .replace("\\n", "\n")
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\");
            }
        } catch (Exception e) {
            // Fallback to returning the whole response
        }
        return jsonResponse;
    }
    
    private String parseAgentDecisionResponse(String response) {
        String cleaned = response.trim().toUpperCase();
        if (cleaned.equals("NO") || cleaned.contains("NO ACTION") || cleaned.contains("NO_ACTION")) {
            return "no_action";
        }
        return response.trim();
    }
    
    private String parsePESTELImpactResponse(String response, String category) {
        if (response.trim().toUpperCase().equals("NO_IMPACT")) {
            return "NO_IMPACT";
        }
        
        // Parse structured response
        Map<String, String> parsed = new HashMap<>();
        String[] lines = response.split("\n");
        
        for (String line : lines) {
            if (line.startsWith("FACTOR:")) {
                parsed.put("factor", line.substring(7).trim());
            } else if (line.startsWith("NEW_VALUE:")) {
                parsed.put("new_value", line.substring(10).trim());
            } else if (line.startsWith("REASON:")) {
                parsed.put("reason", line.substring(7).trim());
            }
        }
        
        if (parsed.containsKey("factor") && parsed.containsKey("new_value")) {
            return String.format("FACTOR:%s|NEW_VALUE:%s|REASON:%s", 
                parsed.get("factor"), parsed.get("new_value"), parsed.getOrDefault("reason", ""));
        }
        
        return response; // Return original if parsing fails
    }
    
    private List<String> parseAffectedAgentsResponse(String response) {
        String cleaned = response.trim().toUpperCase();
        if (cleaned.equals("NONE")) {
            return new ArrayList<>();
        }
        
        List<String> agents = new ArrayList<>();
        String[] parts = response.split(",");
        for (String part : parts) {
            String agentId = part.trim();
            if (!agentId.isEmpty() && !agentId.equalsIgnoreCase("none")) {
                agents.add(agentId);
            }
        }
        return agents;
    }
    
    private String generateFallbackResponse(String requestType) {
        switch (requestType) {
            case "agent_decision":
                return Math.random() < 0.3 ? "Take standard action based on current conditions" : "no_action";
            case "pestel_impact":
                return "NO_IMPACT";
            case "affected_agents":
                return "NONE";
            default:
                return "AI service unavailable";
        }
    }
    
    public void clearCache() {
        requestCache.clear();
    }
    
    public int getCacheSize() {
        return requestCache.size();
    }
}
