package com.inesdatamap.mapperbackend.exceptions;

import java.io.Serial;

import com.inesdatamap.mapperbackend.controllers.errors.BaseErrorCode;
import com.inesdatamap.mapperbackend.controllers.errors.ErrorCode;

/**
 * Exception for file deletion errors
 */
public class FileDeleteException extends BaseException {

	@Serial
	private static final long serialVersionUID = 4297377507013003093L;
	private static final ErrorCode CODE_ERROR = BaseErrorCode.UNEXPECTED_ERROR;

	/**
	 * Constructor
	 *
	 * @param message
	 * 	the message
	 * @param cause
	 * 	the cause
	 */
	public FileDeleteException(String message, Throwable cause) {
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
		return "Error deleting the file";
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
