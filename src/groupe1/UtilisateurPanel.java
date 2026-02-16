package groupe1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UtilisateurPanel extends JPanel {

    private DefaultTableModel model;
    private JTable table;

    public UtilisateurPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(StyleUtils.BG_LIGHT);
        setOpaque(false);

        // Header
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Utilisateurs & Droits");
        titleLabel.setFont(StyleUtils.FONT_TITLE);
        titleLabel.setForeground(StyleUtils.TEXT_DARK);
        topPanel.add(titleLabel, BorderLayout.WEST);

        // Actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        JButton addButton = StyleUtils.createModernButton("Nouvel Utilisateur", StyleUtils.PRIMARY_BLUE);
        addButton.addActionListener(e -> showUserDialog());
        buttonPanel.add(addButton);
        
        topPanel.add(buttonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));

        String[] columnNames = {"ID", "Nom", "Prénom", "Login", "Rôle", "Dernière Connexion"};
        Object[][] data = {
            {"1", "Administrateur", "Principal", "admin", "ADMIN", "En ligne"},
            {"2", "Koffi", "Paul", "pkoffi", "GERANT", "15/02/2026 14:30"},
            {"3", "Soro", "Aminata", "asoro", "GERANT", "16/02/2026 09:15"}
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

    private void showUserDialog() {
        JDialog dialog = DialogUtils.createDialog(this, "Nouvel Utilisateur", 400, 480);

        JPanel formPanel = DialogUtils.createFormPanel();

        JTextField nomField = StyleUtils.createModernTextField();
        JTextField loginField = StyleUtils.createModernTextField();
        JPasswordField passField = new JPasswordField();
        passField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"GERANT", "ADMIN"});
        roleCombo.setFont(StyleUtils.FONT_BODY);
        
        DialogUtils.addFormField(formPanel, "Nom & Prénom", nomField, 0);
        DialogUtils.addFormField(formPanel, "Login", loginField, 1);
        DialogUtils.addFormField(formPanel, "Mot de passe", passField, 2);
        DialogUtils.addFormField(formPanel, "Rôle", roleCombo, 3);

        dialog.add(formPanel, BorderLayout.CENTER);

        JButton saveButton = StyleUtils.createModernButton("Créer", StyleUtils.PRIMARY_BLUE);
        saveButton.setPreferredSize(new Dimension(100, 35));
        
        JButton cancelButton = StyleUtils.createModernButton("Annuler", Color.LIGHT_GRAY);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.addActionListener(e -> dialog.dispose());
        
        saveButton.addActionListener(e -> {
             if(nomField.getText().isEmpty() || loginField.getText().isEmpty()) return;
             model.addRow(new Object[]{ model.getRowCount()+1, nomField.getText(), "", loginField.getText(), roleCombo.getSelectedItem(), "Jamais"});
             dialog.dispose();
        });

        dialog.add(DialogUtils.createButtonPanel(cancelButton, saveButton), BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
