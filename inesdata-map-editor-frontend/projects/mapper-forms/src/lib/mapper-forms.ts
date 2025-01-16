import { FormGroup, FormControl, Validators } from '@angular/forms';

export const dataBaseSourceDtoForm = new FormGroup({
  connectionString: new FormControl(null, [
    Validators.required,
    Validators.minLength(1),
    Validators.maxLength(255)
  ]),
  databaseType: new FormControl(null, [Validators.required]),
  id: new FormControl(null, []),
  name: new FormControl(null, [
    Validators.required,
    Validators.minLength(1),
    Validators.maxLength(255)
  ]),
  password: new FormControl(null, [
    Validators.required,
    Validators.minLength(1),
    Validators.maxLength(255)
  ]),
  type: new FormControl(null, []),
  userName: new FormControl(null, [
    Validators.required,
    Validators.minLength(1),
    Validators.maxLength(255)
  ]),
  version: new FormControl(null, [])
});

export const dataSourceDtoForm = new FormGroup({
  id: new FormControl(null, []),
  name: new FormControl(null, [
    Validators.required,
    Validators.minLength(1),
    Validators.maxLength(255)
  ]),
  type: new FormControl(null, []),
  version: new FormControl(null, [])
});

export const executionDtoForm = new FormGroup({
  executionDate: new FormControl(null, []),
  id: new FormControl(null, []),
  knowledgeGraphFileName: new FormControl(null, []),
  logFileName: new FormControl(null, []),
  mappingFileName: new FormControl(null, []),
  version: new FormControl(null, [])
});

export const fileSourceDtoForm = new FormGroup({
  fileType: new FormControl(null, [Validators.required]),
  id: new FormControl(null, []),
  name: new FormControl(null, [
    Validators.required,
    Validators.minLength(1),
    Validators.maxLength(255)
  ]),
  type: new FormControl(null, []),
  version: new FormControl(null, [])
});

export const linkForm = new FormGroup({
  href: new FormControl(null, []),
  templated: new FormControl(null, [])
});

export const logicalSourceDtoForm = new FormGroup({
  id: new FormControl(null, []),
  iterator: new FormControl(null, []),
  query: new FormControl(null, [
    Validators.minLength(1),
    Validators.maxLength(255)
  ]),
  referenceFormulation: new FormControl(null, []),
  source: new FormControl(null, [
    Validators.minLength(1),
    Validators.maxLength(255)
  ]),
  version: new FormControl(null, [])
});

export const logicalTableDtoForm = new FormGroup({
  id: new FormControl(null, []),
  query: new FormControl(null, []),
  tableName: new FormControl(null, [
    Validators.required,
    Validators.minLength(1),
    Validators.maxLength(255)
  ]),
  version: new FormControl(null, [])
});

export const mappingDtoForm = new FormGroup({
  baseUrl: new FormControl(null, [
    Validators.required,
    Validators.minLength(1),
    Validators.maxLength(255)
  ]),
  fields: new FormControl(null, [Validators.required]),
  id: new FormControl(null, []),
  name: new FormControl(null, [
    Validators.required,
    Validators.minLength(1),
    Validators.maxLength(255)
  ]),
  namespaces: new FormControl(null, []),
  ontologyIds: new FormControl(null, [Validators.required]),
  version: new FormControl(null, [])
});

export const mappingFieldDtoForm = new FormGroup({
  dataSourceId: new FormControl(null, [Validators.required]),
  id: new FormControl(null, []),
  logicalSource: new FormControl(null, []),
  logicalTable: new FormControl(null, []),
  predicates: new FormControl(null, []),
  subject: new FormControl(null, []),
  version: new FormControl(null, [])
});

export const namespaceDtoForm = new FormGroup({
  id: new FormControl(null, []),
  iri: new FormControl(null, []),
  prefix: new FormControl(null, []),
  version: new FormControl(null, [])
});

export const objectMapDtoForm = new FormGroup({
  id: new FormControl(null, []),
  key: new FormControl(null, [
    Validators.required,
    Validators.minLength(1),
    Validators.maxLength(255)
  ]),
  literalValue: new FormControl(null, []),
  objectValue: new FormControl(null, []),
  version: new FormControl(null, [])
});

