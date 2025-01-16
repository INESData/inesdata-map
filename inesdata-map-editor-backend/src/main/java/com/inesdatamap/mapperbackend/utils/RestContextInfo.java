package com.inesdatamap.mapperbackend.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Context information
 *
 * @author gmv
 */
@Component
public class RestContextInfo {

	private static final String[] IP_HEADER_CANDIDATES = { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
			"HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR",
			"HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };
	private static final String UNKNOWN = "unknown";

	@Autowired
	private Tracer tracer;

	@Autowired
	private HttpServletRequest request;

	/**
	 * Gets trace identifier
	 *
	 * @return {@link String} the identifier of the trace
	 */
	public String getTraceId() {
		Span span = tracer.currentSpan();
		boolean hasContext = span != null && span.context() != null;
		return hasContext ? span.context().traceId() : null;

	}

	/**
	 * Gets span identifier
	 *
	 * @return {@link String} the identifier of the span
	 */
	public String getSpanId() {
		Span span = tracer.currentSpan();
		boolean hasContext = span != null && span.context() != null;
		return hasContext ? span.context().spanId() : null;
	}

	/**
	 * Gets HTTP request IP
	 *
	 * @return {@link String} the IP address of the HTTP request
	 */
	public String getClientIpAddress() {
		if (RequestContextHolder.getRequestAttributes() != null) {
			for (String header : IP_HEADER_CANDIDATES) {
				String ip = request.getHeader(header);
				if (ip != null && !ip.isEmpty() && !UNKNOWN.equalsIgnoreCase(ip)) {
					return ip;
				}
			}
			return request.getRemoteAddr();
		}
		return UNKNOWN;
	}

}
