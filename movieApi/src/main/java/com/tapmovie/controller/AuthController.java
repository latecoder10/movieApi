package com.tapmovie.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tapmovie.authentication.entities.RefreshToken;
import com.tapmovie.authentication.entities.User;
import com.tapmovie.authentication.services.AuthService;
import com.tapmovie.authentication.services.JwtService;
import com.tapmovie.authentication.services.RefreshTokenService;
import com.tapmovie.authentication.utils.AuthResponse;
import com.tapmovie.authentication.utils.LoginRequest;
import com.tapmovie.authentication.utils.RefreshTokenRequest;
import com.tapmovie.authentication.utils.RegisterRequest;

/**
 * The AuthController class is responsible for handling authentication-related requests 
 * in the application. It provides endpoints for user registration, login, and refreshing 
 * authentication tokens. This controller communicates with the authentication services 
 * to process requests and return appropriate responses.
 */
@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    /**
     * Constructs an instance of AuthController with the specified services.
     *
     * @param authService the authentication service to handle user authentication operations
     * @param refreshTokenService the service for managing refresh tokens
     * @param jwtService the service for handling JWT token generation
     */
    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }
    
    /**
     * Handles user registration requests.
     * 
     * @param registerRequest the request body containing user registration details
     * @return a ResponseEntity containing the authentication response with access and refresh tokens
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }
    
    /**
     * Handles user login requests.
     * 
     * @param loginRequest the request body containing user login credentials
     * @return a ResponseEntity containing the authentication response with access and refresh tokens
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
    
    /**
     * Handles refresh token requests.
     * 
     * @param refreshTokenRequest the request body containing the refresh token
     * @return a ResponseEntity containing the authentication response with a new access token and the existing refresh token
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
        User user = refreshToken.getUser();
        
        String accessToken = jwtService.generateToken(user);
        
        return ResponseEntity.ok(AuthResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken.getRefreshToken())
                            .build());
    }
}
