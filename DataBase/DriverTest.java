public class DriverTest {
    public static void main(String[] args) {
        System.out.println("🔧 Testing MariaDB Driver Installation");
        System.out.println("=====================================");
        
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            System.out.println("✅ MariaDB Driver loaded successfully!");
            System.out.println("📦 Driver Version: 3.5.6");
            System.out.println("🎯 Ready for database connections");
            
            // Test driver info
            var driver = new org.mariadb.jdbc.Driver();
            System.out.println("📊 Driver Details:");
            System.out.println("   - Name: " + driver.getClass().getName());
            System.out.println("   - Major Version: " + driver.getMajorVersion());
            System.out.println("   - Minor Version: " + driver.getMinorVersion());
            
        } catch (ClassNotFoundException e) {
            System.out.println("❌ ERROR: MariaDB driver not found!");
            System.out.println("Make sure mariadb-java-client-3.5.6.jar is in lib/ directory");
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
        }
    }
}


