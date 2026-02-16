package groupe1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(StyleUtils.BG_LIGHT);
        setOpaque(false);

        // Stats Cards Panel avec animations
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 25, 0));
        statsPanel.setOpaque(false);
        statsPanel.setPreferredSize(new Dimension(0, 180));

        // Cartes KPI avec icônes et animations
        JPanel membersCard = createPremiumStatCard("Total Membres", "150", "+12%", true,
                StyleUtils.PRIMARY_BLUE, IconUtils.createUsersIcon(32, Color.WHITE));
        JPanel moneyCard = createPremiumStatCard("Montant Collecté", "2 500 000 FCFA", "+8.5%", true,
                StyleUtils.SUCCESS_GREEN, IconUtils.createMoneyIcon(32, Color.WHITE));
        JPanel cyclesCard = createPremiumStatCard("Tontines en cours", "5", "+2", true,
                StyleUtils.ACCENT_GOLD, IconUtils.createWalletIcon(32, Color.WHITE));

        statsPanel.add(membersCard);
        statsPanel.add(moneyCard);
        statsPanel.add(cyclesCard);

        add(statsPanel, BorderLayout.NORTH);

        // Content Split (Chart + Table)
        JPanel contentSplit = new JPanel(new GridLayout(2, 1, 0, 20));
        contentSplit.setOpaque(false);

        // 1. Chart Section (2 Columns)
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        chartsPanel.setOpaque(false);
        chartsPanel.add(new EvolutionChartPanel());
        chartsPanel.add(new PieChartPanel());

        contentSplit.add(chartsPanel);

        // 2. Recent Activity Section avec amélioration visuelle
        JPanel activityPanel = createActivityPanel();
        contentSplit.add(activityPanel);

        add(contentSplit, BorderLayout.CENTER);
    }

    /**
     * Crée une carte KPI premium avec animations et icônes
     */
    private JPanel createPremiumStatCard(String title, String value, String trend, boolean trendPositive,
            Color accentColor, Icon icon) {
        JPanel card = new JPanel(new BorderLayout()) {
            private boolean hovered = false;

            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        hovered = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        hovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Ombre portée dynamique
                int shadowOffset = hovered ? 8 : 4;
                int shadowAlpha = hovered ? 50 : 25;
                g2.setColor(new Color(0, 0, 0, shadowAlpha));
                g2.fillRoundRect(shadowOffset, shadowOffset, getWidth() - shadowOffset,
                        getHeight() - shadowOffset, 20, 20);

                // Gradient de fond subtil
                GradientPaint gradient = new GradientPaint(
                        0, 0, Color.WHITE,
                        0, getHeight(), new Color(252, 252, 254));
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Bande d'accent avec gradient
                GradientPaint accentGradient = new GradientPaint(
                        0, 0, accentColor,
                        0, getHeight(), accentColor.darker());
                g2.setPaint(accentGradient);
                g2.fillRoundRect(0, 0, 10, getHeight(), 20, 20);
                g2.fillRect(5, 0, 5, getHeight());

                // Bordure subtile
                g2.setColor(new Color(229, 231, 235));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

                // Effet de brillance en haut (glassmorphism)
                g2.setColor(new Color(255, 255, 255, 60));
                g2.fillRoundRect(10, 5, getWidth() - 20, getHeight() / 3, 15, 15);
            }
        };

        card.setOpaque(false);
        card.setBorder(new EmptyBorder(15, 25, 15, 20));

        // Panel supérieur avec icône et tendance
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        // Icône dans un cercle coloré
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Cercle avec gradient
                GradientPaint gradient = new GradientPaint(
                        0, 0, accentColor,
                        getWidth(), getHeight(), accentColor.darker());
                g2.setPaint(gradient);
                g2.fillOval(0, 0, 48, 48);

                // Icône
                if (icon != null) {
                    icon.paintIcon(this, g2, 8, 8);
                }
            }
        };
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(48, 48));

        // Badge de tendance
        Color trendColor = trendPositive ? StyleUtils.SUCCESS_GREEN : StyleUtils.DANGER_RED;
        Icon trendIcon = trendPositive ? IconUtils.createArrowUpIcon(12, trendColor)
                : IconUtils.createArrowDownIcon(12, trendColor);

        JPanel trendPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        trendPanel.setOpaque(false);

        JLabel trendIconLabel = new JLabel(trendIcon);
        JLabel trendLabel = new JLabel(trend);
        trendLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trendLabel.setForeground(trendColor);

        trendPanel.add(trendIconLabel);
        trendPanel.add(trendLabel);

        topPanel.add(iconPanel, BorderLayout.WEST);
        topPanel.add(trendPanel, BorderLayout.EAST);

        // Panel central avec titre et valeur
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(StyleUtils.TEXT_GRAY);
        titleLabel.setFont(StyleUtils.FONT_BODY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(StyleUtils.TEXT_DARK);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(valueLabel);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);

        // Animation de compteur au chargement (simulé avec un timer)
        if (value.matches("\\d+.*")) {
            try {
                String numStr = value.replaceAll("[^0-9]", "");
                if (!numStr.isEmpty()) {
                    int targetValue = Integer.parseInt(numStr);
                    AnimationUtils.animateCounter(valueLabel, 0, targetValue, 1500,
                            value.substring(numStr.length()));
                }
            } catch (Exception e) {
                // Si l'extraction échoue, afficher la valeur normale
            }
        }

        return card;
    }

    /**
     * Crée le panneau d'activités récentes avec style amélioré
     */
    private JPanel createActivityPanel() {
        JPanel activityPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Ombre portée
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 15, 15);

                // Fond avec gradient
                GradientPaint gradient = new GradientPaint(
                        0, 0, Color.WHITE,
                        0, getHeight(), new Color(252, 252, 254));
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Bordure
                g2.setColor(new Color(229, 231, 235));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        };
        activityPanel.setOpaque(false);

        // Header avec icône
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(0, 60));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel headerLabel = new JLabel("Activités Récentes");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(StyleUtils.TEXT_DARK);
        headerLabel.setIcon(IconUtils.createDashboardIcon(20, StyleUtils.ACCENT_ORANGE));
        headerLabel.setIconTextGap(10);

        headerPanel.add(headerLabel, BorderLayout.WEST);

        activityPanel.add(headerPanel, BorderLayout.NORTH);

        // Table avec données
        String[] columnNames = { "Date", "Membre", "Type", "Montant" };
        Object[][] data = {
                { "16/02/2026", "Jean Dupont", "Cotisation", "10 000 FCFA" },
                { "15/02/2026", "Marie Curie", "Retrait", "50 000 FCFA" },
                { "14/02/2026", "Paul Martin", "Cotisation", "15 000 FCFA" },
                { "12/02/2026", "Alice Wonderland", "Cotisation", "20 000 FCFA" }
        };

        JTable table = new JTable(data, columnNames);
        StyleUtils.styleTable(table);

        // Renderer personnalisé pour les badges de type
        table.getColumnModel().getColumn(2).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (value != null) {
                    String type = value.toString();
                    JLabel label = (JLabel) c;
                    label.setOpaque(true);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setFont(new Font("Segoe UI", Font.BOLD, 12));

                    if (type.equals("Cotisation")) {
                        label.setBackground(new Color(16, 185, 129, 30));
                        label.setForeground(new Color(16, 185, 129));
                    } else if (type.equals("Retrait")) {
                        label.setBackground(new Color(220, 38, 38, 30));
                        label.setForeground(new Color(220, 38, 38));
                    } else {
                        label.setBackground(new Color(245, 158, 11, 30));
                        label.setForeground(new Color(245, 158, 11));
                    }

                    label.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(label.getForeground(), 1, true),
                            BorderFactory.createEmptyBorder(4, 12, 4, 12)));
                }

                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        activityPanel.add(scrollPane, BorderLayout.CENTER);

        return activityPanel;
    }
}
