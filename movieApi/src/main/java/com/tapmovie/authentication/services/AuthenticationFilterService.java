package com.tapmovie.authentication.services;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This service is responsible for filtering incoming HTTP requests to 
 * authenticate users based on JWT (JSON Web Tokens).
 * 
 * It extends {@link OncePerRequestFilter}, ensuring that the filter 
 * is applied once per request.
 */
@Service
public class AuthenticationFilterService extends OncePerRequestFilter {
    
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor for the AuthenticationFilterService.
     * 
     * @param jwtService the service responsible for JWT operations.
     * @param userDetailsService the service that loads user-specific data.
     */
    public AuthenticationFilterService(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Filters requests to authenticate the user based on the provided JWT.
     * 
     * @param request the HTTP request to be processed.
     * @param response the HTTP response to be returned.
     * @param filterChain the filter chain for further processing.
     * 
     * @throws ServletException if an error occurs during request processing.
     * @throws IOException if an input or output error occurs.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                    @NonNull HttpServletResponse response, 
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Retrieve the Authorization header from the request
        final String authHeader = request.getHeader("Authorization");
        
        String jwt;
        String username;

        // Check if the Authorization header is present and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // If not valid, proceed with the filter chain
            filterChain.doFilter(request, response);
            return;
        }
        
        // Extract the JWT from the Authorization header
        jwt = authHeader.substring(7);
        
        // Extract the username from the JWT
        username = jwtService.extractUsername(jwt);
        
        // If username is not null and there is no authentication in the Security Context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load user details from the UserDetailsService
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            // Validate the JWT against the user details
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Create an authentication token for the user
                UsernamePasswordAuthenticationToken authenticationToken = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                
                // Set additional details for the authentication token
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Set the authentication in the Security Context
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        
        // Proceed with the next filter in the chain
        filterChain.doFilter(request, response);
    }
}
