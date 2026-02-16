package groupe1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ConfigurationPanel extends JPanel {

    private DefaultTableModel cycleModel;
    private JTable cycleTable;

    public ConfigurationPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(StyleUtils.BG_LIGHT);
        setOpaque(false);

        JLabel titleLabel = new JLabel("Configuration Système");
        titleLabel.setFont(StyleUtils.FONT_TITLE);
        titleLabel.setForeground(StyleUtils.TEXT_DARK);
        add(titleLabel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(StyleUtils.FONT_BOLD);
        tabbedPane.addTab("Cycles & Périodes", createCyclesPanel());
        tabbedPane.addTab("Paramètres Généraux", createGeneralSettingsPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createCyclesPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Buttons
        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.setBackground(Color.WHITE);
        
        JButton newCycleBtn = StyleUtils.createModernButton("Nouveau Cycle", StyleUtils.PRIMARY_BLUE);
        JButton editCycleBtn = StyleUtils.createModernButton("Modifier", StyleUtils.ACCENT_GOLD);
        JButton closeCycleBtn = StyleUtils.createModernButton("Clôturer", StyleUtils.TEXT_GRAY);

        newCycleBtn.addActionListener(e -> showCycleDialog("Nouveau Cycle", null));
        editCycleBtn.addActionListener(e -> editCycle());
        closeCycleBtn.addActionListener(e -> closeCycle());

        top.add(newCycleBtn);
        top.add(editCycleBtn);
        top.add(closeCycleBtn);
        
        panel.add(top, BorderLayout.NORTH);

        // Table
        String[] cols = {"Cycle", "Début", "Fin", "Statut"};
        Object[][] data = {
            {"Cycle 1 - Janvier 2026", "01/01/2026", "31/01/2026", "CLOTURE"},
            {"Cycle 2 - Février 2026", "01/02/2026", "28/02/2026", "EN_COURS"},
            {"Cycle 3 - Mars 2026", "01/03/2026", "31/03/2026", "A_VENIR"}
        };
        cycleModel = new DefaultTableModel(data, cols);
        cycleTable = new JTable(cycleModel);
        StyleUtils.styleTable(cycleTable);
        
        JScrollPane scroll = new JScrollPane(cycleTable);
        scroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        scroll.getViewport().setBackground(Color.WHITE);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void editCycle() {
        int selectedRow = cycleTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un cycle.", "Attention", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Object[] rowData = new Object[4];
        for (int i = 0; i < 4; i++) rowData[i] = cycleModel.getValueAt(selectedRow, i);
        showCycleDialog("Modifier Cycle", rowData);
    }

    private void closeCycle() {
        int selectedRow = cycleTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un cycle à clôturer.", "Attention", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment clôturer ce cycle ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            cycleModel.setValueAt("CLOTURE", selectedRow, 3);
        }
    }

    private void showCycleDialog(String title, Object[] existingData) {
        JDialog dialog = DialogUtils.createDialog(this, title, 400, 480);

        JPanel formPanel = DialogUtils.createFormPanel();

        JTextField libelleField = StyleUtils.createModernTextField();
        JTextField debutField = StyleUtils.createModernTextField();
        JTextField finField = StyleUtils.createModernTextField();
        JComboBox<String> statutCombo = new JComboBox<>(new String[]{"A_VENIR", "EN_COURS", "CLOTURE"});
        statutCombo.setFont(StyleUtils.FONT_BODY);

        DialogUtils.addFormField(formPanel, "Libellé", libelleField, 0);
        DialogUtils.addFormField(formPanel, "Date Début (jj/mm/aaaa)", debutField, 1);
        DialogUtils.addFormField(formPanel, "Date Fin (jj/mm/aaaa)", finField, 2);
        DialogUtils.addFormField(formPanel, "Statut", statutCombo, 3);

        if (existingData != null) {
            libelleField.setText(existingData[0].toString());
            debutField.setText(existingData[1].toString());
            finField.setText(existingData[2].toString());
            statutCombo.setSelectedItem(existingData[3].toString());
        }

        dialog.add(formPanel, BorderLayout.CENTER);

        JButton saveButton = StyleUtils.createModernButton("Enregistrer", StyleUtils.PRIMARY_BLUE);
        saveButton.setPreferredSize(new Dimension(150, 40));
        
        JButton cancelButton = StyleUtils.createModernButton("Annuler", Color.LIGHT_GRAY);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.addActionListener(e -> dialog.dispose());
        
        saveButton.addActionListener(e -> {
            if (libelleField.getText().isEmpty() || debutField.getText().isEmpty()) return;
            
            if (existingData == null) {
                cycleModel.addRow(new Object[]{libelleField.getText(), debutField.getText(), finField.getText(), statutCombo.getSelectedItem()});
            } else {
                int selectedRow = cycleTable.getSelectedRow();
                cycleModel.setValueAt(libelleField.getText(), selectedRow, 0);
                cycleModel.setValueAt(debutField.getText(), selectedRow, 1);
                cycleModel.setValueAt(finField.getText(), selectedRow, 2);
                cycleModel.setValueAt(statutCombo.getSelectedItem(), selectedRow, 3);
            }
            dialog.dispose();
        });

        dialog.add(DialogUtils.createButtonPanel(cancelButton, saveButton), BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private JPanel createGeneralSettingsPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        panel.add(new JLabel("Nom de l'Application:"));
        panel.add(StyleUtils.createModernTextField());
        
        panel.add(new JLabel("Devise:"));
        JTextField devise = StyleUtils.createModernTextField();
        devise.setText("FCFA");
        panel.add(devise);
        
        panel.add(new JLabel("Sauvegarde Auto:"));
        JCheckBox autoSaveWithText = new JCheckBox("Activer");
        autoSaveWithText.setBackground(Color.WHITE);
        panel.add(autoSaveWithText);

        JButton saveBtn = StyleUtils.createModernButton("Sauvegarder", StyleUtils.SUCCESS_GREEN);
        panel.add(new JLabel("")); 
        panel.add(saveBtn);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(panel, BorderLayout.NORTH);
        return wrapper;
    }
    

}
