package com.inesdatamap.mapperbackend.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.mime.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.inesdatamap.mapperbackend.exceptions.FileCreationException;
import com.inesdatamap.mapperbackend.exceptions.FileDeleteException;
import com.inesdatamap.mapperbackend.model.enums.DataFileTypeEnum;
import com.inesdatamap.mapperbackend.model.jpa.Ontology;

/**
 * Utility for file validation and file
 *
 * @author gmv
 */
public final class FileUtils {

	// Private constructor to prevent instantiation
	private FileUtils() {

	}

	/**
	 * Validates the file mimeType against the supported defined in the DataFileTypeEnum.
	 *
	 * @param mimeType
	 * 	the MIME type to validate
	 *
	 * @throws IllegalArgumentException
	 * 	if the provided mimetype is not supported
	 */
	public static void validateFileExtension(String mimeType) {
		if (!DataFileTypeEnum.isValidFile(mimeType)) {
			throw new IllegalArgumentException("Unsupported file extension: " + mimeType);
		}
	}

	/**
	 * Validates if the file content matches its MIME type.
	 *
	 * @param file
	 * 	the file
	 */
	public static void validateFile(MultipartFile file) {

		try (TikaInputStream tikaInputStream = TikaInputStream.get(file.getInputStream())) {

			TikaConfig tika = new TikaConfig();
			Metadata metadata = new Metadata();
			metadata.set(TikaCoreProperties.RESOURCE_NAME_KEY, file.getOriginalFilename());

			MediaType mimetype = tika.getDetector().detect(tikaInputStream, metadata);

			if (mimetype.equals(MediaType.APPLICATION_XML) || mimetype.equals(MediaType.text("xml")) || mimetype.equals(
				MediaType.TEXT_HTML)) {

				// Check if XML is a valid file
				FileUtils.isValidXML(file);

				if (mimetype.equals(MediaType.TEXT_HTML)) {
					// Tika detects XML files as text/html if they contain HTML characters such as '<' or '>' codified
					mimetype = MediaType.APPLICATION_XML;
				}
			}

			validateFileExtension(mimetype.toString());
		} catch (IOException | TikaException e) {
			throw new FileCreationException("File validation error", e);
		}

	}

	/**
	 * Processes the content of the MultipartFile and sets it as the content of the specified Ontology.
	 *
	 * @param file
	 * 	the MultipartFile containing the content to be processed
	 * @param ontology
	 * 	the Ontology entity where the file content will be stored
	 *
	 * @throws UncheckedIOException
	 * 	if an error occurs while reading the file content.
	 */
	public static void processFileContent(MultipartFile file, Ontology ontology) {

		try {
			// Convert the file content to a byte array
			byte[] fileContent = file.getBytes();

			// Set the byte array as the content of the ontology
			ontology.setContent(fileContent);

		} catch (IOException e) {
			throw new UncheckedIOException("Failed to store file content", e);
		}
	}

	/**
	 * Reads the first line of the given MultipartFile to extract headers and sets them in the provided FileSource object.
	 *
	 * @param file
	 * 	the MultipartFile from which to read the headers
	 *
	 * @return the headers read from the file
	 *
	 * @throws UncheckedIOException
	 * 	if an error occurs while reading the file content
	 */
	public static String processFileHeaders(MultipartFile file) {
		try {
			// Read file content
			BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

			// Read file's first line with headers
			return reader.readLine();

		} catch (IOException e) {
			throw new UncheckedIOException("Failed to store file headers", e);
		}
	}

	/**
	 * Saves the given MultipartFile to the specified path.
	 *
	 * @param file
	 * 	the MultipartFile to save
	 * @param path
	 * 	the path where the file will be saved
	 *
	 * @return the file name
	 */
	public static String saveFile(MultipartFile file, String path) {

		try {
			String fileName = UUID.randomUUID() + FilenameUtils.getName(file.getOriginalFilename());

			Files.createDirectories(Paths.get(path));

			File newFile = new File(path, Objects.requireNonNull(fileName));
			file.transferTo(newFile);

			return newFile.getName();

		} catch (IOException e) {
			throw new FileCreationException("Failed to save file", e);
		}
	}

	/**
	 * Creates a file with the specified content.
	 *
	 * @param content
	 * 	the content of the file
	 * @param path
	 * 	the path where the file will be created
	 *
	 * @return the file
	 */
	public static File createFile(byte[] content, String path) {
		try {

			Path filePath = Paths.get(path);
			Files.createDirectories(filePath.getParent());
			Path file = Files.createFile(filePath);
			Files.write(file, content);

			return file.toFile();

		} catch (IOException e) {
			throw new FileCreationException("Failed to create file", e);
		}
	}

	/**
	 * Deletes the directory at the specified path.
	 *
	 * @param path
	 * 	the path of the file to delete
	 */
	public static void deleteDirectory(Path path) {

		try {
			org.apache.commons.io.FileUtils.deleteDirectory(path.toFile());
		} catch (IOException e) {
			throw new FileDeleteException("Failed to delete directory: " + path, e);
		}

	}

	/**
	 * Creates the directories at the specified path.
	 *
	 * @param path
	 * 	the path
	 */
	public static void createDirectories(Path path) {

		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			throw new FileCreationException("Failed to create directories: " + path, e);
		}
	}

	/**
	 * Gets the file path from the output directory.
	 *
	 * @param dataProcessingPath
	 * 	the data processing path
	 * @param mappingId
	 * 	the mapping id
	 * @param executionTime
	 * 	the execution time
	 * @param fileName
	 * 	the file name
	 *
	 * @return the file path from the output directory
	 */
	public static String getFilePathFromOutputDirectory(String dataProcessingPath, Long mappingId, OffsetDateTime executionTime,
		String fileName) {
		return String.join(File.separator, dataProcessingPath, Constants.DATA_OUTPUT_FOLDER_NAME, mappingId.toString(),
			String.valueOf(executionTime.toEpochSecond()), fileName);
	}

	/**
	 * Validates if the provided MultipartFile contains a valid XML structure.
	 *
	 * @param file
	 * 	the MultipartFile representing the file to be validated
	 *
	 * @throws FileCreationException
	 * 	if the file is not a valid XML or if an error occurs during parsing
	 */
	public static void isValidXML(MultipartFile file) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			dbf.setFeature(Constants.DOCTYPE_DECL, true);
			dbf.setXIncludeAware(false);

			// Parse the XML file using the secured DocumentBuilderFactory
			DocumentBuilder db = dbf.newDocumentBuilder();
			db.parse(file.getInputStream());

		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new FileCreationException("File is not a valid XML", e);
		}
	}

}
