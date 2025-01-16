import { Component, DestroyRef, inject, OnInit } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router } from '@angular/router';
import { DataBaseSourceDTO, FileSourceDTO, FileSourceService, MappingDTO, MappingService, NamespaceDTO, ObjectMapDTO, OntologyService, PredicateObjectMapDTO, PropertyDTO, SearchOntologyDTO } from 'projects/mapper-api-client';
import { DataTypeEnum } from 'src/app/shared/enums/data-type.enum';
import { DataBaseTypeEnum } from 'src/app/shared/enums/database-type.enum';
import { DataFileTypeEnum } from 'src/app/shared/enums/datafile-type.enum';
import { DataSourceTypeEnum } from 'src/app/shared/enums/datasource-type.enum';
import { TermType } from 'src/app/shared/models/term-type.model';
import { TERM_TYPES } from 'src/app/shared/models/term-types';
import { LanguageService } from 'src/app/shared/services/language.service';
import { NotificationService } from 'src/app/shared/services/notification.service';
import { MESSAGES_ERRORS, MESSAGES_MAPPINGS_ERRORS_NODATATYPE, MESSAGES_MAPPINGS_ERRORS_NOITERATOR, MESSAGES_MAPPINGS_PREDICATE_INCOMPLETE, MESSAGES_MAPPINGS_RULE_INCOMPLETE, PARAM_ID, PROPERTIES_ANNOTATION, PROPERTIES_ASSOCIATED, PROPERTIES_DATA, PROPERTIES_OBJECT, RR_DATATYPE, RR_IRI, RR_LITERAL, RR_TEMPLATE, RR_TERMTYPE } from 'src/app/shared/utils/app.constants';
import { mapToDataSource } from 'src/app/shared/utils/types.utils';

@Component({
	selector: 'app-mappings-builder',
	templateUrl: './mappings-builder.component.html'
})
export class MappingsBuilderComponent implements OnInit {
	destroyRef = inject(DestroyRef);

	constructor(private ontologyService: OntologyService, private fileSourceService: FileSourceService, private mappingService: MappingService, private notificationService: NotificationService,
		private router: Router, private languageService: LanguageService, private route: ActivatedRoute) { }

	formats: string[] = [...Object.values(DataBaseTypeEnum), ...Object.values(DataFileTypeEnum)];
	dataTypes: string[] = [...Object.values(DataTypeEnum)];

	queryDialogVisible = false;
	elementDialogVisible = false;
	mappingDTO: MappingDTO;
	mappingId: number;

	ontologies: SearchOntologyDTO[];
	dataSources: FileSourceDTO[] | DataBaseSourceDTO[];
	properties: PropertyDTO[];
	subjectClasses: string[];
	predicateClasses: string[];

	selectedSourceFormat = DataFileTypeEnum.XML;
	selectedSource: FileSourceDTO | DataBaseSourceDTO = null;
	selectedSubjectOntology: SearchOntologyDTO = null;
	selectedSubjectClass: string = null;
	iterator?: string;
	templateUrl: string;

	selectedPredicateOntology: SearchOntologyDTO = null;
	selectedPredicateClass: string = null;
	selectedPredicateProperty: string = null;
	selectedPredicatePropertyUrl: string = null;

	objectMapValue: string;
	selectedDataType: string;

	source: string;
	fileFields: string[];
	selectedField: string;
	errorMessage: string;
	termType: TermType[];
	currentTermType = 'iri';
	isNewTriplesMap = false;
	isFirstEdition = true;
	namespaceMap: Record<string, string>;
	suggestions: string[];
	inputValue: string;
	selectedNamespace: NamespaceDTO;
	blockedSubject = false;

	/**
	 * Initializes the component and subscribe to route parameter to get the ID
	 * if provided. Otherwise, load ontologies.
	 *
	 */
	ngOnInit() {
		this.termType = TERM_TYPES;
		this.getOntologies();
		this.getFileData(this.selectedSourceFormat)
		this.route.paramMap.subscribe((params) => {
			this.mappingId = +params.get(PARAM_ID);
		})
		if (this.mappingId) {
			this.getMapping(this.mappingId);
		}
	}

