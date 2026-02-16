# CAHIER DES CHARGES - TontinePro

**Projet** : Application de Gestion de Tontine  
**Version** : 1.0  
**Date** : 16 Février 2026  
**Équipe** : Groupe 1  

---

## 1. PRÉSENTATION DU PROJET

### 1.1 Contexte
TontinePro est une application desktop de gestion de tontine développée en Java Swing. Elle permet aux associations et groupes d'épargne de gérer efficacement leurs membres, cycles de cotisation, transactions financières et de générer des documents professionnels.

### 1.2 Objectifs
- Digitaliser la gestion des tontines traditionnelles
- Automatiser les calculs de soldes et statistiques
- Générer des cartes de membres professionnelles
- Assurer la traçabilité complète des opérations
- Fournir une interface moderne et intuitive

### 1.3 Périmètre
- **Inclus** : Gestion membres, cycles, transactions, reporting, export de données, génération de cartes ID
- **Exclu** : Intégration bancaire directe, application mobile, paiements en ligne

---

## 2. SPÉCIFICATIONS FONCTIONNELLES

### 2.1 Gestion des Utilisateurs
**Acteurs** : Administrateur, Gestionnaire, Opérateur

**Fonctionnalités** :
- Authentification par login/mot de passe
- Gestion des profils utilisateurs
- Attribution de rôles et permissions
- Historique des connexions

### 2.2 Gestion des Membres
**Acteurs** : Gestionnaire, Opérateur

**Fonctionnalités** :
- **CRUD complet** : Création, lecture, modification, suppression
- **Informations** : Code unique, nom, prénoms, téléphone, adresse, ville, photo
- **Upload de photo** : Sélection d'image via explorateur de fichiers
- **Statuts** : Actif, Inactif, Suspendu
- **Suivi du solde** : Calcul automatique basé sur les transactions
- **Export CSV** : Téléchargement de la liste des membres
- **Génération de carte ID** : Carte professionnelle avec photo, QR code, design premium

**Règles métier** :
- Code membre unique et obligatoire
- Téléphone obligatoire
- Solde mis à jour automatiquement via triggers

### 2.3 Gestion des Cycles
**Acteurs** : Administrateur, Gestionnaire

**Fonctionnalités** :
- Création de cycles avec dates début/fin
- Définition du montant de cotisation
- Choix de la fréquence (hebdomadaire, mensuel, trimestriel)
- Inscription/retrait de membres
- Suivi du nombre de participants
- Calcul automatique du montant total collecté
- Statuts : Planifié, En cours, Terminé, Annulé

**Règles métier** :
- Date fin > Date début
- Montant cotisation > 0
- Nombre de membres mis à jour automatiquement

### 2.4 Gestion des Transactions
**Acteurs** : Gestionnaire, Opérateur

**Fonctionnalités** :
- Enregistrement de transactions : Dépôt, Retrait, Cotisation, Pénalité, Bonus
- Sélection du mode de paiement : Espèces, Mobile Money, Virement, Chèque
- Génération automatique de référence unique
- Association à un membre et optionnellement à un cycle
- Validation/Annulation de transactions
- Historique complet par membre

**Règles métier** :
- Montant > 0
- Référence unique générée automatiquement
- Mise à jour automatique du solde membre
- Mise à jour automatique du montant total cycle (pour cotisations)

### 2.5 Tableau de Bord
**Acteurs** : Tous

**Fonctionnalités** :
- **Cartes KPI** : Nombre de membres, montant total collecté, cycles actifs
- **Graphique d'évolution** : Courbe des 6 derniers mois (dépôts/retraits)
- **Graphique de répartition** : Donut chart (75% dépôts, 25% retraits)
- **Activités récentes** : Tableau des dernières transactions

### 2.6 Fonctionnalités Transversales
- **Notifications Toast** : Messages de succès/erreur/info non-bloquants
- **Export de données** : CSV pour tous les tableaux
- **Recherche** : Barre de recherche globale (header)
- **Profil utilisateur** : Menu popup avec accès au profil et déconnexion

---

## 3. SPÉCIFICATIONS TECHNIQUES

### 3.1 Architecture
**Type** : Application Desktop (Client Lourd)  
**Pattern** : MVC (Model-View-Controller)

**Composants** :
- **Vue** : Panels Swing (DashboardPanel, MembersPanel, etc.)
- **Contrôleur** : MainDashboard (navigation), Event Listeners
- **Modèle** : Classes métier + Base de données MySQL

### 3.2 Technologies

