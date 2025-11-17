package Views.billing;

import Views.components.*;
import Model.*;
import Services.BillService;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.List;

/**
 * Dialog for generating bills (Transaction 2)
 * Assigned to: CRISOLOGO, Paul Martin Ryan A. & LUIS, Kamillu Raphael C.
 */
public class BillGenerationDialog extends JDialog {

    private JComboBox<CustomerItem> customerCombo;
    private JComboBox<MeterItem> meterCombo;
    private JTextField consumptionValueField;
    private DatePicker readingDatePicker;
    private JComboBox<RateItem> rateCombo;
    private JTextField calculatedAmountField;
    private JSpinner dueDaysSpinner;
    private JTextField technicianIDField;

    private CustomerCRUD customerCRUD;
    private MeterCRUD meterCRUD;
    private RateCRUD rateCRUD;
    private ConsumptionCRUD consumptionCRUD;
    private BillService billService;
    private Staff currentStaff;
    private boolean generated = false;

    public BillGenerationDialog(Frame parent, Staff staff) {
        super(parent, "Generate Bill", true);
        this.currentStaff = staff;
        this.customerCRUD = new CustomerCRUD();
        this.meterCRUD = new MeterCRUD();
        this.rateCRUD = new RateCRUD();
        this.consumptionCRUD = new ConsumptionCRUD();
        this.billService = new BillService();
        initComponents();
    }

