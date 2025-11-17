package Views.utilities;

import Views.components.*;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Dialog for adding/editing rate records
 * Assigned to: ATACADOR, Juan Lorenzo N.
 */
public class RateFormDialog extends JDialog {

    private JComboBox<UtilityTypeItem> utilityTypeCombo;
    private JTextField ratePerUnitField;
    private DatePicker effectiveDatePicker;

    private Rate rate;
    private RateCRUD rateCRUD;
    private UtilityTypeCRUD utilityTypeCRUD;
    private boolean saved = false;

    public RateFormDialog(Frame parent, Rate rate) {
        super(parent, rate == null ? "Add Rate" : "Edit Rate", true);
        this.rate = rate;
        this.rateCRUD = new RateCRUD();
        this.utilityTypeCRUD = new UtilityTypeCRUD();
        initComponents();
        if (rate != null) {
            populateFields();
        }
    }

    private void initComponents() {
        setSize(500, 400);
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

        // Utility Type
        gbc.gridy = row;
        formPanel.add(createLabel("Utility Type:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        utilityTypeCombo = new JComboBox<>();
        utilityTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loadUtilityTypes();
        formPanel.add(utilityTypeCombo, gbc);

        // Rate Per Unit
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Rate Per Unit (₱):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        ratePerUnitField = createTextField();
        formPanel.add(ratePerUnitField, gbc);

        // Effective Date
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Effective Date:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        effectiveDatePicker = new DatePicker();
        formPanel.add(effectiveDatePicker, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, ColorScheme.BORDER));

        StyledButton saveButton = new StyledButton("Save", StyledButton.ButtonType.PRIMARY);
        saveButton.addActionListener(e -> saveRate());

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
        utilityTypeCombo.removeAllItems();
        
        for (UtilityType type : types) {
            if (type.isActive()) {
                utilityTypeCombo.addItem(new UtilityTypeItem(type));
            }
        }
    }

    private void populateFields() {
        ratePerUnitField.setText(String.valueOf(rate.ratePerUnit()));
        effectiveDatePicker.setSqlDate(rate.effectiveDate());

        // Select utility type
        if (rate.utilityTypeID() != null) {
            for (int i = 0; i < utilityTypeCombo.getItemCount(); i++) {
                if (utilityTypeCombo.getItemAt(i).getId() == rate.utilityTypeID()) {
                    utilityTypeCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void saveRate() {
        // Validation
        if (utilityTypeCombo.getSelectedItem() == null) {
            showError("Please select a utility type.");
            return;
        }
        if (ratePerUnitField.getText().trim().isEmpty()) {
            showError("Please enter rate per unit.");
            return;
        }

        try {
            UtilityTypeItem selectedType = (UtilityTypeItem) utilityTypeCombo.getSelectedItem();
            double ratePerUnit = Double.parseDouble(ratePerUnitField.getText().trim());

            if (ratePerUnit <= 0) {
                showError("Rate per unit must be greater than zero.");
                return;
            }

            int rateID = (rate != null) ? rate.rateID() : 0;

            Rate newRate = new Rate(
                    rateID,
                    selectedType.getId(),
                    ratePerUnit,
                    effectiveDatePicker.getSqlDate()
            );

            boolean success;
            if (rate == null) {
                success = rateCRUD.addRecord(newRate);
            } else {
                success = rateCRUD.updateRecord(newRate);
            }

            if (success) {
                saved = true;
                JOptionPane.showMessageDialog(this,
                        "Rate saved successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                showError("Failed to save rate.");
            }

        } catch (NumberFormatException e) {
            showError("Invalid rate value. Please enter a valid number.");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public boolean isSaved() {
        return saved;
    }

    // Helper class
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
            return type.utilityTypeName() + " (" + type.unitOfMeasure() + ")";
        }
    }
}