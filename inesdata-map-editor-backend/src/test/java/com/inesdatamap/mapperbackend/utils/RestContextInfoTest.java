package com.inesdatamap.mapperbackend.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test for the {@link RestContextInfo} class
 *
 * @author gmv
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RestContextInfo.class)
class RestContextInfoTest {

	@MockBean
	private Tracer tracer;
	@MockBean
	private HttpServletRequest request;

	@Autowired
	private RestContextInfo contextInfo;

	@AfterEach
	public void tearDown() {
		RequestContextHolder.resetRequestAttributes();
	}

	@Test
	void getTraceIdTest() {
		// mocks
		TraceContext context = mock(TraceContext.class);
		when(context.traceId()).thenReturn("test-trace-id");
		Span span = mock(Span.class);
		when(span.context()).thenReturn(context);
		when(tracer.currentSpan()).thenReturn(span);
		// test
		assertEquals("test-trace-id", contextInfo.getTraceId());
	}

	@Test
	void getTraceIdNoContext1Test() {
		assertNull(contextInfo.getTraceId());
	}

	@Test
	void getTraceIdNoContext2Test() {
		// mocks
		when(tracer.currentSpan()).thenReturn(mock(Span.class));
		// test
		assertNull(contextInfo.getTraceId());
	}

	@Test
	void getSpanIdTest() {
		// mocks
		TraceContext context = mock(TraceContext.class);
		when(context.spanId()).thenReturn("test-span-id");
		Span span = mock(Span.class);
		when(span.context()).thenReturn(context);
		when(tracer.currentSpan()).thenReturn(span);
		// test
		assertEquals("test-span-id", contextInfo.getSpanId());
	}

	@Test
	void getClientIpAddressTest() {
		// mocks
		when(request.getHeader(anyString())).thenReturn("test-client-ip");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		// test
		assertEquals("test-client-ip", contextInfo.getClientIpAddress());
	}

	@Test
	void getClientIpAddressUnknownTest() {
		// mocks
		when(request.getHeader(anyString())).thenReturn("unknown");
		when(request.getRemoteAddr()).thenReturn("test-remote-address");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		// test
		assertEquals("test-remote-address", contextInfo.getClientIpAddress());
	}

	@Test
	void getClientIpAddressEmptyTest() {
		// mocks
		when(request.getHeader(anyString())).thenReturn("");
		when(request.getRemoteAddr()).thenReturn("test-remote-address");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		// test
		assertEquals("test-remote-address", contextInfo.getClientIpAddress());
	}

	@Test
	void getClientIpAddressNullTest() {
		// mocks
		when(request.getRemoteAddr()).thenReturn("test-remote-address");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		// test
		assertEquals("test-remote-address", contextInfo.getClientIpAddress());
	}

	@Test
	void getClientIpAddressNoRequestTest() {
		// test
		assertEquals("unknown", contextInfo.getClientIpAddress());
	}
}
