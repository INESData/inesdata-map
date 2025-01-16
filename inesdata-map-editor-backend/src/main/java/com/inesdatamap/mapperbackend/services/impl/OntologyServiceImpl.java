package com.inesdatamap.mapperbackend.services.impl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.inesdatamap.mapperbackend.exceptions.OntologyParserException;
import com.inesdatamap.mapperbackend.model.dto.OntologyDTO;
import com.inesdatamap.mapperbackend.model.dto.PropertyDTO;
import com.inesdatamap.mapperbackend.model.dto.SearchOntologyDTO;
import com.inesdatamap.mapperbackend.model.jpa.Mapping;
import com.inesdatamap.mapperbackend.model.jpa.Ontology;
import com.inesdatamap.mapperbackend.model.mappers.OntologyMapper;
import com.inesdatamap.mapperbackend.repositories.jpa.MappingRepository;
import com.inesdatamap.mapperbackend.repositories.jpa.OntologyRepository;
import com.inesdatamap.mapperbackend.services.OntologyService;
import com.inesdatamap.mapperbackend.utils.FileUtils;
import com.inesdatamap.mapperbackend.utils.NameSpaceUtils;
import com.inesdatamap.mapperbackend.utils.OWLUtils;

import jakarta.persistence.EntityNotFoundException;

/**
 * Implementation of the OntologyService interface.
 *
 */
@Service
public class OntologyServiceImpl implements OntologyService {

	@Autowired
	private OntologyRepository ontologyRepo;

	@Autowired
	private OntologyMapper ontologyMapper;

	@Autowired
	private MappingRepository mappingRepo;

	/**
	 * Retrieves a list of all ontologies and maps them to their corresponding DTOs.
	 *
	 * @return List of OntologyDTOs
	 */
	@Override
	public Page<SearchOntologyDTO> listOntologies(Pageable pageable) {

		Page<Ontology> ontologiesList = this.ontologyRepo.findAll(pageable);

		return ontologiesList.map(this.ontologyMapper::entitytoSearchOntologyDTO);

	}

	/**
	 * Updates an ontology by its ID.
	 *
	 * @param id
	 *            the ID of the ontology to update
	 * @param ontologyDto
	 *            the OntologyDTO
	 * @return the updated ontology
	 */
	@Override
	public OntologyDTO updateOntology(Long id, OntologyDTO ontologyDto) {

		if (ontologyDto == null) {
			throw new IllegalArgumentException("The ontology has no data to update");
		}

		// Get DB entity
		Ontology ontologyDB = this.getEntity(id);

		// New ontology to save
		Ontology ontologySource = this.ontologyMapper.dtoToEntity(ontologyDto);

		// Updated ontology
		Ontology ontologyUpdated = this.ontologyRepo.saveAndFlush(this.ontologyMapper.merge(ontologySource, ontologyDB));

		return this.ontologyMapper.entityToDto(ontologyUpdated);

	}

	/**
	 * Deletes an ontology by its id.
	 *
	 * @param id
	 *            the ID of the ontology to delete
	 */
	@Override
	public void deleteOntology(Long id) {

		// Get entity if exists
		Ontology ontology = this.getEntity(id);

		// Check if ontology is being used by any mapping
		List<Mapping> mappingsUsingOntology = this.mappingRepo.findAllByOntologiesContaining(ontology);
		if (!mappingsUsingOntology.isEmpty()) {
			throw new IllegalArgumentException("Ontology is being used in one or more mappings and it can not be deleted");
		}

		this.ontologyRepo.deleteById(id);

	}

	/**
	 * Saves an ontology
	 *
	 * @param ontologyDto
	 *            the OntologyDTO
	 * @return the saved ontology
	 */
	@Override
	public OntologyDTO createOntology(OntologyDTO ontologyDto, MultipartFile file) {

		if (ontologyDto == null) {
			throw new IllegalArgumentException("The ontology has no data");
		}

		// DTO to entity
		Ontology ontology = this.ontologyMapper.dtoToEntity(ontologyDto);

		if (file != null && !file.isEmpty()) {

			// Read file content
			FileUtils.processFileContent(file, ontology);

		}

		// Save new entity
		Ontology savedOntology = this.ontologyRepo.save(ontology);

		return this.ontologyMapper.entityToDto(savedOntology);

	}

