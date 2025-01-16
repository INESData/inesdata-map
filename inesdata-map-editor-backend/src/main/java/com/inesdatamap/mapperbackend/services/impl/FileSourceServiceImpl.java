package com.inesdatamap.mapperbackend.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.inesdatamap.mapperbackend.exceptions.FileCreationException;
import com.inesdatamap.mapperbackend.exceptions.FileParserException;
import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.model.dto.FileSourceDTO;
import com.inesdatamap.mapperbackend.model.enums.DataFileTypeEnum;
import com.inesdatamap.mapperbackend.model.jpa.FileSource;
import com.inesdatamap.mapperbackend.model.mappers.FileSourceMapper;
import com.inesdatamap.mapperbackend.properties.AppProperties;
import com.inesdatamap.mapperbackend.repositories.jpa.FileSourceRepository;
import com.inesdatamap.mapperbackend.services.FileSourceService;
import com.inesdatamap.mapperbackend.utils.Constants;
import com.inesdatamap.mapperbackend.utils.FileUtils;

import jakarta.persistence.EntityNotFoundException;

/**
 * File source service implementation
 *
 * @see FileSourceService
 */
@Service
@Transactional(rollbackFor = FileCreationException.class)
public class FileSourceServiceImpl implements FileSourceService {

	@Autowired
	private FileSourceMapper fileSourceMapper;

	@Autowired
	private FileSourceRepository fileSourceRepository;

	@Autowired
	private AppProperties appProperties;

	/**
	 * Saves a data source
	 *
	 * @param fileSourceDTO
	 * 	the FileSourceDTO
	 * @param file
	 * 	file content to save
	 *
	 * @return the saved data source
	 */
	@Override
	public DataSourceDTO createFileSource(FileSourceDTO fileSourceDTO, MultipartFile file) {

		if (fileSourceDTO == null) {
			throw new IllegalArgumentException("The file base source has no data");
		}

		// DTO to entity
		FileSource fileSource = this.fileSourceMapper.dtoToEntity(fileSourceDTO);

		// Save new entity
		FileSource savedFileSource = this.fileSourceRepository.save(fileSource);

		if (file != null && !file.isEmpty()) {

			// Validate the file content
			FileUtils.validateFile(file);

			if (fileSource.getFileType().equals(DataFileTypeEnum.CSV)) {

				// Read CSV file headers
				String headers = FileUtils.processFileHeaders(file);
				savedFileSource.setFields(headers);

			}

			// Build file path
			String filePath = String.join(File.separator, this.appProperties.getDataProcessingPath(), Constants.DATA_INPUT_FOLDER_NAME,
				savedFileSource.getId().toString());

			// Set values in FileSource
			savedFileSource.setFilePath(filePath);

			// Save file
			String fileName = FileUtils.saveFile(file, filePath);
			savedFileSource.setFileName(fileName);

			savedFileSource = this.fileSourceRepository.save(savedFileSource);
		}

		return this.fileSourceMapper.entityToDto(savedFileSource);
	}

	/**
	 * Updates an existing file source
	 *
	 * @param id
	 * 	The identifier of the file source to be updated
	 * @param fileSourceDTO
	 * 	The FileSourceDTO
	 *
	 * @return the updated file source.
	 */
	@Override
	public DataSourceDTO updateFileSource(Long id, FileSourceDTO fileSourceDTO) {

		if (fileSourceDTO == null) {
			throw new IllegalArgumentException("The file source has no data");
		}

		// Get DB entity
		FileSource fileSourceDB = this.getEntity(id);

		// Update existing entity
		this.fileSourceMapper.merge(fileSourceDTO, fileSourceDB);

		return this.fileSourceMapper.entityToDto(fileSourceDB);
	}

	/**
	 * Retrieves a file source entity by its ID.
	 *
	 * @param id
	 * 	the ID of the file source to retrieve
	 *
	 * @return the file source entity corresponding to the given ID
	 */
	@Override
	public FileSource getEntity(Long id) {
		return this.fileSourceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
	}

	/**
	 * Retrieves a FileSourceDTO by its identifier.
	 *
	 * @param id
	 * 	the unique identifier of the file source entity
	 *
	 * @return the file source dto corresponding to the given ID
	 */
	@Override
	public FileSourceDTO getFileSourceById(Long id) {
		return this.fileSourceMapper.entityToDto(this.getEntity(id));

	}

	/**
	 * Retrieves a list of FileSourceDTO objects filtered by the specified file type.
	 *
	 * @param fileType
	 * 	The type of the file sources to retrieve
	 *
	 * @return A list of FileSourceDTO objects representing the file sources of the specified type.
	 */
	@Override
	public List<FileSourceDTO> getFileSourceByType(String fileType) {

		DataFileTypeEnum typeEnum = DataFileTypeEnum.valueOf(fileType);
		List<FileSource> fileSources = this.fileSourceRepository.findByFileTypeOrderByNameAsc(typeEnum);

		return this.fileSourceMapper.fileSourcesToFileSourceDTOs(fileSources);
	}

	/**
	 * Retrieves the fields of a file source as a list of strings, based on the given source ID.
	 *
	 * @param id
	 * 	The ID of the file source whose fields are to be retrieved.
	 *
	 * @return A list of field names extracted from the fields property of the FileSource entity
	 */
	@Override
	public List<String> getFileFields(Long id) {

		FileSource fileSource = this.getEntity(id);
		String fields = fileSource.getFields();

		if (fields == null || fields.isEmpty()) {
			throw new FileParserException("There are no fields in the file");
		}

		String[] delimitedFields = fields.split(Constants.FIELD_DELIMITER_REGEX, -1);
		// If the fields are delimited by not allow delimiter, delimited fields will have one string element
		if (delimitedFields.length < Constants.NUMBER_2) {
			throw new FileParserException("The CSV delimiter is not valid");
		}

		return Arrays.asList(delimitedFields);

	}

