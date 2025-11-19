package Views.employee;

import Views.components.*;
import Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeePanel extends JPanel {

    private StyledTable employeeTable;
    private DefaultTableModel tableModel;
    private EmployeeCRUD employeeCRUD;
    private DepartmentCRUD departmentCRUD;
    private SearchBar searchBar;

    public EmployeePanel() {
        employeeCRUD = new EmployeeCRUD();
        departmentCRUD = new DepartmentCRUD();
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

        JLabel titleLabel = new JLabel("Employee Management");
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

        StyledButton addButton = new StyledButton("Add Employee", StyledButton.ButtonType.PRIMARY);
        StyledButton editButton = new StyledButton("Edit", StyledButton.ButtonType.SECONDARY);
        StyledButton deleteButton = new StyledButton("Delete", StyledButton.ButtonType.DANGER);
        StyledButton refreshButton = new StyledButton("Refresh", StyledButton.ButtonType.SECONDARY);

        searchBar = new SearchBar(250);
        searchBar.setPlaceholder("Search by name or position...");
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
        deleteButton.addActionListener(e -> deleteEmployee());
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

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER, 1));

        String[] columns = {"ID", "First Name", "Last Name", "Department", "Position", "Contact", "Hire Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employeeTable = new StyledTable(tableModel);
        employeeTable.setDateColumnRenderer(6);

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBorder(null);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public void refreshData() {
        if(searchBar != null) searchBar.clearSearchText();
        SwingWorker<List<Employee>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Employee> doInBackground() {
                return employeeCRUD.getAllRecords();
            }

            @Override
            protected void done() {
                try {
                    List<Employee> employees = get();
                    displayEmployees(employees);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(EmployeePanel.this,
                            "Error loading employees: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    private void displayEmployees(List<Employee> employees) {
        tableModel.setRowCount(0);

        for (Employee employee : employees) {
            String departmentName = getDepartmentName(employee.departmentID());

            tableModel.addRow(new Object[]{
                    employee.employeeID(),
                    employee.firstName(),
                    employee.lastName(),
                    departmentName,
                    employee.position(),
                    employee.contactNumber(),
                    employee.hireDate()
            });
        }
    }

    private String getDepartmentName(int departmentID) {
        Department dept = departmentCRUD.getRecordById(departmentID);
        return dept != null ? dept.departmentName() : "Unknown";
    }

    private void filterTable(String searchText) {
        SwingWorker<List<Employee>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Employee> doInBackground() {
                List<Employee> allEmployees = employeeCRUD.getAllRecords();
                if(searchText.isEmpty()) return allEmployees;
                return allEmployees.stream()
                        .filter(e -> e.firstName().toLowerCase().contains(searchText.toLowerCase()) ||
                                e.lastName().toLowerCase().contains(searchText.toLowerCase()) ||
                                e.position().toLowerCase().contains(searchText.toLowerCase()))
                        .collect(Collectors.toList());
            }

            @Override
            protected void done() {
                try {
                    List<Employee> filtered = get();
                    displayEmployees(filtered);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    private void showAddDialog() {
        EmployeeFormDialog dialog = new EmployeeFormDialog((Frame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            refreshData();
        }
    }

    private void showEditDialog() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select an employee to edit.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int employeeID = (int) tableModel.getValueAt(selectedRow, 0);
        Employee employee = employeeCRUD.getRecordById(employeeID);

        if (employee != null) {
            EmployeeFormDialog dialog = new EmployeeFormDialog((Frame) SwingUtilities.getWindowAncestor(this), employee);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                refreshData();
            }
        }
    }

    private void deleteEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select an employee to delete.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int employeeID = (int) tableModel.getValueAt(selectedRow, 0);
        String name = tableModel.getValueAt(selectedRow, 1) + " " + tableModel.getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete employee: " + name + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = employeeCRUD.deleteRecord(employeeID);
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Employee deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to delete employee. They may be associated with other records.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}