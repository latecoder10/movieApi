package com.tapmovie.dto;

import java.util.List;

/**
 * Represents a response containing a list of movies.
 * 
 * This record is used to structure the data returned by API endpoints 
 * that deal with paginated movie data. Records are a special type of 
 * class in Java introduced in Java 14, providing a concise and 
 * immutable way to create data carriers. 
 * 
 * Key Features:
 * 
 * 1. **Automatic Constructor**: Generates a constructor that accepts 
 *    parameters for all fields (e.g., a List<MovieDto>).
 * 
 * 2. **Getter Methods**: Provides public accessor methods for each 
 *    field, allowing easy retrieval of data (e.g., movieDto()).
 * 
 * 3. **Equality and Hashing**: Implements equals() and hashCode() 
 *    methods based on field values, facilitating accurate comparisons 
 *    and proper use in collections.
 * 
 * 4. **String Representation**: Generates a toString() method that 
 *    gives a string representation of the record, including field 
 *    names and values, which aids in debugging and logging.
 * 
 * 5. **Immutability**: All fields are final, ensuring that the data 
 *    cannot be modified after the record is created, enhancing safety 
 *    in multi-threaded applications.
 */
public record MoviePageResponse(List<MovieDto> movieDtos,Integer pageNumber,Integer pageSize,long totalElements,int totalPages,boolean isLast) {
	
}

