package com.tapmovie.controller;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.tapmovie.service.FileService;
import jakarta.servlet.http.HttpServletResponse;

/**
 * The FileController class is responsible for handling file upload and retrieval requests.
 * It provides endpoints for uploading files to the server and serving those files based on
 * their names. This controller leverages the FileService to perform actual file operations.
 */
@RestController
@RequestMapping("/file/")
public class FileController {
    private final FileService fileService;

    // Path to store uploaded files, injected from application properties
    @Value("${project.poster}")
    private String path;

    /**
     * Constructs a FileController with the specified FileService.
     *
     * @param fileService the service to handle file operations
     */
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Handles file upload requests.
     *
     * @param file the multipart file to be uploaded
     * @return a ResponseEntity containing a success message with the uploaded file name
     * @throws IOException if an error occurs during file upload
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFileHandler(@RequestPart("file") MultipartFile file) throws IOException {
        String uploadedFileName = fileService.uploadFile(path, file);
        return ResponseEntity.ok("File uploaded: " + uploadedFileName);
    }

    /**
     * Handles requests to serve a file.
     *
     * @param fileName the name of the file to be retrieved
     * @param response the HTTP response object to write the file to
     * @throws IOException if an error occurs while retrieving the file
     */
    @GetMapping("/{fileName}")
    public void serveFileHandler(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        InputStream resourceFile = fileService.getResourceFile(path, fileName);
        
        // Determine content type based on file extension
        String extension = getFileExtension(fileName);
        MediaType mediaType = getMediaType(extension);
        
        // Set the response content type and write the file to the response output stream
        response.setContentType(mediaType != null ? mediaType.toString() : MediaType.APPLICATION_OCTET_STREAM_VALUE);
        StreamUtils.copy(resourceFile, response.getOutputStream());
    }

    // Helper method to get the file extension from a filename
    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        return (lastIndex == -1) ? "" : fileName.substring(lastIndex + 1).toLowerCase();
    }

    // Helper method to map file extensions to MediaType
    private MediaType getMediaType(String extension) {
        switch (extension) {
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "png":
                return MediaType.IMAGE_PNG;
            case "gif":
                return MediaType.IMAGE_GIF;
            default:
                return null; // Unknown type
        }
    }
}
