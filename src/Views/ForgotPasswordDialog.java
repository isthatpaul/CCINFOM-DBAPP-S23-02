package Views;

import Model.Employee;
import Model.EmployeeCRUD;
import Model.Staff;
import Model.StaffCRUD;
import Services.PasswordUtils;
import Views.components.ColorScheme;
import Views.components.StyledButton;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog that allows staff to reset their password after providing identity details.
 */
public class ForgotPasswordDialog extends JDialog {

    private JTextField usernameField;
    private JTextField employeeIdField;
    private JTextField lastNameField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JLabel errorLabel;

    private final StaffCRUD staffCRUD = new StaffCRUD();
    private final EmployeeCRUD employeeCRUD = new EmployeeCRUD();

    public ForgotPasswordDialog(Frame owner) {
        super(owner, "Reset Password", true);
        initComponents();
    }

    private void initComponents() {
        setSize(420, 420);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("Forgot Password");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        container.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 2, 0);

        usernameField = createTextField();
        employeeIdField = createTextField();
        lastNameField = createTextField();
        newPasswordField = createPasswordField();
        confirmPasswordField = createPasswordField();

        addField(formPanel, gbc, "Username", usernameField);
        addField(formPanel, gbc, "Employee ID", employeeIdField);
        addField(formPanel, gbc, "Last Name (for verification)", lastNameField);
        addField(formPanel, gbc, "New Password", newPasswordField);
        addField(formPanel, gbc, "Confirm Password", confirmPasswordField);

        errorLabel = new JLabel(" ");
        errorLabel.setForeground(ColorScheme.ERROR);
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 0, 0);
        formPanel.add(errorLabel, gbc);

        StyledButton resetButton = new StyledButton("Reset Password", StyledButton.ButtonType.PRIMARY);
        resetButton.addActionListener(e -> handleReset());
        gbc.gridy++;
        gbc.insets = new Insets(15, 0, 0, 0);
        formPanel.add(resetButton, gbc);

        container.add(formPanel, BorderLayout.CENTER);

        JLabel infoLabel = new JLabel("<html><div style='width:340px;'>Enter your employee ID and last name so we can verify your identity before updating the password.</div></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        infoLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        container.add(infoLabel, BorderLayout.SOUTH);

        setContentPane(container);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JComponent field) {
        gbc.insets = new Insets(8, 0, 2, 0);
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(jLabel, gbc);

        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.gridy++;
        panel.add(field, gbc);
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    private void handleReset() {
        String username = usernameField.getText().trim();
        String employeeIdText = employeeIdField.getText().trim();
        String lastNameInput = lastNameField.getText().trim();
        String newPassword = new String(newPasswordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

        if (username.isEmpty() || employeeIdText.isEmpty() || lastNameInput.isEmpty() ||
                newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showError("All fields are required.");
            return;
        }

        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdText);
        } catch (NumberFormatException e) {
            showError("Employee ID must be a number.");
            return;
        }

        if (newPassword.length() < 8) {
            showError("Password must be at least 8 characters.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return;
        }

        Staff staff = staffCRUD.getRecordByUsername(username);
        if (staff == null) {
            showError("No account found for that username.");
            return;
        }

        if (staff.employeeID() != employeeId) {
            showError("Employee ID does not match our records.");
            return;
        }

        Employee employee = employeeCRUD.getRecordById(staff.employeeID());
        if (employee == null || !employee.lastName().equalsIgnoreCase(lastNameInput)) {
            showError("Verification failed. Please confirm your last name.");
            return;
        }

        String newHash = PasswordUtils.hashPassword(newPassword);
        boolean updated = staffCRUD.updatePasswordHash(staff.staffID(), newHash);
        if (updated) {
            JOptionPane.showMessageDialog(this,
                    "Password updated successfully. You can now sign in.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            showError("Unable to update password. Please try again.");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
    }
}

