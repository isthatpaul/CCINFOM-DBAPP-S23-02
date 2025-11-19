package Views.reports;

import Views.components.*;
import Model.DAO.RevenueReportDAO;
import Reports.RevenueReport;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.print.PrinterException;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

/**
 * Revenue Report Panel
 * Directly calls RevenueReportDAO to fetch data
 * Assigned to: ATACADOR, Juan Lorenzo N.
 */
public class RevenueReportPanel extends JPanel {

    private JSpinner yearSpinner;
    private StyledTable reportTable;
    private DefaultTableModel tableModel;
    private RevenueReportDAO reportDAO;
    private JLabel totalRevenueLabel;

    public RevenueReportPanel() {
        reportDAO = new RevenueReportDAO();
        setLayout(new BorderLayout());
        setBackground(ColorScheme.BACKGROUND);
        initComponents();
    }
    
    // ... (All other methods remain unchanged) ...
    private void initComponents() {
        // Main container
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
        JLabel titleLabel = new JLabel("Revenue Report");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel descLabel = new JLabel("Overall revenue from utility collections by utility type");
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
        JLabel yearLabel = new JLabel("Select Year:");
        yearLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        SpinnerModel yearModel = new SpinnerNumberModel(currentYear, 2000, 2100, 1);
        yearSpinner = new JSpinner(yearModel);
        yearSpinner.setPreferredSize(new Dimension(100, 35));
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(yearSpinner, "#");
        yearSpinner.setEditor(editor);
        StyledButton generateButton = new StyledButton("Generate Report", StyledButton.ButtonType.PRIMARY);
        generateButton.addActionListener(e -> generateReport());
        panel.add(yearLabel);
        panel.add(yearSpinner);
        panel.add(generateButton);
        return panel;
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        JLabel summaryTitleLabel = new JLabel("Total Revenue:");
        summaryTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        summaryTitleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        totalRevenueLabel = new JLabel("₱0.00");
        totalRevenueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        totalRevenueLabel.setForeground(ColorScheme.SUCCESS);
        panel.add(summaryTitleLabel);
        panel.add(totalRevenueLabel);
        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER, 1));
        String[] columns = {"Utility Type ID", "Utility Name", "Total Revenue", "Percentage"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reportTable = new StyledTable(tableModel);
        reportTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        reportTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        reportTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        reportTable.getColumnModel().getColumn(3).setPreferredWidth(100);
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
        int year = (Integer) yearSpinner.getValue();
        Calendar cal = Calendar.getInstance();
        cal.set(year, Calendar.JANUARY, 1);
        Date startDate = new Date(cal.getTimeInMillis());
        cal.set(year, Calendar.DECEMBER, 31);
        Date endDate = new Date(cal.getTimeInMillis());
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        SwingWorker<List<RevenueReport>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<RevenueReport> doInBackground() {
                return reportDAO.getRevenueReport(startDate, endDate);
            }
            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                try {
                    List<RevenueReport> data = get();
                    if (data == null || data.isEmpty()) {
                        JOptionPane.showMessageDialog(RevenueReportPanel.this, "No revenue data found for year " + year, "No Data", JOptionPane.INFORMATION_MESSAGE);
                        tableModel.setRowCount(0);
                        totalRevenueLabel.setText("₱0.00");
                        return;
                    }
                    displayReport(data);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(RevenueReportPanel.this, "Error generating report:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void displayReport(List<RevenueReport> data) {
        tableModel.setRowCount(0);
        double totalRevenue = 0;
        for (RevenueReport record : data) {
            totalRevenue += record.totalRevenue();
        }
        for (RevenueReport record : data) {
            double percentage = (totalRevenue > 0) ? (record.totalRevenue() / totalRevenue * 100) : 0;
            tableModel.addRow(new Object[]{
                    record.utilityTypeID(),
                    record.utilityName(),
                    record.totalRevenue(),
                    String.format("%.1f%%", percentage)
            });
        }
        totalRevenueLabel.setText(String.format("₱%.2f", totalRevenue));
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