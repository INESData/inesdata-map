package com.inesdatamap.mapperbackend.repositories.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inesdatamap.mapperbackend.model.enums.DataFileTypeEnum;
import com.inesdatamap.mapperbackend.model.jpa.FileSource;

/**
 * Repository interface for managing FileSource entities.
 *
 */
@Repository
public interface FileSourceRepository extends JpaRepository<FileSource, Long> {

	/**
	 * Return all file sources by type
	 *
	 * @param fileType
	 *            fileType
	 * @return List of data sources by type
	 */
	List<FileSource> findByFileTypeOrderByNameAsc(DataFileTypeEnum fileType);

}
