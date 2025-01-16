package com.inesdatamap.mapperbackend.exceptions;

import java.io.Serial;

import com.inesdatamap.mapperbackend.controllers.errors.BaseErrorCode;
import com.inesdatamap.mapperbackend.controllers.errors.ErrorCode;

/**
 * Exception for graph engine errors
 */
public class RmlWriteException extends BaseException {

	@Serial
	private static final long serialVersionUID = -3763870259946469505L;
	private static final ErrorCode CODE_ERROR = BaseErrorCode.UNEXPECTED_ERROR;

	/**
	 * Constructor
	 *
	 * @param message
	 * 	the message
	 * @param cause
	 * 	the cause
	 */
	public RmlWriteException(String message, Throwable cause) {
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
		return "Error writing the RML";
	}

	/**
	 * (non-javadoc)
	 *
	 * @see BaseException#getErrorDetails()
	 */
	@Override
	public String getErrorDetails() {
		return getMessage();
	}
}
