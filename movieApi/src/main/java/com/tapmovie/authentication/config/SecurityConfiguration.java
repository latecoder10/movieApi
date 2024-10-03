package com.tapmovie.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tapmovie.authentication.services.AuthenticationFilterService;

/**
 * SecurityConfiguration is responsible for configuring the security settings for the application.
 * It specifies how requests are authenticated and authorized, as well as session management.
 */
@Configuration
@EnableWebSecurity // Enables Spring Security's web security support
@EnableMethodSecurity // Enables method-level security annotations
public class SecurityConfiguration {

    private final AuthenticationFilterService authenticationFilterService;
    private final AuthenticationProvider authenticationProvider;

    /**
     * Constructor for SecurityConfiguration.
     * 
     * @param authenticationFilterService The custom authentication filter service
     * @param authenticationProvider The authentication provider for authenticating users
     */
    public SecurityConfiguration(AuthenticationFilterService authenticationFilterService,
                                  AuthenticationProvider authenticationProvider) {
        this.authenticationFilterService = authenticationFilterService;
        this.authenticationProvider = authenticationProvider;
    }

    /**
     * Configures the security filter chain for HTTP requests.
     * 
     * @param http The HttpSecurity object to configure
     * @return The configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Disables CSRF protection (use with caution)
            .authorizeHttpRequests(auth -> auth
                // Specify which requests should be permitted without authentication
                .requestMatchers("/api/v1/auth/**", "/forgotPassword/**")
                .permitAll()
                // All other requests require authentication
                .anyRequest()
                .authenticated())
            .sessionManagement(session -> session
                // Use stateless session management
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Set the custom authentication provider
            .authenticationProvider(authenticationProvider)
            // Add the custom authentication filter before the default UsernamePasswordAuthenticationFilter
            .addFilterBefore(authenticationFilterService, UsernamePasswordAuthenticationFilter.class);
        
        return http.build(); // Build and return the SecurityFilterChain
    }
}
