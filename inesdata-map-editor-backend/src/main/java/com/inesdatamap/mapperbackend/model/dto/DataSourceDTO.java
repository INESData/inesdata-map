package com.inesdatamap.mapperbackend.model.dto;

import com.inesdatamap.mapperbackend.model.enums.DataSourceTypeEnum;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a Data Source.
 */
@Getter
@Setter
public class DataSourceDTO extends BaseEntityDTO {

	@NotNull
	@Size(min = 1, max = 255)
	private String name;

	private DataSourceTypeEnum type;
}
