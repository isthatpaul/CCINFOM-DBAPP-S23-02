package Views.billing;

import Model.*;
import Services.BillService;
import Views.components.*;
import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.UUID;

public class PaymentFormDialog extends JDialog {

    private JComboBox<BillItem> billCombo;
    private JTextField amountPaidField;
    private JTextField receiptNumberField;
    private JComboBox<String> paymentMethodCombo;

    private BillCRUD billCRUD;
    private PaymentCRUD paymentCRUD;
    private BillService billService;
    private Staff currentStaff;
    private boolean processed = false;

    public PaymentFormDialog(Frame parent, Staff staff, Payment paymentToEdit) {
        super(parent, "Process Payment", true);
        this.currentStaff = staff;
        this.billCRUD = new BillCRUD();
        this.paymentCRUD = new PaymentCRUD();
        this.billService = new BillService();
        initComponents();
        // This dialog currently only supports creating new payments, not editing.
        // The 'paymentToEdit' parameter is included for future extensibility.
    }

    private void initComponents() {
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.weightx = 0.3;

        // Bill ComboBox
        gbc.gridy = 0;
        formPanel.add(createLabel("Bill to Pay:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        billCombo = new JComboBox<>();
        loadUnpaidBills();
        formPanel.add(billCombo, gbc);

        // Amount Paid
        gbc.gridy = 1; gbc.gridx = 0;
        formPanel.add(createLabel("Amount Paid:"), gbc);
        gbc.gridx = 1;
        amountPaidField = new JTextField();
        formPanel.add(amountPaidField, gbc);

        // Payment Method
        gbc.gridy = 2; gbc.gridx = 0;
        formPanel.add(createLabel("Payment Method:"), gbc);
        gbc.gridx = 1;
        paymentMethodCombo = new JComboBox<>(new String[]{"Cash", "Card", "Online"});
        formPanel.add(paymentMethodCombo, gbc);

        // Receipt Number
        gbc.gridy = 3; gbc.gridx = 0;
        formPanel.add(createLabel("Receipt Number:"), gbc);
        gbc.gridx = 1;
        receiptNumberField = new JTextField(UUID.randomUUID().toString().substring(0, 12).toUpperCase());
        formPanel.add(receiptNumberField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        StyledButton saveButton = new StyledButton("Process Payment", StyledButton.ButtonType.PRIMARY);
        saveButton.addActionListener(e -> processPayment());
        buttonPanel.add(saveButton);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadUnpaidBills() {
        billCRUD.getAllRecords().stream()
              .filter(bill -> !"PAID".equals(bill.status()))
              .forEach(bill -> billCombo.addItem(new BillItem(bill)));
    }

    private void processPayment() {
        BillItem selectedBillItem = (BillItem) billCombo.getSelectedItem();
        if (selectedBillItem == null) {
            showError("Please select a bill to pay.");
            return;
        }

        try {
            double amountPaid = Double.parseDouble(amountPaidField.getText());
            if (amountPaid <= 0) {
                showError("Amount paid must be positive.");
                return;
            }
            
            String receiptNumber = receiptNumberField.getText().trim();
            if (receiptNumber.isEmpty()) {
                showError("Receipt number cannot be empty.");
                return;
            }

            Payment newPayment = new Payment(
                0,
                selectedBillItem.getId(),
                new Date(System.currentTimeMillis()),
                amountPaid,
                (String) paymentMethodCombo.getSelectedItem(),
                receiptNumber,
                currentStaff.staffID(),
                currentStaff.staffID(), // Assuming processor is also the collector for now
                "COMPLETED"
            );

            // Transaction: 1. Add Payment Record
            if (paymentCRUD.addRecord(newPayment)) {
                // Transaction: 2. Update Bill Status via the service
                billService.updateBillAfterPayment(selectedBillItem.getId(), amountPaid);
                processed = true;
                JOptionPane.showMessageDialog(this, "Payment processed successfully!");
                dispose();
            } else {
                showError("Failed to save payment record. The receipt number might already exist.");
            }

        } catch (NumberFormatException ex) {
            showError("Invalid amount paid. Please enter a valid number.");
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return label;
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean isProcessed() {
        return processed;
    }

    // Inner class for JComboBox items
    private static class BillItem {
        private final Bill bill;
        public BillItem(Bill b) { this.bill = b; }
        public int getId() { return bill.billID(); }
        @Override public String toString() {
            return "Bill #" + bill.billID() + " | Due: " + String.format("₱%.2f", bill.amountDue()) + " (" + bill.status() + ")";
        }
    }
}