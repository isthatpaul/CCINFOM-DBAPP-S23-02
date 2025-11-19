package Views.reports;

import Views.components.*;
import javax.swing.*;
import java.awt.*;

/**
 * Main reports panel with navigation to all report types.
 */
public class ReportsPanel extends JPanel {

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private CustomerAccountReportPanel customerAccountPanel;
    private BillingCollectionReportPanel billingCollectionPanel;
    private OverdueReportPanel overduePanel;
    private RevenueReportPanel revenuePanel;
    private ConsumptionReportPanel consumptionPanel;

    public ReportsPanel() {
        setLayout(new BorderLayout());
        setBackground(ColorScheme.BACKGROUND);
        initComponents();
    }

    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.BORDER),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        JLabel titleLabel = new JLabel("Reports");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);

        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Sidebar with report types
        JPanel sidebar = createSidebar();

        // Content area with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(ColorScheme.BACKGROUND);

        // Initialize report panels
        initReportPanels();

        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.add(sidebar, BorderLayout.WEST);
        mainContent.add(contentPanel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(mainContent, BorderLayout.CENTER);

        // Show first report by default
        showCustomerAccountReport();
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);
        sidebar.setPreferredSize(new Dimension(280, getHeight()));
        sidebar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, ColorScheme.BORDER),
                BorderFactory.createEmptyBorder(20, 15, 20, 15)
        ));

        JLabel sidebarTitle = new JLabel("Report Types");
        sidebarTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sidebarTitle.setForeground(ColorScheme.TEXT_PRIMARY);
        sidebarTitle.setBorder(BorderFactory.createEmptyBorder(0, 10, 15, 0));
        sidebar.add(sidebarTitle);

        sidebar.add(createReportButton("Customer Account Report",
                "Lists all active customer accounts", () -> showCustomerAccountReport()));
        sidebar.add(createReportButton("Billing & Collection Report",
                "Billing summaries and collections", () -> showBillingCollectionReport()));
        sidebar.add(createReportButton("Outstanding/Overdue Report",
                "Unpaid or overdue bills", () -> showOverdueReport()));
        sidebar.add(createReportButton("Revenue Report",
                "Overall revenue by utility type", () -> showRevenueReport()));
        sidebar.add(createReportButton("Consumption Analysis",
                "Usage patterns and trends", () -> showConsumptionReport()));

        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    private JPanel createReportButton(String title, String description, Runnable action) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.BORDER),
                BorderFactory.createEmptyBorder(12, 10, 12, 10)
        ));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.setMaximumSize(new Dimension(280, 70));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel("<html>" + description + "</html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        descLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(3));
        panel.add(descLabel);

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                action.run();
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBackground(ColorScheme.BACKGROUND);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel.setBackground(Color.WHITE);
            }
        });

        return panel;
    }

    private void initReportPanels() {
        customerAccountPanel = new CustomerAccountReportPanel();
        billingCollectionPanel = new BillingCollectionReportPanel();
        overduePanel = new OverdueReportPanel();
        revenuePanel = new RevenueReportPanel();
        consumptionPanel = new ConsumptionReportPanel();

        contentPanel.add(customerAccountPanel, "CUSTOMER_ACCOUNT");
        contentPanel.add(billingCollectionPanel, "BILLING_COLLECTION");
        contentPanel.add(overduePanel, "OVERDUE");
        contentPanel.add(revenuePanel, "REVENUE");
        contentPanel.add(consumptionPanel, "CONSUMPTION");
    }

    private void showCustomerAccountReport() {
        cardLayout.show(contentPanel, "CUSTOMER_ACCOUNT");
    }

    private void showBillingCollectionReport() {
        cardLayout.show(contentPanel, "BILLING_COLLECTION");
    }

    private void showOverdueReport() {
        cardLayout.show(contentPanel, "OVERDUE");
    }

    private void showRevenueReport() {
        cardLayout.show(contentPanel, "REVENUE");
    }

    private void showConsumptionReport() {
        cardLayout.show(contentPanel, "CONSUMPTION");
    }
}