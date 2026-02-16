package groupe1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

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

        String[] columnNames = {"ID Transaction", "Date", "Membre", "Montant", "Type", "Agent"};
        Object[][] data = {
            {"TRX-001", "16/02/2026", "KONE Moussa", "10 000 FCFA", "Dépôt", "Agent 1"},
            {"TRX-002", "16/02/2026", "TOURE Aicha", "25 000 FCFA", "Dépôt", "Agent 1"},
            {"TRX-003", "15/02/2026", "KOUASSI Jean", "5 000 FCFA", "Dépôt", "Agent 2"}
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

    private void showCollectionDialog() {
        JDialog dialog = DialogUtils.createDialog(this, "Nouvelle Collecte", 400, 450);

        JPanel formPanel = DialogUtils.createFormPanel();

        JTextField membreField = StyleUtils.createModernTextField();
        JTextField montantField = StyleUtils.createModernTextField();
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Dépôt", "Retrait"});
        typeCombo.setFont(StyleUtils.FONT_BODY);
        
        DialogUtils.addFormField(formPanel, "Membre", membreField, 0);
        DialogUtils.addFormField(formPanel, "Type", typeCombo, 1);
        DialogUtils.addFormField(formPanel, "Montant", montantField, 2);

        dialog.add(formPanel, BorderLayout.CENTER);

        JButton saveButton = StyleUtils.createModernButton("Valider", StyleUtils.SUCCESS_GREEN);
        saveButton.setPreferredSize(new Dimension(150, 40));
        
        JButton cancelButton = StyleUtils.createModernButton("Annuler", Color.LIGHT_GRAY);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.addActionListener(e -> dialog.dispose());

        saveButton.addActionListener(e -> {
            if (membreField.getText().isEmpty() || montantField.getText().isEmpty()) {
                 Toast.show(dialog, "Veuillez remplir tous les champs.", Toast.Type.ERROR);
                 return;
            }
            String newId = "TRX-" + String.format("%03d", model.getRowCount() + 1);
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            model.addRow(new Object[]{newId, date, membreField.getText(), montantField.getText() + " FCFA", typeCombo.getSelectedItem(), "Admin"});
            Toast.show(this, "Transaction enregistrée !", Toast.Type.SUCCESS);
            dialog.dispose();
        });

        dialog.add(DialogUtils.createButtonPanel(cancelButton, saveButton), BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
