package simu.model;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Local AI Service that runs any model via Ollama
 * No API keys required - runs models through local Ollama instance
 */
public class LocalOllamaAIService {
    private final String ollamaUrl;
    private final String modelName;
    private final Map<String, String> responseCache;
    private final boolean ollamaAvailable;
    private final HttpClient httpClient;
    
    public LocalOllamaAIService() {
        this("http://localhost:11434", "gemma3:4b"); // Default model
    }
    
    public LocalOllamaAIService(String ollamaUrl, String modelName) {
        this.ollamaUrl = ollamaUrl;
        this.modelName = "gemma3:4b";
        this.responseCache = new ConcurrentHashMap<>();
        this.httpClient = HttpClient.newHttpClient();
        this.ollamaAvailable = checkOllamaAvailability();
        
        if (ollamaAvailable) {
            simu.framework.Trace.out(simu.framework.Trace.Level.INFO, 
                "✅ Ollama service initialized successfully with model: " + modelName);
        } else {
            simu.framework.Trace.out(simu.framework.Trace.Level.WAR, 
                "⚠️ Ollama service not available - using advanced fallback logic");
        }
    }
    
    /**
     * Ask Ollama for agent decision
     */
    public CompletableFuture<String> askAgentDecision(PESTELAgent agent, PESTELState globalPESTEL, 
                                                     int currentDay, List<AgentAction> recentActions) {
        if (!ollamaAvailable) {
            return CompletableFuture.completedFuture(generateIntelligentFallback(agent, "decision"));
        }
        
        String prompt = buildAgentDecisionPrompt(agent, globalPESTEL, currentDay, recentActions);
        return askOllama(prompt, "agent_decision")
                .thenApply(this::parseAgentDecisionResponse);
    }
    
    /**
     * Ask Ollama for PESTEL impact analysis
     */
    public CompletableFuture<String> askPESTELImpact(String decision, String pestelCategory, 
                                                    PESTELState currentState) {
        if (!ollamaAvailable) {
            return CompletableFuture.completedFuture(generateIntelligentPESTELFallback(decision, pestelCategory));
        }
        
        String prompt = buildPESTELImpactPrompt(decision, pestelCategory, currentState);
        return askOllama(prompt, "pestel_impact")
                .thenApply(response -> parsePESTELImpactResponse(response, pestelCategory));
    }
    
    /**
     * Ask Ollama for affected agents
     */
    public CompletableFuture<List<String>> askAffectedAgents(String decision, List<PESTELAgent> allAgents) {
        if (!ollamaAvailable) {
            return CompletableFuture.completedFuture(generateIntelligentAffectedAgents(decision, allAgents));
        }
        
        String prompt = buildAffectedAgentsPrompt(decision, allAgents);
        return askOllama(prompt, "affected_agents")
                .thenApply(this::parseAffectedAgentsResponse);
    }
    
    /**
     * Main method to ask Ollama with any prompt
     */
    public CompletableFuture<String> askOllama(String prompt, String requestType) {
        return askOllama(prompt, requestType, false);
    }
    
