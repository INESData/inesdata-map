package com.inesdatamap.mapperbackend.model.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.inesdatamap.mapperbackend.model.dto.MappingFieldDTO;
import com.inesdatamap.mapperbackend.model.jpa.MappingField;

/**
 * Mapper interface for converting between {@link MappingField} and {@link MappingFieldDTO}.
 */
@Mapper(componentModel = "spring", uses = BaseEntityMapper.class)
public interface MappingFieldMapper extends BaseEntityMapper<MappingFieldDTO, MappingField> {

	/**
	 * Converts a {@link MappingFieldDTO} entity to a {@link MappingField}.
	 *
	 * @param dto
	 * 	the DTO
	 *
	 * @return the entity
	 */
	@Override
	@Mapping(target = "source.id", source = "dataSourceId")
	MappingField dtoToEntity(MappingFieldDTO dto);

	/**
	 * Converts a {@link MappingField} entity to a {@link MappingFieldDTO}.
	 *
	 * @param entity
	 * 	the entity
	 *
	 * @return the DTO
	 */
	@Override
	@InheritInverseConfiguration(name = "dtoToEntity")
	MappingFieldDTO entityToDto(MappingField entity);

}
