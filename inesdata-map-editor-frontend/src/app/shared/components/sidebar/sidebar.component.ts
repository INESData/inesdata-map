import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';

import { LanguageService } from '../../services/language.service';

/**
 * SideBarComponent is responsible for displaying the sidebar navigation menu.
 * It initializes the menu items used for navigation within the application.
 */

@Component({
	selector: 'app-sidebar',
	templateUrl: './sidebar.component.html',
	styleUrls: ['./sidebar.component.scss']
})
export class SideBarComponent implements OnInit {

	menuItems: MenuItem[];

	constructor(private languageService: LanguageService) { }

	/**
	 * Initializes the component and sets up the sidebar menu items.
	 *
	 * @returns {void}
	 */
	ngOnInit() {
		this.menuItems = [
			{
				label: this.languageService.translateValue('sidebar.labels.ontologies'),
				icon: 'pi pi-folder',
				routerLink: '/ontologies'
			},
			{
				label: this.languageService.translateValue('sidebar.labels.data-sources'),
				icon: 'pi pi-file',
				routerLink: '/data-sources'
			},
			{
				label: this.languageService.translateValue('sidebar.labels.mappings'),
				icon: 'pi pi-list',
				routerLink: '/mappings'
			}
		];
	}

}
