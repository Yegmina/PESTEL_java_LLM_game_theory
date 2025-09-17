import simu.framework.Trace;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class test_simple_ai {
    public static void main(String[] args) {
        Trace.setTraceLevel(Trace.Level.INFO);
        
        System.out.println("Testing direct HTTP request...");
        
        try {
            URL url = new URL("http://localhost:8000/generate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(60000);

            // Simple short prompt
            String jsonInputString = "{\"prompt\": \"Apple AI investment?\", \"max_tokens\": 10}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            
            if (responseCode == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Response: " + response.toString());
                }
            } else {
                System.out.println("❌ Request failed with status: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
