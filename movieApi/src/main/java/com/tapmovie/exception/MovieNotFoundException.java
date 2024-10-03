package com.tapmovie.exception;

/**
 * Exception thrown when a movie is not found in the system.
 * This extends RuntimeException, making it an unchecked exception.
 */
public class MovieNotFoundException extends RuntimeException {

    /**
     * Unique identifier for serialization.
     */
    private static final long serialVersionUID = 5292204889135491572L;

    /**
     * Constructs a new MovieNotFoundException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public MovieNotFoundException(String message) {
        super(message);
    }
}
