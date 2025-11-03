import { ProductEventType, ProductEventPayload } from "./ProductEvent";

export interface ProductView {
    id: string;
    skuId: string;
    name: string;
    status: ProductLifecycle;
    description: string;
    catalogs: { id: string; name: string }[];
    events: { type: ProductEventType; timestamp: Date, sequence: number, payload: ProductEventPayload }[];
    createdAt: Date;
    updatedAt: Date;
}

export enum ProductLifecycle {
    ACTIVE = "ACTIVE",
    RETIRED = "RETIRED"
}
