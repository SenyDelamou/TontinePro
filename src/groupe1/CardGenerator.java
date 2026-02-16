package groupe1;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.geom.RoundRectangle2D;

public class CardGenerator {

    public static void generateCard(Component parent, String name, String id, String phone, String address, String photoPath) {
        int width = 550;
        int height = 340;
        
        BufferedImage cardImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = cardImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // === BACKGROUND GRADIENT ===
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(28, 45, 90),           // Deep Navy
            width, height, new Color(45, 70, 130)  // Lighter Navy
        );
        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, width, height, 30, 30);
        
        // === DECORATIVE PATTERN (Circles) ===
        g2.setColor(new Color(255, 255, 255, 15));
        g2.fillOval(width - 150, -50, 200, 200);
        g2.fillOval(width - 80, height - 100, 150, 150);
        g2.fillOval(-50, height - 80, 120, 120);
        
        // === ACCENT STRIPE (Orange) ===
        g2.setColor(new Color(220, 80, 10));
        g2.fillRoundRect(0, 0, 8, height, 30, 30);
        
        // === GOLD ACCENT LINE ===
        g2.setColor(new Color(245, 158, 11));
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(30, 90, width - 30, 90);
        
        // === LOGO & BRANDING ===
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 32));
        g2.drawString("TP", 30, 50);
        
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        g2.setColor(new Color(255, 255, 255, 180));
        g2.drawString("TontinePro", 30, 70);
        
        // === CARD TITLE ===
        g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g2.setColor(new Color(245, 158, 11)); // Gold
        g2.drawString("CARTE DE MEMBRE", 30, 115);
        
        // === MEMBER PHOTO (Circular with border) ===
        int photoSize = 100;
        int photoX = width - photoSize - 40;
        int photoY = 30;
        
        // Photo border (gold)
        g2.setColor(new Color(245, 158, 11));
        g2.setStroke(new BasicStroke(4));
        g2.drawOval(photoX - 2, photoY - 2, photoSize + 4, photoSize + 4);
        
        // Photo background
        g2.setColor(new Color(255, 255, 255, 30));
        g2.fillOval(photoX, photoY, photoSize, photoSize);
        
        // Load and draw photo
        if (photoPath != null && !photoPath.isEmpty()) {
            try {
                BufferedImage img = ImageIO.read(new File(photoPath));
                g2.setClip(new java.awt.geom.Ellipse2D.Float(photoX, photoY, photoSize, photoSize));
                g2.drawImage(img, photoX, photoY, photoSize, photoSize, null);
                g2.setClip(null);
            } catch (Exception e) {
                // Fallback icon
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
                g2.setColor(new Color(255, 255, 255, 100));
                g2.drawString("👤", photoX + 25, photoY + 70);
            }
        } else {
            g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
            g2.setColor(new Color(255, 255, 255, 100));
            g2.drawString("👤", photoX + 25, photoY + 70);
        }
        
        // === MEMBER DETAILS ===
        int detailsX = 30;
        int detailsY = 155;
        int lineHeight = 35;
        
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g2.setColor(new Color(255, 255, 255, 150));
        
        // Name
        g2.drawString("NOM COMPLET", detailsX, detailsY);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g2.setColor(Color.WHITE);
        g2.drawString(name.toUpperCase(), detailsX, detailsY + 20);
        
        detailsY += lineHeight + 20;
        
        // ID
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        g2.setColor(new Color(255, 255, 255, 150));
        g2.drawString("ID MEMBRE", detailsX, detailsY);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2.setColor(new Color(245, 158, 11)); // Gold
        g2.drawString(id, detailsX, detailsY + 18);
        
        // Phone & Address (side by side)
        int col2X = detailsX + 180;
        
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        g2.setColor(new Color(255, 255, 255, 150));
        g2.drawString("TÉLÉPHONE", col2X, detailsY);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g2.setColor(Color.WHITE);
        g2.drawString(phone, col2X, detailsY + 18);
        
        detailsY += lineHeight;
        
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        g2.setColor(new Color(255, 255, 255, 150));
        g2.drawString("VILLE", detailsX, detailsY);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g2.setColor(Color.WHITE);
        g2.drawString(address, detailsX, detailsY + 18);
        
        // === QR CODE (Modern style) ===
        int qrSize = 60;
        int qrX = width - qrSize - 30;
        int qrY = height - qrSize - 30;
        
        // QR background
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(qrX - 5, qrY - 5, qrSize + 10, qrSize + 10, 10, 10);
        
        // QR pattern (simplified)
        g2.setColor(new Color(28, 45, 90));
        g2.fillRect(qrX, qrY, qrSize, qrSize);
        g2.setColor(Color.WHITE);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if ((i + j) % 2 == 0) {
                    g2.fillRect(qrX + i * 10, qrY + j * 10, 9, 9);
                }
            }
        }
        
        // === FOOTER TEXT ===
        g2.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        g2.setColor(new Color(255, 255, 255, 120));
        g2.drawString("Membre depuis 2026 • TontinePro Premium", 30, height - 15);
        
        g2.dispose();
        
        // === PREVIEW DIALOG ===
        JDialog preview = new JDialog();
        preview.setTitle("Carte de Membre - " + name);
        preview.setModal(true);
        preview.setSize(width + 60, height + 120);
        preview.setLocationRelativeTo(parent);
        preview.setLayout(new BorderLayout(10, 10));
        
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(new Color(240, 240, 240));
                int x = (getWidth() - width) / 2;
                int y = (getHeight() - height) / 2;
                g.drawImage(cardImage, x, y, null);
            }
        };
        imagePanel.setPreferredSize(new Dimension(width + 40, height + 40));
        
        JButton saveBtn = StyleUtils.createModernButton("💾 Télécharger PNG", StyleUtils.SUCCESS_GREEN);
        saveBtn.setPreferredSize(new Dimension(200, 45));
        saveBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File("Carte_" + id + ".png"));
            if (fc.showSaveDialog(preview) == JFileChooser.APPROVE_OPTION) {
                try {
                    ImageIO.write(cardImage, "png", fc.getSelectedFile());
                    Toast.show(parent, "Carte sauvegardée avec succès !", Toast.Type.SUCCESS);
                    preview.dispose();
                } catch(Exception ex) {
                    Toast.show(parent, "Erreur lors de la sauvegarde.", Toast.Type.ERROR);
                }
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(saveBtn);
        
        preview.add(imagePanel, BorderLayout.CENTER);
        preview.add(buttonPanel, BorderLayout.SOUTH);
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
