-- Ajout des Triggers pour l'audit automatique (Mouchard)

-- 1. Trigger sur AJOUT Adhérent
DELIMITER //
CREATE TRIGGER after_adherent_insert
AFTER INSERT ON Adherent
FOR EACH ROW
BEGIN
    INSERT INTO Mouchard (action, table_concernee, details, date_action)
    VALUES ('AJOUT', 'Adherent', CONCAT('Nouvel adhérent : ', NEW.nom, ' ', NEW.prenoms, ' (ID: ', NEW.id_adherent, ')'), NOW());
END;
//
DELIMITER ;

-- 2. Trigger sur MODIFICATION Adhérent
DELIMITER //
CREATE TRIGGER after_adherent_update
AFTER UPDATE ON Adherent
FOR EACH ROW
BEGIN
    INSERT INTO Mouchard (action, table_concernee, details, date_action)
    VALUES ('MODIFICATION', 'Adherent', CONCAT('Adhérent modifié ID: ', OLD.id_adherent, '. Ancien Tel: ', OLD.telephone, ' -> Nouveau Tel: ', NEW.telephone), NOW());
END;
//
DELIMITER ;

-- 3. Trigger sur SUPPRESSION Adhérent
DELIMITER //
CREATE TRIGGER after_adherent_delete
AFTER DELETE ON Adherent
FOR EACH ROW
BEGIN
    INSERT INTO Mouchard (action, table_concernee, details, date_action)
    VALUES ('SUPPRESSION', 'Adherent', CONCAT('Adhérent supprimé : ', OLD.nom, ' ', OLD.prenoms, ' (ID: ', OLD.id_adherent, ')'), NOW());
END;
//
DELIMITER ;

-- 4. Trigger sur NOUVELLE TRANSACTION (Opération)
DELIMITER //
CREATE TRIGGER after_operation_insert
AFTER INSERT ON Operation
FOR EACH ROW
BEGIN
    INSERT INTO Mouchard (action, table_concernee, details, date_action)
    VALUES ('TRANSACTION', 'Operation', CONCAT('Nouvelle opération : ', NEW.type_operation, ' de ', NEW.montant, ' sur le compte ID: ', NEW.id_compte), NOW());
END;
//
DELIMITER ;
