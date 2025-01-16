package com.inesdatamap.mapperbackend.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inesdatamap.mapperbackend.model.jpa.MappingField;

/**
 * Repository interface for managing Mapping Field entities.
 *
 */
@Repository
public interface MappingFieldRepository extends JpaRepository<MappingField, Long> {

	/**
	 * Check if any mapping field uses the data source
	 *
	 * @param sourceId
	 *            source id
	 * @return true if mapping field is using the data source, false if not
	 */
	boolean existsBySourceId(Long sourceId);

}
