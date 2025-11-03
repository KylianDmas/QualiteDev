import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs/internal/BehaviorSubject";
import { AlertType } from "./model/AlertType";
import { Alert } from "./model/Alert";
import { Observer, Subscription } from "rxjs";

@Injectable({
    providedIn: "root"
})
export class AlertService {
    private publisher: BehaviorSubject<Alert | undefined> = new BehaviorSubject<Alert | undefined>(undefined);

    subscribe(sub: (alert: Alert | undefined) => void): Subscription {
        return this.publisher.asObservable().subscribe(sub);
    }

    private publish(message: string, type: AlertType): void {
        this.publisher.next({ message, type });
    }

    public pushSuccess(message: string): void {
        this.publish(message, AlertType.SUCCESS);
    }

    public pushError(message: string): void {
        this.publish(message, AlertType.ERROR);
    }

    public pushInfo(message: string): void {
        this.publish(message, AlertType.INFO);
    }
}
