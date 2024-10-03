package com.tapmovie.authentication.utils;

/**
 * The ChangePassword record is a simple data structure used to encapsulate
 * the information required for changing a user's password. It contains
 * the new password and a confirmation of the new password.
 * 
 * This record serves as a Data Transfer Object (DTO) to facilitate the
 * transfer of password change data within the application, typically
 * in a user account management context.
 */
public record ChangePassword(String password, String repeatPassword) {
    // No additional methods or functionality are needed for this record,
    // as it automatically provides getter methods for each field.
}
