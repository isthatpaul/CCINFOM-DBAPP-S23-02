package Views;

import Views.components.*;
import Views.customer.CustomerPanel;
import Views.billing.BillPanel;
import Views.billing.ConsumptionPanel;
import Views.billing.PaymentPanel;
import Views.meter.MeterPanel;
import Views.meter.MeterAssignmentPanel;
import Views.employee.EmployeePanel;
import Views.utilities.UtilityTypePanel;
import Views.utilities.RatePanel;
import Views.reports.ReportsPanel;
import Model.Staff;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Main application window with navigation and content area.
 */
public class MainFrame extends JFrame {

    private Staff currentStaff;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JLabel statusLabel;
    private JLabel userLabel;

    // Panel instances
    private Dashboard dashboard;
    private CustomerPanel customerPanel;
    private MeterPanel meterPanel;
    private MeterAssignmentPanel meterAssignmentPanel;
    private BillPanel billPanel;
    private PaymentPanel paymentPanel;
    private EmployeePanel employeePanel;
    private UtilityTypePanel utilityTypePanel;
    private RatePanel ratePanel;
    private ReportsPanel reportsPanel;
    private ConsumptionPanel consumptionPanel;

    public MainFrame(Staff staff) {
        this.currentStaff = staff;
        initComponents();
        initPanels();
        showDashboard();
    }

