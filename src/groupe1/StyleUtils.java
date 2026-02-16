package groupe1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StyleUtils {
    
    // Premium Color Palette (Harmonized)
    public static final Color PRIMARY_BLUE = new Color(28, 45, 90);    // Deep Navy (Sidebar/Header)
    public static final Color ACCENT_ORANGE = new Color(220, 80, 10);  // Vibrant Orange (Buttons/Highlights)
    public static final Color ACCENT_GOLD = new Color(245, 158, 11);   // Gold (Secondary Accent)
    public static final Color SUCCESS_GREEN = new Color(16, 185, 129); // Emerald
    public static final Color DANGER_RED = new Color(220, 38, 38);     // Red
    
    public static final Color BG_LIGHT = new Color(248, 249, 250);     // Very Soft Gray (Dashboard BG)
    public static final Color TEXT_DARK = new Color(33, 37, 41);       // Dark/Black
    public static final Color TEXT_GRAY = new Color(108, 117, 125);    // Muted Gray

    // Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);

    public static void configureGlobalAppearance() {
        try {
            // Set System Look and Feel first to get OS frame borders
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Override Key Properties for Web Look
            UIManager.put("Panel.background", Color.WHITE);
            UIManager.put("Viewport.background", Color.WHITE);
            UIManager.put("TextField.font", FONT_BODY);
            UIManager.put("PasswordField.font", FONT_BODY);
            UIManager.put("Label.font", FONT_BODY);
            UIManager.put("Button.font", FONT_BOLD);
            
            // ScrollBar CSS-like styling
            UIManager.put("ScrollBar.width", 10);
            UIManager.put("ScrollBar.thumb", new Color(190, 190, 190));
            UIManager.put("ScrollBar.track", new Color(245, 245, 245));
            UIManager.put("ScrollBar.thumbDarkShadow", new Color(190, 190, 190));
            UIManager.put("ScrollBar.thumbHighlight", new Color(190, 190, 190));
            UIManager.put("ScrollBar.thumbShadow", new Color(190, 190, 190));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Custom Web-Style ScrollBar UI
    public static class WebScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(180, 180, 180);
            this.trackColor = new Color(250, 250, 250);
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(0, 0));
            btn.setMinimumSize(new Dimension(0, 0));
            btn.setMaximumSize(new Dimension(0, 0));
            return btn;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);
        }
        
        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            g.setColor(trackColor);
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(45); // More breathing room like web tables
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setGridColor(new Color(240, 240, 240));
        table.setFont(FONT_BODY);
        table.setSelectionBackground(new Color(237, 242, 247)); // Very light blue
        table.setSelectionForeground(TEXT_DARK);
        
        // Header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(100, 116, 139)); // Slate gray
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));
        header.setPreferredSize(new Dimension(0, 40));
        
        // Remove default focus border
        table.setFocusable(false);
        
        // Apply Custom ScrollBar to parent ScrollPane if exists
        Container parent = table.getParent();
        if (parent instanceof JViewport) {
            Container grandParent = parent.getParent();
            if (grandParent instanceof JScrollPane) {
                JScrollPane scroll = (JScrollPane) grandParent;
                scroll.getVerticalScrollBar().setUI(new WebScrollBarUI());
                scroll.getHorizontalScrollBar().setUI(new WebScrollBarUI());
                scroll.setBorder(BorderFactory.createEmptyBorder()); // Remove bold border
                scroll.getViewport().setBackground(Color.WHITE);
            }
        }
    }

    public static JButton createModernButton(String text, Color bgColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bgColor.brighter());
                } else {
                    g2.setColor(bgColor);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(150, 40));
        return btn;
    }

    public static JTextField createModernTextField() {
        JTextField field = new JTextField();
        field.setFont(FONT_BODY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true), 
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }
}
