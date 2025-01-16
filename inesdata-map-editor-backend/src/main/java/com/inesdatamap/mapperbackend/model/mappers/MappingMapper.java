package com.inesdatamap.mapperbackend.model.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.inesdatamap.mapperbackend.model.dto.MappingDTO;
import com.inesdatamap.mapperbackend.model.jpa.Mapping;
import com.inesdatamap.mapperbackend.model.jpa.Ontology;

/**
 * Mapper interface for converting between {@link com.inesdatamap.mapperbackend.model.jpa.Mapping} and
 * {@link com.inesdatamap.mapperbackend.model.dto.MappingDTO}.
 */
@Mapper(componentModel = "spring", uses = MappingFieldMapper.class)
public interface MappingMapper extends BaseEntityMapper<MappingDTO, Mapping> {

	@Override
	@org.mapstruct.Mapping(target = "id", ignore = true)
	@org.mapstruct.Mapping(target = "version", ignore = true)
	@org.mapstruct.Mapping(target = "executions", ignore = true)
	Mapping merge(Mapping source, @MappingTarget Mapping target);

	/**
	 * Converts a Mapping entity to its corresponding MappingDTO
	 *
	 * @param entity
	 *            the Mapping entity
	 * @return the MappingDTO
	 */
	@Override
	@org.mapstruct.Mapping(target = "ontologyIds", source = "ontologies", qualifiedByName = "ontologyToOntologyIds")
	MappingDTO entityToDto(Mapping entity);

	/**
	 * Method to extract the IDs from a set of Ontology entities and return them as a list of IDs
	 *
	 * @param ontologies
	 *            s set of Ontology entities
	 * @return a list of IDs of the Ontology entities
	 */
	@Named("ontologyToOntologyIds")
	default List<Long> ontologyToOntologyIds(Set<Ontology> ontologies) {
		List<Long> ontologyIds = new ArrayList<>();
		for (Ontology ontology : ontologies) {
			if (ontology != null && ontology.getId() != null) {
				ontologyIds.add(ontology.getId());
			}
		}
		return ontologyIds;
	}

}
