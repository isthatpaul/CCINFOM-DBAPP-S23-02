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
import java.util.Arrays;
import java.util.regex.Pattern;

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
    
    // Password validation pattern - require uppercase, lowercase, and digit
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");

    public ForgotPasswordDialog(Frame owner) {
        super(owner, "Reset Password", true);
        initComponents();
    }

    private void initComponents() {
        setSize(420, 550);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel container = new JPanel(new BorderLayout(0, 10));
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Title
        JLabel titleLabel = new JLabel("Reset Password");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        container.add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

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

        // Error label
        errorLabel = new JLabel(" ");
        errorLabel.setForeground(ColorScheme.ERROR);
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        gbc.gridy++;
        gbc.insets = new Insets(3, 0, 3, 0);
        formPanel.add(errorLabel, gbc);

        // Reset Password Button
        StyledButton resetButton = new StyledButton("Reset Password", StyledButton.ButtonType.PRIMARY);
        resetButton.setPreferredSize(new Dimension(350, 36));
        resetButton.addActionListener(e -> handleReset());
        gbc.gridy++;
        gbc.insets = new Insets(8, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(resetButton, gbc);

        container.add(formPanel, BorderLayout.CENTER);

        // Info Panel - more compact
        JLabel infoLabel = new JLabel("<html><div style='width:350px; font-size:10px;'>" +
                "<i>Enter your employee ID and last name to verify your identity.</i><br><br>" +
                "<b>Password requirements:</b> 8+ chars, uppercase, lowercase, number" +
                "</div></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        infoLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        container.add(infoLabel, BorderLayout.SOUTH);

        setContentPane(container);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JComponent field) {
        // Label
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        jLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        gbc.gridy++;
        gbc.insets = new Insets(3, 0, 3, 0);
        panel.add(jLabel, gbc);

        // Field
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 8, 0);
        panel.add(field, gbc);
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setPreferredSize(new Dimension(350, 32));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setPreferredSize(new Dimension(350, 32));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return field;
    }

    private void handleReset() {
        String username = usernameField.getText().trim();
        String employeeIdText = employeeIdField.getText().trim();
        String lastNameInput = lastNameField.getText().trim();
        char[] newPasswordChars = newPasswordField.getPassword();
        char[] confirmPasswordChars = confirmPasswordField.getPassword();

        try {
            // Validate all fields are filled
            if (username.isEmpty() || employeeIdText.isEmpty() || lastNameInput.isEmpty() ||
                    newPasswordChars.length == 0 || confirmPasswordChars.length == 0) {
                showError("All fields are required.");
                return;
            }

            // Validate username
            if (username.length() < 3 || username.length() > 50) {
                showError("Invalid username format.");
                return;
            }

            // Validate and parse Employee ID
            int employeeId;
            try {
                employeeId = Integer.parseInt(employeeIdText);
                if (employeeId <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                showError("Employee ID must be a valid positive number.");
                return;
            }

            // Validate last name
            if (lastNameInput.length() < 2 || lastNameInput.length() > 100) {
                showError("Please enter a valid last name.");
                return;
            }

            // Use constant-time comparison for passwords to prevent timing attacks
            if (!Arrays.equals(newPasswordChars, confirmPasswordChars)) {
                showError("Passwords do not match.");
                return;
            }

            // Convert password to String for validation
            String newPassword = new String(newPasswordChars);

            // Validate password length
            if (newPassword.length() < 8) {
                showError("Password must be at least 8 characters.");
                return;
            }

            // Validate password complexity
            if (!PASSWORD_PATTERN.matcher(newPassword).matches()) {
                showError("Password must contain uppercase, lowercase, and number.");
                return;
            }

            // Lookup staff by username
            Staff staff = staffCRUD.getRecordByUsername(username);
            if (staff == null) {
                // Use generic message to prevent username enumeration attacks
                showError("Verification failed. Please check your details.");
                return;
            }

            // Verify employee ID matches
            if (staff.employeeID() != employeeId) {
                // Use generic message to prevent information disclosure
                showError("Verification failed. Please check your details.");
                return;
            }

            // Verify last name matches (case-insensitive)
            Employee employee = employeeCRUD.getRecordById(staff.employeeID());
            if (employee == null || !employee.lastName().trim().equalsIgnoreCase(lastNameInput)) {
                // Use generic message to prevent information disclosure
                showError("Verification failed. Please check your details.");
                return;
            }

            // Hash the new password using PBKDF2
            String newHash = PasswordUtils.hashPassword(newPassword);
            
            // Update the password in the database
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

        } finally {
            // Clear sensitive data from memory immediately
            if (newPasswordChars != null) {
                Arrays.fill(newPasswordChars, '\0');
            }
            if (confirmPasswordChars != null) {
                Arrays.fill(confirmPasswordChars, '\0');
            }
            newPasswordField.setText("");
            confirmPasswordField.setText("");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
    }
}