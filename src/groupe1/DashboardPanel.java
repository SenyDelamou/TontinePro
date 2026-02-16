package groupe1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class DashboardPanel extends JPanel {

    private JLabel membersValue;
    private JLabel moneyValue;
    private JLabel cyclesValue;
    private DefaultTableModel activityModel;

    public DashboardPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(StyleUtils.BG_LIGHT);
        setOpaque(false);

        // Stats Cards Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 25, 0));
        statsPanel.setOpaque(false);
        statsPanel.setPreferredSize(new Dimension(0, 180));

        // Create cards (we will set values later)
        JPanel membersCard = createPremiumStatCard("Total Membres", "0", "Aujourd'hui", true,
                StyleUtils.PRIMARY_BLUE, IconUtils.createUsersIcon(32, Color.WHITE), "members");
        JPanel moneyCard = createPremiumStatCard("Montant Collecté", "0 GNF", "Total", true,
                StyleUtils.SUCCESS_GREEN, IconUtils.createMoneyIcon(32, Color.WHITE), "money");
        JPanel cyclesCard = createPremiumStatCard("Tontines en cours", "0", "Actives", true,
                StyleUtils.ACCENT_GOLD, IconUtils.createWalletIcon(32, Color.WHITE), "cycles");

        statsPanel.add(membersCard);
        statsPanel.add(moneyCard);
        statsPanel.add(cyclesCard);

        add(statsPanel, BorderLayout.NORTH);

        // Content Split
        JPanel contentSplit = new JPanel(new GridLayout(2, 1, 0, 20));
        contentSplit.setOpaque(false);

        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        chartsPanel.setOpaque(false);
        chartsPanel.add(new EvolutionChartPanel());
        chartsPanel.add(new PieChartPanel());
        contentSplit.add(chartsPanel);

        JPanel activityPanel = createActivityPanel();
        contentSplit.add(activityPanel);

        add(contentSplit, BorderLayout.CENTER);

        loadStatistics();
    }

    private void loadStatistics() {
        if (DatabaseConnection.OFFLINE_MODE) {
            AnimationUtils.animateCounter(membersValue, 0, 128, 1500, "");
            AnimationUtils.animateCounter(moneyValue, 0, 1575000, 1500, " GNF");
            AnimationUtils.animateCounter(cyclesValue, 0, 3, 1500, "");

            activityModel.setRowCount(0);
            activityModel.addRow(new Object[] { "16/02/2026", "Abdoulaye Camara", "COTISATION", "25,000 GNF" });
            activityModel.addRow(new Object[] { "15/02/2026", "Awa Touré", "DEPOT", "50,000 GNF" });
            activityModel.addRow(new Object[] { "14/02/2026", "Ibrahima Barry", "COTISATION", "25,000 GNF" });
            activityModel.addRow(new Object[] { "13/02/2026", "Saliou Keïta", "RETRAIT", "15,000 GNF" });
            activityModel.addRow(new Object[] { "12/02/2026", "Fatou Bangoura", "BONUS", "10,000 GNF" });
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            // 1. Members Count
            Statement st1 = conn.createStatement();
            ResultSet rs1 = st1.executeQuery("SELECT COUNT(*) FROM Membres");
            if (rs1.next()) {
                int count = rs1.getInt(1);
                AnimationUtils.animateCounter(membersValue, 0, count, 1500, "");
            }

            // 2. Total Money
            Statement st2 = conn.createStatement();
            ResultSet rs2 = st2.executeQuery("SELECT SUM(solde_compte) FROM Membres");
            if (rs2.next()) {
                double total = rs2.getDouble(1);
                AnimationUtils.animateCounter(moneyValue, 0, (int) total, 1500, " GNF");
            }

            // 3. Active Cycles
            Statement st3 = conn.createStatement();
            ResultSet rs3 = st3.executeQuery("SELECT COUNT(*) FROM Cycles WHERE statut = 'EN_COURS'");
            if (rs3.next()) {
                int cycles = rs3.getInt(1);
                AnimationUtils.animateCounter(cyclesValue, 0, cycles, 1500, "");
            }

            // 4. Recent Activity
            Statement st4 = conn.createStatement();
            ResultSet rs4 = st4.executeQuery(
                    "SELECT t.date_transaction, CONCAT(m.nom, ' ', m.prenoms) as membre, t.type_transaction, t.montant "
                            +
                            "FROM Transactions t JOIN Membres m ON t.id_membre = m.id_membre " +
                            "ORDER BY t.date_transaction DESC LIMIT 5");
            activityModel.setRowCount(0);
            while (rs4.next()) {
                activityModel.addRow(new Object[] {
                        new SimpleDateFormat("dd/MM/yyyy").format(rs4.getTimestamp(1)),
                        rs4.getString(2),
                        rs4.getString(3),
                        String.format("%,.0f GNF", rs4.getDouble(4))
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel createPremiumStatCard(String title, String value, String trend, boolean trendPositive,
            Color accentColor, Icon icon, String type) {
        JPanel card = new JPanel(new BorderLayout()) {
            private boolean hovered = false;
            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        MainDashboard dashboard = (MainDashboard) SwingUtilities.getAncestorOfClass(MainDashboard.class,
                                DashboardPanel.this);
                        if (dashboard != null) {
                            if (type.equals("members"))
                                dashboard.navigateTo("Membres");
                            else if (type.equals("money"))
                                dashboard.navigateTo("Collecte");
                            else if (type.equals("cycles"))
                                dashboard.navigateTo("Configuration");
                        }
                    }

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
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int shadowOffset = hovered ? 8 : 4;
                g2.setColor(new Color(0, 0, 0, hovered ? 50 : 25));
                g2.fillRoundRect(shadowOffset, shadowOffset, getWidth() - shadowOffset, getHeight() - shadowOffset, 20,
                        20);
                g2.setPaint(new GradientPaint(0, 0, Color.WHITE, 0, getHeight(), new Color(252, 252, 254)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setPaint(new GradientPaint(0, 0, accentColor, 0, getHeight(), accentColor.darker()));
                g2.fillRoundRect(0, 0, 10, getHeight(), 20, 20);
                g2.fillRect(5, 0, 5, getHeight());
                g2.setColor(new Color(229, 231, 235));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };

        card.setOpaque(false);
        card.setBorder(new EmptyBorder(15, 25, 15, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, accentColor, getWidth(), getHeight(), accentColor.darker()));
                g2.fillOval(0, 0, 48, 48);
                if (icon != null)
                    icon.paintIcon(this, g2, 8, 8);
            }
        };
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(48, 48));

        JPanel trendPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        trendPanel.setOpaque(false);
        JLabel trendLabel = new JLabel(
                trend);
        trendLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trendLabel.setForeground(trendPositive ? StyleUtils.SUCCESS_GREEN : StyleUtils.DANGER_RED);
        trendPanel.add(trendLabel);

        topPanel.add(iconPanel, BorderLayout.WEST);
        topPanel.add(trendPanel, BorderLayout.EAST);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        JLabel titleLabel = new JLabel(
                title);
        titleLabel.setForeground(StyleUtils.TEXT_GRAY);
        titleLabel.setFont(StyleUtils.FONT_BODY);

        JLabel valLabel = new JLabel(
                value);
        valLabel.setForeground(StyleUtils.TEXT_DARK);
        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));

        if (type.equals("members"))
            membersValue = valLabel;
        else if (type.equals("money"))
            moneyValue = valLabel;
        else if (type.equals("cycles"))
            cyclesValue = valLabel;

        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(valLabel);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createActivityPanel() {
        JPanel activityPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 15, 15);
                g2.setPaint(new GradientPaint(0, 0, Color.WHITE, 0, getHeight(), new Color(252, 252, 254)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(new Color(229, 231, 235));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        };
        activityPanel.setOpaque(false);

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

        String[] columnNames = { "Date", "Membre", "Type", "Montant" };
        activityModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(activityModel);
        StyleUtils.styleTable(table);

        table.getColumnModel().getColumn(2).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value != null) {
                    JLabel label = (JLabel) c;
                    label.setOpaque(true);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    String type = value.toString();
                    if (type.contains("COTISATION") || type.contains("DEPOT")) {
                        label.setBackground(new Color(16, 185, 129, 30));
                        label.setForeground(new Color(16, 185, 129));
                    } else {
                        label.setBackground(new Color(220, 38, 38, 30));
                        label.setForeground(new Color(220, 38, 38));
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
