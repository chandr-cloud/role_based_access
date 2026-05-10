package com.nt.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.nt.auth.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Utility class for JWT token operations.
 * Uses HS512 algorithm with configurable expiry times.
 */
@Component
public class AuthUtil {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    @Value("${jwt.access.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generate access token (short-lived, default 15 minutes)
     */
    public String generateAccessToken(UserRole user) {
        Date now = new Date();
        Date expiry = new Date(System.currentTimeMillis() + accessTokenExpiration);
        String token = Jwts.builder().subject(user.getUsername()).claim("userId", user.getId()).claim("type", "access")
                .issuedAt(now).expiration(expiry).signWith(getSecretKey()).compact();
        return token;
    }

    /**
     * Generate refresh token (long-lived, default 7 days)
     */
    public String generateRefreshToken(UserRole user) {
        Date now = new Date();
        Date expiry = new Date(System.currentTimeMillis() + refreshTokenExpiration);
        String token = Jwts.builder().subject(user.getUsername()).claim("userId", user.getId()).claim("type", "refresh").issuedAt(now).expiration(expiry).signWith(getSecretKey()).compact();
        return token;
    }

    /**
     * Extract username from token
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }

    /**
     * Extract user ID from token
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
        return claims.get("userId", Long.class);
    }

    /**
     * Check if token is expired
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Validate token
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get expiry date for access token
     */
    public Date getAccessTokenExpiry() {
        return new Date(System.currentTimeMillis() + accessTokenExpiration);
    }

    /**
     * Get expiry date for refresh token
     */
    public Date getRefreshTokenExpiry() {
        return new Date(System.currentTimeMillis() + refreshTokenExpiration);
    }

    /**
     * Get access token expiration time in milliseconds
     */
    public long getAccessTokenExpiration() {
        return accessTokenExpiration;
    }
    /**
     * Get refresh token expiration time in milliseconds
     */
    public long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }
}