    private void initComponents() {
        setTitle("Public Utility Billing System - " + currentStaff.role());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 700));

        // Menu bar
        setJMenuBar(createMenuBar());

        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorScheme.BACKGROUND);

        // Sidebar
        JPanel sidebar = createSidebar();

        // Content area with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(ColorScheme.BACKGROUND);

        // Status bar
        JPanel statusBar = createStatusBar();

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(statusBar, BorderLayout.SOUTH);

        add(mainPanel);

        // Update time every second
        Timer timer = new Timer(1000, e -> updateStatusTime());
        timer.start();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.BORDER));

        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> handleLogout());
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // View Menu
        JMenu viewMenu = new JMenu("View");
        JMenuItem dashboardItem = new JMenuItem("Dashboard");
        dashboardItem.addActionListener(e -> showDashboard());
        JMenuItem customersItem = new JMenuItem("Customers");
        customersItem.addActionListener(e -> showCustomers());
        JMenuItem billsItem = new JMenuItem("Bills");
        billsItem.addActionListener(e -> showBills());
        viewMenu.add(dashboardItem);
        viewMenu.addSeparator();
        viewMenu.add(customersItem);
        viewMenu.add(billsItem);

        // Reports Menu
        JMenu reportsMenu = new JMenu("Reports");
        JMenuItem reportsItem = new JMenuItem("View Reports");
        reportsItem.addActionListener(e -> showReports());
        reportsMenu.add(reportsItem);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAbout());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(reportsMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(ColorScheme.SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, ColorScheme.BORDER));

        // Logo/Title
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(ColorScheme.PRIMARY);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setMaximumSize(new Dimension(250, 80));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("PUBS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(titleLabel);

        sidebar.add(titlePanel);
        sidebar.add(Box.createVerticalStrut(20));

        // Navigation buttons
        sidebar.add(createNavButton("🏠 Dashboard", () -> showDashboard()));
        sidebar.add(createNavButton("👥 Customers", () -> showCustomers()));
        sidebar.add(createNavButton("⚡ Meters", () -> showMeters()));
        sidebar.add(createNavButton("🔗 Meter Assignment", () -> showMeterAssignment()));
        sidebar.add(createNavButton("📄 Bills", () -> showBills()));
        sidebar.add(createNavButton("💳 Payments", () -> showPayments()));
        sidebar.add(createNavButton("👔 Employees", () -> showEmployees()));
        sidebar.add(createNavButton("🔧 Utility Types", () -> showUtilityTypes()));
        sidebar.add(createNavButton("💰 Rates", () -> showRates()));
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(createNavButton("📊 Reports", () -> showReports()));

        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    private JButton createNavButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(ColorScheme.TEXT_PRIMARY);
        button.setBackground(ColorScheme.SIDEBAR_BG);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setMaximumSize(new Dimension(250, 45));
        button.setPreferredSize(new Dimension(250, 45));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(229, 231, 235));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ColorScheme.SIDEBAR_BG);
            }
        });

        button.addActionListener(e -> action.run());

        return button;
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(Color.WHITE);
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, ColorScheme.BORDER));
        statusBar.setPreferredSize(new Dimension(getWidth(), 30));

        userLabel = new JLabel("  User: " + currentStaff.username() + " (" + currentStaff.role() + ")");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userLabel.setForeground(ColorScheme.TEXT_SECONDARY);

        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        updateStatusTime();

        statusBar.add(userLabel, BorderLayout.WEST);
        statusBar.add(statusLabel, BorderLayout.EAST);

        return statusBar;
    }

    private void updateStatusTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
        statusLabel.setText(sdf.format(new Date()) + "  ");
    }

    private void initPanels() {
        dashboard = new Dashboard(currentStaff);
        customerPanel = new CustomerPanel();
        meterPanel = new MeterPanel();
        meterAssignmentPanel = new MeterAssignmentPanel();
        billPanel = new BillPanel(currentStaff);
        paymentPanel = new PaymentPanel(currentStaff);
        employeePanel = new EmployeePanel();
        utilityTypePanel = new UtilityTypePanel();
        ratePanel = new RatePanel();
        reportsPanel = new ReportsPanel();
        consumptionPanel = new ConsumptionPanel();

        contentPanel.add(dashboard, "DASHBOARD");
        contentPanel.add(customerPanel, "CUSTOMERS");
        contentPanel.add(meterPanel, "METERS");
        contentPanel.add(meterAssignmentPanel, "METER_ASSIGNMENT");
        contentPanel.add(billPanel, "BILLS");
        contentPanel.add(paymentPanel, "PAYMENTS");
        contentPanel.add(employeePanel, "EMPLOYEES");
        contentPanel.add(utilityTypePanel, "UTILITY_TYPES");
        contentPanel.add(ratePanel, "RATES");
        contentPanel.add(reportsPanel, "REPORTS");
        contentPanel.add(consumptionPanel, "CONSUMPTION");
    }

    private void showDashboard() {
        dashboard.refreshData();
        cardLayout.show(contentPanel, "DASHBOARD");
    }

    private void showCustomers() {
        customerPanel.refreshData();
        cardLayout.show(contentPanel, "CUSTOMERS");
    }

    private void showMeters() {
        meterPanel.refreshData();
        cardLayout.show(contentPanel, "METERS");
    }

    private void showMeterAssignment() {
        meterAssignmentPanel.refreshData();
        cardLayout.show(contentPanel, "METER_ASSIGNMENT");
    }

    private void showBills() {
        billPanel.refreshData();
        cardLayout.show(contentPanel, "BILLS");
    }

    private void showPayments() {
        paymentPanel.refreshData();
        cardLayout.show(contentPanel, "PAYMENTS");
    }

    private void showEmployees() {
        employeePanel.refreshData();
        cardLayout.show(contentPanel, "EMPLOYEES");
    }

    private void showUtilityTypes() {
        utilityTypePanel.refreshData();
        cardLayout.show(contentPanel, "UTILITY_TYPES");
    }

    private void showRates() {
        ratePanel.refreshData();
        cardLayout.show(contentPanel, "RATES");
    }

    private void showConsumption() {
        consumptionPanel.refreshData();
        cardLayout.show(contentPanel, "CONSUMPTION");
    }

    private void showReports() {
        cardLayout.show(contentPanel, "REPORTS");
    }

    private void handleLogout() {
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            });
        }
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(this,
                "Public Utility Billing System\n" +
                        "Version 1.0\n\n" +
                        "A comprehensive billing management system\n" +
                        "for government utility services.\n\n" +
                        "© 2025 CCINFOM-DBAPP-S23-02",
                "About PUBS",
                JOptionPane.INFORMATION_MESSAGE);
    }
}