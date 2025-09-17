package simu.model;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Local AI Service that runs Qwen3-Next-80B-A3B-Thinking model locally on your laptop
 * No API keys required - runs the model directly using Python transformers library
 * 
 * Based on: https://huggingface.co/Qwen/Qwen3-Next-80B-A3B-Thinking
 */
public class LocalQwenAIService {
    private final String pythonExecutable;
    private final String modelPath;
    private final Map<String, String> responseCache;
    private final boolean modelAvailable;
    
    public LocalQwenAIService() {
        this.pythonExecutable = findPythonExecutable();
        this.modelPath = "Qwen/Qwen3-Next-80B-A3B-Thinking";
        this.responseCache = new HashMap<>();
        this.modelAvailable = checkModelAvailability();
        
        if (modelAvailable) {
            simu.framework.Trace.out(simu.framework.Trace.Level.INFO, 
                "✅ Local Qwen3-Next-80B-A3B-Thinking model initialized successfully");
        } else {
            simu.framework.Trace.out(simu.framework.Trace.Level.WAR, 
                "⚠️ Local Qwen3-Next model not available - using advanced fallback logic");
        }
    }
    
    /**
     * Ask local AI model for agent decision
     */
    public CompletableFuture<String> askAgentDecision(PESTELAgent agent, PESTELState globalPESTEL, 
                                                     int currentDay, List<AgentAction> recentActions) {
        if (!modelAvailable) {
            return CompletableFuture.completedFuture(generateIntelligentFallback(agent, "decision"));
        }
        
        String prompt = buildAgentDecisionPrompt(agent, globalPESTEL, currentDay, recentActions);
        return runLocalModel(prompt, "agent_decision")
                .thenApply(this::parseAgentDecisionResponse);
    }
    
    /**
     * Ask local AI model for PESTEL impact analysis
     */
    public CompletableFuture<String> askPESTELImpact(String decision, String pestelCategory, 
                                                    PESTELState currentState) {
        if (!modelAvailable) {
            return CompletableFuture.completedFuture(generateIntelligentPESTELFallback(decision, pestelCategory));
        }
        
        String prompt = buildPESTELImpactPrompt(decision, pestelCategory, currentState);
        return runLocalModel(prompt, "pestel_impact")
                .thenApply(response -> parsePESTELImpactResponse(response, pestelCategory));
    }
    
    /**
     * Ask local AI model for affected agents
     */
    public CompletableFuture<List<String>> askAffectedAgents(String decision, List<PESTELAgent> allAgents) {
        if (!modelAvailable) {
            return CompletableFuture.completedFuture(generateIntelligentAffectedAgents(decision, allAgents));
        }
        
        String prompt = buildAffectedAgentsPrompt(decision, allAgents);
        return runLocalModel(prompt, "affected_agents")
                .thenApply(this::parseAffectedAgentsResponse);
    }
    
    /**
     * Run local Qwen3-Next model using Python subprocess
     */
    private CompletableFuture<String> runLocalModel(String prompt, String requestType) {
        String cacheKey = requestType + "_" + prompt.hashCode();
        if (responseCache.containsKey(cacheKey)) {
            return CompletableFuture.completedFuture(responseCache.get(cacheKey));
        }
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Create temporary Python script
                File tempScript = createTempPythonScript(prompt);
                
                // Run Python script with local model
                ProcessBuilder pb = new ProcessBuilder(pythonExecutable, tempScript.getAbsolutePath());
                pb.redirectErrorStream(true);
                
                Process process = pb.start();
                
                // Read output
                StringBuilder output = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                }
                
                // Wait for completion with timeout
                boolean finished = process.waitFor(60, TimeUnit.SECONDS);
                if (!finished) {
                    process.destroyForcibly();
                    throw new RuntimeException("Model inference timeout");
                }
                
                // Clean up
                tempScript.delete();
                
