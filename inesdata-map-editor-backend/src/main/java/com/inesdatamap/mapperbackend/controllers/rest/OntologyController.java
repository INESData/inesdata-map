package com.inesdatamap.mapperbackend.controllers.rest;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.inesdatamap.mapperbackend.model.dto.OntologyDTO;
import com.inesdatamap.mapperbackend.model.dto.PropertyDTO;
import com.inesdatamap.mapperbackend.model.dto.SearchOntologyDTO;
import com.inesdatamap.mapperbackend.services.OntologyService;
import com.inesdatamap.mapperbackend.utils.Constants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST Controller for Ontology
 */
@RestController()
@RequestMapping("/ontologies")
@Tag(name = "Ontology", description = "Manage ontologies")
@Validated
public class OntologyController {

	private OntologyService ontologyService;

	/**
	 * This constructor initializes the controller with the provided OntologyService
	 *
	 * @param ontologyService
	 *            ontology service
	 */
	public OntologyController(OntologyService ontologyService) {
		this.ontologyService = ontologyService;
	}

	/**
	 * Lists all ontologies paginated.
	 *
	 * @param page
	 *            page number
	 *
	 * @param size
	 *            page size
	 *
	 * @return List of all ontologies
	 */
	@GetMapping(path = "paged")
	@Operation(summary = "List all ontologies paged")
	public ResponseEntity<Page<SearchOntologyDTO>> listOntologies(@RequestParam int page, @RequestParam int size) {
		Page<SearchOntologyDTO> ontologies = this.ontologyService
				.listOntologies(PageRequest.of(page, size, Sort.by(Constants.SORT_BY_NAME).ascending()));
		return ResponseEntity.ok(ontologies);
	}

	/**
	 * Updates the given ontology
	 *
	 * @param id
	 *            ontology identifier
	 * @param ontologyDto
	 *            to update
	 * @return updated ontology
	 */
	@PutMapping("/{id}")
	@Operation(summary = "Update given ontology")
	public ResponseEntity<OntologyDTO> updateOntology(
			@PathVariable(name = "id") @Parameter(name = "id", description = "Ontology identifier to update", required = true) Long id,
			@RequestBody @Parameter(name = "ontology", description = "The ontology to update", required = true) OntologyDTO ontologyDto) {
		return ResponseEntity.ok(this.ontologyService.updateOntology(id, ontologyDto));
	}

	/**
	 * Deletes ontology
	 *
	 * @param id
	 *            ontology identifier
	 * @return status code response
	 */
	@DeleteMapping("/{id}")
	@Operation(summary = "Delete ontology")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> deleteOntology(
			@PathVariable(name = "id") @Parameter(name = "id", description = "Ontology identifier to delete", required = true) Long id) {
		this.ontologyService.deleteOntology(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Saves the given ontology
	 *
	 * @param ontologyDto
	 *            to save
	 * @param file
	 *            file content to save
	 * @return saved ontology
	 */
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Create given ontology")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<OntologyDTO> createOntology(
			@RequestPart("body") @Parameter(name = "ontology", description = "The ontology to save", required = true) OntologyDTO ontologyDto,
			@RequestPart(value = "file", required = false) MultipartFile file) {
		OntologyDTO createdOntology = this.ontologyService.createOntology(ontologyDto, file);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdOntology);
	}

	/**
	 * Gets all ontologies.
	 *
	 * @return Gets all ontologies
	 */
	@GetMapping(path = "")
	@Operation(summary = "Get all ontologies")
	public ResponseEntity<List<SearchOntologyDTO>> getOntologies() {
		List<SearchOntologyDTO> ontologies = this.ontologyService.getOntologies(Sort.by(Constants.SORT_BY_NAME).ascending());
		return ResponseEntity.ok(ontologies);
	}

	/**
	 * Gets all ontology classes.
	 *
	 * @param id
	 *            ontology identifier
	 *
	 * @return Ontology classes
	 */
	@GetMapping("/{id}")
	@Operation(summary = "Get all ontology classes")
	public ResponseEntity<List<String>> getOntologyClasses(
			@PathVariable(name = "id") @Parameter(name = "id", description = "Ontology identifier", required = true) Long id) {
		List<String> classes = this.ontologyService.getOntologyClasses(id);
		return ResponseEntity.ok(classes);
	}

	/**
	 * Gets all properties of the class
	 *
	 * @param id
	 *            ontology identifier
	 *
	 * @param ontologyClass
	 *            ontology class
	 *
	 * @return Ontology class properties
	 */
	@GetMapping("/{id}/{ontologyClass}")
	@Operation(summary = "Get all properties of the class")
	public ResponseEntity<List<PropertyDTO>> getClassProperties(
			@PathVariable(name = "id") @Parameter(name = "id", description = "Ontology identifier", required = true) Long id,
			@PathVariable(name = "ontologyClass") @Parameter(name = "ontologyClass", description = "Ontology class", required = true) String ontologyClass) {
		List<PropertyDTO> classes = this.ontologyService.getClassProperties(id, ontologyClass);
		return ResponseEntity.ok(classes);
	}

	/**
	 * Gets ontology namespace map
	 *
	 * @param id
	 *            ontology identifier
	 *
	 * @return ontology namespace map
	 */
	@GetMapping("/{id}/namespaces")
	@Operation(summary = "Get ontology namespace and prefix map")
	public ResponseEntity<Map<String, String>> getNameSpaceMap(
			@PathVariable(name = "id") @Parameter(name = "id", description = "Ontology identifier", required = true) Long id) {
		java.util.Map<String, String> map = this.ontologyService.getNameSpaceMap(id);
		return ResponseEntity.ok(map);
	}
}
