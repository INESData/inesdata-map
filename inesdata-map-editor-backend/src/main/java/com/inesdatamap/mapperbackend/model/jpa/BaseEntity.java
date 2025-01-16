package com.inesdatamap.mapperbackend.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

/**
 * Base Entity
 *
 * @author gmv
 *
 */
@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

	/**
	 * Entity ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	protected Long id;

	/**
	 * Entity version. Zero by default
	 */
	@Version
	@Column(name = "version", columnDefinition = "bigint default 0")
	protected Long version;

}
