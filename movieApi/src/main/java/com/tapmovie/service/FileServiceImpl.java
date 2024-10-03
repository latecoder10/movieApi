package com.tapmovie.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implementation of the FileService interface.
 * Provides methods for uploading files and retrieving file resources.
 */
@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {
        // Define allowed MIME types for uploaded files
        List<String> allowedTypes = List.of("image/png", "image/jpeg", "image/gif");

        // Check if the file type is allowed
        String contentType = file.getContentType();
        if (!allowedTypes.contains(contentType)) {
            throw new IllegalArgumentException("Invalid file type: " + contentType);
        }

        // Generate a unique filename using UUID to avoid name collisions
        String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String filePath = path + File.separator + uniqueFileName;

        // Create a File object for the target directory
        File directory = new File(path);
        if (!directory.exists()) {
            // Create the directory if it doesn't exist
            directory.mkdirs();
        }

        // Attempt to copy the file to the specified path
        try {
            Files.copy(file.getInputStream(), Paths.get(filePath));
        } catch (IOException e) {
            throw new IOException("Could not save file: " + uniqueFileName, e);
        }

        // Return the unique filename for reference
        return uniqueFileName;
    }

    @Override
    public InputStream getResourceFile(String path, String fileName) throws FileNotFoundException {
        // Construct the full file path
        String filePath = path + File.separator + fileName;

        // Return an InputStream for reading the specified file
        return new FileInputStream(filePath);
    }
}
