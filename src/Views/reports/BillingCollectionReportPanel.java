package Views.reports;

import Views.components.*;
import Model.DAO.BillingReportDAO;
import Reports.BIllingCollectionReport;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;

/**
 * Billing and Collection Report Panel
 * Assigned to: CRISOLOGO, Paul Martin Ryan A.
 */
public class BillingCollectionReportPanel extends JPanel {

    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;
    private StyledTable reportTable;
    private DefaultTableModel tableModel;
    private BillingReportDAO reportDAO;
    private JLabel totalBillsLabel;
    private JLabel totalAmountDueLabel;
    private JLabel totalCollectedLabel;

    public BillingCollectionReportPanel() {
        reportDAO = new BillingReportDAO();
        setLayout(new BorderLayout());
        setBackground(ColorScheme.BACKGROUND);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(ColorScheme.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel titlePanel = createTitlePanel();
        JPanel filterPanel = createFilterPanel();
        JPanel summaryPanel = createSummaryPanel();
        JPanel tablePanel = createTablePanel();
        JPanel actionsPanel = createActionsPanel();

        JPanel topPanel = new JPanel(new BorderLayout(0, 15));
        topPanel.setBackground(ColorScheme.BACKGROUND);
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.CENTER);
        topPanel.add(summaryPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(actionsPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ColorScheme.BACKGROUND);

        JLabel titleLabel = new JLabel("💵 Billing & Collection Report");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel("Billing summaries and collection details per billing period");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(descLabel);

        return panel;
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel startLabel = new JLabel("Start Date:");
        startLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));

        startDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd");
        startDateSpinner.setEditor(startEditor);
        startDateSpinner.setPreferredSize(new Dimension(150, 35));

        JLabel endLabel = new JLabel("End Date:");
        endLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));

        endDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endDateSpinner, "yyyy-MM-dd");
        endDateSpinner.setEditor(endEditor);
        endDateSpinner.setPreferredSize(new Dimension(150, 35));

        StyledButton generateButton = new StyledButton("Generate Report", StyledButton.ButtonType.PRIMARY);
        generateButton.addActionListener(e -> generateReport());

        panel.add(startLabel);
        panel.add(startDateSpinner);
        panel.add(endLabel);
        panel.add(endDateSpinner);
        panel.add(generateButton);

        return panel;
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setBackground(ColorScheme.BACKGROUND);

        totalBillsLabel = new JLabel("0");
        totalAmountDueLabel = new JLabel("₱0.00");
        totalCollectedLabel = new JLabel("₱0.00");

        panel.add(createSummaryCard("Total Bills", totalBillsLabel, ColorScheme.PRIMARY));
        panel.add(createSummaryCard("Amount Due", totalAmountDueLabel, ColorScheme.WARNING));
        panel.add(createSummaryCard("Total Collected", totalCollectedLabel, ColorScheme.SUCCESS));

        return panel;
    }

    private JPanel createSummaryCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(ColorScheme.TEXT_SECONDARY);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(color);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.add(titleLabel);
        content.add(Box.createVerticalStrut(5));
        content.add(valueLabel);

        card.add(content, BorderLayout.CENTER);

        return card;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER, 1));

        String[] columns = {"Bill ID", "Customer", "Consumption", "Amount Due", "Amount Paid", "Billing Date", "Payment Date", "Staff"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reportTable = new StyledTable(tableModel);
        reportTable.setCurrencyColumnRenderer(3);
        reportTable.setCurrencyColumnRenderer(4);
        reportTable.setDateColumnRenderer(5);
        reportTable.setDateColumnRenderer(6);

        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setBorder(null);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setBackground(ColorScheme.BACKGROUND);

        StyledButton printButton = new StyledButton("Print Report", StyledButton.ButtonType.SECONDARY);
        printButton.addActionListener(e -> printReport());

        panel.add(printButton);

        return panel;
    }

    private void generateReport() {
        java.util.Date startDate = (java.util.Date) startDateSpinner.getValue();
        java.util.Date endDate = (java.util.Date) endDateSpinner.getValue();

        Date sqlStartDate = new Date(startDate.getTime());
        Date sqlEndDate = new Date(endDate.getTime());

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        SwingWorker<List<BIllingCollectionReport>, Void> worker = new SwingWorker<List<BIllingCollectionReport>, Void>() {
            @Override
            protected List<BIllingCollectionReport> doInBackground() {
                return reportDAO.getBillingCollectionReport(sqlStartDate, sqlEndDate);
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                try {
                    List<BIllingCollectionReport> data = get();

                    if (data == null || data.isEmpty()) {
                        JOptionPane.showMessageDialog(BillingCollectionReportPanel.this,
                                "No billing data found for the selected date range.",
                                "No Data",
                                JOptionPane.INFORMATION_MESSAGE);
                        tableModel.setRowCount(0);
                        totalBillsLabel.setText("0");
                        totalAmountDueLabel.setText("₱0.00");
                        totalCollectedLabel.setText("₱0.00");
                        return;
                    }

                    displayReport(data);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(BillingCollectionReportPanel.this,
                            "Error generating report:\n" + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    private void displayReport(List<BIllingCollectionReport> data) {
        tableModel.setRowCount(0);

        double totalDue = 0;
        double totalCollected = 0;

        for (BIllingCollectionReport record : data) {
            tableModel.addRow(new Object[]{
                    record.billID(),
                    record.customerName(),
                    String.format("%.2f", record.consumptionValue()),
                    record.amountDue(),
                    record.amountPaid() != null ? record.amountPaid() : 0.0,
                    record.billingDate(),
                    record.paymentDate(),
                    record.staffName()
            });

            totalDue += record.amountDue();
            if (record.amountPaid() != null) {
                totalCollected += record.amountPaid();
            }
        }

        totalBillsLabel.setText(String.valueOf(data.size()));
        totalAmountDueLabel.setText(String.format("₱%.2f", totalDue));
        totalCollectedLabel.setText(String.format("₱%.2f", totalCollected));
    }

    private void printReport() {
        try {
            reportTable.print();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error printing report: " + e.getMessage(),
                    "Print Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}