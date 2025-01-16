package com.inesdatamap.mapperbackend.controllers.errors;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * Unit test for the {@link BaseErrorCode}
 *
 * @author gmv
 */
class BaseErrorCodeTest {

	@Test
	void unexpectedErrorTest() {
		// test
		BaseErrorCode result = BaseErrorCode.UNEXPECTED_ERROR;
		// asserts
		assertEquals("UNEXPECTED_ERROR", result.getCode());
		assertEquals(500, result.getStatus());
	}

	@Test
	void serviceUnavailableTest() {
		// test
		BaseErrorCode result = BaseErrorCode.SERVICE_UNAVAILABLE;
		// asserts
		assertEquals("SERVICE_UNAVAILABLE", result.getCode());
		assertEquals(503, result.getStatus());
	}

	@Test
	void notFoundTest() {
		// test
		BaseErrorCode result = BaseErrorCode.NOT_FOUND;
		// asserts
		assertEquals("NOT_FOUND", result.getCode());
		assertEquals(404, result.getStatus());
	}

	@Test
	void unauthorizedTest() {
		// test
		BaseErrorCode result = BaseErrorCode.UNAUTHORIZED;
		// asserts
		assertEquals("UNAUTHORIZED", result.getCode());
		assertEquals(401, result.getStatus());
	}

	@Test
	void forbiddenTest() {
		// test
		BaseErrorCode result = BaseErrorCode.FORBIDDEN;
		// asserts
		assertEquals("FORBIDDEN", result.getCode());
		assertEquals(403, result.getStatus());
	}

	@Test
	void badRequestTest() {
		// test
		BaseErrorCode result = BaseErrorCode.BAD_REQUEST;
		// asserts
		assertEquals("BAD_REQUEST", result.getCode());
		assertEquals(400, result.getStatus());
	}

	@Test
	void nullDataTest() {
		// test
		BaseErrorCode result = BaseErrorCode.NULL_DATA;
		// asserts
		assertEquals("NULL_DATA", result.getCode());
		assertEquals(400, result.getStatus());
	}

	@Test
	void wrongFormatTest() {
		// test
		BaseErrorCode result = BaseErrorCode.WRONG_FORMAT;
		// asserts
		assertEquals("WRONG_FORMAT", result.getCode());
		assertEquals(400, result.getStatus());
	}

	@Test
	void businessKeyTest() {
		// test
		BaseErrorCode result = BaseErrorCode.BUSINESS_KEY;
		// asserts
		assertEquals("BUSINESS_KEY", result.getCode());
		assertEquals(500, result.getStatus());
	}

	@ParameterizedTest
	@MethodSource("getByStatusTestParams")
	void getByStatusTest(Integer status, BaseErrorCode expected) {
		// test
		ErrorCode result = BaseErrorCode.getByStatus(status);
		// assert
		assertEquals(expected, result);
	}

	private static Stream<Arguments> getByStatusTestParams() {
		// @formatter:off
		return Stream.of(
				arguments(500, BaseErrorCode.UNEXPECTED_ERROR),
				arguments(999, BaseErrorCode.UNEXPECTED_ERROR),
				arguments(404, BaseErrorCode.NOT_FOUND),
				arguments(400, BaseErrorCode.BAD_REQUEST)
		);
		// @formatter:on
	}

}
