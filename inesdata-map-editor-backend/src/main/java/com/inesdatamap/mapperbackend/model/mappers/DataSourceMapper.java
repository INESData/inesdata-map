package com.inesdatamap.mapperbackend.model.mappers;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.model.jpa.DataBaseSource;
import com.inesdatamap.mapperbackend.model.jpa.DataSource;
import com.inesdatamap.mapperbackend.model.jpa.FileSource;

/**
 * Mapper interface for converting between DataSource and DataSourceDTO.
 *
 */
@Mapper(componentModel = "spring", uses = BaseEntityMapper.class)
public abstract class DataSourceMapper implements BaseEntityMapper<DataSourceDTO, DataSource> {

	/**
	 * Password encoder used to encrypt passwords before storing them.
	 */
	@Autowired
	protected PasswordEncoder passwordEncoder;

	/**
	 * Converts a DataSource entity to its corresponding DataSourceDTO.
	 *
	 * This method checks the type of the given entity and uses the appropriate
	 *
	 * conversion method for DataBaseSource or FileSource.
	 *
	 * @param entity
	 *            the DataSource entity to be converted.
	 * @return the corresponding DataSourceDTO for the given entity.
	 */
	public DataSourceDTO dataSourceToDTO(DataSource entity) {
		if (entity instanceof DataBaseSource database) {
			return this.dataBaseToDTO(database);
		} else if (entity instanceof FileSource fileSource) {
			return this.dataFileToDTO(fileSource);

		}
		return null;
	}

	/**
	 * Converts a DataBaseSource entity to its corresponding DataSourceDTO.
	 *
	 * @param dataBaseSource
	 *            the DataBaseSource entity to be converted.
	 * @return the corresponding DataSourceDTO.
	 */
	public abstract DataSourceDTO dataBaseToDTO(DataBaseSource dataBaseSource);

	/**
	 * Converts a FileSource entity to its corresponding DataSourceDTO.
	 *
	 * @param fileSource
	 *            the FileSource entity to be converted.
	 * @return the corresponding DataSourceDTO.
	 */
	public abstract DataSourceDTO dataFileToDTO(FileSource fileSource);
}
