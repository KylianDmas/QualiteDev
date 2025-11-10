import { withMermaid } from 'vitepress-plugin-mermaid'

// https://vitepress.dev/reference/site-config
export default withMermaid({
  title: "R5.08 Support TD/TP",
  description: "",
  outDir: '../../public/material',
  base: '/r5.08-quali-dev-td-tp/material/',
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    search: {
      provider: 'local'
    },
    socialLinks: [
      {
        icon: {
          svg: '<svg xmlns="http://www.w3.org/2000/svg" height="150" viewBox="90 90 210 210" width="150"><defs><style>.cls-1{fill:#e24329;}.cls-2{fill:#fc6d26;}.cls-3{fill:#fca326;}</style></defs><g id="LOGO"><path class="cls-1" d="M282.83,170.73l-.27-.69-26.14-68.22a6.81,6.81,0,0,0-2.69-3.24,7,7,0,0,0-8,.43,7,7,0,0,0-2.32,3.52l-17.65,54H154.29l-17.65-54A6.86,6.86,0,0,0,134.32,99a7,7,0,0,0-8-.43,6.87,6.87,0,0,0-2.69,3.24L97.44,170l-.26.69a48.54,48.54,0,0,0,16.1,56.1l.09.07.24.17,39.82,29.82,19.7,14.91,12,9.06a8.07,8.07,0,0,0,9.76,0l12-9.06,19.7-14.91,40.06-30,.1-.08A48.56,48.56,0,0,0,282.83,170.73Z"/><path class="cls-2" d="M282.83,170.73l-.27-.69a88.3,88.3,0,0,0-35.15,15.8L190,229.25c19.55,14.79,36.57,27.64,36.57,27.64l40.06-30,.1-.08A48.56,48.56,0,0,0,282.83,170.73Z"/><path class="cls-3" d="M153.43,256.89l19.7,14.91,12,9.06a8.07,8.07,0,0,0,9.76,0l12-9.06,19.7-14.91S209.55,244,190,229.25C170.45,244,153.43,256.89,153.43,256.89Z"/><path class="cls-2" d="M132.58,185.84A88.19,88.19,0,0,0,97.44,170l-.26.69a48.54,48.54,0,0,0,16.1,56.1l.09.07.24.17,39.82,29.82s17-12.85,36.57-27.64Z"/></g></svg>'
        },
        link: 'https://gitlab.cloud0.openrichmedia.org/iuto/r5.08-quali-dev-td-tp',
        ariaLabel: 'Gitlab repository'
      }
    ]
  },
  locales: {
    root: {
      label: "Français",
      lang: "fr",
      link: '/fr',
      themeConfig: {
        nav: [
          { text: 'Accueil', link: '/fr' },
          {
            text: "Travaux dirigés", items: [
              { text: 'Liste des TD', link: '/fr/td' }
            ]
          },
          {
            text: 'Travaux pratiques', items: [
              { text: 'Page principale', link: '/fr/tp/' },
              { text: 'Présentation du projet', link: '/fr/tp/presentation-projet' },
              { text: 'Introduction', link: '/fr/tp/introduction' },
            ]
          }
        ],
        sidebar: [
          {
            base: '/fr/tp/',
            text: 'Travaux pratiques',
            items: [
              { text: 'Page principale', link: '/' },
              { text: 'Présentation du projet', link: 'presentation-projet' },
              { text: 'Introduction', link: 'introduction' },
              { text: 'Exercice 1: Analyser l\'application', link: 'exercice-1' },
              { text: 'Exercice 2: Corriger les problèmes de qualité et introduire des tests', link: 'exercice-2' },
              { text: 'Exercice 3: Collaborez sur le monorepo', link: 'exercice-3' },
              { text: 'Exercice 4: Projection des événements dans des vues matérialisées', link: 'exercice-4' },
              { text: 'Exercice 5: Corriger les problèmes de qualité au niveau du BFF et du service de lecture du registre', link: 'exercice-5' },
              { text: 'Exercice 6: Observabilité de l\'application', link: 'exercice-6' },
              { text: 'Exercice 7: Centraliser les logs et activer le monitoring', link: 'exercice-7' },
              // { text: 'Exercice 8', link: 'exercice-8' },
              // { text: 'Exercice 9', link: 'exercice-9' },
            ]
          },
          {
            base: '/fr/td/',
            text: "Travaux dirigés",
            items: [
              { text: 'Liste des TD', link: '/' },
              { text: 'TD1: Prise en main', link: 'td1' },
              { text: 'TD2: Gérer un dépôt de code', link: 'td2' },
              { text: 'TD3: Tests E2E avec Cypress', link: 'td3' },
            ]
          }
        ],
      }
    }
  },
  mermaid: {
  },
  mermaidPlugin: {
  }
})
