# 🚀 Aero Space Management System

Un système de gestion complet pour les ventes, les stocks d'équipements aérospatiaux et la relation client (compagnies aériennes), développé en Java. Conçu pour allier performance, persistance des données sous format CSV (compatible Excel), et une navigation fluide en ligne de commande.

---

## 🌟 Fonctionnalités Principales

### 📦 Gestion des Équipements
- **Opérations CRUD** : Ajoutez, modifiez et supprimez vos pièces et équipements aérospatiaux (moteurs, avionique, capteurs, etc.).
- **Champs spécifiques** : Gestion de la *Référence Fabricant* (ex: CFM-56-7B) et de la *Certification* (ex: FAA, EASA).
- **Ravitaillement Rapide** : Système d'alerte en temps réel pour le stock bas, avec un menu intégré pour réapprovisionner instantanément les équipements en rupture.

### 🏢 Gestion des Compagnies (Clients)
- Suivez toutes les compagnies clientes (Air France, SpaceX, Airbus, etc.).
- Gérer les contacts principaux, adresses, emails et l'historique de tous leurs achats.

### 🧾 Ventes et Commandes
- Effectuez des ventes avec des reçus détaillés.
- Annulation des ventes avec réintégration automatique des pièces dans le stock.

### 📊 Statistiques et Rapports
- Consultez les ventes par période (jour, semaine, mois).
- Visualisez les meilleurs vendeurs et les équipements les plus populaires.
- **Exportation** : Export des rapports dans le dossier `rapports/` (fichier `rapport.pdf`).

### 🔐 Sécurité & Rôles
- **Administrateur** : Accès complet à la plateforme, gestion des produits, clients, vendeurs et analyses statistiques.
- **Vendeur** : Accès simplifié (ventes, recherche d'équipements et clients).

---

## 🛠️ Architecture Technique

- **Langage** : Java
- **Persistance** : Fichiers CSV locaux, séparés par des points-virgules (`;`) pour une compatibilité native avec Microsoft Excel (ex: `produits.csv`, `clients.csv`, `ventes.csv`, `utilisateurs.csv`).
- **Design Pattern** : Utilisation du modèle `Singleton` pour le gestionnaire de données (`DataStore`).

---

## 🚀 Démarrage Rapide

### 1. Prérequis
- Avoir Java (JDK) installé sur votre machine.
- Terminal ou Invite de commandes.

### 2. Compilation
Compilez l'ensemble des fichiers `.java` depuis le dossier racine :
```bash
javac -d . Src/auth/*.java Src/modeles/*.java Src/services/*.java Src/stockage/*.java Src/ui/*.java Main.java
```

### 3. Exécution
Lancez l'application avec la commande :
```bash
java Main
```

---

## 👤 Identifiants par défaut

| Utilisateur     | Identifiant  | Mot de passe | Rôle       |
|-----------------|--------------|--------------|------------|
| Administrateur  | `admin1`     | `admin`      | ADMIN      |
| Vendeur         | `essame`     | `password`   | VENDEUR    |
| Vendeur         | `eyenga`     | `password`   | VENDEUR    |
| Vendeur         | `fotso`      | `password`   | VENDEUR    |

*(Note: Les mots de passe sont hashés en MD5 dans le fichier `utilisateurs.csv` pour des raisons de sécurité).*

---

*Groupe 5 devoir surveillée de programmation orientée objet(POO)* 
