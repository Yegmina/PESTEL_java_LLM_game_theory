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
 * AI Service using Qwen3-Next-80B-A3B-Thinking model for PESTEL-based decision making
 * This model excels in complex reasoning tasks and strategic analysis
 * 
 * Based on Qwen3-Next-80B-A3B-Thinking from Hugging Face:
 * https://huggingface.co/Qwen/Qwen3-Next-80B-A3B-Thinking
 */
public class QwenAIService {
    private static final String HUGGINGFACE_API_URL = "https://api-inference.huggingface.co/models/Qwen/Qwen3-Next-80B-A3B-Thinking";
    private static final String API_KEY_PROPERTY = "HUGGINGFACE_API_KEY";
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(45);
    private static final int MAX_RETRIES = 3;
    
    private final HttpClient httpClient;
    private final String apiKey;
    private final Map<String, Object> requestCache;
    
    public QwenAIService(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(REQUEST_TIMEOUT)
                .build();
        this.requestCache = new HashMap<>();
    }
    
    public QwenAIService() {
        this(System.getProperty(API_KEY_PROPERTY, ""));
    }
    
    /**
     * Ask AI if agent wants to take action using Qwen3-Next thinking capabilities
     */
    public CompletableFuture<String> askAgentDecision(PESTELAgent agent, PESTELState globalPESTEL, 
                                                     int currentDay, List<AgentAction> recentActions) {
        String prompt = buildAgentDecisionPrompt(agent, globalPESTEL, currentDay, recentActions);
        return generateContent(prompt, "agent_decision")
                .thenApply(this::parseAgentDecisionResponse);
    }
    
    /**
     * Ask AI how a decision affects a specific PESTEL factor using advanced reasoning
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
     * Generate strategic analysis using Qwen3-Next's advanced reasoning
     */
    public CompletableFuture<String> generateStrategicAnalysis(PESTELState globalPESTEL, 
                                                              List<AgentAction> recentActions) {
        String prompt = buildStrategicAnalysisPrompt(globalPESTEL, recentActions);
        return generateContent(prompt, "strategic_analysis");
    }
    
    private String buildAgentDecisionPrompt(PESTELAgent agent, PESTELState globalPESTEL, 
                                          int currentDay, List<AgentAction> recentActions) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("<think>\n");
        prompt.append("I need to analyze this complex strategic situation for ").append(agent.getAgentDescription()).append(".\n");
        prompt.append("Let me consider the current global PESTEL state, recent actions by other agents, and this agent's specific context.\n");
        prompt.append("</think>\n\n");
        
        prompt.append("You are a strategic advisor for ").append(agent.getAgentDescription()).append(".\n\n");
        
        prompt.append("CURRENT SITUATION:\n");
        prompt.append("Day: ").append(currentDay).append("\n");
        prompt.append("Your decisions made so far: ").append(agent.getDecisionCount()).append("\n\n");
        
        prompt.append("GLOBAL PESTEL STATE:\n");
        prompt.append(globalPESTEL.toString()).append("\n\n");
        
