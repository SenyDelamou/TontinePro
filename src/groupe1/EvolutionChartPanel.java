package groupe1;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

public class EvolutionChartPanel extends JPanel {

    private List<Double> dataPoints;
    private List<String> labels;
    private float animationProgress = 0f;
    private Timer animationTimer;

    public EvolutionChartPanel() {
        this.dataPoints = new ArrayList<>();
        this.labels = new ArrayList<>();
        setBackground(Color.WHITE);
        setOpaque(false);

        // Demo Data
        addData(100000.0, "Jan");
        addData(150000.0, "Fév");
        addData(120000.0, "Mar");
        addData(200000.0, "Avr");
        addData(180000.0, "Mai");
        addData(250000.0, "Juin");

        // Animation de dessin au chargement
        startDrawAnimation();
    }

    public void addData(Double value, String label) {
        dataPoints.add(value);
        labels.add(label);
        repaint();
    }

    private void startDrawAnimation() {
        animationTimer = new Timer(20, e -> {
            animationProgress += 0.02f;
            if (animationProgress >= 1.0f) {
                animationProgress = 1.0f;
                animationTimer.stop();
            }
            repaint();
        });
        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int padding = 50;
        int graphW = w - 2 * padding;
        int graphH = h - 2 * padding - 30;

        if (dataPoints.isEmpty())
            return;

        // Fond avec gradient et ombre
        paintBackground(g2, w, h);

        // Draw Title avec icône
        g2.setColor(StyleUtils.TEXT_DARK);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        Icon chartIcon = IconUtils.createDashboardIcon(18, StyleUtils.ACCENT_ORANGE);
        chartIcon.paintIcon(this, g2, padding, 15);
        g2.drawString("Évolution des Collectes (6 derniers mois)", padding + 25, 28);

        // Grille d'arrière-plan élégante
        paintGrid(g2, padding, padding + 40, graphW, graphH);

        // Calculate scales
        double maxVal = dataPoints.stream().max(Double::compareTo).orElse(1.0);
        int numPoints = dataPoints.size();
        int xStep = graphW / (numPoints - 1);

        // Points coordinates
        int[] xPoints = new int[numPoints];
        int[] yPoints = new int[numPoints];

        for (int i = 0; i < numPoints; i++) {
            xPoints[i] = padding + i * xStep;
            double val = dataPoints.get(i);
            yPoints[i] = padding + 40 + graphH - (int) ((val / maxVal) * graphH);
        }

        // Animation: limiter le nombre de points affichés
        int visiblePoints = (int) (numPoints * animationProgress);
        if (visiblePoints < 2)
            visiblePoints = 2;

        // Draw Gradient Fill avec animation
        paintGradientFill(g2, xPoints, yPoints, visiblePoints, padding, graphH);

        // Draw Line avec effet de lueur
        paintGlowLine(g2, xPoints, yPoints, visiblePoints);

        // Draw Points & Labels
        paintPointsAndLabels(g2, xPoints, yPoints, visiblePoints, padding, graphH);
    }

    private void paintBackground(Graphics2D g2, int w, int h) {
        // Ombre portée
        g2.setColor(new Color(0, 0, 0, 15));
        g2.fillRoundRect(4, 4, w - 4, h - 4, 15, 15);

        // Fond avec gradient
        GradientPaint gradient = new GradientPaint(
                0, 0, Color.WHITE,
                0, h, new Color(252, 252, 254));
        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, w, h, 15, 15);

