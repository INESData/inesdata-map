import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { Message } from 'primeng/api';
import { Subject } from 'rxjs';
import { environment } from 'src/environments/environment';

import { MESSAGES_ERRORS, MESSAGES_SUCCESS } from '../utils/app.constants';

/**
 * Injectable: Notification service
 *
 * Singleton service that will add to an observable the messages to display.
 * NotificationComponent will subscribe to this observable and will display the messages accordingly.
 *
 * Based on: https://angular.io/guide/component-interaction#parent-and-children-communicate-via-a-service
 */
@Injectable({ providedIn: 'root' })
export class NotificationService {
	private messageSubject: Subject<Message> = new Subject<Message>();
	notificationChange = this.messageSubject.asObservable();

	constructor(private logger: NGXLogger) {}

	/**
	 * Shows success message
	 *
	 * @param message Message detail
	 * @param [params] Parameters for the message detail
	 */
	showSuccess(message: string, summary?: string, params?: unknown) {
		this.logger.info(message);
		this.messageSubject.next({
			severity: 'success',
			summary: summary || MESSAGES_SUCCESS,
			detail: message,
			life: environment.messages.life,
			data: params
		});
	}

	/**
	 * Shows exception error message
	 *
	 * @param error Error
	 * @param summary Summary message
	 */
	showErrorException(error: HttpErrorResponse, summary?: string) {
		this.showErrorMessage(error.error ? error.error.details : error.message, summary);
	}

	/**
	 * Shows error message
	 *
	 * @param message Error message
	 * @param summary Summary message
	 */
	showErrorMessage(message: string, summary?: string) {
		this.logger.error(message);
		this.messageSubject.next({
			severity: 'error',
			summary: summary || MESSAGES_ERRORS,
			detail: message,
			life: environment.messages.life
		});
	}
}
