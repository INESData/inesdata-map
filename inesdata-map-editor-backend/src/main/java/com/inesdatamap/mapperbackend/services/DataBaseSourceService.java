package com.inesdatamap.mapperbackend.services;

import com.inesdatamap.mapperbackend.model.dto.DataBaseSourceDTO;
import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.model.jpa.DataSource;

/**
 * Data base source service
 */
public interface DataBaseSourceService {

	/**
	 * Saves a data base source
	 *
	 * @param dataBaseSourceDTO
	 *            the database source
	 * @return the saved data base source
	 */
	DataSourceDTO createDataBaseSource(DataBaseSourceDTO dataBaseSourceDTO);

	/**
	 * Updates a data base source identified by its ID.
	 *
	 * @param id
	 *            the ID of the data source to update
	 * @param dataBaseSourceDTO
	 *            the DataBaseSourceDTO
	 * @return the updated data source
	 */
	DataSourceDTO updateDataBaseSource(Long id, DataBaseSourceDTO dataBaseSourceDTO);

	/**
	 * Retrieves a data source entity by its ID.
	 *
	 * @param id
	 *            the ID of the data source to retrieve
	 * @return the data source entity corresponding to the given ID
	 */
	DataSource getEntity(Long id);

	/**
	 * Get data base source by id
	 *
	 * @param id
	 *            Id
	 * @return The data base source
	 */
	DataBaseSourceDTO getDataBaseSourceById(Long id);

}
