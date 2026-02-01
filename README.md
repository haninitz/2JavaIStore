# IstoreJava Hanitea et Dalila
Projet SUPINFO – Application de gestion de stock.

## Pré-requis
- **Java 19** (ou compatible avec la config Maven du projet)
- **MySQL** (local)
- IntelliJ IDEA (recommandé)

## 1) Base de données
1. Crée la base et les tables en exécutant le script :
    - `src/main/resources/database.sql`

2. Vérifie la connexion MySQL dans :
    - `src/main/java/fr/istorejava/db_connection/DBConnection.java`

> Par défaut, la DB attend : `istore_db`, user `root`.

## 2) Lancer le projet
- Lancer la classe :
    - `fr.istorejava.Main`

L’application démarre sur l’écran **Login**.

## 3) Comptes & Whitelist
- L’inscription n’est possible que si l’email est **whitelisté** (table `email_whitelist`).
- Le **premier utilisateur** créé devient automatiquement **ADMIN** (règle projet).

Exemples (SQL) :
```sql
INSERT INTO email_whitelist(email) VALUES ('admin@istore.com');
```

## 4) Sécurité
- Les mots de passe ne sont jamais stockés en clair.
- Hashing via **BCrypt** (dépendance `jbcrypt`).

## 5) Architecture (packages)
- `fr.istorejava.ui` : interfaces Swing (Login, SignUp, Dashboards…)
- `fr.istorejava.data` : accès données (MySQL) : Users / Stores / Items / Inventory / Whitelist
- `fr.istorejava.db_connection` : connexion MySQL + session
- `fr.istorejava.security` : utilitaires sécurité (BCrypt)

## 6) Fonctionnalités principales (selon sujet)
- Authentification : **SignUp + Login** (whitelist obligatoire)
- Gestion utilisateurs : CRUD (admin peut supprimer/éditer employés)
- Admin : whitelist email, création/suppression store, création/suppression item
- Stores : assignation employé ↔ store, liste des accès
- Inventory : affichage stock, + / - sur quantité (jamais < 0)

## Notes
- Si la DB n’est pas configurée correctement, l’appli ne pourra pas se connecter.
- En cas d’erreur (login, signup, stock…), des messages sont affichés via `JOptionPane`.
