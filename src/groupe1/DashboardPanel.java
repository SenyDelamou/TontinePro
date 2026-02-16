package groupe1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(StyleUtils.BG_LIGHT);
        setOpaque(false); // Let parent bg show

        // Stats Cards Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 25, 0));
        statsPanel.setOpaque(false);
        statsPanel.setPreferredSize(new Dimension(0, 140));

        statsPanel.add(createStatCard("Total Membres", "150", StyleUtils.PRIMARY_BLUE));
        statsPanel.add(createStatCard("Montant Collecté", "2 500 000 FCFA", StyleUtils.SUCCESS_GREEN));
        statsPanel.add(createStatCard("Tontines en cours", "5", StyleUtils.ACCENT_GOLD));

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

        // 2. Recent Activity Section
        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.setBackground(Color.WHITE);
        activityPanel.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        
        JLabel headerLabel = new JLabel("  Activités Récentes");
        headerLabel.setFont(StyleUtils.FONT_SUBTITLE);
        headerLabel.setPreferredSize(new Dimension(0, 50));
        headerLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)));
        activityPanel.add(headerLabel, BorderLayout.NORTH);
        
        String[] columnNames = {"Date", "Membre", "Type", "Montant"};
        Object[][] data = {
            {"16/02/2026", "Jean Dupont", "Cotisation", "10 000 FCFA"},
            {"15/02/2026", "Marie Curie", "Retrait", "50 000 FCFA"},
            {"14/02/2026", "Paul Martin", "Cotisation", "15 000 FCFA"},
            {"12/02/2026", "Alice Wonderland", "Cotisation", "20 000 FCFA"}
        };
        
        JTable table = new JTable(data, columnNames);
        StyleUtils.styleTable(table);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        activityPanel.add(scrollPane, BorderLayout.CENTER);
        
        contentSplit.add(activityPanel);

        add(contentSplit, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout()) {
             @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Colored Stripe on left
                g2.setColor(color);
                g2.fillRoundRect(0, 0, 10, getHeight(), 15, 15);
                g2.fillRect(5, 0, 5, getHeight()); // Flatten right side of stripe
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(15, 25, 15, 15)); // Add padding left for stripe
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(StyleUtils.TEXT_GRAY);
        titleLabel.setFont(StyleUtils.FONT_BODY);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(StyleUtils.TEXT_DARK);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
}
