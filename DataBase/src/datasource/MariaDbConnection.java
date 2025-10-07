package datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Properties;

public class MariaDbConnection {

    private static Connection conn = null;
    private static Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try {
            // Try multiple locations for config.properties
            String[] possiblePaths = {
                "config.properties",
                "../config.properties", 
                "./config.properties",
                "src/config.properties"
            };
            
            boolean loaded = false;
            for (String path : possiblePaths) {
                try (InputStream input = new FileInputStream(path)) {
                    properties.load(input);
                    System.out.println("Loaded config from: " + path);
                    loaded = true;
                    break;
                } catch (Exception e) {
                    // Try next path
                }
            }
            
            if (!loaded) {
                // Try as resource
                try (InputStream input = MariaDbConnection.class.getClassLoader()
                        .getResourceAsStream("config.properties")) {
                    if (input != null) {
                        properties.load(input);
                        System.out.println("Loaded config from classpath resources");
                        loaded = true;
                    }
                } catch (Exception e) {
                    // Ignore
                }
            }
            
            if (!loaded) {
                System.out.println("Warning: No config.properties found, using environment variables only");
            }
            
        } catch (Exception e) {
            System.out.println("Error loading properties: " + e.getMessage());
        }
    }

    private static String getProperty(String key, String defaultValue) {
        // First check environment variables
        String envValue = System.getenv(key.toUpperCase().replace('.', '_'));
        if (envValue != null) {
            return envValue;
        }
        // Then check properties file
        return properties.getProperty(key, defaultValue);
    }

    public static Connection getConnection() {
        if (conn == null) {
            try {
                String url = getProperty("db.url", "mysql.metropolia.fi:3306/eiakim");
                String user = getProperty("db.user", "eiakim");
                String password = getProperty("db.password", null);
                
                if (password == null) {
                    throw new RuntimeException("Database password not found. Set DB_PASSWORD environment variable or create config.properties");
                }
                
                // Build connection string - remove any existing jdbc:mariadb:// prefix
                String cleanUrl = url.replace("jdbc:mariadb://", "");
                String connectionString = "jdbc:mariadb://" + cleanUrl + "?user=" + user + "&password=" + password;
                
                System.out.println("Connecting to: " + connectionString);
                conn = DriverManager.getConnection(connectionString);
                System.out.println("Database connection successful!");
            } catch (SQLException e) {
                System.out.println("Connection failed.");
                e.printStackTrace();
            }
            return conn;
        } else {
            return conn;
        }
    }

    public static void terminate() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
