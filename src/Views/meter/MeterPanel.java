package Views.meter;

import Views.components.*;
import javax.swing.*;
import java.awt.*;

public class MeterPanel extends JPanel {
    public MeterPanel() {
        setLayout(new BorderLayout());
        setBackground(ColorScheme.BACKGROUND);
        JLabel label = new JLabel("Meter Management Panel", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(label, BorderLayout.CENTER);
    }

    public void refreshData() {
        // Implement data refresh
    }
}