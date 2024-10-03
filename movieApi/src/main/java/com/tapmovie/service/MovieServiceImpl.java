package com.tapmovie.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tapmovie.dto.MovieDto;
import com.tapmovie.dto.MoviePageResponse;
import com.tapmovie.entity.Movie;
import com.tapmovie.exception.EmptyFileException;
import com.tapmovie.exception.InvalidSortFieldException;
import com.tapmovie.exception.MovieNotFoundException;
import com.tapmovie.repositories.MovieRepository;

/**
 * Implementation of the MovieService interface.
 * This service handles operations related to movie management, including
 * adding, retrieving, updating, and deleting movies, as well as file handling.
 */
@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final FileService fileService;

    // Injected configuration properties
    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
    }

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
        // Validate the uploaded file
        if (file == null || file.isEmpty()) {
            throw new EmptyFileException("File cannot be null or empty");
        }

        // 1. Upload the file and get the uploaded file name
        String uploadedFileName = fileService.uploadFile(path, file);

        // 2. Set the uploaded file name in the DTO
        movieDto.setPoster(uploadedFileName);

        // 3. Map DTO to Movie entity
        Movie movie = new Movie();
        movie.setTitle(movieDto.getTitle());
        movie.setDirector(movieDto.getDirector());
        movie.setStudio(movieDto.getStudio());
        movie.setMovieCast(movieDto.getMovieCast());
        movie.setReleaseYear(movieDto.getReleaseYear());
        movie.setPoster(movieDto.getPoster());

        // 4. Save the movie entity to the repository
        Movie savedMovie = movieRepository.save(movie);

        // 5. Generate the poster URL
        String posterUrl = baseUrl + "/file/" + savedMovie.getPoster();

        // 6. Return the Movie DTO with all relevant information
        return new MovieDto(savedMovie.getMovieId(), savedMovie.getTitle(), savedMovie.getDirector(),
                savedMovie.getStudio(), savedMovie.getMovieCast(), savedMovie.getReleaseYear(), savedMovie.getPoster(),
                posterUrl);
    }

    @Override
    public MovieDto getMovie(Integer movieId) {
        // Retrieve the movie by ID, throwing an exception if not found
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id = " + movieId));
        
        // Generate the poster URL
        String posterUrl = baseUrl + "/file/" + movie.getPoster();

        // Return the Movie DTO
        return new MovieDto(movie.getMovieId(), movie.getTitle(), movie.getDirector(), movie.getStudio(),
                movie.getMovieCast(), movie.getReleaseYear(), movie.getPoster(), posterUrl);
    }

    @Override
    public List<MovieDto> getAllMovies() {
        List<MovieDto> movieDtos = new ArrayList<>();
        // Fetch all movies from the repository
        List<Movie> movies = movieRepository.findAll();

        // Convert each Movie entity to Movie DTO
        for (Movie movie : movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            movieDtos.add(new MovieDto(movie.getMovieId(), movie.getTitle(), movie.getDirector(), movie.getStudio(),
                    movie.getMovieCast(), movie.getReleaseYear(), movie.getPoster(), posterUrl));
        }
        return movieDtos;
    }

    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException {
        // Retrieve the movie by ID
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id = " + movieId));

        String fileName = movie.getPoster(); // Store the current poster filename
        if (file != null) {
            // If a new file is uploaded, delete the existing file
            Files.deleteIfExists(Paths.get(path + File.separator + fileName));
            // Upload the new file and get the new filename
            fileName = fileService.uploadFile(path, file);
        }

        // Update the Movie entity with new values
        movie.setTitle(movieDto.getTitle());
        movie.setDirector(movieDto.getDirector());
        movie.setStudio(movieDto.getStudio());
        movie.setMovieCast(movieDto.getMovieCast());
        movie.setReleaseYear(movieDto.getReleaseYear());
        movie.setPoster(file != null ? fileName : movie.getPoster()); // Use new filename if uploaded

        // Save the updated movie entity
        Movie updatedMovie = movieRepository.save(movie);
        String posterUrl = baseUrl + "/file/" + updatedMovie.getPoster();

        // Return the updated Movie DTO
        return new MovieDto(updatedMovie.getMovieId(), updatedMovie.getTitle(), updatedMovie.getDirector(),
                updatedMovie.getStudio(), updatedMovie.getMovieCast(), updatedMovie.getReleaseYear(),
                updatedMovie.getPoster(), posterUrl);
    }

    @Override
    public String deleteMovie(Integer movieId) throws IOException {
        // Retrieve the movie by ID
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id = " + movieId));

        // Delete the file associated with the movie
        Files.deleteIfExists(Paths.get(path + File.separator + movie.getPoster()));

        // Delete the movie entity from the repository
        movieRepository.delete(movie);
        return "Movie deleted with id = " + movie.getMovieId();
    }

    @Override
    public MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) {
        // Create a pageable object for pagination
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<MovieDto> movieDtos = new ArrayList<>();
        List<Movie> movies = moviePages.getContent();

        // Convert each Movie entity to Movie DTO
        for (Movie movie : movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            movieDtos.add(new MovieDto(movie.getMovieId(), movie.getTitle(), movie.getDirector(), movie.getStudio(),
                    movie.getMovieCast(), movie.getReleaseYear(), movie.getPoster(), posterUrl));
        }
        // Return the paginated response
        return new MoviePageResponse(movieDtos, pageNumber, pageSize,
                moviePages.getTotalElements(), moviePages.getTotalPages(), moviePages.isLast());
    }

    @Override
    public MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy,
            String direction) {
        // Define allowed sort fields for validation
        List<String> allowedSortFields = Arrays.asList("title", "director", "studio", "releaseYear");

        // Validate the sort field
        if (!allowedSortFields.contains(sortBy)) {
            throw new InvalidSortFieldException("Invalid sort field: " + sortBy + ". Allowed fields are: " + allowedSortFields);
        }

        // Create sorting and pageable objects
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        List<MovieDto> movieDtos = new ArrayList<>();
        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movies = moviePages.getContent();

        // Convert each Movie entity to Movie DTO
        for (Movie movie : movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            movieDtos.add(new MovieDto(movie.getMovieId(), movie.getTitle(), movie.getDirector(), movie.getStudio(),
                    movie.getMovieCast(), movie.getReleaseYear(), movie.getPoster(), posterUrl));
        }
        // Return the paginated and sorted response
        return new MoviePageResponse(movieDtos, pageNumber, pageSize,
                moviePages.getTotalElements(), moviePages.getTotalPages(), moviePages.isLast());
    }
}
