package com.inesdatamap.mapperbackend.properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for the {@link RestApiInfoProperties} class
 */
@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = RestApiInfoProperties.class)
@TestPropertySource(properties = { "info.rest-api.name=test-name", "info.rest-api.description=test-description",
	"info.rest-api.version=test-version" })
class RestApiInfoPropertiesTest {

	@Autowired
	private RestApiInfoProperties properties;

	@Test
	void getNameTest() {
		assertEquals("test-name", properties.getName());
	}

	@Test
	void getDescriptionTest() {
		assertEquals("test-description", properties.getDescription());
	}

	@Test
	void getVersionTest() {
		assertEquals("test-version", properties.getVersion());
	}

}
