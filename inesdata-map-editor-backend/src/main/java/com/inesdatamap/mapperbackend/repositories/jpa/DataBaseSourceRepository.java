package com.inesdatamap.mapperbackend.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inesdatamap.mapperbackend.model.jpa.DataBaseSource;

/**
 * Repository interface for managing DataBaseSource entities.
 *
 */
@Repository
public interface DataBaseSourceRepository extends JpaRepository<DataBaseSource, Long> {

}
