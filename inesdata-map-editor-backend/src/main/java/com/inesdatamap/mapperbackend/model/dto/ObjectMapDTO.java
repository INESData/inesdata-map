package com.inesdatamap.mapperbackend.model.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a {@link com.inesdatamap.mapperbackend.model.jpa.ObjectMap}.
 */
@Getter
@Setter
public class ObjectMapDTO extends BaseEntityDTO {

	@NotNull
	@Size(min = 1, max = 255)
	private String key;

	private String literalValue;

	private List<ObjectMapDTO> objectValue;

}
