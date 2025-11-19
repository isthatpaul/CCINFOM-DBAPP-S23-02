package Views.meter;

import Views.components.*;
import Model.*;
import Services.MeterAssignmentService;
import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.List;

/**
 * Dialog for meter assignment (Transaction 1)
 * Assigned to: LUIS, Kamillu Raphael C.
 */
public class MeterAssignmentFormDialog extends JDialog {

    private JComboBox<CustomerItem> customerCombo;
    private JComboBox<MeterItem> meterCombo;
    private DatePicker assignmentDatePicker;
    private DatePicker installationDatePicker;
    private JComboBox<String> statusCombo;
    private JLabel utilityTypeLabel;
    private JLabel meterStatusLabel;

    private MeterAssignment assignment;
    private MeterAssignmentService assignmentService;
    private CustomerCRUD customerCRUD;
    private MeterCRUD meterCRUD;
    private Staff currentStaff; // Added to get the logged-in staff member
    private boolean saved = false;

    // Constructor updated to accept the Staff object
    public MeterAssignmentFormDialog(Frame parent, MeterAssignment assignment, Staff staff) {
        super(parent, assignment == null ? "Assign Meter" : "Update Assignment", true);
        this.assignment = assignment;
        this.currentStaff = staff; // Store the staff object
        this.assignmentService = new MeterAssignmentService();
        this.customerCRUD = new CustomerCRUD();
        this.meterCRUD = new MeterCRUD();
        initComponents();
        if (assignment != null) {
            populateFields();
        }
    }

    // ... (initComponents and other UI methods remain unchanged)
    private void initComponents() {
        setSize(600, 600);
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

        // Customer
        gbc.gridy = row;
        formPanel.add(createLabel("Customer:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        customerCombo = new JComboBox<>();
        customerCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loadCustomers();
        formPanel.add(customerCombo, gbc);

        // Meter
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Meter:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        meterCombo = new JComboBox<>();
        meterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loadAvailableMeters();
        meterCombo.addActionListener(e -> updateMeterInfo());
        formPanel.add(meterCombo, gbc);

        // Utility Type (read-only)
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Utility Type:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        utilityTypeLabel = new JLabel();
        utilityTypeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(utilityTypeLabel, gbc);

        // Meter Status (read-only)
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Meter Status:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        meterStatusLabel = new JLabel();
        meterStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(meterStatusLabel, gbc);

        // Assignment Date
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Assignment Date:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        assignmentDatePicker = new DatePicker();
        formPanel.add(assignmentDatePicker, gbc);

        // Installation Date
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Installation Date:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        installationDatePicker = new DatePicker();
        formPanel.add(installationDatePicker, gbc);

        // Status
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Status:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        statusCombo = new JComboBox<>(new String[]{"ACTIVE", "INACTIVE"});
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(statusCombo, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, ColorScheme.BORDER));

        StyledButton saveButton = new StyledButton("Save", StyledButton.ButtonType.PRIMARY);
        saveButton.addActionListener(e -> saveAssignment());

        StyledButton cancelButton = new StyledButton("Cancel", StyledButton.ButtonType.SECONDARY);
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Initialize meter info
        updateMeterInfo();
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(ColorScheme.TEXT_PRIMARY);
        return label;
    }

    private void loadCustomers() {
        List<Customer> customers = customerCRUD.getAllRecords();
        for (Customer customer : customers) {
            customerCombo.addItem(new CustomerItem(customer));
        }
    }

    private void loadAvailableMeters() {
        List<Meter> meters = meterCRUD.getAllRecords();
        meterCombo.removeAllItems();
        
        for (Meter meter : meters) {
            // Only show available meters (or current meter if editing)
            if ("AVAILABLE".equals(meter.meterStatus()) || 
                (assignment != null && meter.meterID() == assignment.meterID())) {
                meterCombo.addItem(new MeterItem(meter));
            }
        }
    }
    
    private void updateMeterInfo() {
        MeterItem selectedMeter = (MeterItem) meterCombo.getSelectedItem();
        if (selectedMeter != null) {
            Meter meter = selectedMeter.getMeter();
            
            // Display utility type
            UtilityTypeCRUD utilityTypeCRUD = new UtilityTypeCRUD();
            UtilityType utilityType = utilityTypeCRUD.getRecordById(meter.utilityTypeID());
            if (utilityType != null) {
                utilityTypeLabel.setText(utilityType.utilityTypeName());
            } else {
                utilityTypeLabel.setText("Unknown");
            }
            
            // Display meter status
            meterStatusLabel.setText(meter.meterStatus());
            if ("AVAILABLE".equals(meter.meterStatus())) {
                meterStatusLabel.setForeground(ColorScheme.SUCCESS);
            } else {
                meterStatusLabel.setForeground(ColorScheme.WARNING);
            }
        }
    }

    private void populateFields() {
        // Select customer
        for (int i = 0; i < customerCombo.getItemCount(); i++) {
            if (customerCombo.getItemAt(i).getId() == assignment.customerID()) {
                customerCombo.setSelectedIndex(i);
                break;
            }
        }

        // Select meter
        for (int i = 0; i < meterCombo.getItemCount(); i++) {
            if (meterCombo.getItemAt(i).getId() == assignment.meterID()) {
                meterCombo.setSelectedIndex(i);
                break;
            }
        }

        assignmentDatePicker.setSqlDate(assignment.assignmentDate());
        installationDatePicker.setSqlDate(assignment.installationDate());
        statusCombo.setSelectedItem(assignment.status());
    }

    private void saveAssignment() {
        if (customerCombo.getSelectedItem() == null || meterCombo.getSelectedItem() == null) {
            showError("Please select a customer and a meter.");
            return;
        }

        CustomerItem selectedCustomer = (CustomerItem) customerCombo.getSelectedItem();
        MeterItem selectedMeter = (MeterItem) meterCombo.getSelectedItem();

        // FIX: Get the Staff ID from the currently logged-in user
        int assignedByStaffID = currentStaff.staffID(); 

        MeterAssignment newAssignment = new MeterAssignment(
                (assignment != null) ? assignment.assignmentID() : 0,
                selectedCustomer.getId(),
                selectedMeter.getId(),
                assignmentDatePicker.getSqlDate(),
                installationDatePicker.getSqlDate(),
                assignedByStaffID,
                (String) statusCombo.getSelectedItem(),
                new Date(System.currentTimeMillis()) // lastUpdated
        );

        boolean success;
        // Use the service to handle the database logic
        if (assignment == null) {
            success = assignmentService.assignMeter(newAssignment);
        } else {
            success = assignmentService.updateAssignment(newAssignment);
        }

        if (success) {
            saved = true;
            JOptionPane.showMessageDialog(this, "Meter assignment saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            showError("Failed to save meter assignment. The meter may already be assigned.");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean isSaved() {
        return saved;
    }

    // Helper classes
    private static class CustomerItem {
        private final Customer customer;
        public CustomerItem(Customer customer) { this.customer = customer; }
        public int getId() { return customer.customerID(); }
        @Override public String toString() { return customer.firstName() + " " + customer.lastName() + " (Account: " + customer.accountNumber() + ")"; }
    }

    private static class MeterItem {
        private final Meter meter;
        public MeterItem(Meter meter) { this.meter = meter; }
        public int getId() { return meter.meterID(); }
        public Meter getMeter() { return meter; }
        @Override public String toString() { return "Serial: " + meter.meterSerialNumber() + " (ID: " + meter.meterID() + ")"; }
    }
}