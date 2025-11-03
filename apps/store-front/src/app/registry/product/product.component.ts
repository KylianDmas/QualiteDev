import { DatePipe } from "@angular/common";
import { Component, inject, signal, WritableSignal } from "@angular/core";
import { FormControl, FormGroup, ReactiveFormsModule } from "@angular/forms";
import { ActivatedRoute, Router, RouterLink } from "@angular/router";
import { catchError, Observable, of, Subject, switchMap, tap } from "rxjs";
import { AlertService } from "../../common/alert.service";
import { ProductDescriptionUpdated, ProductEvent, ProductEventType, ProductNameUpdated } from "../model/ProductEvent";
import { ProductLifecycle, ProductView } from "../model/ProductView";
import { UpdateProductOperationType } from "../model/UpdateProductRequest";
import { ProductsService } from "../products.service";

@Component({
    selector: "registry-product",
    imports: [RouterLink, DatePipe, ReactiveFormsModule],
    template: `
    <div class="flex flex-col gap-4 items-start">
        <div class="flex flex-row-reverse items-end gap-4">
            <h2 class="text-2xl font-bold text-primary-500">Product Details</h2>
            <a [routerLink]="['/registry/products']" class="text-primary-400 ">Back to Products</a>
        </div>
        <h3 class="text-xl font-bold text-primary-500">
            Product Information
            @if (product(); as product) {
                <span>({{ lowerCaseAndFirstCapitalize(product.status) }})</span>
            }
        </h3>
        <p class="text-gray-600">Detailed information about the selected product.</p>
        @if (product(); as product) {
            <div class="flex flex-col gap-2">
                <div class="flex items-center gap-2">
                    <strong>SKU Identifier:</strong>
                    <p>{{ product.skuId }}</p>
                </div>
                <div class="flex items-center gap-2">
                    <strong>Name:</strong>
                    <p>{{ product.name }}</p>
                </div>
                <div class="flex flex-col gap-2">
                    <strong>Description:</strong>
                    <p>{{ product.description }}</p>
                </div>
                <div>
                    <strong>Register date:</strong>
                    <p>{{ product.createdAt | date:'medium' }}</p>
                </div>
                <div class="flex flex-col gap-2">
                    <strong>Last update:</strong>
                    <p>{{ product.updatedAt | date:'medium' }}</p>
                </div>
            </div>
            @if (product.status === ProductLifecycle.ACTIVE) {
                <div class="flex flex-row gap-4">
                    <button (click)="editFormEnabled.set(!editFormEnabled())" class="text-primary-400 hover:cursor-pointer">
                        {{ editFormEnabled() ? 'Cancel' : 'Edit' }}
                    </button>
                    <span>|</span>
                    @if (retireConfirm()) {
                        <div class="flex flex-row gap-2">
                            <p>Are you sure you want to retire this product?</p>
                            <button (click)="retireConfirm.set(false)" class="text-gray-400 hover:cursor-pointer">
                                Cancel
                            </button>
                            <button (click)="onValidateRetire()" class="text-red-400 hover:cursor-pointer">
                                Confirm retire
                            </button>
                        </div>
                    }
                    @else {
                        <button (click)="retireConfirm.set(true)" class="text-red-400 hover:cursor-pointer">
                            Retire product
                        </button>
                    }
                </div>
            }
            @if (editFormEnabled()) {
                <form class="flex flex-col gap-2 w-[50%]" (ngSubmit)="onSubmit()" [formGroup]="editForm" [class.hidden]="!editFormEnabled()">
                    <div class="flex flex-col gap-2">
                        <label for="name" class="font-bold">Name:</label>
                        <input id="name" formControlName="productName" class="border border-gray-300 p-2 rounded-md" />
                    </div>
                    <div class="flex flex-col gap-2">
                        <label for="description" class="font-bold">Description:</label>
                        <textarea id="description" formControlName="productDescription" class="border border-gray-300 p-2 rounded-md"></textarea>
                    </div>
                    <button type="submit" class="bg-primary-500 text-white p-2 rounded-md max-w-fit px-5 hover:cursor-pointer hover:bg-primary-600">Save Changes</button>
                </form>
            }
            <hr class="w-full border-gray-300 mt-4" />
            <h3 class="text-xl font-bold text-primary-500">Event Log</h3>
            <button (click)="eventLogFolded.set(!eventLogFolded())" class="text-primary-400 hover:cursor-pointer">
                {{ eventLogFolded() ? 'Show' : 'Hide' }} Events
            </button>
            @if (!eventLogFolded()) {
                <div class="flex flex-col gap-2">
                    @for (event of getSortedEvents(product); track $index) {
                        <li class="flex flex-col list-none p-4 bg-gray-100 rounded-md">
                            <div><strong>{{ event.title }}</strong> (Date: {{ event.timestamp | date:'medium' }}, Sequence: {{ event.sequence }})</div>
                            @for (line of event.description; track $index) {
                                <p>{{ line }}</p>
                            }
                        </li>
                    }
                </div>
            }
        } @else {
            @if (message()) {
                <p>{{ message() }}</p>
            } @else {
                <p>No product selected.</p>
            }
        }
    </div>
    `,
})
export class Product {
    protected readonly ProductLifecycle = ProductLifecycle;
    private route = inject(ActivatedRoute);
    private router = inject(Router);
    private productsService = inject(ProductsService);
    private alertService = inject(AlertService);

