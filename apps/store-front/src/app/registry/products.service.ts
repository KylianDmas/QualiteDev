import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { map, Observable, Subscriber } from "rxjs";
import { PaginatedProductList } from "./model/PaginatedProductList";
import { ProductStreamElement } from "./model/ProductStreamElement";
import { ProductView } from "./model/ProductView";
import { RegisterProductParams } from "./model/RegisterProduct";
import { UpdateProductRequest } from "./model/UpdateProductRequest";

@Injectable({ providedIn: "platform" })
export class ProductsService {
    static BASE_URL = "/api/products";
    static ENDPOINTS = {
        VIEW: `${ProductsService.BASE_URL}/viewProduct`,
        REGISTER: `${ProductsService.BASE_URL}/registerProduct`,
        UPDATE: `${ProductsService.BASE_URL}/updateProduct`,
        RETIRE: `${ProductsService.BASE_URL}/retireProduct`,
        SEARCH: `${ProductsService.BASE_URL}/searchProducts`,
        STREAM: `${ProductsService.BASE_URL}/streamProductEvents`,
        STREAM_BY_ID: `${ProductsService.BASE_URL}/streamProductEventsById`
    }
    
    private http = inject(HttpClient);

    searchProducts(sku: string, page: number, size: number): Observable<PaginatedProductList> {
        return this.http.post<PaginatedProductList>(ProductsService.ENDPOINTS.SEARCH, { page, size, sku: sku !== '' ? sku : undefined });
    }

    getProductById(id: string) {
        return this.http.post<ProductView>(`${ProductsService.ENDPOINTS.VIEW}`, { id })
            .pipe(
                map(view => ({
                    ...view,
                    createdAt: new Date(view.createdAt),
                    updatedAt: new Date(view.updatedAt),
                    events: view.events.map(event => ({
                        ...event,
                        timestamp: new Date(event.timestamp)
                    }))
                }))
            );
    }

    updateProduct(update: UpdateProductRequest) {
        return this.http.post<ProductView>(`${ProductsService.ENDPOINTS.UPDATE}`, update);
    }

    registerProduct(product: RegisterProductParams) {
        return this.http.post<ProductView>(ProductsService.ENDPOINTS.REGISTER, product);
    }

    retireProduct(id: string) {
        return this.http.post<ProductView>(`${ProductsService.ENDPOINTS.RETIRE}`, { id });
    }

    streamPendingProducts(sku: string, page: number, size: number): Observable<ProductStreamElement> {
        const searchParams = new URLSearchParams();
        searchParams.set("sku", sku);
        searchParams.set("page", page.toString());
        searchParams.set("size", size.toString());
        const source = new EventSource(new URL(ProductsService.ENDPOINTS.STREAM + "?" + searchParams.toString()));
        return new Observable((subscriber: Subscriber<ProductStreamElement>) => {
            source.onerror = error => {
                subscriber.error(error);
            };

            source.onmessage = event => {
                const data = JSON.parse(event.data) as ProductStreamElement;
                subscriber.next(data);
            };

            return () => {
                source.close();
            };
        });
    }
}