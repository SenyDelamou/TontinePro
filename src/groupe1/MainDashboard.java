package groupe1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MainDashboard extends JFrame {

    // Redesign Colors based on screenshot
    private static final Color SIDEBAR_BG = new Color(28, 45, 90); // Deep Navy Blue
    private static final Color BUTTON_ORANGE = new Color(200, 80, 10); // Burnt Orange
    private static final Color HEADER_BG = new Color(220, 220, 220); // Light Grey
    private static final Color FOOTER_BG = new Color(220, 220, 220); // Light Grey

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private List<JButton> menuButtons = new ArrayList<>();
    private String currentActivePage = "Tableau de bord";
    
    // Chat Components
    private ChatPanel chatPanel;
    private JButton chatFab;
    private boolean isChatOpen = false;

    public MainDashboard() {
        setTitle("TontinePro - LimtaScore Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 850);
        setLocationRelativeTo(null);
        
        // Use JLayeredPane for Floating Elements
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null); // Absolute positioning for layers
        setContentPane(layeredPane);

        // --- Layer 0: Main Application ---
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBounds(0, 0, 1280, 850); // Initial bounds
        
        // Initialize Layouts
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        // Sidebar
        mainContainer.add(createSidebar(), BorderLayout.WEST);
        
        // Content Area (Header + Content + Footer)
        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.add(createHeader(), BorderLayout.NORTH);
        
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(Color.WHITE);
        centerWrapper.add(contentPanel, BorderLayout.CENTER);
        centerContainer.add(centerWrapper, BorderLayout.CENTER);
        
        centerContainer.add(createFooter(), BorderLayout.SOUTH);
        
        mainContainer.add(centerContainer, BorderLayout.CENTER);
        
        layeredPane.add(mainContainer, JLayeredPane.DEFAULT_LAYER);
        
        // --- Layer 100: Chat Floating Action Button (FAB) ---
        chatFab = new JButton("Chat"); // Icon would be better, using text for reliability
        chatFab.setFont(new Font("Segoe UI", Font.BOLD, 14));
        chatFab.setBackground(StyleUtils.PRIMARY_BLUE);
        chatFab.setForeground(Color.WHITE);
        chatFab.setFocusPainted(false);
        chatFab.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        chatFab.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Circular FAB simple simulation styling
        chatFab.addActionListener(e -> toggleChat());
        
        layeredPane.add(chatFab, JLayeredPane.PALETTE_LAYER);
        
        // --- Layer 101: Chat Window ---
        chatPanel = new ChatPanel();
        chatPanel.setVisible(false);
        layeredPane.add(chatPanel, JLayeredPane.MODAL_LAYER);

        // Resize Listener to keep everything in place
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = getWidth();
                int h = getHeight();
                
                // Main Container fills screen
                mainContainer.setBounds(0, 0, w, h);
                
                // FAB Position (Bottom Right)
                int fabSize = 60;
                int fabX = w - fabSize - 30;
                int fabY = h - fabSize - 50;
                chatFab.setBounds(fabX, fabY, fabSize, fabSize);
                
                // Chat Window Position (Above FAB)
                int chatW = 320;
                int chatH = 450;
                int chatX = fabX - chatW + fabSize;
                int chatY = fabY - chatH - 10;
                chatPanel.setBounds(chatX, chatY, chatW, chatH);
            }
        });
    }
    
    private void toggleChat() {
        isChatOpen = !isChatOpen;
        chatPanel.setVisible(isChatOpen);
        if (isChatOpen) {
            chatFab.setText("X");
            chatFab.setBackground(StyleUtils.DANGER_RED);
        } else {
            chatFab.setText("Chat");
            chatFab.setBackground(StyleUtils.PRIMARY_BLUE);
        }
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(SIDEBAR_BG);
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
                            int[] xPoints = {x - 15, x, x};
                            int[] yPoints = {y + h/2, y + h/2 - 10, y + h/2 + 10};
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

        // "LimtaScore" Text inside logo (simulated above) or below? Screenshot shows text below.
        JLabel appLabel = new JLabel("TontinePro", SwingConstants.CENTER);
        appLabel.setForeground(Color.WHITE);
        appLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        appLabel.setBounds(0, logoY + 90, 250, 30);
        sidebar.add(appLabel);

        // Menu Items & Panels
        contentPanel.add(new DashboardPanel(), "Tableau de bord");
        contentPanel.add(new MembersPanel(), "Membres");
        contentPanel.add(new CollectionPanel(), "Collecte");
        contentPanel.add(new ComptePanel(), "Compte");
        contentPanel.add(new UtilisateurPanel(), "Utilisateur");
        contentPanel.add(new ConfigurationPanel(), "Configuration");
        contentPanel.add(new ProfilePanel(), "Profil");

        String[] menuItems = {
            "Tableau de bord", "Membres", "Collecte", "Compte", "Utilisateur", "Configuration"
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
        JButton btn = StyleUtils.createModernButton(text, BUTTON_ORANGE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        
        // Custom Border (White rounded)
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            navigateTo(text);
        });

        return btn;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_BG);
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
                } catch(Exception e) {}
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
        footer.setBackground(FOOTER_BG);
        footer.setPreferredSize(new Dimension(0, 40));
        footer.setBorder(new EmptyBorder(0, 20, 0, 20));
        
        JLabel msg = new JLabel("Vos billets sont en sécurité avec TontinePro", SwingConstants.CENTER);
        msg.setForeground(Color.GRAY);
        
        JLabel ver = new JLabel("V 1.0.0.0");
        ver.setForeground(Color.LIGHT_GRAY);
        
        footer.add(msg, BorderLayout.CENTER);
        footer.add(ver, BorderLayout.EAST);
        
        return footer;
    }

    public void navigateTo(String pageName) {
        cardLayout.show(contentPanel, pageName);
        currentActivePage = pageName;
        repaint(); // Re-draw sidebar for arrow position
    }
}
