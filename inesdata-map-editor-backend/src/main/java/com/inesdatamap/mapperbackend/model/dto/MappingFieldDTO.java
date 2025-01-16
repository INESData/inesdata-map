package com.inesdatamap.mapperbackend.model.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a {@link com.inesdatamap.mapperbackend.model.jpa.MappingField}.
 */
@Getter
@Setter
public class MappingFieldDTO extends BaseEntityDTO {

	private LogicalTableDTO logicalTable;

	private LogicalSourceDTO logicalSource;

	private SubjectMapDTO subject;

	private List<PredicateObjectMapDTO> predicates;

	@NotNull
	private Long dataSourceId;

}
