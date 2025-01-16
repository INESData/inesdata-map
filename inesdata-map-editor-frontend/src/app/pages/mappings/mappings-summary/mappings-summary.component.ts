import { Component, DestroyRef, inject, Input, OnChanges } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Router } from '@angular/router';
import { MappingDTO, MappingService } from 'projects/mapper-api-client';
import { LanguageService } from 'src/app/shared/services/language.service';
import { NotificationService } from 'src/app/shared/services/notification.service';
import { MAPPINGS, MESSAGES_MAPPINGS_ERRORS_NONAME, MESSAGES_MAPPINGS_RULE_INCOMPLETE, MESSAGES_MAPPINGS_SUCCESS_CREATED, MESSAGES_MAPPINGS_SUCCESS_UPDATED } from 'src/app/shared/utils/app.constants';

@Component({
	selector: 'app-mappings-summary',
	templateUrl: './mappings-summary.component.html'
})
export class MappingsSummaryComponent implements OnChanges {
	destroyRef = inject(DestroyRef);

	constructor(private mappingService: MappingService, private notificationService: NotificationService,
		private router: Router, private languageService: LanguageService) { }

	mappingName: string;
	mappingBaseUrl: string;
	mappingId: number;
	mapping: unknown[] = [];
	errorMessage = '';

	@Input() mappingDTO: MappingDTO;

	/**
	 * Update mapping name and base url when the input mappingDTO changes
	 */
	ngOnChanges(changes): void {
		if (changes.mappingDTO?.currentValue) {
			this.mappingId = this.mappingDTO.id;
			this.mappingName = this.mappingDTO.name;
			this.mappingBaseUrl = this.mappingDTO.baseUrl;
		}
	}

	/**
	* Clear error message on input change
	*/
	onMappingNameChange(): void {
		if (this.mappingName.trim() !== '') {
			this.errorMessage = '';
		}
	}

	/**
	* Clear error message on input change
	*/
	onMappingBaseUrlChange(): void {
		if (this.mappingBaseUrl.trim() !== '') {
			this.errorMessage = '';
		}
	}

	/**
	 * Deletes selected predicate from mapping.
	 * If field has no predicate-object, delete field from mapping
	 */
	deletePredicateFromField(fieldIndex: number, predicateIndex: number): void {
		const field = this.mappingDTO.fields[fieldIndex];
		field.predicates.splice(predicateIndex, 1);

		if (field.predicates.length === 0) {
			this.mappingDTO.fields.splice(fieldIndex, 1);
		}
	}

	/**
	* Validate mapping, assign name and base url and concat url
	* Update if exists and create if not
	*/
	generateMapping(): void {
		// Validate mapping
		if (!this.mappingDTO?.fields?.length) {
			this.notificationService.showErrorMessage(MESSAGES_MAPPINGS_RULE_INCOMPLETE);
			return;
		}
		if (!this.mappingDTO || !this.validateAndAssignMappingName()) {
			return;
		}

		if (!this.mappingId) {
			this.createMapping();
		} else {
			this.editMapping();
		}
	}

	/**
	 * Creates a mapping
	 */
	createMapping(): void {
		this.mappingService
			.create(this.mappingDTO)
			.pipe(
				takeUntilDestroyed(this.destroyRef)
			).subscribe(() => {
				this.router.navigate([MAPPINGS]);
				this.notificationService.showSuccess(MESSAGES_MAPPINGS_SUCCESS_CREATED);
			})
	}

	/**
	 * Updates the mapping
	 */
	editMapping(): void {
		// Validate mapping
		if (!this.validateAndAssignMappingName()) {
			return;
		}

		this.mappingService
			.updateMapping(this.mappingId, this.mappingDTO)
			.pipe(
				takeUntilDestroyed(this.destroyRef)
			)
			.subscribe((data: MappingDTO) => {
				this.mappingDTO = data;
				this.router.navigate([MAPPINGS]);
				this.notificationService.showSuccess(MESSAGES_MAPPINGS_SUCCESS_UPDATED);
			})
	}

	/**
	 * Validate and assign mapping name
	 */
	validateAndAssignMappingName(): boolean {
		if (this.mappingName.trim() === '' || this.mappingBaseUrl.trim() === '') {
			this.errorMessage = this.languageService.translateValue(MESSAGES_MAPPINGS_ERRORS_NONAME);
			return false;
		}
		this.errorMessage = '';
		this.mappingDTO.name = this.mappingName;
		this.mappingDTO.baseUrl = this.mappingBaseUrl;
		return true;
	}

}