        if (!recentActions.isEmpty()) {
            prompt.append("RECENT ACTIONS BY OTHER GLOBAL ENTITIES:\n");
            for (AgentAction action : recentActions.subList(Math.max(0, recentActions.size() - 10), recentActions.size())) {
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
        
        prompt.append("STRATEGIC QUESTION: Should you take any strategic action today?\n\n");
        prompt.append("Consider:\n");
        prompt.append("- Your organization's current position and capabilities\n");
        prompt.append("- Global trends and recent developments\n");
        prompt.append("- Potential strategic opportunities or threats\n");
        prompt.append("- Your stakeholders and competitive environment\n\n");
        
        prompt.append("Respond with EITHER:\n");
        prompt.append("- 'NO' if no action is needed today\n");
        prompt.append("- A specific strategic action description (one clear sentence)\n\n");
        prompt.append("If taking action, make it realistic and impactful for your organization.\n");
        
        return prompt.toString();
    }
    
    private String buildPESTELImpactPrompt(String decision, String pestelCategory, PESTELState currentState) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("<think>\n");
        prompt.append("I need to analyze how this strategic decision affects the ").append(pestelCategory.toUpperCase()).append(" factors.\n");
        prompt.append("Let me think through the direct and indirect impacts systematically.\n");
        prompt.append("</think>\n\n");
        
        prompt.append("STRATEGIC DECISION TAKEN: ").append(decision).append("\n\n");
        
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
        
        prompt.append("\nANALYSIS QUESTION: How does this strategic decision impact the ").append(pestelCategory.toUpperCase()).append(" factors?\n\n");
        prompt.append("Analyze the direct and indirect effects, considering:\n");
        prompt.append("- Immediate impact on existing ").append(pestelCategory).append(" factors\n");
        prompt.append("- Cascading effects on related areas\n");
        prompt.append("- Long-term implications for the ").append(pestelCategory).append(" environment\n\n");
        
        prompt.append("Respond in this EXACT format:\n");
        prompt.append("FACTOR: [specific_factor_name]\n");
        prompt.append("NEW_VALUE: [detailed new description of this factor]\n");
        prompt.append("REASON: [clear explanation of why this change occurred]\n\n");
        prompt.append("If multiple factors are affected, list each one separately.\n");
        prompt.append("If no factors are significantly affected, respond with: NO_IMPACT\n");
        
        return prompt.toString();
    }
    
    private String buildAffectedAgentsPrompt(String decision, List<PESTELAgent> allAgents) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("<think>\n");
        prompt.append("I need to analyze which global entities would be affected by this strategic decision.\n");
        prompt.append("Let me consider the interconnections and dependencies between different organizations.\n");
        prompt.append("</think>\n\n");
        
        prompt.append("STRATEGIC DECISION: ").append(decision).append("\n\n");
        
        prompt.append("GLOBAL ENTITIES IN THE SYSTEM:\n");
        for (PESTELAgent agent : allAgents) {
            prompt.append("- ").append(agent.getAgentId()).append(": ").append(agent.getAgentDescription()).append("\n");
        }
        
        prompt.append("\nANALYSIS QUESTION: Which entities would be significantly affected by this decision?\n\n");
        prompt.append("Consider:\n");
        prompt.append("- Direct business relationships and partnerships\n");
        prompt.append("- Industry sector connections and supply chains\n");
        prompt.append("- Geographic and regional impacts\n");
        prompt.append("- Regulatory and policy implications\n");
        prompt.append("- Technology and innovation spillovers\n\n");
        
        prompt.append("Respond with a comma-separated list of entity names.\n");
        prompt.append("Example: Walmart, United States, MIT\n");
        prompt.append("If no entities are significantly affected, respond with: NONE\n");
        