	/**
	 * Retrieves a list of attributes and leaf node XPaths from an XML file based on the given file source ID.
	 *
	 * @param id
	 * 	the ID of the file source entity
	 *
	 * @return a sorted list of unique attributes and leaf node XPaths found in the XML file
	 *
	 * @throws IllegalArgumentException
	 * 	if the file does not exist or is a directory.
	 * @throws FileParserException
	 * 	if an error occurs while processing the XML file.
	 */
	@Override
	public List<String> getFileAttributes(Long id) {

		// Get entity
		FileSource fileSource = this.getEntity(id);

		// Validate filePath and fileName
		if (fileSource.getFilePath() == null || fileSource.getFileName() == null) {
			throw new IllegalArgumentException("Invalid file path or name");
		}

		File file = new File(fileSource.getFilePath(), FilenameUtils.getName(fileSource.getFileName()));

		// Check if the file exists and is not a directory
		if (!file.exists() || file.isDirectory()) {
			throw new IllegalArgumentException("File does not exist or is a directory: " + file.getPath());
		}

		// Set to store unique attributes and leaf node XPaths
		Set<String> attributes = new HashSet<>();

		// Process the XML file and extract attributes and leaf node XPaths
		try (InputStream inputStream = new FileInputStream(file)) {

			// Create XMLInputFactory and disable DTDs entirely for that factory
			XMLInputFactory factory = XMLInputFactory.newInstance();
			factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);

			// Create XMLStreamReader with the configured factory
			XMLStreamReader reader = factory.createXMLStreamReader(inputStream);
			extractAttributes(reader, attributes);

		} catch (IOException | XMLStreamException e) {
			throw new FileParserException("Error processing XML file", e);
		}

		// Convert set to list and sort it lexicographically
		List<String> sortedAttributes = new ArrayList<>(attributes);
		Collections.sort(sortedAttributes);

		return sortedAttributes;

	}

	/**
	 * Extracts attributes and leaf node XPaths from an XML stream and stores them in the provided set
	 *
	 * @param reader
	 * 	the XMLStreamReader to read the XML content from
	 * @param attributes
	 * 	a Set to store the unique attributes and leaf node XPaths extracted from the XML
	 *
	 * @throws XMLStreamException
	 * 	if an error occurs during XML parsing.
	 */
	private static void extractAttributes(XMLStreamReader reader, Set<String> attributes) throws XMLStreamException {

		StringBuilder currentPath = new StringBuilder();

		// Loop through XML events
		while (reader.hasNext()) {
			int event = reader.next();

			if (event == XMLStreamConstants.START_ELEMENT) {
				// Process the start element and check for attributes
				processStartElement(reader, currentPath, attributes);

			} else if (event == XMLStreamConstants.CHARACTERS) {
				// Process character data for leaf nodes
				processCharacters(reader, currentPath, attributes);

			} else if (event == XMLStreamConstants.END_ELEMENT) {
				// Remove the last element from the current XPath
				removeLastElementFromXPath(currentPath);
			}
		}
	}

	/**
	 * Processes the start element of the XML and updates the current XPath.
	 *
	 * @param reader
	 * 	the XMLStreamReader positioned at the start element in the XML stream
	 * @param currentPath
	 * 	a StringBuilder representing the current XPath of the element
	 * @param attributes
	 * 	a Set to store the unique attributes with their full XPath
	 *
	 * @return true to indicate that the current element could potentially be a leaf node.
	 */
	private static void processStartElement(XMLStreamReader reader, StringBuilder currentPath, Set<String> attributes) {

		// Update the path with the current element
		if (currentPath.length() > 0) {
			currentPath.append(Constants.PATH_SEPARATOR);
		}
		currentPath.append(reader.getLocalName());

		// Add each attribute of the current element to the set with its full XPath
		for (int i = 0; i < reader.getAttributeCount(); i++) {
			attributes.add("/" + currentPath + Constants.ATTRIBUTE_SELECTOR + reader.getAttributeLocalName(i));
		}
	}

	/**
	 * Processes character data within an XML element and updates the attributes set if the element is a leaf node
	 *
	 * @param reader
	 * 	the XMLStreamReader positioned at the character data in the XML stream
	 * @param currentPath
	 * 	a StringBuilder representing the current XPath of the element
	 * @param attributes
	 * 	a Set to store unique leaf node XPaths
	 */
	private static void processCharacters(XMLStreamReader reader, StringBuilder currentPath, Set<String> attributes) {

		// Get the text from the node
		String text = reader.getText().trim();

		// If the text is not empty
		if (!text.isEmpty()) {
			attributes.add("/" + currentPath.toString());
		}
	}

	/**
	 * Removes the last element from the current XPath
	 *
	 * This method modifies the currentPath by finding the last slash ("/") and truncating the StringBuilder to remove the last element in
	 * the XPath. If no slash is found, the entire currentPath is cleared, indicating that the root has been reached.
	 *
	 * @param currentPath
	 * 	a StringBuilder representing the current XPath to be modified
	 */
	private static void removeLastElementFromXPath(StringBuilder currentPath) {

		// Find the last slash in the XPath
		int lastSlashIndex = currentPath.lastIndexOf(Constants.PATH_SEPARATOR);
		if (lastSlashIndex >= 0) {
			// Adjust the StringBuilder length to the index of the last slash
			// Removes the last element from the XPath
			currentPath.setLength(lastSlashIndex);
		} else {
			// If no slashes are found, clear the StringBuilder
			currentPath.setLength(0);
		}
	}

}