	/**
	 * Retrieves an ontology entity by its ID.
	 *
	 * @param id
	 *            the ID of the ontology to retrieve
	 * @return the ontology entity corresponding to the given ID
	 */
	@Override
	public Ontology getEntity(Long id) {
		return this.ontologyRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id.toString()));
	}

	@Override
	public List<SearchOntologyDTO> getOntologies(Sort nameSort) {

		List<Ontology> ontologiesList = this.ontologyRepo.findAll(nameSort);

		return this.ontologyMapper.entitytoSearchOntologyDTO(ontologiesList);

	}

	/**
	 * Retrieves all class names from the ontology specified by the given ID.
	 *
	 * @param id
	 *            the unique identifier of the ontology entity
	 * @return a list of class names extracted from the ontology
	 * @throws OntologyParserException
	 *             if there is an error retrieving or processing the ontology, including errors from loading the ontology
	 */
	@Override
	public List<String> getOntologyClasses(Long id) {

		List<String> classList = new ArrayList<>();

		try {

			// Get entity from DB
			Ontology ontology = this.getEntity(id);

			// Read ontology file content
			String ontologyContent = this.getOntologyContent(ontology);

			// Create OWLOntologyManager instance and load the ontology
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLOntology owl = manager.loadOntologyFromOntologyDocument(new StringDocumentSource(ontologyContent));

			// Get all classes in ontology and return list
			classList = OWLUtils.getClasses(owl);
			Collections.sort(classList);

		} catch (OWLOntologyCreationException e) {
			throw new OntologyParserException("Failed getting classes from ontology with id: " + id, e);
		}

		return classList;

	}

	/**
	 * Retrieves a list of properties for a specified class from an ontology identified by its ID.
	 *
	 * @param id
	 *            The ID of the ontology entity to retrieve.
	 * @param className
	 *            The name of the class whose properties are to be retrieved.
	 * @return A list of properties for the specified class from the ontology.
	 *
	 */
	@Override
	public List<PropertyDTO> getClassProperties(Long id, String className) {

		// Get entity
		Ontology ontology = this.getEntity(id);

		// Read ontology file content
		String ontologyContent = this.getOntologyContent(ontology);

		try {
			return OWLUtils.getProperties(ontologyContent, className);
		} catch (OWLOntologyCreationException e) {
			throw new OntologyParserException("Failed getting properties from ontology class: " + className, e);
		}
	}

	/**
	 * Retrieves the content of the ontology as a string. Converts the byte array content stored in the ontology to its original string
	 * representation using UTF-8 encoding.
	 *
	 * @param ontology
	 *            the Ontology entity containing the content as a byte array
	 * @return the content of the ontology as a string in UTF-8 format
	 */
	public String getOntologyContent(Ontology ontology) {

		// Validate ontology is not null
		if (ontology.getContent() == null) {
			throw new IllegalArgumentException("Ontology has no content.");
		}

		// Get ontology bytes
		byte[] contentBytes = ontology.getContent();

		// Convert bytes to String
		return new String(contentBytes, StandardCharsets.UTF_8);
	}

	/**
	 * Retrieves a map of namespaces and their prefixes from an ontology
	 *
	 * @param id
	 *            the identifier of the ontology
	 * @return a map where the keys are prefix strings and the values are the corresponding namespace URIs
	 */
	@Override
	public Map<String, String> getNameSpaceMap(Long id) {

		Ontology ontology = this.getEntity(id);
		String ontologyContent = this.getOntologyContent(ontology);

		// Create OWLOntologyManager instance
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		try {
			// Load ontology
			OWLOntology owlOntology = manager.loadOntologyFromOntologyDocument(new StringDocumentSource(ontologyContent));

			return NameSpaceUtils.getPrefixNamespaceMap(owlOntology);
		} catch (OWLOntologyCreationException e) {
			throw new OntologyParserException("Failed getting namespace map from ontology with id: " + id, e);
		}
	}
}
