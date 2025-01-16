import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { NGXLogger } from 'ngx-logger';

import { LanguageService } from './language.service';

/**
 * Main menu service for main and user menus
 */
@Injectable({
	providedIn: 'root'
})
export class MenuService {
	// Menus
	mainMenu: unknown;
	userMenu: unknown;

	userSubscription: unknown;
	languageSubscription: unknown;

	/**
	 * Component constructor
	 *
	 * @param languageService Language service
	 * @param translateService Translate service
	 * @param logger Logger service
	 */
	constructor(
		private languageService: LanguageService,
		private translateService: TranslateService,
		private logger: NGXLogger
	) {}
}
