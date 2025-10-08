import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class NetworkTest {
    public static void main(String[] args) {
        System.out.println("🌐 Testing Network Connectivity to Metropolia Database");
        System.out.println("======================================================");
        
        String host = "mysql.metropolia.fi";
        int port = 3306;
        int timeout = 5000; // 5 seconds
        
        System.out.println("🎯 Target: " + host + ":" + port);
        System.out.println("⏱️ Timeout: " + timeout + "ms");
        
        try (Socket socket = new Socket()) {
            System.out.println("\n🔌 Attempting connection...");
            socket.connect(new InetSocketAddress(host, port), timeout);
            
            System.out.println("✅ SUCCESS: Network connection established!");
            System.out.println("📊 Connection Details:");
            System.out.println("   - Local Address: " + socket.getLocalAddress());
            System.out.println("   - Local Port: " + socket.getLocalPort());
            System.out.println("   - Remote Address: " + socket.getInetAddress());
            System.out.println("   - Remote Port: " + socket.getPort());
            System.out.println("   - Connected: " + socket.isConnected());
            
        } catch (SocketTimeoutException e) {
            System.out.println("❌ ERROR: Connection timed out after " + timeout + "ms");
            System.out.println("💡 Possible causes:");
            System.out.println("   - Firewall blocking port 3306");
            System.out.println("   - VPN required for Metropolia network");
            System.out.println("   - Network connectivity issues");
            System.out.println("   - Server may be down");
            
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            
            if (e.getMessage().contains("No route to host")) {
                System.out.println("💡 TIP: Check if you're connected to Metropolia VPN");
            } else if (e.getMessage().contains("Connection refused")) {
                System.out.println("💡 TIP: Server may be down or port 3306 blocked");
            }
        }
        
        System.out.println("\n🔧 Troubleshooting Steps:");
        System.out.println("1. Check if you're on Metropolia network or VPN");
        System.out.println("2. Try: telnet mysql.metropolia.fi 3306");
        System.out.println("3. Check Windows Firewall settings");
        System.out.println("4. Contact Metropolia IT support if issue persists");
    }
}


