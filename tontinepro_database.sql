-- =====================================================
-- TontinePro - Base de Données Complète
-- Version: 1.0
-- Date: 2026-02-16
-- =====================================================

-- Suppression de la base si elle existe
DROP DATABASE IF EXISTS tontinepro;
CREATE DATABASE tontinepro CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tontinepro;

-- =====================================================
-- TABLE: Utilisateurs (Système d'authentification)
-- =====================================================
CREATE TABLE Utilisateurs (
    id_utilisateur INT AUTO_INCREMENT PRIMARY KEY,
    nom_utilisateur VARCHAR(100) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    nom_complet VARCHAR(200) NOT NULL,
    email VARCHAR(150) UNIQUE,
    role ENUM('ADMIN', 'GESTIONNAIRE', 'OPERATEUR') DEFAULT 'OPERATEUR',
    statut ENUM('ACTIF', 'INACTIF', 'SUSPENDU') DEFAULT 'ACTIF',
    photo_profil VARCHAR(500),
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    derniere_connexion TIMESTAMP NULL,
    INDEX idx_nom_utilisateur (nom_utilisateur),
    INDEX idx_role (role)
) ENGINE=InnoDB;

-- =====================================================
-- TABLE: Membres (Participants à la tontine)
-- =====================================================
CREATE TABLE Membres (
    id_membre INT AUTO_INCREMENT PRIMARY KEY,
    code_membre VARCHAR(20) NOT NULL UNIQUE,
    nom VARCHAR(100) NOT NULL,
    prenoms VARCHAR(150) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    email VARCHAR(150),
    adresse TEXT,
    ville VARCHAR(100),
    photo_path VARCHAR(500),
    date_naissance DATE,
    profession VARCHAR(100),
    statut ENUM('ACTIF', 'INACTIF', 'SUSPENDU') DEFAULT 'ACTIF',
    solde_compte DECIMAL(15,2) DEFAULT 0.00,
    date_adhesion DATE NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_modification TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_code_membre (code_membre),
    INDEX idx_nom (nom),
    INDEX idx_statut (statut),
    INDEX idx_telephone (telephone)
) ENGINE=InnoDB;

-- =====================================================
-- TABLE: Cycles (Périodes de tontine)
-- =====================================================
CREATE TABLE Cycles (
    id_cycle INT AUTO_INCREMENT PRIMARY KEY,
    nom_cycle VARCHAR(150) NOT NULL,
    description TEXT,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    montant_cotisation DECIMAL(15,2) NOT NULL,
    frequence ENUM('HEBDOMADAIRE', 'BIMENSUEL', 'MENSUEL', 'TRIMESTRIEL') DEFAULT 'MENSUEL',
    statut ENUM('PLANIFIE', 'EN_COURS', 'TERMINE', 'ANNULE') DEFAULT 'PLANIFIE',
    nombre_membres INT DEFAULT 0,
    montant_total_collecte DECIMAL(15,2) DEFAULT 0.00,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CHECK (date_fin > date_debut),
    INDEX idx_statut (statut),
    INDEX idx_dates (date_debut, date_fin)
) ENGINE=InnoDB;

-- =====================================================
-- TABLE: Participations (Membres dans un cycle)
-- =====================================================
CREATE TABLE Participations (
    id_participation INT AUTO_INCREMENT PRIMARY KEY,
    id_cycle INT NOT NULL,
    id_membre INT NOT NULL,
    date_inscription TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut ENUM('ACTIF', 'RETIRE', 'SUSPENDU') DEFAULT 'ACTIF',
    FOREIGN KEY (id_cycle) REFERENCES Cycles(id_cycle) ON DELETE CASCADE,
    FOREIGN KEY (id_membre) REFERENCES Membres(id_membre) ON DELETE CASCADE,
    UNIQUE KEY unique_participation (id_cycle, id_membre),
    INDEX idx_cycle (id_cycle),
    INDEX idx_membre (id_membre)
) ENGINE=InnoDB;

-- =====================================================
-- TABLE: Transactions (Dépôts et Retraits)
-- =====================================================
CREATE TABLE Transactions (
    id_transaction INT AUTO_INCREMENT PRIMARY KEY,
    id_membre INT NOT NULL,
    id_cycle INT,
    type_transaction ENUM('DEPOT', 'RETRAIT', 'COTISATION', 'PENALITE', 'BONUS') NOT NULL,
    montant DECIMAL(15,2) NOT NULL,
    description TEXT,
    reference VARCHAR(100) UNIQUE,
    mode_paiement ENUM('ESPECES', 'MOBILE_MONEY', 'VIREMENT', 'CHEQUE') DEFAULT 'ESPECES',
    statut ENUM('EN_ATTENTE', 'VALIDE', 'ANNULE') DEFAULT 'VALIDE',
    id_utilisateur INT,
    date_transaction TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CHECK (montant > 0),
    FOREIGN KEY (id_membre) REFERENCES Membres(id_membre) ON DELETE RESTRICT,
    FOREIGN KEY (id_cycle) REFERENCES Cycles(id_cycle) ON DELETE SET NULL,
    FOREIGN KEY (id_utilisateur) REFERENCES Utilisateurs(id_utilisateur) ON DELETE SET NULL,
    INDEX idx_membre (id_membre),
    INDEX idx_cycle (id_cycle),
    INDEX idx_type (type_transaction),
    INDEX idx_date (date_transaction),
    INDEX idx_reference (reference)
) ENGINE=InnoDB;

-- =====================================================
-- TABLE: Configuration (Paramètres système)
-- =====================================================
CREATE TABLE Configuration (
    id_config INT AUTO_INCREMENT PRIMARY KEY,
    cle_config VARCHAR(100) NOT NULL UNIQUE,
    valeur_config TEXT,
    description TEXT,
    type_donnee ENUM('STRING', 'NUMBER', 'BOOLEAN', 'JSON') DEFAULT 'STRING',
    date_modification TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_cle (cle_config)
) ENGINE=InnoDB;

-- =====================================================
-- TABLE: Mouchard (Audit Trail / Logs)
-- =====================================================
CREATE TABLE Mouchard (
    id_log INT AUTO_INCREMENT PRIMARY KEY,
    action VARCHAR(100) NOT NULL,
    table_concernee VARCHAR(100),
    id_enregistrement INT,
    details TEXT,
    id_utilisateur INT,
    adresse_ip VARCHAR(45),
    date_action TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_utilisateur) REFERENCES Utilisateurs(id_utilisateur) ON DELETE SET NULL,
    INDEX idx_action (action),
    INDEX idx_table (table_concernee),
    INDEX idx_date (date_action),
    INDEX idx_utilisateur (id_utilisateur)
) ENGINE=InnoDB;

