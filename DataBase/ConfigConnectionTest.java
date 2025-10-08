import java.sql.Connection;
import java.sql.DriverManager;
import java.io.FileInputStream;
import java.util.Properties;

public class ConfigConnectionTest {
    public static void main(String[] args) {
        System.out.println("🔗 Testing MariaDB Connection with Config File");
        System.out.println("===============================================");
        
        try {
            // Load MariaDB driver
            Class.forName("org.mariadb.jdbc.Driver");
            System.out.println("✅ MariaDB Driver loaded successfully");
            
            // Load config properties
            Properties config = new Properties();
            config.load(new FileInputStream("config.properties"));
            
            String url = config.getProperty("db.url");
            String user = config.getProperty("db.user");
            String password = config.getProperty("db.password");
            
            System.out.println("📁 Config loaded from: config.properties");
            System.out.println("🌐 URL: " + url);
            System.out.println("👤 User: " + user);
            System.out.println("🔐 Password: [LOADED FROM CONFIG]");
            
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
            var rs = stmt.executeQuery("SELECT 1 as test_value, NOW() as server_time");
            if (rs.next()) {
                System.out.println("✅ Query test successful:");
                System.out.println("   - Test Value: " + rs.getInt("test_value"));
                System.out.println("   - Server Time: " + rs.getTimestamp("server_time"));
            }
            
            // Test if our table exists
            System.out.println("\n🔍 Checking if 'texts' table exists...");
            var rs2 = stmt.executeQuery("SHOW TABLES LIKE 'texts'");
            if (rs2.next()) {
                System.out.println("✅ 'texts' table exists in database");
            } else {
                System.out.println("⚠️ 'texts' table does not exist - you may need to create it");
            }
            
            // Close connection
            conn.close();
            System.out.println("\n🔌 Connection closed successfully");
            System.out.println("🎉 All tests passed! Database is ready for use.");
            
        } catch (ClassNotFoundException e) {
            System.out.println("❌ ERROR: MariaDB driver not found in classpath");
            System.out.println("Make sure mariadb-java-client-3.5.6.jar is in lib/ directory");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            
            if (e.getMessage().contains("Access denied")) {
                System.out.println("\n💡 TIP: Check your database credentials in config.properties");
            } else if (e.getMessage().contains("Connection refused")) {
                System.out.println("\n💡 TIP: Check your internet connection and firewall settings");
            } else if (e.getMessage().contains("config.properties")) {
                System.out.println("\n💡 TIP: Make sure config.properties is in the DataBase directory");
            }
        }
    }
}
