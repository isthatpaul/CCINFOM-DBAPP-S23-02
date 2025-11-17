package Views.reports;

import Views.components.*;
import Model.DAO.ConsumptionReportDAO;
import Reports.ConsumptionAnalysisReport;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

/**
 * Consumption Analysis Report Panel
 * Assigned to: LUIS, Kamillu Raphael C. & CRISOLOGO, Paul Martin Ryan A.
 */
public class ConsumptionReportPanel extends JPanel {

    private JSpinner yearSpinner;
    private StyledTable reportTable;
    private DefaultTableModel tableModel;
    private ConsumptionReportDAO reportDAO;
    private JLabel avgConsumptionLabel;

    public ConsumptionReportPanel() {
        reportDAO = new ConsumptionReportDAO();
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

        add(mainPanel);
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ColorScheme.BACKGROUND);

        JLabel titleLabel = new JLabel("📊 Consumption Analysis Report");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel("Usage patterns and trends to detect inefficiencies");
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

        JLabel yearLabel = new JLabel("Year:");
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

        JLabel summaryTitleLabel = new JLabel("Average Consumption:");
        summaryTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        avgConsumptionLabel = new JLabel("0.00 kWh");
        avgConsumptionLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        avgConsumptionLabel.setForeground(ColorScheme.PRIMARY);

        panel.add(summaryTitleLabel);
        panel.add(avgConsumptionLabel);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER, 1));

        String[] columns = {"Branch", "Average Consumption", "Alert"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reportTable = new StyledTable(tableModel);

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

        SwingWorker<List<ConsumptionAnalysisReport>, Void> worker = new SwingWorker<List<ConsumptionAnalysisReport>, Void>() {
            @Override
            protected List<ConsumptionAnalysisReport> doInBackground() {
                return reportDAO.getAverageConsumptionPerBranch(startDate, endDate);
            }

            @Override
            protected void done() {
                try {
                    List<ConsumptionAnalysisReport> data = get();
                    displayReport(data);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(ConsumptionReportPanel.this,
                            "Error generating report: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    private void displayReport(List<ConsumptionAnalysisReport> data) {
        tableModel.setRowCount(0);

        double totalConsumption = 0;

        for (ConsumptionAnalysisReport record : data) {
            String alert = determineAlert(record.averageConsumption());

            tableModel.addRow(new Object[]{
                    record.branch(),
                    String.format("%.2f kWh", record.averageConsumption()),
                    alert
            });

            totalConsumption += record.averageConsumption();
        }

        double avgConsumption = data.isEmpty() ? 0 : totalConsumption / data.size();
        avgConsumptionLabel.setText(String.format("%.2f kWh", avgConsumption));
    }

    private String determineAlert(double consumption) {
        if (consumption > 1000) {
            return "Very High";
        } else if (consumption > 700) {
            return "High";
        } else if (consumption < 100) {
            return " Unusually Low";
        } else {
            return "Normal";
        }
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