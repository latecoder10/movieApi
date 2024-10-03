package com.tapmovie.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tapmovie.dto.MovieDto;
import com.tapmovie.dto.MoviePageResponse;
import com.tapmovie.service.MovieService;
import com.tapmovie.util.AppConstants;

/**
 * The MovieController class handles HTTP requests related to movie management.
 * It provides endpoints for adding, retrieving, updating, deleting, and paginating
 * through movie records. Access to certain methods is restricted based on user roles.
 */
@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {

    private final MovieService movieService;

    /**
     * Constructs a MovieController with the specified MovieService.
     *
     * @param movieService the service responsible for movie operations
     */
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * Adds a new movie to the system.
     *
     * @param file      the movie file (e.g., poster or trailer)
     * @param movieDto  a JSON representation of the movie details
     * @return a ResponseEntity containing the added MovieDto and HTTP status CREATED
     * @throws IOException if there's an error reading the file or JSON
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> addMovieHandler(@RequestPart MultipartFile file, @RequestPart String movieDto)
            throws IOException {
        MovieDto convertToMovieDto = convertToMovieDto(movieDto);
        return new ResponseEntity<>(movieService.addMovie(convertToMovieDto, file), HttpStatus.CREATED);
    }

    /**
     * Retrieves a specific movie by its ID.
     *
     * @param movieId the ID of the movie to retrieve
     * @return a ResponseEntity containing the MovieDto and HTTP status OK
     */
    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDto> getMovieHandler(@PathVariable Integer movieId) {
        return ResponseEntity.ok(movieService.getMovie(movieId));
    }

    /**
     * Retrieves all movies in the system.
     *
     * @return a ResponseEntity containing a list of MovieDto and HTTP status OK
     */
    @GetMapping("/all")
    public ResponseEntity<List<MovieDto>> getAllMovieHandler() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    /**
     * Updates an existing movie.
     *
     * @param movieId         the ID of the movie to update
     * @param file            the new movie file (optional)
     * @param movieDtoObject  a JSON representation of the updated movie details
     * @return a ResponseEntity containing the updated MovieDto and HTTP status OK
     * @throws IOException if there's an error reading the file or JSON
     */
    @PutMapping("/update/{movieId}")
    public ResponseEntity<MovieDto> updateMovieHandler(@PathVariable Integer movieId, @RequestPart MultipartFile file,
            @RequestPart String movieDtoObject) throws IOException {

        if (file.isEmpty())
            file = null; // If no file is provided, set to null
        MovieDto movieDto = convertToMovieDto(movieDtoObject);
        return ResponseEntity.ok(movieService.updateMovie(movieId, movieDto, file));
    }

    /**
     * Deletes a movie by its ID.
     *
     * @param movieId the ID of the movie to delete
     * @return a ResponseEntity containing a confirmation message and HTTP status OK
     * @throws IOException if there's an error during deletion
     */
    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<String> deleteMovieHandler(@PathVariable Integer movieId) throws IOException {
        return ResponseEntity.ok(movieService.deleteMovie(movieId));
    }

    /**
     * Retrieves movies with pagination.
     *
     * @param page the page number (default is PAGE_NUMBER)
     * @param size the number of movies per page (default is PAGE_SIZE)
     * @return a ResponseEntity containing a MoviePageResponse with paginated movies and HTTP status OK
     */
    @GetMapping("/allMoviesPage")
    public ResponseEntity<MoviePageResponse> getMoviesWithPagination(
            @RequestParam(defaultValue = "" + AppConstants.PAGE_NUMBER, required = false) Integer page,
            @RequestParam(defaultValue = "" + AppConstants.PAGE_SIZE, required = false) Integer size) {
        MoviePageResponse response = movieService.getAllMoviesWithPagination(page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves movies with pagination and sorting.
     *
     * @param page    the page number (default is PAGE_NUMBER)
     * @param size    the number of movies per page (default is PAGE_SIZE)
     * @param sortBy  the field to sort by (default is SORT_BY)
     * @param sortDir the direction of sorting (default is SORT_DIR)
     * @return a ResponseEntity containing a MoviePageResponse with sorted and paginated movies and HTTP status OK
     */
    @GetMapping("/allMoviesPageSort")
    public ResponseEntity<MoviePageResponse> getMoviesWithPaginationAndSorting(
            @RequestParam(defaultValue = "" + AppConstants.PAGE_NUMBER, required = false) Integer page,
            @RequestParam(defaultValue = "" + AppConstants.PAGE_SIZE, required = false) Integer size,
            @RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
        MoviePageResponse response = movieService.getAllMoviesWithPaginationAndSorting(page, size, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }

    /**
     * Converts a JSON string representation of MovieDto to a MovieDto object.
     *
     * @param movieDtoObj the JSON string of the MovieDto
     * @return the converted MovieDto object
     * @throws JsonMappingException if the JSON cannot be mapped to MovieDto
     * @throws JsonProcessingException if there are issues processing the JSON
     */
    private MovieDto convertToMovieDto(String movieDtoObj) throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(movieDtoObj, MovieDto.class);
    }
}
