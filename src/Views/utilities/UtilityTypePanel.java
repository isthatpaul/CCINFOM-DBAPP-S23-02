package Views.utilities;

import Views.components.*;
import javax.swing.*;
import java.awt.*;

public class UtilityTypePanel extends JPanel {
    public UtilityTypePanel() {
        setLayout(new BorderLayout());
        setBackground(ColorScheme.BACKGROUND);
        JLabel label = new JLabel("Utility Type Management Panel", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(label, BorderLayout.CENTER);
    }

    public void refreshData() {
        // Implement data refresh
    }
}