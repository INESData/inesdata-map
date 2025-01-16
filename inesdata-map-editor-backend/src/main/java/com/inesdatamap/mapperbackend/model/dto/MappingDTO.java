package com.inesdatamap.mapperbackend.model.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a {@link com.inesdatamap.mapperbackend.model.jpa.Mapping}.
 */
@Getter
@Setter
public class MappingDTO extends BaseEntityDTO {

	@NotNull
	@Size(min = 1, max = 255)
	private String name;

	@NotNull
	@Size(min = 1, max = 255)
	private String baseUrl;

	@NotEmpty
	private List<MappingFieldDTO> fields;

	@NotNull
	private List<Long> ontologyIds;

	private List<NamespaceDTO> namespaces;

}
