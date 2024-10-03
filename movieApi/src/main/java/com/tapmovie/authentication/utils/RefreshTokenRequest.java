package com.tapmovie.authentication.utils;

/**
 * The RefreshTokenRequest class is a data structure used to encapsulate 
 * the refresh token information needed for refreshing user authentication.
 * 
 * This class serves as a Data Transfer Object (DTO) to facilitate the 
 * transfer of refresh token data between the client and the server.
 */
public class RefreshTokenRequest {
    private String refreshToken;

    // No-argument constructor
    public RefreshTokenRequest() {
    }

    // All-argument constructor
    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // Getter
    public String getRefreshToken() {
        return refreshToken;
    }

    // Setter
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // Optional: toString() method for easier debugging
    @Override
    public String toString() {
        return "RefreshTokenRequest{" +
                "refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
