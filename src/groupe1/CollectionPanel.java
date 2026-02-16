package groupe1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class CollectionPanel extends JPanel {

    private DefaultTableModel model;
    private JTable table;

    public CollectionPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(StyleUtils.BG_LIGHT);
        setOpaque(false);

        // Header / Toolbar
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Transactions & Collectes");
        titleLabel.setFont(StyleUtils.FONT_TITLE);
        titleLabel.setForeground(StyleUtils.TEXT_DARK);
        topPanel.add(titleLabel, BorderLayout.WEST);

        JButton collectButton = StyleUtils.createModernButton("Nouvelle Collecte", StyleUtils.SUCCESS_GREEN);
        collectButton.addActionListener(e -> showCollectionDialog());

        JButton exportButton = StyleUtils.createModernButton("Exporter", StyleUtils.TEXT_GRAY);
        exportButton.setForeground(Color.WHITE);
        exportButton.addActionListener(e -> DataExporter.exportToCSV(table, this));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        btnPanel.add(collectButton);
        btnPanel.add(exportButton);
        topPanel.add(btnPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Collections Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));

        String[] columnNames = { "Référence", "Date", "Membre", "Montant", "Type", "Mode" };

        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        loadTransactionsFromDatabase();
        StyleUtils.styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);
    }

    private void loadTransactionsFromDatabase() {
        if (DatabaseConnection.OFFLINE_MODE) {
            model.setRowCount(0);
            model.addRow(
                    new Object[] { "TXN-2026-001", "16/02/2026 14:30", "Moussa Diakité", "25,000 GNF", "COTISATION",
                            "ESPECES" });
            model.addRow(new Object[] { "TXN-2026-002", "15/02/2026 10:15", "Awa Touré", "50,000 GNF", "DEPOT",
                    "MOBILE_MONEY" });
            model.addRow(new Object[] { "TXN-2026-003", "14/02/2026 16:45", "Abdoulaye Condé", "25,000 GNF",
                    "COTISATION", "ESPECES" });
            model.addRow(new Object[] { "TXN-2026-004", "13/02/2026 09:20", "Fatoumata Sow", "15,000 GNF",
                    "RETRAIT", "ESPECES" });
            model.addRow(new Object[] { "TXN-2026-005", "12/02/2026 11:10", "Ibrahima Bangoura", "10,000 GNF", "BONUS",
                    "ESPECES" });
            return;
        }

        String query = "SELECT t.reference, t.date_transaction, CONCAT(m.nom, ' ', m.prenoms) as membre, " +
                "CONCAT(FORMAT(t.montant, 0), ' GNF') as montant, t.type_transaction, t.mode_paiement " +
                "FROM Transactions t " +
                "JOIN Membres m ON t.id_membre = m.id_membre " +
                "ORDER BY t.date_transaction DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getString("reference"),
                        new SimpleDateFormat("dd/MM/yyyy HH:mm").format(rs.getTimestamp("date_transaction")),
                        rs.getString("membre"),
                        rs.getString("montant"),
                        rs.getString("type_transaction"),
                        rs.getString("mode_paiement")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.show(this, "Erreur lors du chargement des transactions", Toast.Type.ERROR);
        }
    }

    private void showCollectionDialog() {
        JDialog dialog = DialogUtils.createDialog(this, "Nouvelle Collecte", 450, 500);

        JPanel formPanel = DialogUtils.createFormPanel();

        // On utilisera un simple champ texte pour le code membre par simplicité pour
        // l'instant
        JTextField codeMembreField = StyleUtils.createModernTextField();
        JTextField montantField = StyleUtils.createModernTextField();
        JComboBox<String> typeCombo = new JComboBox<>(new String[] { "DEPOT", "RETRAIT", "PENALITE", "COTISATION" });
        typeCombo.setFont(StyleUtils.FONT_BODY);
        JComboBox<String> modeCombo = new JComboBox<>(new String[] { "ESPECES", "MOBILE_MONEY", "VIREMENT" });
        modeCombo.setFont(StyleUtils.FONT_BODY);

        DialogUtils.addFormField(formPanel, "Code Membre (ex: M001)", codeMembreField, 0);
        DialogUtils.addFormField(formPanel, "Type d'opération", typeCombo, 1);
        DialogUtils.addFormField(formPanel, "Mode de paiement", modeCombo, 2);
        DialogUtils.addFormField(formPanel, "Montant", montantField, 3);

        dialog.add(formPanel, BorderLayout.CENTER);

        JButton saveButton = StyleUtils.createModernButton("Valider la transaction", StyleUtils.SUCCESS_GREEN);
        saveButton.setPreferredSize(new Dimension(200, 45));

        JButton cancelButton = StyleUtils.createModernButton("Annuler", Color.LIGHT_GRAY);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.addActionListener(e -> dialog.dispose());

        saveButton.addActionListener(e -> {
            String code = codeMembreField.getText().trim();
            String montantStr = montantField.getText().trim();

            if (code.isEmpty() || montantStr.isEmpty()) {
                Toast.show(dialog, "Veuillez remplir tous les champs.", Toast.Type.ERROR);
                return;
            }

            try {
                double montant = Double.parseDouble(montantStr);
                saveTransactionToDatabase(code, (String) typeCombo.getSelectedItem(),
                        (String) modeCombo.getSelectedItem(), montant);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                Toast.show(dialog, "Montant invalide.", Toast.Type.ERROR);
            }
        });

        dialog.add(DialogUtils.createButtonPanel(cancelButton, saveButton), BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void saveTransactionToDatabase(String codeMembre, String type, String mode, double montant) {
        if (DatabaseConnection.OFFLINE_MODE) {
            Toast.show(this, "Transaction simulée réussie !", Toast.Type.SUCCESS);
            return;
        }

        // 1. Trouver l'ID du membre à partir du code
        int idMembre = -1;
        String findMember = "SELECT id_membre FROM Membres WHERE code_membre = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(findMember)) {
            pstmt.setString(1, codeMembre);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                idMembre = rs.getInt("id_membre");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (idMembre == -1) {
            Toast.show(this, "Membre non trouvé !", Toast.Type.ERROR);
            return;
        }

        // 2. Insérer la transaction
        // On utilise la procédure stockée sp_enregistrer_transaction si possible
        String sql = "{CALL sp_enregistrer_transaction(?, NULL, ?, ?, 'Opération manuelle', ?, 1)}";

        try (Connection conn = DatabaseConnection.getConnection();
                java.sql.CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, idMembre);
            cstmt.setString(2, type);
            cstmt.setDouble(3, montant);
            cstmt.setString(4, mode);

            cstmt.execute();
            Toast.show(this, "Transaction réussie !", Toast.Type.SUCCESS);
            loadTransactionsFromDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.show(this, "Erreur lors de l'enregistrement.", Toast.Type.ERROR);
        }
    }
}
