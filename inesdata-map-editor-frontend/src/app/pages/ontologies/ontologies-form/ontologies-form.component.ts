import { Component, DestroyRef, EventEmitter, inject, Input, OnInit, Output } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormBuilder, FormGroup } from '@angular/forms';
import { OntologyDTO, OntologyService, SearchOntologyDTO } from 'projects/mapper-api-client';
import { ontologyDtoForm } from 'projects/mapper-forms/src/public-api';
import { LanguageService } from 'src/app/shared/services/language.service';
import { NotificationService } from 'src/app/shared/services/notification.service';
import {
	LABELS_NO_FILE_SELECTED,
	MESSAGES_ERRORS_REQUIRED,
	MESSAGES_ONTOLOGIES_SUCCESS_CREATED,
	MESSAGES_ONTOLOGIES_SUCCESS_UPDATED
} from 'src/app/shared/utils/app.constants';
import { createDtoForm } from 'src/app/shared/utils/form.utils';

@Component({
	selector: 'app-ontologies-form',
	templateUrl: './ontologies-form.component.html'
})
export class OntologiesFormComponent implements OnInit {
	destroyRef = inject(DestroyRef);
	fileName: string = this.languageService.translateValue(LABELS_NO_FILE_SELECTED);
	fileSelected = false;

	@Output() formSubmitted = new EventEmitter<void>();
	@Input() isEditMode = false;

	file: File;
	ontologyForm: FormGroup = null;
	private _ontology: SearchOntologyDTO = null;

	/**
	 * This setter updates the _ontology property
	 * whenever a new value is passed from the parent component.
	 */
	@Input() set ontology(value: SearchOntologyDTO) {
		this._ontology = value;
		if (this.ontologyForm) {
			if (value) {
				this.ontologyForm.patchValue(value);
			} else {
				this.ontologyForm.reset();
			}
		}
	}

	/**
	 * Constructor
	 * @param fb the form builder
	 */
	constructor(
		private fb: FormBuilder,
		private languageService: LanguageService,
		private ontologyService: OntologyService,
		private notificationService: NotificationService
	) {}

	/**
	 * On component init
	 */
	ngOnInit() {
		this.ontologyForm = createDtoForm(this.fb, ontologyDtoForm);
	}

	/**
	 * Create new ontology
	 */
	addOntology(ontology: OntologyDTO): void {
		ontology.uploadDate = new Date().toISOString();
		this.ontologyService
			.createOntology(ontology, this.file)
			.pipe(takeUntilDestroyed(this.destroyRef))
			.subscribe(() => {
				this.formSubmitted.emit();
				this.notificationService.showSuccess(MESSAGES_ONTOLOGIES_SUCCESS_CREATED);
			});
	}

	/**
	 * Update ontology
	 */
	updateOntology(id: number, ontology: OntologyDTO): void {
		this.ontologyService
			.updateOntology(id, ontology)
			.pipe(takeUntilDestroyed(this.destroyRef))
			.subscribe(() => {
				this.formSubmitted.emit();
				this.notificationService.showSuccess(MESSAGES_ONTOLOGIES_SUCCESS_UPDATED);
			});
	}

	/**
	 * Extract selected file
	 */
	onFileSelected(event) {
		this.file = event.target.files[0];
		if (this.file) {
			this.fileName = this.file.name;
			this.fileSelected = true;
		} else {
			this.resetFile();
		}
	}

	/**
	 * Reset file to initial state
	 */
	resetFile(): void {
		this.fileName = this.languageService.translateValue(LABELS_NO_FILE_SELECTED);
		this.fileSelected = false;
		this.file = null;
	}

	/**
	 * Get error messages
	 */
	getErrorMessage(controlName: string): string {
		const control = this.ontologyForm.get(controlName);
		if (control?.errors) {
			if (control.errors.required) {
				return this.languageService.translateValue(MESSAGES_ERRORS_REQUIRED);
			}
		}
		return '';
	}

	/**
	 * Check if a control has validation errors
	 */
	hasError(controlName: string): boolean {
		const control = this.ontologyForm.get(controlName);
		return control?.invalid && (control?.touched || control?.dirty);
	}

	/**
	 * On form submission
	 */
	onSubmit(): void {
		// Mark all fields as touched to trigger validation messages
		this.ontologyForm.markAllAsTouched();

		// Check if the form is valid
		if (this.ontologyForm.invalid) {
			return;
		}

		// If the form is valid, proceed with the submission
		const ontology: OntologyDTO = this.ontologyForm.value;
		if (this.isEditMode) {
			this.updateOntology(ontology.id, ontology);
		} else {
			this.addOntology(ontology);
		}
		this.ontologyForm.reset();
		this.resetFile();
	}
}
