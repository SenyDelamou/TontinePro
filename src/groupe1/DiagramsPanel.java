package groupe1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class DiagramsPanel extends JPanel {

    private JTabbedPane tabbedPane;

    public DiagramsPanel() {
        setLayout(new BorderLayout());
        setBackground(StyleUtils.BG_LIGHT);
        setOpaque(false);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = new JLabel("Architecture du Système");
        titleLabel.setFont(StyleUtils.FONT_TITLE);
        titleLabel.setForeground(StyleUtils.TEXT_DARK);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // Tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(StyleUtils.FONT_BODY);

        // Class Diagram
        ZoomablePanel classView = new ClassDiagramView();
        classView.setPreferredSize(new Dimension(1000, 1200));
        JScrollPane classScroll = createZoomableScrollPane(classView);

        // Use Case Diagram
        ZoomablePanel useCaseView = new UseCaseView();
        useCaseView.setPreferredSize(new Dimension(1000, 1000));
        JScrollPane useCaseScroll = createZoomableScrollPane(useCaseView);

        tabbedPane.addTab("Diagramme de Classes", classScroll);
        tabbedPane.addTab("Diagramme de Cas d'Utilisation", useCaseScroll);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        contentPanel.add(tabbedPane, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JScrollPane createZoomableScrollPane(ZoomablePanel panel) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(createZoomToolbar(panel), BorderLayout.NORTH);
        wrapper.add(panel, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getHorizontalScrollBar().setUnitIncrement(16);
        scroll.setBorder(null);
        return scroll;
    }

    private JToolBar createZoomToolbar(ZoomablePanel panel) {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setBackground(Color.WHITE);

        JButton zoomIn = new JButton("+");
        JButton zoomOut = new JButton("-");
        JButton reset = new JButton("100%");

        zoomIn.addActionListener(e -> panel.zoom(0.1));
        zoomOut.addActionListener(e -> panel.zoom(-0.1));
        reset.addActionListener(e -> panel.resetZoom());

        toolbar.add(new JLabel("Zoom : "));
        toolbar.add(zoomOut);
        toolbar.add(reset);
        toolbar.add(zoomIn);

        return toolbar;
    }

    // --- ZOOMABLE PANEL BASE ---
    private abstract class ZoomablePanel extends JPanel {
        protected double scale = 1.0;

        public ZoomablePanel() {
            setBackground(Color.WHITE);
            // Mouse Wheel Zoom
            addMouseWheelListener(new MouseAdapter() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    if (e.isControlDown()) {
                        if (e.getWheelRotation() < 0)
                            zoom(0.1);
                        else
                            zoom(-0.1);
                    } else {
                        getParent().dispatchEvent(e);
                    }
                }
            });
        }

        public void zoom(double factor) {
            scale += factor;
            if (scale < 0.2)
                scale = 0.2;
            if (scale > 3.0)
                scale = 3.0;
            revalidate();
            repaint();
        }

        public void resetZoom() {
            scale = 1.0;
            revalidate();
            repaint();
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            return new Dimension((int) (d.width * scale), (int) (d.height * scale));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // Applies scale
            Graphics2D g2 = (Graphics2D) g;
            g2.scale(scale, scale);
        }
    }

    // --- CLASS DIAGRAM VIEW ---
    private class ClassDiagramView extends ZoomablePanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // Applies scale
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int startX = 50;
            int startY = 50;
            int boxWidth = 200; // Wider for French names
            int boxHeight = 60;
            int gapX = 100;
            int gapY = 80;

            // Define Classes (TRANSLATED)
            Node groupe1 = new Node(startX, startY, "Application", "Point d'Entrée (Main)");
            Node login = new Node(startX, startY + gapY + boxHeight, "Connexion", "Authentification");
            Node dashboard = new Node(startX + boxWidth + gapX, startY + gapY + boxHeight, "Tableau de Bord",
                    "Conteneur Principal");

            Node dbConn = new Node(startX + (boxWidth + gapX) * 2, startY, "Connexion BDD", "Accès Base de Données");
            Node styleUtils = new Node(startX + (boxWidth + gapX) * 2, startY + gapY + boxHeight, "Styles & Thèmes",
                    "Utilitaires UI");

            // Panels
            int panelX = startX + boxWidth + gapX;
            int panelY = startY + (gapY + boxHeight) * 2;
            List<Node> panels = new ArrayList<>();
            String[] panelNames = { "Vue Accueil", "Vue Membres", "Vue Cotisations", "Vue Prêts", "Vue Historique",
                    "Vue Utilisateurs" };
            String[] panelDescs = { "Tableau de bord", "Gestion des membres", "Gestion des collectes",
                    "Gestion des crédits",
                    "Historique du compte", "Gestion des accès" };

            for (int i = 0; i < panelNames.length; i++) {
                panels.add(new Node(panelX + (i % 2 == 0 ? -40 : 40), panelY + i * 50, panelNames[i], panelDescs[i]));
            }

            // Draw Relationships
            g2.setColor(Color.GRAY);
            drawArrow(g2, groupe1, login);
            drawArrow(g2, login, dashboard);

            for (Node p : panels) {
                drawArrow(g2, dashboard, p);
            }

            drawDashedArrow(g2, login, dbConn);
            for (Node p : panels) {
                drawDashedArrow(g2, p, dbConn);
            }

            // Draw Nodes
            drawNode(g2, groupe1, new Color(255, 200, 100)); // Orange
            drawNode(g2, login, new Color(100, 200, 255)); // Blue
            drawNode(g2, dashboard, new Color(100, 255, 100)); // Green
            drawNode(g2, dbConn, Color.LIGHT_GRAY);
            drawNode(g2, styleUtils, Color.LIGHT_GRAY);

            for (Node p : panels) {
                drawNode(g2, p, new Color(220, 220, 255));
            }
        }
    }

    // --- USE CASE DIAGRAM VIEW ---
    private class UseCaseView extends ZoomablePanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // Applies scale
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int centerX = 500; // Fixed center for layout
            int centerY = 400;

            // Actors
            int actorX = 100;
            drawActor(g2, actorX, centerY - 150, "Admin");
            drawActor(g2, actorX, centerY + 150, "Membre");

            // System Boundary
            int sysX = 300;
            int sysY = 50;
            int sysW = 500;
            int sysH = 700;
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawRect(sysX, sysY, sysW, sysH);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            g2.drawString("Système TontinePro", sysX + 10, sysY + 20);

            // Use Cases (TRANSLATED)
            List<Node> useCases = new ArrayList<>();
            String[] ucNames = { "Authentification", "Gestion Membres", "Gestion Tontines", "Opérations Financières",
                    "Rapports & Stats", "Configuration" };

            int ucY = sysY + 60;
            for (String name : ucNames) {
                useCases.add(new Node(sysX + 150, ucY, name, ""));
                ucY += 90;
            }

            // Draw Use Cases
            for (Node uc : useCases) {
                drawOvalNode(g2, uc);
            }

            // Relations
            Point adminPos = new Point(actorX + 30, centerY - 150 + 40);
            Point memberPos = new Point(actorX + 30, centerY + 150 + 40);

            // Admin links
            g2.setColor(Color.BLACK);
            for (Node uc : useCases) {
                g2.drawLine(adminPos.x, adminPos.y, uc.x, uc.y + 20);
            }

            // Member links
            g2.drawLine(memberPos.x, memberPos.y, useCases.get(0).x, useCases.get(0).y + 20); // Auth
            g2.drawLine(memberPos.x, memberPos.y, useCases.get(3).x, useCases.get(3).y + 20); // Ops
        }
    }

    // --- UTILS ---
    private class Node {
        int x, y, w = 200, h = 45; // Sized for content
        String title, subtitle;

        public Node(int x, int y, String title, String subtitle) {
            this.x = x;
            this.y = y;
            this.title = title;
            this.subtitle = subtitle;
        }
    }

    private void drawNode(Graphics2D g2, Node n, Color color) {
        g2.setColor(color);
        g2.fillRoundRect(n.x, n.y, n.w, n.h, 10, 10);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(n.x, n.y, n.w, n.h, 10, 10);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        FontMetrics fm = g2.getFontMetrics();
        int titleWidth = fm.stringWidth(n.title);
        g2.drawString(n.title, n.x + (n.w - titleWidth) / 2, n.y + 18);

        g2.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        fm = g2.getFontMetrics();
        int subWidth = fm.stringWidth(n.subtitle);
        g2.drawString(n.subtitle, n.x + (n.w - subWidth) / 2, n.y + 32);
    }

    private void drawOvalNode(Graphics2D g2, Node n) {
        g2.setColor(new Color(240, 248, 255));
        g2.fillOval(n.x, n.y, n.w, n.h);
        g2.setColor(Color.BLUE);
        g2.drawOval(n.x, n.y, n.w, n.h);
        g2.setColor(Color.BLACK);

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(n.title);
        g2.drawString(n.title, n.x + (n.w - textWidth) / 2, n.y + 28);
    }

    private void drawActor(Graphics2D g2, int x, int y, String name) {
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(x, y, 30, 30);
        g2.drawLine(x + 15, y + 30, x + 15, y + 80);
        g2.drawLine(x, y + 50, x + 30, y + 50);
        g2.drawLine(x + 15, y + 80, x, y + 110);
        g2.drawLine(x + 15, y + 80, x + 30, y + 110);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        g2.drawString(name, x - 10, y + 130);
        g2.setStroke(new BasicStroke(1));
    }

    private void drawArrow(Graphics2D g2, Node from, Node to) {
        int x1 = from.x + from.w / 2;
        int y1 = from.y + from.h;
        int x2 = to.x + to.w / 2;
        int y2 = to.y;
        g2.drawLine(x1, y1, x2, y2);
    }

    private void drawDashedArrow(Graphics2D g2, Node from, Node to) {
        Stroke old = g2.getStroke();
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0));
        g2.drawLine(from.x + from.w, from.y + from.h / 2, to.x, to.y + to.h / 2);
        g2.setStroke(old);
    }
}