-- =====================================================
-- TRIGGERS: Audit automatique
-- =====================================================

DELIMITER //

-- Trigger: Après insertion d'un membre
CREATE TRIGGER after_membre_insert
AFTER INSERT ON Membres
FOR EACH ROW
BEGIN
    INSERT INTO Mouchard (action, table_concernee, id_enregistrement, details)
    VALUES ('INSERT', 'Membres', NEW.id_membre, 
            CONCAT('Nouveau membre: ', NEW.nom, ' ', NEW.prenoms, ' (', NEW.code_membre, ')'));
END//

-- Trigger: Après modification d'un membre
CREATE TRIGGER after_membre_update
AFTER UPDATE ON Membres
FOR EACH ROW
BEGIN
    INSERT INTO Mouchard (action, table_concernee, id_enregistrement, details)
    VALUES ('UPDATE', 'Membres', NEW.id_membre,
            CONCAT('Modification membre: ', NEW.code_membre));
END//

-- Trigger: Après suppression d'un membre
CREATE TRIGGER after_membre_delete
AFTER DELETE ON Membres
FOR EACH ROW
BEGIN
    INSERT INTO Mouchard (action, table_concernee, id_enregistrement, details)
    VALUES ('DELETE', 'Membres', OLD.id_membre,
            CONCAT('Suppression membre: ', OLD.nom, ' ', OLD.prenoms));
END//

-- Trigger: Après insertion d'une transaction
CREATE TRIGGER after_transaction_insert
AFTER INSERT ON Transactions
FOR EACH ROW
BEGIN
    -- Log de la transaction
    INSERT INTO Mouchard (action, table_concernee, id_enregistrement, details, id_utilisateur)
    VALUES ('INSERT', 'Transactions', NEW.id_transaction,
            CONCAT('Transaction ', NEW.type_transaction, ': ', NEW.montant, ' FCFA'),
            NEW.id_utilisateur);
    
    -- Mise à jour du solde du membre
    IF NEW.type_transaction IN ('DEPOT', 'COTISATION', 'BONUS') THEN
        UPDATE Membres 
        SET solde_compte = solde_compte + NEW.montant
        WHERE id_membre = NEW.id_membre;
    ELSEIF NEW.type_transaction IN ('RETRAIT', 'PENALITE') THEN
        UPDATE Membres 
        SET solde_compte = solde_compte - NEW.montant
        WHERE id_membre = NEW.id_membre;
    END IF;
    
    -- Mise à jour du montant total du cycle
    IF NEW.id_cycle IS NOT NULL AND NEW.type_transaction = 'COTISATION' THEN
        UPDATE Cycles
        SET montant_total_collecte = montant_total_collecte + NEW.montant
        WHERE id_cycle = NEW.id_cycle;
    END IF;
END//

