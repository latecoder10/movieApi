package com.tapmovie.authentication.utils;

/**
 * The AuthResponse class is a data transfer object (DTO) that represents the 
 * response returned to the client during the authentication process.
 * It contains the access token and refresh token necessary for user sessions.
 */
public class AuthResponse {
    private String accessToken;   // The JWT access token
    private String refreshToken;  // The refresh token for session renewal

    // No-argument constructor
    public AuthResponse() {
    }

    // All-argument constructor
    public AuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    // Getters
    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    // Setters
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * Static Builder class for constructing AuthResponse instances.
     */
    public static class Builder {
        private String accessToken;   // The JWT access token
        private String refreshToken;  // The refresh token for session renewal

        /**
         * Sets the access token for the AuthResponse being built.
         *
         * @param accessToken The JWT access token.
         * @return The Builder instance for method chaining.
         */
        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        /**
         * Sets the refresh token for the AuthResponse being built.
         *
         * @param refreshToken The refresh token.
         * @return The Builder instance for method chaining.
         */
        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        /**
         * Constructs and returns an AuthResponse instance with the set values.
         *
         * @return The constructed AuthResponse instance.
         */
        public AuthResponse build() {
            return new AuthResponse(accessToken, refreshToken);
        }
    }

    /**
     * Static method to access the Builder for constructing AuthResponse instances.
     *
     * @return A new Builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }
}
