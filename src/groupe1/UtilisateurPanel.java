package groupe1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

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
        JButton addButton = StyleUtils.createModernButton("Nouveau", StyleUtils.PRIMARY_BLUE);
        JButton editButton = StyleUtils.createModernButton("Modifier", StyleUtils.ACCENT_GOLD);
        JButton deleteButton = StyleUtils.createModernButton("Supprimer", StyleUtils.DANGER_RED);

        addButton.addActionListener(e -> showUserDialog(null));
        editButton.addActionListener(e -> editUser());
        deleteButton.addActionListener(e -> deleteUser());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        topPanel.add(buttonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));

        String[] columnNames = { "ID", "Nom Complet", "Login", "Rôle", "Statut", "Dernière Connexion" };

        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        loadUsersFromDatabase();
        StyleUtils.styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);
    }

    private void loadUsersFromDatabase() {
        if (DatabaseConnection.OFFLINE_MODE) {
            model.setRowCount(0);
            model.addRow(new Object[] { 1, "Administrateur Système", "admin", "ADMIN", "ACTIF", "Aujourd'hui" });
            model.addRow(new Object[] { 2, "Gestionnaire Principal", "gestion", "GESTIONNAIRE", "ACTIF", "Hier" });
            model.addRow(new Object[] { 3, "Opérateur 01", "op01", "OPERATEUR", "ACTIF", "14/02/2026" });
            return;
        }

        String query = "SELECT id_utilisateur, nom_complet, nom_utilisateur, role, statut, derniere_connexion FROM Utilisateurs";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("id_utilisateur"),
                        rs.getString("nom_complet"),
                        rs.getString("nom_utilisateur"),
                        rs.getString("role"),
                        rs.getString("statut"),
                        rs.getTimestamp("derniere_connexion") != null ? rs.getTimestamp("derniere_connexion") : "Jamais"
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.show(this, "Erreur de chargement", Toast.Type.ERROR);
        }
    }

    private void editUser() {
        int row = table.getSelectedRow();
        if (row == -1) {
            Toast.show(this, "Sélectionnez un utilisateur.", Toast.Type.INFO);
            return;
        }
        Object[] data = new Object[model.getColumnCount()];
        for (int i = 0; i < model.getColumnCount(); i++) {
            data[i] = model.getValueAt(row, i);
        }
        showUserDialog(data);
    }

    private void showUserDialog(Object[] existingData) {
        String title = (existingData == null) ? "Nouvel Utilisateur" : "Modifier Utilisateur";
        JDialog dialog = DialogUtils.createDialog(this, title, 400, 480);
        JPanel formPanel = DialogUtils.createFormPanel();

        JTextField nomField = StyleUtils.createModernTextField();
        JTextField loginField = StyleUtils.createModernTextField();
        JPasswordField passField = StyleUtils.createModernPasswordField();

        JComboBox<String> roleCombo = new JComboBox<>(new String[] { "OPERATEUR", "GESTIONNAIRE", "ADMIN" });
        roleCombo.setFont(StyleUtils.FONT_BODY);

        if (existingData != null) {
            nomField.setText(existingData[1].toString());
            loginField.setText(existingData[2].toString());
            roleCombo.setSelectedItem(existingData[3].toString());
        }

        DialogUtils.addFormField(formPanel, "Nom Complet", nomField, 0);
        DialogUtils.addFormField(formPanel, "Login", loginField, 1);
        DialogUtils.addFormField(formPanel, "Mot de passe (laisser vide si inchangé)", passField, 2);
        DialogUtils.addFormField(formPanel, "Rôle", roleCombo, 3);

        dialog.add(formPanel, BorderLayout.CENTER);

        JButton saveButton = StyleUtils.createModernButton(existingData == null ? "Créer" : "Enregistrer",
                StyleUtils.PRIMARY_BLUE);
        saveButton.setPreferredSize(new Dimension(100, 35));

        JButton cancelButton = StyleUtils.createModernButton("Annuler", Color.LIGHT_GRAY);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.addActionListener(e -> dialog.dispose());

        saveButton.addActionListener(e -> {
            String nom = nomField.getText().trim();
            String login = loginField.getText().trim();
            String pass = new String(passField.getPassword());

            if (nom.isEmpty() || login.isEmpty()) {
                Toast.show(dialog, "Veuillez remplir les champs obligatoires.", Toast.Type.ERROR);
                return;
            }

            if (existingData == null) {
                if (pass.isEmpty()) {
                    Toast.show(dialog, "Mot de passe requis pour un nouvel utilisateur.", Toast.Type.ERROR);
                    return;
                }
                saveUserToDatabase(nom, login, pass, (String) roleCombo.getSelectedItem());
            } else {
                updateUserInDatabase((int) existingData[0], nom, login, pass, (String) roleCombo.getSelectedItem());
            }
            dialog.dispose();
        });

        dialog.add(DialogUtils.createButtonPanel(cancelButton, saveButton), BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void saveUserToDatabase(String nom, String login, String pass, String role) {
        String sql = "INSERT INTO Utilisateurs (nom_complet, nom_utilisateur, mot_de_passe, role) VALUES (?, ?, MD5(?), ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nom);
            pstmt.setString(2, login);
            pstmt.setString(3, pass);
            pstmt.setString(4, role);
            pstmt.executeUpdate();
            Toast.show(this, "Utilisateur créé !", Toast.Type.SUCCESS);
            loadUsersFromDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.show(this, "Erreur lors de la création.", Toast.Type.ERROR);
        }
    }

    private void updateUserInDatabase(int id, String nom, String login, String pass, String role) {
        String sql;
        if (pass.isEmpty()) {
            sql = "UPDATE Utilisateurs SET nom_complet = ?, nom_utilisateur = ?, role = ? WHERE id_utilisateur = ?";
        } else {
            sql = "UPDATE Utilisateurs SET nom_complet = ?, nom_utilisateur = ?, mot_de_passe = MD5(?), role = ? WHERE id_utilisateur = ?";
        }

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (pass.isEmpty()) {
                pstmt.setString(1, nom);
                pstmt.setString(2, login);
                pstmt.setString(3, role);
                pstmt.setInt(4, id);
            } else {
                pstmt.setString(1, nom);
                pstmt.setString(2, login);
                pstmt.setString(3, pass);
                pstmt.setString(4, role);
                pstmt.setInt(5, id);
            }
            pstmt.executeUpdate();
            Toast.show(this, "Utilisateur mis à jour !", Toast.Type.SUCCESS);
            loadUsersFromDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.show(this, "Erreur lors de la mise à jour.", Toast.Type.ERROR);
        }
    }

    private void deleteUser() {
        int row = table.getSelectedRow();
        if (row == -1) {
            Toast.show(this, "Sélectionnez un utilisateur.", Toast.Type.INFO);
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        if (id == 1) { // Ne pas supprimer l'admin principal
            Toast.show(this, "Impossible de supprimer l'administrateur système.", Toast.Type.ERROR);
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "Supprimer cet utilisateur ?", "Confirmation",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement pstmt = conn
                            .prepareStatement("DELETE FROM Utilisateurs WHERE id_utilisateur = ?")) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                Toast.show(this, "Utilisateur supprimé.", Toast.Type.SUCCESS);
                loadUsersFromDatabase();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.show(this, "Erreur de suppression.", Toast.Type.ERROR);
            }
        }
    }
}