    private reload = new Subject<void>();

    protected product: WritableSignal<ProductView | undefined> = signal(undefined);
    protected message: WritableSignal<string | null> = signal(null);
    protected eventLogFolded: WritableSignal<boolean> = signal(false);
    protected editFormEnabled: WritableSignal<boolean> = signal(false);
    protected editForm: FormGroup = new FormGroup({
        productName: new FormControl<string | null>(null),
        productDescription: new FormControl<string | null>(null),
    });
    protected retireConfirm: WritableSignal<boolean> = signal(false);

    // Product ID
    id = "";

    ngOnInit() {
        this.reload
            .pipe(
                switchMap(() => this.getProduct(this.id))
            ).subscribe(product => {
                this.product.set(product);
                this.editForm.patchValue({
                    productName: this.product()?.name ?? null,
                    productDescription: this.product()?.description ?? null,
                });
            });
        this.route.params.subscribe(params => {
            if (params['id']) {
                this.id = params['id'];
                this.reload.next();
                return;
            }
            this.router.navigate(['/400']);
        });
    }

    getSortedEvents(product: ProductView) {
        return product.events
            .map(event => this.buildEventView(event))
            .sort((a, b) => b.timestamp.getTime() - a.timestamp.getTime());
    }

    getLastEventTimestamp(product: ProductView) {
        const events = this.getSortedEvents(product);
        return events.length > 0 ? events[events.length - 1].timestamp : null;
    }

    getRegisterDate(product: ProductView) {
        return product.events.find(event => event.type === ProductEventType.ProductRegistered)?.timestamp ?? null;
    }

    getProduct(id: string): Observable<ProductView | undefined> {
        return this.productsService.getProductById(id)
            .pipe(
                tap(product => {
                    console.debug('Fetched product:', product);
                }),
                catchError(err => {
                    console.error('Error fetching product:', err);
                    const message = 'Failed to load product details. Please try again later.';
                    this.message.set(message);
                    this.alertService.pushError(message);
                    return of(undefined);
                })
            );
    }

    buildEventView(event: ProductEvent): {
        title: string;
        description: string[];
        timestamp: Date;
        sequence: number;
    } {
        let title: string;
        let description: string[];
        switch (event.type) {
            case ProductEventType.ProductRegistered:
                title = `Product Registered`;
                description = [`Product was registered.`];
                break;
            case ProductEventType.ProductRetired:
                title = `Product Retired`;
                description = [`Product was retired.`];
                break;
            case ProductEventType.ProductNameUpdated:
                title = 'Product Name Updated';
                description = [`Renamed ${(event.payload as ProductNameUpdated).oldName} to ${(event.payload as ProductNameUpdated).newName}`];
                break;
            case ProductEventType.ProductDescriptionUpdated:
                title = 'Product Description Updated';
                description = [
                    'Updated description from ',
                    `${(event.payload as ProductDescriptionUpdated).oldDescription}`,
                    ' to ',
                    `${(event.payload as ProductDescriptionUpdated).newDescription}`];
                break;
            default:
                throw new Error(`Unknown event type: ${event.type}`);
        }
        return { title, description, timestamp: event.timestamp, sequence: event.sequence };
    }

    onSubmit() {
        if (this.editForm.invalid) {
            console.error('Edit Product Form is invalid');
            return;
        }
        const newName: string = this.editForm.value.productName;
        const newDescription: string = this.editForm.value.productDescription;
        this.productsService.updateProduct({
            id: this.id,
            operations: [
                ...(newName !== this.product()?.name ? [{
                    type: UpdateProductOperationType.UPDATE_NAME,
                    payload: {
                        name: newName
                    }
                }] : []),
                ...(newDescription !== this.product()?.description ? [{
                    type: UpdateProductOperationType.UPDATE_DESCRIPTION,
                    payload: {
                        description: newDescription
                    }
                }] : [])
            ]
        })?.subscribe({
            next: () => {
                this.editFormEnabled.set(false);
                this.product.update(product => {
                    if (!product) {
                        return product;
                    }
                    return {
                        ...product,
                        name: newName,
                        description: newDescription
                    }
                });
            },
            error: (error) => {
                console.error('Error updating product:', error);
                this.alertService.pushError('Failed to update product. Please try again later.');
            }
        });
    }

    onValidateRetire() {
        this.productsService.retireProduct(this.id).subscribe({
            next: () => {
                this.alertService.pushSuccess('Product retired successfully.');
                this.router.navigate(['/registry/products']);
            },
            error: (error) => {
                console.error('Error retiring product:', error);
                this.alertService.pushError('Failed to retire product. Please try again later.');
            }
        });
    }

    lowerCaseAndFirstCapitalize(string: string) {
        return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
    }
}