	showDialogQuery() {
		this.queryDialogVisible = true;
	}

	showDialogElement() {
		this.elementDialogVisible = true;
	}

	/**
	 * Gets the list of ontologies.
	 */
	getOntologies(): void {
		this.ontologyService
			.getOntologies()
			.pipe(
				takeUntilDestroyed(this.destroyRef)
			)
			.subscribe((data: SearchOntologyDTO[]) => {
				this.ontologies = data ?? [];
			});
	}

	/**
	 * Gets the classes of the selected ontology.
	 */
	getClasses(id: number): void {
		this.ontologyService
			.getOntologyClasses(id)
			.pipe(
				takeUntilDestroyed(this.destroyRef)
			).subscribe((data: string[]) => {
				if (this.source === 'subject') {
					this.subjectClasses = data ?? [];
				} else if (this.source === 'predicate') {
					this.predicateClasses = data ?? [];
					this.getNamespaceMap();
				}
			})
	}

	/**
	 * Gets the properties of the selected ontology class.
	 */
	getProperties(id: number, className: string): void {
		this.ontologyService
			.getClassProperties(id, className)
			.pipe(
				takeUntilDestroyed(this.destroyRef)
			).subscribe((data: PropertyDTO[]) => {
				this.properties = data ?? [];
			})
	}

	/**
	 * Gets the selected ontology class
	 */
	onOntologySelect(ontology: SearchOntologyDTO, source: 'subject' | 'predicate'): void {
		this.source = source;
		if (this.source === 'subject') {
			this.subjectClasses = null;
			this.selectedSubjectClass = null;
		} else if (this.source === 'predicate') {
			this.properties = null;
			this.predicateClasses = null;
			this.selectedPredicateClass = null;
		}
		this.getClasses(ontology.id);
	}

	/**
	 * Gets the selected ontology class properties
	 */
	onClassSelect(className: string): void {
		this.selectedPredicateProperty = null;
		this.getProperties(this.selectedPredicateOntology.id, className);
	}

	/**
	 * Retrieves data based on the specified format
	 */
	getDataFromFormat(format: string): void {
		this.selectedSource = null;
		const type = mapToDataSource(format);
		if (type == DataSourceTypeEnum.FILE) {
			this.getFileData(format);
		}
	}

	/**
	 * Retrieves file data based on the specified type
	 */
	getFileData(type: string): void {
		this.fileSourceService
			.getFileSourceByType(type)
			.pipe(
				takeUntilDestroyed(this.destroyRef)
			).subscribe((data: FileSourceDTO[]) => {
				this.dataSources = data ?? [];
			})
	}

	/**
	 * Handles the selection of a source, which can be either a FileSourceDTO or a DataBaseSourceDTO
	 */
	onSourceSelected(source: FileSourceDTO | DataBaseSourceDTO): void {
		this.selectedSource = source;
		this.selectedField = null;
		switch (this.selectedSourceFormat) {
			case DataFileTypeEnum.CSV:
				this.getFields(source.id);
				break;
			case DataFileTypeEnum.XML:
				this.getXMLAttributes(source.id);
				break;
		}
	}

	/**
	 * Retrieves the fields for a given source ID
	 */
	getFields(id: number): void {
		this.fileSourceService
			.getFileFields(id)
			.pipe(
				takeUntilDestroyed(this.destroyRef)
			).subscribe((data: string[]) => {
				this.fileFields = data ?? [];
			})
	}

	/**
	 * Retrieves the XML attributes for a given file source ID
	 */
	getXMLAttributes(id: number): void {
		this.fileSourceService
			.getFileAttributes(id)
			.pipe(
				takeUntilDestroyed(this.destroyRef)
			).subscribe((data: string[]) => {
				this.fileFields = data ?? [];
			})
	}

