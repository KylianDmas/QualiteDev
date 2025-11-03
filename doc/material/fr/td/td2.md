# TD2 : Gérer un dépôt de code

## Les méthodes de gestion
Définissez au mieux les méthodes de gestion suivantes :
(Aidez-vous du CM2 et des outils de recherche)
- Git Trunk
- Git Flow

Indiquez les cas d’usage typique de chaque méthode et leurs avantages et inconvénients.

## Git Trunk
1. Définissez le feature-flags (aussi appelé feature-toggles)
2. Indiquez les moyens usuels d’implémenter du feature-flags
3. Décrire le flux de travail du Trunk-Based Repository

## Git flow
1. Décrire le flux de travail du Git Flow
2. Décrire la méthode préférée pour gérer plusieurs versions majeures/mineures en
parallèle

## Noms de branche (GitFlow)
Donnez les noms de branches correspondant aux situations suivantes :
- Une fonctionnalité « Gestion des utilisateurs – suppression » (ticket n°B-768) :
- Un fix « Mauvaise redirection après ajout d’un email à l’utilisateur » (ticket A-46) :
- L’ajout d’une configuration « devcontainer » pour l’environnement de développement :
- Un hotfix pour préparer un patch depuis une version 1.3.1 :
- Une release mineure après 1.4.17 :
- Une branche support après release 12.5.6 (support version mineure) :

## Commit messages

Indiquez les informations importantes qu’un message de commit devrait indiquer d’un coup
d’œil (obligatoires et souhaitées).

Voici plusieurs messages de commit :

```
feat[B-658]: side-menu statistics page link

+ adding a side menu item
+ adding a static link to the stats page
~ making various minor fixes on CSS

Co-author : Kamel Debbiche
Refs: https://doc.myapp.acompany.net/gui-rules/
```

```
docs[A-245]: C4 modeling – books micro-service

+ init structurizr file (..books.service.structurizr)
- removing outdated ADL and documentation
```

```
chore!: drop support of PHP 7

BREAKING CHANGE: use PHP features not available in PHP 7
```

```
feat: add French language support with i18n
```

Identifier les éléments un à un et déterminer leur caractère obligatoire ou optionnel. Enfin,
déterminez une structure générale applicable à cet ensemble.

Lister les types de commit possibles et décrire leur utilisation.

Qu’est qu’un breaking-change ?

## Semantic versioning

REF : https://semver.org/lang/fr/

Décrire en français les numéros de version suivants ces numéros :
- 1.1.0
- 1.0.0-RC.2
- 1.0.0-snapshot+build.9cbd45f6
- 3.0.0-beta.1
- 2.3.1+nightly.230524.0114