#### Backend
- **Langage** : Java 8+
- **Framework UI** : Swing
- **Base de données** : MySQL 8.0
- **Connecteur** : JDBC (MySQL Connector/J)

#### Bibliothèques
- `javax.swing.*` : Interface graphique
- `java.awt.*` : Graphiques et layouts
- `javax.imageio.*` : Traitement d'images
- `java.sql.*` : Connexion base de données

### 3.3 Base de Données

#### Tables Principales
1. **Utilisateurs** : Comptes système (admin, gestionnaires, opérateurs)
2. **Membres** : Participants à la tontine
3. **Cycles** : Périodes de cotisation
4. **Participations** : Liaison membres-cycles
5. **Transactions** : Opérations financières
6. **Configuration** : Paramètres système (clé-valeur)
7. **Mouchard** : Journal d'audit (logs)

#### Triggers
- Audit automatique (INSERT/UPDATE/DELETE sur Membres et Transactions)
- Calcul automatique des soldes membres
- Mise à jour du montant total des cycles
- Comptage automatique des participants

#### Vues
- `vue_resume_membres` : Statistiques par membre
- `vue_stats_cycles` : Statistiques par cycle

#### Procédures Stockées
- `sp_creer_membre` : Création simplifiée
- `sp_enregistrer_transaction` : Enregistrement avec génération de référence
- `sp_obtenir_solde_membre` : Consultation solde

### 3.4 Système de Styles

**Fichier** : `StyleUtils.java`

**Palette de Couleurs** :
- **Bleu Nuit** (`#1C2D5A`) : Sidebar, éléments principaux
- **Orange Vibrant** (`#DC500A`) : Boutons d'action, accents
- **Or** (`#F59E0B`) : Accents secondaires
- **Gris Doux** (`#F8F9FA`) : Arrière-plans
- **Vert Émeraude** (`#10B981`) : Succès
- **Rouge** (`#DC2626`) : Erreurs/Suppressions

**Typographie** :
- Police : Segoe UI
- Titre : 26px Bold
- Sous-titre : 16px Regular
- Corps : 14px Regular

**Composants Personnalisés** :
- Boutons arrondis avec effets hover
- Scrollbars fines et arrondies (style web)
- Tableaux aérés (45px de hauteur de ligne)
- Dialogs modernes avec en-têtes colorés

---

## 4. DESIGN UI/UX

### 4.1 Charte Graphique "LimtaScore Edition"

**Principes** :
- Design moderne inspiré des applications web
- Espaces aérés et lisibles
- Animations fluides et subtiles
- Cohérence visuelle totale

**Éléments** :
- **Header** : Fond gris clair, barre de recherche centrée, avatar utilisateur
- **Sidebar** : Bleu nuit, boutons orange avec bordure blanche, indicateur actif (flèche)
- **Footer** : Message de sécurité + numéro de version
- **Cartes** : Fond blanc, bordure gris clair, ombres légères

### 4.2 Carte de Membre Premium

**Design** :
- **Dimensions** : 550x340px (format carte bancaire élargi)
- **Fond** : Gradient bleu nuit (28,45,90) → (45,70,130)
- **Motifs décoratifs** : Cercles subtils en arrière-plan
- **Bande orange** : Accent vertical 8px sur le bord gauche
- **Ligne dorée** : Séparateur horizontal 2px
- **Logo** : "TP" en blanc 32px + "TontinePro" 14px
- **Photo** : Circulaire 100x100px avec bordure dorée 4px
- **Typographie** :
  - Labels : 11-12px gris clair (150 opacity)
  - Valeurs : 14-18px blanc/doré
  - Nom : 18px Bold uppercase
  - ID : 16px Bold doré
- **QR Code** : 60x60px, fond blanc arrondi, motif damier
- **Footer** : "Membre depuis 2026 • TontinePro Premium" (italique, 10px)

### 4.3 Écrans Principaux

#### Login
- Split-screen : Illustration gauche, formulaire droite
- Champs modernes avec bordures arrondies
- Bouton de connexion proéminent

#### Dashboard
- 3 cartes KPI en haut
- 2 graphiques côte à côte (évolution + répartition)
- Tableau des activités récentes en bas

#### Membres
- Titre + barre d'actions (Ajouter, Modifier, Supprimer, Exporter, Carte ID)
- Tableau avec colonnes : ID, Nom, Prénoms, Téléphone, Adresse, Solde
- Colonne Photo cachée (pour génération de carte)

