package com.inesdatamap.mapperbackend.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuration properties for security
 */
@Configuration
@ConfigurationProperties(prefix = "security")
@Getter
@Setter
public class SecurityProperties {

	private CorsProperties[] cors = {};

	/**
	 * Properties for configure CORS
	 */
	@Getter
	@Setter
	public static class CorsProperties {

		private String path;
		private String[] allowedOrigins;
		private String[] allowedMethods;
		private String[] allowedHeaders;
		private String[] exposedHeaders;

	}
}
