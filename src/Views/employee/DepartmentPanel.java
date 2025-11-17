package Views.employee;

import Views.components.*;
import Model.Department;
import Model.DepartmentCRUD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Department management panel
 */
public class DepartmentPanel extends JPanel {

    private StyledTable departmentTable;
    private DefaultTableModel tableModel;
    private DepartmentCRUD departmentCRUD;

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

        JPanel tablePanel = createTablePanel();

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel);
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
        SwingWorker<List<Department>, Void> worker = new SwingWorker<List<Department>, Void>() {
            @Override
            protected List<Department> doInBackground() {
                return departmentCRUD.getAllRecords();
            }

            @Override
            protected void done() {
                try {
                    List<Department> departments = get();
                    displayDepartments(departments);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(DepartmentPanel.this,
                            "Error loading departments: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
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
}