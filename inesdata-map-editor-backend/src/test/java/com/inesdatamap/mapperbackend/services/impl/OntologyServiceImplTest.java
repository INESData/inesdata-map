package com.inesdatamap.mapperbackend.services.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.inesdatamap.mapperbackend.model.dto.OntologyDTO;
import com.inesdatamap.mapperbackend.model.dto.PropertyDTO;
import com.inesdatamap.mapperbackend.model.dto.SearchOntologyDTO;
import com.inesdatamap.mapperbackend.model.enums.PropertyTypeEnum;
import com.inesdatamap.mapperbackend.model.jpa.Mapping;
import com.inesdatamap.mapperbackend.model.jpa.Ontology;
import com.inesdatamap.mapperbackend.model.mappers.OntologyMapper;
import com.inesdatamap.mapperbackend.repositories.jpa.MappingRepository;
import com.inesdatamap.mapperbackend.repositories.jpa.OntologyRepository;
import com.inesdatamap.mapperbackend.utils.NameSpaceUtils;
import com.inesdatamap.mapperbackend.utils.OWLUtils;

import jakarta.persistence.EntityNotFoundException;

/**
 * Unit tests for the {@link OntologyServiceImpl}
 *
 * @author gmv
 */
@ExtendWith(MockitoExtension.class)
class OntologyServiceImplTest {

	@Mock
	private OntologyRepository ontologyRepo;

	@Mock
	private OntologyMapper ontologyMapper;

	@Mock
	private OWLOntology owlOntology;

	@Mock
	private Ontology ontology;

	@Mock
	private MappingRepository mappingRepo;

	@InjectMocks
	private OntologyServiceImpl ontologyService;

	@Test
	void testListOntologies() {
		// Mock data
		Ontology ontology1 = new Ontology();
		Ontology ontology2 = new Ontology();
		List<Ontology> ontologies = Arrays.asList(ontology1, ontology2);
		Page<Ontology> ontologyPage = new PageImpl<>(ontologies);

		SearchOntologyDTO searchOntologyDTO1 = new SearchOntologyDTO();
		SearchOntologyDTO searchOntologyDTO2 = new SearchOntologyDTO();
		List<SearchOntologyDTO> searchOntologyDTOs = Arrays.asList(searchOntologyDTO1, searchOntologyDTO2);
		Page<SearchOntologyDTO> searchOntologyDTOPage = new PageImpl<>(searchOntologyDTOs);

		// Mock behavior
		Mockito.when(this.ontologyRepo.findAll(Mockito.any(Pageable.class))).thenReturn(ontologyPage);
		Mockito.when(this.ontologyMapper.entitytoSearchOntologyDTO(ontology1)).thenReturn(searchOntologyDTO1);
		Mockito.when(this.ontologyMapper.entitytoSearchOntologyDTO(ontology2)).thenReturn(searchOntologyDTO2);

		// Test
		Pageable pageable = PageRequest.of(0, 10);
		Page<SearchOntologyDTO> result = this.ontologyService.listOntologies(pageable);

		// Verify
		assertEquals(searchOntologyDTOPage.getTotalElements(), result.getTotalElements());
		assertEquals(searchOntologyDTOPage.getContent().size(), result.getContent().size());
	}

	@Test
	void testUpdateOntology() {
		// Mock data
		Long id = 1L;
		OntologyDTO ontologyDto = new OntologyDTO();
		ontologyDto.setName("Updated Ontology");

		Ontology ontologyDB = new Ontology();
		Ontology ontologySource = new Ontology();
		Ontology ontologyUpdated = new Ontology();
		ontologyUpdated.setId(id);

		// Define mock behavior
		when(this.ontologyMapper.dtoToEntity(ontologyDto)).thenReturn(ontologySource);
		when(this.ontologyRepo.saveAndFlush(this.ontologyMapper.merge(ontologySource, ontologyDB))).thenReturn(ontologyUpdated);
		when(this.ontologyMapper.entityToDto(ontologyUpdated)).thenReturn(ontologyDto);
		when(this.ontologyRepo.findById(id)).thenReturn(Optional.of(ontologyDB));

		// Test case where ontologyDto is valid
		OntologyDTO result = this.ontologyService.updateOntology(id, ontologyDto);
		assertEquals(ontologyDto, result);
		verify(this.ontologyRepo).saveAndFlush(this.ontologyMapper.merge(ontologySource, ontologyDB));

		// Test case where ontologyDto is null
		assertThrows(IllegalArgumentException.class, () -> {
			this.ontologyService.updateOntology(id, null);
		}, "The ontology has no data to update");
	}

