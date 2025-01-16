package com.inesdatamap.mapperbackend.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.inesdatamap.mapperbackend.properties.RestApiInfoProperties;

import io.swagger.v3.oas.models.OpenAPI;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * OpenAPI configuration test
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { OpenAPIConfig.class, MappingJackson2HttpMessageConverter.class })
@EnableConfigurationProperties(value = RestApiInfoProperties.class)
@TestPropertySource(properties = { "info.rest-api.name=test-micro", "info.rest-api.description=test-description",
	"info.rest-api.version=test-version" })
class OpenAPIConfigTest {

	@Autowired
	private OpenAPI openAPI;

	@Test
	void getOpenApiInfoTitleTest() {
		assertEquals("test-micro", openAPI.getInfo().getTitle());
	}

	@Test
	void getOpenApiDescriptionTest() {
		assertEquals("test-description", openAPI.getInfo().getDescription());
	}

	@Test
	void getOpenApiInfoVersionTest() {
		assertEquals("test-version", openAPI.getInfo().getVersion());
	}

}
