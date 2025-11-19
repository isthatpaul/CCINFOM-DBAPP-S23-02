package Views.reports;

import Views.components.*;
import Model.DAO.CustomerReportDAO;
import Reports.CustomerAccountReport;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.print.PrinterException;
import java.sql.Date;
import java.util.List;

/**
 * Customer Account Report Panel
 * Assigned to: SAMONTE, Joshua Carlos B.
 */
public class CustomerAccountReportPanel extends JPanel {

    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;
    private StyledTable reportTable;
    private DefaultTableModel tableModel;
    private CustomerReportDAO reportDAO;
    private JLabel totalCustomersLabel;

    public CustomerAccountReportPanel() {
        reportDAO = new CustomerReportDAO();
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
        JLabel titleLabel = new JLabel("Customer Account Report");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel descLabel = new JLabel("Lists all active customer accounts with meter details per branch");
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
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        JLabel summaryTitleLabel = new JLabel("Total Customers:");
        summaryTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        summaryTitleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        totalCustomersLabel = new JLabel("0");
        totalCustomersLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalCustomersLabel.setForeground(ColorScheme.PRIMARY);
        panel.add(summaryTitleLabel);
        panel.add(totalCustomersLabel);
        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER, 1));
        String[] columns = {"Customer ID", "Customer Name", "Branch", "Meter Serial", "Meter Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reportTable = new StyledTable(tableModel);
        reportTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        reportTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        reportTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        reportTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        reportTable.getColumnModel().getColumn(4).setPreferredWidth(120);
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
        SwingWorker<List<CustomerAccountReport>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<CustomerAccountReport> doInBackground() {
                return reportDAO.getCustomerAccountReport(sqlStartDate, sqlEndDate);
            }
            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                try {
                    List<CustomerAccountReport> data = get();
                    if (data == null || data.isEmpty()) {
                        JOptionPane.showMessageDialog(CustomerAccountReportPanel.this, "No data found for the selected date range.", "No Data", JOptionPane.INFORMATION_MESSAGE);
                        tableModel.setRowCount(0);
                        totalCustomersLabel.setText("0");
                        return;
                    }
                    displayReport(data);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(CustomerAccountReportPanel.this, "Error generating report:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void displayReport(List<CustomerAccountReport> data) {
        tableModel.setRowCount(0);
        for (CustomerAccountReport record : data) {
            tableModel.addRow(new Object[]{
                    record.customerID(),
                    record.customerName(),
                    record.branch(),
                    record.meterSerial(),
                    record.meterStatus()
            });
        }
        totalCustomersLabel.setText(String.valueOf(data.size()));
    }

    private void printReport() {
        // FIX: Replaced the simple print call with one that handles header color
        JTableHeader header = reportTable.getTableHeader();
        Color originalColor = header.getBackground();
        header.setBackground(ColorScheme.TABLE_HEADER_BG); // Use the color from your theme
        
        try {
            reportTable.print();
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this,
                    "Error printing report: " + e.getMessage(),
                    "Print Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            // IMPORTANT: Restore the original color to not affect the on-screen UI
            header.setBackground(originalColor);
        }
    }
}