import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { LanguageService } from 'src/app/shared/services/language.service';
import { FORM_CONTROL_ID, LABELS_NO_FILE_SELECTED } from 'src/app/shared/utils/app.constants';
import { getErrorMessage, hasError } from 'src/app/shared/utils/errors.utils';

@Component({
	selector: 'app-data-sources-file-form',
	templateUrl: './data-sources-file-form.component.html'
})
export class DataSourcesFileFormComponent {
	file: File;
	fileName: string = this.languageService.translateValue(LABELS_NO_FILE_SELECTED);
	fileSelected = false;

	@Input() fileSourceForm: FormGroup = null;
	@Output() fileSelectedEvent: EventEmitter<File> = new EventEmitter<File>();

	constructor(private languageService: LanguageService) {}

	/**
	 * Extract selected file
	 */
	onFileSelected(event) {
		this.file = event.target.files[0];
		if (this.file) {
			this.fileName = this.file.name;
			this.fileSelected = true;
			this.fileSelectedEvent.emit(this.file);
		} else {
			this.fileName = this.languageService.translateValue(LABELS_NO_FILE_SELECTED);
			this.fileSelected = false;
		}
	}

	/**
	 * Check if a form control has an error.
	 */
	checkError(controlName: string, fileSourceForm: FormGroup): boolean {
		return hasError(controlName, fileSourceForm);
	}

	/**
	 * Get the error message for a form control.
	 */
	showErrorMessage(controlName: string, fileSourceForm: FormGroup): string {
		return getErrorMessage(controlName, fileSourceForm, this.languageService);
	}

	/**
	 * Check if the form is in edit mode.
	 */
	isEditMode() {
		return this.fileSourceForm.get(FORM_CONTROL_ID).value !== null;
	}
}
