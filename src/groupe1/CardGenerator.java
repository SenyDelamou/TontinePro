package groupe1;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.geom.Ellipse2D;

public class CardGenerator {

    public static void generateCard(Component parent, String name, String id, String phone, String address,
            String photoPath) {
        int width = 550;
        int height = 340;

        BufferedImage cardImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = cardImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // === BACKGROUND GRADIENT ===
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(28, 45, 90), // Deep Navy
                width, height, new Color(45, 70, 130) // Lighter Navy
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
        g2.setFont(new Font("Segoe UI", Font.BOLD, 36));
        g2.drawString("TP", 40, 60);

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        g2.setColor(new Color(255, 255, 255, 180));
        g2.drawString("TontinePro", 40, 85);

        // === CARD TITLE (Refined Badge style) ===
        int badgeW = 160;
        int badgeH = 32;
        int badgeX = 130; // Moved left to avoid photo overlap
        int badgeY = 40;

        g2.setColor(new Color(245, 158, 11, 30));
        g2.fillRoundRect(badgeX, badgeY, badgeW, badgeH, 8, 8);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g2.setColor(new Color(245, 158, 11)); // Gold
        g2.drawString("MEMBRE OFFICIEL", badgeX + 18, badgeY + 21);

        // === MEMBER PHOTO (Top Right) ===
        int photoSize = 120;
        int photoX = width - photoSize - 40;
        int photoY = 30;

        // Glossy photo background
        g2.setColor(new Color(255, 255, 255, 20));
        g2.fillOval(photoX - 5, photoY - 5, photoSize + 10, photoSize + 10);

        // Photo border
        g2.setColor(new Color(245, 158, 11));
        g2.setStroke(new BasicStroke(3));
        g2.drawOval(photoX, photoY, photoSize, photoSize);

        if (photoPath != null && !photoPath.isEmpty()) {
            try {
                BufferedImage img = ImageIO.read(new File(photoPath));
                g2.setClip(new Ellipse2D.Float(photoX, photoY, photoSize, photoSize));
                g2.drawImage(img, photoX, photoY, photoSize, photoSize, null);
                g2.setClip(null);
            } catch (Exception e) {
                drawDefaultAvatar(g2, photoX, photoY, photoSize);
            }
        } else {
            drawDefaultAvatar(g2, photoX, photoY, photoSize);
        }

        // === MEMBER INFO (Main Area) ===
        int detailsX = 40;
        int detailsY = 145; // Moved slightly up

        // Name Label & Value
        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        g2.setColor(new Color(255, 255, 255, 100));
        g2.drawString("NOM DU TITULAIRE", detailsX, detailsY);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 26)); // Slightly smaller font for better fit
        g2.setColor(Color.WHITE);
        g2.drawString(name.toUpperCase(), detailsX, detailsY + 30);

        // ID & Contact Side by Side
        detailsY += 75;

        // ID Block
        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        g2.setColor(new Color(255, 255, 255, 120));
        g2.drawString("ID MEMBRE", detailsX, detailsY);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g2.setColor(new Color(245, 158, 11)); // Gold
        g2.drawString(id, detailsX, detailsY + 22);

        // Phone Block
        int phoneX = detailsX + 160;
        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        g2.setColor(new Color(255, 255, 255, 120));
        g2.drawString("CONTACT", phoneX, detailsY);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g2.setColor(Color.WHITE);
        g2.drawString(phone, phoneX, detailsY + 22);

        // Address Block (Bottom Left)
        detailsY += 50;
        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        g2.setColor(new Color(255, 255, 255, 120));
        g2.drawString("LIEU DE RÉSIDENCE", detailsX, detailsY);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        g2.setColor(Color.WHITE);
        g2.drawString(address, detailsX, detailsY + 22);

        // === QR CODE (Black & Perfectly Positioned - Scannable) ===
        int qrSize = 85;
        int qrX = width - qrSize - 40;
        int qrY = height - qrSize - 40;

        String qrData = String.format("MEMBER:%s|NAME:%s|TEL:%s", id, name, phone);
        QRCodeUtils.drawQRCode(g2, qrData, qrX, qrY, qrSize);

        // Subtle caption below QR
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        g2.setColor(new Color(255, 255, 255, 80));
        g2.drawString("SCANEZ POUR VALIDATION", qrX - 5, qrY + qrSize + 15);

        // === FOOTER ===
        g2.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        g2.setColor(new Color(255, 255, 255, 100));
        g2.drawString("Carte Officielle TontinePro • Version 1.5.0", 40, height - 15);

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
                } catch (Exception ex) {
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

    private static void drawDefaultAvatar(Graphics2D g2, int x, int y, int size) {
        g2.setColor(new Color(255, 255, 255, 30));
        g2.fillOval(x, y, size, size);
        g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, (int) (size * 0.5)));
        g2.setColor(new Color(255, 255, 255, 100));
        g2.drawString("👤", x + (int) (size * 0.25), y + (int) (size * 0.7));
    }
}
