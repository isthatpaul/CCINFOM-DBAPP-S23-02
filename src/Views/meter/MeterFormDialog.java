package Views.meter;

import Views.components.*;
import Model.Meter;
import Model.MeterCRUD;
import Model.UtilityType;
import Model.UtilityTypeCRUD;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Dialog for adding/editing meter records
 */
public class MeterFormDialog extends JDialog {

    private JComboBox<UtilityTypeItem> utilityTypeCombo;
    private JTextField serialNumberField;
    private JComboBox<String> statusCombo;

    private Meter meter;
    private MeterCRUD meterCRUD;
    private UtilityTypeCRUD utilityTypeCRUD;
    private boolean saved = false;

    public MeterFormDialog(Frame parent, Meter meter) {
        super(parent, meter == null ? "Add Meter" : "Edit Meter", true);
        this.meter = meter;
        this.meterCRUD = new MeterCRUD();
        this.utilityTypeCRUD = new UtilityTypeCRUD();
        initComponents();
        if (meter != null) {
            populateFields();
        }
    }

    private void initComponents() {
        setSize(450, 350);
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

        // Utility Type
        gbc.gridy = 0;
        formPanel.add(createLabel("Utility Type:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        utilityTypeCombo = new JComboBox<>();
        utilityTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loadUtilityTypes();
        formPanel.add(utilityTypeCombo, gbc);

        // Serial Number
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Serial Number:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        serialNumberField = createTextField();
        formPanel.add(serialNumberField, gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Status:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        statusCombo = new JComboBox<>(new String[]{"AVAILABLE", "ASSIGNED", "DECOMMISSIONED", "MAINTENANCE"});
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(statusCombo, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, ColorScheme.BORDER));

        StyledButton saveButton = new StyledButton("Save", StyledButton.ButtonType.PRIMARY);
        saveButton.addActionListener(e -> saveMeter());

        StyledButton cancelButton = new StyledButton("Cancel", StyledButton.ButtonType.SECONDARY);
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
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

    private void loadUtilityTypes() {
        List<UtilityType> types = utilityTypeCRUD.getAllRecords();
        for (UtilityType type : types) {
            utilityTypeCombo.addItem(new UtilityTypeItem(type));
        }
    }

    private void populateFields() {
        serialNumberField.setText(meter.meterSerialNumber());
        statusCombo.setSelectedItem(meter.meterStatus());

        // Select utility type
        for (int i = 0; i < utilityTypeCombo.getItemCount(); i++) {
            UtilityTypeItem item = utilityTypeCombo.getItemAt(i);
            if (item.getId() == meter.utilityTypeID()) {
                utilityTypeCombo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void saveMeter() {
        if (serialNumberField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a serial number.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        UtilityTypeItem selectedType = (UtilityTypeItem) utilityTypeCombo.getSelectedItem();

        int meterId = (meter != null) ? meter.meterID() : 0;
        Meter newMeter = new Meter(
                meterId,
                selectedType != null ? selectedType.getId() : 0,
                serialNumberField.getText().trim(),
                (String) statusCombo.getSelectedItem()
        );

        boolean success;
        if (meter == null) {
            success = meterCRUD.addRecord(newMeter);
        } else {
            success = meterCRUD.updateRecord(newMeter);
        }

        if (success) {
            saved = true;
            JOptionPane.showMessageDialog(this,
                    "Meter saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to save meter.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }

    // Helper class for combo box
    private static class UtilityTypeItem {
        private UtilityType type;

        public UtilityTypeItem(UtilityType type) {
            this.type = type;
        }

        public int getId() {
            return type.utilityTypeID();
        }

        @Override
        public String toString() {
            return type.utilityTypeName();
        }
    }
}