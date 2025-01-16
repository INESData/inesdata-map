package com.inesdatamap.mapperbackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.inesdatamap.mapperbackend.properties.SecurityProperties;

/**
 * CORS configuration
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Autowired
	private SecurityProperties properties;

	/**
	 * Adds CORS mappings
	 *
	 * @param registry
	 * 	the CORS registry
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {

		for (SecurityProperties.CorsProperties cors : properties.getCors()) {
			registry.addMapping(cors.getPath()).allowedOrigins(cors.getAllowedOrigins()).allowedMethods(cors.getAllowedMethods())
				.allowedHeaders(cors.getAllowedHeaders()).exposedHeaders(cors.getExposedHeaders());
		}

	}

}
