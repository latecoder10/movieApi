package com.tapmovie.exception;

/**
 * Custom exception to be thrown when an operation attempts to create a file that already exists.
 * This class extends RuntimeException to indicate an error that can occur during the application's
 * runtime, typically indicating a programming error.
 */
public class FileAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new FileAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message which is saved for later retrieval by the {@link #getMessage()} method.
     */
    public FileAlreadyExistsException(String message) {
        super(message);
    }
}
