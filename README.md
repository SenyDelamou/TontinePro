# TontinePro - Application de Gestion de Tontine

![Version](https://img.shields.io/badge/version-1.0-blue)
![Java](https://img.shields.io/badge/Java-8%2B-orange)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![License](https://img.shields.io/badge/license-MIT-green)

Application desktop professionnelle de gestion de tontine développée en Java Swing avec base de données MySQL.

---

## 📋 Table des Matières

- [Aperçu](#aperçu)
- [Fonctionnalités](#fonctionnalités)
- [Technologies](#technologies)
- [Installation](#installation)
- [Utilisation](#utilisation)
- [Structure du Projet](#structure-du-projet)
- [Base de Données](#base-de-données)
- [Captures d'Écran](#captures-décran)
- [Documentation](#documentation)
- [Contributeurs](#contributeurs)

---

## 🎯 Aperçu

TontinePro est une solution complète pour la gestion digitale des tontines (systèmes d'épargne collective). L'application offre une interface moderne inspirée du design web, avec des fonctionnalités avancées de gestion, reporting et génération de documents.

### Caractéristiques Principales
- ✅ Interface moderne "LimtaScore Edition" avec design premium
- ✅ Gestion complète des membres avec upload de photos
- ✅ Système de cycles et cotisations
- ✅ Suivi des transactions (dépôts, retraits, cotisations)
- ✅ Tableau de bord avec graphiques interactifs
- ✅ Génération de cartes de membres professionnelles
- ✅ Export de données en CSV
- ✅ Audit trail complet (Mouchard)
- ✅ Notifications Toast élégantes

---

## 🚀 Fonctionnalités

### Gestion des Membres
- CRUD complet (Créer, Lire, Modifier, Supprimer)
- Upload de photos pour cartes ID
- Suivi automatique des soldes
- Statuts : Actif, Inactif, Suspendu

### Gestion des Cycles
- Création de cycles avec dates et montants
- Fréquences : Hebdomadaire, Mensuel, Trimestriel
- Inscription/retrait de membres
- Calcul automatique des montants collectés

### Transactions
- Types : Dépôt, Retrait, Cotisation, Pénalité, Bonus
- Modes de paiement : Espèces, Mobile Money, Virement, Chèque
- Génération automatique de références uniques
- Mise à jour automatique des soldes

### Tableau de Bord
- 3 cartes KPI (Membres, Montant collecté, Cycles actifs)
- Graphique d'évolution (6 derniers mois)
- Graphique de répartition (Donut chart)
- Activités récentes

### Carte de Membre Premium
- Design moderne avec gradient navy
- Photo circulaire avec bordure dorée
- QR code stylisé
- Format 550x340px (style carte bancaire)
- Export en PNG haute qualité

---

## 🛠️ Technologies

### Backend
- **Java** 8+
- **Swing** (Interface graphique)
- **JDBC** (Connexion base de données)

### Base de Données
- **MySQL** 8.0
- 7 tables principales
- 8 triggers automatiques
- 2 vues pour reporting
- 3 procédures stockées

### Design
- Palette harmonisée (Navy/Orange/Gold)
- Typographie Segoe UI
- Scrollbars personnalisées
- Composants modernes arrondis

---

## 📦 Installation

### Prérequis
- Java JDK 8 ou supérieur
- MySQL 8.0 ou supérieur
- 2 Go RAM minimum
- 500 Mo espace disque

### Étapes

#### 1. Cloner le projet
```bash
git clone https://github.com/votre-repo/tontinepro.git
cd tontinepro
```

#### 2. Créer la base de données
```bash
mysql -u root -p < tontinepro_database.sql
```

#### 3. Configurer la connexion (optionnel)
Modifier les paramètres dans le code si nécessaire :
```java
String url = "jdbc:mysql://localhost:3306/tontinepro";
String user = "root";
String password = "votre_mot_de_passe";
```

#### 4. Compiler
```bash
javac -d . src/groupe1/*.java
```

#### 5. Exécuter
```bash
java groupe1.Groupe1
```

### Connexion par Défaut
- **Utilisateur** : `admin`
- **Mot de passe** : `admin123`

---

## 💻 Utilisation

### Démarrage
1. Lancer l'application
2. Se connecter avec les identifiants admin
3. Accéder au tableau de bord

### Ajouter un Membre
1. Aller dans **"Membres"**
2. Cliquer **"Ajouter"**
3. Remplir le formulaire
4. (Optionnel) Cliquer **"..."** pour ajouter une photo
5. Cliquer **"Enregistrer"**

### Générer une Carte ID
1. Dans **"Membres"**, sélectionner un membre
2. Cliquer **"Carte ID"**
3. Prévisualiser la carte
4. Cliquer **"💾 Télécharger PNG"**

### Enregistrer une Transaction
1. Aller dans **"Collecte"**
2. Sélectionner un membre
3. Choisir le type et le montant
4. Valider

### Exporter des Données
1. Dans n'importe quel tableau
2. Cliquer **"Exporter"**
3. Choisir l'emplacement du fichier CSV

---

## 📁 Structure du Projet

```
Groupe1/
├── src/
│   └── groupe1/
│       ├── Groupe1.java              # Point d'entrée
│       ├── LoginFrame.java           # Écran de connexion
│       ├── MainDashboard.java        # Interface principale
│       ├── DashboardPanel.java       # Tableau de bord
│       ├── MembersPanel.java         # Gestion membres
│       ├── CollectionPanel.java      # Gestion transactions
│       ├── ComptePanel.java          # Historique compte
│       ├── UtilisateurPanel.java     # Gestion utilisateurs
│       ├── ConfigurationPanel.java   # Paramètres
│       ├── ProfilePanel.java         # Profil utilisateur
│       ├── CardGenerator.java        # Générateur de cartes
│       ├── StyleUtils.java           # Système de styles
│       ├── Toast.java                # Notifications
│       ├── DataExporter.java         # Export CSV
│       ├── DialogUtils.java          # Utilitaires dialogs
│       ├── EvolutionChartPanel.java  # Graphique évolution
│       └── PieChartPanel.java        # Graphique donut
├── logo/
│   └── logo (2).png                  # Logo application
├── tontinepro_database.sql           # Script base de données
├── CAHIER_DES_CHARGES.md             # Spécifications
├── README.md                         # Ce fichier
└── manifest.mf                       # Manifest JAR
```

---

## 🗄️ Base de Données

### Tables Principales
- **Utilisateurs** : Comptes système
- **Membres** : Participants tontine
- **Cycles** : Périodes de cotisation
- **Participations** : Liaison membres-cycles
- **Transactions** : Opérations financières
- **Configuration** : Paramètres système
- **Mouchard** : Journal d'audit

### Triggers Automatiques
- Audit de toutes les opérations
- Calcul automatique des soldes
- Mise à jour des montants de cycles
- Comptage des participants

### Vues
- `vue_resume_membres` : Statistiques membres
- `vue_stats_cycles` : Statistiques cycles

### Procédures Stockées
- `sp_creer_membre` : Création membre
- `sp_enregistrer_transaction` : Enregistrement transaction
- `sp_obtenir_solde_membre` : Consultation solde

---

## 📸 Captures d'Écran

### Tableau de Bord
![Dashboard](screenshots/dashboard.png)
*Cartes KPI, graphiques d'évolution et de répartition*

### Gestion des Membres
![Membres](screenshots/membres.png)
*Liste des membres avec actions CRUD*

### Carte de Membre Premium
![Carte](screenshots/carte_membre.png)
*Carte ID professionnelle avec design moderne*

---

## 📚 Documentation

### Documents Disponibles
- **[Cahier des Charges](CAHIER_DES_CHARGES.md)** : Spécifications complètes
- **[Documentation Base de Données](docs/database_documentation.md)** : Structure et utilisation
- **[Diagrammes UML](docs/uml_diagrams.md)** : Cas d'utilisation et classes
- **[Guide d'Utilisation](docs/walkthrough.md)** : Instructions détaillées

### Diagrammes UML
- Diagramme de cas d'utilisation
- Diagramme de classes
- Diagramme de séquence (Génération carte)
- Diagramme d'activité (Transaction)

---

## 👥 Contributeurs

**Groupe 1**
- Développement complet de l'application
- Design UI/UX "LimtaScore Edition"
- Architecture base de données
- Documentation technique

---

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

---

## 🎯 Roadmap Future

### Phase 2
- [ ] Intégration Mobile Money API
- [ ] Notifications SMS/Email automatiques
- [ ] Reporting PDF avancé

### Phase 3
- [ ] Application mobile (Android/iOS)
- [ ] Synchronisation cloud
- [ ] Multi-devises

### Phase 4
- [ ] Tableau de bord analytique (BI)
- [ ] Prédictions ML
- [ ] API REST

---

## 🆘 Support

Pour toute question ou problème :
- **Email** : support@tontinepro.com
- **Documentation** : Voir dossier `docs/`
- **Issues** : [GitHub Issues](https://github.com/votre-repo/tontinepro/issues)

---

## ⭐ Remerciements

Merci d'utiliser TontinePro ! N'hésitez pas à ⭐ le projet si vous le trouvez utile.

---

**TontinePro** - *Digitalisez votre tontine avec élégance* 🎴
