package Views.billing;

import Views.components.*;
import Model.Consumption;
import Model.ConsumptionCRUD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Consumption management panel
 */
public class ConsumptionPanel extends JPanel {

    private StyledTable consumptionTable;
    private DefaultTableModel tableModel;
    private ConsumptionCRUD consumptionCRUD;

    public ConsumptionPanel() {
        consumptionCRUD = new ConsumptionCRUD();
        setLayout(new BorderLayout());
        setBackground(ColorScheme.BACKGROUND);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(ColorScheme.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel headerPanel = createHeaderPanel();
        JPanel toolbarPanel = createToolbarPanel();
        JPanel tablePanel = createTablePanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setBackground(ColorScheme.BACKGROUND);
        centerPanel.add(toolbarPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ColorScheme.BACKGROUND);

        JLabel titleLabel = new JLabel("Consumption Records");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);

        panel.add(titleLabel, BorderLayout.WEST);

        return panel;
    }

    private JPanel createToolbarPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        StyledButton addButton = new StyledButton("Add Record", StyledButton.ButtonType.PRIMARY);
        StyledButton refreshButton = new StyledButton("Refresh", StyledButton.ButtonType.SECONDARY);

        refreshButton.addActionListener(e -> refreshData());

        panel.add(addButton);
        panel.add(refreshButton);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER, 1));

        String[] columns = {"Consumption ID", "Meter ID", "Consumption Value", "Reading Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        consumptionTable = new StyledTable(tableModel);
        consumptionTable.setDateColumnRenderer(3);

        JScrollPane scrollPane = new JScrollPane(consumptionTable);
        scrollPane.setBorder(null);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public void refreshData() {
        SwingWorker<List<Consumption>, Void> worker = new SwingWorker<List<Consumption>, Void>() {
            @Override
            protected List<Consumption> doInBackground() {
                return consumptionCRUD.getAllRecords();
            }

            @Override
            protected void done() {
                try {
                    List<Consumption> records = get();
                    displayRecords(records);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(ConsumptionPanel.this,
                            "Error loading consumption records: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    private void displayRecords(List<Consumption> records) {
        tableModel.setRowCount(0);

        for (Consumption record : records) {
            tableModel.addRow(new Object[]{
                    record.consumptionID(),
                    record.meterID(),
                    String.format("%.2f", record.consumptionValue()),
                    record.readingDate()
            });
        }
    }
}