	@Test
	void testDeleteOntology() {
		// Mock data
		Long id = 1L;
		Ontology ontology = new Ontology();
		Mapping mapping = new Mapping();
		List<Mapping> mappingsUsingOntology = List.of(mapping);

		// Mock behavior
		Mockito.when(this.ontologyRepo.findById(id)).thenReturn(Optional.of(ontology));
		Mockito.when(this.mappingRepo.findAllByOntologiesContaining(ontology)).thenReturn(mappingsUsingOntology);

		// If ontology is in use
		assertThrows(IllegalArgumentException.class, () -> {
			this.ontologyService.deleteOntology(id);
		});

		// Ontology is not in use
		List<Mapping> mappingsUsingOntologyEmpty = List.of();
		Mockito.when(this.mappingRepo.findAllByOntologiesContaining(ontology)).thenReturn(mappingsUsingOntologyEmpty);

		this.ontologyService.deleteOntology(id);
	}

	@Test
	void testGetEntity() {
		// Mock data
		Long id = 1L;
		Ontology ontology = new Ontology();

		// Mock behavior
		Mockito.when(this.ontologyRepo.findById(id)).thenReturn(Optional.of(ontology));

		// Test
		Ontology result = this.ontologyService.getEntity(id);

		// Verify
		assertEquals(ontology, result);
	}

	@Test
	void testCreateOntology_withFile() throws IOException {
		// Arrange
		OntologyDTO dto = new OntologyDTO();
		dto.setName("Test Ontology");

		MockMultipartFile file = new MockMultipartFile("file", "testfile.csv", "text/csv", "File content".getBytes());

		Ontology ontology = new Ontology();
		Ontology savedOntology = new Ontology();
		savedOntology.setId(1L);

		when(this.ontologyMapper.dtoToEntity(dto)).thenReturn(ontology);
		when(this.ontologyRepo.save(ontology)).thenReturn(savedOntology);
		when(this.ontologyMapper.entityToDto(savedOntology)).thenReturn(dto);

		// Act
		OntologyDTO result = this.ontologyService.createOntology(dto, file);

		// Assert
		assertEquals(dto, result);
		verify(this.ontologyRepo).save(ontology);
		assertEquals("File content".getBytes().length, ontology.getContent().length);
	}

	@Test
	void testCreateOntology_exceptions() throws IOException {
		// OntologyDto is null, should throw IllegalArgumentException
		assertThrows(IllegalArgumentException.class, () -> {
			this.ontologyService.createOntology(null, mock(MultipartFile.class));
		}, "The ontology has no data");

		// OntologyDto is valid, file is null (no exception is thrown)
		OntologyDTO dto = new OntologyDTO();
		dto.setName("Test Ontology");
		when(this.ontologyMapper.dtoToEntity(dto)).thenReturn(new Ontology());
		when(this.ontologyRepo.save(any(Ontology.class))).thenReturn(new Ontology());

		assertDoesNotThrow(() -> {
			this.ontologyService.createOntology(dto, null);
		});

		// OntologyDto is valid, file is empty (no exception is thrown)
		MultipartFile emptyFile = mock(MultipartFile.class);
		when(emptyFile.isEmpty()).thenReturn(true);

		assertDoesNotThrow(() -> {
			this.ontologyService.createOntology(dto, emptyFile);
		});

		// OntologyDto is valid, file has content (no exception is thrown)
		MultipartFile validFile = mock(MultipartFile.class);
		when(validFile.isEmpty()).thenReturn(false);
		when(validFile.getBytes()).thenReturn("valid content".getBytes());

		assertDoesNotThrow(() -> {
			this.ontologyService.createOntology(dto, validFile);
		});

	}

	@Test
	void testGetEntityThrowsException() {
		// Mock data
		Long id = 1L;

		// Mock behavior
		Mockito.when(this.ontologyRepo.findById(id)).thenReturn(Optional.empty());

		// Test & Verify
		assertThrows(EntityNotFoundException.class, () -> this.ontologyService.getEntity(id));
	}

	@Test
	void testGetOntologies() {
		// Mock data
		Ontology ontology1 = new Ontology();
		Ontology ontology2 = new Ontology();
		List<Ontology> ontologiesList = Arrays.asList(ontology1, ontology2);

		SearchOntologyDTO dto1 = new SearchOntologyDTO();
		SearchOntologyDTO dto2 = new SearchOntologyDTO();
		List<SearchOntologyDTO> dtoList = Arrays.asList(dto1, dto2);

		// Mock behavior
		when(this.ontologyRepo.findAll(Sort.by(Sort.Order.asc("name")))).thenReturn(ontologiesList);
		when(this.ontologyMapper.entitytoSearchOntologyDTO(ontologiesList)).thenReturn(dtoList);

		// Act
		List<SearchOntologyDTO> result = this.ontologyService.getOntologies(Sort.by(Sort.Order.asc("name")));

		// Assert
		assertEquals(dtoList, result);
	}

