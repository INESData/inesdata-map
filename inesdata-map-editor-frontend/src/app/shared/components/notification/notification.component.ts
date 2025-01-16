import { Component, OnDestroy, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { Subscription } from 'rxjs';

import { LanguageService } from '../../services/language.service';
import { NotificationService } from '../../services/notification.service';

/**
 * Component: Notification component
 *
 * Will be the responsible for displaying success/error messages
 */
@Component({
	selector: 'app-notification',
	templateUrl: './notification.component.html',
	styleUrls: ['./notification.component.scss']
})
export class NotificationComponent implements OnInit, OnDestroy {
	// The subscription
	notificationChangeSubscription: Subscription;

	/**
	 * Creates an instance of notification component.
	 *
	 * @param notificationService the notification service
	 * @param messageService the PrimeNG messages service
	 * @param languageService  the language service
	 */
	constructor(
		private notificationService: NotificationService,
		private messageService: MessageService,
		private languageService: LanguageService
	) {}

	/**
	 * Init component
	 */
	ngOnInit() {
		this.subscribeToNotificationChange();
	}

	/**
	 * Subscribes to notification change
	 */
	subscribeToNotificationChange() {
		this.notificationChangeSubscription = this.notificationService.notificationChange.subscribe((message) => {
			message.detail = this.languageService.translateValue(message.detail);
			message.summary = this.languageService.translateValue(message.summary);
			this.messageService.add(message);
		});
	}

	/**
	 * Destroy component
	 */
	ngOnDestroy() {
		// unsubscribe to avoid memory leaks
		this.notificationChangeSubscription.unsubscribe();
	}
}
