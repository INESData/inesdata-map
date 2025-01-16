package com.inesdatamap.mapperbackend.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a SearchMapping.
 */
@Getter
@Setter
public class SearchMappingDTO extends BaseEntityDTO {

	private String name;

	private List<String> ontologies;

	private List<String> dataSources;

}
