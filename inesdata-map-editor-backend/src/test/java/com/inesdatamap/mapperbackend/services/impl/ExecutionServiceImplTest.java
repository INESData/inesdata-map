package com.inesdatamap.mapperbackend.services.impl;

import java.net.MalformedURLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.inesdatamap.mapperbackend.model.dto.ExecutionDTO;
import com.inesdatamap.mapperbackend.model.jpa.Execution;
import com.inesdatamap.mapperbackend.model.jpa.Mapping;
import com.inesdatamap.mapperbackend.model.mappers.ExecutionMapper;
import com.inesdatamap.mapperbackend.model.mappers.ExecutionMapperImpl;
import com.inesdatamap.mapperbackend.properties.AppProperties;
import com.inesdatamap.mapperbackend.repositories.jpa.ExecutionRepository;
import com.inesdatamap.mapperbackend.utils.Constants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { ExecutionServiceImpl.class, ExecutionMapperImpl.class,
	AppProperties.class }, initializers = ConfigDataApplicationContextInitializer.class)
class ExecutionServiceImplTest {

	@MockBean
	private ExecutionRepository executionRepository;

	@Autowired
	private ExecutionMapper executionMapper;

	@Autowired
	private ExecutionServiceImpl executionService;

	@Test
	void testSaveExecution() {

		Execution execution = new Execution();
		execution.setExecutionDate(OffsetDateTime.now());

		when(executionRepository.save(any())).thenReturn(execution);

		Execution saved = this.executionService.save(execution);

		assertEquals(execution, saved);
	}

	@Test
	void testListExecutions() {

		Execution execution = new Execution();
		execution.setExecutionDate(OffsetDateTime.now());

		when(executionRepository.findByMappingId(any(), any())).thenReturn(new PageImpl<>(List.of(execution)));

		Page<ExecutionDTO> result = this.executionService.listExecutions(1L, PageRequest.of(0, 10));

		assertNotNull(result.getContent());
		assertEquals(execution.getExecutionDate(), result.getContent().get(0).getExecutionDate());
	}

	@Test
	void testGetFileThrowsException() {
		assertThrows(SecurityException.class, () -> mockFile(null));
	}

	@Test
	void testGetMappingFile() throws MalformedURLException {
		String mappingFileName = Constants.MAPPING_FILE_NAME;
		Resource resource = mockFile(mappingFileName);
		assertEquals(mappingFileName, resource.getFilename());
	}

	@Test
	void testGetLogFile() throws MalformedURLException {
		String mappingFileName = Constants.GRAPH_ENGINE_LOG_FILE_NAME;
		Resource resource = mockFile(mappingFileName);
		assertEquals(mappingFileName, resource.getFilename());
	}

	@Test
	void testGetKnowledgeGraphFile() throws MalformedURLException {
		String mappingFileName = Constants.KG_OUTPUT_FILE_NAME;
		Resource resource = mockFile(mappingFileName);
		assertEquals(mappingFileName, resource.getFilename());
	}

	private Resource mockFile(String mappingFileName) throws MalformedURLException {
		Execution execution = new Execution();
		execution.setExecutionDate(OffsetDateTime.now());

		Mapping mapping = new Mapping();
		mapping.setId(1L);
		execution.setMapping(mapping);

		when(executionRepository.findById(any())).thenReturn(Optional.of(execution));

		Resource resource = this.executionService.getFile(1L, mappingFileName);

		return resource;
	}

}
