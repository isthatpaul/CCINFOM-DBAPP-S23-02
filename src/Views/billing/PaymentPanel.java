package Views.billing;

import Views.components.*;
import Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Payment Management Panel
 * Assigned to: SAMONTE, Joshua Carlos B.
 */
public class PaymentPanel extends JPanel {

    private StyledTable paymentTable;
    private DefaultTableModel tableModel;
    private PaymentCRUD paymentCRUD;
    private BillCRUD billCRUD;
    private CustomerCRUD customerCRUD;
    private Staff currentStaff;
    private SearchBar searchBar;

    public PaymentPanel(Staff staff) {
        this.currentStaff = staff;
        paymentCRUD = new PaymentCRUD();
        billCRUD = new BillCRUD();
        customerCRUD = new CustomerCRUD();
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

        JLabel titleLabel = new JLabel("Payment Management");
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

        StyledButton processButton = new StyledButton("Process Payment", StyledButton.ButtonType.PRIMARY);
        StyledButton viewButton = new StyledButton("View Details", StyledButton.ButtonType.SECONDARY);
        StyledButton deleteButton = new StyledButton("Delete", StyledButton.ButtonType.DANGER);
        StyledButton refreshButton = new StyledButton("Refresh", StyledButton.ButtonType.SECONDARY);

        searchBar = new SearchBar(250);
        searchBar.setPlaceholder("Search by Receipt Number or Bill ID...");
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

        processButton.addActionListener(e -> showProcessPaymentDialog());
        viewButton.addActionListener(e -> viewPaymentDetails());
        deleteButton.addActionListener(e -> deletePayment());
        refreshButton.addActionListener(e -> refreshData());

        panel.add(processButton);
        panel.add(viewButton);
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

        String[] columns = {"Payment ID", "Bill ID", "Customer", "Amount Paid", "Payment Date", "Method", "Receipt #", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        paymentTable = new StyledTable(tableModel);
        paymentTable.setCurrencyColumnRenderer(3);
        paymentTable.setDateColumnRenderer(4);

        JScrollPane scrollPane = new JScrollPane(paymentTable);
        scrollPane.setBorder(null);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public void refreshData() {
        SwingWorker<List<Payment>, Void> worker = new SwingWorker<List<Payment>, Void>() {
            @Override
            protected List<Payment> doInBackground() {
                return paymentCRUD.getAllRecords();
            }

            @Override
            protected void done() {
                try {
                    List<Payment> payments = get();
                    displayPayments(payments);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(PaymentPanel.this,
                            "Error loading payments: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    private void displayPayments(List<Payment> payments) {
        tableModel.setRowCount(0);

        for (Payment payment : payments) {
            String customerName = getCustomerNameFromBill(payment.billID());

            tableModel.addRow(new Object[]{
                    payment.paymentID(),
                    payment.billID(),
                    customerName,
                    payment.amountPaid(),
                    payment.paymentDate(),
                    payment.paymentMethod(),
                    payment.receiptNumber(),
                    payment.status()
            });
        }
    }

    private String getCustomerNameFromBill(int billID) {
        Bill bill = billCRUD.getRecordById(billID);
        if (bill != null) {
            Customer customer = customerCRUD.getRecordById(bill.customerID());
            if (customer != null) {
                return customer.firstName() + " " + customer.lastName();
            }
        }
        return "Unknown";
    }

    private void filterTable(String searchText) {
        SwingWorker<List<Payment>, Void> worker = new SwingWorker<List<Payment>, Void>() {
            @Override
            protected List<Payment> doInBackground() {
                List<Payment> allPayments = paymentCRUD.getAllRecords();
                return allPayments.stream()
                        .filter(p -> p.receiptNumber().toLowerCase().contains(searchText.toLowerCase()) ||
                                String.valueOf(p.billID()).contains(searchText))
                        .collect(Collectors.toList());
            }

            @Override
            protected void done() {
                try {
                    List<Payment> filtered = get();
                    displayPayments(filtered);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    private void showProcessPaymentDialog() {
        PaymentFormDialog dialog = new PaymentFormDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                currentStaff,
                null
        );
        dialog.setVisible(true);

        if (dialog.isProcessed()) {
            refreshData();
        }
    }

    private void viewPaymentDetails() {
        int selectedRow = paymentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a payment to view.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int paymentID = (int) tableModel.getValueAt(selectedRow, 0);
        Payment payment = paymentCRUD.getRecordById(paymentID);

        if (payment != null) {
            showPaymentDetailsDialog(payment);
        }
    }

    private void showPaymentDetailsDialog(Payment payment) {
        Bill bill = billCRUD.getRecordById(payment.billID());
        Customer customer = null;
        if (bill != null) {
            customer = customerCRUD.getRecordById(bill.customerID());
        }

        String details = String.format(
                "Payment ID: %d\n" +
                "Bill ID: %d\n" +
                "Customer: %s %s\n" +
                "Amount Paid: ₱%.2f\n" +
                "Payment Date: %s\n" +
                "Payment Method: %s\n" +
                "Receipt Number: %s\n" +
                "Processed By Staff ID: %d\n" +
                "Status: %s",
                payment.paymentID(),
                payment.billID(),
                customer != null ? customer.firstName() : "Unknown",
                customer != null ? customer.lastName() : "",
                payment.amountPaid(),
                payment.paymentDate(),
                payment.paymentMethod(),
                payment.receiptNumber(),
                payment.processedByStaffID(),
                payment.status()
        );

        JOptionPane.showMessageDialog(this,
                details,
                "Payment Details",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void deletePayment() {
        int selectedRow = paymentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a payment to delete.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int paymentID = (int) tableModel.getValueAt(selectedRow, 0);
        String receiptNumber = (String) tableModel.getValueAt(selectedRow, 6);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete Payment (Receipt: " + receiptNumber + ")?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = paymentCRUD.deleteRecord(paymentID);
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Payment deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to delete payment.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}