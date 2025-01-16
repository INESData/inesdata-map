package com.inesdatamap.mapperbackend.properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for the {@link AppProperties} class
 */
@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = AppProperties.class)
@TestPropertySource(properties = { "spring.config.location=classpath:application.yaml" })
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
class AppPropertiesTest {

	@Autowired
	private AppProperties properties;

	@Test
	void getDataProcessingPathTest() {
		assertEquals("/test/input", properties.getDataProcessingPath());
	}

}
