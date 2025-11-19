package Database;

import config.AppConfig; 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database Connection Manager
 * Handles MySQL database connections using config.AppConfig
 */
public class DatabaseConnection {

    private static String url;
    private static String username;
    private static String password;

    static {
        // Use config.AppConfig for centralized configuration
        url = AppConfig.DB_URL;
        username = config.AppConfig.DB_USERNAME;
        password = config.AppConfig.DB_PASSWORD;

        // Load driver explicitly
        try {
            Class.forName(AppConfig.DB_DRIVER);
            System.out.println("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            System.err.println("  Make sure mysql-connector-java is in classpath");
        }
    }

    /**
     * Get database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Test database connection
     * @return true if connection successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get connection info (for debugging)
     * @return Connection info string
     */
    public static String getConnectionInfo() {
        return "URL: " + url + "\nUsername: " + username;
    }
}