    private void initComponents() {
        setSize(600, 700);
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
        customerCombo.addActionListener(e -> loadMetersForCustomer());
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
        meterCombo.addActionListener(e -> loadRatesForMeter());
        formPanel.add(meterCombo, gbc);

        // Reading Date
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Reading Date:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        readingDatePicker = new DatePicker();
        formPanel.add(readingDatePicker, gbc);

        // Consumption Value
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Consumption Value:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        consumptionValueField = createTextField();
        consumptionValueField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calculateAmount();
            }
        });
        formPanel.add(consumptionValueField, gbc);

        // Rate
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Rate:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        rateCombo = new JComboBox<>();
        rateCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rateCombo.addActionListener(e -> calculateAmount());
        formPanel.add(rateCombo, gbc);

        // Calculated Amount (read-only)
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Calculated Amount:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        calculatedAmountField = createTextField();
        calculatedAmountField.setEditable(false);
        calculatedAmountField.setBackground(new Color(240, 240, 240));
        formPanel.add(calculatedAmountField, gbc);

        // Due Days
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Due Days:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        dueDaysSpinner = new JSpinner(new SpinnerNumberModel(15, 1, 90, 1));
        dueDaysSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(dueDaysSpinner, gbc);

        // Technician ID (optional)
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Technician ID (Optional):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        technicianIDField = createTextField();
        formPanel.add(technicianIDField, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, ColorScheme.BORDER));

        StyledButton generateButton = new StyledButton("Generate Bill", StyledButton.ButtonType.PRIMARY);
        generateButton.addActionListener(e -> generateBill());

        StyledButton cancelButton = new StyledButton("Cancel", StyledButton.ButtonType.SECONDARY);
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(generateButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(ColorScheme.TEXT_PRIMARY);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private void loadCustomers() {
        List<Customer> customers = customerCRUD.getAllRecords();
        customerCombo.removeAllItems();
        for (Customer customer : customers) {
            customerCombo.addItem(new CustomerItem(customer));
        }
    }

    private void loadMetersForCustomer() {
        meterCombo.removeAllItems();
        CustomerItem selectedCustomer = (CustomerItem) customerCombo.getSelectedItem();
        if (selectedCustomer != null) {
            // Load meters assigned to this customer via MeterAssignment
            MeterAssignmentCRUD assignmentCRUD = new MeterAssignmentCRUD();
            List<MeterAssignment> assignments = assignmentCRUD.getAllRecords();
            
            for (MeterAssignment assignment : assignments) {
                if (assignment.customerID() == selectedCustomer.getId() && 
                    "ACTIVE".equals(assignment.status())) {
                    Meter meter = meterCRUD.getRecordById(assignment.meterID());
                    if (meter != null) {
                        meterCombo.addItem(new MeterItem(meter));
                    }
                }
            }
        }
    }

    private void loadRatesForMeter() {
        rateCombo.removeAllItems();
        MeterItem selectedMeter = (MeterItem) meterCombo.getSelectedItem();
        if (selectedMeter != null) {
            // Load rates for the utility type of this meter
            List<Rate> rates = rateCRUD.getAllRecords();
            for (Rate rate : rates) {
                if (rate.utilityTypeID() != null && 
                    rate.utilityTypeID() == selectedMeter.getMeter().utilityTypeID()) {
                    rateCombo.addItem(new RateItem(rate));
                }
            }
        }
    }

    private void calculateAmount() {
        try {
            String consumptionText = consumptionValueField.getText().trim();
            RateItem selectedRate = (RateItem) rateCombo.getSelectedItem();

            if (!consumptionText.isEmpty() && selectedRate != null) {
                double consumption = Double.parseDouble(consumptionText);
                double rate = selectedRate.getRate().ratePerUnit();
                double amount = consumption * rate;
                calculatedAmountField.setText(String.format("₱%.2f", amount));
            } else {
                calculatedAmountField.setText("");
            }
        } catch (NumberFormatException e) {
            calculatedAmountField.setText("Invalid");
        }
    }

    private void generateBill() {
        // Validation
        if (customerCombo.getSelectedItem() == null) {
            showError("Please select a customer.");
            return;
        }
        if (meterCombo.getSelectedItem() == null) {
            showError("Please select a meter.");
            return;
        }
        if (consumptionValueField.getText().trim().isEmpty()) {
            showError("Please enter consumption value.");
            return;
        }
        if (rateCombo.getSelectedItem() == null) {
            showError("Please select a rate.");
            return;
        }

        try {
            CustomerItem selectedCustomer = (CustomerItem) customerCombo.getSelectedItem();
            MeterItem selectedMeter = (MeterItem) meterCombo.getSelectedItem();
            RateItem selectedRate = (RateItem) rateCombo.getSelectedItem();

            double consumptionValue = Double.parseDouble(consumptionValueField.getText().trim());
            Date readingDate = readingDatePicker.getSqlDate();
            int dueDays = (Integer) dueDaysSpinner.getValue();

            // Get technician ID (optional)
            Integer technicianID = null;
            String techIDText = technicianIDField.getText().trim();
            if (!techIDText.isEmpty()) {
                technicianID = Integer.parseInt(techIDText);
            }

            // Step 1: Create Consumption record
            Consumption consumption = new Consumption(
                    0, // Auto-generated
                    selectedMeter.getId(),
                    consumptionValue,
                    readingDate
            );

            boolean consumptionCreated = consumptionCRUD.addRecord(consumption);
            if (!consumptionCreated) {
                showError("Failed to create consumption record.");
                return;
            }

            // Get the created consumption ID (need to retrieve it)
            List<Consumption> consumptions = consumptionCRUD.getAllRecords();
            Consumption createdConsumption = consumptions.get(consumptions.size() - 1);

            // Step 2: Generate Bill using BillService
            boolean billGenerated = billService.generateBill(
                    createdConsumption,
                    selectedRate.getRate(),
                    selectedCustomer.getId(),
                    currentStaff.staffID(),
                    technicianID,
                    dueDays
            );

            if (billGenerated) {
                generated = true;
                JOptionPane.showMessageDialog(this,
                        "Bill generated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                showError("Failed to generate bill.");
            }

        } catch (NumberFormatException e) {
            showError("Invalid numeric input: " + e.getMessage());
        } catch (Exception e) {
            showError("Error generating bill: " + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public boolean isGenerated() {
        return generated;
    }

    // Helper classes
    private static class CustomerItem {
        private Customer customer;

        public CustomerItem(Customer customer) {
            this.customer = customer;
        }

        public int getId() {
            return customer.customerID();
        }

        @Override
        public String toString() {
            return customer.firstName() + " " + customer.lastName() + 
                   " (Account: " + customer.accountNumber() + ")";
        }
    }

    private static class MeterItem {
        private Meter meter;

        public MeterItem(Meter meter) {
            this.meter = meter;
        }

        public int getId() {
            return meter.meterID();
        }

        public Meter getMeter() {
            return meter;
        }

        @Override
        public String toString() {
            return "Serial: " + meter.meterSerialNumber() + " (ID: " + meter.meterID() + ")";
        }
    }

    private static class RateItem {
        private Rate rate;

        public RateItem(Rate rate) {
            this.rate = rate;
        }

        public Rate getRate() {
            return rate;
        }

        @Override
        public String toString() {
            return String.format("₱%.2f per unit (Effective: %s)", 
                    rate.ratePerUnit(), rate.effectiveDate());
        }
    }
}