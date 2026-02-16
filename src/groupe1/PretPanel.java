package groupe1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PretPanel extends JPanel {

    private DefaultTableModel model;
    private JTable table;

    public PretPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(StyleUtils.BG_LIGHT);
        setOpaque(false);

        // Header
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Gestion des Prêts & Crédits");
        titleLabel.setFont(StyleUtils.FONT_TITLE);
        titleLabel.setForeground(StyleUtils.TEXT_DARK);
        topPanel.add(titleLabel, BorderLayout.WEST);

        // Actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton newLoanBtn = StyleUtils.createModernButton("Nouveau Prêt", StyleUtils.PRIMARY_BLUE);
        newLoanBtn.addActionListener(e -> showLoanDialog());

        JButton exportBtn = StyleUtils.createModernButton("Exporter", StyleUtils.TEXT_GRAY);
        exportBtn.setForeground(Color.WHITE);
        exportBtn.addActionListener(e -> DataExporter.exportToCSV(table, this));

        buttonPanel.add(newLoanBtn);
        buttonPanel.add(exportBtn);

        topPanel.add(buttonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));

        String[] columnNames = { "Réf Prêt", "Membre", "Montant", "Taux (%)", "À Rembourser", "Date Échéance",
                "Statut" };

        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        loadLoansFromDatabase();
        StyleUtils.styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);
    }

    private void loadLoansFromDatabase() {
        if (DatabaseConnection.OFFLINE_MODE) {
            model.setRowCount(0);
            model.addRow(new Object[] { "PR-2026-001", "Abdoulaye Camara", "500,000 GNF", "5%", "525,000 GNF",
                    "15/05/2026", "EN_COURS" });
            model.addRow(new Object[] { "PR-2026-002", "Aicha Bangoura", "250,000 GNF", "5%", "262,500 GNF",
                    "20/04/2026", "EN_COURS" });
            model.addRow(new Object[] { "PR-2026-003", "Seydou Barry", "100,000 GNF", "5%", "105,000 GNF",
                    "10/03/2026", "EN_COURS" });
            model.addRow(new Object[] { "PR-2026-004", "Fatoumata Sow", "75,000 GNF", "5%", "78,750 GNF",
                    "01/01/2026", "TERMINE" });
            return;
        }

        // Real implementation would fetch from a 'Prets' table which might not exist
        // yet
        // For now, in online mode we show an empty table or a message
        model.setRowCount(0);
    }

    private void showLoanDialog() {
        JDialog dialog = DialogUtils.createDialog(this, "Nouveau Prêt", 450, 500);
        JPanel formPanel = DialogUtils.createFormPanel();

        JTextField memberCodeField = StyleUtils.createModernTextField();
        JTextField amountField = StyleUtils.createModernTextField();
        JTextField rateField = StyleUtils.createModernTextField();
        rateField.setText("5"); // Default rate
        JTextField dateField = StyleUtils.createModernTextField();
        dateField.setText("dd/mm/yyyy");

        DialogUtils.addFormField(formPanel, "Code Membre", memberCodeField, 0);
        DialogUtils.addFormField(formPanel, "Montant du Prêt", amountField, 1);
        DialogUtils.addFormField(formPanel, "Taux d'intérêt (%)", rateField, 2);
        DialogUtils.addFormField(formPanel, "Date d'échéance", dateField, 3);

        dialog.add(formPanel, BorderLayout.CENTER);

        JButton saveButton = StyleUtils.createModernButton("Enregistrer le Prêt", StyleUtils.SUCCESS_GREEN);
        JButton cancelButton = StyleUtils.createModernButton("Annuler", Color.LIGHT_GRAY);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.addActionListener(e -> dialog.dispose());

        saveButton.addActionListener(e -> {
            if (DatabaseConnection.OFFLINE_MODE) {
                Toast.show(this, "Prêt simulé enregistré avec succès !", Toast.Type.SUCCESS);
                dialog.dispose();
            } else {
                Toast.show(this, "Fonctionnalité non implémentée en mode réel.", Toast.Type.INFO);
            }
        });

        dialog.add(DialogUtils.createButtonPanel(cancelButton, saveButton), BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
