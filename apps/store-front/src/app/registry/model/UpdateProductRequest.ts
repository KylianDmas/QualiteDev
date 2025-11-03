export interface UpdateProductRequest {
    id: string;
    operations: UpdateProductOperation[];
}

export interface UpdateProductOperation {
    type: UpdateProductOperationType;
    payload: UpdateProductPayload;
}

export interface UpdateProductNamePayload {
    name: string;
}

export interface UpdateProductDescriptionPayload {
    description: string;
}

type UpdateProductPayload = UpdateProductNamePayload | UpdateProductDescriptionPayload;

export enum UpdateProductOperationType {
    UPDATE_NAME = 'UpdateProductName',
    UPDATE_DESCRIPTION = 'UpdateProductDescription'
}