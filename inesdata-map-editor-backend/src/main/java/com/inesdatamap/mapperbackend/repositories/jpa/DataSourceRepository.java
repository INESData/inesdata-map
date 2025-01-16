package com.inesdatamap.mapperbackend.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inesdatamap.mapperbackend.model.jpa.DataSource;

/**
 * Data source repository
 *
 * @param <T>
 * 	the data source type
 */
@Repository
public interface DataSourceRepository<T extends DataSource> extends JpaRepository<T, Long> {}
