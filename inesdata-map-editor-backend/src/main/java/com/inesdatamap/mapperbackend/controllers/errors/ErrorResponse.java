package com.inesdatamap.mapperbackend.controllers.errors;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Error response data for REST API
 *
 * @author gmv
 */
@Schema(description = "Error response")
public class ErrorResponse implements Serializable {

	@Serial
	private static final long serialVersionUID = 2721651714907098912L;

	/**
	 * the code of error
	 */
	@Schema(description = "Error code")
	private String code;
	/**
	 * the status of error
	 */
	@Schema(description = "Error status")
	private int status;
	/**
	 * the message of error
	 */
	@Schema(description = "Error message")
	private String message;
	/**
	 * the trace identifier
	 */
	@Schema(description = "Trace identifier")
	private String traceId;
	/**
	 * the span identifier
	 */
	@Schema(description = "Span identifier")
	private String spanId;
	/**
	 * the details of error
	 */
	@Schema(description = "Error details")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String details;
	/**
	 * the list of invalid parameters
	 */
	@Schema(description = "Invalid parameters")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<ErrorValidation> invalidParams;
	/**
	 * the list of services involved
	 */
	@Schema(description = "Services involved")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<String> servicesInvolved;

	/**
	 * Constructor
	 */
	public ErrorResponse() {
		// empty constructor
	}

	/**
	 * Constructor
	 *
	 * @param code
	 * 		the code of error
	 * @param message
	 * 		the message of error
	 * @param details
	 * 		the details of error
	 */
	public ErrorResponse(ErrorCode code, String message, String details) {
		super();
		this.code = code.getCode();
		this.status = code.getStatus();
		this.message = message;
		this.details = details;
	}

	/**
	 * Constructor
	 *
	 * @param code
	 * 		the code of error
	 * @param message
	 * 		the message of error
	 * @param invalidParams
	 * 		the list of invalid parameters
	 */
	public ErrorResponse(ErrorCode code, String message, List<ErrorValidation> invalidParams) {
		super();
		this.code = code.getCode();
		this.status = code.getStatus();
		this.message = message;
		this.invalidParams = new ArrayList<>(invalidParams);
	}

	/**
	 * Gets error code
	 *
	 * @return {@link String} the code of error
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets error code
	 *
	 * @param code
	 * 		the new code of error
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Gets error status
	 *
	 * @return {@code int} the status of error
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Sets error status
	 *
	 * @param status
	 * 		the new status of error
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Sets error message
	 *
	 * @return {@link String} the message of error
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets error message
	 *
	 * @param message
	 * 		the new message of error
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets trace identifier
	 *
	 * @return {@link String} the identifier of trace
	 */
	public String getTraceId() {
		return traceId;
	}

	/**
	 * Sets trace identifier
	 *
	 * @param traceId
	 * 		the new identifier of trace
	 */
	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	/**
	 * Gets span identifier
	 *
	 * @return {@link String} the identifier of span
	 */
	public String getSpanId() {
		return spanId;
	}

	/**
	 * Sets span identifier
	 *
	 * @param spanId
	 * 		the new identifier of span
	 */
	public void setSpanId(String spanId) {
		this.spanId = spanId;
	}

	/**
	 * Gets error details
	 *
	 * @return {@link String} the details of error
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * Sets error details
	 *
	 * @param details
	 * 		the new details of error
	 */
	public void setDetails(String details) {
		this.details = details;
	}

	/**
	 * Gets invalid parameters
	 *
	 * @return {@link List}&lt;{@link ErrorValidation}&gt; the list of invalid parameters
	 */
	public List<ErrorValidation> getInvalidParams() {
		List<ErrorValidation> invalidParamsCopy = null;
		if (invalidParams != null) {
			invalidParamsCopy = new ArrayList<>(invalidParams);
		}
		return invalidParamsCopy;
	}

	/**
	 * Set invalid parameters
	 *
	 * @param invalidParams
	 * 		the new list of invalid parameters
	 */
	public void setInvalidParams(List<ErrorValidation> invalidParams) {
		this.invalidParams = new ArrayList<>(invalidParams);
	}

	/**
	 * Gets services involved
	 *
	 * @return {@link List}&lt;{@link String}&gt;
	 * the list of services involved
	 */
	public List<String> getServicesInvolved() {
		return (this.servicesInvolved != null ? new ArrayList<>(servicesInvolved) : null);
	}

	/**
	 * Set services involved
	 *
	 * @param servicesInvolved
	 * 		the new list of services involved
	 */
	public void setServicesInvolved(List<String> servicesInvolved) {
		this.servicesInvolved = (servicesInvolved != null ? new ArrayList<>(servicesInvolved) : new ArrayList<>());
	}

	/**
	 * Adds a new service entry to the list with the format 'name:version'
	 *
	 * @param name
	 * 		the service name
	 * @param version
	 * 		the service version
	 */
	@JsonIgnore
	public void addToServicesInvolved(String name, String version) {
		if (this.servicesInvolved == null) {
			this.servicesInvolved = new ArrayList<>();
		}
		this.servicesInvolved.add(name + ":" + version);
	}

	/**
	 * (non-javadoc)
	 *
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		// @formatter:off
		return "ErrorResponse: ["
				+ "code: " + code
				+ ", status: " + status
				+ ", message: " + message
				+ ", traceId: " + traceId
				+ ", spanId: " + spanId
				+ ", details: " + details
				+ ", invalidParams: " + invalidParams
				+ ", servicesInvolved: " + servicesInvolved
				+ "]";
		// @formatter:on
	}

}
