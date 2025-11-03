# Objectif du TP

Les objectifs du travail pratique sont de vous faire pratiquer les concepts suivants :
- Architecture de microservices
- Conception pilotée par le métier
- Architecture pilotée par les événements
- Améliorations de la qualité et refactorisation
- Tests et développement piloté par les tests
- Intégration continue et déploiement continu
- Documentation et communication
- Travail d'équipe et collaboration
- Revue de code, retour d'information et contrôle de version

::: warning
Le but du travail pratique est de vous aider à comprendre les concepts et à les appliquer en pratique. Il ne s'agit pas de livrer une solution parfaite mais d'apprendre de vos erreurs et d'améliorer vos compétences.
:::

::: tip
N'hésitez pas à poser des questions si vous avez besoin de plus d'informations.
:::

## État initial de l'application

L'application de flux de commandes est un ensemble de microservices qui doit implémenter plusieurs domaines d'affaires.

Cependant, pour des raisons de simplicité, l'application n'est pas complète ni entièrement fonctionnelle.

### Fonctionnalités implémentées

Les fonctionnalités suivantes sont déjà implémentées dans l'application de flux de commandes :

- Service de registre de produits : gère les produits disponibles à la vente et leur disponibilité dans le catalogue.
- CQRS : interface de gestion des événements et projections pour les microservices.
- "Langage publié" (contrats d'interface) : interface d'interlocution pour les microservices. Il ne contient pour l'instant que les objets liés au registre.

### Fonctionnalités manquantes

Les fonctionnalités suivantes sont manquantes dans l'application de flux de commandes :

- Service de catalogue de produits : gère les produits disponibles à la vente et leurs prix. Il doit avoir une vue matérialisée des produits disponibles à la vente (avec les prix et les informations agrégées).
- Service de gestion des stocks : gère le stock des produits disponibles à la vente. Il doit avoir un moyen de gérer la réservation des articles et la disponibilité des stocks.
- Service de panier d'achat : gère le panier d'achat des clients.
- Service de traitement des commandes : gère le cycle de vie des commandes des clients.
- Service de gestion des clients : gère les clients et leurs commandes.
- Service de notification : envoie des notifications aux clients.
- Monnaie : interface des valeurs monétaires pour les microservices.

## Travail pratique

::: warning
Pour une meilleure compréhension du matériel de travail pratique, consultez d'abord la [présentation du projet](./presentation-projet).
:::

Le travail pratique est divisé en plusieurs exercices qui vous aideront à comprendre l'application de flux de commandes et ses microservices. Vous apprendrez également à améliorer la qualité de la conception et de l'implémentation de l'application.

::: tip
Vous commencez le travail de groupe (3 à 4 étudiants) seulement à partir de l'exercice 3. Avant cela, vous travaillerez individuellement.
À partir de l'exercice 4, commencez à travailler sur le même dépôt.
:::

Le travail pratique est divisé en plusieurs exercices :

0. [Présentation du projet](./presentation-projet)
1. [Introduction : Lancer et tester l'application](./introduction) (1 heure)
2. [Exercice 1 : Analyser l'application](./exercice-1) (2 heures)
3. [Exercice 2 : Corriger les problèmes de qualité et introduire des tests](./exercice-2) (2 heures)
4. [Exercice 3 : Collaborez sur le monorepo](./exercice-3) (1 heure)
5. [Exercice 4 : Projection des événements dans des vues matérialisées](./exercice-4) (1 heure)
6. [Exercice 5 : Corriger les problèmes de qualité au niveau du BFF et du service de lecture du registre](./exercice-5) (2 heures)
7. [Exercice 6 : Observabilité de l'application](./exercice-6) (1 heure)
8. [Exercice 7 : Centraliser les logs et activer le monitoring](./exercice-5) (1 heure)


<!-- Aller plus loin (optionnel) :

9. [Exercice 8 : Implémenter la vue matérialisée du catalogue de produits](./exercice-8)
10. [Exercice 9 : Implémenter la fonctionnalité de réservation de gestion des stocks](./exercice-9) -->

## Que livrer ?

Pour chaque exercice, vous devrez livrer les éléments suivants :
- Un rapport au format markdown avec vos réponses aux questions et les étapes que vous avez suivies pour réaliser l'exercice.
- Le cas échéant, les modifications de code que vous avez apportées dans le monorepo sous forme de demande de fusion (doit être fusionnée dans votre branche principale).
- Le cas échéant, vos modifications de documentation doivent être exportées sous forme d'images.
- Le cas échéant, les tests que vous avez écrits pour valider vos modifications.

::: tip
- Veillez à ce que votre rapport soit clair et concis. Utilisez des diagrammes, des extraits de code et des captures d'écran pour illustrer vos réponses.
- N'oubliez pas d'inclure les références aux fichiers que vous avez modifiés et de définir les droits d'accès corrects à vos ressources.
- Incluez dans votre document principal à votre enseignant les liens vers votre dépôt, vos demandes de fusion et votre documentation.
:::