	@Test
	void testGetOntologyClasses() throws Exception {

		// Mock data
		Long id = 1L;
		Ontology ontologyEntity = new Ontology();
		ontologyEntity.setId(id);

		// Simulate that the ontology has content
		byte[] ontologyContentBytes = "Ontology content".getBytes(StandardCharsets.UTF_8);
		ontologyEntity.setContent(ontologyContentBytes);

		// Mock the behavior of getEntity
		lenient().when(this.ontologyRepo.findById(id)).thenReturn(Optional.of(ontologyEntity));

		// Mock for OWLOntologyManager and OWLOntology
		OWLOntologyManager manager = mock(OWLOntologyManager.class);
		OWLOntology owlOntology = mock(OWLOntology.class);
		when(manager.loadOntologyFromOntologyDocument(any(StringDocumentSource.class))).thenReturn(owlOntology);

		// Mock the classes within the OWLOntology
		OWLClass owlClass1 = mock(OWLClass.class);
		OWLClass owlClass2 = mock(OWLClass.class);
		IRI iri1 = mock(IRI.class);
		IRI iri2 = mock(IRI.class);
		when(iri1.getFragment()).thenReturn("Class1");
		when(iri2.getFragment()).thenReturn("Class2");
		when(owlClass1.getIRI()).thenReturn(iri1);
		when(owlClass2.getIRI()).thenReturn(iri2);

		// Configure the mock to return the classes
		List<OWLClass> classes = Arrays.asList(owlClass1, owlClass2);
		when(owlOntology.getClassesInSignature()).thenReturn(new HashSet<>(classes));

		// Mock the OWLOntologyManager for the service
		try (MockedStatic<OWLManager> mockedOWLManager = mockStatic(OWLManager.class)) {
			mockedOWLManager.when(OWLManager::createOWLOntologyManager).thenReturn(manager);

			// Execute the method under test
			List<String> result = this.ontologyService.getOntologyClasses(id);

			// Verify the result
			List<String> expectedClasses = Arrays.asList("Class1", "Class2");

			// Sort both lists before comparing
			Collections.sort(result);
			Collections.sort(expectedClasses);

			assertEquals(expectedClasses, result);

			// Verify interactions with mocks
			verify(this.ontologyRepo).findById(id);
			verify(manager).loadOntologyFromOntologyDocument(any(StringDocumentSource.class));
			verify(owlOntology).getClassesInSignature();
		}
	}

	@Test
	void testGetClasses() throws Exception {

		// Mock OWLOntology and OWLClass
		OWLOntology owlOntology = mock(OWLOntology.class);

		// Mock OWLClass with valid and invalid names
		OWLClass owlClass1 = mock(OWLClass.class);
		OWLClass owlClass2 = mock(OWLClass.class);
		OWLClass owlClass3 = mock(OWLClass.class);
		IRI iri1 = mock(IRI.class);
		IRI iri2 = mock(IRI.class);
		IRI iri3 = mock(IRI.class);

		// Define the behavior for IRI objects
		when(iri1.getFragment()).thenReturn("Class1");
		when(iri2.getFragment()).thenReturn("");
		when(iri3.getFragment()).thenReturn(null);

		// Define the behavior for OWLClass objects
		when(owlClass1.getIRI()).thenReturn(iri1);
		when(owlClass2.getIRI()).thenReturn(iri2);
		when(owlClass3.getIRI()).thenReturn(iri3);

		// Create a set of classes to return from the ontology
		Set<OWLClass> classes = new HashSet<>();
		classes.add(owlClass1);
		classes.add(owlClass2);
		classes.add(owlClass3);

		// Define the behavior for OWLOntology
		when(owlOntology.getClassesInSignature()).thenReturn(classes);

		// Act
		List<String> result = OWLUtils.getClasses(owlOntology);

		// Assert
		// Expected result should only include the valid class name
		List<String> expected = List.of("Class1");
		assertEquals(expected, result);
	}

	@Test
	void testGetClassProperties() throws Exception {
		// Mock data
		Long id = 1L;
		String className = "Person";
		String ontologyContent = buildOntologyContent();

		// Mock Ontology and service methods
		Ontology ontology = mock(Ontology.class);
		OntologyServiceImpl ontologyService = spy(new OntologyServiceImpl());

		doReturn(ontology).when(ontologyService).getEntity(id);
		doReturn(ontologyContent).when(ontologyService).getOntologyContent(ontology);

		List<PropertyDTO> result = ontologyService.getClassProperties(id, className);
		assertEquals("ex:hasName", result.get(0).getName());
		assertEquals(PropertyTypeEnum.DATA, result.get(0).getPropertyType());

	}

