package com.tapmovie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for the application, utilizing Spring's @RestControllerAdvice.
 * This class intercepts exceptions thrown by controllers and provides a unified response format.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles MovieNotFoundException and returns a ProblemDetail response with HTTP 404 status.
     *
     * @param ex the exception thrown when a movie is not found
     * @return a ProblemDetail containing the error status and message
     */
    @ExceptionHandler(MovieNotFoundException.class)
    public ProblemDetail handleMovieNotFoundException(MovieNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles FileAlreadyExistsException and returns a ProblemDetail response with HTTP 400 status.
     *
     * @param ex the exception thrown when a file already exists
     * @return a ProblemDetail containing the error status and message
     */
    @ExceptionHandler(FileAlreadyExistsException.class)
    public ProblemDetail handleFileAlreadyExistsException(FileAlreadyExistsException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Handles EmptyFileException and returns a ProblemDetail response with HTTP 400 status.
     *
     * @param ex the exception thrown when an empty file is encountered
     * @return a ProblemDetail containing the error status and message
     */
    @ExceptionHandler(EmptyFileException.class)
    public ProblemDetail handleEmptyFileException(EmptyFileException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Handles InvalidSortFieldException and returns a ProblemDetail response with HTTP 400 status.
     *
     * @param ex the exception thrown when an invalid sort field is provided
     * @return a ProblemDetail containing the error status and message
     */
    @ExceptionHandler(InvalidSortFieldException.class)
    public ProblemDetail handleInvalidSortFieldException(InvalidSortFieldException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
