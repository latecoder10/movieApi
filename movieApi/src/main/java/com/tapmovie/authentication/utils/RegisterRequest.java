package com.tapmovie.authentication.utils;

/**
 * The RegisterRequest class is a data structure that holds the information 
 * required for a user registration process. This class serves as a Data 
 * Transfer Object (DTO) to facilitate the transfer of user registration 
 * data between the client and the server.
 */
public class RegisterRequest {
    private String name;
    private String email;
    private String username;
    private String password;

    // No-argument constructor
    public RegisterRequest() {
    }

    // All-argument constructor
    public RegisterRequest(String name, String email, String username, String password) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Builder class
    public static class Builder {
        private String name;
        private String email;
        private String username;
        private String password;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public RegisterRequest build() {
            return new RegisterRequest(name, email, username, password);
        }
    }
}
