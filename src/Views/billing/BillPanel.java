package Views.billing;

import Views.components.*;
import Model.*;
import Services.BillService;
import config.AppConfig;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class BillPanel extends JPanel {

    private StyledTable billTable;
    private DefaultTableModel tableModel;
    private BillCRUD billCRUD;
    private CustomerCRUD customerCRUD;
    private Staff currentStaff;
    private SearchBar searchBar;
    private JComboBox<String> statusFilter;
    private BillService billService;

    public BillPanel(Staff staff) {
        this.currentStaff = staff;
        billCRUD = new BillCRUD();
        customerCRUD = new CustomerCRUD();
        billService = new BillService();
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
        JLabel titleLabel = new JLabel("Bill Management");
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

        StyledButton generateButton = new StyledButton("Generate Bill", StyledButton.ButtonType.PRIMARY);
        StyledButton viewButton = new StyledButton("View Details", StyledButton.ButtonType.SECONDARY);
        StyledButton deleteButton = new StyledButton("Delete", StyledButton.ButtonType.DANGER);
        StyledButton applyPenaltiesButton = new StyledButton("Apply Penalties", StyledButton.ButtonType.WARNING);
        StyledButton refreshButton = new StyledButton("Refresh", StyledButton.ButtonType.SECONDARY);

        statusFilter = new JComboBox<>(new String[]{"All", "UNPAID", "PAID", "OVERDUE", "PARTIALLY_PAID"});
        statusFilter.addActionListener(e -> applyFilter());

        searchBar = new SearchBar(250);
        searchBar.setPlaceholder("Search by Customer or Bill ID...");
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

        generateButton.addActionListener(e -> showGenerateBillDialog());
        viewButton.addActionListener(e -> viewBillDetails());
        deleteButton.addActionListener(e -> deleteBill());
        applyPenaltiesButton.addActionListener(e -> applyOverduePenalties());
        refreshButton.addActionListener(e -> refreshData());

        panel.add(generateButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(viewButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(deleteButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(applyPenaltiesButton);
        panel.add(Box.createHorizontalGlue());
        panel.add(new JLabel("Status:"));
        panel.add(Box.createHorizontalStrut(5));
        panel.add(statusFilter);
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

        String[] columns = {"Bill ID", "Customer", "Amount Due", "Due Date", "Status", "Generated By"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        billTable = new StyledTable(tableModel);
        billTable.setCurrencyColumnRenderer(2);
        billTable.setDateColumnRenderer(3);

        JScrollPane scrollPane = new JScrollPane(billTable);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    public void refreshData() {
        statusFilter.setSelectedIndex(0);
        searchBar.clearSearchText(); 
        
        SwingWorker<List<Bill>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Bill> doInBackground() {
                return billCRUD.getAllRecords();
            }

            @Override
            protected void done() {
                try {
                    displayBills(get());
                } catch (Exception e) { e.printStackTrace(); }
            }
        };
        worker.execute();
    }
    
    private void displayBills(List<Bill> bills) {
        tableModel.setRowCount(0);
        for (Bill bill : bills) {
            tableModel.addRow(new Object[]{
                    bill.billID(),
                    getCustomerName(bill.customerID()),
                    bill.amountDue(),
                    bill.dueDate(),
                    bill.status(),
                    "Staff ID: " + bill.generatedByStaffID()
            });
        }
    }
    
    private String getCustomerName(int customerID) {
        Customer customer = customerCRUD.getRecordById(customerID);
        return customer != null ? customer.firstName() + " " + customer.lastName() : "Unknown";
    }

    private void applyFilter() {
        String selectedStatus = (String) statusFilter.getSelectedItem();
        String searchText = searchBar.getSearchText().toLowerCase();

        SwingWorker<List<Bill>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Bill> doInBackground() {
                List<Bill> allBills = billCRUD.getAllRecords();

                return allBills.stream()
                        .filter(bill -> {
                            boolean statusMatch = "All".equals(selectedStatus) || bill.status().equalsIgnoreCase(selectedStatus);
                            
                            boolean searchMatch = searchText.isEmpty() ||
                                    getCustomerName(bill.customerID()).toLowerCase().contains(searchText) ||
                                    String.valueOf(bill.billID()).contains(searchText);

                            return statusMatch && searchMatch;
                        })
                        .collect(Collectors.toList());
            }

            @Override
            protected void done() {
                try {
                    displayBills(get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void showGenerateBillDialog() {
        BillGenerationDialog dialog = new BillGenerationDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                currentStaff
        );
        dialog.setVisible(true);

        if (dialog.isGenerated()) {
            refreshData();
        }
    }
    
    private void viewBillDetails() {
        int selectedRow = billTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a bill to view.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int billID = (int) tableModel.getValueAt(selectedRow, 0);
        Bill bill = billCRUD.getRecordById(billID);

        if (bill != null) {
            showBillDetailsDialog(bill);
        }
    }
    
    private void showBillDetailsDialog(Bill bill) {
        Customer customer = customerCRUD.getRecordById(bill.customerID());
        String details = String.format(
                "Bill ID: %d\n" +
                "Customer: %s %s (ID: %d)\n" +
                "Account Number: %s\n" +
                "Amount Due: ₱%.2f\n" +
                "Due Date: %s\n" +
                "Status: %s\n" +
                "Consumption ID: %d\n" +
                "Rate ID: %d\n" +
                "Generated By Staff ID: %d",
                bill.billID(),
                customer != null ? customer.firstName() : "Unknown",
                customer != null ? customer.lastName() : "",
                bill.customerID(),
                customer != null ? customer.accountNumber() : "Unknown",
                bill.amountDue(),
                bill.dueDate(),
                bill.status(),
                bill.consumptionID(),
                bill.rateID(),
                bill.generatedByStaffID()
        );

        JOptionPane.showMessageDialog(this, details, "Bill Details", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void deleteBill() {
        int selectedRow = billTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a bill to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int billID = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete Bill ID: " + billID + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = billCRUD.deleteRecord(billID);
            if (success) {
                JOptionPane.showMessageDialog(this, "Bill deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete bill.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void applyOverduePenalties() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "This will apply penalties to all applicable overdue bills.\n" +
                "Penalty Rate: " + (AppConfig.OVERDUE_PENALTY_RATE * 100) + "% of amount due\n\n" +
                "Continue?",
                "Apply Penalties",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        SwingWorker<Integer, Void> worker = new SwingWorker<>() {
            @Override
            protected Integer doInBackground() {
                return billService.applyPenaltiesToOverdueBills(currentStaff.staffID());
            }

            @Override
            protected void done() {
                try {
                    int count = get();
                    if (count > 0) {
                        JOptionPane.showMessageDialog(BillPanel.this, "Penalties applied successfully to " + count + " bill(s).", "Success", JOptionPane.INFORMATION_MESSAGE);
                        refreshData();
                    } else {
                        JOptionPane.showMessageDialog(BillPanel.this, "No overdue bills were found that required a penalty.", "Information", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(BillPanel.this, "An error occurred while applying penalties: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }
}