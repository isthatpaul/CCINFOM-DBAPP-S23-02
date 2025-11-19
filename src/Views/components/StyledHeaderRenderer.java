package Views.components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * A temporary renderer to change the header background color for printing.
 * It inherits properties from the original header to maintain style consistency.
 */
public class StyledHeaderRenderer extends DefaultTableCellRenderer {
    
    public StyledHeaderRenderer(JTableHeader header) {
        setOpaque(true);
        setForeground(header.getForeground());
        setBackground(header.getBackground());
        setFont(header.getFont());
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        setHorizontalAlignment(CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setText(value != null ? value.toString() : "");
        return this;
    }
}