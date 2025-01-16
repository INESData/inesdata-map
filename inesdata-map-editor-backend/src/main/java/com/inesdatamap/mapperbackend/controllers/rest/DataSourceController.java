package com.inesdatamap.mapperbackend.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.services.DataSourceService;
import com.inesdatamap.mapperbackend.utils.Constants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST Controller for Data Source
 */
@RestController()
@RequestMapping("/data-sources")
@Tag(name = "DataSource", description = "Manage data sources")
@Validated
public class DataSourceController {

	@Autowired
	private DataSourceService dataSourceService;

	/**
	 * Lists all data sources.
	 *
	 * @param page
	 *            page number
	 *
	 * @param size
	 *            page size
	 *
	 *
	 * @return List of all data sources
	 */
	@GetMapping(path = "")
	@Operation(summary = "List all data sources")
	public ResponseEntity<Page<DataSourceDTO>> listDataSources(@RequestParam int page, @RequestParam int size) {
		Page<DataSourceDTO> dataSources = this.dataSourceService
				.listDataSources(PageRequest.of(page, size, Sort.by(Constants.SORT_BY_NAME).ascending()));
		return ResponseEntity.ok(dataSources);
	}

	/**
	 * Deletes data source
	 *
	 * @param id
	 *            data source identifier
	 * @return status code response
	 */
	@DeleteMapping("/{id}")
	@Operation(summary = "Delete data source")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> deleteDataSource(
			@PathVariable(name = "id") @Parameter(name = "id", description = "Data source identifier to delete", required = true) Long id) {
		this.dataSourceService.deleteDataSource(id);
		return ResponseEntity.noContent().build();
	}

}
