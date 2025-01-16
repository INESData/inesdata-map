package com.inesdatamap.mapperbackend.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.model.enums.DataBaseTypeEnum;
import com.inesdatamap.mapperbackend.model.jpa.DataBaseSource;
import com.inesdatamap.mapperbackend.model.jpa.DataSource;
import com.inesdatamap.mapperbackend.model.mappers.DataSourceMapper;
import com.inesdatamap.mapperbackend.properties.AppProperties;
import com.inesdatamap.mapperbackend.repositories.jpa.DataSourceRepository;
import com.inesdatamap.mapperbackend.repositories.jpa.MappingFieldRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class DataSourceServiceImplTest {

	@Mock
	private DataSourceRepository<DataBaseSource> dataBaseSourceRepository;

	@Mock
	private DataSourceRepository<DataSource> dataSourceRepository;

	@Mock
	private MappingFieldRepository mappingFieldRepo;

	@Mock
	private DataSourceMapper dataSourceMapper;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private AppProperties appProperties;

	@InjectMocks
	private DataSourceServiceImpl dataSourceService;

	@Test
	void testFindById() {
		// mock
		DataBaseSource dbs = new DataBaseSource();
		dbs.setId(1L);

		Mockito.when(this.dataBaseSourceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(dbs));

		// test
		DataBaseSource result = this.dataSourceService.findById(1L);

		// asserts
		Assertions.assertEquals(dbs.getId(), result.getId());
	}

	@Test
	void testFindByIdThrowsNotEntityFoundException() {

		// mock
		DataBaseSource dbs = new DataBaseSource();
		dbs.setId(1L);

		Mockito.when(this.dataBaseSourceRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		// asserts
		Assertions.assertThrows(EntityNotFoundException.class, () -> this.dataSourceService.findById(1L));
	}

	@Test
	void testGetClientDataSource() {

		// mock
		DataBaseSource dbs = new DataBaseSource();
		dbs.setId(1L);
		dbs.setConnectionString("jdbc:mysql://localhost:3306/test");
		dbs.setDatabaseType(DataBaseTypeEnum.MYSQL);

		Mockito.when(this.dataBaseSourceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(dbs));

		// test
		javax.sql.DataSource ds = this.dataSourceService.getClientDataSource(1L);

		// asserts
		Assertions.assertNotNull(ds);

	}

	@Test
	void testListDataSources() {

		// Mock data
		DataSource dataSource = new DataSource();
		DataSourceDTO dataSourceDTO = new DataSourceDTO();
		Page<DataSource> page = new PageImpl<>(List.of(dataSource));
		when(this.dataSourceRepository.findAll(any(Pageable.class))).thenReturn(page);
		when(this.dataSourceMapper.dataSourceToDTO(any(DataSource.class))).thenReturn(dataSourceDTO);

		// Execute the method
		Page<DataSourceDTO> result = this.dataSourceService.listDataSources(Pageable.unpaged());

		// Verify
		assertThat(result).isNotNull();
		assertThat(result.getContent()).containsExactly(dataSourceDTO);
	}

	@Test
	void testDeleteDataSource() {

		// Mock data
		Long id = 1L;
		DataSource dataSource = new DataSource();
		when(this.dataSourceRepository.findById(id)).thenReturn(Optional.of(dataSource));

		// Data source is not in use
		when(this.mappingFieldRepo.existsBySourceId(id)).thenReturn(false);
		this.dataSourceService.deleteDataSource(id);
		verify(this.dataSourceRepository, times(1)).deleteById(id);

		// Data source is in use
		when(this.mappingFieldRepo.existsBySourceId(id)).thenReturn(true);
		assertThrows(IllegalArgumentException.class, () -> this.dataSourceService.deleteDataSource(id));
	}

	@Test
	void testGetEntity() {
		// Prepare mock data
		Long id = 1L;
		DataSource dataSource = new DataSource();
		when(this.dataSourceRepository.findById(id)).thenReturn(Optional.of(dataSource));

		// Execute the method
		DataSource result = this.dataSourceService.getEntity(id);

		// Verify results
		assertThat(result).isEqualTo(dataSource);
	}
}
