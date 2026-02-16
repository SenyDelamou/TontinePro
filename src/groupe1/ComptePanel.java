package groupe1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ComptePanel extends JPanel {

    private DefaultTableModel model;
    private JTable table;

    public ComptePanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(StyleUtils.BG_LIGHT);
        setOpaque(false);

        // Header
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Gestion des Comptes Adhérents");
        titleLabel.setFont(StyleUtils.FONT_TITLE);
        titleLabel.setForeground(StyleUtils.TEXT_DARK);
        topPanel.add(titleLabel, BorderLayout.WEST);

        // Actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        JButton blockButton = StyleUtils.createModernButton("Bloquer/Débloquer", StyleUtils.ACCENT_GOLD);
        blockButton.addActionListener(e -> toggleStatus());
        buttonPanel.add(blockButton);
        
        topPanel.add(buttonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));

        String[] columnNames = {"Numéro Compte", "Adhérent", "Type", "Solde", "Date Création", "Statut"};
        Object[][] data = {
            {"CPT-2026-001", "KONE Moussa", "Épargne", "150 000 FCFA", "01/01/2026", "ACTIF"},
            {"CPT-2026-002", "TOURE Aicha", "Tontine", "200 000 FCFA", "05/01/2026", "ACTIF"},
            {"CPT-2026-003", "KOUASSI Jean", "Épargne", "75 000 FCFA", "10/02/2026", "BLOQUE"}
        };
        
        model = new DefaultTableModel(data, columnNames);
        table = new JTable(model);
        StyleUtils.styleTable(table);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);
    }

    private void toggleStatus() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un compte.", "Info", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String currentStatus = (String) model.getValueAt(selectedRow, 5);
        String newStatus = "ACTIF".equals(currentStatus) ? "BLOQUE" : "ACTIF";
        model.setValueAt(newStatus, selectedRow, 5);
    }
}
