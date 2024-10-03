package com.tapmovie.authentication.services;

import java.time.Instant;
import java.util.UUID;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tapmovie.authentication.entities.RefreshToken;
import com.tapmovie.authentication.entities.User;
import com.tapmovie.authentication.repositories.RefreshTokenRepository;
import com.tapmovie.authentication.repositories.UserRepository;

/**
 * The RefreshTokenService class manages the creation and validation of refresh tokens
 * for user authentication. It interacts with user and refresh token repositories
 * to handle token-related operations.
 */
@Service
public class RefreshTokenService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Constructs an instance of RefreshTokenService.
     *
     * @param userRepository          The repository for managing User entities.
     * @param refreshTokenRepository  The repository for managing RefreshToken entities.
     */
    public RefreshTokenService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * Creates a new refresh token for the specified user.
     *
     * @param username The email of the user for whom the refresh token is to be created.
     * @return The newly created RefreshToken.
     * @throws UsernameNotFoundException if the user with the specified email does not exist.
     */
    public RefreshToken createRefreshToken(String username) {
        // Retrieve the user based on the provided email
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        RefreshToken refreshToken = user.getRefreshToken();

        // Check if a refresh token already exists for the user
        if (refreshToken == null) {
            long refreshTokenValidity = 30 * 1000; // Set validity period (30 seconds)
            refreshToken = new RefreshToken.Builder()
                    .setRefreshToken(UUID.randomUUID().toString()) // Generate a new token
                    .setExpirationTime((int) Instant.now().plusMillis(refreshTokenValidity).getEpochSecond()) // Set expiration time
                    .setUser(user) // Associate with the user
                    .build();

            // Save the new refresh token to the repository
            refreshTokenRepository.save(refreshToken);
        }

        return refreshToken; // Return the refresh token
    }

    /**
     * Verifies the validity of a refresh token.
     *
     * @param refreshToken The refresh token to be verified.
     * @return The verified RefreshToken entity.
     * @throws RuntimeException if the refresh token is not found or has expired.
     */
    public RefreshToken verifyRefreshToken(String refreshToken) {
        // Retrieve the refresh token from the repository
        RefreshToken refToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found!"));

        // Check if the token has expired
        if (refToken.getExpirationTime().compareTo((int) Instant.now().getEpochSecond()) < 0) {
            refreshTokenRepository.delete(refToken); // Remove expired token from the repository
            throw new RuntimeException("Refresh token expired");
        }

        return refToken; // Return the valid refresh token
    }
}
