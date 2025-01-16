package com.inesdatamap.mapperbackend.controllers.exceptions;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.inesdatamap.mapperbackend.controllers.errors.BaseErrorCode;
import com.inesdatamap.mapperbackend.controllers.errors.ErrorResponse;
import com.inesdatamap.mapperbackend.controllers.errors.ErrorValidation;
import com.inesdatamap.mapperbackend.controllers.errors.GlobalExceptionHandler;
import com.inesdatamap.mapperbackend.exceptions.DataValidationException;
import com.inesdatamap.mapperbackend.properties.RestApiInfoProperties;
import com.inesdatamap.mapperbackend.utils.RestContextInfo;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test for the {@link GlobalExceptionHandler} class
 *
 * @author gmv
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = GlobalExceptionHandler.class)
@EnableConfigurationProperties(value = RestApiInfoProperties.class)
// @formatter:off
@TestPropertySource(properties = {
		"info.rest-api.name=test-micro",
		"info.rest-api.description=test-description",
		"info.rest-api.version=test-version"
})
		// @formatter:on
class GlobalExceptionHandlerTest {

	@MockBean
	private RestContextInfo restContextInfo;

	@Autowired
	public GlobalExceptionHandler handler;

	@Test
	void handleBaseExceptionTest() {
		// test
		ResponseEntity<ErrorResponse> respuesta = handler.handleBaseException(new DataValidationException("test"));
		// asserts
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), Objects.requireNonNull(respuesta.getBody()).getStatus());
		assertNull(respuesta.getBody().getTraceId());
	}

	@Test
	void handleBaseExceptionTraceIdTest() {
		// prepare
		mockTraceContext();
		// test
		ResponseEntity<ErrorResponse> respuesta = handler.handleBaseException(new DataValidationException("test"));
		// asserts
		assertEquals(BaseErrorCode.VALIDATION.getCode(), Objects.requireNonNull(respuesta.getBody()).getCode());
		assertEquals("test-trace-id", respuesta.getBody().getTraceId());
	}

	@Test
	void handleBaseExceptionValidationTest() {
		// prepare
		mockTraceContext();
		List<ErrorValidation> errors = Collections.singletonList(new ErrorValidation("test-name", "test-code", "test-message"));
		// test
		ResponseEntity<ErrorResponse> respuesta = handler.handleBaseException(new DataValidationException("message", errors));
		// asserts
		assertEquals(BaseErrorCode.VALIDATION.getCode(), Objects.requireNonNull(respuesta.getBody()).getCode());
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), respuesta.getBody().getStatus());
	}

	@Test
	void handleIllegalArgumentExceptionTest() {
		// prepare
		mockTraceContext();
		// test
		ResponseEntity<ErrorResponse> response = handler.handleIllegalArgumentException(new IllegalArgumentException("Message"));
		// asserts
		assertEquals(BaseErrorCode.BAD_REQUEST.getCode(), Objects.requireNonNull(response.getBody()).getCode());
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
	}

	@Test
	void handleConstraintViolationExceptionTest() {
		// prepare
		mockTraceContext();
		ConstraintViolationException ex = mock(ConstraintViolationException.class);
		when(ex.getConstraintViolations()).thenReturn(Collections.emptySet());
		// test
		ResponseEntity<ErrorResponse> response = handler.handleConstraintViolationException(ex);
		// asserts
		assertEquals(BaseErrorCode.VALIDATION.getCode(), Objects.requireNonNull(response.getBody()).getCode());
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getBody().getStatus());
	}

	@Test
	void handleEntityNotFoundExceptionTest() {
		// prepare
		mockTraceContext();
		EntityNotFoundException ex = mock(EntityNotFoundException.class);
		// test
		ResponseEntity<ErrorResponse> response = handler.handleEntityNotFoundException(ex);
		// asserts
		assertEquals(BaseErrorCode.NOT_FOUND.getCode(), Objects.requireNonNull(response.getBody()).getCode());
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
	}

	@Test
	void handleExceptionCustomTest() throws Exception {
		// prepare
		mockTraceContext();
		NoHandlerFoundException exception = new NoHandlerFoundException("", "", mock(HttpHeaders.class));
		ResponseEntity<Object> respuesta = handler.handleException(exception, mock(WebRequest.class));
		assert respuesta != null;
		// test
		ErrorResponse errorResponse = (ErrorResponse) respuesta.getBody();
		// asserts
		assert errorResponse != null;
		assertEquals(BaseErrorCode.NOT_FOUND.name(), errorResponse.getCode());
		assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatus());
	}

	@Test
	void handleMethodArgumentNotValidExceptionTest() throws Exception {
		// prepare
		mockTraceContext();
		BindingResult bindingResult = mock(BindingResult.class);
		when(bindingResult.getGlobalErrors()).thenReturn(Collections.emptyList());
		when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(new FieldError("test", "test", "test")));
		MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
		when(ex.getBindingResult()).thenReturn(bindingResult);
		// test
		ResponseEntity<ErrorResponse> response = (ResponseEntity) handler.handleException(ex, mock(WebRequest.class));
		// asserts
		assertEquals(BaseErrorCode.VALIDATION.getCode(), Objects.requireNonNull(response.getBody()).getCode());
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getBody().getStatus());
	}

	@Test
	void handleExceptionHttpMessageNotReadableExceptionTest() throws Exception {
		// prepare
		mockTraceContext();
		WebRequest webRequest = mock(WebRequest.class);
		HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);
		when(exception.getLocalizedMessage()).thenReturn("message");
		// test
		ResponseEntity<?> respuesta = handler.handleException(exception, webRequest);
		ErrorResponse dto = (ErrorResponse) Objects.requireNonNull(respuesta).getBody();
		// asserts
		assertEquals(BaseErrorCode.BAD_REQUEST.getCode(), Objects.requireNonNull(dto).getCode());
		assertEquals(HttpStatus.BAD_REQUEST.value(), Objects.requireNonNull(dto).getStatus());
	}

	@Test
	void handleExceptionHttpRequestMethodNotSupportedExceptionTest() throws Exception {
		// prepare
		mockTraceContext();
		// test
		ResponseEntity<?> respuesta = handler.handleException(new HttpRequestMethodNotSupportedException("message"),
				mock(WebRequest.class));
		ErrorResponse dto = (ErrorResponse) Objects.requireNonNull(respuesta).getBody();
		// asserts
		assertEquals(BaseErrorCode.BAD_REQUEST.getCode(), Objects.requireNonNull(dto).getCode());
		assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), Objects.requireNonNull(dto).getStatus());
	}

	@Test
	void handleExceptionHttpMediaTypeNotSupportedExceptionTest() throws Exception {
		// prepare
		mockTraceContext();
		// test
		ResponseEntity<?> respuesta = handler.handleException(new HttpMediaTypeNotSupportedException("message"), mock(WebRequest.class));
		ErrorResponse dto = (ErrorResponse) Objects.requireNonNull(respuesta).getBody();
		// asserts
		assertEquals(BaseErrorCode.BAD_REQUEST.getCode(), Objects.requireNonNull(dto).getCode());
		assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), Objects.requireNonNull(dto).getStatus());
	}

	@Test
	void handleExceptionAsyncRequestTimeoutExceptionTest() throws Exception {
		// prepare
		mockTraceContext();
		ResponseEntity<Object> respuesta = handler.handleException(new AsyncRequestTimeoutException(), mock(WebRequest.class));
		assert respuesta != null;
		// test
		ErrorResponse errorResponse = (ErrorResponse) respuesta.getBody();
		// asserts
		assert errorResponse != null;
		assertEquals(BaseErrorCode.UNEXPECTED_ERROR.name(), errorResponse.getCode());
		assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), errorResponse.getStatus());
	}

	@Test
	void handleExceptionCustom01Test() {
		// prepare
		mockTraceContext();
		Exception exception = new Exception("Generic Exception");
		WebRequest webRequest = mock(WebRequest.class);
		// test
		ResponseEntity<?> respuesta = handler.handleExceptionCustom(exception, webRequest);
		// asserts
		assert respuesta != null;
		ErrorResponse dto = (ErrorResponse) respuesta.getBody();
		assert dto != null;
		assertEquals(BaseErrorCode.UNEXPECTED_ERROR.name(), dto.getCode());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), dto.getStatus());
	}

	@Test
	void handleExceptionCustom03Test() {
		// prepare
		mockTraceContext();
		Exception exception = new HttpRequestMethodNotSupportedException("METHOD");
		WebRequest webRequest = mock(WebRequest.class);
		// test
		ResponseEntity<?> respuesta = handler.handleExceptionCustom(exception, webRequest);
		// asserts
		assert respuesta != null;
		ErrorResponse dto = (ErrorResponse) respuesta.getBody();
		assert dto != null;
		assertEquals(BaseErrorCode.UNEXPECTED_ERROR.getCode(), dto.getCode());
		assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), dto.getStatus());
	}

	private void mockTraceContext() {
		when(restContextInfo.getTraceId()).thenReturn("test-trace-id");
		when(restContextInfo.getSpanId()).thenReturn("test-span-id");
	}
}
