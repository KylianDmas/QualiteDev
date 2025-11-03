import { Component, inject, signal } from "@angular/core";
import { AbstractControl, FormControl, FormGroup, ReactiveFormsModule, ValidationErrors, ValidatorFn, Validators } from "@angular/forms";
import { Router, RouterLink } from "@angular/router";
import { ProductsService } from "../products.service";
import { AlertService } from "../../common/alert.service";
import { NgClass } from "@angular/common";
import { delay, finalize } from "rxjs";

@Component({
    selector: "registry-product",
    imports: [RouterLink, ReactiveFormsModule, NgClass],
    template: `
    <div class="flex flex-col gap-4 items-start">
        <div class="flex flex-row-reverse items-end gap-4">
            <h2 class="text-2xl font-bold text-primary-500">Register new product</h2>
            <a [routerLink]="['/registry/products']" class="text-primary-400 ">Back to Products</a>
        </div>
        <p class="text-gray-600">Fill in informations about the product to be created.</p>
        <div class="w-full max-w-[500px]">
            <form class="flex flex-col gap-2" [formGroup]="form" (ngSubmit)="onSubmit()">
                <div class="flex items-center gap-2">
                    <strong>SKU Identifier:</strong>
                    <input class="border border-gray-300 rounded p-1 placeholder:text-gray-300" formControlName="productSkuId" placeholder="SKU-12345"/>
                </div>
                @if (form.get('productSkuId')?.hasError('required')) {
                    <div class="text-red-500">SKU ID is required.</div>
                }
                @if (form.get('productSkuId')?.hasError('invalidSkuId')) {
                    <div class="text-red-500">SKU ID must respect the pattern [A-Z]&lcub;3&rcub;-[0-9]&lcub;5&rcub;.</div>
                }
                <div class="flex items-center gap-2">
                    <strong>Name:</strong>
                    <input class="border border-gray-300 rounded p-1" formControlName="productName" />
                </div>
                @if (form.get('productName')?.hasError('required')) {
                    <div class="text-red-500">Name is required.</div>
                }
                <div class="flex flex-col gap-2">
                    <strong>Description:</strong>
                    <textarea class="border border-gray-300 rounded p-1" formControlName="productDescription"></textarea>
                </div>
                <button
                    type="submit"
                    class="w-[150px] mt-4 bg-primary-500 text-white px-4 py-2 rounded hover:bg-primary-600"
                    [disabled]="submitting()"
                    [ngClass]="{
                        'opacity-50': submitting(),
                        'hover:bg-primary-600': !submitting(),
                        'cursor-pointer': !submitting()
                    }"
                >Register Product</button>
            </form>
        </div>
    </div>
    `,
})
export class RegisterProduct {
    private router = inject(Router);
    private productsService = inject(ProductsService);
    private alertService = inject(AlertService);

    protected submitting = signal(false);
    protected form: FormGroup = new FormGroup({
        productSkuId: new FormControl<string>('', [
            Validators.required,
            skuIdValidator()
        ]),
        productName: new FormControl<string>('', [
            Validators.required
        ]),
        productDescription: new FormControl<string>(''),
    });

    onSubmit() {
        if (this.form.invalid) {
            console.error('Register Product Form is invalid');
            return;
        }
        this.submitting.set(true);
        this.productsService.registerProduct({
            name: this.form.get('productName')?.value,
            description: this.form.get('productDescription')?.value,
            skuId: this.form.get('productSkuId')?.value,
        })
            .pipe(
                finalize(() => this.submitting.set(false))
            )
            .subscribe({
                next: (_product) => {
                    this.alertService.pushSuccess('Product registered successfully!');
                    this.router.navigate(['/registry/products']);
                },
                error: (error) => {
                    console.error('Error registering product:', error);
                    this.alertService.pushError('Failed to register product. Please try again later.');
                }
            });
    }
}

function skuIdValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        const value = control.value;
        if (typeof value !== 'string') {
            return null;
        }
        const isValid = new RegExp('^[A-Z]{3}-[0-9]{5}$').test(value);
        return isValid ? null : { invalidSkuId: { value } };
    };
}
