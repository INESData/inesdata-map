package com.inesdatamap.mapperbackend.controllers.exceptions;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.inesdatamap.mapperbackend.controllers.errors.BaseErrorCode;
import com.inesdatamap.mapperbackend.controllers.errors.ErrorResponse;
import com.inesdatamap.mapperbackend.controllers.errors.ErrorValidation;
import com.inesdatamap.mapperbackend.exceptions.DataValidationException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test for the {@link DataValidationException} class
 *
 * @author gmv
 */
class DataValidationExceptionTest {

	@Test
	void constructorTest() {
		// prepare
		List<ErrorValidation> errors = Collections.singletonList(new ErrorValidation("test-name", "test-code", "test-message"));
		// test
		DataValidationException exception = new DataValidationException("Data validation problem", errors);
		// asserts
		assertThat(exception).isInstanceOf(RuntimeException.class);
		assertEquals(errors, exception.getErrorsValidation());
	}

	@Test
	void constructorConstraintViolationExceptionTest() {
		// prepare
		ConstraintViolation<?> violation = mock(ConstraintViolation.class);
		when(violation.getPropertyPath()).thenReturn(mock(Path.class));
		when(violation.getMessage()).thenReturn("message");
		ConstraintViolationException exception = mock(ConstraintViolationException.class);
		when(exception.getConstraintViolations()).thenReturn(Collections.singleton(violation));
		// test
		DataValidationException e = new DataValidationException(exception);
		// asserts
		assertThat(e).isInstanceOf(DataValidationException.class);
		assertEquals("message", e.getErrorsValidation().get(0).getDetails());
	}

	@Test
	void constructorMethodArgumentNotValidExceptionTest() {
		// prepare
		FieldError error = mock(FieldError.class);
		when(error.getField()).thenReturn("field");
		when(error.getDefaultMessage()).thenReturn("message");
		BindingResult errors = mock(BindingResult.class);
		when(errors.getFieldErrors()).thenReturn(Collections.singletonList(error));
		MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
		when(exception.getBindingResult()).thenReturn(errors);
		// test
		DataValidationException e = new DataValidationException(exception);
		// asserts
		assertThat(e).isInstanceOf(DataValidationException.class);
		assertEquals("message", e.getErrorsValidation().get(0).getDetails());
	}

	@Test
	void responseExceptionTest() {
		// prepare
		String message = "Data validation problem";
		List<ErrorValidation> errors = Collections.singletonList(new ErrorValidation("test-name", "test-code", "test-message"));
		DataValidationException e = new DataValidationException(message, errors);
		// test
		ErrorResponse response = e.getErrorResponse();
		// asserts
		assertEquals(BaseErrorCode.VALIDATION.getCode(), response.getCode());
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
	}

}

