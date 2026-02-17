package groupe1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainDashboard extends JFrame {

    // Redesign Colors based on screenshot
    // Colors are now handled by StyleUtils

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private List<JButton> menuButtons = new ArrayList<>();
    private Map<String, JPanel> initializedPanels = new HashMap<>();
    private String currentActivePage = "Tableau de bord";

    public MainDashboard() {
        setTitle(StyleUtils.APP_NAME + " - " + StyleUtils.APP_EDITION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 850);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize Layouts
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        // Sidebar
        add(createSidebar(), BorderLayout.WEST);

        // Main Content Area (Header + Content + Footer)
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.add(createHeader(), BorderLayout.NORTH);

        // Content wrapper
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(Color.WHITE);
        centerWrapper.add(contentPanel, BorderLayout.CENTER);
        mainContainer.add(centerWrapper, BorderLayout.CENTER);

        // Footer
        mainContainer.add(createFooter(), BorderLayout.SOUTH);

        add(mainContainer, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(StyleUtils.PRIMARY_BLUE);
                g.fillRect(0, 0, getWidth(), getHeight());

                // Draw white arrow for active item
                if (currentActivePage != null) {
                    for (JButton btn : menuButtons) {
                        if (btn.getText().equals(currentActivePage)) {
                            int y = btn.getY();
                            int h = btn.getHeight();
                            int x = getWidth();

                            Graphics2D g2 = (Graphics2D) g;
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2.setColor(Color.WHITE);
                            int[] xPoints = { x - 15, x, x };
                            int[] yPoints = { y + h / 2, y + h / 2 - 10, y + h / 2 + 10 };
                            g2.fillPolygon(xPoints, yPoints, 3);
                            break;
                        }
                    }
                }
            }
        };
        sidebar.setLayout(null); // Absolute positioning for precise layout
        sidebar.setPreferredSize(new Dimension(250, 800));
        sidebar.setBorder(null);

        // Logo Section
        int logoY = 30;

        // Circle Logo
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillOval(0, 0, 80, 80);
                // "LS" text simulation
                g2.setColor(Color.BLUE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
                g2.drawString("LS", 25, 50);
            }
        };
        logoPanel.setBounds(85, logoY, 80, 80);
        logoPanel.setOpaque(false);
        sidebar.add(logoPanel);

        // "LimtaScore" Text inside logo (simulated above) or below? Screenshot shows
        // text below.
        JLabel appLabel = new JLabel(StyleUtils.APP_NAME, SwingConstants.CENTER);
        appLabel.setForeground(Color.WHITE);
        appLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        appLabel.setBounds(0, logoY + 90, 250, 30);
        sidebar.add(appLabel);

        // Initial layout - empty contentPanel
        // Panels will be added lazily in navigateTo

        // Load initial page
        SwingUtilities.invokeLater(() -> navigateTo(currentActivePage));

        String[] menuItems = {
                "Tableau de bord", "Membres", "Collecte", "Prêts", "Compte", "Utilisateur", "Configuration", "Architecture"
        };

        int startY = 200;
        int gap = 15;
        int btnHeight = 45;

        for (String item : menuItems) {
            JButton btn = createOrangeButton(item);
            btn.setBounds(10, startY, 230, btnHeight);
            menuButtons.add(btn);
            sidebar.add(btn);
            startY += btnHeight + gap;
        }

        return sidebar;
    }

    private JButton createOrangeButton(String text) {
        JButton btn = StyleUtils.createModernButton(text, StyleUtils.ACCENT_ORANGE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);

        // Custom Border (White rounded)
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            navigateTo(text);
        });

        return btn;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(StyleUtils.BG_LIGHT);
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        // Center Search Bar
        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchContainer.setOpaque(false);

        JTextField searchField = new JTextField("Rechercher");
        searchField.setPreferredSize(new Dimension(400, 35));
        searchField.setBackground(new Color(230, 230, 230)); // Slightly darker grey for input
        searchField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        searchField.setHorizontalAlignment(JTextField.CENTER);
        searchField.setForeground(Color.GRAY);

        // Add vertical glue to center vertically
        JPanel vertWrapper = new JPanel(new GridBagLayout());
        vertWrapper.setOpaque(false);
        vertWrapper.add(searchField);

        header.add(vertWrapper, BorderLayout.CENTER);

        // Profile
        JPanel profileContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        profileContainer.setOpaque(false);
        profileContainer.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Circle Avatar
        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Image or Placeholder
                g2.setColor(Color.WHITE);
                g2.fillOval(0, 0, 40, 40);
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(1));
                g2.drawOval(0, 0, 40, 40);

                try {
                    ImageIcon originalIcon = new ImageIcon("logo/logo (2).png"); // Recycled logo for user
                    g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, 40, 40));
                    g2.drawImage(originalIcon.getImage(), 0, 0, 40, 40, null);
                } catch (Exception e) {
                }
            }
        };
        avatar.setPreferredSize(new Dimension(40, 40));
        avatar.setOpaque(false);

        profileContainer.add(avatar);

        // Popup logic same as before
        JPopupMenu profileMenu = new JPopupMenu();
        JMenuItem profileItem = new JMenuItem("Mon Profil");
        profileItem.addActionListener(e -> navigateTo("Profil"));
        JMenuItem logoutItem = new JMenuItem("Déconnexion");
        logoutItem.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            this.dispose();
        });
        profileMenu.add(profileItem);
        profileMenu.addSeparator();
        profileMenu.add(logoutItem);

        profileContainer.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                profileMenu.show(profileContainer, e.getX() - 80, e.getY() + 20);
            }
        });

        header.add(profileContainer, BorderLayout.EAST);

        return header;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(StyleUtils.BG_LIGHT);
        footer.setPreferredSize(new Dimension(0, 40));
        footer.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel msg = new JLabel("Vos billets sont en sécurité avec TontinePro", SwingConstants.CENTER);
        msg.setForeground(Color.GRAY);

        JLabel ver = new JLabel(StyleUtils.APP_VERSION);
        ver.setForeground(Color.LIGHT_GRAY);

        footer.add(msg, BorderLayout.CENTER);
        footer.add(ver, BorderLayout.EAST);

        return footer;
    }

    public void navigateTo(String pageName) {
        // Lazy loading of panels
        if (!initializedPanels.containsKey(pageName)) {
            JPanel panel = createPanelByName(pageName);
            if (panel != null) {
                initializedPanels.put(pageName, panel);
                contentPanel.add(panel, pageName);
            }
        }

        cardLayout.show(contentPanel, pageName);
        currentActivePage = pageName;
        repaint(); // Re-draw sidebar for arrow position
    }

    private JPanel createPanelByName(String pageName) {
        switch (pageName) {
            case "Tableau de bord":
                return new DashboardPanel();
            case "Membres":
                return new MembersPanel();
            case "Collecte":
                return new CollectionPanel();
            case "Prêts":
                return new PretPanel();
            case "Compte":
                return new ComptePanel();
            case "Utilisateur":
                return new UtilisateurPanel();
            case "Configuration":
                return new ConfigurationPanel();
            case "Profil":
                return new ProfilePanel();
            case "Architecture":
                return new DiagramsPanel();
            default:
                return null;
        }
    }
}
