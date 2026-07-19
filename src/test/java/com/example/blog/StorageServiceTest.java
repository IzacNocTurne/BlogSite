package com.example.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StorageServiceTest {

    @Autowired
    private StorageService storageService;

    @Test
    public void testIsGcsEnabledDefault() {
        // By default, since bucket-name is empty in test application.properties, GCS should be disabled
        assertFalse(storageService.isGcsEnabled(), "GCS should be disabled by default");
    }

    @Test
    public void testLocalFallbackStoreAndLoad() throws IOException {
        String filename = "test-file.txt";
        byte[] content = "Hello Local Fallback Storage!".getBytes();
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                filename,
                "text/plain",
                content
        );

        // Store file
        String storedFilename = storageService.store(multipartFile);
        assertNotNull(storedFilename);
        assertTrue(storedFilename.endsWith(filename));

        // Load as resource
        Resource resource = storageService.loadAsResource(storedFilename);
        assertNotNull(resource);
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());

        // Read content
        byte[] retrievedContent = resource.getInputStream().readAllBytes();
        assertArrayEquals(content, retrievedContent);
    }
}
