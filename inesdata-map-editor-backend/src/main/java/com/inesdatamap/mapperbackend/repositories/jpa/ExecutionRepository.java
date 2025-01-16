package com.inesdatamap.mapperbackend.repositories.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inesdatamap.mapperbackend.model.jpa.Execution;

/**
 * Repository interface for managing {@link com.inesdatamap.mapperbackend.model.jpa.Execution} entities.
 */
@Repository
public interface ExecutionRepository extends JpaRepository<Execution, Long> {

	/**
	 * Retrieves all executions for a mapping.
	 *
	 * @param mappingId
	 * 	the mapping ID
	 * @param pageable
	 * 	the pageable
	 *
	 * @return the list of executions
	 */
	Page<Execution> findByMappingId(Long mappingId, Pageable pageable);

}
