import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { NotificationService } from '../services/notification.service';

/**
 * Injectable: Server error interceptor
 */
@Injectable()
export class ServerErrorInterceptor implements HttpInterceptor {
	/**
	 * Creates an instance of server error interceptor.
	 *
	 * @param notificationService the notification service
	 * @param logger the logger service
	 */
	constructor(
		private notificationService: NotificationService,
		private logger: NGXLogger
	) {}

	/**
	 * Intercepts server errors
	 *
	 * @param request the request
	 * @param next the http handler
	 * @returns the request if no errors
	 */
	intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
		this.logger.info('Referring a request to the resource ' + request.url, request);
		return next.handle(request).pipe(
			catchError((error: HttpErrorResponse) => {
				return this.handleError(error, request);
			})
		);
	}

	/**
	 * Handles the error. If a 401 error occurs, the user is authenticated again and the request is retried.
	 *
	 * @param error the error
	 * @param request the request
	 * @returns the error
	 */
	handleError(error: HttpErrorResponse, request: HttpRequest<unknown>): Observable<HttpEvent<unknown>> {
		switch (error.status) {
			case 400:
				this.logger.error('The request has not been correctly conformed; status code: ' + error.status, request, error);
				break;
			case 401:
				this.logger.error(
					'The request has not been completed because it lacks valid authentication credentials for the requested resource; status code: ' +
						error.status,
					request,
					error
				);
				break;
			case 403:
				// user without privileges
				this.logger.error(
					'The request has not been completed because the client is forbidden from the requested resource; status code: ' + error.status,
					request,
					error
				);
				this.notificationService.showErrorException(error.error);
				break;
			case 404:
				this.logger.error(
					'The request has not been completed because the requested resource is not found; status code: ' + error.status,
					request,
					error
				);
				this.notificationService.showErrorException(error.error);
				break;
			default:
				// Error managed by general error handling
				this.logger.error('An error occurred while processing your operation with status code ' + error.status, request, error);
				this.notificationService.showErrorException(error.error);
		}

		return throwError(() => error);
	}
}
