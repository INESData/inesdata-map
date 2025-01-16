import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { MenuService } from 'src/app/shared/services/menu.service';

import { LanguageService } from '../../services/language.service';

/**
 * Header component
 */
@Component({
	selector: 'app-header',
	templateUrl: './header.component.html',
	styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
	items: MenuItem[];

	/**
	 * Component constructor
	 *
	 * @param menuService Menu service
	 * @param languageService Language service
	 */
	constructor(
		public menuService: MenuService,
		public languageService: LanguageService
	) {}

	ngOnInit() {
		this.items = [
			{
				label: 'Home',
				icon: 'pi pi-fw pi-home',
				routerLink: ['/home']
			},
			{
				label: 'Profile',
				icon: 'pi pi-fw pi-user',
				routerLink: ['/profile']
			},
			{
				label: 'Settings',
				icon: 'pi pi-fw pi-cog',
				routerLink: ['/settings']
			}
		];
	}
}
