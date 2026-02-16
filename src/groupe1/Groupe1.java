/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package groupe1;

import javax.swing.*;

/**
 *
 * @author DataVista
 */
public class Groupe1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Définir le Look and Feel système pour un aspect natif
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Afficher le splash screen
        // Launch LoginFrame directly
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
        // Assuming LoginFrame should be shown after splash screen is done,
        // but the provided snippet only includes the splash screen logic.
        // If LoginFrame should be shown, it would typically be called after the splash
        // screen closes.
        // For now, only the provided splash screen logic is included.
    }

}
