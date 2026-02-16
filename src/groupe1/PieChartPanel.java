package groupe1;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.List;

public class PieChartPanel extends JPanel {

    private List<Double> values;
    private List<Color> colors;
    private List<String> labels;

    public PieChartPanel() {
        this.values = new ArrayList<>();
        this.colors = new ArrayList<>();
        this.labels = new ArrayList<>();
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        
        // Demo Data
        addSlice(75.0, StyleUtils.SUCCESS_GREEN, "Dépôts (75%)");
        addSlice(25.0, StyleUtils.DANGER_RED, "Retraits (25%)");
    }

    public void addSlice(Double value, Color color, String label) {
        values.add(value);
        colors.add(color);
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
        
        // Title
        g2.setColor(StyleUtils.TEXT_DARK);
        g2.setFont(StyleUtils.FONT_BOLD);
        g2.drawString("Répartition des Transactions", padding, padding - 15);

        double total = values.stream().mapToDouble(Double::doubleValue).sum();
        
        int diameter = Math.min(w, h) - 2 * padding;
        int x = (w - diameter) / 2;
        int y = (h - diameter) / 2 + 10;

        double startAngle = 90;
        
        // Draw Pie
        for (int i = 0; i < values.size(); i++) {
            double val = values.get(i);
            double angle = (val / total) * 360;
            
            g2.setColor(colors.get(i));
            g2.fill(new Arc2D.Double(x, y, diameter, diameter, startAngle, -angle, Arc2D.PIE));
            
            startAngle -= angle;
        }
        
        // Draw Hole (Donut Chart)
        g2.setColor(Color.WHITE);
        int innerDiameter = diameter / 2;
        int innerX = x + (diameter - innerDiameter) / 2;
        int innerY = y + (diameter - innerDiameter) / 2;
        g2.fillOval(innerX, innerY, innerDiameter, innerDiameter);
        
        // Draw Legend
        int legendX = padding;
        int legendY = h - 30;
        
        for (int i = 0; i < labels.size(); i++) {
            g2.setColor(colors.get(i));
            g2.fillOval(legendX, legendY, 10, 10);
            
            g2.setColor(StyleUtils.TEXT_GRAY);
            g2.setFont(StyleUtils.FONT_BODY);
            g2.drawString(labels.get(i), legendX + 15, legendY + 10);
            
            legendX += 120; // Spacing
        }
    }
}
