import java.io.*;
import java.net.*;

public class test_ai_connection {
    public static void main(String[] args) {
        System.out.println("Testing AI server connection...");
        
        try {
            URL url = new URL("http://localhost:8000/health");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String response = in.readLine();
                System.out.println("Response: " + response);
                System.out.println("✅ AI server is reachable!");
            } else {
                System.out.println("❌ Server returned: " + responseCode);
            }
            
        } catch (Exception e) {
            System.out.println("❌ Connection failed: " + e.getMessage());
        }
    }
}
