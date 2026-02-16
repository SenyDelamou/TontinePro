package groupe1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MembersPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public MembersPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(StyleUtils.BG_LIGHT);
        setOpaque(false);

        // Header / Toolbar
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Gestion des Membres");
        titleLabel.setFont(StyleUtils.FONT_TITLE);
        titleLabel.setForeground(StyleUtils.TEXT_DARK);
        topPanel.add(titleLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton addButton = StyleUtils.createModernButton("Ajouter", StyleUtils.PRIMARY_BLUE);
        JButton editButton = StyleUtils.createModernButton("Modifier", StyleUtils.ACCENT_GOLD);
        JButton deleteButton = StyleUtils.createModernButton("Supprimer", StyleUtils.DANGER_RED);
        
        // Listeners (Keeping existing logic logic)
        addButton.addActionListener(e -> showMemberDialog("Ajouter un membre", null));
        editButton.addActionListener(e -> editMember());
        deleteButton.addActionListener(e -> deleteMember());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        JButton exportButton = StyleUtils.createModernButton("Exporter", StyleUtils.TEXT_GRAY);
        exportButton.setForeground(Color.WHITE);
        exportButton.addActionListener(e -> DataExporter.exportToCSV(table, this));
        buttonPanel.add(exportButton);
        
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);

        // Table in Card
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        
        String[] columnNames = {"ID", "Nom", "Prénoms", "Téléphone", "Adresse", "Solde"};
        Object[][] data = {
            {"M001", "KONE", "Moussa", "01020304", "Abidjan", "150 000 FCFA"},
            {"M002", "TOURE", "Aicha", "05060708", "Bouaké", "200 000 FCFA"},
            {"M003", "KOUASSI", "Jean", "09101112", "Yamoussoukro", "75 000 FCFA"}
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
    
    private void editMember() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            Toast.show(this, "Sélectionnez un membre.", Toast.Type.INFO);
            return;
        }
        Object[] rowData = new Object[model.getColumnCount()];
        for (int i = 0; i < model.getColumnCount(); i++) {
            rowData[i] = model.getValueAt(selectedRow, i);
        }
        showMemberDialog("Modifier", rowData);
    }

    private void deleteMember() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            Toast.show(this, "Sélectionnez un membre.", Toast.Type.INFO);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(selectedRow);
            Toast.show(this, "Membre supprimé.", Toast.Type.SUCCESS);
        }
    }

    private void showMemberDialog(String title, Object[] existingData) {
        JDialog dialog = DialogUtils.createDialog(this, title, 450, 550);

        JPanel formPanel = DialogUtils.createFormPanel();

        JTextField nomField = StyleUtils.createModernTextField();
        JTextField prenomField = StyleUtils.createModernTextField();
        JTextField telField = StyleUtils.createModernTextField();
        JTextField adresseField = StyleUtils.createModernTextField();
        
        DialogUtils.addFormField(formPanel, "Nom", nomField, 0);
        DialogUtils.addFormField(formPanel, "Prénoms", prenomField, 1);
        DialogUtils.addFormField(formPanel, "Téléphone", telField, 2);
        DialogUtils.addFormField(formPanel, "Adresse", adresseField, 3);

        if (existingData != null) {
            nomField.setText(existingData[1].toString());
            prenomField.setText(existingData[2].toString());
            telField.setText(existingData[3].toString());
            adresseField.setText(existingData[4].toString());
        }

        dialog.add(formPanel, BorderLayout.CENTER);

        JButton saveButton = StyleUtils.createModernButton("Enregistrer", StyleUtils.PRIMARY_BLUE);
        saveButton.setPreferredSize(new Dimension(150, 40));
        
        JButton cancelButton = StyleUtils.createModernButton("Annuler", Color.LIGHT_GRAY);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.addActionListener(e -> dialog.dispose());
        
        saveButton.addActionListener(e -> {
            if (existingData == null) {
                String newId = "M" + String.format("%03d", model.getRowCount() + 1);
                model.addRow(new Object[]{newId, nomField.getText(), prenomField.getText(), telField.getText(), adresseField.getText(), "0 FCFA"});
                Toast.show(this, "Membre ajouté avec succès !", Toast.Type.SUCCESS);
            } else {
                int selectedRow = table.getSelectedRow();
                model.setValueAt(nomField.getText(), selectedRow, 1);
                model.setValueAt(prenomField.getText(), selectedRow, 2);
                model.setValueAt(telField.getText(), selectedRow, 3);
                model.setValueAt(adresseField.getText(), selectedRow, 4);
                Toast.show(this, "Membre modifié avec succès.", Toast.Type.SUCCESS);
            }
            dialog.dispose();
        });

        dialog.add(DialogUtils.createButtonPanel(cancelButton, saveButton), BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
