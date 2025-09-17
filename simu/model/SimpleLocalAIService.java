package simu.model;

import simu.framework.Trace;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Simple Local AI Service that connects to qwen_server.py
 * Much more reliable than trying to load 80B model in Java
 */
public class SimpleLocalAIService {
    private static final String LOCAL_SERVER_URL = "http://localhost:8000";
    private boolean serverAvailable = false;

    public SimpleLocalAIService() {
        checkServerAvailability();
    }

    private void checkServerAvailability() {
        try {
            URL url = new URL(LOCAL_SERVER_URL + "/health");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(2000); // 2 second timeout
            conn.setReadTimeout(2000);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                serverAvailable = true;
                Trace.out(Trace.Level.INFO, "✅ Local Qwen server is running at " + LOCAL_SERVER_URL);
            } else {
                Trace.out(Trace.Level.WAR, "❌ Local Qwen server not reachable. Status: " + responseCode);
            }
            conn.disconnect();
        } catch (IOException e) {
            Trace.out(Trace.Level.WAR, "❌ Local Qwen server not running: " + e.getMessage());
            Trace.out(Trace.Level.INFO, "💡 To start server: python qwen_server.py --model Qwen/Qwen2.5-7B-Instruct");
        }
    }

    public boolean isModelAvailable() {
        return serverAvailable;
    }

    public String analyzeDecision(String prompt, String requestType) {
        if (!serverAvailable) {
            return "NO_ACTION"; // Fallback
        }

        try {
            URL url = new URL(LOCAL_SERVER_URL + "/generate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000); // 10 second timeout
            conn.setReadTimeout(60000); // 60 second read timeout

            String jsonInputString = String.format(
                "{\"prompt\": \"%s\", \"max_tokens\": 150, \"temperature\": 0.7}",
                prompt.replace("\"", "\\\"").replace("\n", "\\n")
            );

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return extractResponseFromJson(response.toString());
                }
            } else {
                Trace.out(Trace.Level.WAR, "Local AI server request failed with status: " + responseCode);
                return "NO_ACTION"; // Fallback
            }
        } catch (IOException e) {
            Trace.out(Trace.Level.WAR, "Error communicating with local AI server: " + e.getMessage());
            return "NO_ACTION"; // Fallback
        }
    }

    public String generateDecisionPrompt(String agentId, int currentDay, String context, 
                                       String pestelState, String recentActions) {
        return String.format(
            "You are %s on day %d. Context: %s\nPESTEL State: %s\nRecent Actions: %s\n\n" +
            "Should you take action today? If yes, provide ONE specific strategic decision. " +
            "If no, respond 'NO_ACTION'. Format: ACTION_TYPE|DESCRIPTION|CONFIDENCE(0.0-1.0)",
            agentId, currentDay, context, pestelState, recentActions
        );
    }

    private String extractResponseFromJson(String jsonResponse) {
        try {
            // Simple JSON parsing for {"response": "..."}
            int responseStart = jsonResponse.indexOf("\"response\":\"");
            if (responseStart != -1) {
                responseStart += "\"response\":\"".length();
                int responseEnd = jsonResponse.indexOf("\"", responseStart);
                if (responseEnd != -1) {
                    return jsonResponse.substring(responseStart, responseEnd)
                        .replace("\\n", "\n")
                        .replace("\\\"", "\"")
                        .replace("\\\\", "\\");
                }
            }
        } catch (Exception e) {
            Trace.out(Trace.Level.WAR, "Error parsing AI response: " + e.getMessage());
        }
        return "NO_ACTION"; // Fallback if parsing fails
    }
}
