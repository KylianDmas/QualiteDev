# Introduction : Lancer et tester l'application

Dans cette première étape, vous allez configurer votre environnement de développement et lancer l'application. Vous apprendrez également à tester les différentes fonctionnalités de l'application.

## Étapes à suivre

1. Cloner le dépôt Git contenant le code de l'application.
2. Installer les dépendances nécessaires.
3. Lancer l'application en mode développement.
4. Tester les fonctionnalités de l'application à l'aide des outils fournis.

## Résultats attendus

À la fin de cette étape, vous devriez être en mesure de lancer l'application et de tester ses fonctionnalités de base.

## Lancer les composants

La liste des composants à lancer est la suivante :

- Service de registre de produits
    - Commande (écriture) : `Product Registry Command Service`
        - Process Java sur le port 8091
    - Query (lecture) : `Product Registry Read Service`
        - Process Java sur le port 8092
- BFF (Backend For Frontend) : `Order Flow BFF`
    - Process Java sur le port 8080
- Front de gestion de magasin : `Store Front`
    - Process Node sur le port 4200

## Architecture simplifiée

Concrètement, dans cette architecture "Orientée Services" les composants intéragissent comme suit :

- Le **Store Front** envoie des requêtes au **Order Flow BFF** pour initier le processus de commande.
- Le **Front** et le **BFF** sont couplés 1 à 1 via une API : chaque fonctionnalité du **Front** correspond à un point d'entrée dans le **BFF**. Ainsi, modifier une fonctionnalité de manière atomique est facilité.
- Le **BFF** ne porte aucune logique métier : ce n'est pas son rôle. Cependant, il **peut effectuer des transformations de données** si nécessaire. Le BFF sert alors de passerelle et effectue une "composition des API".
- Le **BFF** communique avec le **Product Registry Domain Service** pour traîter les commandes (entendre "commande" au sens système/technique du terme, c'est à dire une instruction destinée à modifier l'état de l'application ["écrit cette donnée", "supprime cet élément", "met à jour cet élément", etc.]).
- Le **Product Registry Read Service** est utilisé pour récupérer les informations sur les produits disponibles. Il expose une API de lecture optimisée pour les requêtes fréquentes du couple front-end/BFF (ou pour d'autres besoins spécifiques liés à d'autres potentiels consommateurs).

## Jouer les migrations de la base de données

L'application repose sur liquibase pour gérer les migrations de la base de données. Avant de lancer l'application, vous devez exécuter les migrations pour créer les tables nécessaires dans la base de données.

Référez-vous au fichier `README.md` dans le répertoire `libs/sql` pour plus d'informations sur la façon d'exécuter les migrations.

## Lancer les différents modules

Pour lancer les différents modules de l'application, utilisez le script Gradle `quarkusDev` pour chaque module.

Exemple :

```bash
gradle :apps:product-registry-domain-service:quarkusDev
```

Lancer le front :

```bash
pnpm run --filter apps-store-front start
```

## Tester l'application

### Créer un produit

1. Ouvrez votre navigateur web et accédez à `http://localhost:4200`.
2. Cliquez sur "Register New Product".
3. Remplissez le formulaire avec les informations du produit (SKU, Nom, Description).
4. Cliquez sur "Register Prodcut" pour enregistrer le produit.

:::warning
La nature asynchrone de l'architecture peut entraîner un délai entre l'enregistrement du produit et sa disponibilité dans la liste des produits. Patientez quelques instants avant de vérifier la présence du produit dans la liste. En général, la projection ne dure que quelques secondes.
:::

### Lister les produits

1. Dans l'interface du Store Front, cliquez sur "Products". (ou rechargez la page si vous y êtes déjà)
2. Vous devriez voir le produit que vous venez de créer dans la liste des produits disponibles.

### Modifier un produit

1. Dans la liste des produits, cliquez sur le produit que vous souhaitez modifier.
2. Cliquez sur "Edit".
3. Modifiez les informations du produit dans le formulaire.
4. Cliquez sur "Save Changes" pour enregistrer les modifications.

:::info
Vous remarquerez que les modifications sont immédiatement visibles dans l'interface. Cependant, en arrière-plan, le système traite les événements de modification de manière asynchrone. C'est en fait le front qui met à jour l'affichage immédiatement pour une meilleure expérience utilisateur.
:::

:::warning
A contrario, la liste des évènements associés au produit n'est pas mise à jour immédiatement. Vous devrez patienter quelques instants avant de voir les nouveaux événements apparaître dans la liste des événements du produit après un rafraîchissement de la page.
:::

### Retirer un produit

1. Dans la liste des produits, cliquez sur le produit que vous souhaitez retirer.
2. Cliquez sur "Retire Product".
3. Confirmez l'action dans l'invite de confirmation.
4. Le produit sera retiré de la liste des produits disponibles.

### Vérifier l'état final dans la liste des produits

1. Revenez à la liste des produits dans l'interface du Store Front.
2. Vérifiez que le produit que vous avez retiré n'apparaît plus dans la liste des produits disponibles.

:::warning
Comme pour les autres opérations, le retrait du produit est traité de manière asynchrone. Patientez quelques instants avant de vérifier que le produit a bien été retiré de la liste.
:::

:::info
Vous remarquerez que le produit, bien que "supprimé" du registre, reste accessible via son identifiant direct (URL). Cela est dû au fait que le système conserve l'historique des produits pour des raisons d'audit et de traçabilité. Vous pouvez toujours consulter les détails du produit et son historique d'événements en accédant directement à son URL, ou en cliquant sur son entrée dans la liste.

Remarque : il n'est plus possible d'interagir avec le produit (modification, retrait) une fois qu'il a été retiré. De plus, son SKU reste réservé et ne peut pas être réutilisé pour un nouveau produit.
:::

### Observer le contenu de la base de données

Vous pouvez utiliser un outil de gestion de base de données comme DBeaver pour vous connecter à la base de données PostgreSQL et observer les tables et les données.

:::tip
Votre IDE est normalement pré-paramétré pour exposer le port 5432 en local, vous pouvez donc vous connecter à la base de données depuis votre machine hôte.
:::

1. Regardez le schéma `public` pour voir les tables créées par Liquibase.
    - `databasechangelog` : table de suivi des migrations Liquibase.
    - `databasechangeloglock` : table de verrouillage des migrations Liquibase.
2. Regardez le schéma `domain` pour voir les tables utilisées par l'application.
    - `products` : table principale des produits.
3. Regardez le schéma `eventing`:
    - `event_log` : table des événements produits par le domaine.
    - `outbox` : table de l'outbox pour la gestion des messages asynchrones.
4. Regardez le schéma `read_product_registry`:
    - `products_view` : table de lecture optimisée pour les produits.

:::tip
Vous remarquerez que la table `outbox` est vide. En effet, l'outbox ne contient en réalité que des données éphémères et transitoires. Les messages sont consommés rapidement après leur insertion, de sorte que la table reste généralement vide et le schéma de lecture `read_product_registry` est constamment mis à jour avec les dernières informations par l'application.
:::