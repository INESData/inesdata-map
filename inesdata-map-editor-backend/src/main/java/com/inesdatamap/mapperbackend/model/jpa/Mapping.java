package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Mapping db entity representation
 *
 * @author gmv
 */
@Getter
@Setter
@Entity
@Table(name = "MAPPING")
public class Mapping extends BaseEntity implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4239287388230556730L;

	/**
	 * The name of the mapping.
	 */
	@Column(name = "name", nullable = false)
	private String name;

	/**
	 * Fields associated with the mapping.
	 */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "mapping_id", nullable = false)
	private List<MappingField> fields = new ArrayList<>();

	/**
	 * Executions associated to the materialization of a mapping.
	 */
	@OneToMany(mappedBy = "mapping", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Execution> executions = new ArrayList<>();

	/**
	 * Namespaces associated with the mapping.
	 */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "mapping_id", nullable = false)
	private List<Namespace> namespaces = new ArrayList<>();

	/**
	 * Resource Mapping Language (RML) associated with the mapping.
	 */
	@Lob
	@Column(name = "rml")
	@Basic(fetch = FetchType.LAZY)
	private byte[] rml;

	/**
	 * The base URL associated with the mapping
	 */
	@Size(max = 255)
	@Column(name = "base_url")
	private String baseUrl;

	/**
	 * The ontologies associated with the mapping.
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "mapping_ontology", joinColumns = @JoinColumn(name = "mapping_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "ontology_id", nullable = false))
	private Set<Ontology> ontologies = new HashSet<>();

}