        return prompt.toString();
    }
    
    private String buildStrategicAnalysisPrompt(PESTELState globalPESTEL, List<AgentAction> recentActions) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("<think>\n");
        prompt.append("I need to provide a comprehensive strategic analysis of the current global situation.\n");
        prompt.append("Let me analyze the PESTEL factors, recent actions, and emerging patterns.\n");
        prompt.append("</think>\n\n");
        
        prompt.append("STRATEGIC ANALYSIS REQUEST\n\n");
        prompt.append("Current Global PESTEL State:\n");
        prompt.append(globalPESTEL.toString()).append("\n\n");
        
        if (!recentActions.isEmpty()) {
            prompt.append("Recent Strategic Actions:\n");
            for (AgentAction action : recentActions.subList(Math.max(0, recentActions.size() - 20), recentActions.size())) {
                prompt.append("- Day ").append(action.getDay()).append(": ")
                       .append(action.getAgentId()).append(" - ")
                       .append(action.getActionDescription()).append("\n");
            }
            prompt.append("\n");
        }
        
        prompt.append("Please provide a comprehensive strategic analysis including:\n");
        prompt.append("1. Key trends and patterns emerging from recent actions\n");
        prompt.append("2. Critical PESTEL factor changes and their implications\n");
        prompt.append("3. Strategic recommendations for educational institutions\n");
        prompt.append("4. Risk assessment and opportunity identification\n");
        prompt.append("5. Future scenario likelihood assessment\n");
        
        return prompt.toString();
    }
    
    private CompletableFuture<String> generateContent(String prompt, String requestType) {
        String cacheKey = requestType + "_" + prompt.hashCode();
        if (requestCache.containsKey(cacheKey)) {
            return CompletableFuture.completedFuture((String) requestCache.get(cacheKey));
        }
        
        return CompletableFuture.supplyAsync(() -> {
            for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
                try {
                    String response = makeHuggingFaceRequest(prompt);
                    requestCache.put(cacheKey, response);
                    return response;
                } catch (Exception e) {
                    simu.framework.Trace.out(simu.framework.Trace.Level.WAR, 
                        "Qwen3-Next API request failed (attempt " + attempt + "/" + MAX_RETRIES + "): " + e.getMessage());
                    
                    if (attempt == MAX_RETRIES) {
                        return generateAdvancedFallbackResponse(requestType, prompt);
                    }
                    
                    try {
                        TimeUnit.SECONDS.sleep(attempt * 3);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return generateAdvancedFallbackResponse(requestType, prompt);
                    }
                }
            }
            return generateAdvancedFallbackResponse(requestType, prompt);
        });
    }
    
    private String makeHuggingFaceRequest(String prompt) throws IOException, InterruptedException {
        if (apiKey.isEmpty()) {
            throw new IllegalStateException("Hugging Face API key not provided");
        }
        
        String requestBody = buildHuggingFaceRequestBody(prompt);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HUGGINGFACE_API_URL))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .timeout(REQUEST_TIMEOUT)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        
        HttpResponse<String> response = httpClient.send(request, 
                HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new IOException("Qwen3-Next API request failed with status: " + response.statusCode() + 
                                ", body: " + response.body());
        }
        
        return extractTextFromHuggingFaceResponse(response.body());
    }
    
    private String buildHuggingFaceRequestBody(String prompt) {
        return String.format("""
            {
                "inputs": "%s",
                "parameters": {
                    "max_new_tokens": 2048,
                    "temperature": 0.6,
                    "top_p": 0.95,
                    "top_k": 20,
                    "do_sample": true,
                    "return_full_text": false
                },
                "options": {
                    "wait_for_model": true,
                    "use_cache": false
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
    
    private String extractTextFromHuggingFaceResponse(String jsonResponse) {
        try {
            // Hugging Face returns array format: [{"generated_text": "..."}]
            int textStart = jsonResponse.indexOf("\"generated_text\":");
            if (textStart != -1) {
                textStart = jsonResponse.indexOf("\"", textStart + 17) + 1;
                int textEnd = jsonResponse.lastIndexOf("\"");
                
                String extractedText = jsonResponse.substring(textStart, textEnd)
                    .replace("\\n", "\n")
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\");
                
                // Remove thinking tags if present
                if (extractedText.contains("</think>")) {
                    int thinkEnd = extractedText.indexOf("</think>") + 8;
                    if (thinkEnd < extractedText.length()) {
                        extractedText = extractedText.substring(thinkEnd).trim();
                    }
                }
                
                return extractedText;
            }
        } catch (Exception e) {
            simu.framework.Trace.out(simu.framework.Trace.Level.WAR, 
                "Error parsing Qwen3-Next response: " + e.getMessage());
        }
        return jsonResponse;
    }
    
    private String parseAgentDecisionResponse(String response) {
        String cleaned = response.trim();
        String upperCleaned = cleaned.toUpperCase();
        
        if (upperCleaned.equals("NO") || upperCleaned.contains("NO ACTION") || 
            upperCleaned.contains("NO_ACTION") || upperCleaned.contains("TAKE NO ACTION")) {
            return "no_action";
        }
        
        // Clean up any remaining thinking content
        if (cleaned.startsWith("<think>")) {
            int thinkEnd = cleaned.indexOf("</think>");
            if (thinkEnd != -1) {
                cleaned = cleaned.substring(thinkEnd + 8).trim();
            }
        }
        
        return cleaned;
    }
    
    private String parsePESTELImpactResponse(String response, String category) {
        if (response.trim().toUpperCase().equals("NO_IMPACT")) {
            return "NO_IMPACT";
        }
        
        // Parse structured response from Qwen3-Next
        Map<String, String> parsed = new HashMap<>();
        String[] lines = response.split("\n");
        
        for (String line : lines) {
            line = line.trim();
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
                parsed.get("factor"), parsed.get("new_value"), parsed.getOrDefault("reason", "Strategic impact"));
        }
        
        return "NO_IMPACT";
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
    
    private String generateAdvancedFallbackResponse(String requestType, String prompt) {
        switch (requestType) {
            case "agent_decision":
                return generateIntelligentFallbackDecision(prompt);
            case "pestel_impact":
                return generateIntelligentFallbackImpact(prompt);
            case "affected_agents":
                return "NONE";
            case "strategic_analysis":
                return "Strategic analysis temporarily unavailable - using pattern-based insights";
            default:
                return "Qwen3-Next AI service temporarily unavailable";
        }
    }
    
    private String generateIntelligentFallbackDecision(String prompt) {
        // Enhanced fallback logic based on prompt analysis
        String promptLower = prompt.toLowerCase();
        
        if (promptLower.contains("apple") || promptLower.contains("technology")) {
            if (Math.random() < 0.4) {
                return "Invest in next-generation AI chip development and sustainable manufacturing";
            }
        } else if (promptLower.contains("walmart") || promptLower.contains("retail")) {
            if (Math.random() < 0.3) {
                return "Expand sustainable supply chain and renewable energy initiatives";
            }
        } else if (promptLower.contains("amazon") || promptLower.contains("cloud")) {
            if (Math.random() < 0.35) {
                return "Launch advanced AI services and expand global cloud infrastructure";
            }
        } else if (promptLower.contains("united states") || promptLower.contains("china")) {
            if (Math.random() < 0.4) {
                return "Strengthen international cooperation and technology partnerships";
            }
        } else if (promptLower.contains("mit") || promptLower.contains("stanford")) {
            if (Math.random() < 0.45) {
                return "Launch breakthrough research initiative with industry collaboration";
            }
        }
        
        return Math.random() < 0.25 ? "Take strategic action based on current market conditions" : "no_action";
    }
    
    private String generateIntelligentFallbackImpact(String prompt) {
        String promptLower = prompt.toLowerCase();
        
        if (promptLower.contains("invest") && promptLower.contains("economic")) {
            return "FACTOR:investment_growth|NEW_VALUE:Increased investment activity stimulating economic development|REASON:Major investment initiative";
        } else if (promptLower.contains("ai") && promptLower.contains("technological")) {
            return "FACTOR:ai_advancement|NEW_VALUE:Accelerated AI development and innovation capabilities|REASON:AI research and development focus";
        } else if (promptLower.contains("climate") && promptLower.contains("environmental")) {
            return "FACTOR:climate_action|NEW_VALUE:Enhanced climate action and sustainability initiatives|REASON:Environmental leadership and policy";
        } else if (promptLower.contains("cooperation") && promptLower.contains("political")) {
            return "FACTOR:international_cooperation|NEW_VALUE:Strengthened international partnerships and diplomatic relations|REASON:Diplomatic initiative";
        }
        
        return "NO_IMPACT";
    }
    
    public void clearCache() {
        requestCache.clear();
    }
    
    public int getCacheSize() {
        return requestCache.size();
    }
    
    public boolean isAvailable() {
        return !apiKey.isEmpty();
    }
}
