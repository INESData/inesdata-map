package com.inesdatamap.mapperbackend.services;

import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inesdatamap.mapperbackend.model.dto.ExecutionDTO;
import com.inesdatamap.mapperbackend.model.jpa.Execution;

/**
 * Service for operations related to the execution of mappings
 */
public interface ExecutionService {

	/**
	 * Save an execution
	 *
	 * @param execution
	 * 	the execution to save
	 *
	 * @return the saved execution
	 */
	Execution save(Execution execution);

	/**
	 * Retrieves all executions for a mapping.
	 *
	 * @param mappingId
	 * 	the mapping ID
	 * @param pageable
	 * 	the pageable
	 *
	 * @return List of executions
	 */
	Page<ExecutionDTO> listExecutions(Long mappingId, Pageable pageable);

	/**
	 * Gets the file for the given execution
	 *
	 * @param id
	 * 	the execution id
	 * @param filename
	 * 	the file name
	 *
	 * @return the file
	 *
	 * @throws MalformedURLException
	 * 	if the URL is malformed
	 */
	Resource getFile(Long id, String filename) throws MalformedURLException;
}
