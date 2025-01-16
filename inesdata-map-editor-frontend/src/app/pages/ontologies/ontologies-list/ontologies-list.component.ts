import { Component, DestroyRef, inject, OnInit } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { OntologyService, PageSearchOntologyDTO, SearchOntologyDTO } from 'projects/mapper-api-client';
import { LanguageService } from 'src/app/shared/services/language.service';
import { NotificationService } from 'src/app/shared/services/notification.service';
import {
	MESSAGES_ONTOLOGIES_SUCCESS_DELETED,
	ONTOLOGIES_ADD_ONTOLOGY,
	ONTOLOGIES_EDIT_ONTOLOGY,
	PAGE,
	SIZE
} from 'src/app/shared/utils/app.constants';

@Component({
	selector: 'app-ontologies-list',
	templateUrl: './ontologies-list.component.html'
})
export class OntologiesListComponent implements OnInit {
	destroyRef = inject(DestroyRef);

	constructor(
		private ontologyService: OntologyService,
		private languageService: LanguageService,
		private notificationService: NotificationService
	) {}

	paginationInfo: PageSearchOntologyDTO;
	ontologies: SearchOntologyDTO[];
	selectedOntology: SearchOntologyDTO = null;
	header = '';
	isEditMode = false;
	visible = false;
	deleteDialogVisible = false;

	/**
	 * Loads the ontologies when the component is initialized
	 */
	ngOnInit(): void {
		this.loadOntologies(PAGE, SIZE);
	}

	/**
	 * Shows the dialog for adding/editing an ontology
	 */
	showDialog(ontology?: SearchOntologyDTO): void {
		this.selectedOntology = ontology ? { ...ontology } : null;
		this.isEditMode = !!ontology;
		this.header = ontology
			? this.languageService.translateValue(ONTOLOGIES_EDIT_ONTOLOGY)
			: this.languageService.translateValue(ONTOLOGIES_ADD_ONTOLOGY);
		this.visible = true;
	}

	/**
	 * Display delete dialog
	 */
	showDialogDelete(ontology: SearchOntologyDTO): void {
		this.selectedOntology = ontology;
		this.deleteDialogVisible = true;
	}

	/**
	 * Close delete dialog
	 */
	cancelDelete(): void {
		this.deleteDialogVisible = false;
	}

	/**
	 * Loads the ontologies list.
	 */
	loadOntologies(page: number, size: number): void {
		this.ontologyService
			.listOntologies(page, size)
			.pipe(
				takeUntilDestroyed(this.destroyRef)
			)
			.subscribe((data: PageSearchOntologyDTO) => {
				this.ontologies = data.content ?? [];
				this.paginationInfo = data;
			});
	}

	/**
	 * Delete ontology by its id.
	 */
	deleteOntology(id: number): void {
		this.ontologyService
			.deleteOntology(id)
			.pipe(
				takeUntilDestroyed(this.destroyRef)
			)
			.subscribe(() => {
				this.ontologies = this.ontologies.filter((ontology) => ontology.id !== id);
				this.notificationService.showSuccess(MESSAGES_ONTOLOGIES_SUCCESS_DELETED);
			});
		this.deleteDialogVisible = false;
	}

	/**
	 * Method that is called when the page number changes.
	 */
	onPageChange(newPage: number): void {
		this.loadOntologies(newPage, this.paginationInfo.size);
	}

	/**
	 * Called when a form is successfully submitted
	 */
	onFormSubmitted() {
		this.visible = false;
		this.loadOntologies(PAGE, SIZE);
		this.selectedOntology = null;
	}
}
