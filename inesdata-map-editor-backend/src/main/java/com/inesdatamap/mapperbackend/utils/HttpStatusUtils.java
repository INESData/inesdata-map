package com.inesdatamap.mapperbackend.utils;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.inesdatamap.mapperbackend.exceptions.BaseException;

import jakarta.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

/**
 * Utility for gets http status from exception
 *
 * @author gmv
 */
public final class HttpStatusUtils {

	/**
	 * Private constructor
	 */
	private HttpStatusUtils() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Gets the http status from an exception
	 *
	 * @param ex
	 * 		the exception
	 *
	 * @return {@link HttpStatus} the http status of exception
	 */
	public static HttpStatus getHttpStatusFromException(Exception ex) {
		HttpStatus status = INTERNAL_SERVER_ERROR;
		if (ex instanceof BaseException base) {
			status = HttpStatus.resolve(base.getErrorCode().getStatus());
		} else if (ex instanceof ConstraintViolationException) {
			status = UNPROCESSABLE_ENTITY;
		} else if (ex instanceof HttpRequestMethodNotSupportedException) {
			status = METHOD_NOT_ALLOWED;
		} else if (ex instanceof HttpMediaTypeNotSupportedException) {
			status = UNSUPPORTED_MEDIA_TYPE;
		} else if (ex instanceof HttpMediaTypeNotAcceptableException) {
			status = NOT_ACCEPTABLE;
		} else if (ex instanceof MissingServletRequestParameterException) {
			status = BAD_REQUEST;
		} else if (ex instanceof ServletRequestBindingException) {
			status = BAD_REQUEST;
		} else if (ex instanceof TypeMismatchException) {
			status = BAD_REQUEST;
		} else if (ex instanceof HttpMessageNotReadableException) {
			status = BAD_REQUEST;
		} else if (ex instanceof MethodArgumentNotValidException) {
			status = BAD_REQUEST;
		} else if (ex instanceof MissingServletRequestPartException) {
			status = BAD_REQUEST;
		} else if (ex instanceof BindException) {
			status = BAD_REQUEST;
		} else if (ex instanceof NoHandlerFoundException) {
			status = NOT_FOUND;
		} else if (ex instanceof AsyncRequestTimeoutException) {
			status = SERVICE_UNAVAILABLE;
		}
		return status;
	}

}
