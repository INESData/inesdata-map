package com.inesdatamap.mapperbackend.services.impl;

import java.io.File;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.model.jpa.DataBaseSource;
import com.inesdatamap.mapperbackend.model.jpa.DataSource;
import com.inesdatamap.mapperbackend.model.mappers.DataSourceMapper;
import com.inesdatamap.mapperbackend.model.routing.ClientDataSourceRouter;
import com.inesdatamap.mapperbackend.properties.AppProperties;
import com.inesdatamap.mapperbackend.repositories.jpa.DataSourceRepository;
import com.inesdatamap.mapperbackend.repositories.jpa.MappingFieldRepository;
import com.inesdatamap.mapperbackend.services.DataSourceService;
import com.inesdatamap.mapperbackend.utils.Constants;
import com.inesdatamap.mapperbackend.utils.FileUtils;

import jakarta.persistence.EntityNotFoundException;

/**
 * Data source service implementation
 *
 * @see DataSourceService
 */
@Service
@Transactional
public class DataSourceServiceImpl implements DataSourceService {

	@Autowired
	private DataSourceRepository<DataBaseSource> dataBaseSourceRepository;

	@Autowired
	private DataSourceRepository<DataSource> dataSourceRepository;

	@Autowired
	private MappingFieldRepository mappingFieldRepo;

	@Autowired
	private DataSourceMapper dataSourceMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AppProperties appProperties;

	@Override
	public DataBaseSource findById(Long dataSourceId) {
		return this.dataBaseSourceRepository.findById(dataSourceId).orElseThrow(() -> new EntityNotFoundException(dataSourceId.toString()));
	}

	@Override
	public javax.sql.DataSource getClientDataSource(Long dataSourceId) {

		DataBaseSource ds = this.findById(dataSourceId);
		ClientDataSourceRouter router = new ClientDataSourceRouter();

		return router.getDatasource(dataSourceId, ds.getConnectionString(), ds.getDatabaseType(), ds.getUserName(), ds.getPassword());
	}

	/**
	 * Retrieves a list of all data sources and maps them to their corresponding DTOs.
	 *
	 * @return List of data sources
	 */
	@Override
	public Page<DataSourceDTO> listDataSources(Pageable pageable) {
		Page<DataSource> dataSourcesPage = this.dataSourceRepository.findAll(pageable);

		return dataSourcesPage.map(this.dataSourceMapper::dataSourceToDTO);

	}

	/**
	 * Deletes a data source by its id.
	 *
	 * @param id
	 * 	the ID of the data source to delete
	 */
	@Override
	public void deleteDataSource(Long id) {

		// Get entity if exists
		this.getEntity(id);

		// Delete files related to the data source
		this.deleteDataSourceFiles(id);

		// Check if data source is being used by any mapping
		boolean isInUse = this.mappingFieldRepo.existsBySourceId(id);
		if (isInUse) {
			throw new IllegalArgumentException("The data source is being used in one or more mappings and it can not be deleted");
		}

		this.dataSourceRepository.deleteById(id);

	}

	/**
	 * Retrieves a data source entity by its ID.
	 *
	 * @param id
	 * 	the ID of the data source to retrieve
	 *
	 * @return the data source entity corresponding to the given ID
	 */
	@Override
	public DataSource getEntity(Long id) {
		return this.dataSourceRepository.findById(id).orElseThrow(
			() -> new EntityNotFoundException("Entity not found with id: " + id.toString()));
	}

	/**
	 * Deletes files related to the data source.
	 *
	 * @param id
	 * 	the ID of the data source
	 */
	private void deleteDataSourceFiles(Long id) {

		String dataSourceFolderPath = String.join(File.separator, appProperties.getDataProcessingPath(), Constants.DATA_INPUT_FOLDER_NAME,
			id.toString());

		FileUtils.deleteDirectory(Paths.get(dataSourceFolderPath));

	}

}
