package groupe1;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

public class EvolutionChartPanel extends JPanel {

    private List<Double> dataPoints;
    private List<String> labels;

    public EvolutionChartPanel() {
        this.dataPoints = new ArrayList<>();
        this.labels = new ArrayList<>();
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        
        // Demo Data
        addData(100000.0, "Jan");
        addData(150000.0, "Fév");
        addData(120000.0, "Mar");
        addData(200000.0, "Avr");
        addData(180000.0, "Mai");
        addData(250000.0, "Juin");
    }

    public void addData(Double value, String label) {
        dataPoints.add(value);
        labels.add(label);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int padding = 40;
        int graphW = w - 2 * padding;
        int graphH = h - 2 * padding;

        if (dataPoints.isEmpty()) return;

        // Draw Title
        g2.setColor(StyleUtils.TEXT_DARK);
        g2.setFont(StyleUtils.FONT_BOLD);
        g2.drawString("Évolution des Collectes (6 derniers mois)", padding, padding - 15);

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
            // Invert Y axis
            yPoints[i] = padding + graphH - (int) ((val / maxVal) * graphH);
        }

        // Draw Gradient Fill
        GeneralPath poly = new GeneralPath();
        poly.moveTo(padding, padding + graphH); // Bottom Left
        for (int i = 0; i < numPoints; i++) {
            poly.lineTo(xPoints[i], yPoints[i]);
        }
        poly.lineTo(padding + (numPoints - 1) * xStep, padding + graphH); // Bottom Right
        poly.closePath();

        GradientPaint gp = new GradientPaint(0, padding, new Color(26, 35, 126, 100), 0, h, new Color(26, 35, 126, 0));
        g2.setPaint(gp);
        g2.fill(poly);

        // Draw Line
        g2.setColor(StyleUtils.PRIMARY_BLUE);
        g2.setStroke(new BasicStroke(3f));
        g2.drawPolyline(xPoints, yPoints, numPoints);

        // Draw Points & Labels
        for (int i = 0; i < numPoints; i++) {
            // Point
            g2.setColor(StyleUtils.ACCENT_GOLD);
            g2.fillOval(xPoints[i] - 5, yPoints[i] - 5, 10, 10);
            g2.setColor(Color.WHITE);
            g2.fillOval(xPoints[i] - 3, yPoints[i] - 3, 6, 6);

            // Label
            g2.setColor(StyleUtils.TEXT_GRAY);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            String label = labels.get(i);
            FontMetrics fm = g2.getFontMetrics();
            int labelW = fm.stringWidth(label);
            g2.drawString(label, xPoints[i] - labelW / 2, padding + graphH + 20);
        }
    }
}
