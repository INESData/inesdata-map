import { Component } from '@angular/core';

/**
 * Footer component
 */
@Component({
	selector: 'app-footer',
	templateUrl: './footer.component.html',
	styleUrls: ['./footer.component.scss']
})
export class FooterComponent {
	year: number;

	/**
	 * Component constructor
	 */
	constructor() {
		this.year = new Date().getFullYear();
	}
}
