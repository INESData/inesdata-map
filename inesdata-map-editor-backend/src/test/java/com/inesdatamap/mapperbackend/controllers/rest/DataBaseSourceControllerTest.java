package com.inesdatamap.mapperbackend.controllers.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.inesdatamap.mapperbackend.model.dto.DataBaseSourceDTO;
import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.services.ClientDataSourceService;
import com.inesdatamap.mapperbackend.services.DataBaseSourceService;
import com.inesdatamap.mapperbackend.services.DataSourceService;

/**
 * Unit tests for the {@link DataBaseSourceController}
 *
 * @author gmv
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DataBaseSourceController.class)
class DataBaseSourceControllerTest {

	@MockBean
	private ClientDataSourceService clientDataSourceService;

	@MockBean
	private DataSourceService dataSourceService;

	@MockBean
	private DataBaseSourceService dataBaseSourceService;

	@Autowired
	private DataBaseSourceController controller;

	@Test
	void testGetTable() {

		// mock
		List<String> tables = Arrays.asList("table1", "table2");
		Mockito.when(this.clientDataSourceService.getTableNames(Mockito.anyLong(), Mockito.any())).thenReturn(tables);

		// test
		ResponseEntity<List<String>> result = this.controller.getTableNames(1L);

		// verifies & asserts
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(tables, result.getBody());
	}

	@Test
	void testGetTableColumns() {

		// mock
		List<String> columns = Arrays.asList("column1", "column2");
		Mockito.when(this.clientDataSourceService.getColumnNames(Mockito.anyLong(), Mockito.anyString(), Mockito.any()))
				.thenReturn(columns);

		// test
		ResponseEntity<List<String>> result = this.controller.getColumnNames(1L, "test");

		// verifies & asserts
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(columns, result.getBody());

	}

	@Test
	void testGetTableColumnsThrowsException() {

		// mock
		List<String> columns = Arrays.asList("column1", "column2");
		Mockito.when(this.clientDataSourceService.getColumnNames(Mockito.anyLong(), Mockito.anyString(), Mockito.any()))
				.thenReturn(columns);

		// test and assert
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.controller.getColumnNames(1L, "");
		});

	}

	@Test
	void testCreateDataBaseSource() {

		DataSourceDTO dataSourceDTO = new DataSourceDTO();
		DataBaseSourceDTO dataBaseSourceDTO = new DataBaseSourceDTO();

		// Mock the service call
		when(this.dataBaseSourceService.createDataBaseSource(Mockito.any(DataBaseSourceDTO.class))).thenReturn(dataSourceDTO);

		// Test the controller method
		ResponseEntity<DataSourceDTO> result = this.controller.createDataBaseSource(dataBaseSourceDTO);

		// Verify and assert
		assertEquals(HttpStatus.CREATED, result.getStatusCode());
		assertEquals(dataSourceDTO, result.getBody());
	}

	@Test
	void testUpdateDataBaseSource() {

		DataSourceDTO dataSourceDTO = new DataSourceDTO();
		DataBaseSourceDTO dataBaseSourceDTO = new DataBaseSourceDTO();

		Long id = 1L;
		// Mock the service call
		when(this.dataBaseSourceService.updateDataBaseSource(Mockito.eq(id), Mockito.any(DataBaseSourceDTO.class)))
				.thenReturn(dataSourceDTO);

		// Test the controller method
		ResponseEntity<DataSourceDTO> result = this.controller.updateDataBaseSource(id, dataBaseSourceDTO);

		// Verify and assert
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(dataSourceDTO, result.getBody());
	}
}
