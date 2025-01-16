package com.inesdatamap.mapperbackend.controllers.rest;

import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inesdatamap.mapperbackend.services.ExecutionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST Controller for Execution
 */
@RestController
@RequestMapping("/executions")
@Tag(name = "Execution", description = "Manage executions")
public class ExecutionController {

	private final ExecutionService executionService;

	/**
	 * Constructor
	 *
	 * @param executionService
	 * 	the execution service
	 */
	public ExecutionController(ExecutionService executionService) {
		this.executionService = executionService;
	}

	/**
	 * Download the specified file from the given execution
	 *
	 * @param id
	 * 	the execution id
	 * @param filename
	 * 	the file name
	 *
	 * @return the file
	 *
	 * @throws MalformedURLException
	 * 	if the URL is malformed
	 */
	@GetMapping("/{id}/files")
	@Operation(summary = "Download the specified file from the given execution")
	public ResponseEntity<Resource> downloadFile(@PathVariable Long id, @RequestParam String filename) throws MalformedURLException {

		Resource file = executionService.getFile(id, filename);

		if (!file.exists() || !file.isReadable()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(
			file);
	}

}
