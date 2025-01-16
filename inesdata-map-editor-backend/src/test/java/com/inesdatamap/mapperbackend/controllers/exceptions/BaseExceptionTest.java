package com.inesdatamap.mapperbackend.controllers.exceptions;

import java.io.Serial;

import org.junit.jupiter.api.Test;

import com.inesdatamap.mapperbackend.controllers.errors.BaseErrorCode;
import com.inesdatamap.mapperbackend.controllers.errors.ErrorCode;
import com.inesdatamap.mapperbackend.controllers.errors.ErrorResponse;
import com.inesdatamap.mapperbackend.exceptions.BaseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit test for the {@link BaseException}
 *
 * @author gmv
 */
class BaseExceptionTest {

	@Test
	void constructorTest() {
		// test
		BaseException result = new TestException("test");
		// asserts
		assertEquals("test", result.getMessage());
		assertNull(result.getCause());
	}

	@Test
	void constructorCauseTest() {
		// prepare
		RuntimeException cause = new RuntimeException("test");
		// test
		BaseException e = new TestException("test", cause);
		// asserts
		assertEquals("test", e.getMessage());
		assertEquals(cause, e.getCause());
	}

	@Test
	void errorResponseTest() {
		// prepare
		BaseException e = new TestException("test");
		// test
		ErrorResponse result = e.getErrorResponse();
		// asserts
		assertEquals(BaseErrorCode.UNEXPECTED_ERROR.getCode(), result.getCode());
		assertEquals(BaseErrorCode.UNEXPECTED_ERROR.getStatus(), result.getStatus());
	}

	private static class TestException extends BaseException {

		@Serial
		private static final long serialVersionUID = 6434749993651468907L;

		protected TestException(String message) {
			super(message);
		}

		protected TestException(String message, Throwable cause) {
			super(message, cause);
		}

		@Override
		public ErrorCode getErrorCode() {
			return BaseErrorCode.UNEXPECTED_ERROR;
		}

		@Override
		public String getErrorMessage() {
			return null;
		}

		@Override
		public String getErrorDetails() {
			return null;
		}
	}

}