#### Dialogs
- En-tête coloré avec titre
- Formulaire avec labels au-dessus des champs
- Boutons Annuler (gris) et Enregistrer (bleu) en bas

---

## 5. SÉCURITÉ

### 5.1 Authentification
- Mots de passe hashés (MD5)
- Session utilisateur avec timeout
- Historique des connexions

### 5.2 Autorisation
- Rôles : ADMIN, GESTIONNAIRE, OPERATEUR
- Permissions par fonctionnalité
- Contrôle d'accès basé sur les rôles (RBAC)

### 5.3 Audit
- Toutes les opérations loggées dans `Mouchard`
- Traçabilité : qui, quoi, quand
- Conservation des logs (pas de suppression automatique)

### 5.4 Intégrité des Données
- Contraintes de clés étrangères
- Validation des montants (> 0)
- Validation des dates (fin > début)
- Transactions atomiques

---

## 6. PERFORMANCES

### 6.1 Optimisations
- Index sur colonnes fréquemment recherchées
- Vues matérialisées pour statistiques
- Lazy loading des images
- Cache des configurations

### 6.2 Capacité
- **Membres** : Jusqu'à 10 000
- **Transactions** : Jusqu'à 100 000/an
- **Cycles** : Illimité
- **Temps de réponse** : < 2s pour toute opération

---

## 7. LIVRABLES

### 7.1 Code Source
- ✅ Package `groupe1` avec toutes les classes Java
- ✅ Fichiers de ressources (logo, images)
- ✅ Script SQL complet (`tontinepro_database.sql`)

### 7.2 Documentation
- ✅ Cahier des charges (ce document)
- ✅ Documentation base de données (`database_documentation.md`)
- ✅ Guide d'utilisation (`walkthrough.md`)
- ✅ Checklist des tâches (`task.md`)

### 7.3 Base de Données
- ✅ Script de création complet
- ✅ Données de démonstration (5 membres, 2 cycles, 5 transactions)
- ✅ Triggers et procédures stockées
- ✅ Vues pour reporting

### 7.4 Application
- ✅ Fichiers `.class` compilés
- ✅ Fichier JAR exécutable (optionnel)
- ✅ Manifest pour exécution

---

## 8. INSTALLATION ET DÉPLOIEMENT

### 8.1 Prérequis
- Java JDK 8 ou supérieur
- MySQL 8.0 ou supérieur
- 2 Go RAM minimum
- 500 Mo espace disque

### 8.2 Installation Base de Données
```bash
mysql -u root -p < tontinepro_database.sql
```

### 8.3 Configuration Application
Modifier les paramètres de connexion dans le code :
```java
String url = "jdbc:mysql://localhost:3306/tontinepro";
String user = "root";
String password = "votre_mot_de_passe";
```

### 8.4 Compilation
```bash
javac -d . src/groupe1/*.java
```

### 8.5 Exécution
```bash
java groupe1.Groupe1
```

---

## 9. MAINTENANCE ET ÉVOLUTION

### 9.1 Maintenance Corrective
- Correction de bugs signalés
- Mise à jour des dépendances
- Optimisation des requêtes SQL

### 9.2 Évolutions Futures
- **Phase 2** : Intégration Mobile Money API
- **Phase 3** : Application mobile (Android/iOS)
- **Phase 4** : Reporting avancé (PDF, Excel)
- **Phase 5** : Notifications SMS/Email automatiques
- **Phase 6** : Tableau de bord analytique (BI)

---

## 10. GLOSSAIRE

| Terme | Définition |
|-------|------------|
| Tontine | Système d'épargne collective où les membres cotisent régulièrement |
| Cycle | Période définie pendant laquelle les cotisations sont collectées |
| Cotisation | Montant fixe versé par chaque membre à chaque échéance |
| Mouchard | Journal d'audit enregistrant toutes les opérations |
| KPI | Key Performance Indicator (Indicateur clé de performance) |
| CRUD | Create, Read, Update, Delete (Opérations de base) |
| Toast | Notification non-bloquante qui apparaît temporairement |

---

## 11. ANNEXES

### Annexe A : Schéma de Base de Données
Voir fichier `tontinepro_database.sql` pour le schéma complet.

### Annexe B : Captures d'Écran
- Dashboard avec graphiques
- Gestion des membres
- Carte de membre générée
- Dialogs d'ajout/modification

### Annexe C : Exemples de Code
Voir fichiers sources dans `src/groupe1/`

---

**Document rédigé par** : Groupe 1  
**Validé par** : [À compléter]  
**Date de validation** : [À compléter]
