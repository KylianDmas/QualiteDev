import { ProductSummary } from "./ProductSummary";

export interface PaginatedProductList {
    products: ProductSummary[];
    page: number;
    pageSize: number;
    totalElements: number;
}