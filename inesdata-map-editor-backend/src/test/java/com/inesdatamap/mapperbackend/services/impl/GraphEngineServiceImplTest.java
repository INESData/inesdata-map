package com.inesdatamap.mapperbackend.services.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.inesdatamap.mapperbackend.exceptions.GraphEngineException;
import com.inesdatamap.mapperbackend.utils.ProcessBuilderFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GraphEngineServiceImplTest {

	@Mock
	private ProcessBuilderFactory processBuilderFactory;

	@Mock
	private ProcessBuilder processBuilder;

	@Mock
	private Process process;

	@InjectMocks
	private GraphEngineServiceImpl graphEngineService;

	@Test
	void runTest() throws IOException, InterruptedException {

		List<String> expectedResult = List.of("Process", "Executed");

		InputStream stream = new ByteArrayInputStream("Process\nExecuted".getBytes());

		Path path = Paths.get("");

		try (MockedStatic<Files> mockFiles = mockStatic(Files.class)) {

			mockFiles.when(() -> Files.createFile(any())).thenReturn(path);
			mockFiles.when(() -> Files.createDirectories(any())).thenReturn(path);
			mockFiles.when(() -> Files.write(any(), any(byte[].class))).thenReturn(path);

			when(processBuilderFactory.createProcessBuilder(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
				anyString())).thenReturn(processBuilder);
			when(processBuilder.start()).thenReturn(process);
			when(process.getInputStream()).thenReturn(stream);
			when(process.waitFor()).thenReturn(0);

			List<String> result = graphEngineService.run("", "", "");

			assertEquals(expectedResult, result);
			mockFiles.verify(() -> Files.createFile(any()));
			mockFiles.verify(() -> Files.createDirectories(any()), times(2));
			mockFiles.verify(() -> Files.write(any(), any(byte[].class)));
		}
	}

	@Test
	void runThrowsGraphEngineExceptionOnExitCode() throws IOException, InterruptedException {

		Path path = Paths.get("");

		try (MockedStatic<Files> mockFiles = mockStatic(Files.class)) {

			mockFiles.when(() -> Files.createFile(any())).thenReturn(path);
			mockFiles.when(() -> Files.createDirectories(any())).thenReturn(path);
			mockFiles.when(() -> Files.write(any(), any(byte[].class))).thenReturn(path);

			when(processBuilderFactory.createProcessBuilder(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
				anyString())).thenReturn(processBuilder);
			when(processBuilder.start()).thenReturn(process);
			when(process.waitFor()).thenReturn(1);

			assertThrows(GraphEngineException.class, () -> {
				graphEngineService.run("", "", "");
			});
			mockFiles.verify(() -> Files.createFile(any()));
			mockFiles.verify(() -> Files.createDirectories(any()), times(2));
			mockFiles.verify(() -> Files.write(any(), any(byte[].class)));
		}

	}

	@Test
	void runThrowsGraphEngineExceptionOnIOException() throws IOException {

		Path path = Paths.get("");

		try (MockedStatic<Files> mockFiles = mockStatic(Files.class)) {

			mockFiles.when(() -> Files.createDirectories(any())).thenReturn(path);

			when(processBuilderFactory.createProcessBuilder(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
				anyString())).thenReturn(processBuilder);
			when(processBuilder.start()).thenThrow(IOException.class);

			assertThrows(GraphEngineException.class, () -> {
				graphEngineService.run("", "", "");
			});

			mockFiles.verify(() -> Files.createDirectories(any()));
		}
	}

	@Test
	void runThrowsGraphEngineExceptionOnInterruptedException() throws IOException, InterruptedException {

		Path path = Paths.get("");

		try (MockedStatic<Files> mockFiles = mockStatic(Files.class)) {
			mockFiles.when(() -> Files.createDirectories(any())).thenReturn(path);

			when(processBuilderFactory.createProcessBuilder(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
				anyString())).thenReturn(processBuilder);
			when(processBuilder.start()).thenReturn(process);
			when(process.waitFor()).thenThrow(InterruptedException.class);

			assertThrows(GraphEngineException.class, () -> {
				graphEngineService.run("", "", "");
			});

			mockFiles.verify(() -> Files.createDirectories(any()));
		}
	}

}
