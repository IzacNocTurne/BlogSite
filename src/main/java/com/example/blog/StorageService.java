package com.example.blog;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class StorageService implements InitializingBean {

    private final Path rootLocation = Paths.get("uploads");

    @Value("${gcp.storage.bucket-name:}")
    private String bucketName;

    @Value("${gcp.storage.project-id:}")
    private String projectId;

    @Value("${gcp.storage.credentials-path:}")
    private String credentialsPath;

    private Storage storage;

    public StorageService() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (isGcsEnabled()) {
            try {
                StorageOptions.Builder builder = StorageOptions.newBuilder();
                if (StringUtils.hasText(credentialsPath)) {
                    GoogleCredentials credentials;
                    if (credentialsPath.startsWith("classpath:")) {
                        try (InputStream is = new ClassPathResource(credentialsPath.substring(10)).getInputStream()) {
                            credentials = GoogleCredentials.fromStream(is);
                        }
                    } else {
                        try (InputStream is = new FileInputStream(credentialsPath)) {
                            credentials = GoogleCredentials.fromStream(is);
                        }
                    }
                    builder.setCredentials(credentials);
                }
                if (StringUtils.hasText(projectId)) {
                    builder.setProjectId(projectId);
                }
                this.storage = builder.build().getService();
            } catch (IOException e) {
                throw new RuntimeException("Failed to initialize Google Cloud Storage", e);
            }
        }
    }

    public boolean isGcsEnabled() {
        return StringUtils.hasText(bucketName);
    }

    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new RuntimeException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;

            if (isGcsEnabled()) {
                BlobId blobId = BlobId.of(bucketName, uniqueFilename);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                        .setContentType(file.getContentType())
                        .build();
                storage.create(blobInfo, file.getBytes());
                return uniqueFilename;
            } else {
                Path destinationFile = this.rootLocation.resolve(
                        Paths.get(uniqueFilename))
                        .normalize().toAbsolutePath();
                if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                    // This is a security check
                    throw new RuntimeException(
                            "Cannot store file outside current directory.");
                }
                Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
                return uniqueFilename;
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to store file " + filename, e);
        }
    }

    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    public Resource loadAsResource(String filename) {
        if (isGcsEnabled()) {
            try {
                Blob blob = storage.get(BlobId.of(bucketName, filename));
                if (blob == null || !blob.exists()) {
                    throw new RuntimeException("Could not read file from GCS: " + filename);
                }
                byte[] content = blob.getContent();
                return new ByteArrayResource(content) {
                    @Override
                    public String getFilename() {
                        return filename;
                    }
                };
            } catch (Exception e) {
                throw new RuntimeException("Could not read file from GCS: " + filename, e);
            }
        } else {
            try {
                Path file = load(filename);
                Resource resource = new UrlResource(file.toUri());
                if (resource.exists() || resource.isReadable()) {
                    return resource;
                }
                else {
                    throw new RuntimeException(
                            "Could not read file: " + filename);
                }
            }
            catch (MalformedURLException e) {
                throw new RuntimeException("Could not read file: " + filename, e);
            }
        }
    }
}
