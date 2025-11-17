package Views.employee;

import Views.components.*;
import javax.swing.*;
import java.awt.*;

public class EmployeePanel extends JPanel {
    public EmployeePanel() {
        setLayout(new BorderLayout());
        setBackground(ColorScheme.BACKGROUND);
        JLabel label = new JLabel("Employee Management Panel", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(label, BorderLayout.CENTER);
    }

    public void refreshData() {
        // Implement data refresh
    }
}