	/**
	 * Collect information from subject, predicate and object and
	 * add it to the mapping DTO
	 */
	addRule(): void {

		const { selectedSource, selectedSourceFormat, templateUrl, iterator, selectedSubjectClass, selectedPredicatePropertyUrl, objectMapValue, currentTermType, selectedDataType, mappingDTO, isNewTriplesMap, isFirstEdition } = this;

		// Rule must be completed
		if (!(selectedSource?.id && selectedSourceFormat && selectedSubjectClass && templateUrl && selectedSubjectClass && selectedPredicatePropertyUrl && objectMapValue && currentTermType)) {
			this.notificationService.showErrorMessage(MESSAGES_MAPPINGS_RULE_INCOMPLETE, MESSAGES_ERRORS);
			return;
		}

		// Validate iterator and data type (non required fields in rule)
		if (!this.validateMappingInputs()) return;

		// Create object-predicate DTOs
		const objectMap = this.createObjectMap(objectMapValue, currentTermType, selectedDataType);
		const predicate = this.createPredicate(selectedPredicatePropertyUrl, objectMap);

		// On create
		if (!mappingDTO?.id) {
			//If no mapping DTO, create it
			if (!mappingDTO) {
				this.createMappingDTO(selectedSource.id, selectedSourceFormat, iterator, templateUrl, selectedSubjectClass, predicate);
			} else if (mappingDTO.fields) {
				// Mapping DTO exists, process fields
				this.processMappingField(isNewTriplesMap, selectedSource.id, selectedSourceFormat, iterator, templateUrl, selectedSubjectClass, predicate);
			}
			// On update
		} else {
			if (isFirstEdition) {
				// On first rule of edition (is first edition true)
				this.addNewFieldToMapping(selectedSource.id, selectedSourceFormat, iterator, templateUrl, selectedSubjectClass, predicate);
				this.isNewTriplesMap = false;
				this.isFirstEdition = false;
			} else {
				// On next rules of edition
				this.processMappingField(isNewTriplesMap, selectedSource.id, selectedSourceFormat, iterator, templateUrl, selectedSubjectClass, predicate);
			}
		}
		this.addNamespaceToMapping(this.selectedNamespace);
		this.clearObjectPredicate();
	}

	/**
	 * Processes a mapping field based on creating a new triples map or update an existing one
	 */
	processMappingField(isNewTriplesMap: boolean, selectedSourceId: number, selectedSourceFormat: string, iterator: string, templateUrl: string, selectedSubjectClass: string, predicate: PredicateObjectMapDTO[]): void {
		if (isNewTriplesMap) {
			// Add field
			this.addNewFieldToMapping(selectedSourceId, selectedSourceFormat, iterator, templateUrl, selectedSubjectClass, predicate);
			this.isNewTriplesMap = false;
			this.blockedSubject = true;
		} else {
			// Add predicate-object pair to field
			this.addPredicateToField(predicate);
			this.blockedSubject = true;
		}
	}

	/**
	 * Gets the mapping data for the given mapping ID
	 */
	getMapping(id: number): void {
		this.mappingService
			.getMapping(id)
			.pipe(
				takeUntilDestroyed(this.destroyRef)
			).subscribe((data: MappingDTO) => {
				this.mappingDTO = data;
			})
	}

	/**
	 * Create object map DTO
	 */
	createObjectMap(objectMapValue: string, currentTermType: string, selectedDataType?: string): ObjectMapDTO[] {
		const objectMap: ObjectMapDTO[] = [
			{ key: RR_TEMPLATE, literalValue: objectMapValue },
			{ key: RR_TERMTYPE, literalValue: currentTermType === 'literal' ? RR_LITERAL : RR_IRI },
		];

		if (currentTermType === 'literal') {
			objectMap.push({ key: RR_DATATYPE, literalValue: "xsd:" + selectedDataType });
		}

		return objectMap;
	}

	/**
	 * Create predicate DTO
	 */
	createPredicate(predicateName: string, objectMap: ObjectMapDTO[]): PredicateObjectMapDTO[] {
		return [
			{
				predicate: predicateName,
				objectMap: objectMap,
			},
		];
	}

