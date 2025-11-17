import Views.LoginFrame;
import Database.DatabaseConnection;
import config.AppConfig;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Public Utility Billing System - Main Driver Class
 * Entry point for the application
 *
 * @author CCINFOM-DBAPP-S23-02 Team
 * @version 1.0.0
 * @since 2025-11-17
 */
public class PublicUtilityBillingSystem {

    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        printStartupBanner();
        setSystemLookAndFeel();
        testDatabaseConnection();

        SwingUtilities.invokeLater(() -> {
            try {
                initializeApplication();
            } catch (Exception e) {
                handleStartupError(e);
            }
        });
    }

    private static void printStartupBanner() {
        System.out.println("==========================================================");
        System.out.println("    " + AppConfig.APP_SHORT_NAME.toUpperCase());
        System.out.println("==========================================================");
        System.out.println("Name         : " + AppConfig.APP_NAME);
        System.out.println("Version      : " + AppConfig.APP_VERSION);
        System.out.println("Organization : " + AppConfig.APP_ORGANIZATION);
        System.out.println("Database     : " + AppConfig.DB_URL.split("/")[3]);
        System.out.println("Java Version : " + System.getProperty("java.version"));
        System.out.println("OS           : " + System.getProperty("os.name"));
        System.out.println("Start Time   : " + LocalDateTime.now().format(DATE_FORMATTER) + " UTC");
        System.out.println("User         : " + System.getProperty("user.name"));
        System.out.println("==========================================================");
        System.out.println();
        System.out.println("Initializing application...");
    }

    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
            System.out.println("✓ System Look and Feel applied successfully");
        } catch (Exception e) {
            System.err.println("⚠ Could not set Look and Feel: " + e.getMessage());
        }
    }

    private static void testDatabaseConnection() {
        System.out.println("Testing database connection...");
        boolean connected = DatabaseConnection.testConnection();

        if (connected) {
            System.out.println("✓ Database connection successful");
        } else {
            System.err.println("✗ Database connection failed!");
            System.err.println("  Please check:");
            System.err.println("  - MySQL server is running");
            System.err.println("  - Database: public_utility_billing_system exists");
            System.err.println("  - Username: " + AppConfig.DB_USERNAME);
            System.err.println("  - Password is correct");
        }
    }

    private static void initializeApplication() {
        System.out.println("✓ Creating login window...");

        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);

        System.out.println("✓ Application started successfully!");
        System.out.println("✓ Login window displayed");
        System.out.println();
        System.out.println("Waiting for user authentication...");
        System.out.println("==========================================================");
    }

    private static void handleStartupError(Exception e) {
        System.err.println();
        System.err.println("==========================================================");
        System.err.println("✗ FATAL ERROR: Failed to start application");
        System.err.println("==========================================================");
        System.err.println("Error: " + e.getMessage());
        e.printStackTrace();
        System.err.println("==========================================================");

        JOptionPane.showMessageDialog(
                null,
                "Failed to start " + AppConfig.APP_NAME + "\n\n" +
                        "Error: " + e.getMessage() + "\n\n" +
                        "Please check the console for details.",
                "Startup Error",
                JOptionPane.ERROR_MESSAGE
        );

        System.exit(1);
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println();
            System.out.println("==========================================================");
            System.out.println("Application shutting down...");
            System.out.println("End Time   : " + LocalDateTime.now().format(DATE_FORMATTER) + " UTC");
            System.out.println("==========================================================");
        }));
    }
}