export const ontologyDtoForm = new FormGroup({
  content: new FormControl(null, []),
  id: new FormControl(null, []),
  name: new FormControl(null, [
    Validators.required,
    Validators.minLength(1),
    Validators.maxLength(255)
  ]),
  title: new FormControl(null, [
    Validators.minLength(0),
    Validators.maxLength(255)
  ]),
  uploadDate: new FormControl(null, []),
  url: new FormControl(null, [
    Validators.minLength(0),
    Validators.maxLength(255)
  ]),
  version: new FormControl(null, []),
  versionName: new FormControl(null, [
    Validators.required,
    Validators.minLength(1),
    Validators.maxLength(255)
  ])
});

export const pageDataSourceDtoForm = new FormGroup({
  content: new FormControl(null, []),
  empty: new FormControl(null, []),
  first: new FormControl(null, []),
  last: new FormControl(null, []),
  number: new FormControl(null, []),
  numberOfElements: new FormControl(null, []),
  pageable: new FormControl(null, []),
  size: new FormControl(null, []),
  sort: new FormControl(null, []),
  totalElements: new FormControl(null, []),
  totalPages: new FormControl(null, [])
});

export const pageMetadataForm = new FormGroup({
  number: new FormControl(null, []),
  size: new FormControl(null, []),
  totalElements: new FormControl(null, []),
  totalPages: new FormControl(null, [])
});

export const pageSearchMappingDtoForm = new FormGroup({
  content: new FormControl(null, []),
  empty: new FormControl(null, []),
  first: new FormControl(null, []),
  last: new FormControl(null, []),
  number: new FormControl(null, []),
  numberOfElements: new FormControl(null, []),
  pageable: new FormControl(null, []),
  size: new FormControl(null, []),
  sort: new FormControl(null, []),
  totalElements: new FormControl(null, []),
  totalPages: new FormControl(null, [])
});

export const pageSearchOntologyDtoForm = new FormGroup({
  content: new FormControl(null, []),
  empty: new FormControl(null, []),
  first: new FormControl(null, []),
  last: new FormControl(null, []),
  number: new FormControl(null, []),
  numberOfElements: new FormControl(null, []),
  pageable: new FormControl(null, []),
  size: new FormControl(null, []),
  sort: new FormControl(null, []),
  totalElements: new FormControl(null, []),
  totalPages: new FormControl(null, [])
});

export const pageableObjectForm = new FormGroup({
  offset: new FormControl(null, []),
  pageNumber: new FormControl(null, []),
  pageSize: new FormControl(null, []),
  paged: new FormControl(null, []),
  sort: new FormControl(null, []),
  unpaged: new FormControl(null, [])
});

export const pagedModelExecutionDtoForm = new FormGroup({
  content: new FormControl(null, []),
  page: new FormControl(null, [])
});

export const predicateObjectMapDtoForm = new FormGroup({
  id: new FormControl(null, []),
  objectMap: new FormControl(null, [Validators.required]),
  predicate: new FormControl(null, [
    Validators.required,
    Validators.minLength(1),
    Validators.maxLength(255)
  ]),
  version: new FormControl(null, [])
});

export const propertyDtoForm = new FormGroup({
  associated: new FormControl(null, []),
  name: new FormControl(null, [Validators.required]),
  propertyType: new FormControl(null, [Validators.required])
});

export const searchMappingDtoForm = new FormGroup({
  dataSources: new FormControl(null, []),
  id: new FormControl(null, []),
  name: new FormControl(null, []),
  ontologies: new FormControl(null, []),
  version: new FormControl(null, [])
});

export const searchOntologyDtoForm = new FormGroup({
  id: new FormControl(null, []),
  name: new FormControl(null, [
    Validators.required,
    Validators.minLength(1),
    Validators.maxLength(255)
  ]),
  title: new FormControl(null, [
    Validators.minLength(0),
    Validators.maxLength(255)
  ]),
  uploadDate: new FormControl(null, []),
  url: new FormControl(null, [
    Validators.required,
    Validators.minLength(1),
    Validators.maxLength(255)
  ]),
  versionName: new FormControl(null, [
    Validators.required,
    Validators.minLength(1),
    Validators.maxLength(255)
  ])
});

export const sortObjectForm = new FormGroup({
  empty: new FormControl(null, []),
  sorted: new FormControl(null, []),
  unsorted: new FormControl(null, [])
});

export const subjectMapDtoForm = new FormGroup({
  className: new FormControl(null, [
    Validators.required,
    Validators.minLength(1),
    Validators.maxLength(255)
  ]),
  id: new FormControl(null, []),
  template: new FormControl(null, [
    Validators.required,
    Validators.minLength(1),
    Validators.maxLength(255)
  ]),
  version: new FormControl(null, [])
});
