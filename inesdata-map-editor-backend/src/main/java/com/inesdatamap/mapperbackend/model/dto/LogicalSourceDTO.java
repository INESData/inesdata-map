package com.inesdatamap.mapperbackend.model.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a {@link com.inesdatamap.mapperbackend.model.jpa.LogicalSource}.
 */
@Getter
@Setter
public class LogicalSourceDTO extends BaseEntityDTO {

	@Size(min = 1, max = 255)
	private String source;

	private String referenceFormulation;

	private String iterator;

	@Size(min = 1, max = 255)
	private String query;

}