-- Trigger: Après insertion d'une participation
CREATE TRIGGER after_participation_insert
AFTER INSERT ON Participations
FOR EACH ROW
BEGIN
    -- Incrémenter le nombre de membres dans le cycle
    UPDATE Cycles
    SET nombre_membres = nombre_membres + 1
    WHERE id_cycle = NEW.id_cycle;
    
    -- Log
    INSERT INTO Mouchard (action, table_concernee, id_enregistrement, details)
    VALUES ('INSERT', 'Participations', NEW.id_participation,
            CONCAT('Membre ajouté au cycle ID: ', NEW.id_cycle));
END//

-- Trigger: Après suppression d'une participation
CREATE TRIGGER after_participation_delete
AFTER DELETE ON Participations
FOR EACH ROW
BEGIN
    -- Décrémenter le nombre de membres dans le cycle
    UPDATE Cycles
    SET nombre_membres = nombre_membres - 1
    WHERE id_cycle = OLD.id_cycle;
    
    -- Log
    INSERT INTO Mouchard (action, table_concernee, id_enregistrement, details)
    VALUES ('DELETE', 'Participations', OLD.id_participation,
            CONCAT('Membre retiré du cycle ID: ', OLD.id_cycle));
END//

DELIMITER ;

-- =====================================================
-- DONNÉES INITIALES
-- =====================================================

-- Utilisateur administrateur par défaut
INSERT INTO Utilisateurs (nom_utilisateur, mot_de_passe, nom_complet, email, role)
VALUES ('admin', MD5('admin123'), 'Administrateur Système', 'admin@tontinepro.com', 'ADMIN');

-- Configuration par défaut
INSERT INTO Configuration (cle_config, valeur_config, description, type_donnee) VALUES
('APP_NAME', 'TontinePro', 'Nom de l\'application', 'STRING'),
('APP_VERSION', '1.0.0', 'Version de l\'application', 'STRING'),
('DEVISE', 'FCFA', 'Devise utilisée', 'STRING'),
('PENALITE_RETARD', '500', 'Montant de la pénalité de retard', 'NUMBER'),
('TAUX_INTERET', '2.5', 'Taux d\'intérêt annuel (%)', 'NUMBER'),
('EMAIL_NOTIFICATIONS', 'true', 'Activer les notifications par email', 'BOOLEAN'),
('SMS_NOTIFICATIONS', 'false', 'Activer les notifications par SMS', 'BOOLEAN');

-- Membres de démonstration
INSERT INTO Membres (code_membre, nom, prenoms, telephone, adresse, ville, date_adhesion, solde_compte) VALUES
('M001', 'KONE', 'Moussa', '0102030405', 'Cocody Angré', 'Abidjan', '2026-01-15', 150000.00),
('M002', 'TOURE', 'Aicha', '0506070809', 'Deux Plateaux', 'Abidjan', '2026-01-20', 200000.00),
('M003', 'KOUASSI', 'Jean-Baptiste', '0910111213', 'Marcory Zone 4', 'Abidjan', '2026-02-01', 75000.00),
('M004', 'DIALLO', 'Fatoumata', '0203040506', 'Yopougon', 'Abidjan', '2026-02-05', 120000.00),
('M005', 'BAMBA', 'Seydou', '0607080910', 'Abobo', 'Abidjan', '2026-02-10', 95000.00);

-- Cycle de démonstration
INSERT INTO Cycles (nom_cycle, description, date_debut, date_fin, montant_cotisation, frequence, statut) VALUES
('Cycle Janvier-Juin 2026', 'Premier cycle de l\'année 2026', '2026-01-01', '2026-06-30', 25000.00, 'MENSUEL', 'EN_COURS'),
('Cycle Juillet-Décembre 2026', 'Second cycle de l\'année 2026', '2026-07-01', '2026-12-31', 30000.00, 'MENSUEL', 'PLANIFIE');

-- Participations
INSERT INTO Participations (id_cycle, id_membre) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5);

-- Transactions de démonstration
INSERT INTO Transactions (id_membre, id_cycle, type_transaction, montant, description, reference, mode_paiement) VALUES
(1, 1, 'COTISATION', 25000.00, 'Cotisation Janvier 2026', 'TXN-2026-001', 'MOBILE_MONEY'),
(2, 1, 'COTISATION', 25000.00, 'Cotisation Janvier 2026', 'TXN-2026-002', 'ESPECES'),
(3, 1, 'COTISATION', 25000.00, 'Cotisation Janvier 2026', 'TXN-2026-003', 'VIREMENT'),
(1, 1, 'DEPOT', 50000.00, 'Dépôt supplémentaire', 'TXN-2026-004', 'ESPECES'),
(4, 1, 'COTISATION', 25000.00, 'Cotisation Janvier 2026', 'TXN-2026-005', 'MOBILE_MONEY');

-- =====================================================
-- VUES UTILES
-- =====================================================

