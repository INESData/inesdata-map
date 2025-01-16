package com.inesdatamap.mapperbackend.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.inesdatamap.mapperbackend.exceptions.GraphEngineException;
import com.inesdatamap.mapperbackend.model.dto.MappingDTO;
import com.inesdatamap.mapperbackend.model.dto.SearchMappingDTO;
import com.inesdatamap.mapperbackend.model.enums.DataFileTypeEnum;
import com.inesdatamap.mapperbackend.model.enums.DataSourceTypeEnum;
import com.inesdatamap.mapperbackend.model.jpa.DataSource;
import com.inesdatamap.mapperbackend.model.jpa.FileSource;
import com.inesdatamap.mapperbackend.model.jpa.Mapping;
import com.inesdatamap.mapperbackend.model.jpa.MappingField;
import com.inesdatamap.mapperbackend.model.jpa.Namespace;
import com.inesdatamap.mapperbackend.model.jpa.ObjectMap;
import com.inesdatamap.mapperbackend.model.jpa.Ontology;
import com.inesdatamap.mapperbackend.model.jpa.PredicateObjectMap;
import com.inesdatamap.mapperbackend.model.jpa.SubjectMap;
import com.inesdatamap.mapperbackend.model.mappers.MappingFieldMapperImpl;
import com.inesdatamap.mapperbackend.model.mappers.MappingMapper;
import com.inesdatamap.mapperbackend.model.mappers.MappingMapperImpl;
import com.inesdatamap.mapperbackend.model.mappers.PredicateObjectMapMapperImpl;
import com.inesdatamap.mapperbackend.properties.AppProperties;
import com.inesdatamap.mapperbackend.repositories.jpa.DataSourceRepository;
import com.inesdatamap.mapperbackend.repositories.jpa.FileSourceRepository;
import com.inesdatamap.mapperbackend.repositories.jpa.MappingRepository;
import com.inesdatamap.mapperbackend.repositories.jpa.OntologyRepository;
import com.inesdatamap.mapperbackend.services.ExecutionService;
import com.inesdatamap.mapperbackend.services.GraphEngineService;

import jakarta.persistence.EntityNotFoundException;

