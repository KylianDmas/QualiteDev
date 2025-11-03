# TD3 : Tests E2E avec Cypress

## Définitions

- Qu'est-ce qu'un test E2E ?
    - *Que veut dire E2E ?*
    - *Quelles sont ses caractéristiques principales ?*
- Quand utiliser des tests E2E ?
    - *À quel(s) besoin(s) répond ce type de test ?*
    - *Quel est sa place dans le cycle de vie et de développement ?*
    - *Avec quelle autre technique du web cette méthode de test partage-t-elle des similitudes ?*
- Comment concevoir des tests E2E ?
    - *Sur quoi portent exclusivement les tests E2E ?*
    - *Peut-on utiliser des jeux de données dynamiques ?*
- Quel est un des points importants lors du choix d'un framework de test ?

## Cypress

- Qu'est-ce que Cypress ?

## Prise en main de l'outil

### Les environnements

La méthode classique (shared repo) consiste à simplement installer cypress en dépendance de développement dans votre dépôt/module.

L’avantage de cette méthode est qu’elle permet de mettre en route rapidement
l’environnement de test mais elle crée une dépendance directe entre vos scripts cypress et votre application.

La seconde méthode (package) consiste à créer un projet séparé « <nom de l’application>-e2e » dans le même monorepo permettant ainsi la séparation des deux modules de code tout en centralisant la gestion des dépendances.

### Installation dans notre dépôt

:::tip
Pour cet exercice, nous allons utiliser la seconde méthode (package) dans le dépôt order-flow pour créer un module e2e séparé du module store-front.
:::

1. Initialiser un nouveau projet npm dans un dossier `e2e` dans le dossier des modules applicatifs :
```bash
cd apps
mkdir store-front-e2e
cd store-front-e2e
pnpm init
```

:::tip
Faites attention à renommer le champ `name` du `package.json` pour respecter la nomenclature des packages dans le monorepo.
:::

2. Ouvrir un terminal à la racine du dépôt order-flow.

3. Installer Cypress en dépendance de développement :
```bash
pnpm --filter apps-store-front-e2e add cypress --save-catalog
```

Cette commande va installer Cypress en définissant la dépendance dans le fichier `package.json` et en l'ajoutant dans le fichier `pnpm-workspace.yaml` à la section `catalog`.

On lance ensuite l'installation des binaires de Cypress :

```bash
pnpm --filter apps-store-front-e2e exec cypress install
```

Enfin, on doit installer des dépendances systèmes nécessaires au bon fonctionnement de Cypress (notamment sous Linux) :

```bash
apt install libgtk-3-0 libgbm-dev libnotify-dev libnss3 libxss1 libasound2 libxtst6 xauth xvfb
```

:::warning
Cette commande installe des paquets systèmes dans l'instance présente. Pour un fonctionnement long terme, il est préférable de les ajouter dans le Dockerfile de l'instance devcontainer. Pensez à reconstruire l'instance après modification du Dockerfile.
:::

4. Jouer Cypress pour la première fois afin de générer la structure de dossiers et fichiers nécessaires :
On ajoute un script dans le `package.json` du module e2e :
```json
"scripts": {
    "cypress:run": "cypress run --headless --browser chrome"
}
```

Puis on lance la commande :
```bash
pnpm --filter apps-store-front-e2e run cypress:run
```

:::danger
Cette commande va échouer car, dans un conteneur, il n'y a pas d'affichage graphique. Ne vous inquiétez pas, c'est normal.

Il suffit de wrapper la commande avec `xvfb-run` pour simuler un affichage graphique :
On modifie le script dans le `package.json` du module e2e :
```json
"scripts": {
    "cypress:run": "xvfb-run cypress run --headless --browser chromium"
}
```
:::

:::danger
La commande échouera la première fois car il n'y a pas encore de tests définis ni de configuration. C'est normal.

Pour initialiser la configuration, on va créer un fichier `cypress.config.js` à la racine du module e2e avec le contenu suivant :
```js
const { defineConfig } = require('cypress');
module.exports = defineConfig({
  e2e: {
    baseUrl: 'http://localhost:4200', // URL de l'application à tester
  },
});
```

On ajoutera aussi un `supportFile` :
```bash
mkdir -p apps/store-front-e2e/cypress/support
touch apps/store-front-e2e/cypress/support/e2e.js
```

:::

:::warning
Installer un navigateur compatible avec Cypress dans le conteneur devcontainer si ce n'est pas déjà fait (Chromium, Chrome, Firefox).

On utilisera Chromium dans cet exercice.

```bash
apt install chromium
```

:::

### Écriture d'un test E2E simple

1. Créer un fichier de test très simple pour vérifier que l'application se lance correctement.
Créer un fichier `home.spec.cy.js` dans le dossier `cypress/e2e` du module e2e avec le contenu suivant :
```js
describe('Page d\'accueil', () => {
    it('devrait afficher le titre correct', () => {
        cy.visit('/'); // Visite la page d'accueil
        cy.contains('h1', 'Store Management'); // Vérifie que le titre est correct
    });
});
```

2. Lancer les tests E2E :
```bash
pnpm --filter apps-store-front-e2e run cypress:run
```
Cette commande lancera Cypress en mode headless et exécutera le test défini. Vous devriez voir dans la console que le test a réussi.

<details>
<summary>Résultat attendu</summary>

```plaintext
Page d'accueil
    ✓ devrait afficher le titre correct (231ms)


  1 passing (243ms)


  (Results)

  ┌────────────────────────────────────────────────────────────────────────────────────────────────┐
  │ Tests:        1                                                                                │
  │ Passing:      1                                                                                │
  │ Failing:      0                                                                                │
  │ Pending:      0                                                                                │
  │ Skipped:      0                                                                                │
  │ Screenshots:  0                                                                                │
  │ Video:        false                                                                            │
  │ Duration:     0 seconds                                                                        │
  │ Spec Ran:     home.spec.cy.js                                                                  │
  └────────────────────────────────────────────────────────────────────────────────────────────────┘


====================================================================================================

  (Run Finished)


       Spec                                              Tests  Passing  Failing  Pending  Skipped  
  ┌────────────────────────────────────────────────────────────────────────────────────────────────┐
  │ ✔  home.spec.cy.js                          245ms        1        1        -        -        - │
  └────────────────────────────────────────────────────────────────────────────────────────────────┘
    ✔  All specs passed!                        245ms        1        1        -        -        -  
```

</details>

### Aller plus loin

Lors de l'ajout de nouveaux tests, vous pouvez utiliser les fonctionnalités suivantes de Cypress :
- Sélecteurs CSS/HTML pour cibler des éléments spécifiques.
- Commandes Cypress pour interagir avec la page (clics, saisie de texte, etc.).
- Assertions pour vérifier les résultats attendus.

De manière général, les sélecteurs HTML/CSS ne sont pas fiables à long terme. Il est préférable d'ajouter des attributs spécifiques pour les tests, par exemple `data-cy="element-name"`.

Ainsi, dans votre code de test, vous pouvez cibler les éléments de manière plus robuste :
```js
cy.get('[data-cy="element-name"]').click();
```

Enfin, n'hésitez pas à consulter la [documentation officielle de Cypress](https://docs.cypress.io/) pour découvrir toutes les fonctionnalités avancées et les meilleures pratiques pour écrire des tests E2E efficaces.
Les pratiques recommandées sont spécifiquement localisées dans la section [Best Practices](https://docs.cypress.io/app/core-concepts/best-practices).