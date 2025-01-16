package com.inesdatamap.mapperbackend.model.dto;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing an Execution.
 */
@Getter
@Setter
public class ExecutionDTO extends BaseEntityDTO {

	/**
	 * The execution date
	 */
	private OffsetDateTime executionDate;

	/**
	 * The mapping file name
	 */
	private String mappingFileName;

	/**
	 * The knowledge graph file name
	 */
	private String knowledgeGraphFileName;

	/**
	 * The log file name
	 */
	private String logFileName;

}
