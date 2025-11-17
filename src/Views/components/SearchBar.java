package Views.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Reusable search bar component with clear functionality
 */
public class SearchBar extends JPanel {

    private JTextField searchField;
    private JButton clearButton;
    private SearchListener searchListener;

    public interface SearchListener {
        void onSearch(String searchText);
        void onClear();
    }

    public SearchBar() {
        this(300);
    }

    public SearchBar(int width) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        setMaximumSize(new Dimension(width, 35));
        setPreferredSize(new Dimension(width, 35));

        initComponents();
    }

    private void initComponents() {
        // Search icon label
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        // Search text field
        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setBorder(null);
        searchField.setBackground(Color.WHITE);

        // Add key listener for real-time search
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (searchListener != null) {
                    searchListener.onSearch(searchField.getText());
                }
            }
        });

        // Clear button
        clearButton = new JButton("✕");
        clearButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        clearButton.setForeground(ColorScheme.TEXT_SECONDARY);
        clearButton.setBackground(Color.WHITE);
        clearButton.setBorderPainted(false);
        clearButton.setFocusPainted(false);
        clearButton.setContentAreaFilled(false);
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.setPreferredSize(new Dimension(25, 25));
        clearButton.setVisible(false);

        clearButton.addActionListener(e -> clearSearch());

        // Show/hide clear button based on text
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                clearButton.setVisible(!searchField.getText().isEmpty());
            }
        });

        add(searchIcon, BorderLayout.WEST);
        add(searchField, BorderLayout.CENTER);
        add(clearButton, BorderLayout.EAST);
    }

    public void setSearchListener(SearchListener listener) {
        this.searchListener = listener;
    }

    public String getSearchText() {
        return searchField.getText();
    }

    public void clearSearch() {
        searchField.setText("");
        clearButton.setVisible(false);
        if (searchListener != null) {
            searchListener.onClear();
        }
    }

    public void setPlaceholder(String placeholder) {
        searchField.setToolTipText(placeholder);
    }
}