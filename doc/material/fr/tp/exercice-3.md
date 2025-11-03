# TP Exercice 3: Collaborez sur le monorepo

L'objectif de cet exercice est de collaborer sur le monorepo de l'application order flow. Vous devrez travailler ensemble sur la même base de code et gérer les dépendances entre les microservices.

::: tip
Pour valider les étapes de mise en œuvre, vous pouvez demander à l'autre équipe de revoir vos modifications.
Assurez-vous de communiquer avec votre responsable de TP si vous avez besoin d'une deuxième validation.
:::

::: warning
Attention aux collisions avec les modifications des membres de l'équipe. Vous devez communiquer tout au long du processus pour vous assurer que les modifications sont cohérentes dans tout le monorepo et ne sont pas en conflit. Vous pouvez utiliser l'UML pour décrire également votre solution et décider de la meilleure façon de la mettre en œuvre.

Vous pourriez rencontrer des situations où des modifications préliminaires sont nécessaires de la part d'une personne/équipe spécifique pour permettre à l'autre de mettre en œuvre ses modifications. Essayez alors de planifier les modifications de manière à pouvoir travailler en parallèle au maximum.
:::

**temps estimé** : 1 heure

**difficulté** : facile

## Tâche 1 : Choisir un modèle de contrôle de version et une stratégie de branchement

Pour faciliter la collaboration entre les membres de l'équipe, vous devez choisir un modèle de contrôle de version. En utilisant vos connaissances des systèmes de contrôle de version, vous devez choisir un modèle qui vous permettra de travailler ensemble sur la même base de code entre 2 équipes.

## Tâche 2 : Définir les responsabilités des équipes

Pour faciliter la collaboration entre les membres de l'équipe, vous devez définir les responsabilités de chaque équipe. Pour ce faire, aidez-vous des responsabilités de l'application et de l'architecture des microservices.

## Tâche 3 : README et règles de collaboration

Pour faciliter la collaboration entre les membres de l'équipe, vous devez définir les règles de collaboration. Vous devez compléter le fichier README existant si nécessaire et rédiger un fichier CONTRIBUTING.md qui expliquera comment travailler ensemble et comment communiquer entre les équipes.

::: tip
Vous pouvez utiliser [Readme.so](https://readme.so/) pour générer des fichiers markdown.
:::

## Tâche 4 : Divisez votre groupe en 2 équipes

Divisez votre groupe en 2 équipes et attribuez les responsabilités de chaque équipe. Vous devez définir les rôles de chaque membre de l'équipe et les canaux de communication entre les équipes (Trello, Mail, Slack, etc.).

::: tip
Les exercices suivants vous demanderont de réaliser les exercices en équipe.
Vous pouvez utiliser des outils de collaboration IDE comme Live Share dans Visual Studio Code pour travailler ensemble ou effectuer un pair programming plus traditionnel.
:::


## Tâche 5: Préparation

Faites une première version 0.1.0 de votre base de code en suivant vos règles de contributions et de cycle de release.

Faites une version 0.1.0 :

- Cette version sera marquée en tant que 0.x pour signifier que la stack logiciel est dans un état "précoce" du développement et ne devrait pas être utilisé en production.
- Cette version servira comme ligne de base pour votre travail collectif.

## Règles pour Fusionner les modifications

Lorsque vous devez fusionner les modifications des deux équipes et vous assurer que les modifications soient cohérentes dans tout le monorepo, n'oubliez pas de signaler vos demandes de fusion pour évaluation.

Votre processus doit être propre :

- Créer une branche pour chaque tâche/fonctionnalité
- Assurez-vous d'avoir un historique propre
- Squashez les commits si nécessaire
- Assurez-vous d'avoir une description de merge request propre
- Suivez bien le processus que vous avez établi dans l'exercice précédent

## Règles pour préparer un patch

Utilisez votre stratégie de embranchement pour préparer un patch pour les modifications que vous avez apportées.

Incrémentez la version des parties de la base de code que vous avez modifiées.

Utilisez la version sémantique pour incrémenter les numéros de version.

## Mettre à jour la documentation

Vous devez mettre à jour la documentation appropriée dans le monorepo pour refléter les modifications que vous avez apportées.

## Taguer un patch

Taguez votre patch avec le numéro de version que vous avez choisi et ajoutez une note de version pour décrire les modifications que vous avez apportées.

## Tâche 6 : Initialisez votre "journal de bord"

- Créez un dossier `journal` dans le sous-répertoire `doc` et ajoutez un fichier `README.md` à l'intérieur pour décrire le contenu du journal.
- Prévoyez une section pour documenter les décisions importantes prises lors du développement.
- Prévoyez une seconde section destinées à rassembler les réponses aux questions du TP pour l'évaluation.