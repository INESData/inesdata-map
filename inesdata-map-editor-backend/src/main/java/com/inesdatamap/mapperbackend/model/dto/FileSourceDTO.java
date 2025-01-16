package com.inesdatamap.mapperbackend.model.dto;

import com.inesdatamap.mapperbackend.model.enums.DataFileTypeEnum;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a File Source.
 */
@Getter
@Setter
public class FileSourceDTO extends DataSourceDTO {

	@NotNull
	private DataFileTypeEnum fileType;

}
