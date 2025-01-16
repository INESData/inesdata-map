package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Logical table entity representation
 *
 * @author gmv
 *
 */
@Getter
@Setter
@Entity
@Table(name = "LOGICAL_TABLE")
public class LogicalTable extends BaseEntity implements Serializable {

	/**
	 * serialVersionUID for serialization
	 */
	private static final long serialVersionUID = -2497821547060275570L;

	/**
	 * The name of the logical table.
	 */
	@Column(name = "table_name")
	private String tableName;

	/**
	 * The query associated with the logical table.
	 */
	@Column(name = "query")
	private String query;
}
