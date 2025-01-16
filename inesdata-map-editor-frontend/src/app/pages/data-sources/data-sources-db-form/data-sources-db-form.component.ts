import { Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { LanguageService } from 'src/app/shared/services/language.service';
import { getErrorMessage, hasError } from 'src/app/shared/utils/errors.utils';

@Component({
	selector: 'app-data-sources-db-form',
	templateUrl: './data-sources-db-form.component.html'
})
export class DataSourcesDbFormComponent {
	@Input() dbSourceForm: FormGroup = null;

	constructor(private languageService: LanguageService) { }

	/**
	 * Check if a form control has an error.
	 */
	checkError(controlName: string, dbSourceForm: FormGroup): boolean {
		return hasError(controlName, dbSourceForm);
	}

	/**
	 * Get the error message for a form control.
	 */
	showErrorMessage(controlName: string, dbSourceForm: FormGroup): string {
		return getErrorMessage(controlName, dbSourceForm, this.languageService);
	}
}
