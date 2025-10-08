import java.sql.Connection;
import java.sql.DriverManager;

public class SimpleConnectionTest {
    public static void main(String[] args) {
        System.out.println("🔗 Testing MariaDB Connection to Metropolia Database");
        System.out.println("==================================================");
        
        try {
            // Load MariaDB driver
            Class.forName("org.mariadb.jdbc.Driver");
            System.out.println("✅ MariaDB Driver loaded successfully");
            
            // Connection parameters
            String url = "jdbc:mariadb://mysql.metropolia.fi:3306/eiakim";
            String user = "eiakim";
            String password = System.getenv("DB_PASSWORD");
            
            System.out.println("🌐 URL: " + url);
            System.out.println("👤 User: " + user);
            
            if (password == null || password.isEmpty()) {
                System.out.println("❌ ERROR: DB_PASSWORD environment variable not set!");
                System.out.println("Please set it with:");
                System.out.println("   $env:DB_PASSWORD = \"your_metropolia_password\"");
                return;
            }
            
            System.out.println("🔐 Password: [SET]");
            
            // Attempt connection
            System.out.println("\n🔌 Attempting connection...");
            Connection conn = DriverManager.getConnection(url, user, password);
            
            System.out.println("✅ SUCCESS: Database connection established!");
            System.out.println("📊 Connection Info:");
            System.out.println("   - Database: " + conn.getCatalog());
            System.out.println("   - Driver: " + conn.getMetaData().getDriverName());
            System.out.println("   - Version: " + conn.getMetaData().getDriverVersion());
            
            // Test a simple query
            System.out.println("\n🔍 Testing simple query...");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery("SELECT 1 as test_value");
            if (rs.next()) {
                System.out.println("✅ Query test successful: " + rs.getInt("test_value"));
            }
            
            // Close connection
            conn.close();
            System.out.println("\n🔌 Connection closed successfully");
            System.out.println("🎉 All tests passed!");
            
        } catch (ClassNotFoundException e) {
            System.out.println("❌ ERROR: MariaDB driver not found in classpath");
            System.out.println("Make sure mariadb-java-client-3.5.6.jar is in lib/ directory");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("❌ ERROR: Connection failed");
            System.out.println("Error: " + e.getMessage());
            
            if (e.getMessage().contains("Access denied")) {
                System.out.println("\n💡 TIP: Check your Metropolia database password");
            } else if (e.getMessage().contains("Connection refused")) {
                System.out.println("\n💡 TIP: Check your internet connection and firewall settings");
            }
        }
    }
}


