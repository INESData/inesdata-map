import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { filter, map } from 'rxjs';

import { LanguageService } from '../../services/language.service';
import { HOME, LABELS_HOME, URL_EDIT } from '../../utils/app.constants';

/**
 * BreadcrumbComponent is responsible for displaying breadcrumb navigation in the application.
 * It listens to router events and updates the breadcrumb items accordingly.
 */
@Component({
	selector: 'app-breadcrumb',
	templateUrl: './breadcrumb.component.html',
	styleUrls: ['./breadcrumb.component.scss']
})
export class BreadcrumbComponent implements OnInit {
	items: MenuItem[] = [];
	home: MenuItem = {
		label: this.languageService.translateValue(LABELS_HOME),
		routerLink: HOME
	};

	constructor(
		private router: Router,
		private activatedRoute: ActivatedRoute,
		private languageService: LanguageService
	) { }

	/**
	 * Initializes the component and sets up the breadcrumb navigation on router events.
	 *
	 * @returns {void}
	 */
	ngOnInit(): void {
		this.router.events
			.pipe(
				// Filter only NavigationEnd events
				filter((event) => event instanceof NavigationEnd),
				// Build breadcrumb from activated route
				map(() => this.buildBreadcrumb(this.activatedRoute.root))
			)
			.subscribe((breadcrumbs) => {
				// Check if route is valid
				this.setBreadcrumbIfValidRoute(breadcrumbs);
			});
	}

	/**
	 * Build the breadcrumb navigation items for the given route.
	 *
	 * @param {ActivatedRoute} route - The currently activated route.
	 * @param {string} [url=''] - The base URL to be appended with each route segment.
	 * @param {MenuItem[]} [breadcrumbs=[]] - The array of breadcrumb items to be updated.
	 * @returns {MenuItem[]} - The updated array of breadcrumb items.
	 */
	private buildBreadcrumb(route: ActivatedRoute, url = '', breadcrumbs: MenuItem[] = []): MenuItem[] {
		// Get activated route
		const children: ActivatedRoute[] = route.children;

		if (children.length === 0) {
			return breadcrumbs;
		}

		children.forEach(child => {
			// Build route
			const routeURL: string = child.snapshot.url.map((segment) => segment.path).join('/');
			// Normalize the routeURL if it contains 'edit'
			const breadcrumbURL = routeURL.includes(URL_EDIT) ? URL_EDIT : routeURL;

			if (breadcrumbURL) {
				const newUrl = `${url}/${breadcrumbURL}`;
				breadcrumbs.push({
					label: this.languageService.translateValue('sidebar.labels.' + breadcrumbURL),
					routerLink: newUrl
				});
			}

			// Build breadcrumbs for child routes
			this.buildBreadcrumb(child, url ? `${url}/${routeURL}` : routeURL, breadcrumbs);
		});

		return breadcrumbs;
	}

	/**
	 * Sets the breadcrumb items if the current route is valid.
	 *
	 * @param {MenuItem[]} breadcrumbs - The array of breadcrumb items to be set.
	 * @returns {void}
	 */
	private setBreadcrumbIfValidRoute(breadcrumbs: MenuItem[]): void {
		this.items = [];
		// If route is home leave empty
		if (this.router.url === HOME) {
			return;
		}
		// Get routes in routing
		const definedRoutes = this.router.config.map((route) => route.path);
		// Check if first part of current URL  is in routing
		const isValidRoute = definedRoutes.includes(this.router.url.split('/')[1]);

		if (isValidRoute) {
			this.items = breadcrumbs;
		}
	}
}
