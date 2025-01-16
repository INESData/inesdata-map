package com.inesdatamap.mapperbackend.repositories.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inesdatamap.mapperbackend.model.jpa.Mapping;
import com.inesdatamap.mapperbackend.model.jpa.Ontology;

/**
 * Repository interface for managing Mapping entities.
 *
 */
@Repository
public interface MappingRepository extends JpaRepository<Mapping, Long> {

	/**
	 * Return all mappings containing the ontology
	 *
	 * @param ontology
	 *            Ontology
	 * @return List of mapping
	 */
	List<Mapping> findAllByOntologiesContaining(Ontology ontology);
}
