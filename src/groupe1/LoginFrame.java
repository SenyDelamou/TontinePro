package groupe1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Connexion - TontinePro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full Screen
        setLocationRelativeTo(null);

        // Main Container: Split Pane or Grid Layout
        // We use a JPanel with GridLayout (1 row, 2 cols)
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        // --- Left Panel (Art/Image) ---
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();

                // Linear Gradient: Dark Blue to lighter Blue
                GradientPaint gp = new GradientPaint(0, 0, new Color(10, 25, 60), w, h, new Color(30, 60, 140));
                g2.setPaint(gp);
                g2.fillRect(0, 0, w, h);

                // Draw decorative circles
                g2.setColor(new Color(255, 255, 255, 20)); // Semi-transparent white
                g2.fillOval(-100, -100, 400, 400);
                g2.fillOval(w - 200, h - 200, 500, 500);

                // Draw a pattern or text
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 48));
                FontMetrics fm = g2.getFontMetrics();
                String text1 = "Bienvenue sur";
                String text2 = "TontinePro";

                g2.drawString(text1, (w - fm.stringWidth(text1)) / 2, h / 2 - 20);
                g2.drawString(text2, (w - fm.stringWidth(text2)) / 2, h / 2 + 50);
            }
        };
        mainPanel.add(leftPanel);

        // --- Right Panel (Form) ---
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);

        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBackground(Color.WHITE);
        formContainer.setPreferredSize(new Dimension(400, 500));

        // Logo (Small)
        JLabel logoLabel = new JLabel();
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon originalIcon = new ImageIcon("logo/logo (2).png");
            Image scaledImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            logoLabel.setText("LS");
            logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
            logoLabel.setForeground(StyleUtils.PRIMARY_BLUE);
        }

        JLabel titleLabel = new JLabel("Connexion");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(StyleUtils.TEXT_DARK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Accédez à votre espace de gestion");
        subtitleLabel.setFont(StyleUtils.FONT_BODY);
        subtitleLabel.setForeground(StyleUtils.TEXT_GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        formContainer.add(logoLabel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        formContainer.add(titleLabel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 10)));
        formContainer.add(subtitleLabel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 40)));

        // Form Fields
        usernameField = StyleUtils.createModernTextField();
        usernameField.setPreferredSize(new Dimension(350, 45));
        usernameField.setMaximumSize(new Dimension(350, 45));

        passwordField = StyleUtils.createModernPasswordField();
        passwordField.setPreferredSize(new Dimension(350, 45));
        passwordField.setMaximumSize(new Dimension(350, 45));

        // Labels for fields
        JLabel userLabel = new JLabel("Nom d'utilisateur");
        userLabel.setFont(StyleUtils.FONT_BOLD);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Wrapper for left alignment of labels relative to fields? No, center is fine
        // for modern look usually, or let's use a wrapper.

        formContainer.add(createAlignedLabel("Nom d'utilisateur", 350));
        formContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        formContainer.add(usernameField);

        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        formContainer.add(createAlignedLabel("Mot de passe", 350));
        formContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        formContainer.add(passwordField);

        formContainer.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton loginButton = StyleUtils.createModernButton("Se connecter", StyleUtils.PRIMARY_BLUE);
        loginButton.setPreferredSize(new Dimension(350, 45));
        loginButton.setMaximumSize(new Dimension(350, 45));
        // Use a different color or sticking to Primary Blue? Primary Blue is fine.
        loginButton.setBackground(new Color(26, 35, 126));

        loginButton.addActionListener(e -> authenticate());

        formContainer.add(loginButton);

        rightPanel.add(formContainer);
        mainPanel.add(rightPanel);

        add(mainPanel);
    }

    // Helper for aligned labels
    private JPanel createAlignedLabel(String text, int width) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setMaximumSize(new Dimension(width, 20));
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setForeground(StyleUtils.TEXT_DARK);
        p.add(l, BorderLayout.WEST);
        return p;
    }

    private void authenticate() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            Toast.show(this, "Veuillez remplir tous les champs", Toast.Type.ERROR);
            return;
        }

        String query = "SELECT id_utilisateur, role FROM Utilisateurs WHERE nom_utilisateur = ? AND mot_de_passe = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, hashMD5(password));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Succès - Ouvrir le Dashboard
                    new MainDashboard().setVisible(true);
                    this.dispose();
                } else {
                    Toast.show(this, "Identifiants incorrects", Toast.Type.ERROR);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.show(this, "Erreur de connexion à la base de données", Toast.Type.ERROR);
        }
    }

    private String hashMD5(String input) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            java.math.BigInteger no = new java.math.BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
