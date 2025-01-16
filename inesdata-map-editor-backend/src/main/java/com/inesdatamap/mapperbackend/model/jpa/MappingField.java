package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * MappingField db entity representation
 *
 * @author gmv
 */
@Getter
@Setter
@Entity
@Table(name = "MAPPING_FIELD")
public class MappingField extends BaseEntity implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8225534420024816076L;

	/**
	 * The logical table associated with the mapping field.
	 */
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "logical_table_id")
	private LogicalTable logicalTable;

	/**
	 * The logical source associated with the mapping field.
	 */
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "logical_source_id")
	private LogicalSource logicalSource;

	/**
	 * The subject associated with the mapping field.
	 */
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "subject_id")
	private SubjectMap subject;

	/**
	 * The predicates associated with the mapping field.
	 */
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "mapping_field_id", nullable = false)
	private List<PredicateObjectMap> predicates = new ArrayList<>();

	/**
	 * The source associated with the mapping field.
	 */
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "source_id", nullable = false)
	private DataSource source;
}
