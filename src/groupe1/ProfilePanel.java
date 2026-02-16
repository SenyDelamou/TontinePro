package groupe1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProfilePanel extends JPanel {

    public ProfilePanel() {
        setLayout(new BorderLayout());
        setBackground(StyleUtils.BG_LIGHT);
        setOpaque(true);

        // Core Layout: Scrollable for small screens
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(StyleUtils.BG_LIGHT);

        // 1. HEADER SECTION (Banner)
        JPanel bannerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Rich Gradient Banner
                GradientPaint gradient = new GradientPaint(
                        0, 0, StyleUtils.PRIMARY_BLUE,
                        getWidth(), getHeight(), StyleUtils.PRIMARY_BLUE.brighter());
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Subtle decorative shapes
                g2.setColor(new Color(255, 255, 255, 20));
                g2.fillOval(getWidth() - 150, -50, 300, 300);
                g2.fillOval(-100, 100, 200, 200);
            }
        };
        bannerPanel.setPreferredSize(new Dimension(0, 180));
        bannerPanel.setMinimumSize(new Dimension(0, 180));
        bannerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        mainContent.add(bannerPanel);

        // 2. OVERLAPPING AVATAR & INFO SECTION
        JPanel profileHeader = new JPanel(null);
        profileHeader.setOpaque(false);
        profileHeader.setPreferredSize(new Dimension(0, 100));
        profileHeader.setMinimumSize(new Dimension(0, 100));
        profileHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // Overlapping Avatar
        JPanel avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Border/Glow
                g2.setColor(Color.WHITE);
                g2.fillOval(0, 0, 120, 120);

                // Avatar Circle
                g2.setColor(StyleUtils.ACCENT_ORANGE);
                g2.fillOval(5, 5, 110, 110);

                // Initial
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 52));
                FontMetrics fm = g2.getFontMetrics();
                String initial = "A";
                int x = (120 - fm.stringWidth(initial)) / 2;
                int y = (120 + fm.getAscent() - 10) / 2;
                g2.drawString(initial, x, y);
            }
        };
        avatarPanel.setOpaque(false);
        avatarPanel.setBounds(50, -60, 120, 120);
        profileHeader.add(avatarPanel);

        // Name and Role
        JLabel nameLabel = new JLabel("Admin User");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        nameLabel.setForeground(StyleUtils.TEXT_DARK);
        nameLabel.setBounds(190, 10, 400, 35);
        profileHeader.add(nameLabel);

        JLabel roleLabel = new JLabel("Administrateur • Session active depuis 2h");
        roleLabel.setFont(StyleUtils.FONT_SUBTITLE);
        roleLabel.setForeground(StyleUtils.TEXT_GRAY);
        roleLabel.setBounds(190, 45, 400, 25);
        profileHeader.add(roleLabel);

        mainContent.add(profileHeader);

        // 3. CONTENT CARDS
        JPanel cardsWrapper = new JPanel(new GridLayout(1, 2, 30, 0));
        cardsWrapper.setOpaque(false);
        cardsWrapper.setBorder(new EmptyBorder(20, 50, 50, 50));
        cardsWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));

        // Card 1: Personal Info
        cardsWrapper.add(createModernInfoCard("Informations Personnelles",
                IconUtils.createUserIcon(20, StyleUtils.PRIMARY_BLUE),
                new String[] { "Nom Complet", "Email", "Téléphone", "Ville" },
                new String[] { "Admin User", "admin@tontinepro.com", "+224 621 00 00 00", "Conakry" }));

        // Card 2: Security & Actions
        cardsWrapper.add(createModernSecurityCard());

        mainContent.add(cardsWrapper);

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUI(new StyleUtils.WebScrollBarUI());
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createModernInfoCard(String title, Icon icon, String[] labels, String[] values) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new ShadowBorder(),
                new EmptyBorder(25, 25, 25, 25)));

        // Header
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setIcon(icon);
        titleLabel.setIconTextGap(10);
        card.add(titleLabel, BorderLayout.NORTH);

        // Fields
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(15, 0, 5, 0);

        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = i * 2;
            JLabel l = new JLabel(labels[i]);
            l.setFont(new Font("Segoe UI", Font.BOLD, 12));
            l.setForeground(StyleUtils.TEXT_GRAY);
            fieldsPanel.add(l, gbc);

            gbc.gridy = i * 2 + 1;
            gbc.insets = new Insets(0, 0, 15, 0);
            JTextField field = StyleUtils.createModernTextField();
            field.setText(values[i]);
            field.setEditable(false);
            field.setBackground(new Color(248, 249, 250));
            fieldsPanel.add(field, gbc);
        }

        card.add(fieldsPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createModernSecurityCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new ShadowBorder(),
                new EmptyBorder(25, 25, 25, 25)));

        JLabel titleLabel = new JLabel("Sécurité & Accès");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setIcon(IconUtils.createSettingsIcon(20, StyleUtils.PRIMARY_BLUE));
        titleLabel.setIconTextGap(10);
        card.add(titleLabel, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(20, 0, 0, 0));

        // Password Change Section
        content.add(createFieldLabel("Nouveau mot de passe"));
        JPasswordField p1 = new JPasswordField();
        stylePasswordField(p1);
        content.add(p1);
        content.add(Box.createVerticalStrut(15));

        content.add(createFieldLabel("Confirmer le mot de passe"));
        JPasswordField p2 = new JPasswordField();
        stylePasswordField(p2);
        content.add(p2);
        content.add(Box.createVerticalStrut(30));

        JButton updateBtn = StyleUtils.createModernButton("Mettre à jour le compte", StyleUtils.PRIMARY_BLUE);
        updateBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        updateBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JButton logoutBtn = StyleUtils.createModernButton("Déconnexion sécurisée", StyleUtils.DANGER_RED);
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        content.add(updateBtn);
        content.add(Box.createVerticalStrut(10));
        content.add(logoutBtn);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private JLabel createFieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(StyleUtils.TEXT_GRAY);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setBorder(new EmptyBorder(0, 0, 5, 0));
        return l;
    }

    private void stylePasswordField(JPasswordField p) {
        p.setPreferredSize(new Dimension(0, 40));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                new EmptyBorder(0, 10, 0, 10)));
    }

    // Modern Shadow Border
    private static class ShadowBorder extends javax.swing.border.AbstractBorder {
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Subtle Shadow
            g2.setColor(new Color(0, 0, 0, 15));
            for (int i = 1; i <= 3; i++) {
                g2.drawRoundRect(x + i, y + i, width - 2 * i - 1, height - 2 * i - 1, 15, 15);
            }

            // Border
            g2.setColor(new Color(229, 231, 235));
            g2.drawRoundRect(x, y, width - 1, height - 1, 15, 15);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(5, 5, 10, 10);
        }
    }
}
