import { Component, DestroyRef, inject, OnInit } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Router } from '@angular/router';
import { DataSourceDTO, DataSourceService, ExecutionService, MappingService, OntologyService, PageDataSourceDTO, PageSearchMappingDTO, SearchOntologyDTO } from 'projects/mapper-api-client';
import { SearchMappingDTO } from 'projects/mapper-api-client/model/searchMappingDTO';
import { LanguageService } from 'src/app/shared/services/language.service';
import { NotificationService } from 'src/app/shared/services/notification.service';
import { LABELS_NO_FILE_SELECTED, MAPPINGS_BUILDER_EDIT, MESSAGES_MAPPINGS_SUCCESS_DELETED, PAGE, SIZE } from 'src/app/shared/utils/app.constants';

@Component({
	selector: 'app-mappings-list',
	templateUrl: './mappings-list.component.html'
})
export class MappingsListComponent implements OnInit {

	destroyRef = inject(DestroyRef);

	constructor(private mappingService: MappingService, private notificationService: NotificationService, private executionService: ExecutionService, private router: Router, private languageService: LanguageService,
		private ontologyService: OntologyService, private dataSourceService: DataSourceService
	) { }
	selectedCategories: unknown[] = [];
	selectedOntologies: SearchOntologyDTO[] = [];
	selectedDataSources: DataSourceDTO[] = [];
	dataSources: DataSourceDTO[];
	selectedMapping: SearchMappingDTO;
	mappings: SearchMappingDTO[];
	ontologies: SearchOntologyDTO[];
	paginationInfo: PageSearchMappingDTO;
	addHistoryDialog = false;
	deleteDialogVisible = false;
	autoDialogVisible = false;
	importDialogVisible = false;

	file: File;
	fileName: string = this.languageService.translateValue(LABELS_NO_FILE_SELECTED);
	fileSelected = false;

	/**
	 * Loads the mappings when the component is initialized
	 */
	ngOnInit(): void {
		this.loadMappings(PAGE, SIZE);
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
	 * Loads the mappings list.
	 */
	loadMappings(page: number, size: number): void {
		this.mappingService
			.listMappings(page, size)
			.pipe(
				takeUntilDestroyed(this.destroyRef)
			)
			.subscribe((data: PageSearchMappingDTO) => {
				this.mappings = data.content ?? [];
				this.paginationInfo = data;
			});
	}

	/**
	 * Delete mapping by its id.
	 */
	deleteMapping(id: number): void {
		this.mappingService
			.deleteMapping(id)
			.pipe(
				takeUntilDestroyed(this.destroyRef)
			)
			.subscribe(() => {
				this.loadMappings(PAGE, SIZE);
				this.notificationService.showSuccess(MESSAGES_MAPPINGS_SUCCESS_DELETED);
			});
		this.deleteDialogVisible = false;
	}

	/**
	 * Loads the ontologies list.
	 */
	loadOntologies(): void {
		this.ontologyService
			.getOntologies()
			.pipe(takeUntilDestroyed(this.destroyRef))
			.subscribe((data: SearchOntologyDTO[]) => {
				this.ontologies = data ?? [];
			});
	}

	/**
	 * Loads the data sources list.
	 */
	loadDataSources(): void {
		this.dataSourceService
			.listDataSources(PAGE, SIZE)
			.pipe(takeUntilDestroyed(this.destroyRef))
			.subscribe((data: PageDataSourceDTO) => {
				this.dataSources = data.content ?? [];
			});
	}

	/**
	 * Method that is called when the page number changes.
	 */
	onPageChange(newPage: number): void {
		this.loadMappings(newPage, this.paginationInfo.size);
	}

	/**
	 * Display materialisation history dialog
	 */
	showDialog(mapping: SearchMappingDTO) {
		this.selectedMapping = mapping;
		this.addHistoryDialog = true;
	}

	/**
	 * Display delete dialog
	 */
	showDialogDelete(mapping: SearchMappingDTO): void {
		this.selectedMapping = mapping;
		this.deleteDialogVisible = true;
	}

	showDialogAuto() {
		this.autoDialogVisible = true;
	}

	/**
	 * Display import mapping dialog, load data sources and ontologies
	 */
	showDialogImport() {
		this.selectedOntologies = [];
		this.selectedDataSources = [];
		this.loadDataSources();
		this.loadOntologies();
		this.importDialogVisible = true;
	}

	/**
	* Close import dialog
	*/
	cancelImport(): void {
		this.importDialogVisible = false;
	}

	/**
	* Close delete dialog
	*/
	cancelDelete(): void {
		this.deleteDialogVisible = false;
	}

	editMapping(id: string) {
		this.router.navigate([MAPPINGS_BUILDER_EDIT, id]);
	}

	categories: unknown[] = [
		{ name: 'Test 1', key: 'A' },
		{ name: 'Test 2', key: 'M' },
		{ name: 'Test 3', key: 'P' },
		{ name: 'Test 4', key: 'R' }
	];
}
