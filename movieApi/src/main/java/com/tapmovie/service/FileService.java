package com.tapmovie.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

/**
 * Interface for file handling services.
 * Defines methods for uploading files and retrieving file resources.
 */
public interface FileService {

    /**
     * Uploads a file to the specified path.
     *
     * @param path The directory path where the file will be uploaded.
     * @param file The MultipartFile object representing the file to be uploaded.
     * @return The name of the uploaded file.
     * @throws IOException If an I/O error occurs during file upload.
     */
    String uploadFile(String path, MultipartFile file) throws IOException;

    /**
     * Retrieves a file resource as an InputStream.
     *
     * @param path The directory path where the file is located.
     * @param fileName The name of the file to be retrieved.
     * @return An InputStream for reading the file content.
     * @throws FileNotFoundException If the file does not exist at the specified path.
     */
    InputStream getResourceFile(String path, String fileName) throws FileNotFoundException;
}
