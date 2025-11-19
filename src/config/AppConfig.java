package config;

/**
 * Application Configuration
 * Centralized configuration settings for the Public Utility Billing System
 */
public class AppConfig {

    // Application Info
    public static final String APP_NAME = "Public Utility Billing System";
    public static final String APP_SHORT_NAME = "PUBS";
    public static final String APP_VERSION = "1.0.0";
    public static final String APP_ORGANIZATION = "CCINFOM-DBAPP-S23-02";

    // Database Configuration
    public static final String DB_URL = "jdbc:mysql://localhost:3306/public_utility_billing_system";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "12345";
    public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    // Application Settings
    public static final int SESSION_TIMEOUT_MINUTES = 30;
    public static final int DEFAULT_PAGE_SIZE = 50;
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DISPLAY_DATE_FORMAT = "MMM dd, yyyy";

    // Report Settings
    public static final String REPORTS_OUTPUT_DIR = "reports/";
    public static final String TEMP_DIR = "temp/";
    public static final String EXPORT_CSV_EXTENSION = ".csv";
    public static final String EXPORT_PDF_EXTENSION = ".pdf";

    // Business Rules
    public static final int DEFAULT_BILL_DUE_DAYS = 21;
    public static final double OVERDUE_PENALTY_RATE = 0.02; // 2% per month
    public static final double MIN_PAYMENT_AMOUNT = 1.00;
    public static final int MAX_PARTIAL_PAYMENTS = 10;

    // UI Settings
    public static final int WINDOW_MIN_WIDTH = 1200;
    public static final int WINDOW_MIN_HEIGHT = 700;
    public static final int DIALOG_DEFAULT_WIDTH = 500;
    public static final int DIALOG_DEFAULT_HEIGHT = 400;
    public static final int TABLE_ROW_HEIGHT = 35;
    public static final int TABLE_HEADER_HEIGHT = 40;

    // System Settings
    public static final boolean DEBUG_MODE = false;
    public static final boolean AUTO_BACKUP = true;
    public static final int AUTO_BACKUP_INTERVAL_HOURS = 24;

    private AppConfig() {}
   

    /**
     * Get full application name with version
     */
    public static String getFullAppName() {
        return APP_NAME + " v" + APP_VERSION;
    }

    /**
     * Get copyright text
     */
    public static String getCopyright() {
        return "© 2025 " + APP_ORGANIZATION;
    }

    /**
     * Get about text for About dialog
     */
    public static String getAboutText() {
        return APP_NAME + "\n" +
                "Version " + APP_VERSION + "\n\n" +
                "A comprehensive billing management system\n" +
                "for government utility services.\n\n" +
                getCopyright();
    }
}