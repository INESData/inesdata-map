package com.inesdatamap.mapperbackend.model.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a SearchOntologyDTO.
 */
@Getter
@Setter
public class SearchOntologyDTO {

	private Long id;

	@NotNull
	@Size(min = 1, max = 255)
	private String name;

	@Size(max = 255)
	private String title;

	@PastOrPresent
	private LocalDateTime uploadDate;

	@NotNull
	@Size(min = 1, max = 255)
	private String url;

	@NotNull
	@Size(min = 1, max = 255)
	private String versionName;

}
