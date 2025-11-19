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
import config.AppConfig;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Main application window with navigation and content area.
 */
public class MainFrame extends JFrame {

    private Staff currentStaff;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JLabel statusLabel;
    private JLabel userLabel;
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

        setJMenuBar(createMenuBar());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorScheme.BACKGROUND);

        JPanel sidebar = createSidebar();

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(ColorScheme.BACKGROUND);

        JPanel statusBar = createStatusBar();

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(statusBar, BorderLayout.SOUTH);

        add(mainPanel);

        Timer timer = new Timer(1000, e -> updateStatusTime());
        timer.start();
    }
    
    // ... (All UI creation methods are correct and remain unchanged) ...
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.BORDER));
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> handleLogout());
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
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
        JMenu adminMenu = new JMenu("Admin");
        JMenuItem employeesItem = new JMenuItem("Employees");
        employeesItem.addActionListener(e -> showEmployees());
        JMenuItem utilitiesItem = new JMenuItem("Utility Types");
        utilitiesItem.addActionListener(e -> showUtilityTypes());
        JMenuItem ratesItem = new JMenuItem("Rates");
        ratesItem.addActionListener(e -> showRates());
        adminMenu.add(employeesItem);
        adminMenu.add(utilitiesItem);
        adminMenu.add(ratesItem);
        JMenu reportsMenu = new JMenu("Reports");
        JMenuItem reportsItem = new JMenuItem("View Reports");
        reportsItem.addActionListener(e -> showReports());
        reportsMenu.add(reportsItem);
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAbout());
        helpMenu.add(aboutItem);
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        if (Objects.equals(currentStaff.role(), "Admin")) {
            menuBar.add(adminMenu);
        }
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
        sidebar.add(createNavButton("Dashboard", this::showDashboard));
        sidebar.add(createNavButton("Customers", this::showCustomers));
        sidebar.add(createNavButton("Meters", this::showMeters));
        sidebar.add(createNavButton("Meter Assignment", this::showMeterAssignment));
        sidebar.add(createNavButton("Consumption", this::showConsumption));
        sidebar.add(createNavButton("Bills", this::showBills));
        sidebar.add(createNavButton("Payments", this::showPayments));
        if (Objects.equals(currentStaff.role(), "Admin")) {
            sidebar.add(createNavButton("Employees", this::showEmployees));
            sidebar.add(createNavButton("Utility Types", this::showUtilityTypes));
            sidebar.add(createNavButton("Rates", this::showRates));
        }
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(createNavButton("Reports", this::showReports));
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
        meterAssignmentPanel = new MeterAssignmentPanel(currentStaff);
        billPanel = new BillPanel(currentStaff);
        paymentPanel = new PaymentPanel(currentStaff);
        reportsPanel = new ReportsPanel();
        consumptionPanel = new ConsumptionPanel(currentStaff);

        contentPanel.add(dashboard, "DASHBOARD");
        contentPanel.add(customerPanel, "CUSTOMERS");
        contentPanel.add(meterPanel, "METERS");
        contentPanel.add(meterAssignmentPanel, "METER_ASSIGNMENT");
        contentPanel.add(billPanel, "BILLS");
        contentPanel.add(paymentPanel, "PAYMENTS");
        contentPanel.add(reportsPanel, "REPORTS");
        contentPanel.add(consumptionPanel, "CONSUMPTION");
        
        if (Objects.equals(currentStaff.role(), "Admin")) {
            employeePanel = new EmployeePanel();
            utilityTypePanel = new UtilityTypePanel();
            ratePanel = new RatePanel();
            
            contentPanel.add(employeePanel, "EMPLOYEES");
            contentPanel.add(utilityTypePanel, "UTILITY_TYPES");
            contentPanel.add(ratePanel, "RATES");
        }
    }

    // FIX: All show...() methods are changed to show the panel BEFORE refreshing data.
    // This makes the UI feel instant and responsive.

    private void showDashboard() {
        cardLayout.show(contentPanel, "DASHBOARD");
        dashboard.refreshData();
    }

    private void showCustomers() {
        cardLayout.show(contentPanel, "CUSTOMERS");
        customerPanel.refreshData();
    }

    private void showMeters() {
        cardLayout.show(contentPanel, "METERS");
        meterPanel.refreshData();
    }

    private void showMeterAssignment() {
        cardLayout.show(contentPanel, "METER_ASSIGNMENT");
        meterAssignmentPanel.refreshData();
    }

    private void showBills() {
        cardLayout.show(contentPanel, "BILLS");
        billPanel.refreshData();
    }

    private void showPayments() {
        cardLayout.show(contentPanel, "PAYMENTS");
        paymentPanel.refreshData();
    }

    private void showEmployees() {
        if (employeePanel != null) {
            cardLayout.show(contentPanel, "EMPLOYEES");
            employeePanel.refreshData();
        } else {
            showUnauthorizedAccessMessage();
        }
    }

    private void showUtilityTypes() {
        if (utilityTypePanel != null) {
            cardLayout.show(contentPanel, "UTILITY_TYPES");
            utilityTypePanel.refreshData();
        } else {
            showUnauthorizedAccessMessage();
        }
    }

    private void showRates() {
        if (ratePanel != null) {
            cardLayout.show(contentPanel, "RATES");
            ratePanel.refreshData();
        } else {
            showUnauthorizedAccessMessage();
        }
    }

    private void showConsumption() {
        cardLayout.show(contentPanel, "CONSUMPTION");
        consumptionPanel.refreshData();
    }

    private void showReports() {
        cardLayout.show(contentPanel, "REPORTS");
        // No data refresh needed for the main reports container panel
    }
    
    // ... (logout, about, and unauthorized access methods are unchanged) ...
    private void handleLogout() {
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            });
        }
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(this, AppConfig.getAboutText(), "About " + AppConfig.APP_SHORT_NAME, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showUnauthorizedAccessMessage() {
        JOptionPane.showMessageDialog(this, "You do not have permission to access this section.", "Access Denied", JOptionPane.ERROR_MESSAGE);
    }
}