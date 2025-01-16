package com.inesdatamap.mapperbackend.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.inesdatamap.mapperbackend.exceptions.FileCreationException;

@ExtendWith(MockitoExtension.class)
class FileUtilsTest {

	@Test
	void testIsValidXML_ValidXML() throws Exception {
		// Arrange: Create a valid XML string
		String validXml = "<?xml version=\"1.0\"?><root><element>value</element></root>";

		// Mock MultipartFile
		MultipartFile mockFile = mock(MultipartFile.class);
		when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(validXml.getBytes()));

		// Act & Assert: Call the method and assert no exception is thrown
		assertDoesNotThrow(() -> FileUtils.isValidXML(mockFile));
	}

	@Test
	void testIsValidXML_InvalidXML() throws Exception {
		// Arrange: Create an invalid XML string (missing closing tag)
		String invalidXml = "<?xml version=\"1.0\"?><root><element>value</element>";

		// Mock MultipartFile
		MultipartFile mockFile = mock(MultipartFile.class);
		when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(invalidXml.getBytes()));

		// Act & Assert: Expect a FileCreationException to be thrown
		assertThrows(FileCreationException.class, () -> FileUtils.isValidXML(mockFile));
	}

}
