import { KeyValuePipe, NgClass } from "@angular/common";
import { Component, inject, OnInit, signal, WritableSignal } from "@angular/core";
import { timer } from "rxjs";
import { AlertService } from "./alert.service";
import { Alert } from "./model/Alert";
import { AlertType } from "./model/AlertType";

@Component({
    selector: "app-toaster",
    template: `
        <div class="flex flex-col gap-4">
        @for (toast of toasts() | keyvalue: keepOrder; track toast.key) {
            <div class="rounded-sm px-4 py-2 cursor-pointer" [ngClass]="{
                'error': toast.value.type === AlertType.ERROR,
                'info': toast.value.type === AlertType.INFO,
                'success': toast.value.type === AlertType.SUCCESS,
                'slide-in-b': toast.value.state === 'enter',
                'fade-out': toast.value.state === 'leave'
            }"
            (click)="onClick(toast.key)">
                {{ toast.value.message }}
            </div>
        }
        </div>
    `,
    styles: [`
        :host {
            position: fixed;
            bottom: 20px;
            right: 20px;
        }

        @-webkit-keyframes slide-in-b {
            0% {
                -webkit-transform: translateY(100px);
                        transform: translateY(100px);
                opacity: 0;
            }
            100% {
                -webkit-transform: translateY(0);
                        transform: translateY(0);
                opacity: 1;
            }
        }
        @keyframes slide-in-b {
            0% {
                -webkit-transform: translateY(100px);
                        transform: translateY(100px);
                opacity: 0;
            }
            100% {
                -webkit-transform: translateY(0);
                        transform: translateY(0);
                opacity: 1;
            }
        }

        @-webkit-keyframes fade-out {
            0% {
                opacity: 1;
            }
            100% {
                opacity: 0;
            }
        }
        @keyframes fade-out {
            0% {
                opacity: 1;
            }
            100% {
                opacity: 0;
            }
        }

        .slide-in-b {
            -webkit-animation: slide-in-b 0.5s cubic-bezier(0.250, 0.460, 0.450, 0.940) both;
                    animation: slide-in-b 0.5s cubic-bezier(0.250, 0.460, 0.450, 0.940) both;
        }

        .fade-out {
            -webkit-animation: fade-out 0.5s ease-out both;
                    animation: fade-out 0.5s ease-out both;
        }

        .error {
            background-color: #e52b2bc7;
            color: white;
        }

        .info {
            background-color: #4385cbd2;
            color: white;
        }

        .success {
            background-color: #50d555c6;
            color: white;
        }
    `],
    imports: [KeyValuePipe, NgClass]
})
export class Toaster implements OnInit {
    protected readonly AlertType = AlertType;
    protected keepOrder = () => 0;
    private readonly DEFAULT_DISPLAY_DURATION_MS = 5000;
    private readonly FADE_OUT_DELAY_MS = 500;

    private alertService: AlertService = inject(AlertService);

    toasts: WritableSignal<Map<string, ToasterParams>> = signal(new Map());

    ngOnInit(): void {
        this.alertService.subscribe(alert => {
            if (alert) {
                this.showMessage(alert);
            }
        });
    }

    showMessage(alert: Alert) {
        const key = Math.random().toString(36).substring(2);
        this.toasts.update(toasts => new Map(toasts).set(key, { ...alert, state: 'enter' }));
        timer(this.DEFAULT_DISPLAY_DURATION_MS)
            .subscribe(() => {
                this.clearMessage(key);
            });
    }

    clearMessage(key: string) {
        this.toasts.update(toasts => {
            const newToasts = new Map(toasts);
            const toast = newToasts.get(key);
            if (toast) {
                newToasts.set(key, { ...toast, state: 'leave' });
            }
            return newToasts;
        });
        timer(this.FADE_OUT_DELAY_MS)
            .subscribe(() => {
                this.toasts.update(msgs => {
                    const newMsgs = new Map(msgs);
                    newMsgs.delete(key);
                    return newMsgs;
                });
            });
    }

    onClick(toastKey: string) {
        this.clearMessage(toastKey);
    }
}

type ToasterParams = Alert & {
    state: 'enter' | 'leave';
}