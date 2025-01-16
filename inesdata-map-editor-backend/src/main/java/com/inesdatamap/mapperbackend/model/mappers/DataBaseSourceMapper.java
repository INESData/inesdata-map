package com.inesdatamap.mapperbackend.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.inesdatamap.mapperbackend.model.dto.DataBaseSourceDTO;
import com.inesdatamap.mapperbackend.model.jpa.DataBaseSource;

/**
 * Mapper interface for converting between DataBaseSource and DataBaseSourceDTO.
 *
 */
@Mapper(componentModel = "spring", uses = BaseEntityMapper.class)
public abstract class DataBaseSourceMapper implements BaseEntityMapper<DataBaseSourceDTO, DataBaseSource> {

	/**
	 * Password encoder used to encrypt passwords before storing them.
	 */
	@Autowired
	protected PasswordEncoder passwordEncoder;

	/**
	 * Converts a DataBaseSourceDTO to a DataBaseSource entity.
	 *
	 * @param dataBaseSourceDTO
	 *            The DataBaseSourceDTO to be converted
	 * @return The DataBaseSource
	 */
	@Override
	@Mapping(target = "password", expression = "java(dataBaseSourceDTO.getPassword()!= null ? passwordEncoder.encode(dataBaseSourceDTO.getPassword()) : null)")
	public abstract DataBaseSource dtoToEntity(DataBaseSourceDTO dataBaseSourceDTO);

	/**
	 * Converts a DataBaseSource entity to a DataBaseSourceDTO.
	 *
	 * @param dataBaseSource
	 *            The DataBaseSource entity to be converted
	 * @return The DataBaseSourceDTO
	 */
	@Mapping(target = "password", ignore = true)
	public abstract DataBaseSourceDTO toDTO(DataBaseSource dataBaseSource);

	/**
	 * Merges the values from a DataBaseSourceDTO into an existing DataBaseSource entity.
	 *
	 * @param dataBaseSourceDTO
	 *            The DataBaseSourceDTO
	 * @param dataBaseSource
	 *            The DataBaseSource entity to be updated
	 */
	@Mapping(target = "password", expression = "java(dataBaseSourceDTO.getPassword()!= null ? passwordEncoder.encode(dataBaseSourceDTO.getPassword()) : dataBaseSource.getPassword())")
	public abstract void merge(DataBaseSourceDTO dataBaseSourceDTO, @MappingTarget DataBaseSource dataBaseSource);

}
