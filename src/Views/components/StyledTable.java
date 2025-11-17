package Views.components;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * Custom styled JTable with professional appearance.
 */
public class StyledTable extends JTable {

    public StyledTable(TableModel model) {
        super(model);
        styleTable();
    }

    private void styleTable() {
        // Font
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setRowHeight(35);
        setShowGrid(true);
        setGridColor(ColorScheme.BORDER);
        setSelectionBackground(ColorScheme.TABLE_SELECTION);
        setSelectionForeground(ColorScheme.TEXT_PRIMARY);
        setIntercellSpacing(new Dimension(1, 1));

        // Header styling
        JTableHeader header = getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(ColorScheme.TABLE_HEADER_BG);
        header.setForeground(ColorScheme.TABLE_HEADER_FG);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));

        // Alternating row colors
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? ColorScheme.TABLE_ROW_EVEN : ColorScheme.TABLE_ROW_ODD);
                }

                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return c;
            }
        });

        // Auto resize
        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    /**
     * Set up date column renderer
     */
    public void setDateColumnRenderer(int columnIndex) {
        getColumnModel().getColumn(columnIndex).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof java.sql.Date) {
                    value = new java.text.SimpleDateFormat("MMM dd, yyyy").format((java.sql.Date) value);
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
    }

    /**
     * Set up currency column renderer
     */
    public void setCurrencyColumnRenderer(int columnIndex) {
        getColumnModel().getColumn(columnIndex).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Number) {
                    value = String.format("₱%.2f", ((Number) value).doubleValue());
                }
                setHorizontalAlignment(SwingConstants.RIGHT);
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
    }
}