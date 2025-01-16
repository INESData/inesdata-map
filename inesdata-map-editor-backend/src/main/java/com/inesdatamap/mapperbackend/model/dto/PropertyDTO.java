package com.inesdatamap.mapperbackend.model.dto;

import com.inesdatamap.mapperbackend.model.enums.PropertyTypeEnum;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a Property.
 */
@Getter
@Setter
public class PropertyDTO {

	@NotNull
	private PropertyTypeEnum propertyType;

	@NotNull
	private String name;

	@NotNull
	private boolean isAssociated;

}
