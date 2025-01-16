package com.inesdatamap.mapperbackend.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inesdatamap.mapperbackend.exceptions.GraphEngineException;
import com.inesdatamap.mapperbackend.services.GraphEngineService;
import com.inesdatamap.mapperbackend.utils.FileUtils;
import com.inesdatamap.mapperbackend.utils.ProcessBuilderFactory;

/**
 * Graph engine service implementation
 */
@Service
public class GraphEngineServiceImpl implements GraphEngineService {

	@Autowired
	private ProcessBuilderFactory processBuilderFactory;

	/**
	 * Logger
	 */
	protected final Log logger = LogFactory.getLog(this.getClass());

	@Override
	public List<String> run(String mappingPath, String knowledgeGraphOutputFilePath, String logFilePath) {

		List<String> results;

		// Create the output directory
		FileUtils.createDirectories(Paths.get(knowledgeGraphOutputFilePath).getParent());

		String[] commands = new String[] { "python", "-m", "kg_generation", "-m", mappingPath, "-o", knowledgeGraphOutputFilePath };

		// Run the graph engine
		results = startProcess(logFilePath, commands);

		return results;
	}

	private List<String> startProcess(String logFilePath, String... commands) {

		List<String> results;

		try {

			ProcessBuilder processBuilder = processBuilderFactory.createProcessBuilder(commands);

			Process process = processBuilder.start();
			results = readProcessOutput(process.getInputStream());

			int exitCode = process.waitFor();
			logger.info("ExitCode: " + exitCode);

			String processOutput = String.join(System.lineSeparator(), results);

			// Create log file
			FileUtils.createFile(processOutput.getBytes(), logFilePath);

			if (exitCode != 0) {
				logger.error(processOutput);
				throw new GraphEngineException("GraphEngine error", null);
			}

		} catch (IOException e) {
			throw new GraphEngineException("GraphEngine error", e);
		} catch (InterruptedException e) {
			logger.warn("Thread interrupted!");
			// Restore interrupted state...
			Thread.currentThread().interrupt();
			throw new GraphEngineException("GraphEngine error", e);
		}

		return results;
	}

	private static List<String> readProcessOutput(InputStream inputStream) throws IOException {
		if (inputStream == null) {
			return List.of();
		}
		try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream))) {
			return output.lines().toList();
		}
	}

}
