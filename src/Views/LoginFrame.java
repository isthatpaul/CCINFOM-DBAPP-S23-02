package Views;

import Views.components.*;
import Model.Staff;
import Model.StaffCRUD;
import Services.PasswordUtils;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

/**
 * Login screen for the Public Utility Billing System.
 * Professional government service portal design.
 */
public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;
    private StaffCRUD staffCRUD;
    private StyledButton loginButton;
    private JCheckBox showPasswordCheckbox;

    // Simple rate limiting - track failed login attempts
    private int failedAttempts = 0;
    private long lastFailedAttemptTime = 0;
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCKOUT_DURATION_MS = 30000; // 30 seconds

    // Government color scheme
    private static final Color GOV_BLUE = new Color(30, 58, 138);
    private static final Color GOV_BLUE_LIGHT = new Color(59, 130, 246);
    private static final Color GOV_HEADER = new Color(17, 24, 39);
    private static final Color GOV_BG = new Color(243, 244, 246);

    public LoginFrame() {
        staffCRUD = new StaffCRUD();
        initComponents();
    }

    private void initComponents() {
        setTitle("Public Utility Billing System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 820);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(GOV_BG);

        // Top government banner
        JPanel bannerPanel = createBannerPanel();

        // Logo/Header Panel
        JPanel headerPanel = createHeaderPanel();

        // Login Form Panel
        JPanel formPanel = createFormPanel();

        // Footer Panel
        JPanel footerPanel = createFooterPanel();

        // Center container for header + form
        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setOpaque(false);
        centerContainer.add(headerPanel, BorderLayout.NORTH);
        centerContainer.add(formPanel, BorderLayout.CENTER);

        mainPanel.add(bannerPanel, BorderLayout.NORTH);
        mainPanel.add(centerContainer, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createBannerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(GOV_HEADER);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel bannerLabel = new JLabel("GOVERNMENT SERVICE PORTAL");
        bannerLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        bannerLabel.setForeground(Color.WHITE);
        panel.add(bannerLabel, BorderLayout.WEST);

        return panel;
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(GOV_BLUE);
        panel.setBorder(BorderFactory.createEmptyBorder(60, 30, 60, 30));

        JLabel titleLabel = new JLabel("Public Utility Billing System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setOpaque(false);
        outerPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 60, 50, 60));
        panel.setPreferredSize(new Dimension(670, 450));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Sign In header
        JLabel welcomeLabel = new JLabel("Sign In to Your Account");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(GOV_HEADER);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel.add(welcomeLabel, gbc);

        JLabel instructionLabel = new JLabel("Enter your credentials to access the system");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        instructionLabel.setForeground(new Color(107, 114, 128));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        panel.add(instructionLabel, gbc);

        // Username
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 8, 0);
        JLabel userLabel = new JLabel("USERNAME");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        userLabel.setForeground(new Color(55, 65, 81));
        panel.add(userLabel, gbc);

        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        usernameField.setPreferredSize(new Dimension(0, 48));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                new GovFieldBorder(),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 18, 0);
        panel.add(usernameField, gbc);

        // Password
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 0, 8, 0);
        JLabel passLabel = new JLabel("PASSWORD");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        passLabel.setForeground(new Color(55, 65, 81));
        panel.add(passLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        passwordField.setPreferredSize(new Dimension(0, 48));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                new GovFieldBorder(),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 12, 0);
        panel.add(passwordField, gbc);

        // Show password checkbox
        showPasswordCheckbox = new JCheckBox("Show password");
        showPasswordCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPasswordCheckbox.setForeground(new Color(75, 85, 99));
        showPasswordCheckbox.setOpaque(false);
        showPasswordCheckbox.setFocusPainted(false);
        showPasswordCheckbox.addActionListener(e -> togglePasswordVisibility());
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(showPasswordCheckbox, gbc);

        // Error label
        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        errorLabel.setForeground(new Color(220, 38, 38));
        errorLabel.setPreferredSize(new Dimension(0, 24));
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 12, 0);
        panel.add(errorLabel, gbc);

        // Login button
        loginButton = new StyledButton("Sign In", StyledButton.ButtonType.PRIMARY);
        loginButton.setPreferredSize(new Dimension(0, 50));
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginButton.addActionListener(e -> handleLogin());
        gbc.gridy = 8;
        gbc.insets = new Insets(5, 0, 18, 0);
        panel.add(loginButton, gbc);

        // Forgot password link
        JLabel forgotPasswordLabel = new JLabel("<html><u>Forgot password?</u></html>");
        forgotPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        forgotPasswordLabel.setForeground(GOV_BLUE_LIGHT);
        forgotPasswordLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotPasswordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openForgotPasswordDialog();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                forgotPasswordLabel.setForeground(GOV_BLUE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                forgotPasswordLabel.setForeground(GOV_BLUE_LIGHT);
            }
        });
        gbc.gridy = 9;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(forgotPasswordLabel, gbc);

        outerPanel.add(panel);
        return outerPanel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 18));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(229, 231, 235)));

        JLabel copyrightLabel = new JLabel("2025 Public Utility Billing System");
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        copyrightLabel.setForeground(new Color(107, 114, 128));

        JLabel separatorLabel = new JLabel("•");
        separatorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        separatorLabel.setForeground(new Color(156, 163, 175));

        JLabel versionLabel = new JLabel("Version 3.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        versionLabel.setForeground(new Color(156, 163, 175));

        panel.add(copyrightLabel);
        panel.add(separatorLabel);
        panel.add(versionLabel);

        return panel;
    }

    private void togglePasswordVisibility() {
        if (showPasswordCheckbox.isSelected()) {
            passwordField.setEchoChar((char) 0);
        } else {
            passwordField.setEchoChar('•');
        }
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        char[] passwordChars = passwordField.getPassword();

        try {
            // Check if account is temporarily locked
            if (isAccountLocked()) {
                long remainingTime = (LOCKOUT_DURATION_MS - (System.currentTimeMillis() - lastFailedAttemptTime)) / 1000;
                showError("Too many failed attempts. Please try again in " + remainingTime + " seconds.");
                return;
            }

            // Validate input
            if (username.isEmpty() || passwordChars.length == 0) {
                showError("Please enter both username and password.");
                return;
            }

            // Validate username format
            if (username.length() < 3 || username.length() > 50) {
                showError("Invalid credentials.");
                return;
            }

            // Validate password length
            if (passwordChars.length < 8) {
                showError("Invalid credentials.");
                return;
            }

            // Convert password to String for verification (will be cleared after use)
            String password = new String(passwordChars);

            // Disable login button during authentication
            loginButton.setEnabled(false);
            loginButton.setText("Authenticating...");
            errorLabel.setText("");

            // Authenticate user in background thread
            SwingWorker<Staff, Void> worker = new SwingWorker<>() {
                @Override
                protected Staff doInBackground() {
                    try {
                        return staffCRUD.getRecordByUsername(username);
                    } catch (Exception e) {
                        // Log the error but don't expose details to user
                        System.err.println("Database error during login: " + e.getMessage());
                        return null;
                    }
                }

                @Override
                protected void done() {
                    try {
                        Staff staff = get();
                        
                        if (staff != null && PasswordUtils.verifyPassword(password, staff.passwordHash())) {
                            // Successful login - reset failed attempts
                            failedAttempts = 0;
                            lastFailedAttemptTime = 0;

                            // Upgrade password hash if using legacy format
                            if (PasswordUtils.needsRehash(staff.passwordHash())) {
                                try {
                                    String newHash = PasswordUtils.hashPassword(password);
                                    staffCRUD.updatePasswordHash(staff.staffID(), newHash);
                                } catch (Exception e) {
                                    // Log error but don't prevent login
                                    System.err.println("Failed to upgrade password hash: " + e.getMessage());
                                }
                            }

                            // Clear password field before switching screens
                            clearPasswordField();

                            // Close login frame and open main application
                            dispose();
                            SwingUtilities.invokeLater(() -> {
                                MainFrame mainFrame = new MainFrame(staff);
                                mainFrame.setVisible(true);
                            });
                        } else {
                            // Failed login - increment counter
                            handleFailedLogin();
                        }
                    } catch (Exception e) {
                        // Generic error message to prevent information disclosure
                        showError("An error occurred. Please try again.");
                        System.err.println("Login error: " + e.getMessage());
                    } finally {
                        // Always clear password from memory
                        clearPassword(password);
                        clearPasswordField();
                        loginButton.setEnabled(true);
                        loginButton.setText("Sign In");
                    }
                }
            };

            worker.execute();

        } finally {
            // Always clear password char array from memory
            if (passwordChars != null) {
                Arrays.fill(passwordChars, '\0');
            }
        }
    }

    private boolean isAccountLocked() {
        if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
            long timeSinceLastFailed = System.currentTimeMillis() - lastFailedAttemptTime;
            if (timeSinceLastFailed < LOCKOUT_DURATION_MS) {
                return true;
            } else {
                // Reset after lockout period expires
                failedAttempts = 0;
                lastFailedAttemptTime = 0;
            }
        }
        return false;
    }

    private void handleFailedLogin() {
        failedAttempts++;
        lastFailedAttemptTime = System.currentTimeMillis();

        if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
            showError("Too many failed attempts. Account temporarily locked.");
        } else {
            // Use generic message to prevent username enumeration
            showError("Invalid credentials. Please try again.");
        }

        clearPasswordField();
    }

    private void clearPassword(String password) {
        if (password != null) {
            // Overwrite string content in memory (best effort)
            char[] chars = password.toCharArray();
            Arrays.fill(chars, '\0');
        }
    }

    private void clearPasswordField() {
        if (passwordField != null) {
            char[] pwd = passwordField.getPassword();
            if (pwd != null) {
                Arrays.fill(pwd, '\0');
            }
            passwordField.setText("");
            showPasswordCheckbox.setSelected(false);
            passwordField.setEchoChar('•');
        }
    }

    private void showError(String message) {
        errorLabel.setText("⚠ " + message);
    }

    private void openForgotPasswordDialog() {
        ForgotPasswordDialog dialog = new ForgotPasswordDialog(this);
        dialog.setVisible(true);
    }

    // Custom border for government-style input fields
    private static class GovFieldBorder extends AbstractBorder {
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (c.hasFocus()) {
                g2d.setColor(new Color(59, 130, 246));
                g2d.setStroke(new BasicStroke(2));
            } else {
                g2d.setColor(new Color(209, 213, 219));
                g2d.setStroke(new BasicStroke(1));
            }
            
            g2d.drawRoundRect(x, y, width - 1, height - 1, 6, 6);
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(1, 1, 1, 1);
        }
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