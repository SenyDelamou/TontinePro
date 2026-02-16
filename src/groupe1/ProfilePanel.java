package groupe1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProfilePanel extends JPanel {

    public ProfilePanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(StyleUtils.BG_LIGHT);
        setOpaque(false);

        // Header
        JLabel titleLabel = new JLabel("Mon Profil");
        titleLabel.setFont(StyleUtils.FONT_TITLE);
        titleLabel.setForeground(StyleUtils.TEXT_DARK);
        add(titleLabel, BorderLayout.NORTH);

        // Content
        JPanel contentInfo = new JPanel(new BorderLayout(20, 0));
        contentInfo.setOpaque(false);
        
        // Left: Avatar Card
        JPanel avatarCard = new JPanel();
        avatarCard.setLayout(new BoxLayout(avatarCard, BoxLayout.Y_AXIS));
        avatarCard.setBackground(Color.WHITE);
        avatarCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(30, 30, 30, 30)
        ));
        avatarCard.setPreferredSize(new Dimension(300, 400));
        
        // Large Avatar
        JPanel avatarCircle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(StyleUtils.PRIMARY_BLUE);
                g2.fillOval(0, 0, 100, 100);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 48));
                g2.drawString("A", 33, 68);
            }
        };
        avatarCircle.setPreferredSize(new Dimension(100, 100));
        avatarCircle.setMaximumSize(new Dimension(100, 100));
        avatarCircle.setOpaque(false);
        avatarCircle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel nameLabel = new JLabel("Admin User");
        nameLabel.setFont(StyleUtils.FONT_TITLE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel roleLabel = new JLabel("Administrateur");
        roleLabel.setForeground(StyleUtils.TEXT_GRAY);
        roleLabel.setFont(StyleUtils.FONT_SUBTITLE);
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        avatarCard.add(avatarCircle);
        avatarCard.add(Box.createRigidArea(new Dimension(0, 20)));
        avatarCard.add(nameLabel);
        avatarCard.add(Box.createRigidArea(new Dimension(0, 10)));
        avatarCard.add(roleLabel);
        
        contentInfo.add(avatarCard, BorderLayout.WEST);
        
        // Right: Edit Details Form
        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(30, 30, 30, 30)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        formCard.add(new JLabel("Nom Complet"), gbc);
        gbc.gridx = 1;
        JTextField nameField = StyleUtils.createModernTextField();
        nameField.setText("Admin User");
        formCard.add(nameField, gbc);
        
        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        formCard.add(new JLabel("Email"), gbc);
        gbc.gridx = 1;
        JTextField emailField = StyleUtils.createModernTextField();
        emailField.setText("admin@tontinepro.com");
        formCard.add(emailField, gbc);

        // Divider
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 20, 10);
        JSeparator sep = new JSeparator();
        sep.setForeground(Color.LIGHT_GRAY);
        formCard.add(sep, gbc);
        
        // Password Change
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formCard.add(new JLabel("Nouveau Mot de passe"), gbc);
        gbc.gridx = 1;
        JPasswordField passField = new JPasswordField();
        passField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        passField.setPreferredSize(new Dimension(0, 35));
        formCard.add(passField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formCard.add(new JLabel("Confirmer Mot de passe"), gbc);
        gbc.gridx = 1;
        JPasswordField confirmField = new JPasswordField();
        confirmField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        confirmField.setPreferredSize(new Dimension(0, 35));
        formCard.add(confirmField, gbc);
        
        // Save Button
        gbc.gridx = 1; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        JButton saveBtn = StyleUtils.createModernButton("Mettre à jour", StyleUtils.PRIMARY_BLUE);
        saveBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Profil mis à jour avec succès !"));
        formCard.add(saveBtn, gbc);
        
        // Make fields align top
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(formCard, BorderLayout.NORTH);
        
        contentInfo.add(wrapper, BorderLayout.CENTER);
        
        add(contentInfo, BorderLayout.CENTER);
    }
}
