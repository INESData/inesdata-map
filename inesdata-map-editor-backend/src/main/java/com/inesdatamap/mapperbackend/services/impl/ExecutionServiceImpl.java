package com.inesdatamap.mapperbackend.services.impl;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.inesdatamap.mapperbackend.model.dto.ExecutionDTO;
import com.inesdatamap.mapperbackend.model.jpa.Execution;
import com.inesdatamap.mapperbackend.model.mappers.ExecutionMapper;
import com.inesdatamap.mapperbackend.properties.AppProperties;
import com.inesdatamap.mapperbackend.repositories.jpa.ExecutionRepository;
import com.inesdatamap.mapperbackend.services.ExecutionService;
import com.inesdatamap.mapperbackend.utils.Constants;
import com.inesdatamap.mapperbackend.utils.FileUtils;

/**
 * Execution service implementation
 */
@Service
public class ExecutionServiceImpl implements ExecutionService {

	@Autowired
	private ExecutionRepository repository;

	@Autowired
	private ExecutionMapper executionMapper;

	@Autowired
	private AppProperties appProperties;

	@Override
	public Execution save(Execution execution) {
		return repository.save(execution);
	}

	@Override
	public Page<ExecutionDTO> listExecutions(Long mappingId, Pageable pageable) {

		Page<Execution> executions = repository.findByMappingId(mappingId, pageable);

		return executions.map(executionMapper::entityToDto);
	}

	@Override
	public Resource getFile(Long id, String filename) throws MalformedURLException {

		Execution execution = repository.findById(id).orElseThrow();

		String filePath = FileUtils.getFilePathFromOutputDirectory(appProperties.getDataProcessingPath(), execution.getMapping().getId(),
			execution.getExecutionDate(), "");

		if (!Constants.MAPPING_FILE_NAME.equals(filename) && !Constants.KG_OUTPUT_FILE_NAME.equals(filename)
			&& !Constants.GRAPH_ENGINE_LOG_FILE_NAME.equals(filename)) {
			throw new SecurityException("Invalid filename");
		}

		Path basePath = Paths.get(filePath).normalize();
		String safeFilename = FilenameUtils.normalize(filename);

		if (safeFilename == null) {
			throw new SecurityException("Invalid filename");
		}
		Path safePath = basePath.resolve(safeFilename).normalize();
		if (!safePath.startsWith(basePath)) {
			throw new SecurityException("Invalid filename");
		}

		return new UrlResource(safePath.toUri());

	}

}
