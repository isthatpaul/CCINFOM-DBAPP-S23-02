package Views.customer;

import Views.components.*;
import Model.Customer;
import Services.CustomerService;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

/**
 * Dialog for adding/editing customer records
 */
public class CustomerFormDialog extends JDialog {

    private JTextField accountNumberField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField streetField;
    private JTextField cityField;
    private JTextField provinceField;
    private JTextField zipCodeField;
    private JTextField contactNumberField;
    private JComboBox<String> billingStatusCombo;
    private DatePicker createdDatePicker;

    private Customer customer;
    private CustomerService customerService;
    private boolean saved = false;

    public CustomerFormDialog(Frame parent, Customer customer) {
        super(parent, customer == null ? "Add Customer" : "Edit Customer", true);
        this.customer = customer;
        this.customerService = new CustomerService();
        initComponents();
        if (customer != null) {
            populateFields();
        }
    }

    private void initComponents() {
        setSize(500, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.weightx = 0.3;

        int row = 0;

        // Account Number
        gbc.gridy = row;
        formPanel.add(createLabel("Account Number:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        accountNumberField = createTextField();
        formPanel.add(accountNumberField, gbc);

        // First Name
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("First Name:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        firstNameField = createTextField();
        formPanel.add(firstNameField, gbc);

        // Last Name
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        lastNameField = createTextField();
        formPanel.add(lastNameField, gbc);

        // Street
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Street:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        streetField = createTextField();
        formPanel.add(streetField, gbc);

        // City
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("City:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        cityField = createTextField();
        formPanel.add(cityField, gbc);

        // Province
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Province:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        provinceField = createTextField();
        formPanel.add(provinceField, gbc);

        // Zip Code
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Zip Code:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        zipCodeField = createTextField();
        formPanel.add(zipCodeField, gbc);

        // Contact Number
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Contact Number:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        contactNumberField = createTextField();
        formPanel.add(contactNumberField, gbc);

        // Billing Status
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Billing Status:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        billingStatusCombo = new JComboBox<>(new String[]{"ACTIVE", "INACTIVE", "SUSPENDED"});
        billingStatusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(billingStatusCombo, gbc);

        // Created Date
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Created Date:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        createdDatePicker = new DatePicker();
        formPanel.add(createdDatePicker, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, ColorScheme.BORDER));

        StyledButton saveButton = new StyledButton("Save", StyledButton.ButtonType.PRIMARY);
        saveButton.addActionListener(e -> saveCustomer());

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
        accountNumberField.setText(customer.accountNumber());
        firstNameField.setText(customer.firstName());
        lastNameField.setText(customer.lastName());
        streetField.setText(customer.street());
        cityField.setText(customer.city());
        provinceField.setText(customer.province());
        zipCodeField.setText(customer.zipCode());
        contactNumberField.setText(customer.contactNumber());
        billingStatusCombo.setSelectedItem(customer.billingStatus());
        createdDatePicker.setSqlDate(customer.createdDate());
    }

    private void saveCustomer() {
        // Validate fields
        if (accountNumberField.getText().trim().isEmpty() ||
                firstNameField.getText().trim().isEmpty() ||
                lastNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create customer object
        int customerId = (customer != null) ? customer.customerID() : 0;
        Customer newCustomer = new Customer(
                customerId,
                accountNumberField.getText().trim(),
                firstNameField.getText().trim(),
                lastNameField.getText().trim(),
                streetField.getText().trim(),
                cityField.getText().trim(),
                provinceField.getText().trim(),
                zipCodeField.getText().trim(),
                contactNumberField.getText().trim(),
                createdDatePicker.getSqlDate(),
                (String) billingStatusCombo.getSelectedItem()
        );

        // Save using service
        boolean success;
        if (customer == null) {
            success = customerService.addCustomer(newCustomer);
        } else {
            success = customerService.updateCustomer(newCustomer);
        }

        if (success) {
            saved = true;
            JOptionPane.showMessageDialog(this,
                    "Customer saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to save customer. Please check the data.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }
}