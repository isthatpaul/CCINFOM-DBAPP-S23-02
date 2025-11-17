package Views.employee;

import Views.components.*;
import Model.Staff;
import Model.StaffCRUD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Staff management panel
 */
public class StaffPanel extends JPanel {

    private StyledTable staffTable;
    private DefaultTableModel tableModel;
    private StaffCRUD staffCRUD;

    public StaffPanel() {
        staffCRUD = new StaffCRUD();
        setLayout(new BorderLayout());
        setBackground(ColorScheme.BACKGROUND);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(ColorScheme.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel titleLabel = new JLabel("Staff Management");
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

        String[] columns = {"Staff ID", "Employee ID", "Username", "Role", "Branch"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        staffTable = new StyledTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(staffTable);
        scrollPane.setBorder(null);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public void refreshData() {
        SwingWorker<List<Staff>, Void> worker = new SwingWorker<List<Staff>, Void>() {
            @Override
            protected List<Staff> doInBackground() {
                return staffCRUD.getAllRecords();
            }

            @Override
            protected void done() {
                try {
                    List<Staff> staffList = get();
                    displayStaff(staffList);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(StaffPanel.this,
                            "Error loading staff: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    private void displayStaff(List<Staff> staffList) {
        tableModel.setRowCount(0);

        for (Staff staff : staffList) {
            tableModel.addRow(new Object[]{
                    staff.staffID(),
                    staff.employeeID(),
                    staff.username(),
                    staff.role(),
                    staff.assignedBranch()
            });
        }
    }
}