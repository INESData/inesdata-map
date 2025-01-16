package com.inesdatamap.mapperbackend.services;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import com.inesdatamap.mapperbackend.model.dto.OntologyDTO;
import com.inesdatamap.mapperbackend.model.dto.PropertyDTO;
import com.inesdatamap.mapperbackend.model.dto.SearchOntologyDTO;
import com.inesdatamap.mapperbackend.model.jpa.Ontology;

/**
 * Service interface for managing ontologies.
 */

public interface OntologyService {

	/**
	 * Gets entity by its id.
	 *
	 * @param id
	 *            the ID of the ontology to get
	 * @return Ontology
	 */
	Ontology getEntity(Long id);

	/**
	 * Retrieves all ontologies paginated.
	 *
	 * @param pageable
	 *            pageable
	 *
	 * @return List of ontologies paginated
	 */
	Page<SearchOntologyDTO> listOntologies(Pageable pageable);

	/**
	 * Updates an ontology identified by its ID.
	 *
	 * @param id
	 *            the ID of the ontology to update
	 * @param ontologyDto
	 *            the OntologyDTO
	 *
	 * @return the updated ontology
	 */
	OntologyDTO updateOntology(Long id, OntologyDTO ontologyDto);

	/**
	 * Deletes an ontology by its id.
	 *
	 * @param id
	 *            the ID of the ontology to delete
	 */
	void deleteOntology(Long id);

	/**
	 * Saves an ontology
	 *
	 * @param ontologyDto
	 *            the OntologyDTO
	 * @param file
	 *            file content to save
	 * @return the saved ontology
	 */
	OntologyDTO createOntology(OntologyDTO ontologyDto, MultipartFile file);

	/**
	 * Retrieves all ontologies.
	 *
	 * @param ascending
	 *            ascending
	 *
	 * @return List of ontologies
	 */
	List<SearchOntologyDTO> getOntologies(Sort ascending);

	/**
	 * Gets all ontology classes.
	 *
	 * @param id
	 *            ontology identifier
	 *
	 * @return Ontology classes
	 */
	List<String> getOntologyClasses(Long id);

	/**
	 * Gets all ontology class properties.
	 *
	 * @param id
	 *            ontology identifier
	 * @param ontologyClass
	 *            ontology class
	 *
	 * @return Ontology class properties
	 */
	List<PropertyDTO> getClassProperties(Long id, String ontologyClass);

	/**
	 * Gets ontology namespace map
	 *
	 * @param id
	 *            ontology identifier
	 *
	 * @return ontology namespace map
	 */
	Map<String, String> getNameSpaceMap(Long id);

}
