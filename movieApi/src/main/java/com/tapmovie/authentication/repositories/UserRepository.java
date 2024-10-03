package com.tapmovie.authentication.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.tapmovie.authentication.entities.User;

import jakarta.transaction.Transactional;

/**
 * Repository interface for managing {@link User} entities.
 * 
 * This interface extends {@link JpaRepository}, providing CRUD operations and
 * custom query methods for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Retrieves a {@link User} entity by its email.
     *
     * This method follows the Spring Data JPA naming conventions, allowing 
     * the framework to automatically generate the necessary SQL query to 
     * find a user based on the specified email.
     *
     * @param email the email of the user to be retrieved.
     *              This parameter must not be null.
     * @return an {@link Optional<User>} containing the found user or 
     *         an empty Optional if no user is found with the given email.
     *
     * @throws IllegalArgumentException if the email is null.
     * 
     * Usage:
     * <pre>
     * Optional<User> userOptional = userRepository.findByEmail("example@example.com");
     * userOptional.ifPresent(user -> {
     *     // Process the found user
     * });
     * </pre>
     *
     * This method is particularly useful in scenarios where the email 
     * must be verified during authentication, allowing for safe handling of 
     * potentially absent users without risking a NullPointerException.
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Updates the password of a user identified by their email.
     *
     * This method uses a custom query to perform the update operation.
     * 
     * @param email the email of the user whose password needs to be updated.
     *              This parameter must not be null.
     * @param password the new password to set for the user.
     * 
     * @throws IllegalArgumentException if the email or password is null.
     */
    @Transactional
    @Modifying
    @Query("update User u set u.password= ?2 where u.email= ?1")
    void updatePassword(String email, String password);
}
