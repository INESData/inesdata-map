package com.inesdatamap.mapperbackend.controllers.errors;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Error response data for fields validations
 *
 * @author gmv
 */
public class ErrorValidation implements Serializable {

	@Serial
	private static final long serialVersionUID = 3904372875916968293L;

	/**
	 * the name of field
	 */
	@Schema(description = "Field name")
	private String field;
	/**
	 * the code of validation
	 */
	@Schema(description = "Validation code")
	private String code;
	/**
	 * the details of validation
	 */
	@Schema(description = "Validation details")
	private String details;

	/**
	 * Constructor
	 *
	 * @param field
	 * 		the name of field
	 * @param code
	 * 		the code of validation
	 * @param details
	 * 		the details of validation
	 */
	public ErrorValidation(@JsonProperty("field") String field, @JsonProperty("code") String code,
			@JsonProperty("details") String details) {
		this.field = field;
		this.code = code;
		this.details = details;
	}

	/**
	 * Gets field name
	 *
	 * @return {@link String} the name of field
	 */
	public String getField() {
		return field;
	}

	/**
	 * Sets field name
	 *
	 * @param field
	 * 		new name of field
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * Gets validation code
	 *
	 * @return {@link String} new code of validation
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets validation code
	 *
	 * @param code
	 * 		new code of validation
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Gets validation details
	 *
	 * @return {@link String} the details of validation
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * Sets validation details
	 *
	 * @param details
	 * 		new details of validation
	 */
	public void setDetails(String details) {
		this.details = details;
	}

	/**
	 * (non-javadoc)
	 *
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		// @formatter:off
		return "ErrorValidation: ["
				+ "field: " + field
				+ ", code: " + code
				+ ", details: " + details
				+ "]";
		// @formatter:on
	}
}

