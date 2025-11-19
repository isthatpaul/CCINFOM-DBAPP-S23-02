package Views.reports;

import Views.components.*;
import Model.DAO.OverdueReportDAO;
import Reports.OverdueReport;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.print.PrinterException;
import java.sql.Date;
import java.util.List;

/**
 * Outstanding/Overdue Accounts Report Panel
 * Assigned to: LUIS, Kamillu Raphael C.
 */
public class OverdueReportPanel extends JPanel {

    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;
    private StyledTable reportTable;
    private DefaultTableModel tableModel;
    private OverdueReportDAO reportDAO;
    private JLabel totalOverdueAccountsLabel;
    private JLabel totalOverdueAmountLabel;

    public OverdueReportPanel() {
        reportDAO = new OverdueReportDAO();
        setLayout(new BorderLayout());
        setBackground(ColorScheme.BACKGROUND);
        initComponents();
    }
    
    // ... (All other methods remain unchanged) ...
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
        add(mainPanel);
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ColorScheme.BACKGROUND);
        JLabel titleLabel = new JLabel("Outstanding/Overdue Accounts Report");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel descLabel = new JLabel("Lists all unpaid or overdue bills per customer");
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
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setBackground(ColorScheme.BACKGROUND);
        totalOverdueAccountsLabel = new JLabel("0");
        totalOverdueAmountLabel = new JLabel("₱0.00");
        panel.add(createSummaryCard("Total Overdue Accounts", totalOverdueAccountsLabel, ColorScheme.ERROR));
        panel.add(createSummaryCard("Total Overdue Amount", totalOverdueAmountLabel, ColorScheme.ERROR));
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
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
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
        String[] columns = {"Customer ID", "Customer Name", "Total Overdue Amount", "Priority"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reportTable = new StyledTable(tableModel);
        reportTable.setCurrencyColumnRenderer(2);
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
        SwingWorker<List<OverdueReport>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<OverdueReport> doInBackground() {
                return reportDAO.getOverdueAccounts(sqlStartDate, sqlEndDate);
            }
            @Override
            protected void done() {
                try {
                    List<OverdueReport> data = get();
                    displayReport(data);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(OverdueReportPanel.this, "Error generating report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void displayReport(List<OverdueReport> data) {
        tableModel.setRowCount(0);
        double totalOverdueAmount = 0;
        for (OverdueReport record : data) {
            String priority = determinePriority(record.totalOverdue());
            tableModel.addRow(new Object[]{
                    record.customerID(),
                    record.customerName(),
                    record.totalOverdue(),
                    priority
            });
            totalOverdueAmount += record.totalOverdue();
        }
        totalOverdueAccountsLabel.setText(String.valueOf(data.size()));
        totalOverdueAmountLabel.setText(String.format("₱%.2f", totalOverdueAmount));
    }

    private String determinePriority(double overdueAmount) {
        if (overdueAmount >= 10000) {
            return "CRITICAL";
        } else if (overdueAmount >= 5000) {
            return "HIGH";
        } else if (overdueAmount >= 2000) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }

    private void printReport() {
        // FIX: Replaced the simple print call with one that handles header color
        JTableHeader header = reportTable.getTableHeader();
        Color originalColor = header.getBackground();
        header.setBackground(ColorScheme.TABLE_HEADER_BG);
        
        try {
            reportTable.print();
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this,
                    "Error printing report: " + e.getMessage(),
                    "Print Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            header.setBackground(originalColor);
        }
    }
}