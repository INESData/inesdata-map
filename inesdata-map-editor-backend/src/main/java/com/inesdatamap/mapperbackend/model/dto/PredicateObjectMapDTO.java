package com.inesdatamap.mapperbackend.model.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a {@link com.inesdatamap.mapperbackend.model.jpa.PredicateObjectMap}.
 */
@Getter
@Setter
public class PredicateObjectMapDTO extends BaseEntityDTO {

	@NotNull
	@Size(min = 1, max = 255)
	private String predicate;

	@NotEmpty
	private List<ObjectMapDTO> objectMap;

}
