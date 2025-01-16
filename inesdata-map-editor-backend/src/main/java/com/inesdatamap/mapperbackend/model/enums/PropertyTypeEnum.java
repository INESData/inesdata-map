package com.inesdatamap.mapperbackend.model.enums;

/**
 * Property Type enumeration
 *
 * @author gmv
 */
public enum PropertyTypeEnum {
	/**
	 * Represents a DATA property type.
	 */
	DATA("DATA"),

	/**
	 * Represents an OBJECT property type.
	 */
	OBJECT("OBJECT"),

	/**
	 * Represents an ANNOTATION property type.
	 */
	ANNOTATION("ANNOTATION");

	/**
	 * The code representing the property type.
	 */
	private String code;

	/**
	 * Constructor for PropertyTypeEnum.
	 *
	 * @param code
	 *            the code representing the property type.
	 */
	private PropertyTypeEnum(String code) {
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return this.code;
	}

}
