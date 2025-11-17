package Views.employee;

import Views.components.*;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.List;

/**
 * Dialog for adding/editing employee records
 * Assigned to: CRISOLOGO, Paul Martin Ryan A.
 */
public class EmployeeFormDialog extends JDialog {

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField streetField;
    private JTextField cityField;
    private JTextField provinceField;
    private JTextField zipCodeField;
    private JTextField contactNumberField;
    private JComboBox<DepartmentItem> departmentCombo;
    private JTextField positionField;
    private DatePicker hireDatePicker;

    private Employee employee;
    private EmployeeCRUD employeeCRUD;
    private DepartmentCRUD departmentCRUD;
    private boolean saved = false;

    public EmployeeFormDialog(Frame parent, Employee employee) {
        super(parent, employee == null ? "Add Employee" : "Edit Employee", true);
        this.employee = employee;
        this.employeeCRUD = new EmployeeCRUD();
        this.departmentCRUD = new DepartmentCRUD();
        initComponents();
        if (employee != null) {
            populateFields();
        }
    }

    private void initComponents() {
        setSize(550, 700);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0;
        gbc.weightx = 0.3;

        int row = 0;

        // First Name
        gbc.gridy = row;
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

        // Department
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Department:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        departmentCombo = new JComboBox<>();
        departmentCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loadDepartments();
        formPanel.add(departmentCombo, gbc);

        // Position
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Position:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        positionField = createTextField();
        formPanel.add(positionField, gbc);

        // Hire Date
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Hire Date:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        hireDatePicker = new DatePicker();
        formPanel.add(hireDatePicker, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, ColorScheme.BORDER));

        StyledButton saveButton = new StyledButton("Save", StyledButton.ButtonType.PRIMARY);
        saveButton.addActionListener(e -> saveEmployee());

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

    private void loadDepartments() {
        List<Department> departments = departmentCRUD.getAllRecords();
        for (Department dept : departments) {
            departmentCombo.addItem(new DepartmentItem(dept));
        }
    }

    private void populateFields() {
        firstNameField.setText(employee.firstName());
        lastNameField.setText(employee.lastName());
        streetField.setText(employee.street());
        cityField.setText(employee.city());
        provinceField.setText(employee.province());
        zipCodeField.setText(employee.zipCode());
        contactNumberField.setText(employee.contactNumber());
        positionField.setText(employee.position());
        hireDatePicker.setSqlDate(employee.hireDate());

        // Select department
        for (int i = 0; i < departmentCombo.getItemCount(); i++) {
            if (departmentCombo.getItemAt(i).getId() == employee.departmentID()) {
                departmentCombo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void saveEmployee() {
        // Validation
        if (firstNameField.getText().trim().isEmpty() ||
            lastNameField.getText().trim().isEmpty() ||
            positionField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        DepartmentItem selectedDept = (DepartmentItem) departmentCombo.getSelectedItem();
        if (selectedDept == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a department.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int employeeID = (employee != null) ? employee.employeeID() : 0;
        Date lastLoginDate = (employee != null) ? employee.lastLoginDate() : new Date(System.currentTimeMillis());

        Employee newEmployee = new Employee(
                employeeID,
                firstNameField.getText().trim(),
                lastNameField.getText().trim(),
                streetField.getText().trim(),
                cityField.getText().trim(),
                provinceField.getText().trim(),
                zipCodeField.getText().trim(),
                contactNumberField.getText().trim(),
                selectedDept.getId(),
                positionField.getText().trim(),
                hireDatePicker.getSqlDate(),
                lastLoginDate
        );

        boolean success;
        if (employee == null) {
            success = employeeCRUD.addRecord(newEmployee);
        } else {
            success = employeeCRUD.updateRecord(newEmployee);
        }

        if (success) {
            saved = true;
            JOptionPane.showMessageDialog(this,
                    "Employee saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to save employee.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }

    // Helper class
    private static class DepartmentItem {
        private Department department;

        public DepartmentItem(Department department) {
            this.department = department;
        }

        public int getId() {
            return department.departmentID();
        }

        @Override
        public String toString() {
            return department.departmentName();
        }
    }
}