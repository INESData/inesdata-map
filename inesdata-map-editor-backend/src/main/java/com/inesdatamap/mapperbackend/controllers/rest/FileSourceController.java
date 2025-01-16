package com.inesdatamap.mapperbackend.controllers.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.model.dto.FileSourceDTO;
import com.inesdatamap.mapperbackend.services.FileSourceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST Controller for FileSource
 */
@RestController()
@RequestMapping("/file-sources")
@Tag(name = "FileSource", description = "Manage file sources")
@Validated
public class FileSourceController {

	private final FileSourceService fileSourceService;

	/**
	 * Constructor
	 *
	 * @param fileSourceService
	 *            the file source service
	 */
	public FileSourceController(FileSourceService fileSourceService) {
		this.fileSourceService = fileSourceService;
	}

	/**
	 * Saves the given file source
	 *
	 * @param fileSourceDTO
	 *            to save
	 * @param file
	 *            file content to save
	 * @return saved file source
	 */
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Create given file source")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<DataSourceDTO> createFileSource(
			@RequestPart("body") @Parameter(name = "fileSource", description = "The file source to save", required = true) FileSourceDTO fileSourceDTO,
			@RequestPart(value = "file", required = false) MultipartFile file) {
		DataSourceDTO createdDataSource = this.fileSourceService.createFileSource(fileSourceDTO, file);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdDataSource);
	}

	/**
	 * Updates the given file source
	 *
	 * @param id
	 *            file source identifier
	 *
	 * @param fileSourceDTO
	 *            to save
	 * @return saved file source
	 */
	@PutMapping(value = "/{id}")
	@Operation(summary = "Updates given file source")
	public ResponseEntity<DataSourceDTO> updateFileSource(
			@PathVariable(name = "id") @Parameter(name = "id", description = "File source identifier to update", required = true) Long id,
			@RequestBody @Parameter(name = "fileSource", description = "The file source to update", required = true) FileSourceDTO fileSourceDTO) {
		return ResponseEntity.ok(this.fileSourceService.updateFileSource(id, fileSourceDTO));
	}

	/**
	 * Get the given data file source.
	 *
	 * @param id
	 *            identifier
	 *
	 * @return The data file source
	 */
	@GetMapping("/{id}")
	@Operation(summary = "Gets given data file source")
	public ResponseEntity<FileSourceDTO> getFileSource(
			@PathVariable(name = "id") @Parameter(name = "id", description = "File source identifier", required = true) Long id) {
		return ResponseEntity.ok(this.fileSourceService.getFileSourceById(id));
	}

	/**
	 * Get all file sources filtered by type
	 *
	 * @param type
	 *            type
	 *
	 * @return List of file sources
	 */
	@GetMapping("")
	@Operation(summary = "Gets all file sources filtered by type")
	public ResponseEntity<List<FileSourceDTO>> getFileSourceByType(
			@RequestParam(name = "type") @Parameter(name = "type", description = "File source type", required = true) String type) {
		return ResponseEntity.ok(this.fileSourceService.getFileSourceByType(type));
	}

	/**
	 * Get all CSV file fields
	 *
	 * @param id
	 *            identifier
	 *
	 * @return List of file fields
	 */
	@GetMapping("/{id}/fields")
	@Operation(summary = "Gets all CSV file fields")
	public ResponseEntity<List<String>> getFileFields(
			@PathVariable(name = "id") @Parameter(name = "id", description = "File source identifier", required = true) Long id) {
		return ResponseEntity.ok(this.fileSourceService.getFileFields(id));
	}

	/**
	 * Get all XML file attributes
	 *
	 * @param id
	 *            identifier
	 *
	 * @return List of attributes
	 */
	@GetMapping("/{id}/attributes")
	@Operation(summary = "Gets all XML file attributes")
	public ResponseEntity<List<String>> getFileAttributes(
			@PathVariable(name = "id") @Parameter(name = "id", description = "File source identifier", required = true) Long id) {
		return ResponseEntity.ok(this.fileSourceService.getFileAttributes(id));
	}

}
