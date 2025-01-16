import { HttpErrorResponse } from '@angular/common/http';
import { ErrorHandler, Injectable } from '@angular/core';
import { LanguageService } from 'src/app/shared/services/language.service';

import { NotificationService } from '../services/notification.service';
import { SpinnerService } from '../services/spinner.service';
import { MESSAGES_ERRORS_GENERIC, MESSAGES_ERRORS_INTERNET_CONNECTION } from '../utils/app.constants';

/**
 * Global error handler
 */
@Injectable()
export class GlobalErrorHandlerService implements ErrorHandler {
	/**
	 * Handler constructor
	 *
	 * @param spinnerService Spinner service
	 * @param notificationService Notification service
	 * @param languageService Language service
	 */
	constructor(
		private spinnerService: SpinnerService,
		private notificationService: NotificationService,
		private languageService: LanguageService
	) {}

	/**
	 * Handle global errors
	 *
	 * @param error Error
	 */
	handleError(error: Error | HttpErrorResponse) {
		console.error(error);
		if (error instanceof HttpErrorResponse) {
			if (!navigator.onLine) {
				// Offline error
				this.notificationService.showErrorMessage(this.languageService.translateValue(MESSAGES_ERRORS_INTERNET_CONNECTION));
			} else {
				// Unprocessed errors
				this.notificationService.showErrorException(error);
			}
		} else {
			// Other error
			this.notificationService.showErrorMessage(this.languageService.translateValue(MESSAGES_ERRORS_GENERIC));
		}

		// Force stop spinner if is running
		this.spinnerService.stopSpinner();
	}
}
