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
    public static final Color PRIMARY_BLUE = new Color(28, 45, 90); // Deep Navy (Sidebar/Header)
    public static final Color ACCENT_ORANGE = new Color(220, 80, 10); // Vibrant Orange (Buttons/Highlights)
    public static final Color ACCENT_GOLD = new Color(245, 158, 11); // Gold (Secondary Accent)
    public static final Color SUCCESS_GREEN = new Color(16, 185, 129); // Emerald
    public static final Color DANGER_RED = new Color(220, 38, 38); // Red

    public static final Color BG_LIGHT = new Color(248, 249, 250); // Very Soft Gray (Dashboard BG)
    public static final Color TEXT_DARK = new Color(33, 37, 41); // Dark/Black
    public static final Color TEXT_GRAY = new Color(108, 117, 125); // Muted Gray

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
        table.setFont(FONT_BODY);
        table.setRowHeight(45);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(220, 80, 10, 30));
        table.setSelectionForeground(TEXT_DARK);

        // Alternance de couleurs pour les lignes
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
                }
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return c;
            }
        });

        // Header styling
        table.getTableHeader().setFont(FONT_BOLD);
        table.getTableHeader().setBackground(BG_LIGHT);
        table.getTableHeader().setForeground(TEXT_DARK);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(229, 231, 235)));
        table.getTableHeader().setPreferredSize(new Dimension(0, 50));

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
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Ombre portée
                if (getModel().isPressed()) {
                    g2.setColor(new Color(0, 0, 0, 30));
                    g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 12, 12);
                } else {
                    g2.setColor(new Color(0, 0, 0, 20));
                    g2.fillRoundRect(0, 3, getWidth(), getHeight(), 12, 12);
                }

                // Fond du bouton
                if (getModel().isRollover()) {
                    g2.setColor(bgColor.brighter());
                } else {
                    g2.setColor(bgColor);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                // Texte
                g2.setColor(getForeground());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };

        button.setFont(FONT_BODY);
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 40));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        return button;
    }

    public static JTextField createModernTextField() {
        JTextField field = new JTextField();
        field.setFont(FONT_BODY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        return field;
    }

    // ========== PREMIUM ADDITIONS ==========

    /**
     * Crée un gradient linéaire premium
     */
    public static GradientPaint createGradientPaint(int x1, int y1, Color color1, int x2, int y2, Color color2) {
        return new GradientPaint(x1, y1, color1, x2, y2, color2);
    }

    /**
     * Crée un gradient radial pour effets de profondeur
     */
    public static RadialGradientPaint createRadialGradient(int centerX, int centerY, int radius, Color centerColor,
            Color edgeColor) {
        float[] fractions = { 0.0f, 1.0f };
        Color[] colors = { centerColor, edgeColor };
        return new RadialGradientPaint(centerX, centerY, radius, fractions, colors);
    }

    /**
     * Crée un panneau avec effet glassmorphism
     */
    public static JPanel createGlassmorphicPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fond semi-transparent
                g2.setColor(new Color(255, 255, 255, 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Bordure subtile
                g2.setColor(new Color(255, 255, 255, 100));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);
            }
        };
    }

    /**
     * Crée une carte premium avec ombre et gradient
     */
    public static JPanel createPremiumCard(Color accentColor) {
        return new JPanel() {
            private boolean hovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Ombre portée (plus prononcée au survol)
                int shadowOffset = hovered ? 6 : 3;
                int shadowAlpha = hovered ? 40 : 20;
                g2.setColor(new Color(0, 0, 0, shadowAlpha));
                g2.fillRoundRect(shadowOffset, shadowOffset, getWidth(), getHeight(), 15, 15);

                // Fond blanc avec gradient subtil
                GradientPaint gradient = new GradientPaint(
                        0, 0, Color.WHITE,
                        0, getHeight(), new Color(250, 250, 252));
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Bande d'accent colorée
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 0, 8, getHeight(), 15, 15);
                g2.fillRect(4, 0, 4, getHeight());

                // Bordure subtile
                g2.setColor(new Color(229, 231, 235));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        };
    }

    /**
     * Crée un bouton premium avec gradient et animations
     */
    public static JButton createPremiumButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            private boolean hovered = false;
            private boolean pressed = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                        repaint();
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        pressed = true;
                        repaint();
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        pressed = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Ombre portée
                if (!pressed) {
                    g2.setColor(new Color(0, 0, 0, 30));
                    g2.fillRoundRect(0, 4, getWidth(), getHeight() - 2, 12, 12);
                }

                // Gradient de fond
                Color topColor = hovered ? baseColor.brighter() : baseColor;
                Color bottomColor = hovered ? baseColor : baseColor.darker();

                GradientPaint gradient = new GradientPaint(
                        0, 0, topColor,
                        0, getHeight(), bottomColor);
                g2.setPaint(gradient);

                int yOffset = pressed ? 2 : 0;
                g2.fillRoundRect(0, yOffset, getWidth(), getHeight() - yOffset, 12, 12);

                // Effet de brillance en haut
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRoundRect(2, yOffset + 2, getWidth() - 4, getHeight() / 2, 10, 10);

                // Texte
                g2.setColor(getForeground());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2 + yOffset;
                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };

        button.setFont(FONT_BOLD);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 42));

        return button;
    }

    /**
     * Crée un champ de texte premium avec animation de focus
     */
    public static JTextField createPremiumTextField(String placeholder) {
        JTextField field = new JTextField() {
            private boolean focused = false;

            {
                addFocusListener(new java.awt.event.FocusAdapter() {
                    @Override
                    public void focusGained(java.awt.event.FocusEvent e) {
                        focused = true;
                        repaint();
                    }

                    @Override
                    public void focusLost(java.awt.event.FocusEvent e) {
                        focused = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fond
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Bordure colorée au focus
                if (focused) {
                    g2.setColor(ACCENT_ORANGE);
                    g2.setStroke(new BasicStroke(2f));
                } else {
                    g2.setColor(new Color(209, 213, 219));
                    g2.setStroke(new BasicStroke(1f));
                }

                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
            }
        };

        field.setFont(FONT_BODY);
        field.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        field.setOpaque(false);

        // Placeholder
        if (placeholder != null && !placeholder.isEmpty()) {
            field.setText(placeholder);
            field.setForeground(Color.GRAY);

            field.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent e) {
                    if (field.getText().equals(placeholder)) {
                        field.setText("");
                        field.setForeground(TEXT_DARK);
                    }
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    if (field.getText().isEmpty()) {
                        field.setText(placeholder);
                        field.setForeground(Color.GRAY);
                    }
                }
            });
        }

        return field;
    }

    /**
     * Applique un effet de lueur (glow) à un composant
     */
    public static void paintGlowEffect(Graphics2D g2, int x, int y, int width, int height, Color glowColor,
            int radius) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Plusieurs couches pour créer l'effet de lueur
        for (int i = radius; i > 0; i--) {
            int alpha = (int) (30 * (1.0 - (double) i / radius));
            g2.setColor(new Color(glowColor.getRed(), glowColor.getGreen(), glowColor.getBlue(), alpha));
            g2.fillRoundRect(x - i, y - i, width + 2 * i, height + 2 * i, 15 + i, 15 + i);
        }
    }

    /**
     * Crée un loader/spinner élégant
     */
    public static JPanel createLoadingSpinner(int size, Color color) {
        return new JPanel() {
            private int rotation = 0;
            private Timer timer;

            {
                setOpaque(false);
                setPreferredSize(new Dimension(size, size));

                timer = new Timer(30, e -> {
                    rotation = (rotation + 6) % 360;
                    repaint();
                });
                timer.start();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int radius = size / 2 - 4;

                g2.rotate(Math.toRadians(rotation), centerX, centerY);

                // Arc de cercle
                g2.setColor(color);
                g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawArc(centerX - radius, centerY - radius, radius * 2, radius * 2, 0, 270);
            }
        };
    }
}
