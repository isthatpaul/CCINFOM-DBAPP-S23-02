package Views.billing;

import Views.components.*;
import Model.Consumption;
import Model.ConsumptionCRUD;
import Model.Staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Consumption management panel
 */
public class ConsumptionPanel extends JPanel {

    private StyledTable consumptionTable;
    private DefaultTableModel tableModel;
    private ConsumptionCRUD consumptionCRUD;
    private Staff currentStaff;
    private SearchBar searchBar;

    public ConsumptionPanel(Staff staff) {
        consumptionCRUD = new ConsumptionCRUD();
        this.currentStaff = staff;
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
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        StyledButton addButton = new StyledButton("Add Record", StyledButton.ButtonType.PRIMARY);
        StyledButton editButton = new StyledButton("Edit", StyledButton.ButtonType.SECONDARY);
        StyledButton deleteButton = new StyledButton("Delete", StyledButton.ButtonType.DANGER);
        StyledButton refreshButton = new StyledButton("Refresh", StyledButton.ButtonType.SECONDARY);
        
        searchBar = new SearchBar(250);
        searchBar.setPlaceholder("Search by Meter ID...");
        searchBar.setSearchListener(new SearchBar.SearchListener() {
            @Override
            public void onSearch(String searchText) {
                filterTable(searchText);
            }

            @Override
            public void onClear() {
                refreshData();
            }
        });

        addButton.addActionListener(e -> openAddConsumptionDialog());
        editButton.addActionListener(e -> showEditDialog());
        deleteButton.addActionListener(e -> deleteConsumption());
        refreshButton.addActionListener(e -> refreshData());

        panel.add(addButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(editButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(deleteButton);
        panel.add(Box.createHorizontalGlue());
        panel.add(new JLabel("Search:"));
        panel.add(Box.createHorizontalStrut(5));
        panel.add(searchBar);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(refreshButton);

        return panel;
    }
    
    private void openAddConsumptionDialog() {
        // This dialog is likely for adding only, as it doesn't take a record to edit.
        AddConsumptionDialog dialog = new AddConsumptionDialog((Frame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
        if (dialog.isRecordAdded()) {
            refreshData(); 
        }
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
        consumptionTable.setCurrencyColumnRenderer(2); 

        JScrollPane scrollPane = new JScrollPane(consumptionTable);
        scrollPane.setBorder(null);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public void refreshData() {
        if (searchBar != null) {
            searchBar.clearSearchText();
        }

        SwingWorker<List<Consumption>, Void> worker = new SwingWorker<>() {
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
                    record.consumptionValue(), 
                    record.readingDate()
            });
        }
    }

    private void filterTable(String searchText) {
        SwingWorker<List<Consumption>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Consumption> doInBackground() {
                List<Consumption> allRecords = consumptionCRUD.getAllRecords();
                if (searchText == null || searchText.trim().isEmpty()) {
                    return allRecords;
                }
                String lowerCaseText = searchText.toLowerCase();
                return allRecords.stream()
                        .filter(c -> String.valueOf(c.meterID()).contains(lowerCaseText))
                        .collect(Collectors.toList());
            }

            @Override
            protected void done() {
                try {
                    displayRecords(get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void showEditDialog() {
        // Since there's no edit dialog, inform the user.
        JOptionPane.showMessageDialog(this, "Editing consumption records is not yet implemented.", "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteConsumption() {
        int selectedRow = consumptionTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a consumption record to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int consumptionID = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete consumption record ID: " + consumptionID + "?\nThis could affect existing bills.",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = consumptionCRUD.deleteRecord(consumptionID);
            if (success) {
                JOptionPane.showMessageDialog(this, "Consumption record deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete record. It may be associated with a bill.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}