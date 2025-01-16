import { Pipe, PipeTransform } from '@angular/core';
import { LanguageService } from 'src/app/shared/services/language.service';

import { LABELS_NO, LABELS_YES } from '../utils/app.constants';

/**
 * Pipe: Boolean to yes / no
 *
 * Converts a boolean into a string
 */
@Pipe({
	name: 'boolToYesNo'
})
export class BoolToYesNoPipe implements PipeTransform {
	constructor(private languageService: LanguageService) {}

	/**
	 * Transforms boolean to string
	 *
	 * @param value the boolean
	 * @returns transform the string
	 */
	transform(value: boolean): string {
		return value ? this.languageService.translateValue(LABELS_YES) : this.languageService.translateValue(LABELS_NO);
	}
}
