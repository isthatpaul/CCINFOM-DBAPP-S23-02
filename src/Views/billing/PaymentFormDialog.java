package Views.billing;

import Views.components.*;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

/**
 * Dialog for processing payments (Transaction 3)
 * Assigned to: SAMONTE, Joshua Carlos B.
 */
public class PaymentFormDialog extends JDialog {

    private JComboBox<BillItem> billCombo;
    private JLabel customerLabel;
    private JLabel amountDueLabel;
    private JTextField amountPaidField;
    private DatePicker paymentDatePicker;
    private JComboBox<String> paymentMethodCombo;
    private JTextField receiptNumberField;
    private JButton generateReceiptButton;

    private BillCRUD billCRUD;
    private CustomerCRUD customerCRUD;
    private PaymentCRUD paymentCRUD;
    private Staff currentStaff;
    private Payment payment;
    private boolean processed = false;

    public PaymentFormDialog(Frame parent, Staff staff, Payment payment) {
        super(parent, payment == null ? "Process Payment" : "Edit Payment", true);
        this.currentStaff = staff;
        this.payment = payment;
        this.billCRUD = new BillCRUD();
        this.customerCRUD = new CustomerCRUD();
        this.paymentCRUD = new PaymentCRUD();
        initComponents();
        if (payment != null) {
            populateFields();
        }
    }

