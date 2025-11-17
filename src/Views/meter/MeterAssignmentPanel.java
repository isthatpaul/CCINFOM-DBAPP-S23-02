package Views.meter;

import Views.components.*;
import javax.swing.*;
import java.awt.*;

public class MeterAssignmentPanel extends JPanel {
    public MeterAssignmentPanel() {
        setLayout(new BorderLayout());
        setBackground(ColorScheme.BACKGROUND);
        JLabel label = new JLabel("Meter Assignment Panel", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(label, BorderLayout.CENTER);
    }

    public void refreshData() {
        // Implement data refresh
    }
}