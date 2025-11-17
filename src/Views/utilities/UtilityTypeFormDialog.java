package Views.utilities;

import Views.components.*;
import Model.UtilityType;
import Model.UtilityTypeCRUD;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

/**
 * Dialog for adding/editing utility type records
 * Assigned to: SAMONTE, Joshua Carlos B.
 */
public class UtilityTypeFormDialog extends JDialog {

    private JTextField utilityTypeNameField;
    private JTextArea descriptionArea;
    private JTextField unitOfMeasureField;
    private JCheckBox isActiveCheckbox;

    private UtilityType utilityType;
    private UtilityTypeCRUD utilityTypeCRUD;
    private boolean saved = false;

    public UtilityTypeFormDialog(Frame parent, UtilityType utilityType) {
        super(parent, utilityType == null ? "Add Utility Type" : "Edit Utility Type", true);
        this.utilityType = utilityType;
        this.utilityTypeCRUD = new UtilityTypeCRUD();
        initComponents();
        if (utilityType != null) {
            populateFields();
        }
    }

    private void initComponents() {
        setSize(500, 500);
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

        // Utility Type Name
        gbc.gridy = row;
        formPanel.add(createLabel("Utility Type Name:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        utilityTypeNameField = createTextField();
        formPanel.add(utilityTypeNameField, gbc);

        // Unit of Measure
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Unit of Measure:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        unitOfMeasureField = createTextField();
        formPanel.add(unitOfMeasureField, gbc);

        // Description
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(createLabel("Description:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descScrollPane.setPreferredSize(new Dimension(300, 100));
        formPanel.add(descScrollPane, gbc);

        // Is Active
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(createLabel("Active:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        isActiveCheckbox = new JCheckBox("Is Active");
        isActiveCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        isActiveCheckbox.setBackground(Color.WHITE);
        isActiveCheckbox.setSelected(true);
        formPanel.add(isActiveCheckbox, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, ColorScheme.BORDER));

        StyledButton saveButton = new StyledButton("Save", StyledButton.ButtonType.PRIMARY);
        saveButton.addActionListener(e -> saveUtilityType());

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

    private void populateFields() {
        utilityTypeNameField.setText(utilityType.utilityTypeName());
        descriptionArea.setText(utilityType.description());
        unitOfMeasureField.setText(utilityType.unitOfMeasure());
        isActiveCheckbox.setSelected(utilityType.isActive());
    }

    private void saveUtilityType() {
        // Validation
        if (utilityTypeNameField.getText().trim().isEmpty()) {
            showError("Please enter utility type name.");
            return;
        }
        if (unitOfMeasureField.getText().trim().isEmpty()) {
            showError("Please enter unit of measure.");
            return;
        }

        int utilityTypeID = (utilityType != null) ? utilityType.utilityTypeID() : 0;
        Date createdDate = (utilityType != null) ? utilityType.createdDate() : new Date(System.currentTimeMillis());
        Date modifiedDate = new Date(System.currentTimeMillis());

        UtilityType newUtilityType = new UtilityType(
                utilityTypeID,
                utilityTypeNameField.getText().trim(),
                descriptionArea.getText().trim(),
                unitOfMeasureField.getText().trim(),
                createdDate,
                modifiedDate,
                isActiveCheckbox.isSelected()
        );

        boolean success;
        if (utilityType == null) {
            success = utilityTypeCRUD.addRecord(newUtilityType);
        } else {
            success = utilityTypeCRUD.updateRecord(newUtilityType);
        }

        if (success) {
            saved = true;
            JOptionPane.showMessageDialog(this,
                    "Utility type saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            showError("Failed to save utility type.");
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
}