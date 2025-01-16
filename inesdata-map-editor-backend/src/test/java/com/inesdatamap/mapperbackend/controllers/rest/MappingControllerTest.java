package com.inesdatamap.mapperbackend.controllers.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import com.inesdatamap.mapperbackend.model.dto.ExecutionDTO;
import com.inesdatamap.mapperbackend.model.dto.MappingDTO;
import com.inesdatamap.mapperbackend.model.dto.SearchMappingDTO;
import com.inesdatamap.mapperbackend.services.ExecutionService;
import com.inesdatamap.mapperbackend.services.MappingService;
import com.inesdatamap.mapperbackend.utils.Constants;

/**
 * Unit tests for the {@link MappingController}
 *
 * @author gmv
 */
@ContextConfiguration(classes = MappingController.class)
@WebMvcTest(MappingController.class)
class MappingControllerTest {

	@Autowired
	private MappingController mappingController;

	@MockBean
	private ExecutionService executionService;

	@MockBean
	private MappingService mappingService;

	@Test
	void testDeleteOntology() {

		// mock
		Long id = 1L;

		// test
		ResponseEntity<Void> result = this.mappingController.deleteMapping(id);

		// Verifies & asserts
		assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

		// Verify that the service method was called once
		Mockito.verify(this.mappingService, Mockito.times(1)).deleteMapping(id);
	}

	@Test
	void testListMappings() {
		int page = 0;
		int size = 10;
		Page<SearchMappingDTO> expectedMappings = new PageImpl<>(List.of(new SearchMappingDTO()));

		when(this.mappingService.listMappings(PageRequest.of(page, size))).thenReturn(expectedMappings);

		ResponseEntity<Page<SearchMappingDTO>> response = this.mappingController.listMappings(page, size);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedMappings, response.getBody());
	}

	@Test
	void testCreateMapping() {
		MappingDTO mappingDTO = new MappingDTO();
		MappingDTO createdMapping = new MappingDTO();

		when(this.mappingService.save(any())).thenReturn(createdMapping);

		ResponseEntity<MappingDTO> response = this.mappingController.create(mappingDTO);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(createdMapping, response.getBody());
	}

	@Test
	void testMaterializeMapping() {
		Long id = 1L;
		List<String> expectedResults = List.of("result1", "result2");

		when(this.mappingService.materialize(id)).thenReturn(expectedResults);

		ResponseEntity<Void> response = this.mappingController.materializeMapping(id);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

	}

	@Test
	void testListExecutions() {

		Long mappingId = 1L;
		int page = 0;
		int size = 10;
		Page<ExecutionDTO> expectedExecutions = new PageImpl<>(List.of(new ExecutionDTO()));

		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Constants.SORT_BY_DATE).descending());

		when(this.executionService.listExecutions(mappingId, pageRequest)).thenReturn(expectedExecutions);

		ResponseEntity<PagedModel<ExecutionDTO>> response = this.mappingController.listExecutions(mappingId, page, size);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		verify(this.executionService).listExecutions(mappingId, pageRequest);
	}

	@Test
	void testGetMapping() {

		Long id = 1L;
		MappingDTO expectedMapping = new MappingDTO();

		when(this.mappingService.getMappingById(id)).thenReturn(expectedMapping);

		ResponseEntity<MappingDTO> response = this.mappingController.getMapping(id);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedMapping, response.getBody());

		verify(this.mappingService).getMappingById(id);
	}

	@Test
	void testUpdateMapping() {

		Long id = 1L;
		MappingDTO mappingDto = new MappingDTO();
		MappingDTO updatedMapping = new MappingDTO();

		when(this.mappingService.updateMapping(id, mappingDto)).thenReturn(updatedMapping);

		ResponseEntity<MappingDTO> response = this.mappingController.updateMapping(id, mappingDto);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(updatedMapping, response.getBody());

		verify(this.mappingService).updateMapping(id, mappingDto);
	}
}
