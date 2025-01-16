package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Execution db entity representation
 */
@Getter
@Setter
@Entity
@Table(name = "EXECUTION")
public class Execution extends BaseEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = -6205501422386913819L;

	/**
	 * The execution date
	 */
	@Column(name = "execution_date", nullable = false)
	private OffsetDateTime executionDate;

	/**
	 * The mapping file name
	 */
	@Column(name = "mapping_file_name", nullable = false)
	private String mappingFileName;

	/**
	 * The knowledge graph file name
	 */
	@Column(name = "knowledge_graph_file_name", nullable = false)
	private String knowledgeGraphFileName;

	/**
	 * The log file name
	 */
	@Column(name = "log_file_name", nullable = false)
	private String logFileName;

	/**
	 * The mapping associated with the execution
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "mapping_id", nullable = false)
	private Mapping mapping;
}
