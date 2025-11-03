# TP Exercice 6: Observabilité de l'application

L'objectif de cet exercice est de vous montrer comment améliorer l'observabilité de l'application order flow en utilisant simplement un bon format de logs et en activant des métriques.

L'observabilité est un aspect clé du développement logiciel. Elle vous permet de comprendre le comportement de votre application et de détecter rapidement les problèmes.

En clair : pas de monitoring sans bons logs ! Vous pourrez toujours ajouter autant d'outils de monitoring que vous voulez, si les logs produits par l'application ne sont pas exploitables, vous n'irez pas loin.

**temps estimé** : 2 heures

**difficulté** : moyenne

## Tâche 1 : Instaurer un format de logs structuré

La première étape pour améliorer l'observabilité de l'application est d'instaurer un format de logs structuré. Un format structuré permet de produire des logs qui sont faciles à analyser et à rechercher.

Dans Quarkus, vous pouvez configurer le format de logs en modifiant le fichier `application.yaml`. Vous devez configurer l'application pour qu'elle utilise le format JSON pour les logs.

Reportez-vous à la [documentation sur la journalisation de Quarkus](https://quarkus.io/guides/logging) pour en savoir plus sur la configuration du format de logs.

Votre objectif est de produire 2 sorties de logs :
- Une sortie console en format texte lisible par un humain (pour le développement local)
- Une sortie fichier en format JSON structuré (pour la production et la centralisation des logs)

:::tip
Le format JSON est un format de logs structuré qui permet de produire des logs qui sont faciles à analyser et à rechercher. Il est recommandé d'utiliser ce format pour la production et la centralisation des logs car il plus facile à exploiter par des outils de monitoring.
:::

:::warning
Les logs JSON que l'application produira vont s'accumuler dans un fichier. Assurez-vous de nettoyer régulièrement ce fichier pour éviter qu'il ne devienne trop volumineux. Excluez également ce fichier du contrôle de version.
:::

Une fois le format de logs configuré, exécutez l'application et générez quelques logs en effectuant des opérations via l'API ou dans le Store manager (store-front). Vérifiez que les logs sont bien produits dans les deux formats (console et fichier).

## Tâche 2 : Ajouter des logs dans la logique applicative

Les objets métiers étant des POJO, ils ne produisent pas de logs par eux-mêmes car il est impossible d'injecter les outils nécessaire sans dénaturer leur fonctionnement. Vous devez donc ajouter des logs dans la logique applicative qui les encapsulent, c'est-à-dire dans les services de l'application, qui font interface entre les contrôleurs web (resources) et les objets métiers.

Pour vous aider, certains services importants ont déjà des commentaires `TODO` indiquant où ajouter des logs. Ne vous limitez pas à ces endroits, ajoutez des logs partout où vous jugez que c'est pertinent.

## Tâche 3 : Activer les métriques

Les métriques sont des données quantitatives qui permettent de mesurer le comportement de l'application. Elles sont essentielles pour comprendre la performance de l'application et détecter les problèmes.

Quarkus intègre nativement le support de [Micrometer](https://quarkus.io/guides/micrometer) pour la collecte de métriques. Vous devez activer Micrometer dans l'application et configurer les métriques que vous souhaitez collecter.

Reportez-vous à la [documentation sur Micrometer dans Quarkus](https://quarkus.io/guides/micrometer) pour en savoir plus sur l'activation et la configuration des métriques.

Votre objectif est de collecter les métriques suivantes :
- Nombre de requêtes HTTP reçues par l'application
- Temps de réponse des requêtes HTTP
- Nombre d'erreurs HTTP (4xx et 5xx)
Une fois les métriques configurées, exécutez l'application et générez quelques requêtes HTTP via l'API ou dans le Store manager (store-front). Vérifiez que les métriques sont bien collectées en accédant à l'endpoint `/q/metrics` de l'application.

## Tâche 4 : 