# Guide d'Utilisation - TontinePro

**Version** : 1.0  
**Date** : 16 Février 2026  
**Public** : Administrateurs, Gestionnaires, Opérateurs

---

## 📚 Table des Matières

1. [Démarrage](#1-démarrage)
2. [Connexion](#2-connexion)
3. [Interface Principale](#3-interface-principale)
4. [Gestion des Membres](#4-gestion-des-membres)
5. [Gestion des Cycles](#5-gestion-des-cycles)
6. [Gestion des Transactions](#6-gestion-des-transactions)
7. [Tableau de Bord](#7-tableau-de-bord)
8. [Génération de Cartes ID](#8-génération-de-cartes-id)
9. [Export de Données](#9-export-de-données)
10. [Gestion du Profil](#10-gestion-du-profil)
11. [FAQ](#11-faq)

---

## 1. Démarrage

### Lancement de l'Application

#### Méthode 1 : Ligne de commande
```bash
cd "C:\Users\DataVista\Desktop\COURS DE JAVA\Groupe1"
java groupe1.Groupe1
```

#### Méthode 2 : Double-clic (si JAR créé)
Double-cliquer sur `TontinePro.jar`

### Première Utilisation

**Identifiants par défaut** :
- **Nom d'utilisateur** : `admin`
- **Mot de passe** : `admin123`

> ⚠️ **Important** : Changez le mot de passe administrateur après la première connexion pour des raisons de sécurité.

---

## 2. Connexion

### Étapes de Connexion

1. **Lancer l'application**
2. **Écran de connexion** s'affiche (split-screen avec illustration)
3. **Saisir** votre nom d'utilisateur
4. **Saisir** votre mot de passe
5. **Cliquer** sur le bouton **"Se connecter"**

### En cas d'Erreur

**Message** : "Nom d'utilisateur ou mot de passe incorrect"
- ✅ Vérifier les majuscules/minuscules
- ✅ Vérifier que Caps Lock est désactivé
- ✅ Contacter l'administrateur si le problème persiste

**Message** : "Erreur de connexion à la base de données"
- ✅ Vérifier que MySQL est démarré
- ✅ Vérifier les paramètres de connexion
- ✅ Contacter le support technique

---

## 3. Interface Principale

### Composants de l'Interface

#### Header (En-tête)
- **Barre de recherche** : Recherche globale (centre)
- **Avatar utilisateur** : Menu profil (droite)

#### Sidebar (Menu latéral)
- **Logo TontinePro** : En haut
- **Menu de navigation** :
  - 📊 Tableau de bord
  - 👥 Membres
  - 💰 Collecte
  - 📁 Compte
  - 👤 Utilisateur
  - ⚙️ Configuration

#### Zone de Contenu
- Affiche le panel sélectionné

#### Footer (Pied de page)
- Message de sécurité
- Numéro de version

### Navigation

**Cliquer** sur un élément du menu latéral pour accéder au module correspondant.

L'élément actif est indiqué par :
- Couleur orange vif
- Flèche blanche pointant vers la droite

---

## 4. Gestion des Membres

### 4.1 Consulter la Liste des Membres

1. **Cliquer** sur **"Membres"** dans le menu latéral
2. La liste s'affiche avec les colonnes :
   - ID
   - Nom
   - Prénoms
   - Téléphone
   - Adresse
   - Solde

### 4.2 Ajouter un Nouveau Membre

#### Étapes

1. **Cliquer** sur le bouton **"Ajouter"** (bleu, en haut à droite)
2. **Remplir le formulaire** :
   - **Nom** : Nom de famille (obligatoire)
   - **Prénoms** : Prénoms complets (obligatoire)
   - **Téléphone** : Numéro de téléphone (obligatoire)
   - **Adresse** : Adresse complète
   - **Photo** : Cliquer sur **"..."** pour sélectionner une image
3. **Cliquer** sur **"Enregistrer"**

#### Résultat

- ✅ Notification verte : "Membre ajouté avec succès !"
- ✅ Le membre apparaît dans le tableau
- ✅ Un code unique est généré automatiquement (ex: M001, M002...)

### 4.3 Modifier un Membre

1. **Sélectionner** le membre dans le tableau (cliquer sur la ligne)
2. **Cliquer** sur le bouton **"Modifier"** (orange)
3. **Modifier** les informations souhaitées
4. **Cliquer** sur **"Enregistrer"**

### 4.4 Supprimer un Membre

1. **Sélectionner** le membre dans le tableau
2. **Cliquer** sur le bouton **"Supprimer"** (rouge)
3. **Confirmer** la suppression dans la boîte de dialogue
4. **Cliquer** sur **"Oui"**

> ⚠️ **Attention** : La suppression est définitive et supprime également l'historique des transactions du membre.

### 4.5 Ajouter une Photo

#### Lors de la Création/Modification

1. Dans le formulaire, localiser le champ **"Photo"**
2. **Cliquer** sur le bouton **"..."** à droite
3. **Naviguer** vers l'emplacement de l'image
4. **Sélectionner** l'image (formats acceptés : JPG, PNG)
5. **Cliquer** sur **"Ouvrir"**
6. Le chemin de l'image s'affiche dans le champ
7. **Enregistrer** le membre

#### Formats Recommandés
- **Format** : JPG ou PNG
- **Taille** : 500x500px minimum
- **Poids** : < 2 Mo
- **Fond** : Uni de préférence

---

## 5. Gestion des Cycles

### 5.1 Créer un Nouveau Cycle

1. **Aller** dans **"Configuration"** > **"Cycles"**
2. **Cliquer** sur **"Nouveau Cycle"**
3. **Remplir** :
   - **Nom du cycle** : Ex: "Cycle Janvier-Juin 2026"
   - **Date de début** : Date de démarrage
   - **Date de fin** : Date de clôture
   - **Montant cotisation** : Montant fixe par membre
   - **Fréquence** : Hebdomadaire, Mensuel, Trimestriel
4. **Cliquer** sur **"Créer"**

### 5.2 Inscrire des Membres à un Cycle

1. **Sélectionner** le cycle
2. **Cliquer** sur **"Gérer Participants"**
3. **Cocher** les membres à inscrire
4. **Cliquer** sur **"Valider"**

### 5.3 Consulter les Statistiques d'un Cycle

1. **Sélectionner** le cycle
2. **Cliquer** sur **"Détails"**
3. Visualiser :
   - Nombre de participants
   - Montant total collecté
   - Montant attendu
   - Taux de collecte (%)

---

## 6. Gestion des Transactions

### 6.1 Enregistrer un Dépôt

1. **Aller** dans **"Collecte"**
2. **Cliquer** sur **"Nouvelle Transaction"**
3. **Sélectionner** le membre
4. **Choisir** le type : **"Dépôt"**
5. **Saisir** le montant
6. **Choisir** le mode de paiement :
   - Espèces
   - Mobile Money
   - Virement
   - Chèque
7. **Ajouter** une description (optionnel)
8. **Cliquer** sur **"Enregistrer"**

#### Résultat
- ✅ Transaction enregistrée
- ✅ Référence unique générée (ex: TXN-2026-001234)
- ✅ Solde du membre mis à jour automatiquement
- ✅ Notification de succès

### 6.2 Enregistrer un Retrait

**Même procédure que le dépôt**, mais :
- Choisir le type : **"Retrait"**
- Le solde sera **débité** automatiquement

### 6.3 Enregistrer une Cotisation

1. **Sélectionner** le membre
2. **Choisir** le type : **"Cotisation"**
3. **Sélectionner** le cycle concerné
4. **Saisir** le montant (généralement = montant cycle)
5. **Enregistrer**

#### Résultat
- ✅ Solde membre mis à jour
- ✅ Montant total du cycle incrémenté
- ✅ Historique enregistré

### 6.4 Consulter l'Historique

1. **Aller** dans **"Compte"**
2. **Sélectionner** un membre
3. Visualiser toutes ses transactions avec :
   - Date
   - Type
   - Montant
   - Référence
   - Statut

---

## 7. Tableau de Bord

### 7.1 Cartes KPI

En haut du tableau de bord, 3 cartes affichent :

1. **Membres Totaux**
   - Nombre total de membres actifs
   - Icône : 👥

2. **Montant Collecté**
   - Somme totale des dépôts et cotisations
   - Devise : FCFA
   - Icône : 💰

3. **Cycles Actifs**
   - Nombre de cycles en cours
   - Icône : 🔄

### 7.2 Graphique d'Évolution

**Graphique linéaire** montrant :
- Évolution des dépôts et retraits sur 6 mois
- Courbe bleue : Dépôts
- Courbe rouge : Retraits

### 7.3 Graphique de Répartition

**Graphique donut** montrant :
- 75% Dépôts (vert)
- 25% Retraits (orange)

### 7.4 Activités Récentes

**Tableau** des 10 dernières transactions avec :
- Date
- Membre
- Type
- Montant

---

## 8. Génération de Cartes ID

### 8.1 Générer une Carte

#### Prérequis
- Le membre doit avoir une photo uploadée

#### Étapes

1. **Aller** dans **"Membres"**
2. **Sélectionner** le membre dans le tableau
3. **Cliquer** sur le bouton **"Carte ID"** (bleu clair)
4. **Aperçu** de la carte s'affiche

### 8.2 Aperçu de la Carte

La carte affiche :
- **Logo TontinePro** en haut à gauche
- **Photo du membre** (circulaire, bordure dorée) en haut à droite
- **Nom complet** en majuscules
- **Code membre** (ID) en doré
- **Téléphone**
- **Ville**
- **QR Code** en bas à droite
- **Footer** : "Membre depuis 2026 • TontinePro Premium"

### 8.3 Télécharger la Carte

1. Dans la fenêtre d'aperçu, **cliquer** sur **"💾 Télécharger PNG"**
2. **Choisir** l'emplacement de sauvegarde
3. **Nommer** le fichier (par défaut : `Carte_M001.png`)
4. **Cliquer** sur **"Enregistrer"**

#### Résultat
- ✅ Fichier PNG haute qualité (550x340px)
- ✅ Prêt pour impression
- ✅ Notification de succès

### 8.4 Impression

**Recommandations** :
- **Format** : Carte de visite (85x54mm) ou personnalisé
- **Papier** : Cartonné 300g minimum
- **Finition** : Pelliculage brillant ou mat
- **Résolution** : 300 DPI

---

## 9. Export de Données

### 9.1 Exporter la Liste des Membres

1. **Aller** dans **"Membres"**
2. **Cliquer** sur le bouton **"Exporter"** (gris)
3. **Choisir** l'emplacement
4. **Nommer** le fichier (par défaut : `export_data.csv`)
5. **Cliquer** sur **"Enregistrer"**

### 9.2 Exporter les Transactions

1. **Aller** dans **"Collecte"** ou **"Compte"**
2. **Cliquer** sur **"Exporter"**
3. Même procédure que pour les membres

### 9.3 Ouvrir le Fichier CSV

**Avec Excel** :
1. Ouvrir Excel
2. Fichier > Ouvrir
3. Sélectionner le fichier CSV
4. Choisir le délimiteur : **Virgule**

**Avec Google Sheets** :
1. Aller sur Google Sheets
2. Fichier > Importer
3. Uploader le fichier CSV

---

## 10. Gestion du Profil

### 10.1 Accéder au Profil

1. **Cliquer** sur l'avatar en haut à droite
2. **Sélectionner** **"Mon Profil"** dans le menu

### 10.2 Modifier les Informations

1. Dans la page profil, **modifier** :
   - Nom complet
   - Email
   - Photo de profil
2. **Cliquer** sur **"Enregistrer"**

### 10.3 Changer le Mot de Passe

1. Dans la page profil, **cliquer** sur **"Changer mot de passe"**
2. **Saisir** :
   - Mot de passe actuel
   - Nouveau mot de passe
   - Confirmation nouveau mot de passe
3. **Cliquer** sur **"Valider"**

#### Règles de Mot de Passe
- Minimum 8 caractères
- Au moins une majuscule
- Au moins un chiffre
- Au moins un caractère spécial (recommandé)

### 10.4 Déconnexion

1. **Cliquer** sur l'avatar
2. **Sélectionner** **"Déconnexion"**
3. Retour à l'écran de connexion

---

## 11. FAQ

### Questions Fréquentes

#### Q1 : Comment réinitialiser le mot de passe d'un utilisateur ?
**R** : Seul l'administrateur peut réinitialiser les mots de passe via le module "Utilisateur".

#### Q2 : Puis-je supprimer une transaction ?
**R** : Non, les transactions ne peuvent pas être supprimées pour des raisons d'audit. Vous pouvez les annuler via le statut.

#### Q3 : Comment ajouter un nouveau cycle ?
**R** : Aller dans Configuration > Cycles > Nouveau Cycle.

#### Q4 : La carte ID ne se génère pas, pourquoi ?
**R** : Vérifiez que le membre a une photo uploadée. Sans photo, une icône placeholder sera utilisée.

#### Q5 : Comment voir l'historique complet d'un membre ?
**R** : Aller dans "Compte", sélectionner le membre, toutes ses transactions s'affichent.

#### Q6 : Puis-je modifier le montant d'une cotisation après création du cycle ?
**R** : Oui, via Configuration > Cycles > Modifier.

#### Q7 : Comment sauvegarder mes données ?
**R** : Effectuer régulièrement une sauvegarde de la base de données MySQL via `mysqldump`.

#### Q8 : L'application fonctionne-t-elle hors ligne ?
**R** : Oui, tant que MySQL est accessible localement.

#### Q9 : Combien de membres puis-je gérer ?
**R** : Jusqu'à 10 000 membres sans problème de performance.

#### Q10 : Comment obtenir du support ?
**R** : Contacter support@tontinepro.com ou consulter la documentation complète.

---

## 📞 Support Technique

**Email** : support@tontinepro.com  
**Documentation** : Voir dossier `docs/`  
**Heures** : Lundi-Vendredi, 9h-17h

---

## 📝 Notes Importantes

> 💡 **Astuce** : Utilisez la barre de recherche en haut pour trouver rapidement un membre par nom ou téléphone.

> ⚠️ **Attention** : Effectuez des sauvegardes régulières de votre base de données.

> ✅ **Bonne pratique** : Changez les mots de passe par défaut dès la première utilisation.

---

**TontinePro** - *Guide d'Utilisation v1.0*  
© 2026 Groupe 1. Tous droits réservés.
