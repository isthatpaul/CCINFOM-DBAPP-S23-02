package Views.meter;

import Views.components.*;
import Model.Meter;
import Model.MeterCRUD;
import Model.UtilityType;
import Model.UtilityTypeCRUD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Meter Management Panel
 * Assigned to: LUIS, Kamillu Raphael C.
 */
public class MeterPanel extends JPanel {

    private StyledTable meterTable;
    private DefaultTableModel tableModel;
    private MeterCRUD meterCRUD;
    private UtilityTypeCRUD utilityTypeCRUD;
    private SearchBar searchBar;

    public MeterPanel() {
        meterCRUD = new MeterCRUD();
        utilityTypeCRUD = new UtilityTypeCRUD();
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

        JLabel titleLabel = new JLabel("Meter Management");
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

        StyledButton addButton = new StyledButton("Add Meter", StyledButton.ButtonType.PRIMARY);
        StyledButton editButton = new StyledButton("Edit", StyledButton.ButtonType.SECONDARY);
        StyledButton deleteButton = new StyledButton("Delete", StyledButton.ButtonType.DANGER);
        StyledButton refreshButton = new StyledButton("Refresh", StyledButton.ButtonType.SECONDARY);

        searchBar = new SearchBar(250);
        searchBar.setPlaceholder("Search by serial number or status...");
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

        addButton.addActionListener(e -> showAddDialog());
        editButton.addActionListener(e -> showEditDialog());
        deleteButton.addActionListener(e -> deleteMeter());
        refreshButton.addActionListener(e -> refreshData());

        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(new JLabel("Search:"));
        panel.add(searchBar);
        panel.add(refreshButton);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER, 1));

        String[] columns = {"Meter ID", "Utility Type", "Serial Number", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        meterTable = new StyledTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(meterTable);
        scrollPane.setBorder(null);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public void refreshData() {
        SwingWorker<List<Meter>, Void> worker = new SwingWorker<List<Meter>, Void>() {
            @Override
            protected List<Meter> doInBackground() {
                return meterCRUD.getAllRecords();
            }

            @Override
            protected void done() {
                try {
                    List<Meter> meters = get();
                    displayMeters(meters);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(MeterPanel.this,
                            "Error loading meters: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    private void displayMeters(List<Meter> meters) {
        tableModel.setRowCount(0);

        for (Meter meter : meters) {
            String utilityTypeName = getUtilityTypeName(meter.utilityTypeID());
            tableModel.addRow(new Object[]{
                    meter.meterID(),
                    utilityTypeName,
                    meter.meterSerialNumber(),
                    meter.meterStatus()
            });
        }
    }

    private String getUtilityTypeName(int utilityTypeID) {
        UtilityType type = utilityTypeCRUD.getRecordById(utilityTypeID);
        return type != null ? type.utilityTypeName() : "Unknown";
    }

    private void filterTable(String searchText) {
        SwingWorker<List<Meter>, Void> worker = new SwingWorker<List<Meter>, Void>() {
            @Override
            protected List<Meter> doInBackground() {
                List<Meter> allMeters = meterCRUD.getAllRecords();
                return allMeters.stream()
                        .filter(m -> m.meterSerialNumber().toLowerCase().contains(searchText.toLowerCase()) ||
                                m.meterStatus().toLowerCase().contains(searchText.toLowerCase()))
                        .collect(Collectors.toList());
            }

            @Override
            protected void done() {
                try {
                    List<Meter> filtered = get();
                    displayMeters(filtered);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    private void showAddDialog() {
        MeterFormDialog dialog = new MeterFormDialog((Frame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            refreshData();
        }
    }

    private void showEditDialog() {
        int selectedRow = meterTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a meter to edit.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int meterID = (int) tableModel.getValueAt(selectedRow, 0);
        Meter meter = meterCRUD.getRecordById(meterID);

        if (meter != null) {
            MeterFormDialog dialog = new MeterFormDialog((Frame) SwingUtilities.getWindowAncestor(this), meter);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                refreshData();
            }
        }
    }

    private void deleteMeter() {
        int selectedRow = meterTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a meter to delete.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int meterID = (int) tableModel.getValueAt(selectedRow, 0);
        String serialNumber = (String) tableModel.getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete meter: " + serialNumber + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = meterCRUD.deleteRecord(meterID);
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Meter deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to delete meter. It may be assigned to a customer.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}