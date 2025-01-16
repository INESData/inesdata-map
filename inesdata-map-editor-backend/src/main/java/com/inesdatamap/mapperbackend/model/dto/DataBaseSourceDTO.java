package com.inesdatamap.mapperbackend.model.dto;

import com.inesdatamap.mapperbackend.model.enums.DataBaseTypeEnum;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a Data Base Source.
 */
@Getter
@Setter
public class DataBaseSourceDTO extends DataSourceDTO {

	@NotNull
	@Size(min = 1, max = 255)
	private String connectionString;

	@NotNull
	@Size(min = 1, max = 255)
	private String userName;

	@NotNull
	@Size(min = 1, max = 255)
	private String password;

	@NotNull
	private DataBaseTypeEnum databaseType;

}
