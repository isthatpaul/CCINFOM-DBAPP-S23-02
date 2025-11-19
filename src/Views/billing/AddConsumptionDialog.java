package Views.billing;

import Model.*;
import Views.components.DatePicker;
import Views.components.StyledButton;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class AddConsumptionDialog extends JDialog {

    private JComboBox<MeterItem> meterCombo;
    private JTextField consumptionValueField;
    private DatePicker readingDatePicker;

    private MeterCRUD meterCRUD;
    private ConsumptionCRUD consumptionCRUD;
    private boolean recordAdded = false;

    public AddConsumptionDialog(Frame parent) {
        super(parent, "Add Consumption Record", true);
        this.meterCRUD = new MeterCRUD();
        this.consumptionCRUD = new ConsumptionCRUD();
        initComponents();
    }

    private void initComponents() {
        setSize(500, 300);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.weightx = 0.3;

        // Meter ComboBox
        gbc.gridy = 0;
        formPanel.add(new JLabel("Meter:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        meterCombo = new JComboBox<>();
        loadMeters();
        formPanel.add(meterCombo, gbc);

        // Consumption Value
        gbc.gridy = 1; gbc.gridx = 0;
        formPanel.add(new JLabel("Consumption Value:"), gbc);
        gbc.gridx = 1;
        consumptionValueField = new JTextField();
        formPanel.add(consumptionValueField, gbc);

        // Reading Date
        gbc.gridy = 2; gbc.gridx = 0;
        formPanel.add(new JLabel("Reading Date:"), gbc);
        gbc.gridx = 1;
        readingDatePicker = new DatePicker();
        formPanel.add(readingDatePicker, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        StyledButton saveButton = new StyledButton("Save Record", StyledButton.ButtonType.PRIMARY);
        saveButton.addActionListener(e -> saveRecord());
        buttonPanel.add(saveButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadMeters() {
        meterCRUD.getAllRecords().forEach(meter -> meterCombo.addItem(new MeterItem(meter)));
    }

    private void saveRecord() {
        MeterItem selectedMeter = (MeterItem) meterCombo.getSelectedItem();
        if (selectedMeter == null) {
            showError("Please select a meter.");
            return;
        }

        try {
            double consumptionValue = Double.parseDouble(consumptionValueField.getText());
            Date readingDate = readingDatePicker.getSqlDate();

            Consumption newConsumption = new Consumption(0, selectedMeter.getId(), consumptionValue, readingDate);

            if (consumptionCRUD.addRecord(newConsumption)) {
                recordAdded = true;
                JOptionPane.showMessageDialog(this, "Consumption record added successfully!");
                dispose();
            } else {
                showError("Failed to add consumption record.");
            }
        } catch (NumberFormatException ex) {
            showError("Invalid consumption value. Please enter a valid number.");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean isRecordAdded() {
        return recordAdded;
    }

    private static class MeterItem {
        private final Meter meter;
        public MeterItem(Meter m) { this.meter = m; }
        public int getId() { return meter.meterID(); }
        @Override public String toString() { return "Serial: " + meter.meterSerialNumber() + " (ID: " + meter.meterID() + ")"; }
    }
}