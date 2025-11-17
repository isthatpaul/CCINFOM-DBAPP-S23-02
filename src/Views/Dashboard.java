package Views;

import Views.components.*;
import Model.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Dashboard showing key statistics and quick actions.
 */
public class Dashboard extends JPanel {

    private Staff currentStaff;
    private JLabel totalCustomersLabel;
    private JLabel pendingBillsLabel;
    private JLabel totalRevenueLabel;
    private JLabel overdueAccountsLabel;

    public Dashboard(Staff staff) {
        this.currentStaff = staff;
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

        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);

        JLabel welcomeLabel = new JLabel("Welcome back, " + currentStaff.username() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeLabel.setForeground(ColorScheme.TEXT_SECONDARY);

        JPanel titleContainer = new JPanel();
        titleContainer.setLayout(new BoxLayout(titleContainer, BoxLayout.Y_AXIS));
        titleContainer.setBackground(Color.WHITE);
        titleContainer.add(titleLabel);
        titleContainer.add(Box.createVerticalStrut(5));
        titleContainer.add(welcomeLabel);

        headerPanel.add(titleContainer, BorderLayout.WEST);

        // Content
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(ColorScheme.BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Statistics cards
        JPanel statsPanel = createStatsPanel();

        contentPanel.add(statsPanel, BorderLayout.NORTH);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 20, 0));
        panel.setBackground(ColorScheme.BACKGROUND);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        totalCustomersLabel = new JLabel("0");
        pendingBillsLabel = new JLabel("0");
        totalRevenueLabel = new JLabel("₱0.00");
        overdueAccountsLabel = new JLabel("0");

        panel.add(createStatCard("Total Customers", totalCustomersLabel, ColorScheme.PRIMARY));
        panel.add(createStatCard("Pending Bills", pendingBillsLabel, ColorScheme.WARNING));
        panel.add(createStatCard("Monthly Revenue", totalRevenueLabel, ColorScheme.SUCCESS));
        panel.add(createStatCard("Overdue Accounts", overdueAccountsLabel, ColorScheme.ERROR));

        return panel;
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(ColorScheme.TEXT_SECONDARY);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(accentColor);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(titleLabel);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(valueLabel);

        card.add(leftPanel, BorderLayout.CENTER);

        return card;
    }

    public void refreshData() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            int totalCustomers, pendingBills, overdueAccounts;
            double monthlyRevenue;

            @Override
            protected Void doInBackground() {
                try {
                    CustomerCRUD customerCRUD = new CustomerCRUD();
                    BillCRUD billCRUD = new BillCRUD();
                    PaymentCRUD paymentCRUD = new PaymentCRUD();

                    totalCustomers = customerCRUD.getAllRecords().size();

                    List<Bill> allBills = billCRUD.getAllRecords();
                    pendingBills = (int) allBills.stream()
                            .filter(b -> "UNPAID".equals(b.status()) || "PARTIALLY_PAID".equals(b.status()))
                            .count();

                    overdueAccounts = (int) allBills.stream()
                            .filter(b -> "OVERDUE".equals(b.status()))
                            .count();

                    // Calculate current month revenue
                    List<Payment> payments = paymentCRUD.getAllRecords();
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    int currentMonth = cal.get(java.util.Calendar.MONTH);
                    int currentYear = cal.get(java.util.Calendar.YEAR);

                    monthlyRevenue = payments.stream()
                            .filter(p -> {
                                cal.setTime(p.paymentDate());
                                return cal.get(java.util.Calendar.MONTH) == currentMonth &&
                                        cal.get(java.util.Calendar.YEAR) == currentYear;
                            })
                            .mapToDouble(Payment::amountPaid)
                            .sum();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                totalCustomersLabel.setText(String.valueOf(totalCustomers));
                pendingBillsLabel.setText(String.valueOf(pendingBills));
                totalRevenueLabel.setText(String.format("₱%.2f", monthlyRevenue));
                overdueAccountsLabel.setText(String.valueOf(overdueAccounts));
            }
        };

        worker.execute();
    }
}