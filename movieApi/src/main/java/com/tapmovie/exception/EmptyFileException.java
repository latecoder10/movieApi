package com.tapmovie.exception;

import java.io.IOException;

/**
 * Custom exception to be thrown when an operation attempts to process an empty file.
 * This class extends the IOException to indicate that an I/O error has occurred
 * specifically related to file handling.
 */
public class EmptyFileException extends IOException {
    
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new EmptyFileException with the specified detail message.
     *
     * @param message the detail message which is saved for later retrieval by the {@link #getMessage()} method.
     */
    public EmptyFileException(String message) {
        super(message);
    }
}
