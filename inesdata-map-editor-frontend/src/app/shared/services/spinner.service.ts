import { Injectable } from '@angular/core';
import { NGXLogger } from 'ngx-logger';

/**
 * Injectable: Spinner service
 */
@Injectable({
	providedIn: 'root'
})
export class SpinnerService {
	// Spinner state
	loading: boolean;

	/**
	 * Creates an instance of spinner service.
	 */
	constructor(private logger: NGXLogger) {
		this.loading = false;
	}

	/**
	 * Starts spinner
	 */
	startSpinner() {
		this.loading = true;
		this.logger.info('Spinner started');
	}

	/**
	 * Stops spinner
	 */
	stopSpinner() {
		this.loading = false;
		this.logger.info('Spinner stopped');
	}
}
