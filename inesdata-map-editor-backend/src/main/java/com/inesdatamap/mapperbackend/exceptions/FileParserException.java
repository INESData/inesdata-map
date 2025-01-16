package com.inesdatamap.mapperbackend.exceptions;

import java.io.Serial;

import com.inesdatamap.mapperbackend.controllers.errors.BaseErrorCode;
import com.inesdatamap.mapperbackend.controllers.errors.ErrorCode;

/**
 * Exception for file parsing errors
 */
public class FileParserException extends BaseException {

	@Serial
	private static final long serialVersionUID = -18000228315820106L;
	private static final ErrorCode CODE_ERROR = BaseErrorCode.UNEXPECTED_ERROR;

	/**
	 * Constructor
	 *
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public FileParserException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor
	 *
	 * @param message
	 *            the message
	 */
	public FileParserException(String message) {
		super(message);
	}

	/**
	 * (non-javadoc)
	 *
	 * @see BaseException#getErrorCode()
	 */
	@Override
	public ErrorCode getErrorCode() {
		return CODE_ERROR;
	}

	/**
	 * (non-javadoc)
	 *
	 * @see BaseException#getErrorMessage()
	 */
	@Override
	public String getErrorMessage() {
		return "Error parsing the file";
	}

	/**
	 * (non-javadoc)
	 *
	 * @see BaseException#getErrorDetails()
	 */
	@Override
	public String getErrorDetails() {
		return this.getMessage();
	}

}
