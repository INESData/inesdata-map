package com.inesdatamap.mapperbackend.model.routing;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.inesdatamap.mapperbackend.model.enums.DataBaseTypeEnum;

/**
 * Router for the client data source
 */
public class ClientDataSourceRouter extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return ClientDatabaseContextHolder.get();
	}

	/**
	 * Create a data source
	 *
	 * @param dataSourceId
	 * 	the data source id
	 * @param url
	 * 	the data source url
	 * @param dbType
	 * 	the data source type
	 * @param username
	 * 	the data source username
	 * @param password
	 * 	the data source password
	 *
	 * @return the configured data source
	 */
	public DataSource getDatasource(Long dataSourceId, String url, DataBaseTypeEnum dbType, String username, String password) {

		Map<Object, Object> dataSourceMap = new HashMap<>();

		DataSource ds = DataSourceBuilder.create().driverClassName(getDriver(dbType)).url(url).username(username).password(password)
			.build();
		dataSourceMap.put(dataSourceId, ds);

		this.setTargetDataSources(dataSourceMap);
		this.setDefaultTargetDataSource(ds);

		return ds;
	}

	/**
	 * Get the driver for the data source
	 *
	 * @param dbType
	 * 	the database type
	 *
	 * @return the driver
	 */
	private static String getDriver(DataBaseTypeEnum dbType) {
		return switch (dbType) {
			case H2 -> "org.h2.Driver";
			case MYSQL -> "com.mysql.cj.jdbc.Driver";
			case POSTGRESQL -> "org.postgresql.Driver";
			default -> throw new IllegalArgumentException("Invalid database type");
		};
	}
}