	/**
	 * Create mapping DTO with all collected properties
	 */
	createMappingDTO(dataSourceId: number, sourceFormat: string, iterator: string, templateUrl: string, subjectClass: string, predicates: PredicateObjectMapDTO[]): void {
		this.mappingDTO = {
			name: '',
			baseUrl: '',
			ontologyIds: [this.selectedSubjectOntology.id, this.selectedPredicateOntology.id],
			fields: [
				{
					dataSourceId,
					logicalSource: sourceFormat === 'XML' || sourceFormat === 'JSON' ? { iterator } : null,
					logicalTable: null,
					subject: {
						template: templateUrl,
						className: this.selectedSubjectOntology.url + subjectClass,
					},
					predicates,
				},
			],
		};
		this.blockedSubject = true;
	}

	/**
	 * Add predicate DTO to field
	 */
	addPredicateToField(predicate: PredicateObjectMapDTO[]): void {
		const lastField = this.mappingDTO.fields[this.mappingDTO.fields.length - 1];
		lastField.predicates.push(...predicate);
		if (!this.mappingDTO.ontologyIds.includes(this.selectedPredicateOntology.id)) {
			this.mappingDTO.ontologyIds.push(this.selectedPredicateOntology.id);
		}
	}

	/**
	 * Add new field to mapping DTO
	 */
	addNewFieldToMapping(dataSourceId: number, sourceFormat: string, iterator: string, templateUrl: string, subjectClass: string, predicates: PredicateObjectMapDTO[]): void {
		this.mappingDTO.fields.push({
			dataSourceId,
			logicalSource: sourceFormat === 'XML' || sourceFormat === 'JSON' ? { iterator } : null,
			logicalTable: null,
			subject: {
				template: templateUrl,
				className: this.selectedSubjectOntology.url + subjectClass,
			},
			predicates
		});
		[this.selectedPredicateOntology.id, this.selectedSubjectOntology.id].forEach(id => {
			if (!this.mappingDTO.ontologyIds.includes(id)) {
				this.mappingDTO.ontologyIds.push(id);
			}
		});
		this.blockedSubject = true;
	}

	/**
	 * Validate iterator from subject and data type from object
	 */
	validateMappingInputs(): boolean {
		const { currentTermType, selectedDataType, selectedSourceFormat, iterator } = this;

		if (currentTermType === 'literal' && !selectedDataType) {
			this.notificationService.showErrorMessage(MESSAGES_MAPPINGS_ERRORS_NODATATYPE, MESSAGES_ERRORS);
			return false;
		}

		if ((selectedSourceFormat === 'XML' || selectedSourceFormat === 'JSON') && !iterator) {
			this.notificationService.showErrorMessage(MESSAGES_MAPPINGS_ERRORS_NOITERATOR, MESSAGES_ERRORS);
			return false;
		}

		return true;
	}

	/**
	 * Returns the corresponding icon class and for the property type
	 */
	getIconAndTitle(property: PropertyDTO): { iconClasses: string[], titles: string[] } {
		const iconClasses = [];
		const titles = [];

		switch (property.propertyType) {
			case 'DATA':
				iconClasses.push('pi pi-table');
				titles.push(this.languageService.translateValue(PROPERTIES_DATA));
				break;


			case 'OBJECT':
				iconClasses.push('pi pi-box');
				titles.push(this.languageService.translateValue(PROPERTIES_OBJECT));
				break;

			case 'ANNOTATION':
				iconClasses.push('pi pi-pen-to-square');
				titles.push(this.languageService.translateValue(PROPERTIES_ANNOTATION));
				break;

			default:
				break;
		}
		if (property.associated) {
			iconClasses.push('pi pi-link');
			titles.push(this.languageService.translateValue(PROPERTIES_ASSOCIATED));
		}

		return { iconClasses, titles };
	}

	/**
	 * Retrieves the namespace map for the selected ontology
	 */
	getNamespaceMap(): void {
		this.ontologyService
			.getNameSpaceMap(this.selectedPredicateOntology.id)
			.pipe(
				takeUntilDestroyed(this.destroyRef)
			)
			.subscribe((data: Record<string, string>) => {
				this.namespaceMap = this.cleanNamespaceMap(data);
			});
	}

	/**
	 * Cleans the keys of the namespace map by removing trailing colons
	 */
	cleanNamespaceMap(data: Record<string, string>): Record<string, string> {
		const cleanedNamespaceMap: Record<string, string> = {};
		for (const key in data) {
			const cleanedKey = key.endsWith(':') ? key.slice(0, -1) : key;
			cleanedNamespaceMap[cleanedKey] = data[key];
		}
		return cleanedNamespaceMap;
	}

