package Views;

import Views.components.*;
import Model.Staff;
import Model.StaffCRUD;
import Services.PasswordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Login screen for the Public Utility Billing System.
 * Provides role-based authentication.
 */
public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;
    private StaffCRUD staffCRUD;

    public LoginFrame() {
        staffCRUD = new StaffCRUD();
        initComponents();
    }

    private void initComponents() {
        setTitle("Public Utility Billing System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorScheme.BACKGROUND);

        // Logo/Header Panel
        JPanel headerPanel = createHeaderPanel();

        // Login Form Panel
        JPanel formPanel = createFormPanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ColorScheme.PRIMARY);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        // Logo placeholder (you can replace with actual logo)
        JLabel logoLabel = new JLabel("⚡");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 60));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Public Utility Billing System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Government Service Portal");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 210, 255));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(logoLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(subtitleLabel);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridx = 0;

        // Welcome text
        JLabel welcomeLabel = new JLabel("Sign In");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        panel.add(welcomeLabel, gbc);

        // Username
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 5, 0);
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        userLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        panel.add(userLabel, gbc);

        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(usernameField, gbc);

        // Password
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 0, 5, 0);
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        panel.add(passLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel.add(passwordField, gbc);

        // Error label
        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(ColorScheme.ERROR);
        gbc.gridy = 5;
        panel.add(errorLabel, gbc);

        // Login button
        StyledButton loginButton = new StyledButton("Sign In", StyledButton.ButtonType.PRIMARY);
        loginButton.setPreferredSize(new Dimension(200, 45));
        loginButton.addActionListener(e -> handleLogin());
        gbc.gridy = 6;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel.add(loginButton, gbc);

        // Forgot password link
        JLabel forgotPasswordLabel = new JLabel("<html><u>Forgot password?</u></html>");
        forgotPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgotPasswordLabel.setForeground(ColorScheme.PRIMARY);
        forgotPasswordLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotPasswordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openForgotPasswordDialog();
            }
        });
        gbc.gridy = 7;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel.add(forgotPasswordLabel, gbc);

        // Enter key listener
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });

        return panel;
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }

        // Authenticate user
        SwingWorker<Staff, Void> worker = new SwingWorker<>() {
            @Override
            protected Staff doInBackground() {
                return staffCRUD.getRecordByUsername(username);
            }

            @Override
            protected void done() {
                try {
                    Staff staff = get();
                    if (staff != null && PasswordUtils.verifyPassword(password, staff.passwordHash())) {
                        if (PasswordUtils.needsRehash(staff.passwordHash())) {
                            String newHash = PasswordUtils.hashPassword(password);
                            staffCRUD.updatePasswordHash(staff.staffID(), newHash);
                        }
                        // Successful login
                        dispose();
                        SwingUtilities.invokeLater(() -> {
                            MainFrame mainFrame = new MainFrame(staff);
                            mainFrame.setVisible(true);
                        });
                    } else {
                        showError("Invalid username or password");
                        passwordField.setText("");
                    }
                } catch (Exception e) {
                    showError("Login failed: " + e.getMessage());
                }
            }
        };

        worker.execute();
    }

    private void showError(String message) {
        errorLabel.setText(message);
    }

    private void openForgotPasswordDialog() {
        ForgotPasswordDialog dialog = new ForgotPasswordDialog(this);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
