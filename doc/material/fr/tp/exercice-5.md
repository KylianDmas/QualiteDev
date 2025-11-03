# TP Exercice 5: Corriger les problèmes de qualité au niveau du BFF et du service de lecture du registre

L'objectif de cet exercice est de corriger les problèmes de qualité existants dans le BFF (Backend For Frontend) et le service de lecture du registre de produits. Vous apprendrez à améliorer la qualité de l'implémentation de ses composants.

**temps estimé** : 2 heures

**difficulté** : difficile

## Tâche 1 : "Fuite technique" et "Fuite métier"

Les fuites techniques se produisent lorsque des détails d'implémentation sont exposés à des niveaux d'abstraction plus élevés, ce qui rend le système plus difficile à comprendre et à maintenir. Par exemple, si le service de lecture du registre de produits expose des détails sur la façon dont les produits sont stockés dans la base de données, cela constitue une fuite technique.

Les fuites métier, en revanche, se produisent lorsque la logique métier est mal encapsulée ou exposée de manière inappropriée. Par exemple, si le BFF expose des détails sur la façon dont les produits sont gérés dans le système, cela constitue une fuite métier.

Dans les méthodes `streamProductEvents` et `streamProductListEvents` du `ReadProductService`, il existe des détails de mise en œuvre qui pourraient être cachés à l'intérieur du `ProductEventBroadcaster`. Identifiez ces détails et refactorisez le code en conséquence.

## Tâche 2 : Validation des entrées

Dans le `ProductRegistryQueryResource`, ajoutez des annotations de validation appropriées pour les paramètres d'entrée des méthodes `searchProducts` et `getProductById`. Assurez-vous que les paramètres sont correctement validés avant d'être utilisés dans la logique métier.

:::warning
N'oubliez pas de mettre à jour les tests d'intégration si nécessaire pour refléter les changements apportés à la validation des entrées et ajouter des cas de tests pertinents.
:::

## Tâche 3 : Problème de ségrégation des responsabilités

1. Identifiez comment la ségrégation sur le modèle "onion layers" est mise en œuvre dans les différentes parties de l'application :
- Persistence de données
- Logique métier
- Communication inter-modules
- Présentation (API)

Vous remarquerez que chaque couche a des responsabilités clairement définies et qu'il existe peu de dépendances entre elles. Cela facilite la maintenance et l'évolution de l'application. Néanmoins, il est possible que certaines parties de l'application ne respectent pas totalement ce modèle, ce qui peut entraîner des problèmes de qualité et de testabilité.

2. En vous penchant sur les projections `ProductView` et `ProductSummary` ainsi que leur usage, un détail devrait vous poser problème dans le flux `Front -> BFF -> Service de lecture`. Identifiez ce détail et corrigez-le.

::: tip
Dans le schéma architectural courant, le BFF est conçu pour faire de la composition d'API tandis que les services devraient exposer un modèle normalisé.
:::

## Tâche 4 : Problème de conformité avec les conventions établies

Certaines parties du code ne respectent pas les conventions de codage établies dans le projet.

1. L'interface `ProductQuery` (dans le package `apps.product-registry-read-service`) contient les différentes structures de données utilisées pour manipuler les paramètres d'entrée qui devraient être utilisées pour appeler les méthodes du service de lecture. Cependant, dans le `ProductRegistryQueryResource`, ces structures ne sont pas utilisées correctement.

2. Dans le `RetireProductService`, la méthode `retire` ne respecte pas les conventions de codage établies dans le projet. Les services voisins utilisent la sémantique "handle".

Refactorisez le code pour vous assurer que toutes les conventions de codage sont respectées. Assurez-vous que le code est cohérent avec le reste du projet et qu'il suit les meilleures pratiques de développement.

