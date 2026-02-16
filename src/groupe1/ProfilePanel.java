package groupe1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.sql.*;

public class ProfilePanel extends JPanel {

    private Image profileImage = null;
    private Image bannerImage = null;

    private JTextField nameField, emailField, phoneField, locField;

    public ProfilePanel() {
        setLayout(new BorderLayout());
        setBackground(StyleUtils.BG_LIGHT);
        setOpaque(true);

        // Wrapper for the entire scrollable content
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(StyleUtils.BG_LIGHT);

        // --- 1. PREMIUM HEADER SECTION ---
        JPanel headerArea = createWebBannerArea();
        wrapper.add(headerArea);

        // --- 2. MAIN CONTENT AREA (2 Columns) ---
        JPanel mainColumns = new JPanel(new GridBagLayout());
        mainColumns.setOpaque(false);
        mainColumns.setBorder(new EmptyBorder(0, 50, 50, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1.0;

        // LEFT COLUMN (30%) - Summary Card
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        mainColumns.add(createLeftSidebar(), gbc);

        // RIGHT COLUMN (70%) - Detail Cards
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.insets = new Insets(0, 30, 0, 0);
        mainColumns.add(createRightContentArea(), gbc);

        wrapper.add(mainColumns);

        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new StyleUtils.WebScrollBarUI());
        add(scrollPane, BorderLayout.CENTER);

        loadUserData();
    }

    private void loadUserData() {
        String query = "SELECT nom_complet, email FROM Utilisateurs WHERE id_utilisateur = 1";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                nameField.setText(rs.getString("nom_complet"));
                emailField.setText(rs.getString("email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveUserData() {
        String sql = "UPDATE Utilisateurs SET nom_complet = ?, email = ? WHERE id_utilisateur = 1";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, emailField.getText());
            pstmt.executeUpdate();
            Toast.show(this, "Profil mis à jour !", Toast.Type.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.show(this, "Erreur de sauvegarde.", Toast.Type.ERROR);
        }
    }

    private JPanel createWebBannerArea() {
        JPanel area = new JPanel(null);
        area.setOpaque(false);
        area.setPreferredSize(new Dimension(0, 260));
        area.setMinimumSize(new Dimension(0, 260));
        area.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

        // Banner with deep gradient and patterns
        JPanel banner = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                if (bannerImage != null) {
                    // Draw user banner
                    double imgAspect = (double) bannerImage.getWidth(null) / bannerImage.getHeight(null);
                    double panelAspect = (double) getWidth() / getHeight();
                    int drawWidth, drawHeight, x = 0, y = 0;

                    if (imgAspect > panelAspect) {
                        drawHeight = getHeight();
                        drawWidth = (int) (drawHeight * imgAspect);
                        x = (getWidth() - drawWidth) / 2;
                    } else {
                        drawWidth = getWidth();
                        drawHeight = (int) (drawWidth / imgAspect);
                        y = (getHeight() - drawHeight) / 2;
                    }
                    g2.drawImage(bannerImage, x, y, drawWidth, drawHeight, null);

                    // Add subtle overlay to Ensure text readability if needed
                    g2.setColor(new Color(0, 0, 0, 30));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    // Default Premium Gradient
                    GradientPaint gp = new GradientPaint(0, 0, new Color(15, 23, 42),
                            getWidth(), getHeight(), new Color(30, 41, 59));
                    g2.setPaint(gp);
                    g2.fillRect(0, 0, getWidth(), getHeight());

                    // Modern geometric pattern
                    g2.setColor(new Color(255, 255, 255, 10));
                    int[] px = { 0, getWidth() / 2, 0 };
                    int[] py = { 0, 0, getHeight() };
                    g2.fillPolygon(px, py, 3);

                    g2.setColor(new Color(StyleUtils.PRIMARY_BLUE.getRed(), StyleUtils.PRIMARY_BLUE.getGreen(),
                            StyleUtils.PRIMARY_BLUE.getBlue(), 40));
                    g2.fillOval(getWidth() - 200, -100, 400, 400);
                }
            }
        };
        banner.setBounds(0, 0, 2500, 180);

        // Banner Edit Button
        JButton editBannerBtn = createModernIconButton(IconUtils.createSettingsIcon(16, Color.WHITE));
        editBannerBtn.setToolTipText("Changer la bannière");
        editBannerBtn.setBounds(10, 10, 40, 40);
        editBannerBtn.addActionListener(e -> chooseImage(false));
        banner.setLayout(null);
        banner.add(editBannerBtn);

        area.add(banner);

        // Centered Avatar
        JPanel avatarOuter = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Border/Outer Circle
                g2.setColor(Color.WHITE);
                g2.fillOval(0, 0, 140, 140);

                // Clipping area for image
                Shape clip = new java.awt.geom.Ellipse2D.Double(5, 5, 130, 130);
                g2.setClip(clip);

                if (profileImage != null) {
                    // Draw user image
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    int iw = profileImage.getWidth(null);
                    int ih = profileImage.getHeight(null);
                    int side = Math.min(iw, ih);
                    g2.drawImage(profileImage, 5, 5, 135, 135,
                            (iw - side) / 2, (ih - side) / 2, (iw + side) / 2, (ih + side) / 2, null);
                } else {
                    // Default Avatar
                    g2.setColor(StyleUtils.PRIMARY_BLUE);
                    g2.fillOval(5, 5, 130, 130);

                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 64));
                    String initial = "A";
                    FontMetrics fm = g2.getFontMetrics();
                    int x = (140 - fm.stringWidth(initial)) / 2;
                    int y = ((140 - fm.getHeight()) / 2) + fm.getAscent();
                    g2.drawString(initial, x, y);
                }
                g2.setClip(null);
            }
        };
        avatarOuter.setOpaque(false);
        avatarOuter.setLayout(null);
        avatarOuter.setBounds(80, 100, 140, 140);

        // Avatar Edit Button
        JButton editAvatarBtn = createModernIconButton(IconUtils.createSettingsIcon(14, StyleUtils.PRIMARY_BLUE));
        editAvatarBtn.setBackground(Color.WHITE);
        editAvatarBtn.setBounds(100, 100, 32, 32);
        editAvatarBtn.addActionListener(e -> chooseImage(true));
        avatarOuter.add(editAvatarBtn);

        area.add(avatarOuter);

        // Profile Info Header (Name and Role)
        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBounds(240, 185, 500, 70);

        JLabel nameLabel = new JLabel("Admin User");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        nameLabel.setForeground(StyleUtils.TEXT_DARK);

        JLabel roleLabel = new JLabel("Administrateur • TontinePro Premium");
        roleLabel.setFont(StyleUtils.FONT_SUBTITLE);
        roleLabel.setForeground(StyleUtils.TEXT_GRAY);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(2));
        infoPanel.add(roleLabel);

        area.add(infoPanel);

        return area;
    }

    private JButton createModernIconButton(Icon icon) {
        JButton btn = new JButton(icon);
        btn.setPreferredSize(new Dimension(32, 32));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(false);

        // Custom painting for circle background
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (btn.getModel().isPressed())
                    g2.setColor(new Color(255, 255, 255, 80));
                else if (btn.getModel().isRollover())
                    g2.setColor(new Color(255, 255, 255, 40));
                else
                    g2.setColor(new Color(255, 255, 255, 20));

                if (btn.getBackground() == Color.WHITE) {
                    g2.setColor(btn.getModel().isRollover() ? new Color(245, 245, 245) : Color.WHITE);
                }

                g2.fillOval(0, 0, btn.getWidth(), btn.getHeight());
                super.paint(g2, c);
                g2.dispose();
            }
        });
        return btn;
    }

    private void chooseImage(boolean forProfile) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "png", "jpeg"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                BufferedImage img = ImageIO.read(file);
                if (forProfile)
                    profileImage = img;
                else
                    bannerImage = img;
                repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors du chargement de l'image.");
            }
        }
    }

    private JPanel createLeftSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setOpaque(false);

        // 1. Completion Tracker Card
        JPanel completionCard = createGlassCard("Progression Profil", true);
        completionCard.setLayout(new BorderLayout(0, 10));

        JLabel progressLabel = new JLabel("Profil complété à 85%");
        progressLabel.setFont(StyleUtils.FONT_SUBTITLE);
        completionCard.add(progressLabel, BorderLayout.NORTH);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(85);
        bar.setForeground(StyleUtils.SUCCESS_GREEN);
        bar.setBackground(new Color(0, 0, 0, 10));
        bar.setBorderPainted(false);
        bar.setPreferredSize(new Dimension(0, 8));
        completionCard.add(bar, BorderLayout.CENTER);

        sidebar.add(completionCard);
        sidebar.add(Box.createVerticalStrut(20));

        // 2. Summary Info
        JPanel summaryCard = createGlassCard("Aperçu Rapide", false);
        summaryCard.setLayout(new GridBagLayout());
        GridBagConstraints sgbc = new GridBagConstraints();
        sgbc.fill = GridBagConstraints.HORIZONTAL;
        sgbc.weightx = 1.0;
        sgbc.insets = new Insets(5, 0, 5, 0);

        addSummaryRow(summaryCard, "Membre depuis", "Janvier 2026", 0, sgbc);
        addSummaryRow(summaryCard, "Rôle", "Administrateur", 1, sgbc);
        addSummaryRow(summaryCard, "Dernière connexion", "Il y a 2h", 2, sgbc);
        addSummaryRow(summaryCard, "Tontines actives", "5", 3, sgbc);

        sidebar.add(summaryCard);
        sidebar.add(Box.createVerticalStrut(20));

        // 3. Quick Actions
        JButton settingsBtn = StyleUtils.createModernButton("Paramètres Système", StyleUtils.TEXT_GRAY);
        settingsBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        settingsBtn.addActionListener(e -> {
            Window win = SwingUtilities.getWindowAncestor(this);
            if (win instanceof MainDashboard) {
                ((MainDashboard) win).navigateTo("Configuration");
            }
        });
        sidebar.add(settingsBtn);

        return sidebar;
    }

    private JPanel createRightContentArea() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        // Section 1: Personal Details
        content.add(
                createSectionHeader("Informations du Compte", IconUtils.createUserIcon(18, StyleUtils.PRIMARY_BLUE)));
        JPanel personalCard = createGlassCard(null, false);
        personalCard.setLayout(new GridLayout(2, 2, 20, 20));

        nameField = StyleUtils.createModernTextField();
        nameField.setText("Admin User");
        emailField = StyleUtils.createModernTextField();
        emailField.setText("admin@tontinepro.com");
        phoneField = StyleUtils.createModernTextField();
        phoneField.setText("+224 621 00 00 00");
        locField = StyleUtils.createModernTextField();
        locField.setText("Conakry, Guinée");

        personalCard.add(wrapField("Nom Complet", nameField));
        personalCard.add(wrapField("Adresse Email", emailField));
        personalCard.add(wrapField("Numéro de Téléphone", phoneField));
        personalCard.add(wrapField("Localisation", locField));

        content.add(personalCard);
        content.add(Box.createVerticalStrut(30));

        // Section 2: Security
        content.add(createSectionHeader("Sécurité & Authentification",
                IconUtils.createSettingsIcon(18, StyleUtils.PRIMARY_BLUE)));
        JPanel securityCard = createGlassCard(null, false);
        securityCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 0, 10, 0);

        gbc.gridy = 0;
        JPasswordField currentPasswordField = StyleUtils.createModernPasswordField();
        currentPasswordField.setText("********");
        currentPasswordField.setEditable(true);
        securityCard.add(wrapField("Mot de passe actuel", currentPasswordField), gbc);
        gbc.gridy = 1;
        JPasswordField newPasswordField = StyleUtils.createModernPasswordField();
        newPasswordField.setEditable(true);
        securityCard.add(wrapField("Nouveau mot de passe", newPasswordField), gbc);

        content.add(securityCard);
        content.add(Box.createVerticalStrut(30));

        // Bottom Actions
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setOpaque(false);
        JButton saveBtn = StyleUtils.createModernButton("Enregistrer les modifications", StyleUtils.PRIMARY_BLUE);
        saveBtn.addActionListener(e -> saveUserData());
        saveBtn.setPreferredSize(new Dimension(220, 45));
        actions.add(saveBtn);
        content.add(actions);

        return content;
    }

    private JPanel createGlassCard(String title, boolean withShadow) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (withShadow) {
                    g2.setColor(new Color(0, 0, 0, 10));
                    g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 16, 16);
                }

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - (withShadow ? 3 : 1), getHeight() - (withShadow ? 3 : 1), 16, 16);

                g2.setColor(new Color(229, 231, 235));
                g2.drawRoundRect(0, 0, getWidth() - (withShadow ? 4 : 2), getHeight() - (withShadow ? 4 : 2), 16, 16);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        if (title != null) {
            card.setLayout(new BorderLayout());
            JLabel l = new JLabel(title);
            l.setFont(new Font("Segoe UI", Font.BOLD, 14));
            l.setForeground(StyleUtils.TEXT_DARK);
            l.setBorder(new EmptyBorder(0, 0, 15, 0));
            card.add(l, BorderLayout.NORTH);
        }

        return card;
    }

    private JPanel createSectionHeader(String title, Icon icon) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(0, 5, 15, 0));

        JLabel l = new JLabel(title);
        l.setFont(new Font("Segoe UI", Font.BOLD, 18));
        l.setForeground(StyleUtils.TEXT_DARK);
        l.setIcon(icon);
        l.setIconTextGap(12);

        p.add(l);
        return p;
    }

    private JPanel wrapField(String label, JTextField field) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(StyleUtils.TEXT_GRAY);
        l.setBorder(new EmptyBorder(0, 0, 5, 0));
        p.add(l);
        p.add(field);

        return p;
    }

    private void addSummaryRow(JPanel p, String label, String value, int row, GridBagConstraints gbc) {
        gbc.gridy = row * 2;
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(StyleUtils.TEXT_GRAY);
        p.add(l, gbc);

        gbc.gridy = row * 2 + 1;
        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.BOLD, 13));
        v.setForeground(StyleUtils.TEXT_DARK);
        v.setBorder(new EmptyBorder(0, 0, 12, 0));
        p.add(v, gbc);
    }
}
