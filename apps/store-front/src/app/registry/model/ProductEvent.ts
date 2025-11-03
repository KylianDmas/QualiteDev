export enum ProductEventType {
    ProductRegistered = "ProductRegistered",
    ProductRetired = "ProductRetired",
    ProductNameUpdated = "ProductNameUpdated",
    ProductDescriptionUpdated = "ProductDescriptionUpdated"
}

export interface ProductEmptyPayload {}

export interface ProductNameUpdated {
    oldName: string;
    newName: string;
}

export interface ProductDescriptionUpdated {
    oldDescription: string;
    newDescription: string;
}

export type ProductEventPayload = ProductNameUpdated | ProductDescriptionUpdated | ProductEmptyPayload;

export interface ProductEvent {
    type: ProductEventType;
    timestamp: Date;
    sequence: number;
    payload?: ProductEventPayload;
}