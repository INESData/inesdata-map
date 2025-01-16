package com.inesdatamap.mapperbackend.repositories.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inesdatamap.mapperbackend.model.jpa.Ontology;

/**
 * Repository interface for managing Ontology entities.
 *
 */
@Repository
public interface OntologyRepository extends JpaRepository<Ontology, Long> {

	@Override
	Page<Ontology> findAll(Pageable pageable);

}
