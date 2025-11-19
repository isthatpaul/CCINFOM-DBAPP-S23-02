package Views.components;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class SearchBar extends JPanel {
    private JTextField searchField;
    private StyledButton clearButton;
    private SearchListener searchListener;
    private String placeholder;
    private boolean isPlaceholderVisible = true;

    public interface SearchListener {
        void onSearch(String searchText);
        void onClear();
    }

    public SearchBar(int columns) {
        setLayout(new BorderLayout());
        
        searchField = new JTextField(columns);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 0, ColorScheme.BORDER),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        clearButton = new StyledButton("x", StyledButton.ButtonType.DANGER);
        clearButton.setPreferredSize(new Dimension(40, searchField.getPreferredSize().height));
        clearButton.setVisible(false);

        setPlaceholderLook();

        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (isPlaceholderVisible) {
                    searchField.setText("");
                    setRealTextLook();
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchField.getText().isEmpty()) {
                    setPlaceholderLook();
                }
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { handleTextChange(); }
            public void removeUpdate(DocumentEvent e) { handleTextChange(); }
            public void changedUpdate(DocumentEvent e) { handleTextChange(); }
        });

        clearButton.addActionListener(e -> clearSearch());
        
        add(searchField, BorderLayout.CENTER);
        add(clearButton, BorderLayout.EAST);
    }
    
    private void setPlaceholderLook() {
        searchField.setForeground(ColorScheme.TEXT_SECONDARY);
        searchField.setText(placeholder);
        isPlaceholderVisible = true;
    }
    
    private void setRealTextLook() {
        searchField.setForeground(ColorScheme.TEXT_PRIMARY);
        isPlaceholderVisible = false;
    }

    private void handleTextChange() {
        boolean hasText = !searchField.getText().isEmpty() && !isPlaceholderVisible;
        clearButton.setVisible(hasText);
        if (searchListener != null && hasText) {
            searchListener.onSearch(searchField.getText());
        }
    }
    
    public void setSearchListener(SearchListener listener) {
        this.searchListener = listener;
    }
    
    public String getSearchText() {
        return isPlaceholderVisible ? "" : searchField.getText();
    }

    public void setPlaceholder(String text) {
        this.placeholder = text;
        if(isPlaceholderVisible) {
            searchField.setText(placeholder);
        }
    }
    
    public void clearSearch() {
        searchField.setText("");
        setPlaceholderLook();
        if (searchListener != null) {
            searchListener.onClear(); // This is what causes the loop
        }
    }

    public void clearSearchText() {
        SwingUtilities.invokeLater(() -> {
            searchField.setText("");
            if (!searchField.hasFocus()) {
                setPlaceholderLook();
            }
        });
    }
}