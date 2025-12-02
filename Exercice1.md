# TP Exercice 1: Analyser l'application

## Objectif

L'objectif de cet exercice est d'analyser l'architecture de l'application Order flow, basée sur les microservices, le Domain-Driven Design (DDD) et l'Event-Driven Architecture (EDA), en identifiant ses concepts clés et sa ségrégation des responsabilités.

---

## Tâche 1 : Ségrégation des responsabilités

### 1. Quels sont les principaux domaines métiers de l'application Order flow ?

Les domaines métiers principaux de l'application sont :

1.  **Registre de Produits (`Product Registry`)** :
    * Gestion du cycle de vie des produits (enregistrement, modification, retrait).
    * Implémenté par les services `product-registry-domain-service` (écriture) et `product-registry-read-service` (lecture).
2.  **Vitrine / Boutique (`Store`)** :
    * Interface utilisateur et point d'entrée pour la consultation du catalogue et les commandes.
    * Implémenté par `store-front` (Frontend) et `store-back` (Backend for Frontend - BFF).


### 2. Comment les services sont-ils conçus pour implémenter les domaines métiers ?

Les domaines métiers sont implémentés en utilisant une architecture de **Microservices** et le patron **CQRS (Command Query Responsibility Segregation)** :

* **Services de Commande (écriture)** : Se concentrent sur la validation des règles métier et l'émission d'événements (ex: `apps/product-registry-domain-service`).
* **Services de Requête (lecture)** : Se concentrent sur l'exposition de données optimisées pour la lecture (vues matérialisées) (ex: `apps/product-registry-read-service`).
* **Services d'Interface (BFF)** : Agissent comme un agrégateur et une passerelle pour le frontend (ex: `apps/store-back`).

### 3. Quelles sont les responsabilités des modules :

| Module | Responsabilité |
| :--- | :--- |
| `apps/store-back` | **Backend for Frontend (BFF)**. Expose une API simple et orchestre les appels (REST/RPC) vers les services de domaine d'écriture et de lecture pour le frontend. |
| `apps/store-front` | **Frontend (Angular)**. Fournit l'interface utilisateur pour interagir avec le BFF. |
| `libs/kernel` | **Noyau Partagé (Shared Kernel)**. Contient les concepts de domaine fondamentaux partagés et les objets de valeur (ex: `Product.java`, `ProductId.java`, `ProductEventV1.java`, interfaces de Repository). |
| `apps/product-registry-domain-service` | **Service d'Écriture (Commande)**. Implémente la logique métier (DDD) et utilise l'**Event Sourcing** pour enregistrer les événements de domaine. |
| `apps/product-registry-read-service` | **Service de Lecture (Requête)**. Construit et expose des **vues matérialisées (Projections)** en consommant les événements émis par le service d'écriture. |
| `libs/bom-platform` | **Bill of Materials (BOM)**. Définit les versions cohérentes et fiables des dépendances (Quarkus, Hibernate, etc.) pour l'ensemble du projet. |
| `libs/cqrs-support` | **Support Technique CQRS/Event Sourcing**. Fournit l'infrastructure générique (interfaces `DomainEvent`, `Projector`) et l'implémentation de l'**Outbox Pattern** pour la gestion transactionnelle des événements. |
| `libs/sql` | **Gestion des Schémas de Base de Données**. Contient les scripts **Liquibase** (ou équivalent) pour l'initialisation et la migration des schémas de base de données pour tous les services. |

---

## Tâche 2 : Identifier les concepts principaux

### 1. Quels sont les concepts principaux utilisés dans l'application Order flow ?

Les concepts architecturaux et de conception utilisés sont :

1.  **Domain-Driven Design (DDD)** : Structuration autour d'agrégats (ex: `Product`), d'entités et de contextes délimités.
2.  **Command Query Responsibility Segregation (CQRS)** : Séparation entre la logique d'écriture et la logique de lecture.
3.  **Event Sourcing** : L'état du domaine est stocké comme une séquence immuable d'événements.
4.  **Architecture Orientée Événements (EDA)** : La communication inter-services se fait via la diffusion d'événements.
5.  **Outbox Pattern** : Assure la cohérence transactionnelle entre la persistance de l'état (journal d'événements) et la diffusion des événements.
6.  **Vues Matérialisées / Projections** : Tables optimisées pour la lecture, mises à jour par des "projectors" qui consomment les événements.

### 2. Comment les concepts principaux sont-ils implémentés dans les différents modules ?

| Concept | Implémentation | Module(s) clé(s) |
| :--- | :--- | :--- |
| **Event Sourcing** | `EventLogEntity` stocke les événements produits par l'agrégat. | `apps/product-registry-domain-service` (utilise `libs/cqrs-support`) |
| **CQRS** | Séparation physique et conceptuelle entre les services. | `apps/product-registry-domain-service` et `apps/product-registry-read-service` |
| **Outbox Pattern** | `OutboxEntity` est remplie dans la même transaction que l'enregistrement de l'événement de domaine. Un *Poller* (`OutboxPartitionedPoller.java`) diffuse ensuite ces événements. | `libs/cqrs-support`, `apps/product-registry-read-service` |
| **Projection** | Les classes qui implémentent l'interface `Projector` (ex: `ProductViewProjector.java`) transforment les événements entrants en vues de lecture persistées (`ProductViewEntity`). | `apps/product-registry-read-service` |

### 3. Que fait la bibliothèque `libs/cqrs-support` ? Comment est-elle utilisée dans les autres modules (relation entre métier et structure du code) ?

* **Rôle** : Fournir les primitives d'infrastructure génériques nécessaires à la mise en œuvre de CQRS/Event Sourcing.
* **Utilisation** :
    * Elle définit les interfaces génériques (`DomainEvent`, `Projector`).
    * Elle implémente les mécanismes de l'**Outbox Pattern** (`OutboxEntity`, `JpaOutboxRepository`) pour l'écriture transactionnelle.
* **Relation** : Elle permet la **séparation des préoccupations**. Le code métier (dans `product-registry-domain-service`) se concentre sur les règles de domaine, tandis que l'infrastructure technique nécessaire au pattern (transactionnalité, diffusion d'événements) est déléguée à cette bibliothèque de support.

### 4. Que fait la bibliothèque `libs/bom-platform` ?

C'est un **Bill of Materials (BOM)** Gradle. Elle sert uniquement à définir et à centraliser un ensemble de versions de dépendances (ex: Quarkus, bibliothèques communes) pour assurer la cohérence et la compatibilité dans tous les sous-modules.

### 5. Comment l'implémentation actuelle du CQRS et du Kernel assure-t-elle la fiabilité des états internes de l'application ?

La fiabilité est assurée par :

1.  **Event Sourcing (Immuabilité)** : L'état du domaine est dérivé et non stocké directement. Le journal d'événements est immuable, ce qui permet de reconstruire l'état à tout moment et empêche les mises à jour directes et potentiellement incohérentes.
2.  **Outbox Pattern (Atomicité Transactionnelle)** : Le service d'écriture utilise l'Outbox Pattern pour lier :
    * L'enregistrement d'un nouvel état dans le journal d'événements.
    * L'enregistrement de l'événement dans la table Outbox (pour la diffusion).

    Ces deux opérations se produisent dans une **seule transaction de base de données**. Si l'une échoue, les deux sont annulées, garantissant que l'état du domaine et la diffusion de l'événement restent toujours cohérents.
