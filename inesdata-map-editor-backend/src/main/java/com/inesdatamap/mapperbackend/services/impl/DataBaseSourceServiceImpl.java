package com.inesdatamap.mapperbackend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inesdatamap.mapperbackend.model.dto.DataBaseSourceDTO;
import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.model.jpa.DataBaseSource;
import com.inesdatamap.mapperbackend.model.jpa.DataSource;
import com.inesdatamap.mapperbackend.model.mappers.DataBaseSourceMapper;
import com.inesdatamap.mapperbackend.model.mappers.DataSourceMapper;
import com.inesdatamap.mapperbackend.repositories.jpa.DataBaseSourceRepository;
import com.inesdatamap.mapperbackend.repositories.jpa.DataSourceRepository;
import com.inesdatamap.mapperbackend.services.DataBaseSourceService;

import jakarta.persistence.EntityNotFoundException;

/**
 * Data base source service implementation
 *
 * @see DataBaseSourceService
 */
@Service
@Transactional
public class DataBaseSourceServiceImpl implements DataBaseSourceService {

	@Autowired
	private DataSourceMapper dataSourceMapper;

	@Autowired
	private DataBaseSourceMapper dataBaseSourceMapper;

	@Autowired
	private DataSourceRepository<DataSource> dataSourceRepository;

	@Autowired
	private DataBaseSourceRepository dataBaseSourceRepository;

	/**
	 * Creates a new data base source from the provided DataBaseSourceDTO.
	 *
	 * @param dataBaseSourceDTO
	 *            The DataBaseSourceDTO
	 * @return the new data base source.
	 */
	@Override
	public DataSourceDTO createDataBaseSource(DataBaseSourceDTO dataBaseSourceDTO) {

		if (dataBaseSourceDTO == null) {
			throw new IllegalArgumentException("The data base source has no data");
		}

		// DTO to entity
		DataBaseSource dataBaseSource = this.dataBaseSourceMapper.dtoToEntity(dataBaseSourceDTO);

		// Save new entity
		DataBaseSource savedDataSource = this.dataSourceRepository.save(dataBaseSource);
		return this.dataBaseSourceMapper.toDTO(savedDataSource);

	}

	/**
	 * Updates an existing data base source with the provided DataBaseSourceDTO.
	 *
	 * @param id
	 *            The identifier of the data base source to be updated
	 * @param dataBaseSourceDTO
	 *            The DataBaseSourceDTO
	 * @return the updated data base source.
	 */
	@Override
	public DataSourceDTO updateDataBaseSource(Long id, DataBaseSourceDTO dataBaseSourceDTO) {

		if (dataBaseSourceDTO == null) {
			throw new IllegalArgumentException("The data base source has no data");
		}

		// Get DB entity
		DataBaseSource dataSourceDB = this.getEntity(id);

		// Update existing entity with new DTO values
		this.dataBaseSourceMapper.merge(dataBaseSourceDTO, dataSourceDB);

		// Updated data source
		DataBaseSource datasourceUpdated = this.dataBaseSourceRepository.save(dataSourceDB);
		return this.dataSourceMapper.entityToDto(datasourceUpdated);

	}

	/**
	 * Retrieves a data base source entity by its ID.
	 *
	 * @param id
	 *            the ID of the data base source to retrieve
	 * @return the data base source entity corresponding to the given ID
	 */
	@Override
	public DataBaseSource getEntity(Long id) {
		return this.dataBaseSourceRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id.toString()));
	}

	/**
	 * Retrieves a DataBaseSourceDTO by its identifier.
	 *
	 * @param id
	 *            the unique identifier of the data base source entity
	 * @return the data base source dto corresponding to the given ID
	 */
	@Override
	public DataBaseSourceDTO getDataBaseSourceById(Long id) {
		return this.dataBaseSourceMapper.toDTO(this.getEntity(id));
	}

}
