package com.inesdatamap.mapperbackend.exceptions;

import java.io.Serial;

import com.inesdatamap.mapperbackend.controllers.errors.ErrorCode;
import com.inesdatamap.mapperbackend.controllers.errors.ErrorResponse;

/**
 * Base abstract class for gauss exceptions
 *
 * @author gmv
 */
public abstract class BaseException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 5279597889255882607L;

	/**
	 * Constructor
	 *
	 * @param message
	 * 		the detail message
	 */
	protected BaseException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 *
	 * @param message
	 * 		the detail message
	 * @param cause
	 * 		the cause
	 */
	protected BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Gets error code
	 *
	 * @return {@link ErrorCode} the code of error
	 */
	public abstract ErrorCode getErrorCode();

	/**
	 * Gets error message
	 *
	 * @return {@link String} the message of error
	 */
	public abstract String getErrorMessage();

	/**
	 * Gets error details
	 *
	 * @return {@link String} the details of error
	 */
	public abstract String getErrorDetails();

	/**
	 * Gets error response
	 *
	 * @return {@link ErrorResponse} the response of error
	 */
	public ErrorResponse getErrorResponse() {
		return new ErrorResponse(getErrorCode(), getErrorMessage(), getErrorDetails());
	}

}

