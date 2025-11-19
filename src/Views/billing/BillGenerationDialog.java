package Views.billing;

import Views.components.*;
import Model.*;
import Services.BillService;
import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class BillGenerationDialog extends JDialog {

    private JComboBox<CustomerItem> customerCombo;
    private JComboBox<MeterItem> meterCombo;
    private JTextField consumptionValueField;
    private DatePicker readingDatePicker;

    private final CustomerCRUD customerCRUD;
    private final MeterCRUD meterCRUD;
    private final ConsumptionCRUD consumptionCRUD;
    private final BillService billService;
    private final Staff currentStaff;
    private boolean generated = false;

    public BillGenerationDialog(Frame parent, Staff staff) {
        super(parent, "Generate Bill", true);
        this.currentStaff = staff;
        this.customerCRUD = new CustomerCRUD();
        this.meterCRUD = new MeterCRUD();
        this.consumptionCRUD = new ConsumptionCRUD();
        this.billService = new BillService();
        initComponents();
    }

    private void initComponents() {
        setSize(500, 350);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.weightx = 0.3;

        int row = 0;

        gbc.gridy = row++;
        formPanel.add(createLabel("Customer:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        customerCombo = new JComboBox<>();
        customerCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loadCustomers();
        customerCombo.addActionListener(e -> loadMetersForCustomer());
        formPanel.add(customerCombo, gbc);

        gbc.gridy = row++;
        gbc.gridx = 0; gbc.weightx = 0.3;
        formPanel.add(createLabel("Meter:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        meterCombo = new JComboBox<>();
        meterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(meterCombo, gbc);

        gbc.gridy = row++;
        gbc.gridx = 0; gbc.weightx = 0.3;
        formPanel.add(createLabel("Reading Date:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        readingDatePicker = new DatePicker();
        formPanel.add(readingDatePicker, gbc);

        gbc.gridy = row++;
        gbc.gridx = 0; gbc.weightx = 0.3;
        formPanel.add(createLabel("Consumption Value:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        consumptionValueField = createTextField();
        formPanel.add(consumptionValueField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, ColorScheme.BORDER));
        StyledButton generateButton = new StyledButton("Generate Bill", StyledButton.ButtonType.PRIMARY);
        generateButton.addActionListener(e -> generateBill());
        StyledButton cancelButton = new StyledButton("Cancel", StyledButton.ButtonType.SECONDARY);
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);
        buttonPanel.add(generateButton);

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
        customerCombo.removeAllItems();
        customerCRUD.getAllRecords().forEach(c -> customerCombo.addItem(new CustomerItem(c)));
        if (customerCombo.getItemCount() > 0) {
            customerCombo.setSelectedIndex(0);
        }
    }

    private void loadMetersForCustomer() {
        meterCombo.removeAllItems();
        CustomerItem selectedCustomer = (CustomerItem) customerCombo.getSelectedItem();
        if (selectedCustomer != null) {
            meterCRUD.getMetersByCustomerId(selectedCustomer.getId())
                     .forEach(m -> meterCombo.addItem(new MeterItem(m)));
        }
    }

    private void generateBill() {
        if (customerCombo.getSelectedItem() == null || meterCombo.getSelectedItem() == null || consumptionValueField.getText().trim().isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }

        try {
            MeterItem selectedMeter = (MeterItem) meterCombo.getSelectedItem();
            double consumptionValue = Double.parseDouble(consumptionValueField.getText().trim());
            Date readingDate = readingDatePicker.getSqlDate();
            
            if (consumptionValue < 0) {
                showError("Consumption value cannot be negative.");
                return;
            }

            Consumption tempConsumption = new Consumption(0, selectedMeter.getId(), consumptionValue, readingDate);
            
            if (!consumptionCRUD.addRecord(tempConsumption)) {
                showError("Failed to create consumption record.");
                return;
            }

            Consumption createdConsumption = consumptionCRUD.getLatestConsumptionForMeter(selectedMeter.getId());
            if (createdConsumption == null) {
                showError("Could not retrieve new consumption record after creation.");
                return;
            }

            Bill generatedBill = billService.generateBillFromConsumption(createdConsumption, currentStaff.staffID());

            generated = true;
            JOptionPane.showMessageDialog(this, "Bill #" + generatedBill.billID() + " generated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (NumberFormatException e) {
            showError("Invalid consumption value. Please enter a valid number.");
        } catch (BillService.BillingException e) {
            showError("Error generating bill: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showError("An unexpected error occurred: " + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean isGenerated() {
        return generated;
    }

    private static class CustomerItem {
        private final Customer customer;
        public CustomerItem(Customer c) { this.customer = c; }
        public int getId() { return customer.customerID(); }
        @Override public String toString() { return customer.firstName() + " " + customer.lastName() + " (" + customer.accountNumber() + ")"; }
    }

    private static class MeterItem {
        private final Meter meter;
        public MeterItem(Meter m) { this.meter = m; }
        public int getId() { return meter.meterID(); }
        @Override public String toString() { return "Serial: " + meter.meterSerialNumber() + " (ID: " + meter.meterID() + ")"; }
    }
}