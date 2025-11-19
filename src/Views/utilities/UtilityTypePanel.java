package Views.utilities;

import Views.components.*;
import Model.UtilityType;
import Model.UtilityTypeCRUD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class UtilityTypePanel extends JPanel {

    private StyledTable utilityTypeTable;
    private DefaultTableModel tableModel;
    private UtilityTypeCRUD utilityTypeCRUD;
    private SearchBar searchBar;
    private JComboBox<String> activeFilter;

    public UtilityTypePanel() {
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

        JLabel titleLabel = new JLabel("Utility Type Management");
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

        StyledButton addButton = new StyledButton("Add Utility Type", StyledButton.ButtonType.PRIMARY);
        StyledButton editButton = new StyledButton("Edit", StyledButton.ButtonType.SECONDARY);
        StyledButton deleteButton = new StyledButton("Delete", StyledButton.ButtonType.DANGER);
        StyledButton refreshButton = new StyledButton("Refresh", StyledButton.ButtonType.SECONDARY);

        activeFilter = new JComboBox<>(new String[]{"All", "Active Only", "Inactive Only"});
        activeFilter.addActionListener(e -> applyFilter());

        searchBar = new SearchBar(250);
        searchBar.setPlaceholder("Search by name or unit...");
        searchBar.setSearchListener(new SearchBar.SearchListener() {
            @Override
            public void onSearch(String searchText) {
                applyFilter();
            }

            @Override
            public void onClear() {
                refreshData();
            }
        });

        addButton.addActionListener(e -> showAddDialog());
        editButton.addActionListener(e -> showEditDialog());
        deleteButton.addActionListener(e -> deleteUtilityType());
        refreshButton.addActionListener(e -> refreshData());

        panel.add(addButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(editButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(deleteButton);
        panel.add(Box.createHorizontalGlue());
        panel.add(new JLabel("Filter:"));
        panel.add(Box.createHorizontalStrut(5));
        panel.add(activeFilter);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(new JLabel("Search:"));
        panel.add(Box.createHorizontalStrut(5));
        panel.add(searchBar);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(refreshButton);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER, 1));

        String[] columns = {"ID", "Utility Type", "Unit of Measure", "Description", "Created Date", "Active"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 5) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };

        utilityTypeTable = new StyledTable(tableModel);
        utilityTypeTable.setDateColumnRenderer(4);

        JScrollPane scrollPane = new JScrollPane(utilityTypeTable);
        scrollPane.setBorder(null);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public void refreshData() {
        if(searchBar != null) searchBar.clearSearchText();
        if(activeFilter != null) activeFilter.setSelectedIndex(0);
        
        SwingWorker<List<UtilityType>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<UtilityType> doInBackground() {
                return utilityTypeCRUD.getAllRecords();
            }

            @Override
            protected void done() {
                try {
                    List<UtilityType> utilityTypes = get();
                    displayUtilityTypes(utilityTypes);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(UtilityTypePanel.this,
                            "Error loading utility types: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    private void displayUtilityTypes(List<UtilityType> utilityTypes) {
        tableModel.setRowCount(0);

        for (UtilityType type : utilityTypes) {
            tableModel.addRow(new Object[]{
                    type.utilityTypeID(),
                    type.utilityTypeName(),
                    type.unitOfMeasure(),
                    type.description(),
                    type.createdDate(),
                    type.isActive()
            });
        }
    }

    private void applyFilter() {
        String selectedFilter = (String) activeFilter.getSelectedItem();
        String searchText = searchBar.getSearchText();

        SwingWorker<List<UtilityType>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<UtilityType> doInBackground() {
                List<UtilityType> allTypes = utilityTypeCRUD.getAllRecords();

                return allTypes.stream()
                        .filter(type -> {
                            // Active filter
                            boolean activeMatch = true;
                            if ("Active Only".equals(selectedFilter)) {
                                activeMatch = type.isActive();
                            } else if ("Inactive Only".equals(selectedFilter)) {
                                activeMatch = !type.isActive();
                            }

                            // Search filter
                            boolean searchMatch = searchText.isEmpty() ||
                                    type.utilityTypeName().toLowerCase().contains(searchText.toLowerCase()) ||
                                    type.unitOfMeasure().toLowerCase().contains(searchText.toLowerCase());

                            return activeMatch && searchMatch;
                        })
                        .collect(Collectors.toList());
            }

            @Override
            protected void done() {
                try {
                    List<UtilityType> filtered = get();
                    displayUtilityTypes(filtered);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    private void showAddDialog() {
        UtilityTypeFormDialog dialog = new UtilityTypeFormDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                null
        );
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            refreshData();
        }
    }

    private void showEditDialog() {
        int selectedRow = utilityTypeTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a utility type to edit.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int utilityTypeID = (int) tableModel.getValueAt(selectedRow, 0);
        UtilityType utilityType = utilityTypeCRUD.getRecordById(utilityTypeID);

        if (utilityType != null) {
            UtilityTypeFormDialog dialog = new UtilityTypeFormDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    utilityType
            );
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                refreshData();
            }
        }
    }

    private void deleteUtilityType() {
        int selectedRow = utilityTypeTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a utility type to delete.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int utilityTypeID = (int) tableModel.getValueAt(selectedRow, 0);
        String typeName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete utility type: " + typeName + "?\n" +
                "This may affect related meters and rates.",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = utilityTypeCRUD.deleteRecord(utilityTypeID);
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Utility type deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to delete utility type. It may be referenced by other records.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}