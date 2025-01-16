package com.inesdatamap.mapperbackend.model.enums;

/**
 * File Type enumeration
 *
 * @author gmv
 */
public enum DataFileTypeEnum {
	/**
	 * Represents a CSV file type.
	 */
	CSV("CSV", new String[] { "text/csv" }),

	/**
	 * Represents a JSON file type.
	 */
	JSON("JSON", new String[] { "application/json" }),

	/**
	 * Represents an XML file type.
	 */
	XML("XML", new String[] { "application/xml", "text/xml" });

	/**
	 * The code representing the file type.
	 */
	private String code;

	/**
	 * The MIME type representing the file type.
	 */
	private final String[] mimeTypes;

	/**
	 * Constructor for DataFileTypeEnum.
	 *
	 * @param code
	 *            the code representing the file type.
	 * @param mimeTypes
	 *            the MIME type representing the file type.
	 */
	private DataFileTypeEnum(String code, String[] mimeTypes) {
		this.code = code;
		this.mimeTypes = mimeTypes;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * @return the MIME type representing the file type.
	 */
	public String[] getMimeTypes() {
		return this.mimeTypes.clone();
	}

	/**
	 * Validate if the file mimeType is valid according to the enum.
	 *
	 * @param mimeType
	 *            The file mimeType to validate.
	 *
	 * @return true if the mimeType is valid, false otherwise.
	 */
	public static boolean isValidFile(String mimeType) {
		for (DataFileTypeEnum type : values()) {
			for (String validMimeType : type.getMimeTypes()) {
				if (validMimeType.equalsIgnoreCase(mimeType)) {
					return true;
				}
			}
		}
		return false;
	}

}
