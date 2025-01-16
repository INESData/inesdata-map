package com.inesdatamap.mapperbackend.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

/**
 * ProcessBuilderFactory interface
 */
@Component
public class ProcessBuilderFactoryImpl implements ProcessBuilderFactory {

	/**
	 * Logger
	 */
	protected final Log logger = LogFactory.getLog(this.getClass());

	@Override
	public ProcessBuilder createProcessBuilder(String... commands) {

		logger.info("Commands: " + String.join(" ", commands));

		ProcessBuilder processBuilder = new ProcessBuilder(commands);
		processBuilder.redirectErrorStream(true);
		return processBuilder;
	}

}
