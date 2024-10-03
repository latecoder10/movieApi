package com.tapmovie.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tapmovie.authentication.repositories.UserRepository;

/**
 * Configuration class for setting up Spring Security beans in the application.
 * This class provides the necessary components for user authentication and 
 * password encoding, integrating with the user data repository.
 */
@Configuration
public class ApplicationConfig {

    private final UserRepository userRepository;

    /**
     * Constructor for injecting the UserRepository dependency.
     *
     * @param userRepository the repository used to access user data.
     */
    public ApplicationConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a UserDetailsService bean that loads user-specific data.
     * This service retrieves user details from the database based on the username.
     * 
     * @return a UserDetailsService implementation that throws a UsernameNotFoundException 
     * if the user with the specified username is not found in the repository.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    /**
     * Provides an AuthenticationProvider bean that uses DaoAuthenticationProvider.
     * This provider integrates the UserDetailsService and password encoder to handle
     * user authentication.
     * 
     * @return an AuthenticationProvider configured with the UserDetailsService and PasswordEncoder.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Creates an AuthenticationManager bean, which is responsible for managing 
     * authentication operations in the application.
     * 
     * @param config the AuthenticationConfiguration to retrieve the AuthenticationManager.
     * @return an AuthenticationManager instance.
     * @throws Exception if there is an issue retrieving the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Provides a PasswordEncoder bean for encoding passwords using BCrypt.
     * This encoder is used to securely hash passwords before storing them 
     * in the database and for comparing hashed passwords during authentication.
     * 
     * @return a PasswordEncoder instance configured to use BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
