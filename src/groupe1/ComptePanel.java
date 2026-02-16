package groupe1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

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

        String[] columnNames = { "Numéro Compte", "Adhérent", "Solde", "Date Création", "Statut" };

        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        loadComptesFromDatabase();
        StyleUtils.styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);
    }

    private void loadComptesFromDatabase() {
        String query = "SELECT code_membre, CONCAT(nom, ' ', prenoms) as adherent, solde_compte, date_adhesion, statut FROM Membres";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getString("code_membre"),
                        rs.getString("adherent"),
                        String.format("%,.0f FCFA", rs.getDouble("solde_compte")),
                        new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("date_adhesion")),
                        rs.getString("statut")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.show(this, "Erreur de chargement des comptes", Toast.Type.ERROR);
        }
    }

    private void toggleStatus() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            Toast.show(this, "Sélectionnez un compte.", Toast.Type.INFO);
            return;
        }

        String codeMembre = (String) model.getValueAt(selectedRow, 0);
        String currentStatus = (String) model.getValueAt(selectedRow, 4);
        String newStatus = "ACTIF".equals(currentStatus) ? "SUSPENDU" : "ACTIF";

        String sql = "UPDATE Membres SET statut = ? WHERE code_membre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setString(2, codeMembre);
            int updated = pstmt.executeUpdate();
            if (updated > 0) {
                model.setValueAt(newStatus, selectedRow, 4);
                Toast.show(this, "Statut mis à jour : " + newStatus, Toast.Type.SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.show(this, "Erreur lors de la mise à jour", Toast.Type.ERROR);
        }
    }
}
