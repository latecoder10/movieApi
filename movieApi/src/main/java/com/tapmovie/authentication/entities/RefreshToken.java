package com.tapmovie.authentication.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tokenId;

    @Column(nullable = false, length = 500)
    @NotBlank(message = "Please enter refresh token value!")
    private String refreshToken;

    @Column(nullable = false)
    private Integer expirationTime;

    @OneToOne
    private User user;

    // Private constructor for the builder
    private RefreshToken(Builder builder) {
        this.tokenId = builder.tokenId;
        this.refreshToken = builder.refreshToken;
        this.expirationTime = builder.expirationTime;
        this.user = builder.user;
    }

    // Static builder class
    public static class Builder {
        private Integer tokenId;
        private String refreshToken;
        private Integer expirationTime;
        private User user;

        public Builder setTokenId(Integer tokenId) {
            this.tokenId = tokenId;
            return this;
        }

        public Builder setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public Builder setExpirationTime(Integer expirationTime) {
            this.expirationTime = expirationTime;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public RefreshToken build() {
            return new RefreshToken(this);
        }
    }

    // Getters
    public Integer getTokenId() {
        return tokenId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Integer getExpirationTime() {
        return expirationTime;
    }

    public User getUser() {
        return user;
    }

    // No-argument constructor
    public RefreshToken() {}
}
