# Documentation de la Base de Données - TontinePro

Cette documentation détaille la structure, les règles métier et les mécanismes automatiques de la base de données MySQL utilisée par TontinePro.

## 1. Vue d'ensemble
La base de données `tontinepro` est conçue pour assurer une intégrité transactionnelle forte et automatiser le suivi financier via des triggers et procédures stockées.

### Schéma Relationnel (Résumé)
- **Utilisateurs** : Gère l'accès au système (RBAC).
- **Membres** : Répertoire central des adhérents et leurs soldes.
- **Cycles** : Périodes de tontine avec paramétrage financier.
- **Participations** : Liaison n-n entre Membres et Cycles.
- **Transactions** : Historique exhaustif des flux financiers.
- **Mouchard** : Journal d'audit pour la traçabilité.

---

## 2. Dictionnaire des Tables

### Table: `Utilisateurs`
| Colonne | Type | Description |
|---------|------|-------------|
| id_utilisateur | INT (PK) | Identifiant unique |
| nom_utilisateur | VARCHAR | Login (Unique) |
| mot_de_passe | VARCHAR | Hash MD5 |
| role | ENUM | ADMIN, GESTIONNAIRE, OPERATEUR |
| statut | ENUM | ACTIF, INACTIF, SUSPENDU |

### Table: `Membres`
| Colonne | Type | Description |
|---------|------|-------------|
| id_membre | INT (PK) | Identifiant unique |
| code_membre | VARCHAR | Identifiant métier unique (ex: M001) |
| solde_compte | DECIMAL | Solde actuel (Mis à jour par triggers) |
| statut | ENUM | ACTIF, INACTIF, SUSPENDU |

### Table: `Transactions`
| Colonne | Type | Description |
|---------|------|-------------|
| id_transaction | INT (PK) | Identifiant unique |
| type_transaction| ENUM | DEPOT, RETRAIT, COTISATION, PENALITE, BONUS |
| montant | DECIMAL | Valeur de l'opération |
| reference | VARCHAR | Code unique généré par procédure |

---

## 3. Automatisation (Triggers)

La base de données auto-gère les soldes et les statistiques pour garantir la cohérence des données, même en cas de modification manuelle en SQL.

- **`after_transaction_insert`** : 
    - Met à jour `Membres.solde_compte` (Ajoute si DEPOT/COTISATION, soustrait si RETRAIT).
    - Met à jour `Cycles.montant_total_collecte` si type = COTISATION.
    - Enregistre une entrée dans le `Mouchard`.
- **`after_membre_insert/update/delete`** : Journalise chaque action dans le `Mouchard` pour audit.
- **`after_participation_insert/delete`** : Met à jour automatiquement le compteur `Cycles.nombre_membres`.

---

## 4. Procédures Stockées

### `sp_enregistrer_transaction`
- **Rôle** : Centralise la création de transactions.
- **Actions** : Génère une référence unique (TXN-YYYY-XXXXXX) et insère l'enregistrement.
- **Bénéfice** : Garantit que chaque transaction possède une référence unique et valide au moment de l'écriture.

### `sp_creer_membre`
- **Rôle** : Simplifie l'inscription d'un membre avec initialisation de la date d'adhésion.

---

## 5. Vues de Reporting

- **`vue_resume_membres`** : Fournit une synthèse par membre (totals dépôts, retraits, nombre de transactions).
- **`vue_stats_cycles`** : Calcule le taux de collecte d'un cycle (Montant collecté vs Montant attendu).

---

## 6. Sécurité et Audit
Toutes les suppressions critiques (`Membres`, `Transactions`) sont loggées avec le détail de l'ancien enregistrement dans la table `Mouchard`. Les mots de passe sont hashés en MD5 nativement via MySQL.
