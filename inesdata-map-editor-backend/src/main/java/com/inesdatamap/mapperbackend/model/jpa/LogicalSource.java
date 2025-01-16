package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Logical source entity representation
 *
 * @author gmv
 *
 */
@Getter
@Setter
@Entity
@Table(name = "LOGICAL_SOURCE")
public class LogicalSource extends BaseEntity implements Serializable {

	/**
	 * serialVersionUID for serialization
	 */
	private static final long serialVersionUID = 2258710450708360140L;

	/**
	 * The source of the logical source.
	 */
	@Column(name = "source")
	private String source;

	/**
	 * The reference formulation of the logical source.
	 */
	@Column(name = "reference_formulation")
	private String referenceFormulation;

	/**
	 * The iterator of the logical source.
	 */
	@Column(name = "iterator")
	private String iterator;

	/**
	 * The query associated with the logical source.
	 */
	@Column(name = "query")
	private String query;
}
