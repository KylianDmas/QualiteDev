import { Component } from "@angular/core";

@Component({
    selector: "app-about",
    template: `
        <h2 class="text-2xl font-bold text-primary-500">Orderflow Store Front</h2>
        <p>
            Orderflow Store Front is a demonstration tool for showcasing the capabilities of the Orderflow platform.
            It provides a user-friendly interface for managing and visualizing order flows objects.
        </p>
        <p>
            This application is built using Angular and showcases various features. It serves mainly as an administration tool.
        </p>
        <p class="text-sm text-gray-500 mt-4">
            @Copyright 2025 Thibaud FAURIE. All rights reserved.
        </p>
    `,
    host: {
        'class': 'flex flex-col gap-y-4 p-4 bg-gray-100'
    }
})
export class About {}