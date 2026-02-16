# CAHIER DES CHARGES - PROJET TontinePro (LimtaScore Edition)

**Version :** 1.0  
**Date :** 16 Février 2026  
**Équipe :** Groupe 1  

---

## 1. PRÉSENTATION DU PROJET

### 1.1 Contexte
Le projet **TontinePro** est une application de gestion automatisée pour les associations de tontine. Elle vise à remplacer les cahiers manuels par une solution numérique fiable, sécurisée et moderne.

Cette version spécifique, **"LimtaScore Edition"**, met l'accent sur une expérience utilisateur (UX) haut de gamme, inspirée des standards du web moderne.

### 1.2 Objectifs
- Simplifier l'enregistrement des membres et des transactions.
- Offrir une visibilité immédiate sur la santé financière de la tontine.
- Sécuriser les données via une base de données relationnelle.
- Proposer une interface intuitive et esthétique.

---

## 2. SPÉCIFICATIONS FONCTIONNELLES

### 2.1 Authentification et Sécurité
- **Login Sécurisé** : Interface plein écran avec séparation visuelle (Art/Formulaire).
- **Rôles Utilisateurs** :
    - *Administrateur* : Accès total (Configuration, Utilisateurs, Mouchard).
    - *Gérant* : Accès limité (Membres, Collectes). (À implémenter complètement via la BDD).
- **Audit (Mouchard)** : Traçabilité des actions sensibles via des Triggers SQL (UPDATE/DELETE sur les transactions).

### 2.2 Gestion des Membres
- **CRUD Complet** : Ajouter, Modifier, Supprimer un membre.
- **Consultation** : Liste tabulaire avec recherche et tri.
- **Exportation** : Export des données membres au format CSV/Excel en un clic.

### 2.3 Gestion des Collectes (Transactions)
- **Opérations** : Enregistrement des Dépôts et Retraits.
- **Suivi** : Historique des transactions par membre.
- **Validation** : Contrôle des saisies via des formulaires modaux.

### 2.4 Tableau de Bord (Dashboard)
- **Indicateurs Clés (KPI)** :
    - Nombre total de membres.
    - Montant total collecté (Encaisse).
    - Taux de participation.
- **Visualisation** : Graphiques d'évolution (Courbe des dépôts sur la période).

### 2.5 Communication et Support
- **Support Chat (Chatbox)** :
    - Widget flottant en bas de page.
    - Interface de conversation instantanée avec auto-réponse simulée.
    - Historique des messages "User" (Bleu) et "Agent" (Gris).
- **Notifications Toast** :
    - Alertes non-bloquantes (Succès/Erreur/Info) apparaissant en bas à droite.
    - Remplacement des fenêtres `JOptionPane` intrusives.

### 2.6 Configuration
- **Gestion des Cycles** : Définition des périodes de tontine (Actif/Clôturé).
- **Paramètres Généraux** : Informations de l'association.

---

## 3. SPÉCIFICATIONS TECHNIQUES

### 3.1 Stack Technologique
- **Langage** : Java (JDK 17+ recommandé).
- **Interface Graphique (GUI)** : Java Swing.
    - Utilisation avancée de `JLayeredPane` pour les widgets flottants.
    - Personnalisation via `Graphics2D` (Boutons arrondis, Ombres, Gradients).
    - `UIManager` global pour le style (Scrollbars, Polices).
- **Base de Données** : MySQL.
- **Connectivité** : JDBC (Java Database Connectivity).

### 3.2 Architecture
- **Modèle MVC (Adapté)** : Séparation logique entre les Vues (`Panel`), les Modèles de données (`TableModel`) et la logique métier.
- **Helpers** :
    - `StyleUtils.java` : Centralisation du Design System (Couleurs, Polices, Composants).
    - `DialogUtils.java` : Fabrique de fenêtres modales standardisées.
    - `DataExporter.java` : Gestionnaire d'export CSV.
    - `Toast.java` : Gestionnaire de notifications.

### 3.3 Base de Données (Schéma Simplifié)
- **Tables** : `membre`, `utilisateur`, `compte` (transactions), `cycle`.
- **Mécanismes** : Clés étrangères pour l'intégrité référentielle.
- **Triggers** : `after_update_compte` pour peupler la table `mouchard`.

---

## 4. CHARTE GRAPHIQUE & UI/UX

### 4.1 Identité Visuelle "LimtaScore"
- **Couleurs Principales** :
    - *Bleu Nuit (Navy)* : `#1C2D5A` (Sidebar, Header Chat).
    - *Orange Brûlé* : `#C8500A` (Boutons d'action, Accents).
    - *Blanc/Gris Clair* : `#FFFFFF` / `#DCDCDC` (Fonds, Contenu).
- **Typographie** : `Segoe UI` (Moderne, Sans-serif).

### 4.2 Composants "Web-Style"
- **Boutons** : Plats (Flat), Coins arrondis, Effet Hover.
- **Formulaires** : Labels au-dessus des champs, Espacement aéré (Padding généreux).
- **Tableaux** : Lignes hautes (45px), En-têtes minimalistes, Pas de grille verticale.
- **Scrollbars** : Fines, arrondies, sans boutons fléchés (Style Chrome/MacOS).

---

## 5. ÉVOLUTIONS FUTURES POSSIBLES
- Intégration réelle d'une API de Chat (ex: WebSocket).
- Génération de reçus PDF pour les transactions.
- Version Mobile (Android) connectée à la même base de données.
- Authentification biométrique.

---
*Document généré automatiquement par l'assistant IA TontinePro.*
