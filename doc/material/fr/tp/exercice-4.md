# TP Exercice 4: Projection des événements dans des vues matérialisées

L'objectif de cet exercice est de comprendre le concept de projection des événements dans des vues matérialisées. Vous apprendrez à analyser une base de code existante et à répondre à des questions sur son fonctionnement.

**temps estimé** : 1 heure

**difficulté** : moyenne

## Tâche 1 : Questions sur la base de code

1. Expliquer le rôle de l'interface `Projector` dans le système de gestion des événements.

2. Expliquer le rôle du type `S` dans l'interface `Projector`.

3. Compléter la Javadoc de l'interface `Projector` en ajoutant la description de `S`.

4. Quel est l'intérêt de passer par une interface `Projector` plutôt que d'utiliser une classe concrète ?

5. Quel est le rôle de la classe `ProjectionResult` dans l'interface `Projector` ?

:::tip
Chercher à quoi correspond le terme `Monade` sur le web.
:::

6. Expliquer en quoi l'usage de la Monade est intéressant par rapport à la méthode de gestion d'erreur traditionnelle en Java et détailler les avantages concrets.

## Tâche 2 : Questions concernant l'Outboxing

1. Expliquer le rôle de l'interface `OutboxRepository` dans le système de gestion des événements.

2. Expliquer comment l'Outbox Pattern permet de garantir la livraison des événements dans un système distribué.

3. En analysant le code existant, décrire le fonctionnement de l'Outbox Pattern concrètement dans le contexte de l'application. Créez un diagramme pour illustrer le flux des événements. Créez un diagramme de séquence pour montrer le séquencement des interactions entre les différents composants. Précisez les intéractions transactionnelles.

4. Expliquer comment l'Outbox Pattern peut être utilisé pour gérer les erreurs de livraison des événements dans cette base de code. Référez-vous ici au schéma de données dans les fichiers XML liquibase et aux implémentations concrètes.

## Tâche 3 : Questions concernant le journal d'évènements

1. Expliquer le rôle du journal d'événements dans le système de gestion des événements.

2. Pourquoi l'interface `EventLogRepository` ne comporte-t-elle qu'une seule méthode `append` ? Pourquoi n'y a-t-il pas de méthode pour récupérer les événements ou les supprimer ?

3. En tirant vos conclusions de votre réponse à la question 2 et de l'analyse de l'application (Objets liés à l'event log, schéma de base de données), déterminez les implications de cette conception sur la gestion des événements dans l'application et quelles pourraient être les autres usages du journal d'événements.

## Tâche 4 : Limites de CQRS

1. Identifier et expliquer les principales limites de l'architecture CQRS dans le contexte de l'application.

2. Quelles limites intrinsèques à CQRS sont déjà compensées par la mise en œuvre actuelle de l'application ?

3. Quelles autres limites pourraient être introduites par cette mise en œuvre ?

4. Que se passerait-il dans le cas d'une projection multiple (un évènement donnant lieu à plusieurs actions conjointes mais de nature différente) ?

5. Question bonus : Proposez des solutions pour atténuer les limites identifiées dans les questions précédentes (notamment la question 3).
