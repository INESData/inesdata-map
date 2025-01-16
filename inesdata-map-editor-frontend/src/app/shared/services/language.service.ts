import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import { LANGUAGE_STORAGE_NAME } from '../utils/app.constants';

/**
 * Language service management
 */
@Injectable({
	providedIn: 'root'
})
export class LanguageService {
	/**
	 * Language service constructor
	 *
	 * @param translateService Translate service
	 */
	constructor(private translateService: TranslateService) {}

	/**
	 * Gets the list of available languages
	 *
	 * @returns Languages list
	 */
	getLanguages() {
		return this.translateService.langs;
	}

	/**
	 * Set the selected language to the service and store in localStorage
	 *
	 * @param lang Language to use
	 */
	setLanguage(lang: string) {
		this.translateService.use(lang);
		this.translateService.setDefaultLang(lang);
		localStorage.setItem(LANGUAGE_STORAGE_NAME, lang);
	}

	/**
	 * Gets the current selected language
	 *
	 * @returns Current language
	 */
	getCurrentLanguage() {
		return this.translateService.currentLang;
	}

	/**
	 * Translates the key using the service
	 *
	 * @param key the value
	 * @param [params] the parameters to interpolate
	 * @returns Translation
	 */
	translateValue(key: string, params?: unknown) {
		return this.translateService.instant(key, params);
	}
}
