package com.inesdatamap.mapperbackend.utils;

/**
 * Constants class
 *
 * @author gmv
 */
public final class Constants {

	// Numbers
	/**
	 * Constant representing the number 0
	 */
	public static final int NUMBER_0 = 0;
	/**
	 * Constant representing the number 2
	 */
	public static final int NUMBER_2 = 2;
	/**
	 * Constant representing the number 10
	 */
	public static final int NUMBER_10 = 10;

	/**
	 * Constant representing the number 16
	 */
	public static final int NUMBER_16 = 16;

	/**
	 * Constant for the data input folder
	 */
	public static final String DATA_INPUT_FOLDER_NAME = "input";

	/**
	 * Constant for the data output folder
	 */
	public static final String DATA_OUTPUT_FOLDER_NAME = "output";

	/**
	 * Constant for the KG output file name
	 */
	public static final String KG_OUTPUT_FILE_NAME = "knowledge-graph.nt";

	/**
	 * Constant for the graph engine log file name
	 */
	public static final String GRAPH_ENGINE_LOG_FILE_NAME = "graph-engine.log";

	/**
	 * Constant for the mapping file name
	 */
	public static final String MAPPING_FILE_NAME = "mapping.ttl";

	// Filters
	/**
	 * Constant representing the sort by name
	 */
	public static final String SORT_BY_NAME = "name";

	/**
	 * Constant representing the sort by date
	 */
	public static final String SORT_BY_DATE = "executionDate";

	// Regex

	/**
	 * Constant for field delimiter regex
	 */
	public static final String FIELD_DELIMITER_REGEX = "[,;\t]";

	/**
	 * Constant for path separator
	 */
	public static final String PATH_SEPARATOR = "/";
	/**
	 * Constant for path attribute selector
	 */
	public static final String ATTRIBUTE_SELECTOR = "/@";

	// URLs
	/**
	 * Constant for doctype-decl
	 */
	public static final String DOCTYPE_DECL = "http://apache.org/xml/features/disallow-doctype-decl";

	private Constants() {
		// Empty constructor to avoid initialization
	}

}
