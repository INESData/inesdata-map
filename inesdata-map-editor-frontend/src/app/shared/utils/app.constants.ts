import { marker as translate } from '@colsen1991/ngx-translate-extract-marker';

/**
 * Global constants
 */

// Language
export const DEFAULT_LANGUAGE = 'en';
export const AVAILABLE_LANGUAGES: string[] = ['es', 'en'];
export const LANGUAGE_STORAGE_NAME = 'language';

// Filters
export const PAGE = 0;
export const SIZE = 10;

// Messages
export const MESSAGES_SUCCESS: string = translate('messages.success');
export const MESSAGES_ERRORS: string = translate('messages.errors.title');
export const MESSAGES_ERRORS_GENERIC: string = translate('messages.errors.generic');
export const MESSAGES_ERRORS_INTERNET_CONNECTION: string = translate('messages.errors.internet.connection');
export const MESSAGES_ERRORS_LOGIN: string = translate('messages.errors.login');
export const MESSAGES_ERRORS_REQUIRED: string = translate('messages.errors.required');

export const MESSAGES_ONTOLOGIES_SUCCESS_CREATED: string = translate('messages.ontologies.success.created');
export const MESSAGES_ONTOLOGIES_SUCCESS_UPDATED: string = translate('messages.ontologies.success.updated');
export const MESSAGES_ONTOLOGIES_SUCCESS_DELETED: string = translate('messages.ontologies.success.deleted');

export const MESSAGES_DATA_SOURCES_SUCCESS_CREATED: string = translate('messages.data-sources.success.created');
export const MESSAGES_DATA_SOURCES_SUCCESS_UPDATED: string = translate('messages.data-sources.success.updated');
export const MESSAGES_DATA_SOURCES_SUCCESS_DELETED: string = translate('messages.data-sources.success.deleted');

export const MESSAGES_MAPPINGS_SUCCESS_CREATED: string = translate('messages.mappings.success.created');
export const MESSAGES_MAPPINGS_SUCCESS_DELETED: string = translate('messages.mappings.success.deleted');
export const MESSAGES_MAPPINGS_SUCCESS_UPDATED: string = translate('messages.mappings.success.updated');
export const MESSAGES_MAPPINGS_RULE_INCOMPLETE: string = translate('messages.mappings.rule.incomplete');
export const MESSAGES_MAPPINGS_PREDICATE_INCOMPLETE: string = translate('messages.mappings.predicate.incomplete');

export const MESSAGES_MATERIALISATIONS_SUCCESS: string = translate('messages.materialisations.success.executed');
export const MESSAGES_MATERIALISATIONS_ERRORS_NOTFOUND: string = translate('messages.materialisations.errors.not-found');

export const MESSAGES_DATA_SOURCES_ERRORS_NOFILE: string = translate('messages.data-sources.errors.no-file');

export const MESSAGES_MAPPINGS_ERRORS_NONAME: string = translate('messages.mappings.errors.no-name-url');
export const MESSAGES_MAPPINGS_ERRORS_NODATATYPE: string = translate('messages.mappings.errors.no-data-type');
export const MESSAGES_MAPPINGS_ERRORS_NOITERATOR: string = translate('messages.mappings.errors.no-iterator');

// Labels
export const LABELS_YES: string = translate('labels.yes');
export const LABELS_NO: string = translate('labels.no');
export const LABELS_HOME: string = translate('labels.home');
export const LABELS_NO_FILE_SELECTED: string = translate('labels.file-name');

// Placeholders
export const PLACEHOLDERS_ASTERISKS = '*************************';

// Form controls
export const FORM_CONTROL_ID = 'id';
export const FORM_CONTROL_PASSWORD = 'password';
export const FORM_CONTROL_TYPE = 'type';
export const FORM_CONTROL_FILETYPE = 'fileType';
export const FORM_CONTROL_DBTYPE = 'databaseType';

//Ontologies
export const ONTOLOGIES_ADD_ONTOLOGY: string = translate('ontologies.ontology.add');
export const ONTOLOGIES_EDIT_ONTOLOGY: string = translate('ontologies.ontology.edit');

//Data sources
export const DATA_SOURCES_ADD_DATA_SOURCE: string = translate('data-sources.data-source.add');
export const DATA_SOURCES_EDIT_DATA_SOURCE: string = translate('data-sources.data-source.edit');
export const DATA_SOURCES_DATA_BASE_TYPE = 'databaseType';
export const DATA_SOURCES_FILE_TYPE = 'fileType';

// Routes
export const HOME = '/home';
export const MAPPINGS = '/mappings';
export const MAPPINGS_BUILDER_EDIT = '/mappings/builder/edit';

// URLs
export const URL_EDIT = 'edit';
export const URL_DELIMITER = '/';

// Params
export const PARAM_ID = 'id';

//RML
export const RR_TEMPLATE = "rr:template";
export const RR_TERMTYPE = "rr:termType";
export const RR_LITERAL = "rr:Literal";
export const RR_IRI = "rr:IRI";
export const RR_DATATYPE = "rr:datatype";

// Sidebar
export const SIDEBAR_LABELS_ONTOLOGIES: string = translate('sidebar.labels.ontologies');
export const SIDEBAR_LABELS_DATASOURCES: string = translate('sidebar.labels.data-sources');
export const SIDEBAR_LABELS_MAPPINGS: string = translate('sidebar.labels.mappings');
export const SIDEBAR_LABELS_BUILDER: string = translate('sidebar.labels.builder');
export const SIDEBAR_LABELS_EDIT: string = translate('sidebar.labels.edit');

//Properties
export const PROPERTIES_DATA: string = translate('properties.data');
export const PROPERTIES_OBJECT: string = translate('properties.object');
export const PROPERTIES_ANNOTATION: string = translate('properties.annotation');
export const PROPERTIES_ASSOCIATED: string = translate('properties.associated');
