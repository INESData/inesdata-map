package com.inesdatamap.mapperbackend.exceptions;

import java.io.Serial;

import com.inesdatamap.mapperbackend.controllers.errors.BaseErrorCode;
import com.inesdatamap.mapperbackend.controllers.errors.ErrorCode;

/**
 * Exception for ontology parsing errors
 */
public class OntologyParserException extends BaseException {

	@Serial
	private static final long serialVersionUID = 7328972212757178767L;
	private static final ErrorCode CODE_ERROR = BaseErrorCode.UNEXPECTED_ERROR;

	/**
	 * Constructor
	 *
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public OntologyParserException(String message, Throwable cause) {
		super(message, cause);
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
		return "Error parsing ontology content";
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
