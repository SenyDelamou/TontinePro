/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package groupe1;

/**
 *
 * @author DataVista
 */
public class Groupe1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            StyleUtils.configureGlobalAppearance();
            new LoginFrame().setVisible(true);
        });
    }
    
}
