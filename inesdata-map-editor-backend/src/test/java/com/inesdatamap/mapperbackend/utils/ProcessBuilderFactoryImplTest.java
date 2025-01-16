package com.inesdatamap.mapperbackend.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ProcessBuilderFactoryImpl.class)
class ProcessBuilderFactoryImplTest {

	@Autowired
	private ProcessBuilderFactory processBuilderFactory;

	@Test
	void testCreateProcessBuilder() {

		// Arrange
		String command = "java";
		String arg1 = "--version";

		// Act
		ProcessBuilder pb = processBuilderFactory.createProcessBuilder(command, arg1);

		// Assert
		assertNotNull(pb);
	}

}
