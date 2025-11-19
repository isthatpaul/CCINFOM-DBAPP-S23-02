package Views.employee;

import Views.components.*;
import Model.Department;
import Model.DepartmentCRUD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class DepartmentPanel extends JPanel {

    private StyledTable departmentTable;
    private DefaultTableModel tableModel;
    private DepartmentCRUD departmentCRUD;
    private SearchBar searchBar;

    public DepartmentPanel() {
        departmentCRUD = new DepartmentCRUD();
        setLayout(new BorderLayout());
        setBackground(ColorScheme.BACKGROUND);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(ColorScheme.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel titleLabel = new JLabel("Department Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);

        JPanel toolbarPanel = createToolbarPanel();
        JPanel tablePanel = createTablePanel();
        
        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setBackground(ColorScheme.BACKGROUND);
        centerPanel.add(toolbarPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
    }
    
    private JPanel createToolbarPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        StyledButton addButton = new StyledButton("Add", StyledButton.ButtonType.PRIMARY);
        StyledButton editButton = new StyledButton("Edit", StyledButton.ButtonType.SECONDARY);
        StyledButton deleteButton = new StyledButton("Delete", StyledButton.ButtonType.DANGER);
        StyledButton refreshButton = new StyledButton("Refresh", StyledButton.ButtonType.SECONDARY);

        addButton.addActionListener(e -> showAddDialog());
        editButton.addActionListener(e -> showEditDialog());
        deleteButton.addActionListener(e -> deleteDepartment());
        refreshButton.addActionListener(e -> refreshData());

        searchBar = new SearchBar(250);
        searchBar.setPlaceholder("Search by department name...");
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

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER, 1));

        String[] columns = {"Department ID", "Department Name", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        departmentTable = new StyledTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(departmentTable);
        scrollPane.setBorder(null);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public void refreshData() {
        if (searchBar != null) {
            searchBar.clearSearchText();
        }
        
        SwingWorker<List<Department>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Department> doInBackground() {
                return departmentCRUD.getAllRecords();
            }

            @Override
            protected void done() {
                try {
                    displayDepartments(get());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(DepartmentPanel.this, "Error loading departments: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void displayDepartments(List<Department> departments) {
        tableModel.setRowCount(0);
        for (Department dept : departments) {
            tableModel.addRow(new Object[]{
                    dept.departmentID(),
                    dept.departmentName(),
                    dept.description()
            });
        }
    }
    
    private void filterTable(String searchText) {
        SwingWorker<List<Department>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Department> doInBackground() {
                List<Department> allDepts = departmentCRUD.getAllRecords();
                if(searchText.isEmpty()) return allDepts;
                String lowerCaseText = searchText.toLowerCase();
                return allDepts.stream()
                        .filter(d -> d.departmentName().toLowerCase().contains(lowerCaseText))
                        .collect(Collectors.toList());
            }

            @Override
            protected void done() {
                try {
                    displayDepartments(get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }
    
    private void showAddDialog() {
        // Assuming a DepartmentFormDialog will be created. For now, show info.
        JOptionPane.showMessageDialog(this, "Adding a new department is not yet implemented.", "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showEditDialog() {
        int selectedRow = departmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a department to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Placeholder for edit functionality
        JOptionPane.showMessageDialog(this, "Editing departments is not yet implemented.", "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteDepartment() {
        int selectedRow = departmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a department to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int departmentID = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete department: " + name + "?\nThis may affect associated employees.",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = departmentCRUD.deleteRecord(departmentID);
            if (success) {
                JOptionPane.showMessageDialog(this, "Department deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete department. It may be referenced by employees.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}