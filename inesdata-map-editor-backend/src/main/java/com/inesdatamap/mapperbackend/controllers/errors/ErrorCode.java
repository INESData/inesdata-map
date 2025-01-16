package com.inesdatamap.mapperbackend.controllers.errors;

import java.io.Serializable;

/**
 * Interface for gauss error codes
 *
 * @author gmv
 */
public interface ErrorCode extends Serializable {

	/**
	 * Gets the error code
	 *
	 * @return {@link String} code of error
	 */
	String getCode();

	/**
	 * Gets the error status
	 *
	 * @return {@link Integer} status of error
	 */
	Integer getStatus();

}
