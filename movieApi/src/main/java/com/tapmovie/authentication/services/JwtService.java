package com.tapmovie.authentication.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service class for handling JSON Web Token (JWT) operations.
 * This class provides methods for generating, validating, and extracting information
 * from JWTs, which are used for authentication and authorization in the application.
 */
@Service
public class JwtService {

    // Secret key used for signing the JWTs. Should be kept secure and not hard-coded in production.
    private static final String SECRET_KEY = "BF7FD11ACE545745B7BA1AF98B6F156D127BC7BB544BAB6A4FD74E4FC7";

    /**
     * Extracts the username from the given JWT.
     *
     * @param token the JWT from which to extract the username
     * @return the username contained in the JWT
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the given JWT.
     *
     * @param token the JWT from which to extract the claim
     * @param claimsResolver a function to extract a specific claim from the Claims object
     * @param <T> the type of the claim to be extracted
     * @return the extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the given JWT.
     *
     * @param token the JWT from which to extract the claims
     * @return a Claims object containing all the claims from the JWT
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Decodes the SECRET_KEY to obtain the signing key for JWT.
     *
     * @return the signing key as a Key object
     */
    private Key getSignInKey() {
        // Decode the SECRET_KEY from Base64 format
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a JWT for the specified user details.
     *
     * @param userDetails the user details for which to generate the token
     * @return the generated JWT as a String
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JWT with additional claims for the specified user details.
     *
     * @param extraClaims additional claims to include in the JWT
     * @param userDetails the user details for which to generate the token
     * @return the generated JWT as a String
     */
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 25 * 1000)) // Token expiration time
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates the given JWT by checking if it is expired and matches the provided user details.
     *
     * @param token the JWT to validate
     * @param userDetails the user details to compare with
     * @return true if the token is valid; false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Checks if the given JWT is expired.
     *
     * @param token the JWT to check
     * @return true if the token is expired; false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the given JWT.
     *
     * @param token the JWT from which to extract the expiration date
     * @return the expiration date of the JWT
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