        // Bordure
        g2.setColor(new Color(229, 231, 235));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(0, 0, w - 1, h - 1, 15, 15);
    }

    private void paintGrid(Graphics2D g2, int x, int y, int w, int h) {
        g2.setColor(new Color(229, 231, 235, 100));
        g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[] { 5, 5 }, 0));

        // Lignes horizontales
        int numHLines = 5;
        for (int i = 0; i <= numHLines; i++) {
            int yPos = y + (h * i / numHLines);
            g2.drawLine(x, yPos, x + w, yPos);
        }

        // Lignes verticales
        int numVLines = 6;
        for (int i = 0; i <= numVLines; i++) {
            int xPos = x + (w * i / numVLines);
            g2.drawLine(xPos, y, xPos, y + h);
        }
    }

    private void paintGradientFill(Graphics2D g2, int[] xPoints, int[] yPoints,
            int visiblePoints, int padding, int graphH) {
        GeneralPath poly = new GeneralPath();
        poly.moveTo(padding, padding + 40 + graphH);

        for (int i = 0; i < visiblePoints; i++) {
            poly.lineTo(xPoints[i], yPoints[i]);
        }

        poly.lineTo(xPoints[visiblePoints - 1], padding + 40 + graphH);
        poly.closePath();

        // Gradient multi-couleurs plus riche
        GradientPaint gp = new GradientPaint(
                0, padding + 40, new Color(28, 45, 90, 120),
                0, padding + 40 + graphH, new Color(28, 45, 90, 10));
        g2.setPaint(gp);
        g2.fill(poly);
    }

    private void paintGlowLine(Graphics2D g2, int[] xPoints, int[] yPoints, int visiblePoints) {
        // Effet de lueur (glow) autour de la ligne
        g2.setStroke(new BasicStroke(8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(28, 45, 90, 30));

        for (int i = 0; i < visiblePoints - 1; i++) {
            g2.drawLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
        }

        // Ligne principale
        g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // Gradient sur la ligne
        for (int i = 0; i < visiblePoints - 1; i++) {
            GradientPaint lineGradient = new GradientPaint(
                    xPoints[i], yPoints[i], StyleUtils.PRIMARY_BLUE,
                    xPoints[i + 1], yPoints[i + 1], StyleUtils.ACCENT_ORANGE);
            g2.setPaint(lineGradient);
            g2.drawLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
        }
    }

    private void paintPointsAndLabels(Graphics2D g2, int[] xPoints, int[] yPoints,
            int visiblePoints, int padding, int graphH) {
        for (int i = 0; i < visiblePoints; i++) {
            // Point avec effet de profondeur
            // Ombre du point
            g2.setColor(new Color(0, 0, 0, 50));
            g2.fillOval(xPoints[i] - 6, yPoints[i] - 5, 12, 12);

            // Cercle extérieur doré
            GradientPaint pointGradient = new GradientPaint(
                    xPoints[i] - 7, yPoints[i] - 7, StyleUtils.ACCENT_GOLD.brighter(),
                    xPoints[i] + 7, yPoints[i] + 7, StyleUtils.ACCENT_GOLD.darker());
            g2.setPaint(pointGradient);
            g2.fillOval(xPoints[i] - 7, yPoints[i] - 7, 14, 14);

            // Cercle intérieur blanc
            g2.setColor(Color.WHITE);
            g2.fillOval(xPoints[i] - 4, yPoints[i] - 4, 8, 8);

            // Petit point central
            g2.setColor(StyleUtils.ACCENT_GOLD);
            g2.fillOval(xPoints[i] - 2, yPoints[i] - 2, 4, 4);

            // Label du mois
            g2.setColor(StyleUtils.TEXT_GRAY);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            String label = labels.get(i);
            FontMetrics fm = g2.getFontMetrics();
            int labelW = fm.stringWidth(label);
            g2.drawString(label, xPoints[i] - labelW / 2, padding + 40 + graphH + 25);

            // Valeur au-dessus du point (au survol - simulé pour le dernier point)
            if (i == visiblePoints - 1) {
                String valueStr = String.format("%.0f K", dataPoints.get(i) / 1000);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                g2.setColor(Color.WHITE);

                int valueW = fm.stringWidth(valueStr);
                int tooltipX = xPoints[i] - valueW / 2 - 8;
                int tooltipY = yPoints[i] - 30;

                // Bulle de tooltip
                g2.setColor(StyleUtils.PRIMARY_BLUE);
                g2.fillRoundRect(tooltipX, tooltipY, valueW + 16, 20, 10, 10);

                // Petit triangle pointant vers le point
                int[] triX = { xPoints[i] - 4, xPoints[i] + 4, xPoints[i] };
                int[] triY = { tooltipY + 20, tooltipY + 20, tooltipY + 26 };
                g2.fillPolygon(triX, triY, 3);

                g2.setColor(Color.WHITE);
                g2.drawString(valueStr, tooltipX + 8, tooltipY + 14);
            }
        }
    }
}
