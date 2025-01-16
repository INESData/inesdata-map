package com.inesdatamap.mapperbackend.controllers.rest;

import java.net.MalformedURLException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import com.inesdatamap.mapperbackend.services.ExecutionService;
import com.inesdatamap.mapperbackend.utils.Constants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link ExecutionController}
 *
 * @author gmv
 */
@ContextConfiguration(classes = ExecutionController.class)
@WebMvcTest(ExecutionController.class)
class ExecutionControllerTest {

	@Autowired
	private ExecutionController executionController;

	@MockBean
	private ExecutionService executionService;

	@Test
	void getFile() throws MalformedURLException {

		Long id = 1L;
		String filename = Constants.MAPPING_FILE_NAME;
		Resource resource = mock(Resource.class);

		when(this.executionService.getFile(any(), any())).thenReturn(resource);
		when(resource.isReadable()).thenReturn(true);
		when(resource.exists()).thenReturn(true);

		ResponseEntity<Resource> result = this.executionController.downloadFile(id, filename);

		assertEquals(HttpStatus.OK, result.getStatusCode());

	}

	@Test
	void testGetFileNotExists() throws MalformedURLException {

		Long id = 1L;
		String filename = Constants.MAPPING_FILE_NAME;
		Resource resource = mock(Resource.class);

		when(this.executionService.getFile(any(), any())).thenReturn(resource);
		when(resource.exists()).thenReturn(false);

		ResponseEntity<Resource> result = this.executionController.downloadFile(id, filename);

		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
	}

	@Test
	void testGetFileNotReadable() throws MalformedURLException {

		Long id = 1L;
		String filename = Constants.MAPPING_FILE_NAME;
		Resource resource = mock(Resource.class);

		when(this.executionService.getFile(any(), any())).thenReturn(resource);
		when(resource.isReadable()).thenReturn(false);

		ResponseEntity<Resource> result = this.executionController.downloadFile(id, filename);

		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
	}

}
