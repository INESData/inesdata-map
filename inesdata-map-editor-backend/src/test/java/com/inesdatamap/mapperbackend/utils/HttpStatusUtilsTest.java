package com.inesdatamap.mapperbackend.utils;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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

import com.inesdatamap.mapperbackend.exceptions.DataValidationException;

import jakarta.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

/**
 * Unit test for the {@link HttpStatusUtils} class
 *
 * @author gmv
 */
class HttpStatusUtilsTest {

	@ParameterizedTest
	@MethodSource("httpStatusExceptionTestParams")
	void httpStatusExceptionTest(Exception ex, HttpStatus expected) {
		// test
		HttpStatus result = HttpStatusUtils.getHttpStatusFromException(ex);
		// assert
		assertEquals(expected, result);
	}

	private static Stream<Arguments> httpStatusExceptionTestParams() {
		return Stream.of(arguments(new DataValidationException("test"), UNPROCESSABLE_ENTITY),
				arguments(mock(ConstraintViolationException.class), UNPROCESSABLE_ENTITY),
				arguments(mock(HttpRequestMethodNotSupportedException.class), METHOD_NOT_ALLOWED),
				arguments(mock(HttpMediaTypeNotSupportedException.class), UNSUPPORTED_MEDIA_TYPE),
				arguments(mock(HttpMediaTypeNotAcceptableException.class), NOT_ACCEPTABLE),
				arguments(mock(MissingServletRequestParameterException.class), BAD_REQUEST),
				arguments(mock(ServletRequestBindingException.class), BAD_REQUEST),
				arguments(mock(TypeMismatchException.class), BAD_REQUEST),
				arguments(mock(HttpMessageNotReadableException.class), BAD_REQUEST),
				arguments(mock(MethodArgumentNotValidException.class), BAD_REQUEST),
				arguments(mock(MissingServletRequestPartException.class), BAD_REQUEST), arguments(mock(BindException.class), BAD_REQUEST),
				arguments(mock(NoHandlerFoundException.class), NOT_FOUND),
				arguments(mock(AsyncRequestTimeoutException.class), SERVICE_UNAVAILABLE));
	}
}
