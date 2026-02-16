package groupe1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DialogUtils {

    public static JDialog createDialog(Component parent, String title, int width, int height) {
        Frame owner = (Frame) SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(owner, title, true);
        dialog.setSize(width, height);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);
        
        // Custom Header for premium look (inside the window)
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(20, 20, 10, 20));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(StyleUtils.PRIMARY_BLUE);
        
        header.add(titleLabel, BorderLayout.WEST);
        
        // Separator
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(229, 231, 235));
        
        JPanel headerWrapper = new JPanel(new BorderLayout());
        headerWrapper.add(header, BorderLayout.CENTER);
        headerWrapper.add(sep, BorderLayout.SOUTH);
        
        dialog.add(headerWrapper, BorderLayout.NORTH);
        
        return dialog;
    }

    public static JPanel createFormPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(20, 20, 20, 20));
        return form;
    }

    public static void addFormField(JPanel panel, String labelText, JComponent field, int gridy) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = gridy * 2;
        
        JLabel label = new JLabel(labelText);
        label.setFont(StyleUtils.FONT_BOLD);
        label.setForeground(StyleUtils.TEXT_DARK);
        panel.add(label, gbc);
        
        gbc.insets = new Insets(0, 0, 15, 0);
        gbc.gridy = gridy * 2 + 1;
        
        // Ensure field height
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 40));
        panel.add(field, gbc);
    }
    
    public static JPanel createButtonPanel(JButton... buttons) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(0, 20, 10, 20)); // padding
        
        for (JButton btn : buttons) {
            panel.add(btn);
        }
        return panel;
    }
}