	/**
	 * Search with prefix in namespace map to return the url value
	 */
	onPropertySelect(property: string): void {
		this.selectedPredicateProperty = property;

		const splittedProperty: string[] = property.split(':');
		const prefix = splittedProperty[0];
		const propertyName = splittedProperty[1];
		const foundUrl = this.namespaceMap[prefix];

		if (foundUrl) {
			this.selectedPredicatePropertyUrl = foundUrl + propertyName;
			this.selectedNamespace = {
				prefix,
				iri: foundUrl
			}
		} else {
			this.notificationService.showErrorMessage(MESSAGES_MAPPINGS_PREDICATE_INCOMPLETE, MESSAGES_ERRORS);
		}
	}

	/**
	 * Adds a namespace to the mapping if it does not already exist
	 */
	addNamespaceToMapping(namespace: NamespaceDTO): void {
		if (!this.mappingDTO.namespaces) {
			this.mappingDTO.namespaces = [];
		}
		const urlExists = this.mappingDTO.namespaces.some(existingNamespace => existingNamespace.iri && existingNamespace.iri === namespace.iri);
		if (!urlExists) {
			this.mappingDTO.namespaces?.push(namespace);
		}
	}

	/**
	 * Process query based on the term type
	 */
	search(event) {
		const fieldsToSearch = (this.selectedSourceFormat === DataFileTypeEnum.XML || this.selectedSourceFormat === DataFileTypeEnum.JSON)
			? this.filterAndTrimFields(this.fileFields)
			: this.fileFields;

		const query = event.query;
		if (this.currentTermType === 'literal') {
			this.inputValue = '';
			this.executeSearch(query, fieldsToSearch);

		} else if (this.currentTermType === 'iri' && query.includes('{')) {
			const charIndex = query.lastIndexOf('{');
			if (charIndex !== -1) {
				// Get query part before { and save it in inputValue
				this.inputValue = query.substring(0, charIndex + 1).trim();
				// Get query part after { and search
				const searchText = query.substring(charIndex + 1);
				this.executeSearch(searchText, fieldsToSearch);

			}
		}
	}

	/**
	 * Filters and trims fields based on the iterator
	 */
	filterAndTrimFields(fieldsToSearch: string[]): string[] {
		return fieldsToSearch
			.filter(field => field.startsWith(this.iterator))
			.map(field => field.slice(this.iterator.length + 1));
	}

	/**
	 * Filters the list of file fields based on the provided search text
	 */
	executeSearch(searchText: string, fieldsToSearch: string[]): void {
		this.suggestions = fieldsToSearch.filter(field =>
			field.toLowerCase().includes(searchText.toLowerCase())
		);
	}

	/**
	 * Concat input value saved before with the selected option
	 */
	onSelect(event): void {
		const selectedValue = event.value;
		this.objectMapValue = this.inputValue + selectedValue;
	}

	/**
	 * Clear all selected properties from object and predicate
	 */
	clearObjectPredicate(): void {
		this.objectMapValue = '';
		this.currentTermType = 'iri';
		this.selectedDataType = null;
		this.selectedPredicateOntology = null;
		this.predicateClasses = null;
		this.selectedPredicateClass = null;
		this.selectedPredicateProperty = null;
		this.selectedPredicatePropertyUrl = null;
	}

	/**
	 * Clear all selected properties
	 */
	newTriplesMap(): void {
		this.blockedSubject = false;
		this.selectedSourceFormat = DataFileTypeEnum.XML;
		this.selectedSource = null;
		this.selectedSubjectOntology = null;
		this.selectedSubjectClass = '';
		this.iterator = null;
		this.templateUrl = '';
		this.selectedPredicateOntology = null;
		this.predicateClasses = null;
		this.selectedPredicateClass = null;
		this.selectedPredicateProperty = null;
		this.selectedPredicatePropertyUrl = null;
		this.objectMapValue = '';
		this.currentTermType = 'iri';
		this.selectedDataType = null;
		this.isNewTriplesMap = true;
	}
}
