import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { routes } from "./registry.routes";
import { ProductsService } from "./products.service";

@NgModule({
    imports: [RouterModule.forChild(routes)],
    providers: [ProductsService]
})
export class RegistryModule {}