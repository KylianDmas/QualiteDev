import { Routes } from "@angular/router";

export const routes: Routes = [
    {
        path: "products",
        children: [
            {
                path: "",
                loadComponent: () => import("./products/products.component").then(m => m.Products)
            },
            {
                path: "new",
                loadComponent: () => import("./register-product/register-product.component").then(m => m.RegisterProduct)
            },
            {
                path: ":id",
                loadComponent: () => import("./product/product.component").then(m => m.Product)
            }
        ]
    }
]