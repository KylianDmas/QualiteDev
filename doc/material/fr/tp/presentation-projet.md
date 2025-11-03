# Présentation du projet Order flow

Ce document présente le projet order flow. Il décrit le contexte, les objectifs et le but du projet.

## Contexte

Le projet "order flow" est une application simplifiée de plateforme de commerce électronique qui permet aux clients de passer des commandes de produits ou de services et aux commerçants de gérer leur catalogue de produit. Il repose sur plusieurs services web pour gérer le cycle de vie des produits, des commandes ainsi que des domaines connexes.

## Objectifs

Les objectifs du projet order flow sont de vous aider à comprendre les concepts suivants :
- Architecture SOA
- Conception pilotée par le métier (DDD)
- Architecture en oignon
- Architecture pilotée par les événements
- Patron de conception CQRS
- Journal d'événements métiers
- Concepts de qualité en ingénierie logicielle

## But

Le but du projet order flow est de vous fournir un travail pratique qui vous aidera à :
- Comprendre l'application order flow et ses microservices
- Améliorer la qualité de l'implémentation de l'application
- Appliquer les concepts de qualité en ingénierie logicielle
- Préparer un support d'évaluation

## Système applicatif

::: warning
Ne tentez pas d'apprendre tous les détails de l'application order flow ni de la conception pilotée par le métier en une seule fois. Concentrez-vous plutôt sur la compréhension des concepts principaux et de leur implémentation dans les microservices.
:::

::: tip
N'hésitez pas à poser des questions si vous avez besoin de plus d'informations.
:::

Le système est sous-découpé en plusieurs éléments :
- Modules d'application : fournie les fonctionnalités principales et couvre les IHM
- Services : exposent des interfaces vers le domaine métier et les couches d'infrastructure telles que la persistance des données
- Bibliothèques : fournissent des fonctionnalités réutilisables

## Domaine métier

L'application order flow repose sur un domaine principal `Shopping en ligne` qui englobe plusieurs sous-domaines fonctionnels, domaines de support et domaines génériques (cf https://alexsoyes.com/ddd-domain-driven-design/) (cf `/doc/business/domain.cml`):

- Domaines principaux (valeur ajoutée) :
  - Panier d'achat
  - Traitement des commandes
- Domaines de support (fonctions nécessaires) :
  - Registre des produits
  - Catalogue de produits
  - Gestion des stocks
  - Gestion des clients
- Domaines génériques (fonctions fournies par des systèmes externes) :
  - Notification
  - Facturation

## Architecture des services et des bibliothèques

L'application order flow est composée de plusieurs services implémentant les domaines selon un modèle CQRS et piloté par les événements.

- Services principaux :
  - Service de panier d'achat :
    - Agrégats
    - Commandes
    - Requêtes
  - Service de traitement des commandes :
    - Agrégats
    - Commandes
    - Requêtes
- Services de soutien :
  - Service de registre des produits :
    - Agrégat (unique : il n'y a qu'un seul registre pour simplifier)
    - Commandes
    - Requêtes
  - Service de catalogue de produits
    - Agrégats
    - Commandes
    - Requêtes
  - Service de gestion des stocks
    - Agrégat (unique : il n'y a qu'un seul stock pour simplifier)
    - Commandes
    - Requêtes
  - Service de gestion des clients
    - Agrégats
    - Commandes
    - Requêtes
- Services génériques :
  - Service de notification
    - Commandes
    - Requêtes
  - Sourcing d'événements :
    - Interface d'événement
    - Interface de stockage d'événements
  - Monnaie :
    - Objet-valeur

:::warning
Pour une plus grande simplicité, les problématiques d'authentification et d'autorisation ne sont pas abordées dans ce projet.
:::

## CQRS, modèle, vues et journal d'événements

L'application order flow utilise le modèle CQRS (Command Query Responsibility Segregation) pour séparer les opérations de lecture et d'écriture. Cela permet d'optimiser les performances et de simplifier la gestion des données.

### Modèle

Le modèle de l'application order flow est basé sur des agrégats qui encapsulent l'état et le comportement des entités. Chaque agrégat est responsable de la gestion de son propre état et de la validation des invariants.

### Vues

Les vues sont des projections de l'état des agrégats qui sont utilisées pour répondre aux requêtes. Elles sont mises à jour en temps réel en écoutant les événements du journal d'événements.

### Journal d'événements

Le journal d'événements est une séquence d'événements qui représente les changements d'état des agrégats. Il est utilisé pour reconstruire l'état des agrégats à tout moment et pour auditer les actions effectuées sur l'application.

### CQRS

En plus de son journal d'événements, l'application order flow utilise le modèle CQRS. CQRS signifie "Command Query Responsibility Segregation". C'est un modèle utilisé pour séparer les opérations de lecture et d'écriture d'une application.

Les avantages de CQRS sont qu'il permet d'optimiser séparément les opérations de lecture et d'écriture, de faire évoluer indépendamment les opérations de lecture et d'écriture, et de simplifier le code en séparant les responsabilités dans la base de code.

Bien que CQRS soit un modèle puissant, il est également complexe et peut être difficile à implémenter dans une modèle d'architecture à granularité fine, surtout dans un système distribué et asynchrone en ce qui concerne la cohérence : dans un tel système, la cohérence transactionnelle est éventuelle et non immédiate. Cela signifie que l'état de l'application n'est pas immédiatement cohérent entre des microservices mais le sera à un moment donné dans le futur.

Pour compenser cette "latence métier", des mécanismes peuvent être mis en place mais ils apportent leur lot de complexité technique. Une solution plus simple consiste à simplement sacrifier la finesse de granularité de séparation des responsabilités. On passe ainsi de microservices, des services logiciels strictement silotés et ne gérant qu'une seule responsabilité et qu'une seule unité de stockage, à des services plus classiques pour permettre une gestion moins fine des données mais une facilité transactionnelle extrêmement accrue.

Dans l'application order flow, le modèle CQRS est implémenté en utilisant un système de projection des évènements métiers.

[Informations supplémentaires sur CQRS (Martin Fowler)](https://martinfowler.com/bliki/CQRS.html)
[Comment appliquer le modèle CQRS étape par étape (Daniel Whittaker)](https://danielwhittaker.me/2020/02/20/cqrs-step-step-guide-flow-typical-application/)

### Quid de l'event sourcing ?

L'event sourcing est une technique qui consiste à stocker l'état d'une application sous forme d'une séquence d'événements. Au lieu de stocker uniquement l'état actuel d'un objet, l'event sourcing enregistre chaque changement d'état en tant qu'événement distinct. Cela permet de reconstruire l'état de l'application à tout moment en rejouant les événements.

Cependant, l'event sourcing introduit également des défis, notamment en termes de complexité et de gestion des événements. Il est important de bien concevoir le modèle d'événements et de mettre en place des mécanismes de gestion des erreurs pour garantir la robustesse de l'application. Enfin, l'event sourcing réduit aussi la capacité de débogage des données stockées : les unités de données sont stockées sous forme d'événements, ce qui complique leur inspection directe et individuelle.

Dans l'application order flow, l'event sourcing n'est donc pas utilisé. A la place, un simple journal d'événements est utilisé pour enregistrer les actions effectuées sur les agrégats. Ce journal permet de retracer l'historique des modifications et de garantir la traçabilité des actions (aspect important pour un contexte commercial et monétique, par exemple).