    /**
     * Main method to ask Ollama with streaming option
     */
    public CompletableFuture<String> askOllama(String prompt, String requestType, boolean stream) {
        String cacheKey = requestType + "_" + prompt.hashCode();
        if (responseCache.containsKey(cacheKey)) {
            return CompletableFuture.completedFuture(responseCache.get(cacheKey));
        }
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                String json = String.format("""
                {
                  "model": "%s",
                  "prompt": "%s",
                  "stream": %s,
                  "options": {
                    "temperature": 0.6,
                    "top_p": 0.95,
                    "top_k": 20
                  }
                }
                """, modelName, escapeJson(prompt), stream);
                
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ollamaUrl + "/api/generate"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
                
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                
                if (response.statusCode() != 200) {
                    throw new RuntimeException("Ollama API returned status: " + response.statusCode());
                }
                
                String result = extractResponseFromJson(response.body());
                responseCache.put(cacheKey, result);
                return result;
                
            } catch (Exception e) {
                simu.framework.Trace.out(simu.framework.Trace.Level.WAR, 
                    "Ollama request failed: " + e.getMessage());
                return generateIntelligentFallback(null, requestType);
            }
        });
    }
    
    /**
     * Extract response text from Ollama JSON response
     */
    private String extractResponseFromJson(String jsonResponse) {
        try {
            // Simple JSON parsing - look for "response" field
            int responseIndex = jsonResponse.indexOf("\"response\":\"");
            if (responseIndex == -1) {
                return jsonResponse; // Return raw response if format unexpected
            }
            
            int start = responseIndex + 12; // After "\"response\":\""
            int end = jsonResponse.indexOf("\"", start);
            
            if (end > start) {
                String response = jsonResponse.substring(start, end);
                // Unescape JSON characters
                return response.replace("\\n", "\n")
                              .replace("\\\"", "\"")
                              .replace("\\\\", "\\");
            }
            
            return jsonResponse;
        } catch (Exception e) {
            return jsonResponse; // Fallback to raw response
        }
    }
    
    /**
     * Escape JSON string for prompt
     */
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    /**
     * Check if Ollama is available
     */
    private boolean checkOllamaAvailability() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ollamaUrl + "/api/tags"))
                .header("Content-Type", "application/json")
                .GET()
                .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
            
        } catch (Exception e) {
            simu.framework.Trace.out(simu.framework.Trace.Level.WAR, 
                "Ollama availability check failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Change model at runtime
     */
    public boolean setModel(String newModelName) {
        // Verify the model exists by checking tags
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ollamaUrl + "/api/tags"))
                .header("Content-Type", "application/json")
                .GET()
                .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200 && response.body().contains(newModelName)) {
//                this.modelName = "gemma3:4b";
                clearCache(); // Clear cache since model changed
                simu.framework.Trace.out(simu.framework.Trace.Level.INFO, 
                    "✅ Switched to model: " + newModelName);
                return true;
            }
        } catch (Exception e) {
            simu.framework.Trace.out(simu.framework.Trace.Level.WAR, 
                "Failed to switch model: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get available models from Ollama
     */
    public List<String> getAvailableModels() {
        List<String> models = new ArrayList<>();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ollamaUrl + "/api/tags"))
                .header("Content-Type", "application/json")
                .GET()
                .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                // Simple parsing of models list
                String body = response.body();
                String[] lines = body.split("\"name\":\"");
                for (int i = 1; i < lines.length; i++) {
                    String modelLine = lines[i];
                    int endIndex = modelLine.indexOf("\"");
                    if (endIndex > 0) {
                        models.add(modelLine.substring(0, endIndex));
                    }
                }
            }
        } catch (Exception e) {
            simu.framework.Trace.out(simu.framework.Trace.Level.WAR, 
                "Failed to get available models: " + e.getMessage());
        }
        return models;
    }
    
    /**
     * Build prompts for Ollama (same as original)
     */
    private String buildAgentDecisionPrompt(PESTELAgent agent, PESTELState globalPESTEL, 
                                          int currentDay, List<AgentAction> recentActions) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a strategic advisor for ").append(agent.getAgentDescription()).append(".\n\n");
        
        prompt.append("Current Day: ").append(currentDay).append("\n");
        prompt.append("Previous Decisions: ").append(agent.getDecisionCount()).append("\n\n");
        
        // Simplified PESTEL state for processing
        prompt.append("Key Global Factors:\n");
        prompt.append("- Economic Growth: ").append(globalPESTEL.getEconomic("growth")).append("\n");
        prompt.append("- Technology Innovation: ").append(globalPESTEL.getTechnological("innovation")).append("\n");
        prompt.append("- Political Stability: ").append(globalPESTEL.getPolitical("stability")).append("\n");
        prompt.append("- Climate Status: ").append(globalPESTEL.getEnvironmental("climate_change")).append("\n\n");
        
        if (!recentActions.isEmpty() && recentActions.size() > 0) {
            prompt.append("Recent Actions by Others:\n");
            recentActions.stream()
                .filter(a -> !a.getAgentId().equals(agent.getAgentId()))
                .limit(3)
                .forEach(action -> prompt.append("- ").append(action.getAgentId())
                    .append(": ").append(action.getActionDescription()).append("\n"));
            prompt.append("\n");
        }
        
        prompt.append("Should you take strategic action today?\n");
        prompt.append("Respond with:\n");
        prompt.append("- 'NO' if no action needed\n");
        prompt.append("- One specific strategic action if yes\n");
        
        return prompt.toString();
    }
    
    private String buildPESTELImpactPrompt(String decision, String pestelCategory, PESTELState currentState) {
        return String.format("""
            Decision: %s
            
            How does this affect %s factors?
            
            Current %s state: %s
            
            Respond in format:
            FACTOR: [factor_name]
            NEW_VALUE: [new description]
            REASON: [why changed]
            
            Or 'NO_IMPACT' if no effect.
            """, decision, pestelCategory.toUpperCase(), pestelCategory, 
            currentState.getFactor(pestelCategory, "main"));
    }
    
    private String buildAffectedAgentsPrompt(String decision, List<PESTELAgent> allAgents) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Decision: ").append(decision).append("\n\n");
        prompt.append("Available agents: ");
        allAgents.stream().limit(10).forEach(agent -> 
            prompt.append(agent.getAgentId()).append(", "));
        prompt.append("\n\nWhich agents are affected? List comma-separated or 'NONE'.\n");
        return prompt.toString();
    }
    
    /**
     * Response parsing methods (same as original)
     */
    private String parseAgentDecisionResponse(String response) {
        String cleaned = response.trim().toUpperCase();
        if (cleaned.equals("NO") || cleaned.contains("NO ACTION")) {
            return "no_action";
        }
        return response.trim();
    }
    
    private String parsePESTELImpactResponse(String response, String category) {
        if (response.trim().toUpperCase().equals("NO_IMPACT")) {
            return "NO_IMPACT";
        }
        
        // Simple parsing for model response
        if (response.contains("FACTOR:") && response.contains("NEW_VALUE:")) {
            return response.replace("\n", "|");
        }
        
        return "NO_IMPACT";
    }
    
    private List<String> parseAffectedAgentsResponse(String response) {
        if (response.trim().toUpperCase().equals("NONE")) {
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
    
    /**
     * Generate intelligent fallback decisions without AI (same as original)
     */
    private String generateIntelligentFallback(PESTELAgent agent, String type) {
        if (agent == null) return "no_action";
        
        String agentId = agent.getAgentId();
        
        // Real-world entity specific fallback decisions
        if (agentId.contains("Apple")) {
            return Math.random() < 0.4 ? "Launch innovative AI-powered product with sustainability features" : "no_action";
        } else if (agentId.contains("Amazon")) {
            return Math.random() < 0.35 ? "Expand AWS cloud services and sustainable delivery solutions" : "no_action";
        } else if (agentId.contains("Walmart")) {
            return Math.random() < 0.3 ? "Implement AI-powered supply chain optimization" : "no_action";
        } else if (agentId.contains("United States")) {
            return Math.random() < 0.4 ? "Launch strategic technology and infrastructure initiative" : "no_action";
        } else if (agentId.contains("China")) {
            return Math.random() < 0.35 ? "Strengthen international cooperation and innovation programs" : "no_action";
        } else if (agentId.contains("MIT")) {
            return Math.random() < 0.45 ? "Establish breakthrough research program with industry partnerships" : "no_action";
        } else if (agentId.contains("Stanford")) {
            return Math.random() < 0.4 ? "Launch interdisciplinary research initiative" : "no_action";
        }
        
        return Math.random() < 0.25 ? "Take strategic action based on current conditions" : "no_action";
    }
    
    private String generateIntelligentPESTELFallback(String decision, String category) {
        String decisionLower = decision.toLowerCase();
        String agentType = extractAgentFromDecision(decision);
        
        switch (category.toLowerCase()) {
            case "technological":
                if (decisionLower.contains("ai") || decisionLower.contains("research") || decisionLower.contains("innovation")) {
                    return String.format("FACTOR:innovation_leadership|NEW_VALUE:%s drives technological advancement through strategic innovation|REASON:Technology leadership initiative", agentType);
                }
                break;
            case "economic":
                if (decisionLower.contains("invest") || decisionLower.contains("expand") || decisionLower.contains("launch")) {
                    return String.format("FACTOR:market_expansion|NEW_VALUE:%s stimulates economic growth through strategic investment|REASON:Market development initiative", agentType);
                }
                break;
            case "environmental":
                if (decisionLower.contains("sustainable") || decisionLower.contains("climate") || decisionLower.contains("green")) {
                    return String.format("FACTOR:sustainability_leadership|NEW_VALUE:%s advances environmental sustainability through green initiatives|REASON:Environmental commitment", agentType);
                }
                break;
            case "political":
                if (decisionLower.contains("cooperation") || decisionLower.contains("alliance") || decisionLower.contains("partnership")) {
                    return String.format("FACTOR:diplomatic_relations|NEW_VALUE:%s enhances international cooperation through strategic partnerships|REASON:Diplomatic leadership", agentType);
                }
                break;
        }
        
        return "NO_IMPACT";
    }
    
    private List<String> generateIntelligentAffectedAgents(String decision, List<PESTELAgent> allAgents) {
        List<String> affected = new ArrayList<>();
        String decisionLower = decision.toLowerCase();
        
        // Smart selection based on decision content
        if (decisionLower.contains("ai") || decisionLower.contains("technology")) {
            allAgents.stream()
                .filter(a -> a.getAgentId().contains("Apple") || a.getAgentId().contains("MIT") || a.getAgentId().contains("Stanford"))
                .limit(2)
                .forEach(a -> affected.add(a.getAgentId()));
        }
        
        if (decisionLower.contains("trade") || decisionLower.contains("economic")) {
            allAgents.stream()
                .filter(a -> a.getAgentId().contains("United States") || a.getAgentId().contains("China"))
                .limit(2)
                .forEach(a -> affected.add(a.getAgentId()));
        }
        
        return affected;
    }
    
    private String extractAgentFromDecision(String decision) {
        // Extract likely agent type from decision context
        if (decision.contains("$") && decision.contains("B")) return "Major Corporation";
        if (decision.contains("alliance") || decision.contains("cooperation")) return "Nation State";
        if (decision.contains("research") || decision.contains("university")) return "Research Institution";
        return "Strategic Entity";
    }
    
    /**
     * Analyze decision for PESTEL impact (interface method for AIEnhancedPESTELEngine)
     */
    public String analyzeDecision(String prompt, String requestType) {
        if (!ollamaAvailable) {
            return generateIntelligentFallback(null, requestType);
        }
        
        try {
            CompletableFuture<String> response = askOllama(prompt, requestType);
            return response.get(); // Wait for completion
        } catch (Exception e) {
            simu.framework.Trace.out(simu.framework.Trace.Level.WAR, 
                "Ollama analysis failed: " + e.getMessage());
            return generateIntelligentFallback(null, requestType);
        }
    }
    
    /**
     * Generate decision prompt for AI analysis
     */
    public String generateDecisionPrompt(String agentId, int currentDay, String context, 
                                       String pestelState, String recentActions) {
        return String.format(
            "You are %s on day %d. Context: %s\nPESTEL State: %s\nRecent Actions: %s\n\n" +
            "Should you take action today? If yes, provide specific strategic decision. " +
            "If no, respond 'NO_ACTION'. Format: ACTION_TYPE|DESCRIPTION|CONFIDENCE(0.0-1.0)",
            agentId, currentDay, context, pestelState, recentActions
        );
    }

    public boolean isOllamaAvailable() {
        return ollamaAvailable;
    }
    
    public String getCurrentModel() {
        return modelName;
    }
    
    public String getOllamaUrl() {
        return ollamaUrl;
    }
    
    public void clearCache() {
        responseCache.clear();
    }
}
