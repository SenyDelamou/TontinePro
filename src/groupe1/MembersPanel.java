package groupe1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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

        // Listeners
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

        JButton cardButton = StyleUtils.createModernButton("Carte ID", StyleUtils.PRIMARY_BLUE.brighter());
        cardButton.addActionListener(e -> generateCard());
        buttonPanel.add(cardButton);

        topPanel.add(buttonPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Table in Card
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));

        // Added "Photo" column (Index 6)
        String[] columnNames = { "ID", "Nom", "Prénoms", "Téléphone", "Adresse", "Solde", "Photo" };

        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);

        // Charger les données depuis la base de données
        loadMembersFromDatabase();
        StyleUtils.styleTable(table);

        // Hide Photo Column
        table.getColumnModel().getColumn(6).setMinWidth(0);
        table.getColumnModel().getColumn(6).setMaxWidth(0);
        table.getColumnModel().getColumn(6).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);
    }

    private void generateCard() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            Toast.show(this, "Sélectionnez un membre pour générer sa carte.", Toast.Type.INFO);
            return;
        }

        String id = model.getValueAt(selectedRow, 0).toString();
        String name = model.getValueAt(selectedRow, 1) + " " + model.getValueAt(selectedRow, 2);
        String phone = model.getValueAt(selectedRow, 3).toString();
        String address = model.getValueAt(selectedRow, 4).toString();
        String photo = model.getValueAt(selectedRow, 6).toString(); // Hidden column

        CardGenerator.generateCard(this, name, id, phone, address, photo);
    }

    // ... editMember and deleteMember ...

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

        String codeMembre = model.getValueAt(selectedRow, 0).toString();
        String nomComplet = model.getValueAt(selectedRow, 1) + " " + model.getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Confirmer la suppression de " + nomComplet + " ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            deleteMemberFromDatabase(codeMembre);
        }
    }

    private void showMemberDialog(String title, Object[] existingData) {
        JDialog dialog = DialogUtils.createDialog(this, title, 500, 600);

        JPanel formPanel = DialogUtils.createFormPanel();

        JTextField nomField = StyleUtils.createModernTextField();
        JTextField prenomField = StyleUtils.createModernTextField();
        JTextField telField = StyleUtils.createModernTextField();
        JTextField adresseField = StyleUtils.createModernTextField();

        // Photo Field
        JPanel photoPanel = new JPanel(new BorderLayout(5, 0));
        photoPanel.setOpaque(false);
        JTextField photoPathField = StyleUtils.createModernTextField();
        photoPathField.setEditable(false);
        JButton browseBtn = new JButton("...");
        browseBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                photoPathField.setText(fc.getSelectedFile().getAbsolutePath());
            }
        });
        photoPanel.add(photoPathField, BorderLayout.CENTER);
        photoPanel.add(browseBtn, BorderLayout.EAST);

        DialogUtils.addFormField(formPanel, "Nom", nomField, 0);
        DialogUtils.addFormField(formPanel, "Prénoms", prenomField, 1);
        DialogUtils.addFormField(formPanel, "Téléphone", telField, 2);
        DialogUtils.addFormField(formPanel, "Adresse", adresseField, 3);
        DialogUtils.addFormField(formPanel, "Photo", photoPanel, 4);

        if (existingData != null) {
            nomField.setText(existingData[1].toString());
            prenomField.setText(existingData[2].toString());
            telField.setText(existingData[3].toString());
            adresseField.setText(existingData[4].toString());
            if (existingData[6] != null)
                photoPathField.setText(existingData[6].toString());
        }

        dialog.add(formPanel, BorderLayout.CENTER);

        JButton saveButton = StyleUtils.createModernButton("Enregistrer", StyleUtils.PRIMARY_BLUE);
        saveButton.setPreferredSize(new Dimension(150, 40));

        JButton cancelButton = StyleUtils.createModernButton("Annuler", Color.LIGHT_GRAY);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.addActionListener(e -> dialog.dispose());

        saveButton.addActionListener(e -> {
            String nom = nomField.getText().trim();
            String prenoms = prenomField.getText().trim();
            String telephone = telField.getText().trim();
            String adresse = adresseField.getText().trim();
            String photoPath = photoPathField.getText().trim();

            // Validation
            if (nom.isEmpty() || prenoms.isEmpty() || telephone.isEmpty()) {
                Toast.show(this, "Veuillez remplir tous les champs obligatoires", Toast.Type.ERROR);
                return;
            }

            if (existingData == null) {
                // Nouveau membre
                saveMemberToDatabase(nom, prenoms, telephone, adresse, photoPath);
            } else {
                // Modification
                String codeMembre = existingData[0].toString();
                updateMemberInDatabase(codeMembre, nom, prenoms, telephone, adresse, photoPath);
            }
            dialog.dispose();
        });

        dialog.add(DialogUtils.createButtonPanel(cancelButton, saveButton), BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Charger les membres depuis la base de données
     */
    private void loadMembersFromDatabase() {
        if (DatabaseConnection.OFFLINE_MODE) {
            model.setRowCount(0);
            model.addRow(
                    new Object[] { "M001", "KONE", "Moussa", "624-00-11-22", "Kaloum, Conakry", "150,000 GNF", "" });
            model.addRow(
                    new Object[] { "M002", "TOURE", "Aicha", "620-33-44-55", "Ratoma, Conakry", "200,000 GNF", "" });
            model.addRow(
                    new Object[] { "M003", "DIALLO", "Alpha", "664-55-66-77", "Kankan centre", "75,000 GNF", "" });
            model.addRow(
                    new Object[] { "M004", "SYLLA", "Fanta", "622-88-99-00", "Labé, Guinée", "320,000 GNF", "" });
            model.addRow(
                    new Object[] { "M005", "TRAORE", "Bakary", "621-11-22-33", "Nzérékoré", "125,000 GNF", "" });
            return;
        }

        String query = "SELECT code_membre, nom, prenoms, telephone, " +
                "CONCAT(ville, ', ', COALESCE(adresse, '')) as adresse, " +
                "CONCAT(FORMAT(solde_compte, 0), ' GNF') as solde, " +
                "COALESCE(photo_path, '') as photo " +
                "FROM Membres WHERE statut = 'ACTIF' ORDER BY id_membre";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            model.setRowCount(0); // Vider le tableau

            while (rs.next()) {
                Object[] row = {
                        rs.getString("code_membre"),
                        rs.getString("nom"),
                        rs.getString("prenoms"),
                        rs.getString("telephone"),
                        rs.getString("adresse"),
                        rs.getString("solde"),
                        rs.getString("photo")
                };
                model.addRow(row);
            }
            System.out.println("✅ " + model.getRowCount() + " membres chargés");
        } catch (Exception e) {
            System.err.println("❌ Erreur lors du chargement des membres");
            e.printStackTrace();
            Toast.show(this, "Erreur de chargement des données", Toast.Type.ERROR);
        }
    }

    /**
     * Sauvegarder un membre dans la base de données
     */
    private void saveMemberToDatabase(String nom, String prenoms, String telephone, String adresse, String photoPath) {
        try {
            // Générer un code membre unique
            String codeMembre = generateMemberCode();

            String query = "INSERT INTO Membres (code_membre, nom, prenoms, telephone, adresse, ville, photo_path, date_adhesion, statut) "
                    +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, CURDATE(), 'ACTIF')";

            java.sql.PreparedStatement pstmt = DatabaseConnection.prepareStatement(query);
            if (pstmt != null) {
                pstmt.setString(1, codeMembre);
                pstmt.setString(2, nom);
                pstmt.setString(3, prenoms);
                pstmt.setString(4, telephone);
                pstmt.setString(5, adresse != null ? adresse : "");
                pstmt.setString(6, extractCity(adresse));
                pstmt.setString(7, photoPath != null ? photoPath : "");

                int result = pstmt.executeUpdate();
                if (result > 0) {
                    System.out.println("✅ Membre " + codeMembre + " ajouté avec succès");
                    loadMembersFromDatabase(); // Recharger les données
                    Toast.show(this, "Membre ajouté avec succès !", Toast.Type.SUCCESS);
                }
                pstmt.close();
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'ajout du membre");
            e.printStackTrace();
            Toast.show(this, "Erreur lors de l'ajout du membre", Toast.Type.ERROR);
        }
    }

    /**
     * Mettre à jour un membre dans la base de données
     */
    private void updateMemberInDatabase(String codeMembre, String nom, String prenoms, String telephone, String adresse,
            String photoPath) {
        try {
            String query = "UPDATE Membres SET nom = ?, prenoms = ?, telephone = ?, adresse = ?, ville = ?, photo_path = ? "
                    +
                    "WHERE code_membre = ?";

            java.sql.PreparedStatement pstmt = DatabaseConnection.prepareStatement(query);
            if (pstmt != null) {
                pstmt.setString(1, nom);
                pstmt.setString(2, prenoms);
                pstmt.setString(3, telephone);
                pstmt.setString(4, adresse != null ? adresse : "");
                pstmt.setString(5, extractCity(adresse));
                pstmt.setString(6, photoPath != null ? photoPath : "");
                pstmt.setString(7, codeMembre);

                int result = pstmt.executeUpdate();
                if (result > 0) {
                    System.out.println("✅ Membre " + codeMembre + " modifié avec succès");
                    loadMembersFromDatabase();
                    Toast.show(this, "Membre modifié avec succès !", Toast.Type.SUCCESS);
                }
                pstmt.close();
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la modification du membre");
            e.printStackTrace();
            Toast.show(this, "Erreur lors de la modification", Toast.Type.ERROR);
        }
    }

    /**
     * Supprimer un membre de la base de données
     */
    private void deleteMemberFromDatabase(String codeMembre) {
        try {
            String query = "UPDATE Membres SET statut = 'INACTIF' WHERE code_membre = ?";

            java.sql.PreparedStatement pstmt = DatabaseConnection.prepareStatement(query);
            if (pstmt != null) {
                pstmt.setString(1, codeMembre);

                int result = pstmt.executeUpdate();
                if (result > 0) {
                    System.out.println("✅ Membre " + codeMembre + " désactivé");
                    loadMembersFromDatabase();
                    Toast.show(this, "Membre supprimé avec succès !", Toast.Type.SUCCESS);
                }
                pstmt.close();
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la suppression du membre");
            e.printStackTrace();
            Toast.show(this, "Erreur lors de la suppression", Toast.Type.ERROR);
        }
    }

    /**
     * Générer un code membre unique
     */
    private String generateMemberCode() {
        String query = "SELECT MAX(CAST(SUBSTRING(code_membre, 2) AS UNSIGNED)) as max_code FROM Membres";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                int maxCode = rs.getInt("max_code");
                return String.format("M%03d", maxCode + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "M001";
    }

    /**
     * Extraire la ville de l'adresse
     */
    private String extractCity(String adresse) {
        if (adresse == null || adresse.isEmpty())
            return "";
        String[] parts = adresse.split(",");
        return parts.length > 0 ? parts[0].trim() : adresse;
    }
}