	@Test
	void testGetProperties_validInput() throws Exception {

		String ontologyContent = buildOntologyContent();
		String className = "Person";

		List<PropertyDTO> result = OWLUtils.getProperties(ontologyContent, className);

		boolean hasProperty = false;
		for (PropertyDTO property : result) {
			if ("ex:hasName".equals(property.getName())) {
				hasProperty = true;
				break;
			}
		}
		assertTrue(hasProperty);
		assertNotNull(result);
	}

	@Test
	void testGetProperties_invalidInput() {

		// Act & Assert
		assertThrows(IllegalArgumentException.class, () -> OWLUtils.getProperties(null, "Class1"), "Ontology content is empty.");
		assertThrows(IllegalArgumentException.class, () -> OWLUtils.getProperties("Ontology content", null), "Class name is empty.");
	}

	@Test
	void testGetOntologyContent() {
		// Case 1: Ontology with content
		Ontology ontologyWithContent = new Ontology();
		byte[] contentBytes = "Ontology content".getBytes(StandardCharsets.UTF_8);
		ontologyWithContent.setContent(contentBytes);

		// Act
		String resultWithContent = this.ontologyService.getOntologyContent(ontologyWithContent);

		// Assert
		assertEquals("Ontology content", resultWithContent);

		// Case 2: Ontology without content
		Ontology ontologyWithoutContent = new Ontology();
		ontologyWithoutContent.setContent(null);

		// Act and Assert
		assertThrows(IllegalArgumentException.class, () -> {
			this.ontologyService.getOntologyContent(ontologyWithoutContent);
		}, "Ontology has no content.");
	}


	@Test
	void testGetDataProperties() throws OWLOntologyCreationException {

		String ontologyContent = buildOntologyContent();
		String className = "Person";

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology owl = manager
				.loadOntologyFromOntologyDocument(new StringDocumentSource(ontologyContent));
		OWLClass owlClass = owl.classesInSignature().filter(clazz -> clazz.getIRI().getFragment().equals(className)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Class " + className + " not found in the ontology"));

		Map<String, String> namespacePrefixes = NameSpaceUtils.getPrefixNamespaceMap(owl);

		List<PropertyDTO> dataProperties = OWLUtils.getDataProperties(owlClass, owl, namespacePrefixes);
		assertEquals("ex:hasName", dataProperties.get(0).getName());
	}

	@Test
	void testGetObjectProperties() throws OWLOntologyCreationException {

		String ontologyContent = buildOntologyContent();
		String className = "Person";

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology owl = manager.loadOntologyFromOntologyDocument(new StringDocumentSource(ontologyContent));
		OWLClass owlClass = owl.classesInSignature().filter(clazz -> clazz.getIRI().getFragment().equals(className)).findFirst()

				.orElseThrow(() -> new IllegalArgumentException("Class " + className + " not found in the ontology"));

		Map<String, String> namespacePrefixes = NameSpaceUtils.getPrefixNamespaceMap(owl);

		List<PropertyDTO> objectProperties = OWLUtils.getObjectProperties(owlClass, owl, namespacePrefixes);
		assertEquals("ex:hasFriend", objectProperties.get(0).getName());
	}

	@Test
	void testGetAnnotationProperties() throws OWLOntologyCreationException {

		String ontologyContent = buildOntologyContent();

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology owl = manager.loadOntologyFromOntologyDocument(new StringDocumentSource(ontologyContent));

		Map<String, String> namespacePrefixes = NameSpaceUtils.getPrefixNamespaceMap(owl);

		List<PropertyDTO> annotationProperties = OWLUtils.getAnnotationProperties(owl, namespacePrefixes);
		assertEquals("ex:author", annotationProperties.get(0).getName());
	}

	@Test
	void testGetNameSpaceMap() throws OWLOntologyCreationException {

		String ontologyContent = buildOntologyContent();
		byte[] ontologyContentBytes = ontologyContent.getBytes(StandardCharsets.UTF_8);
		Ontology ontology = new Ontology();
		ontology.setContent(ontologyContentBytes);

		when(this.ontologyRepo.findById(anyLong())).thenReturn(Optional.ofNullable(ontology));
		Map<String, String> namespaceMap = this.ontologyService.getNameSpaceMap(1L);

		assertNotNull(namespaceMap);
		assertTrue(namespaceMap.containsKey("rdfs:"));

	}

	private static String buildOntologyContent() {

		String content = "@prefix ex: <http://example.org/> .\n"
				+ "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + "\n" + "ex:Person rdf:type rdfs:Class .\n" + "\n"
				+ "ex:hasName rdf:type rdf:Property ;\n" + "    rdfs:domain ex:Person ;\n" + "    rdfs:range rdfs:Literal .\n" + "\n"
				+ "ex:hasFriend rdf:type rdf:ObjectProperty ;\n" + "    rdfs:domain ex:Person ;\n" + "    rdfs:range ex:Person .\n" + "\n"
				+ "ex:author rdf:type owl:AnnotationProperty .";

		return content;
	}
}
