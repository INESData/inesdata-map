package com.inesdatamap.mapperbackend.services;

import java.util.List;

/**
 * Service to execute the graph engine
 */
public interface GraphEngineService {

	/**
	 * Run the graph engine
	 *
	 * @param mappingPath
	 * 	the path to the mapping file
	 * @param outputDir
	 * 	the output directory
	 * @param logFilePath
	 * 	the log file path
	 *
	 * @return the result
	 */
	List<String> run(String mappingPath, String outputDir, String logFilePath);

}