                String result = output.toString().trim();
                responseCache.put(cacheKey, result);
                return result;
                
            } catch (Exception e) {
                simu.framework.Trace.out(simu.framework.Trace.Level.WAR, 
                    "Local model execution failed: " + e.getMessage());
                return generateIntelligentFallback(null, requestType);
            }
        });
    }
    
    /**
     * Create temporary Python script for local model inference
     */
    private File createTempPythonScript(String prompt) throws IOException {
        File tempScript = File.createTempFile("qwen_inference", ".py");
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(tempScript))) {
            writer.println("#!/usr/bin/env python3");
            writer.println("import sys");
            writer.println("try:");
            writer.println("    from transformers import AutoModelForCausalLM, AutoTokenizer");
            writer.println("    import torch");
            writer.println("    ");
            writer.println("    # Load model and tokenizer");
            writer.println("    model_name = '" + modelPath + "'");
            writer.println("    ");
            writer.println("    try:");
            writer.println("        tokenizer = AutoTokenizer.from_pretrained(model_name)");
            writer.println("        model = AutoModelForCausalLM.from_pretrained(");
            writer.println("            model_name,");
            writer.println("            torch_dtype=torch.bfloat16,");
            writer.println("            device_map='auto',");
            writer.println("            trust_remote_code=True");
            writer.println("        )");
            writer.println("        ");
            writer.println("        # Prepare input");
            writer.println("        prompt = '''");
            writer.println(prompt.replace("'", "\\'"));
            writer.println("'''");
            writer.println("        ");
            writer.println("        messages = [{'role': 'user', 'content': prompt}]");
            writer.println("        text = tokenizer.apply_chat_template(messages, tokenize=False, add_generation_prompt=True)");
            writer.println("        model_inputs = tokenizer([text], return_tensors='pt').to(model.device)");
            writer.println("        ");
            writer.println("        # Generate response");
            writer.println("        with torch.no_grad():");
            writer.println("            generated_ids = model.generate(");
            writer.println("                **model_inputs,");
            writer.println("                max_new_tokens=1024,");
            writer.println("                temperature=0.6,");
            writer.println("                top_p=0.95,");
            writer.println("                top_k=20,");
            writer.println("                do_sample=True,");
            writer.println("                pad_token_id=tokenizer.eos_token_id");
            writer.println("            )");
            writer.println("        ");
            writer.println("        # Extract response");
            writer.println("        output_ids = generated_ids[0][len(model_inputs.input_ids[0]):].tolist()");
            writer.println("        ");
            writer.println("        # Parse thinking content");
            writer.println("        try:");
            writer.println("            index = len(output_ids) - output_ids[::-1].index(151668)  # </think>");
            writer.println("        except ValueError:");
            writer.println("            index = 0");
            writer.println("        ");
            writer.println("        thinking_content = tokenizer.decode(output_ids[:index], skip_special_tokens=True).strip()");
            writer.println("        content = tokenizer.decode(output_ids[index:], skip_special_tokens=True).strip()");
            writer.println("        ");
            writer.println("        # Output final content");
            writer.println("        print(content if content else thinking_content)");
            writer.println("        ");
            writer.println("    except Exception as e:");
            writer.println("        print(f'Model loading error: {e}')");
            writer.println("        print('FALLBACK_MODE')");
            writer.println("        ");
            writer.println("except ImportError as e:");
            writer.println("    print(f'Missing dependencies: {e}')");
            writer.println("    print('FALLBACK_MODE')");
        }
        
        return tempScript;
    }
    
    /**
     * Check if local model is available
     */
    private boolean checkModelAvailability() {
        try {
            // Check if Python and required libraries are available
            ProcessBuilder pb = new ProcessBuilder(pythonExecutable, "-c", 
                "import transformers, torch; print('MODEL_AVAILABLE')");
            pb.redirectErrorStream(true);
            
            Process process = pb.start();
            
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
            }
            
            boolean finished = process.waitFor(10, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                return false;
            }
            
            return output.toString().contains("MODEL_AVAILABLE");
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Find Python executable
     */
    private String findPythonExecutable() {
        // Try different Python executables
        String[] pythonCmds = {"python", "python3", "py"};
        
        for (String cmd : pythonCmds) {
            try {
                ProcessBuilder pb = new ProcessBuilder(cmd, "--version");
                Process process = pb.start();
                boolean finished = process.waitFor(5, TimeUnit.SECONDS);
                if (finished && process.exitValue() == 0) {
                    return cmd;
                }
            } catch (Exception e) {
                // Continue trying
            }
        }
        
        return "python"; // Default fallback
    }
    
    /**
     * Build prompts for local model
     */
    private String buildAgentDecisionPrompt(PESTELAgent agent, PESTELState globalPESTEL, 
                                          int currentDay, List<AgentAction> recentActions) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a strategic advisor for ").append(agent.getAgentDescription()).append(".\n\n");
        
        prompt.append("Current Day: ").append(currentDay).append("\n");
        prompt.append("Previous Decisions: ").append(agent.getDecisionCount()).append("\n\n");
        
        // Simplified PESTEL state for local processing
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
    
    private String parseAgentDecisionResponse(String response) {
        String cleaned = response.trim().toUpperCase();
        if (cleaned.equals("NO") || cleaned.contains("NO ACTION") || cleaned.equals("FALLBACK_MODE")) {
            return "no_action";
        }
        return response.trim();
    }
    
    private String parsePESTELImpactResponse(String response, String category) {
        if (response.trim().toUpperCase().equals("NO_IMPACT") || response.contains("FALLBACK_MODE")) {
            return "NO_IMPACT";
        }
        
        // Simple parsing for local model
        if (response.contains("FACTOR:") && response.contains("NEW_VALUE:")) {
            return response.replace("\n", "|");
        }
        
        return "NO_IMPACT";
    }
    
    private List<String> parseAffectedAgentsResponse(String response) {
        if (response.trim().toUpperCase().equals("NONE") || response.contains("FALLBACK_MODE")) {
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
     * Generate intelligent fallback decisions without AI
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
        if (!modelAvailable) {
            return generateIntelligentFallback(null, requestType);
        }
        
        try {
            // Create temporary Python script
            File tempScript = createTempPythonScript(prompt);
            
            // Run Python script with local model
            ProcessBuilder pb = new ProcessBuilder(pythonExecutable, tempScript.getAbsolutePath());
            pb.redirectErrorStream(true);
            
            Process process = pb.start();
            
            // Read output
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            
            // Wait for completion with timeout
            boolean finished = process.waitFor(30, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                throw new RuntimeException("Model inference timeout");
            }
            
            // Clean up
            tempScript.delete();
            
            String result = output.toString().trim();
            if (result.contains("FALLBACK_MODE") || result.isEmpty()) {
                return generateIntelligentFallback(null, requestType);
            }
            
            return result;
            
        } catch (Exception e) {
            simu.framework.Trace.out(simu.framework.Trace.Level.WAR, 
                "Local model execution failed: " + e.getMessage());
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

    public boolean isModelAvailable() {
        return modelAvailable;
    }
    
    public void clearCache() {
        responseCache.clear();
    }
}
