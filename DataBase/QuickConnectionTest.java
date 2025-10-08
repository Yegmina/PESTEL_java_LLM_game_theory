import java.sql.Connection;
import java.sql.DriverManager;
import datasource.MariaDbConnection;

public class QuickConnectionTest {
    public static void main(String[] args) {
        try {
            String url = "jdbc:mariadb://mysql.metropolia.fi:3306/eiakim";
            String user = "eiakim";
            String password = System.getenv("DB_PASSWORD");
            
            if (password == null) {
                System.out.println("❌ DB_PASSWORD environment variable not set!");
                System.out.println("Set it with: $env:DB_PASSWORD = \"your_password\"");
                return;
            }
            
            System.out.println("🔗 Testing connection to: " + url);
            System.out.println("👤 User: " + user);
            
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Database connection successful!");
            
            conn.close();
            System.out.println("🔌 Connection closed.");
            
        } catch (Exception e) {
            System.out.println("❌ Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
