import simu.model.SimpleLocalAIService;

public class test_ai_service {
    public static void main(String[] args) {
        System.out.println("Testing SimpleLocalAIService...");
        
        SimpleLocalAIService aiService = new SimpleLocalAIService();
        
        System.out.println("Model Available: " + aiService.isModelAvailable());
        
        if (aiService.isModelAvailable()) {
            System.out.println("Testing AI decision...");
            String prompt = "Should Apple invest in quantum computing? Answer with ACTION_TYPE|DESCRIPTION|CONFIDENCE";
            String response = aiService.analyzeDecision(prompt, "TEST");
            System.out.println("AI Response: " + response);
        } else {
            System.out.println("❌ AI service not available");
        }
    }
}
