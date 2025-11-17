package Views.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Custom styled button with consistent appearance and hover effects.
 */
public class StyledButton extends JButton {

    public enum ButtonType {
        PRIMARY, SECONDARY, SUCCESS, WARNING, DANGER
    }

    private Color baseColor;
    private Color hoverColor;
    private ButtonType type;

    public StyledButton(String text) {
        this(text, ButtonType.PRIMARY);
    }

    public StyledButton(String text, ButtonType type) {
        super(text);
        this.type = type;
        setButtonColors(type);
        styleButton();
    }

    public StyledButton(String text, Icon icon, ButtonType type) {
        super(text, icon);
        this.type = type;
        setButtonColors(type);
        styleButton();
    }

    private void setButtonColors(ButtonType type) {
        switch (type) {
            case PRIMARY:
                baseColor = Views.components.ColorScheme.PRIMARY;
                hoverColor = ColorScheme.PRIMARY_HOVER;
                break;
            case SECONDARY:
                baseColor = ColorScheme.SECONDARY;
                hoverColor = ColorScheme.SECONDARY.darker();
                break;
            case SUCCESS:
                baseColor = ColorScheme.SUCCESS;
                hoverColor = ColorScheme.SUCCESS_HOVER;
                break;
            case WARNING:
                baseColor = ColorScheme.WARNING;
                hoverColor = ColorScheme.WARNING_HOVER;
                break;
            case DANGER:
                baseColor = ColorScheme.ERROR;
                hoverColor = ColorScheme.ERROR_HOVER;
                break;
        }
    }

    private void styleButton() {
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setBackground(baseColor);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(true);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Padding
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    setBackground(hoverColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(baseColor);
            }
        });
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            setBackground(ColorScheme.TEXT_LIGHT);
        } else {
            setBackground(baseColor);
        }
    }
}