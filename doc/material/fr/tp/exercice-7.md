# TP Exercice 7: Centraliser les logs et activer le monitoring

Le but de cet exercice est de vous montrer comment centraliser les logs et activer le monitoring dans l'application order flow.

L'observabilité est un aspect clé du développement logiciel. Elle vous permet de comprendre le comportement de votre application et de détecter rapidement les problèmes. La centralisation des journaux et l'activation de la surveillance sont deux aspects importants de l'observabilité parce qu'ils vous donnent un aperçu du comportement de votre application de manière centralisée sans dépendre de plusieurs sources, outils ou interfaces utilisateur.

::: tip
Vous pouvez valider les étapes de l'implémentation en posant des questions à l'enseignant.
:::

**temps estimé**: 1 heure

**difficulté**: intermédiaire

## Tâche 0 : Vérifier la journalisation dans l'application

Votre première étape préliminaire consiste à vérifier la journalisation dans l'application. Vous devez vérifier les journaux produits par l'application et comprendre comment ils sont générés. Vous pouvez également vérifier que chaque partie de l'application produit des logs de manière cohérente (en particulier dans la logique métier, qui est le champ d'application le plus important).
Reportez-vous à la [documentation sur la journalisation de Quarkus](https://quarkus.io/guides/logging) pour comprendre comment configurer la journalisation dans une application Quarkus.

## Tâche 1 : Production de logs

Dans une application Quarkus, les logs sont produits par l'application et peuvent être configurés pour être envoyés vers une sortie spécifique. Par défaut, les logs sont envoyés à la console.
Vous devez configurer l'application pour qu'elle envoie les logs vers un fichier afin qu'ils puissent être collectés par un outil dédié.
Reportez-vous à la [documentation sur la journalisation de Quarkus](https://quarkus.io/guides/logging#file-log-handler) pour en savoir plus sur la journalisation dans un fichier.
Vous devrez
- Configurer l'application pour qu'elle envoie les journaux dans un fichier.
- Définir un format de journal commun pour tous les journaux.

## Tâche 2 : Centralisation des logs

Les logs sont produits par l'application et peuvent être envoyés vers une sortie spécifique. Par défaut, les logs sont envoyés à la console ou à un fichier.
Maintenant, vous allez utiliser une combinaison d'outils pour centraliser les logs produits par l'application. Vous allez utiliser les outils suivants :
- [Grafana Loki](https://grafana.com/oss/loki/) : un système d'agrégation de logs horizontalement évolutif, hautement disponible et multi-tenant inspiré de Prometheus. (Consultez le guide de démarrage [ici](https://grafana.com/docs/loki/latest/get-started/?pg=oss-loki&plcmt=resources))
- [Grafana Alloy](https://grafana.com/docs/alloy/latest/?pg=oss-loki&plcmt=resources) : une plateforme d'observabilité entièrement gérée, évolutive et sécurisée qui vous permet de visualiser, d'alerter et d'interroger vos journaux, vos mesures et vos traces avec Grafana.
- [Grafana](https://grafana.com/) : une application web open-source multiplateforme d'analyse et de visualisation interactive.

L'objectif est de permettre à votre application Quarkus de produire des logs qui seront récupérés par Promtail et envoyés à Loki. Ensuite, vous serez en mesure de visualiser les logs dans Grafana avec des étiquettes de logs automatiques, le marquage et l'indexation.

Vous devrez :
- Installer Grafana Loki et Grafana Alloy en tant que services devcontainer.
- Configurer Alloy pour définir les bonnes étiquettes et envoyer les logs à Loki.
- Se connecter à Grafana et créer une nouvelle source de données pour Loki.

## Task 3: Explorer le Monitoring temps-réel

Maintenant que vous avez centralisé les logs, vous pouvez explorer le monitoring en temps-réel avec Grafana.

Prenez quelques captures d'écran des logs produits par l'application dans Grafana. Vous pouvez aussi créer des dashboard pour visualiser les logs en temps-réel. Ajoutez quelques explications à propos des logs produits par l'application.
