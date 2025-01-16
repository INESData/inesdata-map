package com.inesdatamap.mapperbackend.model.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing an Ontology.
 */
@Getter
@Setter
public class OntologyDTO extends BaseEntityDTO {

	@NotNull
	@Size(min = 1, max = 255)
	private String name;

	private byte[] content;

	@Size(max = 255)
	private String title;

	@PastOrPresent
	private LocalDateTime uploadDate;

	@Size(max = 255)
	private String url;

	@NotNull
	@Size(min = 1, max = 255)
	private String versionName;

}
