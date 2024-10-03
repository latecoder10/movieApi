package com.tapmovie.authentication.utils;

/**
 * The LoginRequest class is a data structure used to encapsulate the 
 * information needed for a user to log in to the application. 
 * It contains the user's email and password.
 * 
 * This class serves as a Data Transfer Object (DTO) to facilitate the 
 * transfer of login data between the client and the server.
 */
public class LoginRequest {
    private String email;
    private String password;

    // No-argument constructor
    public LoginRequest() {
    }

    // All-argument constructor
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Builder class for creating instances of LoginRequest
    public static class Builder {
        private String email;
        private String password;

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public LoginRequest build() {
            return new LoginRequest(email, password);
        }
    }
}