:::tip
Vous serez amené à parfois modifier les signatures de certaines méthodes. N'hésitez pas à propager ces changements dans les parties existantes de l'application en utilisant les fonctionnalités de refactorisation de votre IDE. En l'occurence, vous pouvez utilisez la fonctionnalité "Find Usages" et "Rename Symbol" pour vous aider dans ces tâches.
:::

## Tâche 5 : Questions

1. Dans le `ProjectionDispatcher`, l'implémentation actuelle est simplifiée à l'extrême : Un agrégat (object métier), une vue, un gestionnaire de projection.

Expliquez les limitations dans le cas :
- D'un agrégat avec plusieurs vues.
- D'une vue alimentée par plusieurs agrégats.
- De plusieurs gestionnaires d'évènements pour différentes vues, actions ou traitements éventuellement dispatchés sur plusieurs instances d'un même service ou distribués sur plusieurs services.

2. Proposez des améliorations structurelles (schéma, classes, interfaces, requêtes SQL) pour lever ces limitations.

3. Dans la mesure où l'outbox est actuellement remplie au fur et à mesure des modifications apportées aux agrégats, que se passerait-il lors de l'évolution de l'application, si l'on devait ajouter une nouvelle vue projetée à partir d'un agrégat existant ? Comment géreriez-vous la situation pour garantir que la nouvelle vue soit correctement initialisée avec les données existantes ?

## Tâche 6 : Bonus - Amélioration de l'UX du Store Front

Dans le Store Front, lorsque vous créez ou modifiez un produit, il n'y a pas de feedback visuel indiquant que les projections sont en cours de traitement puis terminées. Seul le rafraîchissement manuel de la page permet de voir les changements car, jusqu'à présent, on considère qu'un retour positif de l'API signifie que l'opération a été acceptée pour traitement et validée d'un point de vue métier.

Des morceaux de code sont déjà présents dans le service "lecture" pour permettre de streamer les évènements liés aux produits. Utilisez cette fonctionnalité pour améliorer l'expérience utilisateur dans le Store Front en ajoutant un indicateur visuel (spinner, barre de progression, message, etc.) qui informe l'utilisateur que les modifications sont en cours de traitement et qu'il doit patienter avant de voir les changements reflétés dans l'interface. Vous pouvez ensuite rafraîchir automatiquement l'UI une fois que les projections sont terminées.

:::tip
Vous pouvez utiliser le SSE (Server-Sent Events) pour implémenter cette fonctionnalité de streaming dans le Store Front. Le SSE permet au serveur d'envoyer des mises à jour en temps réel au client via une connexion HTTP persistante.
:::

:::warning
Le SSE ne permet pas de paramétrer les flux côté client. Vous avez le choix entre :
- Créer un flux par produit (ex: `/products/{productId}/events`)
- Créer un flux global pour tous les produits (ex: `/products/events`) et filtrer côté client les évènements qui l'intéressent.

Choisissez la solution que vous préférez.
:::

:::warning
Faites attention au fait que vous allez faire passer le flux SSE à travers le BFF. Assurez-vous que le BFF relaie correctement les évènements du service de lecture vers le Store Front.
:::

:::warning
Si vous modifiez le modèle de données, assurez-vous d'utiliser un ou plusieurs change Liquibase dans le package `libs/sql` pour appliquer les modifications à la base de données.
:::

## Tâche 7 : Bonus - Tests de bout en bout (E2E)

Ajoutez des tests E2E (end-to-end) pour vérifier notamment que l'amélioration de l'UX du Store Front fonctionne correctement. Vous pouvez utiliser un framework de test comme [Cypress](https://www.cypress.io/).

:::tip
Référez-vous à l'introduction à Cypress dans le [TD dédié](../td/td3.md).
:::

:::warning
Si vous utilisez Cypress ou tout autre framework de test basé sur une automatisation de navigateur, assurez-vous d'utiliser un navigateur en mode "headless" pour exécuter les tests dans un environnement n'ayant pas d'interface graphique (devcontainer, CI/CD, etc.).
:::
