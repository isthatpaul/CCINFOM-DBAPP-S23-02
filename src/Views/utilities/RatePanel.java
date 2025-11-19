package Views.utilities;

import Views.components.*;
import Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class RatePanel extends JPanel {

    private StyledTable rateTable;
    private DefaultTableModel tableModel;
    private RateCRUD rateCRUD;
    private UtilityTypeCRUD utilityTypeCRUD;
    private SearchBar searchBar;
    private JComboBox<UtilityTypeFilterItem> utilityTypeFilter;

    public RatePanel() {
        rateCRUD = new RateCRUD();
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

        JLabel titleLabel = new JLabel("Rate Management");
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

        StyledButton addButton = new StyledButton("Add Rate", StyledButton.ButtonType.PRIMARY);
        StyledButton editButton = new StyledButton("Edit", StyledButton.ButtonType.SECONDARY);
        StyledButton deleteButton = new StyledButton("Delete", StyledButton.ButtonType.DANGER);
        StyledButton refreshButton = new StyledButton("Refresh", StyledButton.ButtonType.SECONDARY);

        utilityTypeFilter = new JComboBox<>();
        utilityTypeFilter.addItem(new UtilityTypeFilterItem(null, "All Utility Types"));
        loadUtilityTypeFilter();
        utilityTypeFilter.addActionListener(e -> applyFilter());

        searchBar = new SearchBar(200);
        searchBar.setPlaceholder("Search by rate...");
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
        deleteButton.addActionListener(e -> deleteRate());
        refreshButton.addActionListener(e -> refreshData());

        panel.add(addButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(editButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(deleteButton);
        panel.add(Box.createHorizontalGlue());
        panel.add(new JLabel("Filter:"));
        panel.add(Box.createHorizontalStrut(5));
        panel.add(utilityTypeFilter);
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

        String[] columns = {"Rate ID", "Utility Type", "Rate Per Unit", "Effective Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        rateTable = new StyledTable(tableModel);
        rateTable.setCurrencyColumnRenderer(2);
        rateTable.setDateColumnRenderer(3);

        JScrollPane scrollPane = new JScrollPane(rateTable);
        scrollPane.setBorder(null);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadUtilityTypeFilter() {
        List<UtilityType> types = utilityTypeCRUD.getAllRecords();
        for (UtilityType type : types) {
            if (type.isActive()) {
                utilityTypeFilter.addItem(new UtilityTypeFilterItem(type.utilityTypeID(), type.utilityTypeName()));
            }
        }
    }

    public void refreshData() {
        if(searchBar != null) searchBar.clearSearchText();
        if(utilityTypeFilter != null) utilityTypeFilter.setSelectedIndex(0);

        SwingWorker<List<Rate>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Rate> doInBackground() {
                return rateCRUD.getAllRecords();
            }

            @Override
            protected void done() {
                try {
                    List<Rate> rates = get();
                    displayRates(rates);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(RatePanel.this,
                            "Error loading rates: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    private void displayRates(List<Rate> rates) {
        tableModel.setRowCount(0);

        for (Rate rate : rates) {
            String utilityTypeName = getUtilityTypeName(rate.utilityTypeID());

            tableModel.addRow(new Object[]{
                    rate.rateID(),
                    utilityTypeName,
                    rate.ratePerUnit(),
                    rate.effectiveDate()
            });
        }
    }

    private String getUtilityTypeName(Integer utilityTypeID) {
        if (utilityTypeID == null) {
            return "N/A";
        }
        UtilityType type = utilityTypeCRUD.getRecordById(utilityTypeID);
        return type != null ? type.utilityTypeName() : "Unknown";
    }

    private void applyFilter() {
        UtilityTypeFilterItem selectedFilter = (UtilityTypeFilterItem) utilityTypeFilter.getSelectedItem();
        String searchText = searchBar.getSearchText();

        SwingWorker<List<Rate>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Rate> doInBackground() {
                List<Rate> allRates = rateCRUD.getAllRecords();

                return allRates.stream()
                        .filter(rate -> {
                            boolean typeMatch = selectedFilter == null || 
                                              selectedFilter.getId() == null ||
                                              (rate.utilityTypeID() != null && 
                                               rate.utilityTypeID().equals(selectedFilter.getId()));

                            boolean searchMatch = searchText.isEmpty() ||
                                    String.valueOf(rate.ratePerUnit()).contains(searchText);

                            return typeMatch && searchMatch;
                        })
                        .collect(Collectors.toList());
            }

            @Override
            protected void done() {
                try {
                    List<Rate> filtered = get();
                    displayRates(filtered);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    private void showAddDialog() {
        RateFormDialog dialog = new RateFormDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                null
        );
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            refreshData();
        }
    }

    private void showEditDialog() {
        int selectedRow = rateTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a rate to edit.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int rateID = (int) tableModel.getValueAt(selectedRow, 0);
        Rate rate = rateCRUD.getRecordById(rateID);

        if (rate != null) {
            RateFormDialog dialog = new RateFormDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    rate
            );
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                refreshData();
            }
        }
    }

    private void deleteRate() {
        int selectedRow = rateTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a rate to delete.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int rateID = (int) tableModel.getValueAt(selectedRow, 0);
        String utilityType = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete rate for " + utilityType + "?\n" +
                "This may affect bill calculations.",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = rateCRUD.deleteRecord(rateID);
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Rate deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to delete rate. It may be referenced by bills.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class UtilityTypeFilterItem {
        private Integer id;
        private String name;

        public UtilityTypeFilterItem(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}