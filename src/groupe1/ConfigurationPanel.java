package groupe1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationPanel extends JPanel {

    private DefaultTableModel cycleModel;
    private JTable cycleTable;
    private Map<String, JTextField> settingFields = new HashMap<>();

    public ConfigurationPanel() {
        setLayout(new BorderLayout());
        setBackground(StyleUtils.BG_LIGHT);
        setOpaque(true);

        // Header Section
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(25, 40, 20, 40));

        JLabel titleLabel = new JLabel("Configuration Système");
        titleLabel.setFont(StyleUtils.FONT_TITLE);
        titleLabel.setForeground(StyleUtils.TEXT_DARK);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Gérer les cycles de tontine et les paramètres de l'application");
        subtitle.setFont(StyleUtils.FONT_SUBTITLE);
        subtitle.setForeground(StyleUtils.TEXT_GRAY);
        headerPanel.add(subtitle, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBorder(new EmptyBorder(0, 40, 40, 40));

        tabbedPane.addTab("Cycles & Périodes", createCyclesTab());
        tabbedPane.addTab("Paramètres Généraux", createGeneralSettingsTab());

        add(tabbedPane, BorderLayout.CENTER);

        loadCyclesFromDatabase();
        loadSettingsFromDatabase();
    }

    private JPanel createCyclesTab() {
        JPanel container = new JPanel(new BorderLayout(0, 20));
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(20, 0, 0, 0));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        toolbar.setOpaque(false);

        JButton newCycleBtn = StyleUtils.createModernButton("Nouveau Cycle", StyleUtils.PRIMARY_BLUE);
        JButton editCycleBtn = StyleUtils.createModernButton("Modifier", StyleUtils.ACCENT_GOLD);
        JButton closeCycleBtn = StyleUtils.createModernButton("Clôturer", StyleUtils.DANGER_RED);

        newCycleBtn.addActionListener(e -> showCycleDialog("Nouveau Cycle", null));
        editCycleBtn.addActionListener(e -> editCycle());
        closeCycleBtn.addActionListener(e -> closeCycle());

        toolbar.add(newCycleBtn);
        toolbar.add(editCycleBtn);
        toolbar.add(closeCycleBtn);
        container.add(toolbar, BorderLayout.NORTH);

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                new EmptyBorder(5, 5, 5, 5)));

        String[] cols = { "ID", "Libellé", "Début", "Fin", "Statut" };
        cycleModel = new DefaultTableModel(cols, 0);
        cycleTable = new JTable(cycleModel);
        StyleUtils.styleTable(cycleTable);
        cycleTable.setRowHeight(45);

        // Hide ID Column
        cycleTable.getColumnModel().getColumn(0).setMinWidth(0);
        cycleTable.getColumnModel().getColumn(0).setMaxWidth(0);

        JScrollPane scroll = new JScrollPane(cycleTable);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);

        tableCard.add(scroll, BorderLayout.CENTER);
        container.add(tableCard, BorderLayout.CENTER);
        return container;
    }

    private JPanel createGeneralSettingsTab() {
        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setOpaque(false);
        scrollContent.setBorder(new EmptyBorder(20, 0, 0, 0));

        JPanel identityCard = createSettingsCard("Identité du Système",
                IconUtils.createSettingsIcon(18, StyleUtils.PRIMARY_BLUE));
        identityCard.add(createInputRow("APP_NAME", "Nom de l'Organisation"));
        identityCard.add(createInputRow("DEVISE", "Devise par défaut"));

        JPanel sysCard = createSettingsCard("Paramètres Financiers",
                IconUtils.createMoneyIcon(18, StyleUtils.SUCCESS_GREEN));
        sysCard.add(createInputRow("PENALITE_RETARD", "Pénalité de retard"));
        sysCard.add(createInputRow("TAUX_INTERET", "Taux d'intérêt (%)"));

        scrollContent.add(identityCard);
        scrollContent.add(Box.createVerticalStrut(20));
        scrollContent.add(sysCard);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        JButton saveAll = StyleUtils.createModernButton("Sauvegarder les paramètres", StyleUtils.SUCCESS_GREEN);
        saveAll.addActionListener(e -> saveSettingsToDatabase());
        saveAll.setPreferredSize(new Dimension(220, 45));
        footer.add(saveAll);
        scrollContent.add(footer);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(new JScrollPane(scrollContent), BorderLayout.CENTER);
        return wrapper;
    }

    private void loadCyclesFromDatabase() {
        if (DatabaseConnection.OFFLINE_MODE) {
            cycleModel.setRowCount(0);
            cycleModel.addRow(new Object[] { 1, "Cycle Janvier-Juin 2026", "2026-01-01", "2026-06-30", "EN_COURS" });
            cycleModel
                    .addRow(new Object[] { 2, "Cycle Juillet-Décembre 2026", "2026-07-01", "2026-12-31", "PLANIFIE" });
            return;
        }

        String query = "SELECT id_cycle, nom_cycle, date_debut, date_fin, statut FROM Cycles";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            cycleModel.setRowCount(0);
            while (rs.next()) {
                cycleModel.addRow(new Object[] {
                        rs.getInt("id_cycle"), rs.getString("nom_cycle"),
                        rs.getString("date_debut"), rs.getString("date_fin"),
                        rs.getString("statut")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSettingsFromDatabase() {
        if (DatabaseConnection.OFFLINE_MODE) {
            settingFields.get("APP_NAME").setText("TontinePro (Demo)");
            settingFields.get("DEVISE").setText("FCFA");
            settingFields.get("PENALITE_RETARD").setText("500");
            settingFields.get("TAUX_INTERET").setText("2.5");
            return;
        }

        String query = "SELECT cle_config, valeur_config FROM Configuration";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String key = rs.getString("cle_config");
                if (settingFields.containsKey(key)) {
                    settingFields.get(key).setText(rs.getString("valeur_config"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveSettingsToDatabase() {
        String sql = "UPDATE Configuration SET valeur_config = ? WHERE cle_config = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Map.Entry<String, JTextField> entry : settingFields.entrySet()) {
                pstmt.setString(1, entry.getValue().getText());
                pstmt.setString(2, entry.getKey());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            Toast.show(this, "Paramètres sauvegardés !", Toast.Type.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.show(this, "Erreur de sauvegarde.", Toast.Type.ERROR);
        }
    }

    private JPanel createSettingsCard(String title, Icon icon) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                new EmptyBorder(20, 25, 20, 25)));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel l = new JLabel(title);
        l.setFont(new Font("Segoe UI", Font.BOLD, 16));
        l.setIcon(icon);
        l.setIconTextGap(10);
        l.setBorder(new EmptyBorder(0, 0, 20, 0));
        card.add(l);
        return card;
    }

    private JPanel createInputRow(String key, String label) {
        JPanel row = new JPanel(new BorderLayout(15, 5));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(800, 75));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(new EmptyBorder(0, 0, 15, 0));
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(StyleUtils.TEXT_GRAY);
        l.setPreferredSize(new Dimension(200, 30));
        row.add(l, BorderLayout.WEST);
        JTextField f = StyleUtils.createModernTextField();
        settingFields.put(key, f);
        row.add(f, BorderLayout.CENTER);
        return row;
    }

    private void editCycle() {
        int row = cycleTable.getSelectedRow();
        if (row == -1)
            return;
        Object[] data = new Object[5];
        for (int i = 0; i < 5; i++)
            data[i] = cycleModel.getValueAt(row, i);
        showCycleDialog("Modifier Cycle", data);
    }

    private void closeCycle() {
        int row = cycleTable.getSelectedRow();
        if (row == -1)
            return;
        int id = (int) cycleModel.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(this, "Clôturer ce cycle ?", "Confirmation",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            updateCycleStatus(id, "TERMINE");
        }
    }

    private void updateCycleStatus(int id, String status) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("UPDATE Cycles SET statut = ? WHERE id_cycle = ?")) {
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            loadCyclesFromDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCycleDialog(String title, Object[] existingData) {
        JDialog dialog = DialogUtils.createDialog(this, title, 400, 480);
        JPanel formPanel = DialogUtils.createFormPanel();
        JTextField libelleField = StyleUtils.createModernTextField();
        JTextField debutField = StyleUtils.createModernTextField();
        JTextField finField = StyleUtils.createModernTextField();
        JComboBox<String> statutCombo = new JComboBox<>(new String[] { "PLANIFIE", "EN_COURS", "TERMINE" });

        DialogUtils.addFormField(formPanel, "Libellé", libelleField, 0);
        DialogUtils.addFormField(formPanel, "Début (yyyy-mm-dd)", debutField, 1);
        DialogUtils.addFormField(formPanel, "Fin (yyyy-mm-dd)", finField, 2);
        DialogUtils.addFormField(formPanel, "Statut", statutCombo, 3);

        if (existingData != null) {
            libelleField.setText(existingData[1].toString());
            debutField.setText(existingData[2].toString());
            finField.setText(existingData[3].toString());
            statutCombo.setSelectedItem(existingData[4].toString());
        }

        JButton saveButton = StyleUtils.createModernButton("Sauvegarder", StyleUtils.PRIMARY_BLUE);
        saveButton.addActionListener(e -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                PreparedStatement pstmt;
                if (existingData == null) {
                    pstmt = conn.prepareStatement(
                            "INSERT INTO Cycles (nom_cycle, date_debut, date_fin, statut, montant_cotisation) VALUES (?, ?, ?, ?, 0)");
                } else {
                    pstmt = conn.prepareStatement(
                            "UPDATE Cycles SET nom_cycle=?, date_debut=?, date_fin=?, statut=? WHERE id_cycle=?");
                    pstmt.setInt(5, (int) existingData[0]);
                }
                pstmt.setString(1, libelleField.getText());
                pstmt.setString(2, debutField.getText());
                pstmt.setString(3, finField.getText());
                pstmt.setString(4, (String) statutCombo.getSelectedItem());
                pstmt.executeUpdate();
                loadCyclesFromDatabase();
                dialog.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        JButton cancel = StyleUtils.createModernButton("Annuler", Color.LIGHT_GRAY);
        cancel.addActionListener(e -> dialog.dispose());
        dialog.add(DialogUtils.createButtonPanel(cancel, saveButton), BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
