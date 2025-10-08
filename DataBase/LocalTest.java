import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class LocalTest {
    public static void main(String[] args) {
        System.out.println("🏠 Testing Local Database Connection");
        System.out.println("====================================");
        
        // Try different local database options
        String[] testConfigs = {
            "jdbc:mariadb://localhost:3306/test",
            "jdbc:mysql://localhost:3306/test", 
            "jdbc:h2:mem:testdb"
        };
        
        for (String url : testConfigs) {
            System.out.println("\n🔍 Testing: " + url);
            testConnection(url);
        }
        
        System.out.println("\n💡 RECOMMENDATION:");
        System.out.println("Since Metropolia database is not accessible:");
        System.out.println("1. Install local MariaDB/MySQL");
        System.out.println("2. Use H2 in-memory database for testing");
        System.out.println("3. Connect to Metropolia VPN and try again");
    }
    
    private static void testConnection(String url) {
        try {
            // Try MariaDB driver first
            try {
                Class.forName("org.mariadb.jdbc.Driver");
                Connection conn = DriverManager.getConnection(url, "root", "");
                System.out.println("✅ SUCCESS with MariaDB driver!");
                conn.close();
                return;
            } catch (Exception e) {
                // Try MySQL driver
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection conn = DriverManager.getConnection(url, "root", "");
                    System.out.println("✅ SUCCESS with MySQL driver!");
                    conn.close();
                    return;
                } catch (Exception e2) {
                    // Try H2 driver
                    try {
                        Class.forName("org.h2.Driver");
                        Connection conn = DriverManager.getConnection(url, "sa", "");
                        System.out.println("✅ SUCCESS with H2 driver!");
                        conn.close();
                        return;
                    } catch (Exception e3) {
                        System.out.println("❌ Failed: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Failed: " + e.getMessage());
        }
    }
}


