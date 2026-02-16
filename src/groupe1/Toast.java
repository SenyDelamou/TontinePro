package groupe1;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Toast extends JWindow {

    private final int DURATION = 3000;
    
    public enum Type {
        SUCCESS, INFO, ERROR
    }

    public Toast(String message, Component locationComp, Type type) {
        setAlwaysOnTop(true);
        setBackground(new Color(0, 0, 0, 0)); // Transparent background for window
        
        JPanel content = new JPanel(new BorderLayout(15, 0));
        content.setBackground(getColorForType(type));
        content.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel label = new JLabel(message);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel icon = new JLabel(getIconChar(type));
        icon.setForeground(Color.WHITE);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        
        content.add(icon, BorderLayout.WEST);
        content.add(label, BorderLayout.CENTER);
        
        // Rounded Window
        setContentPane(content);
        pack();
        
        // Calculate Location (Bottom Right or Top Center)
        if (locationComp != null && locationComp.isShowing()) {
            Point loc = locationComp.getLocationOnScreen();
            
            // Bottom Right position inside the main frame
            int x = loc.x + locationComp.getWidth() - getWidth() - 30;
            int y = loc.y + locationComp.getHeight() - getHeight() - 50; // Above footer
            
            // Bounds check?
            setLocation(x, y);
        } else {
             setLocationRelativeTo(null);
        }

        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
    }
    
    private Color getColorForType(Type type) {
        switch (type) {
            case SUCCESS: return StyleUtils.SUCCESS_GREEN;
            case ERROR: return StyleUtils.DANGER_RED;
            case INFO: return StyleUtils.PRIMARY_BLUE;
            default: return Color.DARK_GRAY;
        }
    }

    private String getIconChar(Type type) {
         switch (type) {
            case SUCCESS: return "✓";
            case ERROR: return "✕"; // X mark
            case INFO: return "ℹ";
            default: return "";
        }
    }

    public void showToast() {
        setVisible(true);
        // Fade in? For now just timer
        new Thread(() -> {
            try {
                // simple animation could be added here (opacity)
                for (float i = 0f; i <= 0.9f; i += 0.05f) {
                    setOpacity(i);
                    Thread.sleep(20);
                }
                setOpacity(0.9f); // Slightly transparent
                
                Thread.sleep(DURATION);
                
                for (float i = 0.9f; i >= 0f; i -= 0.05f) {
                    setOpacity(i);
                    Thread.sleep(20);
                }
                dispose();
            } catch (Exception e) {
                dispose();
            }
        }).start();
    }
    
    public static void show(Component parent, String message, Type type) {
        new Toast(message, parent, type).showToast();
    }
}
