package com.tapmovie.authentication.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class ForgotPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer forgotPasswordId;

    @Column(nullable = false)
    private Integer otp;

    @Column(nullable = false)
    private Date expirationTime;

    @OneToOne
    private User user;

    // No-argument constructor
    public ForgotPassword() {}

    // All-argument constructor
    public ForgotPassword(Integer forgotPasswordId, Integer otp, Date expirationTime, User user) {
        this.forgotPasswordId = forgotPasswordId;
        this.otp = otp;
        this.expirationTime = expirationTime;
        this.user = user;
    }

    // Getters and Setters
    public Integer getForgotPasswordId() {
        return forgotPasswordId;
    }

    public void setForgotPasswordId(Integer forgotPasswordId) {
        this.forgotPasswordId = forgotPasswordId;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Static builder pattern
    public static ForgotPasswordBuilder builder() {
        return new ForgotPasswordBuilder();
    }

    public static class ForgotPasswordBuilder {
        private Integer forgotPasswordId;
        private Integer otp;
        private Date expirationTime;
        private User user;

        public ForgotPasswordBuilder forgotPasswordId(Integer forgotPasswordId) {
            this.forgotPasswordId = forgotPasswordId;
            return this;
        }

        public ForgotPasswordBuilder otp(Integer otp) {
            this.otp = otp;
            return this;
        }

        public ForgotPasswordBuilder expirationTime(Date expirationTime) {
            this.expirationTime = expirationTime;
            return this;
        }

        public ForgotPasswordBuilder user(User user) {
            this.user = user;
            return this;
        }

        public ForgotPassword build() {
            return new ForgotPassword(forgotPasswordId, otp, expirationTime, user);
        }
    }
}
