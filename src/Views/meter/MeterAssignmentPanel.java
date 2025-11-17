package Views.meter;

import Views.components.*;
import Model.*;
import Services.MeterAssignmentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Meter Assignment Panel
 * Assigned to: LUIS, Kamillu Raphael C.
 */
public class MeterAssignmentPanel extends JPanel {

    private StyledTable assignmentTable;
    private DefaultTableModel tableModel;
    private MeterAssignmentCRUD assignmentCRUD;
    private CustomerCRUD customerCRUD;
    private MeterCRUD meterCRUD;
    private MeterAssignmentService assignmentService;
    private SearchBar searchBar;
    private JComboBox<String> statusFilter;

    public MeterAssignmentPanel() {
        assignmentCRUD = new MeterAssignmentCRUD();
        customerCRUD = new CustomerCRUD();
        meterCRUD = new MeterCRUD();
        assignmentService = new MeterAssignmentService();
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

        JLabel titleLabel = new JLabel("Meter Assignment Management");
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

        StyledButton assignButton = new StyledButton("Assign Meter", StyledButton.ButtonType.PRIMARY);
        StyledButton updateButton = new StyledButton("Update", StyledButton.ButtonType.SECONDARY);
        StyledButton deleteButton = new StyledButton("Remove Assignment", StyledButton.ButtonType.DANGER);
        StyledButton refreshButton = new StyledButton("Refresh", StyledButton.ButtonType.SECONDARY);

        statusFilter = new JComboBox<>(new String[]{"All", "ACTIVE", "INACTIVE"});
        statusFilter.addActionListener(e -> applyFilter());

        searchBar = new SearchBar(250);
        searchBar.setPlaceholder("Search by Customer or Meter ID...");
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

        assignButton.addActionListener(e -> showAssignMeterDialog());
        updateButton.addActionListener(e -> showUpdateDialog());
        deleteButton.addActionListener(e -> removeAssignment());
        refreshButton.addActionListener(e -> refreshData());

        panel.add(assignButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(new JLabel("Status:"));
        panel.add(statusFilter);
        panel.add(new JLabel("Search:"));
        panel.add(searchBar);
        panel.add(refreshButton);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER, 1));

        String[] columns = {"Assignment ID", "Customer", "Meter Serial", "Assignment Date", "Installation Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        assignmentTable = new StyledTable(tableModel);
        assignmentTable.setDateColumnRenderer(3);
        assignmentTable.setDateColumnRenderer(4);

        JScrollPane scrollPane = new JScrollPane(assignmentTable);
        scrollPane.setBorder(null);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public void refreshData() {
        SwingWorker<List<MeterAssignment>, Void> worker = new SwingWorker<List<MeterAssignment>, Void>() {
            @Override
            protected List<MeterAssignment> doInBackground() {
                return assignmentCRUD.getAllRecords();
            }

            @Override
            protected void done() {
                try {
                    List<MeterAssignment> assignments = get();
                    displayAssignments(assignments);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(MeterAssignmentPanel.this,
                            "Error loading assignments: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    private void displayAssignments(List<MeterAssignment> assignments) {
        tableModel.setRowCount(0);

        for (MeterAssignment assignment : assignments) {
            String customerName = getCustomerName(assignment.customerID());
            String meterSerial = getMeterSerial(assignment.meterID());

            tableModel.addRow(new Object[]{
                    assignment.assignmentID(),
                    customerName,
                    meterSerial,
                    assignment.assignmentDate(),
                    assignment.installationDate(),
                    assignment.status()
            });
        }
    }

    private String getCustomerName(int customerID) {
        Customer customer = customerCRUD.getRecordById(customerID);
        if (customer != null) {
            return customer.firstName() + " " + customer.lastName() + " (ID: " + customerID + ")";
        }
        return "Unknown";
    }

    private String getMeterSerial(int meterID) {
        Meter meter = meterCRUD.getRecordById(meterID);
        return meter != null ? meter.meterSerialNumber() : "Unknown";
    }

    private void applyFilter() {
        String selectedStatus = (String) statusFilter.getSelectedItem();
        String searchText = searchBar.getSearchText();

        SwingWorker<List<MeterAssignment>, Void> worker = new SwingWorker<List<MeterAssignment>, Void>() {
            @Override
            protected List<MeterAssignment> doInBackground() {
                List<MeterAssignment> allAssignments = assignmentCRUD.getAllRecords();

                return allAssignments.stream()
                        .filter(assignment -> {
                            boolean statusMatch = "All".equals(selectedStatus) || assignment.status().equals(selectedStatus);
                            boolean searchMatch = searchText.isEmpty() ||
                                    String.valueOf(assignment.customerID()).contains(searchText) ||
                                    String.valueOf(assignment.meterID()).contains(searchText);
                            return statusMatch && searchMatch;
                        })
                        .collect(Collectors.toList());
            }

            @Override
            protected void done() {
                try {
                    List<MeterAssignment> filtered = get();
                    displayAssignments(filtered);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    private void showAssignMeterDialog() {
        MeterAssignmentFormDialog dialog = new MeterAssignmentFormDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                null
        );
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            refreshData();
        }
    }

    private void showUpdateDialog() {
        int selectedRow = assignmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select an assignment to update.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int assignmentID = (int) tableModel.getValueAt(selectedRow, 0);
        MeterAssignment assignment = assignmentCRUD.getRecordById(assignmentID);

        if (assignment != null) {
            MeterAssignmentFormDialog dialog = new MeterAssignmentFormDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    assignment
            );
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                refreshData();
            }
        }
    }

    private void removeAssignment() {
        int selectedRow = assignmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select an assignment to remove.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int assignmentID = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove this meter assignment?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = assignmentService.deleteAssignment(assignmentID);
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Assignment removed successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to remove assignment.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}