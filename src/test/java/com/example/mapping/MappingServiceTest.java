package com.example.mapping;

import com.example.mapping.model.MappingResult;
import com.example.mapping.service.MappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MappingServiceTest {

	@Autowired
	private MappingService mappingService;

	@Test
	public void testPerformMappingWithValidFiles() throws IOException {
		String mappingContent = loadResourceFile("test_mapping.obda");
		String similarityContent = loadResourceFile("test_similarity");
		double threshold = 0.5;

		MockMultipartFile mappingFile = new MockMultipartFile("mappingFile", "test_mapping.obda", "text/plain", mappingContent.getBytes());
		MockMultipartFile similarityFile = new MockMultipartFile("similarityFile", "test_similarity", "text/plain", similarityContent.getBytes());

		try {
			MappingResult result = mappingService.performMapping(mappingFile, similarityFile, threshold);
			assertNotNull(result);
			assertTrue(result.getMappingLines().size() > 0);
			assertEquals(3, result.getNewMappings().size());
			assertThat(result.getUpdatedMappings()).isNotEmpty();
		} catch (IOException e) {
			fail("An exception should not have been thrown for valid files.");
		}
	}

	@Test
	public void testPerformMappingWithInvalidMappingFile() {
		MockMultipartFile mappingFile = new MockMultipartFile("mappingFile", "invalid_mapping.obda", "text/plain", "invalid mapping content".getBytes());
		MockMultipartFile similarityFile = new MockMultipartFile("similarityFile", "test_similarity", "text/plain", "valid similarity content".getBytes());
		double threshold = 0.5;

		assertThrows(IllegalArgumentException.class, () -> {
			mappingService.performMapping(mappingFile, similarityFile, threshold);
		});
	}

	@Test
	public void testPerformMappingWithInvalidSimilarityFile() throws IOException {
		String mappingContent = loadResourceFile("test_mapping.obda");
		double threshold = 0.5;

		MockMultipartFile mappingFile = new MockMultipartFile("mappingFile", "test_mapping.obda", "text/plain", mappingContent.getBytes());
		MockMultipartFile similarityFile = new MockMultipartFile("similarityFile", "invalid_similarity", "text/plain", "invalid similarity content".getBytes());

		try {
			MappingResult result = mappingService.performMapping(mappingFile, similarityFile, threshold);

			assertNotNull(result);
			assertTrue(result.getNewMappings().isEmpty());
		} catch (IOException e) {
			fail("An exception should not have been thrown for valid files.");
		}

	}

	@Test
	public void testPerformMappingWithZeroThreshold() throws IOException {
		String mappingContent = loadResourceFile("test_mapping.obda");
		String similarityContent = loadResourceFile("test_similarity");
		double threshold = 0.0;

		MockMultipartFile mappingFile = new MockMultipartFile("mappingFile", "test_mapping.obda", "text/plain", mappingContent.getBytes());
		MockMultipartFile similarityFile = new MockMultipartFile("similarityFile", "test_similarity", "text/plain", similarityContent.getBytes());

		try {
			MappingResult result = mappingService.performMapping(mappingFile, similarityFile, threshold);

			assertNotNull(result);
			System.out.println(result.getNewMappings());
			assertFalse(result.getNewMappings().isEmpty());

		} catch (IOException e) {
			fail("An exception should not have been thrown for valid files.");
		}
	}

	private String loadResourceFile(String resourceName) throws IOException {
		Path resourcePath = Paths.get("src", "test", "resources", resourceName);
		return Files.readString(resourcePath);
	}

}
