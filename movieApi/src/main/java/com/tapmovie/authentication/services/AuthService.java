package com.tapmovie.authentication.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tapmovie.authentication.entities.User;
import com.tapmovie.authentication.entities.UserRole;
import com.tapmovie.authentication.repositories.UserRepository;
import com.tapmovie.authentication.utils.AuthResponse;
import com.tapmovie.authentication.utils.LoginRequest;
import com.tapmovie.authentication.utils.RegisterRequest;

/**
 * The AuthService class provides authentication and registration functionalities
 * for users in the application.
 * It handles the creation of user accounts, user login, and token generation.
 */
@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructs an instance of AuthService.
     *
     * @param passwordEncoder         The password encoder for encoding user passwords.
     * @param userRepository          The repository for managing User entities.
     * @param jwtService              The service for generating and validating JWTs.
     * @param refreshTokenService     The service for managing refresh tokens.
     * @param authenticationManager    The authentication manager for handling user authentication.
     */
    public AuthService(PasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       JwtService jwtService,
                       RefreshTokenService refreshTokenService,
                       AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registers a new user in the application.
     *
     * @param registerRequest The request object containing user registration data.
     * @return An AuthResponse containing the access token and refresh token.
     */
    public AuthResponse register(RegisterRequest registerRequest) {
        // Create a new User entity from the registration request
        var user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER) // Set default user role
                .build();

        // Save the user to the database
        User savedUser = userRepository.save(user);
        
        // Generate access and refresh tokens for the registered user
        var accessToken = jwtService.generateToken(savedUser);
        var refreshToken = refreshTokenService.createRefreshToken(savedUser.getEmail());

        // Return the tokens in the AuthResponse
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }

    /**
     * Authenticates a user and generates tokens upon successful login.
     *
     * @param loginRequest The request object containing user login credentials.
     * @return An AuthResponse containing the access token and refresh token.
     * @throws UsernameNotFoundException if the user with the given email does not exist.
     */
    public AuthResponse login(LoginRequest loginRequest) {
        // Authenticate the user using the provided email and password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), 
                        loginRequest.getPassword())
        );

        // Retrieve the authenticated user from the repository
        var user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        // Generate access and refresh tokens for the logged-in user
        var accessToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(loginRequest.getEmail());

        // Return the tokens in the AuthResponse
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }
}
