import { Component } from '@angular/core';

import { SpinnerService } from './shared/services/spinner.service';

/**
 * Main app component
 */
@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.scss']
})
export class AppComponent {
	/**
	 * Component constructor
	 *
	 * @param spinnerService Spinner service
	 */
	constructor(public spinnerService: SpinnerService) {}
}
