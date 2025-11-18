package Views.components;

import java.awt.Color;

/**
 * Centralized color scheme for the Public Utility Billing System.
 * Professional government-appropriate colors.
 */
public class ColorScheme {
    // Primary Colors
    public static final Color PRIMARY = new Color(30, 58, 138);        // #1e3a8a Deep Blue
    public static final Color SECONDARY = new Color(59, 130, 246);     // #3b82f6 Blue

    // Semantic Colors
    public static final Color SUCCESS = new Color(16, 185, 129);       // #10b981 Green
    public static final Color WARNING = new Color(245, 158, 11);       // #f59e0b Orange
    public static final Color ERROR = new Color(239, 68, 68);          // #ef4444 Red
    public static final Color INFO = new Color(59, 130, 246);          // #3b82f6 Blue

    // Background Colors
    public static final Color BACKGROUND = new Color(243, 244, 246);   // #f3f4f6 Light Gray
    public static final Color PANEL_BG = Color.WHITE;                  // #ffffff White
    public static final Color SIDEBAR_BG = new Color(249, 250, 251);   // #f9fafb Very Light Gray

    // Text Colors
    public static final Color TEXT_PRIMARY = new Color(31, 41, 55);    // #1f2937 Dark Gray
    public static final Color TEXT_SECONDARY = new Color(107, 114, 128); // #6b7280 Medium Gray
    public static final Color TEXT_LIGHT = new Color(156, 163, 175);   // #9ca3af Light Gray
    public static final Color TEXT_ON_PRIMARY = Color.WHITE;

    // Border Colors
    public static final Color BORDER = new Color(229, 231, 235);       // #e5e7eb
    public static final Color BORDER_FOCUS = SECONDARY;

    // Table Colors
    public static final Color TABLE_HEADER_BG = PRIMARY;
    public static final Color TABLE_HEADER_FG = TEXT_PRIMARY;
    public static final Color TABLE_ROW_EVEN = Color.WHITE;
    public static final Color TABLE_ROW_ODD = new Color(249, 250, 251);
    public static final Color TABLE_SELECTION = new Color(219, 234, 254); // Light blue

    // Button Hover Colors
    public static final Color PRIMARY_HOVER = new Color(30, 58, 138).darker();
    public static final Color SUCCESS_HOVER = SUCCESS.darker();
    public static final Color WARNING_HOVER = WARNING.darker();
    public static final Color ERROR_HOVER = ERROR.darker();

    private ColorScheme() {
        // Utility class - no instantiation
    }
}
