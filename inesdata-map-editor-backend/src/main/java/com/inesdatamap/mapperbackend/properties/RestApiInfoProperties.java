package com.inesdatamap.mapperbackend.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Properties for REST API information: name, description, version and artifact
 * <p>
 * Used in:
 * <p>
 * - OpenApi info
 * <p>
 * - Actuator info (Actuator info.env.enabled: exposes any property from the environment whose name starts with 'info.')
 *
 * @author gmv
 */
@Configuration
@ConfigurationProperties("info.rest-api")
public class RestApiInfoProperties {

	private String name;
	private String description;
	private String version;

	/**
	 * Default constructor
	 */
	public RestApiInfoProperties() {
		super();
	}

	/**
	 * Gets the name of the REST API
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name of the REST API
	 *
	 * @param name
	 * 		the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the description of REST API
	 *
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets the description of REST API
	 *
	 * @param description
	 * 		the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the version of REST API
	 *
	 * @return the version
	 */
	public String getVersion() {
		return this.version;
	}

	/**
	 * Sets the version of REST API
	 *
	 * @param version
	 * 		the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

}
