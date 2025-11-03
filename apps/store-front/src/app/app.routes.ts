import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'registry/products',
        pathMatch: 'full'
    },
    {
        path: 'registry',
        loadChildren: () => import('./registry/registry.module').then(m => m.RegistryModule)
    },
    {
        path: 'about',
        loadComponent: () => import('./about/about.component').then(m => m.About)
    }
];
