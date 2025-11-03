# TP Exercice 2: Corriger les problèmes de qualité et introduire des tests (Côté métier du registre de produit [OLTP])

L'objectif de cet exercice est de corriger les problèmes de qualité existants. Vous apprendrez à améliorer la qualité de l'implémentation de l'application.

**portée** : service de registre de produits (`product-registry-domain-service`)

**temps estimé** : 2 heures

**difficulté** : moyenne

## Tâche 1 : Compléter les commentaires et la Javadoc

La Javadoc est un élément essentiel de la documentation du code. Elle permet de décrire les classes, les méthodes et les paramètres de manière claire et concise. Elle permet à votre IDE de fournir des informations utiles lors de l'écriture du code et de générer de la documentation publiable (utile si vous devez partager votre code avec d'autres développeurs).

La base de code fournie comporte des commentaires `TODO` indiquant les éléments à compléter.

Aidez-vous aussi de MegaLinter pour identifier les endroits où il manque de la Javadoc.

:::tip
Cherchez la chaîne `TODO: Complete Javadoc` dans le code.
:::

## Tâche 2 : Corriger les erreurs et les problèmes de qualité remontés

Par défaut, MegaLinter est configuré pour analyser le code Java avec des règles spécifiques. Vous devez examiner les résultats de l'analyse et corriger les erreurs et les problèmes de qualité identifiés.

Lancer MegaLinter aura pour effet de générer un rapport détaillé des problèmes de qualité du code, y compris les erreurs de style, les problèmes de documentation et les violations des bonnes pratiques.

Pour lancer MegaLinter, utilisez la commande suivante :

```bash
pnpm mega-linter-runner -p $WORKSPACE_ROOT
```

:::tip
En l'occurrence, MegaLinter est aussi capable de proposer directement des corrections pour certains problèmes identifiés. Ce "code corrigé" peut être trouvé dans le rapport de MegaLinter `megalinter-reports/updated_sources` et servir de base à vos modifications.
:::

## Tâche 3 : Ajouter des tests unitaires pour l'objet Product du Kernel

Ajoutez une classe de tests unitaires pour la classe `Product`. L'objectif est de tester la logique métier de l'objet.
Les tests unitaires doivent couvrir les cas suivants :

- Méthode statique `create` : tester la création d'un produit
  - Tester la création d'un produit valide
    - La méthode doit retourner un produit
    - La méthode de doit pas jeter d'exception
    - Le produit doit passer à l'état `Actif`
  - Tester la création d'un produit invalide (nom null ou vide, description null, skuId null)
    - La méthode doit jeter une exception
  - Tester la mise à jour d'un produit avec des entrées invalides
  - Tester la mise à jour d'un produit dans un état valide (actif)
    - La méthode ne doit pas jeter d'exception
    - Le produit doit être mis à jour
  - Tester la mise à jour d'un produit dans un état invalide (retiré)
    - La méthode doit jeter une exception
  - Tester la suppression d'un produit dans un état valide (actif)
    - La méthode ne doit pas jeter d'exception
    - Le produit doit passer à l'état `Retiré`
  - Tester la suppression d'un produit dans un état invalide (retiré)
    - La méthode doit jeter une exception

## Tâche 4 : Ajouter des tests d'intégration

Ajoutez une classe de tests d'intégration pour la classe `ProductRegistryCommandResource`. L'objectif est de tester l'intégration entre le client et le serveur via l'API HTTP (BFF et service de registre de produits).
Les tests d'intégration doivent couvrir les cas suivants :

- `POST /api/products` : tester l'enregistrement d'un produit
  - Tester l'enregistrement d'un produit avec un produit valide
    - La réponse doit être une réponse 201 Created avec l'url du produit créé
  - Tester l'enregistrement d'un produit avec un produit invalide (champ manquant/DTO invalide)
    - La réponse doit être Bad request
  - Tester l'enregistrement d'un produit avec un produit nul (pas de corps)
    - La réponse doit être Bad request
- `PATCH /api/products/{id}/name` : tester la mise à jour du nom d'un produit
  - Tester la mise à jour d'un produit avec un nom valide
    - La réponse doit être une réponse 204 No Content
  - Tester la mise à jour d'un produit avec un nom invalide (champ manquant/DTO invalide)
    - La réponse doit être Bad request
  - Tester la mise à jour d'un produit avec un nom nul (pas de corps)
    - La réponse doit être Bad request
- `DELETE /api/product/{id}` : tester la suppression d'un produit
  - Tester la suppression d'un produit avec un produit valide (existant)
    - La réponse doit être une réponse 204 No Content
  - Tester la suppression d'un produit avec un produit invalide (non existant)
    - La réponse doit être Bad request

Même exercice pour la ressource de requête `ProductRegistryQueryResource` :

- `GET /api/products` : tester la recherche d'un produit
  - Tester la recherche d'un produit avec un filtre avec correspondance
    - La réponse doit être une réponse 200 OK avec la liste des produits
  - Tester la recherche d'un produit avec un filtre sans correspondance
    - La réponse doit être une réponse 200 OK avec une liste vide
  - Tester la recherche d'un produit sans filtre
    - La réponse doit être une réponse 200 OK avec la liste de tous les produits de la page
- `GET /api/products/{id}` : tester la recherche d'un produit par identifiant
  - Tester la recherche d'un produit avec un identifiant existant
    - La réponse doit être une réponse 200 OK avec le produit
  - Tester la recherche d'un produit avec un identifiant non existant
    - La réponse doit être une réponse 404 Not Found

::: tip
Des modifications de code peuvent être nécessaires pour que les tests passent.

Pour les tests d'intégration, voir la [documentation Quarkus](https://quarkus.io/guides/getting-started-testing).
:::

::: tip
Vous devrez peut-être utiliser le mocking pour isoler les tests unitaires. Pour l'utilisation du mocking des Beans CDI de Quarkus, voir la [documentation Quarkus](https://quarkus.io/guides/getting-started-testing#mock-support).
:::

## Tâche 5 : Questions

1. Quelle est la différence entre les tests unitaires et les tests d'intégration ?

2. Est-il pertinent de systématiquement couvrir 100% de la base de code par des tests ? Expliquer votre réponse.

3. Quels avantages apporte une architecture en couches d'oignon dans la couverture des tests ? Expliquer votre réponse en prenant pour exemple ce que vous avez pu observer sur l'écriture des tests de la tâche 3.

4. Expliquer la nomenclature des packages `infra`, `application`, `jpa`, `web`, `client`, `model`.
