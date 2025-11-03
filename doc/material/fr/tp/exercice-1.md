# TP Exercice 1: Analyser l'application

L'objectif de cet exercice est d'analyser l'application Order flow et ses microservices. Vous apprendrez à comprendre les concepts principaux et comment ils sont implémentés dans les différents modules.

**temps estimé** : 2 heures

**difficulté** : facile

## Tâche 1 : Ségrégation des responsabilités

L'application Order flow est un ensemble de services qui doivent implémenter plusieurs domaines métiers. Comparé aux applications monolithiques traditionnelles, les SOA sont conçues pour faire appel à des unités de traitements plus petites et concentrées sur un domaine métier spécifique. De plus, comme l'application utilise des modèles spécifiques, la ségrégation des responsabilités peut être plus fine que d'habitude.

## Tâche 1 : Questions

1. Quels sont les principaux domaines métiers de l'application Order flow ?

:::tip
Consultez les fichiers de cartographie de contexte dans le dossier `doc`.
:::

2. Comment les services sont-ils conçus pour implémenter les domaines métiers ?

:::tip
Consultez l'architecture des microservices et les noms des packages dans les dossiers `apps` et `libs`.
:::

3. Quelles sont les responsabilités des modules :

- `apps/store-back`
- `apps/store-front`
- `libs/kernel`
- `apps/product-registry-domain-service`
- `apps/product-registry-read-service`
- `libs/bom-platform`
- `libs/cqrs-support`
- `libs/sql`

:::tip
Vous pouvez observer les fichiers de code des dossiers `apps` et `libs` et afficher le graphe des dépendances gradle avec `gradle <module_name>:dependencies`. Ajoutez `| grep -oE '^\+\-\-\-\sproject\s\:.*$' | awk '!seen[$0]++'` pour filtrer les résultats sur les projets locaux.
:::

## Tâche 2 : Identifier les concepts principaux

L'application Order flow utilise des modèles spécifiques pour implémenter les domaines métiers. Ces modèles sont basés sur les principes de la conception pilotée par le domaine (DDD) et de l'architecture pilotée par les événements (EDA).

Vous devez identifier les concepts principaux utilisés dans l'application Order flow.

## Tâche 2 : Questions

1. Quels sont les concepts principaux utilisés dans l'application Order flow ?

:::tip
Comment sont stockées les données ?
Comment sont gérées les transactions ?
Comment sont gérés les évènements métiers ?
Comment sont gérées les erreurs (métier et technique) ?
Quelles formes prennent les échanges entre les services ?
:::

2. Comment les concepts principaux sont-ils implémentés dans les différents modules ?

::: tip
Identifiez les "parties mobiles" de l'application, telles que la sémantique des espaces de noms et les structures de code. Identifiez si un concept ou un modèle spécifique est directement implémenté ou si une solution générique est utilisée (par exemple, une bibliothèque, un framework, une base de données, un service/logiciel tiers). Vous pouvez également vous appuyer sur l'observation des fichiers de déclaration de dépendances (cf. [Gradle](https://docs.gradle.org/current/userguide/userguide.html)).
:::

::: warning
Cette question nécessite de faire des recherches et de comprendre la structure du code.
Cependant, vous pouvez également vous appuyer sur la [présentation du projet](./presentation-projet) pour vous aider à clarifier les sujets de la question.
:::

3. Que fait la bibliothèque `libs/cqrs-support` ? Comment est-elle utilisée dans les autres modules (relation entre métier et structure du code) ?

4. Que fait la bibliothèque `libs/bom-platform` ?

5. Comment l'implémentation actuelle du CQRS et du Kernel assure-t-elle la fiabilité des états internes de l'application ?

## Tâche 3 : Identifier les problèmes de qualité

L'application Order flow n'est pas encore complète ni entièrement fonctionnelle. Il vous incombe d'améliorer la conception et la qualité de l'implémentation de l'application et, si nécessaire, d'implémenter les fonctionnalités manquantes.

Vous devez identifier les problèmes de qualité dans l'application Order flow. Pour ce faire, vous devez utiliser l'outil [MegaLinter](https://www.megalinter.io/), un outil de linting qui analyse le code source et signale les problèmes de qualité.

::: warning
Vous ne devez pas corriger les problèmes de qualité dans cet exercice. L'objectif est d'identifier les problèmes de qualité et de comprendre comment améliorer la conception et la qualité de l'implémentation de l'application. Vous pouvez éventuellement fournir des extraits de code pour illustrer vos conclusions.
:::

::: tip
Vous pouvez appeler le professeur si vous avez besoin d'aide pour identifier les problèmes de qualité. N'hésitez pas à demander une vérification régulière pendant les sessions.
:::

::: tip
Vous pouvez ajuster la configuration de MegaLinter pour correspondre aux standards de codage du projet (par exemple, indentation, conventions de nommage, etc.). Le dépôt du projet repose sur Quarkus et cela implique certains standards de codage spécifiques.
Faites également attention à traiter tous les problèmes signalés par MegaLinter, même s'ils ne sont pas directement liés à la qualité du code (une explication peut être suffisante dans certains cas).
:::

Pour installer MegaLinter, vous pouvez [utiliser pnpm](https://megalinter.io/8/mega-linter-runner/#local-installation) :

```bash
pnpm install mega-linter-runner -D -w
```

Pensez à reporter la version du `package.json` vers `pnpm-workspace.yaml` dans la section catalog:

::: code-group
```json [package.json]
{
  "name": "@org.openrichmedia.priv.tfa.quali-dev/root",
  ...
  "devDependencies": {
    ...
    "mega-linter-runner": "^9.0.0", // [!code --]
    "mega-linter-runner": "catalog:", // [!code ++]
  },
  ...
}
```

```yaml [pnpm-workspace.yaml]
...
catalog:
  ...
  # [!code ++]
  mega-linter-runner: ^9.0.0
  ...
...
```
:::

Configurez MegaLinter pour qu'il prenne en compte les spécificités de votre projet. Générez la configuration de base avec :

```bash
pnpm mega-linter-runner --install
```

Supprimez ensuite les fichiers de configurations que vous n'utiliserais pas.

Modifiez le fichier `.mega-linter.yml` pour qu'il prenne en compte les spécificités de votre projet. Par exemple, vous pouvez activer ou désactiver certains linters, ajuster les règles de style, etc.

:::tip
Cette configuration va activer le linting pour Java dans les sous-modules concernés et désactiver les linters "Repository" (leaks, diff, etc...).
:::

```yaml
...
APPLY_FIXES: all # [!code --]
APPLY_FIXES: none # [!code ++]
CLEAR_REPORT_FOLDER: true # [!code ++]
VALIDATE_ALL_CODEBASE: true # [!code ++]
...
# [!code --]
# ENABLE:
ENABLE: # [!code ++]
  - JAVA # [!code ++]
...
# [!code --]
# DISABLE:
DISABLE: # [!code ++]
  - REPOSITORY # [!code ++]
...
```
