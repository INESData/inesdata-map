package com.inesdatamap.mapperbackend.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.inesdatamap.mapperbackend.model.dto.DataBaseSourceDTO;
import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.model.jpa.DataBaseSource;
import com.inesdatamap.mapperbackend.model.jpa.DataSource;
import com.inesdatamap.mapperbackend.model.mappers.DataBaseSourceMapper;
import com.inesdatamap.mapperbackend.model.mappers.DataSourceMapper;
import com.inesdatamap.mapperbackend.repositories.jpa.DataBaseSourceRepository;
import com.inesdatamap.mapperbackend.repositories.jpa.DataSourceRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * Unit tests for the {@link DataBaseSourceServiceImpl}
 *
 * @author gmv
 */
@ExtendWith(MockitoExtension.class)
class DataBaseSourceServiceImplTest {

	@Mock
	private DataBaseSourceMapper dataBaseSourceMapper;

	@Mock
	private DataSourceMapper dataSourceMapper;

	@Mock
	private DataBaseSourceRepository dataBaseSourceRepository;

	@Mock
	private DataSourceRepository<DataSource> dataSourceRepository;

	@InjectMocks
	private DataBaseSourceServiceImpl dataBaseSourceService;

	@Test
	void createDataBaseSource() {
		// Arrange
		DataBaseSourceDTO inputDto = new DataBaseSourceDTO();
		DataBaseSource entity = new DataBaseSource();
		DataBaseSource savedEntity = new DataBaseSource();
		DataBaseSourceDTO outputDto = new DataBaseSourceDTO();

		when(this.dataBaseSourceMapper.dtoToEntity(inputDto)).thenReturn(entity);
		when(this.dataSourceRepository.save(entity)).thenReturn(savedEntity);
		when(this.dataBaseSourceMapper.toDTO(savedEntity)).thenReturn(outputDto);

		// Act
		DataSourceDTO result = this.dataBaseSourceService.createDataBaseSource(inputDto);

		// Assert
		assertNotNull(result);
		assertEquals(outputDto, result);
		verify(this.dataBaseSourceMapper).dtoToEntity(inputDto);
		verify(this.dataSourceRepository).save(entity);
		verify(this.dataBaseSourceMapper).toDTO(savedEntity);
	}

	@Test
	void updateDataBaseSource() {
		// Inicializar los mocks
		MockitoAnnotations.openMocks(this);

		// Arrange
		Long id = 1L;
		DataBaseSourceDTO inputDto = new DataBaseSourceDTO();
		DataBaseSource existingEntity = new DataBaseSource();
		DataBaseSource updatedEntity = new DataBaseSource();
		DataBaseSourceDTO outputDto = new DataBaseSourceDTO();

		// Config mocks
		when(this.dataBaseSourceRepository.findById(id)).thenReturn(Optional.of(existingEntity));
		doNothing().when(this.dataBaseSourceMapper).merge(inputDto, existingEntity);
		when(this.dataBaseSourceRepository.save(existingEntity)).thenReturn(updatedEntity);
		when(this.dataSourceMapper.entityToDto(updatedEntity)).thenReturn(outputDto);

		// Act
		DataSourceDTO result = this.dataBaseSourceService.updateDataBaseSource(id, inputDto);

		// Assert
		assertNotNull(result, "The result should not be null");
		assertEquals(outputDto, result, "The result should match the expected output DTO");

		// Verify
		verify(this.dataBaseSourceRepository).findById(id);
		verify(this.dataBaseSourceMapper).merge(inputDto, existingEntity);
		verify(this.dataBaseSourceRepository).save(existingEntity);
		verify(this.dataSourceMapper).entityToDto(updatedEntity);
	}

	@Test
	void testGetEntity() {
		Long id = 1L;

		// Mocking
		when(this.dataBaseSourceRepository.findById(id)).thenReturn(Optional.empty());

		// Test & Verify
		assertThrows(EntityNotFoundException.class, () -> this.dataBaseSourceService.getEntity(id));
	}

}
