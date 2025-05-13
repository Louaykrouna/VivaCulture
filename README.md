#VivaCulture 
Plateforme d'expériences sociales et culturelles 

*Année universitaire : 2024-2025*  
*Établissement : Esprit School of Engineering*

## 📋 Table des matières* 

## 1- Description
## 2- Fonctionnalités 
## 3- Technologies
## 4- Installation]
## 5- Utilisation
## 6- Captures d'écran
## 7- Contributions
## 8- Licence
## 9- Remerciements

## 1- Description 🌟:
VivaCulture est une application de bureau immersive dédiée à la découverte, la planification et la participation à des événements culturels et sociaux à travers la Tunisie. 
Conçue dans le cadre du module Projet Intégré : Développement Web Java à l’Esprit School of Engineering, cette plateforme vise à dynamiser la scène événementielle
locale en connectant organisateurs, partenaires et participants autour d’expériences uniques.

Avec ses six modules de gestion complémentaires, VivaCulture permet une administration fluide des contenus, des interactions utilisateurs et des réservations.
L'application met l’accent sur l’accessibilité, la convivialité et l’automatisation des processus liés aux événements, tout en favorisant la visibilité des initiatives culturelles.

## 2- Fonctionnalités: 🚀 
### 👥 Module Utilisateur
- 3 rôles : simple user/organisateur/Admin
- Sécurité avancée :
  - Authentification par Google
  - Hachage bcrypt des mots de passe
  - Verification des compte par email
  - Possibilité de changer mot de pas en cas d'oublier , par mail .

###🎫 Module Événement: 
Fonctionnalités: *
-Création d’un événement avec saisie du titre, de la description et des dates
-Statut “En attente” jusqu’à approbation ou rejet par l’admin
-Géolocalisation & Météo :
Sélection de la ville via autocomplete puis affichage instantané sur carte (API géolocalisation)
-Consultation des prévisions météo pour la date choisie (API météo)
-Notifications :
 *E-nvoi automatique d’un e-mail de confirmation en cas d’acceptation
 *-Envoi d’un e-mail de rejet.
-Exportation des détails de l'évènement en PDF.

### 🗓 Module réservation :
Fonctionnalités: 
-Réservation d'événements
-Réservation en ligne pour les événements
-API de statistiques pour suivre l'engagement
-Historique des réservations pour chaque utilisateur identifié par email

##🤝Module Partenaire & Collaboration
Ce module assure la gestion des partenaires et de leurs collaborations via des opérations complètes de CRUD.
Il comprend :
Intégration API dans l'entité Partenaire :
  - Envoi automatique de mails
  - Paiement sécurisé via Stripe

##📝Module Blog : 
Module blog: résumer d un blog 
-Traduction d un blog 
-Aimer ou dislike d un blog
-Le partage sur les réseaux sociaux .

Lors de l’ajout d’un nouveau partenaire, une collaboration doit être sélectionnée.

Statistiques disponibles pour les deux entités

Filtrage et recherche selon le nom pour une navigation rapide et intuitive 

## 💻 Technologies

### Backend
- *Framework* : Symfony 6.4
- *Base de données* : MySQL
- *Sécurité* : JWT, OAuth2

### Frontend
- *Framework* : symfony
- *Bibliothèques* :
  - FullCalendar pour les RDV
  -Openweather: 
  -  pour les traductions
  - Chart.js pour les stats

### Services externes
- *Paiement* : Stripe API
- *Notifications* : Twilio (SMS/Email)
- *IA* : OpenAI (descriptions d'articles)


## 🛠 Installation

### Prérequis
- PHP 8.2+
- Composer
- MySQL 8.0+

### Étapes
# 1. Cloner le dépôt
git clone https://github.com/votre-user/VivaCulture.git
cd VivaCulture

# 2. Installer les dépendances
composer install
npm install

# 3. Configurer l'environnement
cp .env.example .env
# Modifier les variables dans .env

# 4. Lancer la base de données
mysql -u root -p < database.sql

# 5. Démarrer les serveurs
symfony server:start
npm run dev
`
## 📸 Captures d'écran

## 🤝 Contributions
Les contributions sont les bienvenues ! Suivez ce workflow :

Forker le projet

Créer une branche (git checkout -b feature/ma-fonctionnalite)

Commiter vos changements (git commit -m 'Ajout d'une super fonctionnalité')

Pousser vers la branche (git push origin feature/ma-fonctionnalite)

Ouvrir une Pull Request

## 📜 Licence
Ce projet est sous licence MIT - voir le fichier LICENSE pour plus de détails.

## 🙏 Remerciements

Encadrants : Mr Moataz et Mr Oussema Sellami - Esprit School of Engineering

Contributeurs : Voir CONTRIBUTORS.md

