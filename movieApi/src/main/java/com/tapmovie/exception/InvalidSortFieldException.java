package com.tapmovie.exception;

/**
 * Exception thrown when an invalid sort field is provided for sorting operations.
 * This extends RuntimeException, allowing it to be unchecked.
 */
public class InvalidSortFieldException extends RuntimeException {
    
    /**
     * Constructs a new InvalidSortFieldException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public InvalidSortFieldException(String message) {
        super(message);
    }
}
