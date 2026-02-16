package groupe1;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.geom.RoundRectangle2D;

public class CardGenerator {

    public static void generateCard(Component parent, String name, String id, String phone, String address, String photoPath) {
        int width = 450;
        int height = 280;
        
        BufferedImage photo = null; // In real app, load from photoPath
        
        BufferedImage cardImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = cardImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // 1. Backgrounds
        // Left Stripe (Blue)
        g2.setColor(StyleUtils.PRIMARY_BLUE);
        g2.fillRect(0, 0, 120, height);
        
        // Main Body (White)
        g2.setColor(Color.WHITE);
        g2.fillRect(120, 0, width - 120, height);
        
        // Accent Lines
        g2.setColor(StyleUtils.ACCENT_GOLD);
        g2.fillRect(115, 0, 5, height);
        
        // 2. Content in Blue Stripe (Photo & Logo)
        // Logo Placeholder
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 40));
        g2.drawString("TP", 35, 60);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g2.drawString("TontinePro", 30, 80);
        
        // Photo Placeholder (Circle)
        int photoSize = 80;
        int photoX = 20;
        int photoY = 100;
        
        g2.setColor(Color.WHITE);
        g2.fillOval(photoX, photoY, photoSize, photoSize);
        g2.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(photoX, photoY, photoSize, photoSize);
        
        // Try to draw loaded photo if exists, else draw simple icon
        if (photoPath != null && !photoPath.isEmpty()) {
             try {
                BufferedImage img = ImageIO.read(new File(photoPath));
                // Circular clip
                g2.setClip(new java.awt.geom.Ellipse2D.Float(photoX, photoY, photoSize, photoSize));
                g2.drawImage(img, photoX, photoY, photoSize, photoSize, null);
                g2.setClip(null);
             } catch (Exception e) {
                 // Fallback
                 g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
                 g2.drawString("👤", photoX + 20, photoY + 55);
             }
        } else {
             g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
             g2.drawString("👤", photoX + 20, photoY + 55);
        }

        // 3. Content in White Area (Details)
        int textX = 150;
        int textY = 50;
        
        // Header
        g2.setColor(StyleUtils.PRIMARY_BLUE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
        g2.drawString("CARTE DE MEMBRE", textX, textY);
        
        g2.setColor(Color.GRAY);
        g2.drawLine(textX, textY + 10, width - 20, textY + 10);
        
        // Info
        textY += 50;
        g2.setColor(StyleUtils.TEXT_DARK);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        drawField(g2, "Nom & Prénom :", name, textX, textY);
        textY += 30;
        drawField(g2, "ID Membre :", id, textX, textY);
        textY += 30;
        drawField(g2, "Téléphone :", phone, textX, textY);
        textY += 30;
        drawField(g2, "Ville :", address, textX, textY);
        
        // Mock QR Code (Bottom Right)
        g2.setColor(Color.BLACK);
        g2.fillRect(width - 70, height - 70, 50, 50);
        g2.setColor(Color.WHITE);
        g2.fillRect(width - 65, height - 65, 40, 40);
        g2.setColor(Color.BLACK);
        g2.fillRect(width - 60, height - 60, 30, 30); // Simple box
        
        g2.dispose();
        
        // Show or Save
        JDialog preview = new JDialog();
        preview.setTitle("Aperçu de la Carte - " + name);
        preview.setModal(true);
        preview.setSize(width + 40, height + 80);
        preview.setLocationRelativeTo(parent);
        
        JPanel p = new JPanel() {
             @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(cardImage, 10, 10, null);
            }
        };
        p.add(new JLabel(new ImageIcon(cardImage)));
        
        JButton saveBtn = StyleUtils.createModernButton("Télécharger PNG", StyleUtils.SUCCESS_GREEN);
        saveBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File("Carte_" + id + ".png"));
            if (fc.showSaveDialog(preview) == JFileChooser.APPROVE_OPTION) {
                try {
                    ImageIO.write(cardImage, "png", fc.getSelectedFile());
                    Toast.show(parent, "Carte sauvegardée !", Toast.Type.SUCCESS);
                    preview.dispose();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        preview.add(p, BorderLayout.CENTER);
        preview.add(saveBtn, BorderLayout.SOUTH);
        preview.setVisible(true);
    }
    
    private static void drawField(Graphics2D g2, String label, String value, int x, int y) {
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g2.setColor(StyleUtils.PRIMARY_BLUE);
        g2.drawString(label, x, y);
        
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        g2.setColor(StyleUtils.TEXT_DARK);
        g2.drawString(value, x + 120, y);
    }
}
