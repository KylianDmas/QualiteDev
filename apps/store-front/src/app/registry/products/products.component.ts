import { Component, inject, OnInit, signal, WritableSignal } from "@angular/core";
import { RouterLink } from "@angular/router";
import { BehaviorSubject, catchError, concatMap, delay, of, range, tap } from "rxjs";
import { AlertService } from "../../common/alert.service";
import { ProductSummary } from "../model/ProductSummary";
import { ProductsService } from "../products.service";

@Component({
    selector: "registry-products",
    imports: [RouterLink],
    template: `
    <div class="flex flex-col gap-y-4">
        <h2 class="text-2xl font-bold text-primary-500">Products</h2>
        <a [routerLink]="['/registry/products/new']" class="text-primary-400 ">Register New Product</a>
        <p class="text-gray-600">List of available products in the registry.</p>
        <table class="w-full table-auto text-left">
            <thead class="uppercase text-xs text-gray-700 bg-gray-50">
                <tr class="border-b border-gray-400">
                    <th class="p-2 ">SKU</th>
                    <th class="p-2">Product Name</th>
                    <th class="p-2">Status</th>
                    <th class="p-2">Catalogs</th>
                </tr>
            </thead>
            <tbody>
                @if (products().length === 0) {
                    <tr>
                        <td colspan="4" class="p-2 text-center">No products found.</td>
                    </tr>
                }
                @else {
                    @for (item of products(); track $index) {
                        <tr class="border-b border-gray-400 hover:cursor-pointer hover:bg-gray-100" [routerLink]="['/registry/products', item.id]">
                            <td class="p-2">{{ item.skuId }}</td>
                            <td class="p-2">{{ item.name }}</td>
                            <td class="p-2">{{ item.status }}</td>
                            <td class="p-2">{{ item.catalogs }}</td>
                        </tr>
                    }
                }
            </tbody>
        </table>
    </div>
    `,
})
export class Products implements OnInit {
    private readonly DEFAULT_FETCHING_PARAMETERS = {
        skuSearch: '',
        page: 1,
        pageSize: 25
    };

    private productsService: ProductsService = inject(ProductsService);
    private alertService: AlertService = inject(AlertService);

    private fetchingParameters: BehaviorSubject<{
        skuSearch: string;
        page: number;
        pageSize: number;
    }> = new BehaviorSubject(this.DEFAULT_FETCHING_PARAMETERS);
    protected products: WritableSignal<ProductSummary[]> = signal([]);

    ngOnInit() {
        this.fetchingParameters.subscribe(params => {
            this.fetchProducts(params.skuSearch, params.page, params.pageSize)
                .subscribe(page => {
                    this.products.set(page.products);
                });
        });
    }

    fetchProducts(skuSearch: string, page: number, pageSize: number) {
        return this.productsService.searchProducts(
            skuSearch,
            page,
            pageSize
        ).pipe(
            tap(page => {
                console.debug("Fetched products:", page.products);
            }),
            catchError(err => {
                console.error("Error fetching products:", err);
                this.alertService.pushError('Failed to fetch products. Please try again later.');
                return of({ products: [], page: 1, pageSize: 25, totalElements: 0 });
            })
        )
    }
}