package com.inesdatamap.mapperbackend.controllers.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.inesdatamap.mapperbackend.model.dto.OntologyDTO;
import com.inesdatamap.mapperbackend.model.dto.PropertyDTO;
import com.inesdatamap.mapperbackend.model.dto.SearchOntologyDTO;
import com.inesdatamap.mapperbackend.model.enums.PropertyTypeEnum;
import com.inesdatamap.mapperbackend.services.OntologyService;
import com.inesdatamap.mapperbackend.utils.Constants;

/**
 * Unit tests for the {@link OntologyController}
 *
 * @author gmv
 */
@ContextConfiguration(classes = OntologyController.class)
@WebMvcTest(OntologyController.class)
class OntologyControllerTest {

	@MockBean
	private OntologyService ontologyService;

	@Autowired
	private OntologyController controller;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testListOntologies() {

		// mock
		SearchOntologyDTO ontology1 = new SearchOntologyDTO();
		SearchOntologyDTO ontology2 = new SearchOntologyDTO();
		List<SearchOntologyDTO> ontologies = Arrays.asList(ontology1, ontology2);
		Page<SearchOntologyDTO> page = new PageImpl<>(ontologies);

		Mockito.when(this.ontologyService
				.listOntologies(PageRequest.of(Constants.NUMBER_0, Constants.NUMBER_10, Sort.by(Constants.SORT_BY_NAME).ascending())))
				.thenReturn(page);

		// test
		ResponseEntity<Page<SearchOntologyDTO>> result = this.controller.listOntologies(Constants.NUMBER_0, Constants.NUMBER_10);

		// verifies & asserts
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(page, result.getBody());
	}

	@Test
	void testUpdateOntology() {

		// mock
		Long id = 1L;
		OntologyDTO ontologyDto = new OntologyDTO();
		OntologyDTO updatedOntology = new OntologyDTO();

		// Configure mock for service
		Mockito.when(this.ontologyService.updateOntology(id, ontologyDto)).thenReturn(updatedOntology);

		ResponseEntity<OntologyDTO> result = this.controller.updateOntology(id, ontologyDto);

		// verifies & asserts
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(updatedOntology, result.getBody());
	}

	@Test
	void testDeleteOntology() {

		// mock
		Long id = 1L;

		// test
		ResponseEntity<Void> result = this.controller.deleteOntology(id);

		// Verifies & asserts
		assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

		// Verify that the service method was called once
		Mockito.verify(this.ontologyService, Mockito.times(1)).deleteOntology(id);
	}

	@Test
	void createOntology_withFile() throws Exception {
		// Arrange
		OntologyDTO ontologyDto = new OntologyDTO();
		ontologyDto.setName("Test Ontology");

		MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE,
				"This is the file content".getBytes());

		MockMultipartFile jsonBody = new MockMultipartFile("body", "body", MediaType.APPLICATION_JSON_VALUE,
				"{\"name\":\"Test Ontology\"}".getBytes());

		when(this.ontologyService.createOntology(Mockito.any(OntologyDTO.class), Mockito.any())).thenReturn(ontologyDto);

		// Act & Assert
		this.mockMvc.perform(multipart("/ontologies").file(file).file(jsonBody).contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
				.andExpect(status().isCreated()) // Changed from isOk() to isCreated()
				.andExpect(jsonPath("$.name").value("Test Ontology"));
	}

	@Test
	void createOntology_withoutFile() throws Exception {
		// Arrange
		OntologyDTO ontologyDto = new OntologyDTO();
		ontologyDto.setName("Test Ontology");

		MockMultipartFile jsonBody = new MockMultipartFile("body", "body", MediaType.APPLICATION_JSON_VALUE,
				"{\"name\":\"Test Ontology\"}".getBytes());

		when(this.ontologyService.createOntology(Mockito.any(OntologyDTO.class), Mockito.isNull())).thenReturn(ontologyDto);

		// Act & Assert
		this.mockMvc.perform(multipart("/ontologies").file(jsonBody).contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
				.andExpect(status().isCreated()) // Changed from isOk() to isCreated()
				.andExpect(jsonPath("$.name").value("Test Ontology"));
	}

	@Test
	void testGetOntologies() {
		// Arrange
		SearchOntologyDTO ontology1 = new SearchOntologyDTO();
		ontology1.setName("Ontology 1");

		SearchOntologyDTO ontology2 = new SearchOntologyDTO();
		ontology2.setName("Ontology 2");

		List<SearchOntologyDTO> ontologies = Arrays.asList(ontology1, ontology2);

		// Configure mock for service
		Mockito.when(this.ontologyService.getOntologies(Sort.by(Constants.SORT_BY_NAME).ascending())).thenReturn(ontologies);

		// Act: Call the controller method
		ResponseEntity<List<SearchOntologyDTO>> result = this.controller.getOntologies();

		// Verifies & asserts
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(ontologies, result.getBody());
	}

	@Test
	void testGetOntologyClasses() {
		// Arrange
		Long ontologyId = 1L;
		List<String> ontologyClasses = Arrays.asList("Class1", "Class2", "Class3");

		// Configure mock for service
		Mockito.when(this.ontologyService.getOntologyClasses(ontologyId)).thenReturn(ontologyClasses);

		// Act: Call the controller method
		ResponseEntity<List<String>> result = this.controller.getOntologyClasses(ontologyId);

		// Verifies & asserts
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(ontologyClasses, result.getBody());
	}

	@Test
	void testGetClassProperties() {
		// Arrange
		Long ontologyId = 1L;
		String ontologyClass = "TestOntologyClass";
		PropertyDTO property = new PropertyDTO();
		property.setName("Property1");
		property.setPropertyType(PropertyTypeEnum.OBJECT);
		List<PropertyDTO> classProperties = new ArrayList<>();
		classProperties.add(property);

		// Configure mock for service
		Mockito.when(this.ontologyService.getClassProperties(ontologyId, ontologyClass)).thenReturn(classProperties);

		// Act: Call the controller method
		ResponseEntity<List<PropertyDTO>> result = this.controller.getClassProperties(ontologyId, ontologyClass);

		// Verifies & asserts
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(classProperties, result.getBody());
	}

	@Test
	void testGetNamespaceMap() {

		Long id = 1L;
		Map<String, String> namespaceMap = new HashMap<>();
		namespaceMap.put("solar", "https://w3id.org/solar/o/core#");
		namespaceMap.put("schema", "https://schema.org/");
		namespaceMap.put("skos", "http://www.w3.org/2004/02/skos/core#");

		Mockito.when(this.ontologyService.getNameSpaceMap(id)).thenReturn(namespaceMap);

		ResponseEntity<Map<String, String>> result = this.controller.getNameSpaceMap(id);

		assertEquals(namespaceMap, result.getBody());
	}

}