/**
 * Unit tests for the {@link MappingServiceImpl}
 *
 * @author gmv
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AppProperties.class, MappingServiceImpl.class, MappingMapperImpl.class, MappingFieldMapperImpl.class,
		PredicateObjectMapMapperImpl.class }, initializers = ConfigDataApplicationContextInitializer.class)
class MappingServiceImplTest {

	@MockBean
	private MappingRepository mappingRepo;

	@MockBean
	private OntologyRepository ontologyRepository;

	@MockBean
	private DataSourceRepository<DataSource> dataSourceRepository;

	@MockBean
	private FileSourceRepository fileSourceRepository;

	@MockBean
	private GraphEngineService graphEngineService;

	@MockBean
	private ExecutionService executionService;

	@Autowired
	private MappingMapper mappingMapper;

	@Autowired
	private MappingServiceImpl mappingService;

	@Test
	void testDeleteMapping() {
		// Mock data
		Long id = 1L;
		Mapping mapping = new Mapping();
		mapping.setId(id);

		// Mock behavior
		when(this.mappingRepo.findById(id)).thenReturn(Optional.of(mapping));

		// Test
		this.mappingService.deleteMapping(id);

		// Verify
		Mockito.verify(this.mappingRepo, Mockito.times(1)).deleteById(id);
	}

	@Test
	void testGetEntity() {
		// Mock data
		Long id = 1L;

		// Mock behavior
		when(this.mappingRepo.findById(id)).thenReturn(Optional.empty());

		// Test & Verify
		assertThrows(EntityNotFoundException.class, () -> this.mappingService.getEntity(id));
	}

	@Test
	void testListMappings() {
		// Mock data
		Pageable pageable = PageRequest.of(0, 10);

		Mapping mapping = buildMapping();

		Page<Mapping> mappingPage = new PageImpl<>(List.of(mapping));

		// Mock behavior
		when(this.mappingRepo.findAll(pageable)).thenReturn(mappingPage);

		// Test
		Page<SearchMappingDTO> resultPage = this.mappingService.listMappings(pageable);

		// Verify
		assertEquals(1, resultPage.getTotalElements());
		SearchMappingDTO resultDto = resultPage.getContent().get(0);
		assertEquals(1L, resultDto.getId());
		assertEquals("Test Mapping", resultDto.getName());
		assertEquals(List.of("Ontology1"), resultDto.getOntologies());
		assertEquals(List.of("Source1"), resultDto.getDataSources());
	}

	@Test
	void testGetMappingById() {
		Long id = 1L;
		Mapping mapping = new Mapping();
		mapping.setId(id);
		mapping.setName("Test mapping");

		when(this.mappingRepo.findById(id)).thenReturn(Optional.of(mapping));

		MappingDTO resultDto = this.mappingService.getMappingById(id);

		assertEquals(id, resultDto.getId());
		assertEquals("Test mapping", resultDto.getName());

		Mockito.verify(this.mappingRepo, Mockito.times(1)).findById(id);
	}

	@Test
	void materializeMappingTest() {

		Mapping mapping = buildMapping();
		Path path = Paths.get("");
		List<String> expectedResults = List.of("Graph", "created!");

		try (MockedStatic<Files> mockFiles = mockStatic(Files.class)) {
			// Mock behavior
			when(this.mappingRepo.findById(anyLong())).thenReturn(Optional.of(mapping));
			when(this.graphEngineService.run(anyString(), anyString(), anyString())).thenReturn(expectedResults);
			mockFiles.when(() -> Files.createFile(any())).thenReturn(path);

			List<String> results = this.mappingService.materialize(1L);

			assertEquals(expectedResults, results);

			mockFiles.verify(() -> Files.createFile(any()));
			mockFiles.verify(() -> Files.createDirectories(any()));
			mockFiles.verify(() -> Files.write(any(), any(byte[].class)));
		}
	}

	@Test
	void testUpdateMapping() {

		Long id = 1L;

		// Check if dto is null
		assertThrows(IllegalArgumentException.class, () -> {
			this.mappingService.updateMapping(id, null);
		});

		String fileName = "people.csv";
		String filePath = String.join(File.separator, "path", "to");

		FileSource source = buildFileSource(filePath, fileName, DataFileTypeEnum.CSV, DataSourceTypeEnum.FILE);
		SubjectMap subjectMap = buildSubjectMap("http://example.org/Person", "http://example.org/person/{id}");
		ObjectMap objectMap = buildObjectMap("rml:reference", "name");
		PredicateObjectMap predicateObjectMap = buildPredicateObjectMap("http://example.org/hasName", List.of(objectMap));
		MappingField field1 = buildMappingField(source, subjectMap, List.of(predicateObjectMap));
		Ontology ontology = buildOntology();

		Mapping mapping = buildMapping("CSV Mapping", List.of(field1));

		Mapping mappingDB = new Mapping();
		Mapping mappingSource = new Mapping();
		Mapping updatedMapping = new Mapping();
		updatedMapping.setName("Mapping DTO");

		when(this.ontologyRepository.getReferenceById(anyLong())).thenReturn(ontology);
		when(this.dataSourceRepository.getReferenceById(anyLong())).thenReturn(source);
		when(this.fileSourceRepository.getReferenceById(anyLong())).thenReturn(source);
		when(this.mappingRepo.findById(id)).thenReturn(Optional.of(mappingDB));
		when(this.mappingRepo.saveAndFlush(this.mappingMapper.merge(mappingSource, mappingDB))).thenReturn(updatedMapping);

		MappingDTO result = this.mappingService.updateMapping(id, this.mappingMapper.entityToDto(mapping));
		assertNotNull(result);

		verify(this.mappingRepo).saveAndFlush(this.mappingMapper.merge(mappingSource, mappingDB));
	}

	@Test
	void materializeMappingThrowsExceptionTest() {

		Mapping mapping = buildMapping();
		Path path = Paths.get("");
		try (MockedStatic<Files> mockFiles = mockStatic(Files.class)) {
			// Mock behavior
			when(this.mappingRepo.findById(anyLong())).thenReturn(Optional.of(mapping));
			when(this.graphEngineService.run(anyString(), anyString(), anyString())).thenThrow(GraphEngineException.class);
			mockFiles.when(() -> Files.createFile(any())).thenReturn(path);

			assertThrows(GraphEngineException.class, () -> this.mappingService.materialize(1L));
		}
	}

	@Test
	void testCreateMapping() {

		String fileName = "file.csv";
		String filePath = String.join(File.separator, "path", "to");

		FileSource source = buildFileSource(filePath, fileName, DataFileTypeEnum.CSV, DataSourceTypeEnum.FILE);
		SubjectMap subjectMap = buildSubjectMap("http://example.org/Person", "http://example.org/person/{id}");
		ObjectMap objectMap = buildObjectMap("rml:reference", "name");
		PredicateObjectMap predicateObjectMap = buildPredicateObjectMap("http://example.org/hasName", List.of(objectMap));
		MappingField field1 = buildMappingField(source, subjectMap, List.of(predicateObjectMap));
		Ontology ontology = buildOntology();

		Mapping mapping = buildMapping("CSV Mapping", List.of(field1));

		when(this.mappingRepo.save(mapping)).thenReturn(mapping);
		when(this.ontologyRepository.getReferenceById(anyLong())).thenReturn(ontology);
		when(this.dataSourceRepository.getReferenceById(anyLong())).thenReturn(source);
		when(this.fileSourceRepository.getReferenceById(anyLong())).thenReturn(source);

		Mapping result = this.mappingService.create(this.mappingMapper.entityToDto(mapping));

		assertNotNull(result);
		assertEquals(source, result.getFields().get(0).getSource());

	}

	@Test
	void testbuildRml() {

		String fileName = "file.csv";
		String filePath = String.join(File.separator, "path", "to");

		FileSource source = buildFileSource(filePath, fileName, DataFileTypeEnum.CSV, DataSourceTypeEnum.FILE);
		SubjectMap subjectMap = buildSubjectMap("http://example.org/Person", "http://example.org/person/{id}");
		ObjectMap objectMap = buildObjectMap("rml:reference", "name");
		PredicateObjectMap predicateObjectMap = buildPredicateObjectMap("http://example.org/hasName", List.of(objectMap));
		MappingField field1 = buildMappingField(source, subjectMap, List.of(predicateObjectMap));
		Ontology ontology = buildOntology();
		Mapping mapping = buildMapping("CSV Mapping", List.of(field1));

		when(this.mappingRepo.save(mapping)).thenReturn(mapping);
		when(this.ontologyRepository.getReferenceById(anyLong())).thenReturn(ontology);
		when(this.dataSourceRepository.getReferenceById(anyLong())).thenReturn(source);
		when(this.fileSourceRepository.getReferenceById(anyLong())).thenReturn(source);

		Mapping result = this.mappingService.create(this.mappingMapper.entityToDto(mapping));

		String rmlContent = new String(result.getRml(), StandardCharsets.UTF_8);

		assertTrue(rmlContent.contains("rr:predicate ns1:hasName"));
		assertTrue(rmlContent.contains("rml:reference \"name\""));

	}

	@Test
	void testSave() {
		Mapping mapping = buildMapping();

		when(mappingRepo.save(mapping)).thenReturn(mapping);

		MappingDTO result = mappingService.save(mapping);

		assertEquals(mapping.getName(), result.getName());
	}

	@Test
	void testSetnamespaces() {

		ModelBuilder builder = new ModelBuilder();
		Mapping mapping = new Mapping();

		List<Namespace> namespaces = new ArrayList<>();
		Namespace ns1 = new Namespace();
		ns1.setPrefix("ns1");
		ns1.setIri("http://www.w3.org/2004/02/skos/core#");
		namespaces.add(ns1);
		mapping.setNamespaces(namespaces);

		Set<Ontology> ontologies = new HashSet<>();
		Ontology ontology = new Ontology();
		ontology.setUrl("http://www.w3.org/ns/lemon/ontolex#");
		ontologies.add(ontology);
		mapping.setOntologies(ontologies);

		this.mappingService.setNamespaces(builder, mapping);

		assertEquals("ns1", mapping.getNamespaces().get(0).getPrefix());
		assertEquals("http://www.w3.org/2004/02/skos/core#", mapping.getNamespaces().get(0).getIri());

	}

	private static Mapping buildMapping() {
		Mapping mapping = new Mapping();
		Set<Ontology> ontologies = new HashSet<>();
		Ontology ontology = buildOntology();
		ontologies.add(ontology);

		mapping.setRml("RML CONTENT".getBytes());
		mapping.setId(1L);
		mapping.setName("Test Mapping");
		mapping.setOntologies(ontologies);

		MappingField field1 = new MappingField();

		DataSource source = new DataSource();
		source.setName("Source1");
		field1.setSource(source);

		mapping.setFields(List.of(field1));
		return mapping;
	}

	private static Mapping buildMapping(String name, List<MappingField> fields) {
		Mapping mapping = new Mapping();
		Set<Ontology> ontologies = new HashSet<>();
		Ontology ontology = buildOntology();
		ontologies.add(ontology);
		mapping.setOntologies(ontologies);
		mapping.setId(1L);
		mapping.setName(name);
		mapping.setBaseUrl("http://example.org/");
		mapping.setFields(fields);
		return mapping;
	}

	private static MappingField buildMappingField(FileSource source, SubjectMap subjectMap, List<PredicateObjectMap> predicates) {
		MappingField field = new MappingField();

		field.setSource(source);
		field.setSubject(subjectMap);
		field.setPredicates(predicates);
		return field;
	}

	private static FileSource buildFileSource(String filePath, String fileName, DataFileTypeEnum fileType, DataSourceTypeEnum type) {
		FileSource source = new FileSource();
		source.setId(1L);
		source.setFilePath(filePath);
		source.setFileName(fileName);
		source.setFileType(fileType);
		source.setType(type);
		return source;
	}

	private static SubjectMap buildSubjectMap(String className, String template) {
		SubjectMap subjectMap = new SubjectMap();
		subjectMap.setClassName(className);
		subjectMap.setTemplate(template);
		return subjectMap;
	}

	private static PredicateObjectMap buildPredicateObjectMap(String predicate, List<ObjectMap> objectMaps) {
		PredicateObjectMap predicateObjectMap = new PredicateObjectMap();
		predicateObjectMap.setPredicate(predicate);
		predicateObjectMap.setObjectMap(objectMaps);
		return predicateObjectMap;
	}

	private static ObjectMap buildObjectMap(String key, String literalValue) {
		ObjectMap objectMap = new ObjectMap();
		objectMap.setKey(key);
		objectMap.setLiteralValue(literalValue);
		return objectMap;
	}

	private static Ontology buildOntology() {
		Ontology ontology = new Ontology();
		ontology.setId(1L);
		ontology.setName("Ontology1");
		ontology.setUrl("http://example.org/");
		return ontology;
	}
}
