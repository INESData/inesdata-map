package com.inesdatamap.mapperbackend.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a {@link com.inesdatamap.mapperbackend.model.jpa.LogicalTable}.
 */
@Getter
@Setter
public class LogicalTableDTO extends BaseEntityDTO {

	@NotNull
	@Size(min = 1, max = 255)
	private String tableName;

	private String query;

}
