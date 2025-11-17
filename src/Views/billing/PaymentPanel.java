package Views.billing;

import Views.components.*;
import Model.Staff;
import javax.swing.*;
import java.awt.*;

public class PaymentPanel extends JPanel {
    public PaymentPanel(Staff staff) {
        setLayout(new BorderLayout());
        setBackground(ColorScheme.BACKGROUND);
        JLabel label = new JLabel("Payment Processing Panel", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(label, BorderLayout.CENTER);
    }

    public void refreshData() {
        // Implement data refresh
    }
}