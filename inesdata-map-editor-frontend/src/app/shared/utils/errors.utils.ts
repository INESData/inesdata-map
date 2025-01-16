import { FormGroup } from '@angular/forms';

import { LanguageService } from '../services/language.service';
import { MESSAGES_ERRORS_REQUIRED } from './app.constants';

/**
 * Check if a control has validation errors
 */
export function hasError(controlName: string, form: FormGroup): boolean {
	const control = form.get(controlName);
	return control?.invalid && (control?.touched || control?.dirty);
}

/**
 * Get error messages
 */
export function getErrorMessage(controlName: string, form: FormGroup, languageService: LanguageService): string {
	const control = form.get(controlName);
	if (control?.errors) {
		if (control.errors.required) {
			return languageService.translateValue(MESSAGES_ERRORS_REQUIRED);
		}
	}
	return '';
}
