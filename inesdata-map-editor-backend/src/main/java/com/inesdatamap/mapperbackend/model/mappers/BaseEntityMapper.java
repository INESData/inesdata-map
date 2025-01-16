package com.inesdatamap.mapperbackend.model.mappers;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.inesdatamap.mapperbackend.model.dto.BaseEntityDTO;
import com.inesdatamap.mapperbackend.model.jpa.BaseEntity;

/**
 * Mapper interface to define the mapper between the entity and its DTO
 *
 * @param <D>
 *            DTO class
 * @param <E>
 *            Entity class
 *
 */
public interface BaseEntityMapper<D extends BaseEntityDTO, E extends BaseEntity> {

	/**
	 * Maps an BaseEntity to an BaseEntityDTO.
	 *
	 * @param entity
	 *            the entity
	 * @return the corresponding BaseEntityDTO
	 */
	D entityToDto(E entity);

	/**
	 * Maps a BaseEntityDTO to a BaseEntity.
	 *
	 * @param entityDTO
	 *            the entityDTO
	 * @return the corresponding BaseEntity
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "version", ignore = true)
	E dtoToEntity(D entityDTO);

	/**
	 * Merges two instances of entities
	 *
	 * @param source
	 *            {@link E} instance of entity source
	 * @param target
	 *            {@link E} instance of entity target
	 *
	 * @return {@link E} instance of entity target, merged with source
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "version", ignore = true)
	E merge(E source, @MappingTarget E target);
}
