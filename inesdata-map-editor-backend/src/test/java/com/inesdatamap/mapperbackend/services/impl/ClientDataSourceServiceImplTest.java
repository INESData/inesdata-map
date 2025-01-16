package com.inesdatamap.mapperbackend.services.impl;

import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.inesdatamap.mapperbackend.model.enums.DataBaseTypeEnum;
import com.inesdatamap.mapperbackend.model.routing.ClientDataSourceRouter;
import com.inesdatamap.mapperbackend.services.ClientDataSourceService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ClientDataSourceServiceImpl.class)
class ClientDataSourceServiceImplTest {

	@Autowired
	private ClientDataSourceService clientDataSourceService;

	@Test
	void testGetTableNames() {

		long dataSourceAId = 1L;
		String urlDatasourceA = "jdbc:h2:mem:testdba";
		String urlDatasourceB = "jdbc:h2:mem:testdbb";
		String username = "";
		String password = "";

		ClientDataSourceRouter router = new ClientDataSourceRouter();

		DataSource dsA = router.getDatasource(dataSourceAId, urlDatasourceA, DataBaseTypeEnum.H2, username, password);
		List<String> dsATables = clientDataSourceService.getTableNames(dataSourceAId, dsA);

		DataSource dsB = router.getDatasource(dataSourceAId, urlDatasourceB, DataBaseTypeEnum.H2, username, password);
		List<String> dsBTables = clientDataSourceService.getTableNames(dataSourceAId, dsB);

		dsATables.forEach(System.out::println);

		Assertions.assertEquals(dsATables.size(), dsBTables.size());
		Assertions.assertFalse(dsATables.isEmpty());

	}

	@Test
	void testGetColumnNames() {

		long dataSourceAId = 1L;
		String urlDatasourceA = "jdbc:h2:mem:testdba";
		String username = "";
		String password = "";
		String tableName = "SESSIONS";

		ClientDataSourceRouter router = new ClientDataSourceRouter();

		DataSource dsA = router.getDatasource(dataSourceAId, urlDatasourceA, DataBaseTypeEnum.H2, username, password);
		List<String> dsAColumns = clientDataSourceService.getColumnNames(dataSourceAId, tableName, dsA);

		dsAColumns.forEach(System.out::println);

		Assertions.assertFalse(dsAColumns.isEmpty());

	}

}
