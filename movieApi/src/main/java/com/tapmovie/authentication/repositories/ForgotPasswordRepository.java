package com.tapmovie.authentication.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tapmovie.authentication.entities.ForgotPassword;
import com.tapmovie.authentication.entities.User;

/**
 * Repository interface for managing ForgotPassword entities.
 * Provides methods for CRUD operations and custom queries.
 */
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {
    
    /**
     * Finds a ForgotPassword record by OTP and associated user.
     *
     * @param otp the One Time Password to search for
     * @param user the User associated with the ForgotPassword record
     * @return an Optional containing the ForgotPassword entity if found, otherwise empty
     */
    @Query("SELECT fp FROM ForgotPassword fp WHERE fp.otp = ?1 AND fp.user = ?2")
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);
    
    /**
     * Finds a ForgotPassword record by associated user.
     *
     * @param user the User associated with the ForgotPassword record
     * @return an Optional containing the ForgotPassword entity if found, otherwise empty
     */
    @Query("SELECT fp FROM ForgotPassword fp WHERE fp.user = ?1")
    Optional<ForgotPassword> findByUser(User user);
}
