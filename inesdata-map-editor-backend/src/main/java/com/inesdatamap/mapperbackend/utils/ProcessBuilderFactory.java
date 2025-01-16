package com.inesdatamap.mapperbackend.utils;

/**
 * ProcessBuilderFactory interface
 */
public interface ProcessBuilderFactory {

	/**
	 * Create a process builder
	 *
	 * @param command
	 * 	the command to execute
	 *
	 * @return the process builder
	 */
	ProcessBuilder createProcessBuilder(String... command);

}
