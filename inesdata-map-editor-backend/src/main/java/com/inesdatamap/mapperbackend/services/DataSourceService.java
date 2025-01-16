package com.inesdatamap.mapperbackend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.model.jpa.DataBaseSource;
import com.inesdatamap.mapperbackend.model.jpa.DataSource;

/**
 * Data source service
 */
public interface DataSourceService {

	/**
	 * Find a data source by id
	 *
	 * @param dataSourceId
	 *            the data source id
	 *
	 * @return the data source
	 */
	DataBaseSource findById(Long dataSourceId);

	/**
	 * Get the client data source
	 *
	 * @param dataSourceId
	 *            the data source id
	 *
	 * @return the configured client data source
	 */
	javax.sql.DataSource getClientDataSource(Long dataSourceId);

	/**
	 * Gets entity by its id.
	 *
	 * @param id
	 *            the ID of the data source to get
	 * @return DataSource
	 */
	DataSource getEntity(Long id);

	/**
	 * Retrieves all data sources.
	 *
	 * @param pageable
	 *            pageable
	 *
	 * @return List of data sources
	 */
	Page<DataSourceDTO> listDataSources(Pageable pageable);

	/**
	 * Deletes a data source by its id.
	 *
	 * @param id
	 *            the ID of the data source to delete
	 */
	void deleteDataSource(Long id);
}
