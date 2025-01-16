package com.inesdatamap.mapperbackend.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.inesdatamap.mapperbackend.properties.RestApiInfoProperties;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

/**
 * OpenAPI configuration
 */
@Configuration
public class OpenAPIConfig {

	/**
	 * Constructor
	 *
	 * @param converter
	 * 	the Jackson converter
	 */
	public OpenAPIConfig(MappingJackson2HttpMessageConverter converter) {
		List<MediaType> supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
		supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
		converter.setSupportedMediaTypes(supportedMediaTypes);
	}

	/**
	 * Gets the OpenAPI info from properties
	 *
	 * @param restApiInfoProperties
	 * 	the REST API info properties
	 *
	 * @return the OpenAPI info
	 */
	@Bean
	public OpenAPI getOpenApiInfo(RestApiInfoProperties restApiInfoProperties) {
		return new OpenAPI().info(new Info().title(restApiInfoProperties.getName()).version(restApiInfoProperties.getVersion())
			.description(restApiInfoProperties.getDescription())).addServersItem(new Server().url("/"));
	}

}
