package com.inesdatamap.mapperbackend.services;

import java.util.List;

import javax.sql.DataSource;

/**
 * Service for client data source operations.
 */
public interface ClientDataSourceService {

	/**
	 * Get table names from a database data source.
	 *
	 * @param dataSourceId
	 * 	the data source id
	 * @param dataSource
	 * 	the configured data source
	 *
	 * @return the list of table names
	 */
	List<String> getTableNames(Long dataSourceId, DataSource dataSource);

	/**
	 * Get column names from a database table.
	 *
	 * @param dataSourceId
	 * 	the data source id
	 * @param table
	 * 	the table name
	 * @param dataSource
	 * 	the configured data source
	 *
	 * @return the list of column names
	 */
	List<String> getColumnNames(Long dataSourceId, String table, DataSource dataSource);
}
