package groupe1;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class SplashScreen extends JWindow {

    public SplashScreen(int duration) {
        setSize(500, 300);
        setLocationRelativeTo(null);
        
        // Transparent background for rounded corners effect
        setBackground(new Color(0, 0, 0, 0));
        
        JPanel content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient Background (Deep Navy to slightly lighter)
                GradientPaint gp = new GradientPaint(0, 0, StyleUtils.PRIMARY_BLUE, 0, getHeight(), StyleUtils.PRIMARY_BLUE.darker());
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Watermark / Pattern effect (Optional circles)
                g2.setColor(new Color(255, 255, 255, 10));
                g2.fillOval(-50, -50, 200, 200);
                g2.fillOval(getWidth() - 150, getHeight() - 100, 300, 300);
            }
        };
        content.setLayout(null);
        setContentPane(content);
        
        // Logo / Title
        JLabel titleLabel = new JLabel("TontinePro", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 80, 500, 50);
        content.add(titleLabel);
        
        JLabel subtitleLabel = new JLabel("LimtaScore Edition", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(StyleUtils.ACCENT_GOLD);
        subtitleLabel.setBounds(0, 130, 500, 30);
        content.add(subtitleLabel);
        
        // Loading Bar
        JProgressBar progressBar = new JProgressBar();
        progressBar.setBounds(50, 230, 400, 6);
        progressBar.setBackground(new Color(255, 255, 255, 50));
        progressBar.setForeground(StyleUtils.ACCENT_ORANGE);
        progressBar.setBorderPainted(false);
        content.add(progressBar);
        
        JLabel statusLabel = new JLabel("Chargement...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(200, 200, 200));
        statusLabel.setBounds(0, 245, 500, 20);
        content.add(statusLabel);
        
        setVisible(true);
        
        // Simulate Loading with Animation
        new Thread(() -> {
            try {
                for (int i = 0; i <= 100; i++) {
                    progressBar.setValue(i);
                    if (i < 30) {
                        statusLabel.setText("Initialisation des modules...");
                        Thread.sleep(15);
                    } else if (i < 70) {
                        statusLabel.setText("Chargement de la base de données...");
                        Thread.sleep(20);
                    } else {
                        statusLabel.setText("Lancement de l'interface...");
                        Thread.sleep(10);
                    }
                }
                Thread.sleep(200);
                dispose();
                
                // Launch Login
                SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
