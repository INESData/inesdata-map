package com.inesdatamap.mapperbackend.controllers.rest;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.inesdatamap.mapperbackend.model.dto.DataBaseSourceDTO;
import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.services.ClientDataSourceService;
import com.inesdatamap.mapperbackend.services.DataBaseSourceService;
import com.inesdatamap.mapperbackend.services.DataSourceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Database source controller
 */
@RestController()
@RequestMapping("/database-sources")
@Tag(name = "DataBaseSource", description = "Manage database sources")
public class DataBaseSourceController {

	private final ClientDataSourceService clientDataSourceService;
	private final DataSourceService dataSourceService;
	private final DataBaseSourceService dataBaseSourceService;

	/**
	 * Constructor
	 *
	 * @param clientDataSourceService
	 *            the client data source service
	 * @param dataSourceService
	 *            the data source service
	 * @param dataBaseSourceService
	 *            the data base source service
	 */
	public DataBaseSourceController(ClientDataSourceService clientDataSourceService, DataSourceService dataSourceService,
			DataBaseSourceService dataBaseSourceService) {
		this.clientDataSourceService = clientDataSourceService;
		this.dataSourceService = dataSourceService;
		this.dataBaseSourceService = dataBaseSourceService;
	}

	/**
	 * Get table names for a database source
	 *
	 * @param id
	 *            the data source ID
	 *
	 * @return the list of table names
	 */
	@GetMapping("/{id}/table-names")
	@Operation(summary = "Get table names for a database source", description = "Retrieves a list of table names for the specified data source ID.")
	public ResponseEntity<List<String>> getTableNames(@PathVariable Long id) {

		DataSource dbDataSource = this.dataSourceService.getClientDataSource(id);

		return ResponseEntity.ok(this.clientDataSourceService.getTableNames(id, dbDataSource));
	}

	/**
	 * Get column names for a database table
	 *
	 * @param id
	 *            the data source ID
	 * @param table
	 *            the table name
	 *
	 * @return the list of table names
	 */
	@GetMapping("/{id}/table-column-names")
	@Operation(summary = "Get column names for a database table", description = "Retrieves a list of column names for the specified table name and a data source ID.")
	public ResponseEntity<List<String>> getColumnNames(@PathVariable Long id, @RequestParam(name = "table") String table) {

		if (StringUtils.isEmpty(table)) {
			throw new IllegalArgumentException("Table name is required");
		}

		DataSource dbDataSource = this.dataSourceService.getClientDataSource(id);

		return ResponseEntity.ok(this.clientDataSourceService.getColumnNames(id, table, dbDataSource));
	}

	/**
	 * Saves the given data base source
	 *
	 * @param dataBaseSourceDTO
	 *            to save
	 * @return saved data base source
	 */
	@PostMapping
	@Operation(summary = "Create given data base source")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<DataSourceDTO> createDataBaseSource(
			@RequestBody @Parameter(name = "dataBaseSource", description = "The data base source to save", required = true) DataBaseSourceDTO dataBaseSourceDTO) {
		DataSourceDTO createdDataBaseSource = this.dataBaseSourceService.createDataBaseSource(dataBaseSourceDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdDataBaseSource);
	}

	/**
	 * Updates the given data source
	 *
	 * @param id
	 *            data base source identifier
	 * @param dataBaseSourceDTO
	 *            to update
	 * @return updated data base source
	 */
	@PutMapping(value = "/{id}")
	@Operation(summary = "Update given data base source")
	public ResponseEntity<DataSourceDTO> updateDataBaseSource(
			@PathVariable(name = "id") @Parameter(name = "id", description = "Data base source identifier to update", required = true) Long id,
			@RequestBody @Parameter(name = "dataBaseSource", description = "The data base source to update", required = true) DataBaseSourceDTO dataBaseSourceDTO) {
		return ResponseEntity.ok(this.dataBaseSourceService.updateDataBaseSource(id, dataBaseSourceDTO));
	}

	/**
	 * Get the given data base source.
	 *
	 * @param id
	 *            identifier
	 *
	 * @return The data base source
	 */
	@GetMapping("/{id}")
	@Operation(summary = "Gets given data base source")
	public ResponseEntity<DataBaseSourceDTO> getDataBaseSource(
			@PathVariable(name = "id") @Parameter(name = "id", description = "Data source identifier", required = true) Long id) {
		return ResponseEntity.ok(this.dataBaseSourceService.getDataBaseSourceById(id));
	}

}