    private void initComponents() {
        setSize(550, 650);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.weightx = 0.3;

        int row = 0;

        // Bill Selection
        gbc.gridy = row;
        formPanel.add(createLabel("Select Bill:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        billCombo = new JComboBox<>();
        billCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loadUnpaidBills();
        billCombo.addActionListener(e -> updateBillInfo());
        formPanel.add(billCombo, gbc);

        // Customer (read-only)
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Customer:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        customerLabel = new JLabel();
        customerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(customerLabel, gbc);

        // Amount Due (read-only)
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Amount Due:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        amountDueLabel = new JLabel();
        amountDueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        amountDueLabel.setForeground(ColorScheme.ERROR);
        formPanel.add(amountDueLabel, gbc);

        // Amount Paid
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Amount Paid:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        amountPaidField = createTextField();
        formPanel.add(amountPaidField, gbc);

        // Payment Date
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Payment Date:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        paymentDatePicker = new DatePicker();
        formPanel.add(paymentDatePicker, gbc);

        // Payment Method
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Payment Method:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        paymentMethodCombo = new JComboBox<>(new String[]{"CASH", "ONLINE", "E-WALLET", "CHECK", "BANK_TRANSFER"});
        paymentMethodCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(paymentMethodCombo, gbc);

        // Receipt Number
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Receipt Number:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        receiptNumberField = createTextField();
        
        JPanel receiptPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        receiptPanel.setBackground(Color.WHITE);
        receiptPanel.add(receiptNumberField);
        
        generateReceiptButton = new JButton("Generate");
        generateReceiptButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        generateReceiptButton.addActionListener(e -> generateReceiptNumber());
        receiptPanel.add(generateReceiptButton);
        
        formPanel.add(receiptPanel, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, ColorScheme.BORDER));

        StyledButton processButton = new StyledButton(
                payment == null ? "Process Payment" : "Update Payment", 
                StyledButton.ButtonType.PRIMARY
        );
        processButton.addActionListener(e -> processPayment());

        StyledButton cancelButton = new StyledButton("Cancel", StyledButton.ButtonType.SECONDARY);
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(processButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Initialize bill info
        updateBillInfo();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(ColorScheme.TEXT_PRIMARY);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(15);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private void loadUnpaidBills() {
        List<Bill> bills = billCRUD.getAllRecords();
        billCombo.removeAllItems();
        
        for (Bill bill : bills) {
            if (!"PAID".equals(bill.status())) {
                billCombo.addItem(new BillItem(bill));
            }
        }
    }

    private void updateBillInfo() {
        BillItem selectedBill = (BillItem) billCombo.getSelectedItem();
        if (selectedBill != null) {
            Bill bill = selectedBill.getBill();
            Customer customer = customerCRUD.getRecordById(bill.customerID());
            
            if (customer != null) {
                customerLabel.setText(customer.firstName() + " " + customer.lastName() + 
                                     " (Account: " + customer.accountNumber() + ")");
            }
            
            amountDueLabel.setText(String.format("₱%.2f", bill.amountDue()));
        }
    }

    private void generateReceiptNumber() {
        String receipt = "RCP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        receiptNumberField.setText(receipt);
    }

    private void populateFields() {
        // Load bill and select it
        Bill bill = billCRUD.getRecordById(payment.billID());
        if (bill != null) {
            for (int i = 0; i < billCombo.getItemCount(); i++) {
                if (billCombo.getItemAt(i).getBill().billID() == bill.billID()) {
                    billCombo.setSelectedIndex(i);
                    break;
                }
            }
        }

        amountPaidField.setText(String.valueOf(payment.amountPaid()));
        paymentDatePicker.setSqlDate(payment.paymentDate());
        paymentMethodCombo.setSelectedItem(payment.paymentMethod());
        receiptNumberField.setText(payment.receiptNumber());
    }

    private void processPayment() {
        // Validation
        if (billCombo.getSelectedItem() == null) {
            showError("Please select a bill.");
            return;
        }
        if (amountPaidField.getText().trim().isEmpty()) {
            showError("Please enter amount paid.");
            return;
        }
        if (receiptNumberField.getText().trim().isEmpty()) {
            showError("Please enter or generate a receipt number.");
            return;
        }

        try {
            BillItem selectedBill = (BillItem) billCombo.getSelectedItem();
            Bill bill = selectedBill.getBill();
            
            double amountPaid = Double.parseDouble(amountPaidField.getText().trim());
            Date paymentDate = paymentDatePicker.getSqlDate();
            String paymentMethod = (String) paymentMethodCombo.getSelectedItem();
            String receiptNumber = receiptNumberField.getText().trim();

            // Validate amount
            if (amountPaid <= 0) {
                showError("Amount paid must be greater than zero.");
                return;
            }

            // Check receipt number uniqueness
            if (payment == null) { // Only check for new payments
                List<Payment> allPayments = paymentCRUD.getAllRecords();
                for (Payment p : allPayments) {
                    if (p.receiptNumber().equals(receiptNumber)) {
                        showError("Receipt number already exists. Please generate a new one.");
                        return;
                    }
                }
            }

            // Create/Update Payment record
            int paymentID = (payment != null) ? payment.paymentID() : 0;
            Payment newPayment = new Payment(
                    paymentID,
                    bill.billID(),
                    paymentDate,
                    amountPaid,
                    paymentMethod,
                    receiptNumber,
                    currentStaff.staffID(),
                    0, // No collector ID
                    "COMPLETED"
            );

            boolean success;
            if (payment == null) {
                success = paymentCRUD.addRecord(newPayment);
            } else {
                success = paymentCRUD.updateRecord(newPayment);
            }

            if (success) {
                // Update Bill Status
                updateBillStatus(bill, amountPaid);
                
                processed = true;
                JOptionPane.showMessageDialog(this,
                        "Payment processed successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                showError("Failed to process payment.");
            }

        } catch (NumberFormatException e) {
            showError("Invalid amount: " + e.getMessage());
        } catch (Exception e) {
            showError("Error processing payment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateBillStatus(Bill bill, double amountPaid) {
        double newAmountDue = bill.amountDue() - amountPaid;
        String newStatus;

        if (newAmountDue <= 0) {
            newStatus = "PAID";
            newAmountDue = 0;
        } else if (newAmountDue < bill.amountDue()) {
            newStatus = "PARTIALLY_PAID";
        } else {
            newStatus = bill.status();
        }

        Bill updatedBill = new Bill(
                bill.billID(),
                bill.customerID(),
                bill.consumptionID(),
                bill.rateID(),
                newAmountDue,
                bill.dueDate(),
                newStatus,
                bill.generatedByStaffID(),
                bill.technicianID()
        );

        billCRUD.updateRecord(updatedBill);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public boolean isProcessed() {
        return processed;
    }

    // Helper class
    private static class BillItem {
        private Bill bill;

        public BillItem(Bill bill) {
            this.bill = bill;
        }

        public Bill getBill() {
            return bill;
        }

        @Override
        public String toString() {
            return String.format("Bill #%d - ₱%.2f (%s)", 
                    bill.billID(), bill.amountDue(), bill.status());
        }
    }
}