-- Vue: Résumé des membres
CREATE VIEW vue_resume_membres AS
SELECT 
    m.id_membre,
    m.code_membre,
    CONCAT(m.nom, ' ', m.prenoms) AS nom_complet,
    m.telephone,
    m.ville,
    m.statut,
    m.solde_compte,
    COUNT(DISTINCT p.id_cycle) AS nombre_cycles,
    COUNT(DISTINCT t.id_transaction) AS nombre_transactions,
    COALESCE(SUM(CASE WHEN t.type_transaction IN ('DEPOT', 'COTISATION', 'BONUS') THEN t.montant ELSE 0 END), 0) AS total_depots,
    COALESCE(SUM(CASE WHEN t.type_transaction IN ('RETRAIT', 'PENALITE') THEN t.montant ELSE 0 END), 0) AS total_retraits
FROM Membres m
LEFT JOIN Participations p ON m.id_membre = p.id_membre
LEFT JOIN Transactions t ON m.id_membre = t.id_membre
GROUP BY m.id_membre;

-- Vue: Statistiques des cycles
CREATE VIEW vue_stats_cycles AS
SELECT 
    c.id_cycle,
    c.nom_cycle,
    c.statut,
    c.date_debut,
    c.date_fin,
    c.montant_cotisation,
    c.nombre_membres,
    c.montant_total_collecte,
    c.montant_cotisation * c.nombre_membres AS montant_attendu,
    ROUND((c.montant_total_collecte / NULLIF(c.montant_cotisation * c.nombre_membres, 0)) * 100, 2) AS taux_collecte
FROM Cycles c;

-- =====================================================
-- PROCÉDURES STOCKÉES
-- =====================================================

DELIMITER //

-- Procédure: Créer un nouveau membre
CREATE PROCEDURE sp_creer_membre(
    IN p_code VARCHAR(20),
    IN p_nom VARCHAR(100),
    IN p_prenoms VARCHAR(150),
    IN p_telephone VARCHAR(20),
    IN p_adresse TEXT,
    IN p_ville VARCHAR(100),
    IN p_photo VARCHAR(500)
)
BEGIN
    INSERT INTO Membres (code_membre, nom, prenoms, telephone, adresse, ville, photo_path, date_adhesion)
    VALUES (p_code, p_nom, p_prenoms, p_telephone, p_adresse, p_ville, p_photo, CURDATE());
    
    SELECT LAST_INSERT_ID() AS id_membre;
END//

-- Procédure: Enregistrer une transaction
CREATE PROCEDURE sp_enregistrer_transaction(
    IN p_id_membre INT,
    IN p_id_cycle INT,
    IN p_type VARCHAR(20),
    IN p_montant DECIMAL(15,2),
    IN p_description TEXT,
    IN p_mode_paiement VARCHAR(20),
    IN p_id_utilisateur INT
)
BEGIN
    DECLARE v_reference VARCHAR(100);
    
    -- Générer une référence unique
    SET v_reference = CONCAT('TXN-', YEAR(NOW()), '-', LPAD(FLOOR(RAND() * 999999), 6, '0'));
    
    INSERT INTO Transactions (id_membre, id_cycle, type_transaction, montant, description, reference, mode_paiement, id_utilisateur)
    VALUES (p_id_membre, p_id_cycle, p_type, p_montant, p_description, v_reference, p_mode_paiement, p_id_utilisateur);
    
    SELECT LAST_INSERT_ID() AS id_transaction, v_reference AS reference;
END//

-- Procédure: Obtenir le solde d'un membre
CREATE PROCEDURE sp_obtenir_solde_membre(IN p_id_membre INT)
BEGIN
    SELECT 
        m.code_membre,
        CONCAT(m.nom, ' ', m.prenoms) AS nom_complet,
        m.solde_compte,
        COUNT(t.id_transaction) AS nombre_transactions
    FROM Membres m
    LEFT JOIN Transactions t ON m.id_membre = t.id_membre
    WHERE m.id_membre = p_id_membre
    GROUP BY m.id_membre;
END//

DELIMITER ;

-- =====================================================
-- INDEX SUPPLÉMENTAIRES POUR PERFORMANCE
-- =====================================================

CREATE INDEX idx_membres_ville ON Membres(ville);
CREATE INDEX idx_transactions_montant ON Transactions(montant);
CREATE INDEX idx_cycles_frequence ON Cycles(frequence);

-- =====================================================
-- FIN DU SCRIPT
-- =====================================================

-- Afficher un résumé
SELECT 'Base de données TontinePro créée avec succès!' AS Message;
SELECT COUNT(*) AS Nombre_Membres FROM Membres;
SELECT COUNT(*) AS Nombre_Cycles FROM Cycles;
SELECT COUNT(*) AS Nombre_Transactions FROM Transactions;
