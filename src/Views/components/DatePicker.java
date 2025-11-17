package Views.components;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date picker component using JSpinner (pure Java Swing)
 */
public class DatePicker extends JPanel {

    private JSpinner dateSpinner;
    private SimpleDateFormat dateFormat;

    public DatePicker() {
        this("yyyy-MM-dd");
    }

    public DatePicker(String format) {
        dateFormat = new SimpleDateFormat(format);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        initComponents();
    }

    private void initComponents() {
        // Create spinner with date model
        SpinnerDateModel model = new SpinnerDateModel();
        dateSpinner = new JSpinner(model);

        // Create custom editor with format
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, dateFormat.toPattern());
        dateSpinner.setEditor(editor);

        // Styling
        dateSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateSpinner.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        dateSpinner.setPreferredSize(new Dimension(150, 35));

        add(dateSpinner, BorderLayout.CENTER);
    }

    public Date getDate() {
        return (Date) dateSpinner.getValue();
    }

    public void setDate(Date date) {
        dateSpinner.setValue(date);
    }

    public java.sql.Date getSqlDate() {
        Date date = getDate();
        return new java.sql.Date(date.getTime());
    }

    public void setSqlDate(java.sql.Date sqlDate) {
        dateSpinner.setValue(new Date(sqlDate.getTime()));
    }

    public JSpinner getSpinner() {
        return dateSpinner;
    }
}