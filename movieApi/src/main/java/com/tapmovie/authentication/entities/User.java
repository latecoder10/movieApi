package com.tapmovie.authentication.entities;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @NotBlank(message = "The name can't be blank")
    private String name;

    @NotBlank(message = "The username can't be blank")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "The email can't be blank")
    @Column(unique = true)
    @Email(message = "Please enter email in proper format")
    private String email;

    @NotBlank(message = "The password can't be blank")
    @Size(min = 5, message = "The password must have at least 5 characters")
    private String password;

    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;

    @Enumerated(EnumType.STRING)
    private UserRole role;
    
    @OneToOne(mappedBy = "user")
    private ForgotPassword forgotPassword;
    
    private boolean isEnabled = true;
    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired = true;

    // No-argument constructor
    public User() {
    }

    // All-argument constructor
    public User(Integer userId, String name, String username, String email, String password, UserRole role) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    // Getters
    public Integer getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    // Setters
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    	

    public ForgotPassword getForgotPassword() {
		return forgotPassword;
	}

	public void setForgotPassword(ForgotPassword forgotPassword) {
		this.forgotPassword = forgotPassword;
	}



	// Builder Pattern
    public static class Builder {
        private final User user;

        public Builder() {
            user = new User();
        }

        public Builder userId(Integer userId) {
            user.userId = userId;
            return this;
        }

        public Builder name(String name) {
            user.name = name;
            return this;
        }

        public Builder username(String username) {
            user.username = username;
            return this;
        }

        public Builder email(String email) {
            user.email = email;
            return this;
        }

        public Builder password(String password) {
            user.password = password;
            return this;
        }

        public Builder refreshToken(RefreshToken refreshToken) {
            user.refreshToken = refreshToken;
            return this;
        }
        public Builder forgotPassword(ForgotPassword forgotPassword) {
        	user.forgotPassword = forgotPassword;
        	return this;
        }

        public Builder role(UserRole role) {
            user.role = role;
            return this;
        }

        public Builder enabled(boolean isEnabled) {
            user.isEnabled = isEnabled;
            return this;
        }

        public Builder accountNonExpired(boolean isAccountNonExpired) {
            user.isAccountNonExpired = isAccountNonExpired;
            return this;
        }

        public Builder accountNonLocked(boolean isAccountNonLocked) {
            user.isAccountNonLocked = isAccountNonLocked;
            return this;
        }

        public Builder credentialsNonExpired(boolean isCredentialsNonExpired) {
            user.isCredentialsNonExpired = isCredentialsNonExpired;
            return this;
        }

        public User build() {
            return user;
        }
    }
    
    public static Builder builder() {
        return new Builder();
    }
}
