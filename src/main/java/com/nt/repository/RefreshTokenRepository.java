package com.nt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nt.auth.RefreshToken;

/**
 * Repository for refresh token operations.
 * Supports finding tokens by token string, user, and revoking tokens.
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * Find a refresh token by token string
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Find all valid refresh tokens for a user
     */
    @Query("SELECT rt FROM RefreshToken rt WHERE rt.user.id = :userId AND rt.revoked = false AND rt.expiryDate > CURRENT_TIMESTAMP")
    List<RefreshToken> findValidTokensByUserId(@Param("userId") Long userId);

    /**
     * Find all refresh tokens for a user (including revoked)
     */
    @Query("SELECT rt FROM RefreshToken rt WHERE rt.user.id = :userId")
    List<RefreshToken> findByUserId(@Param("userId") Long userId);

    /**
     * Revoke a specific refresh token
     */
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.token = :token")
    int revokeToken(@Param("token") String token);

    /**
     * Revoke all refresh tokens for a user
     */
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.user.id = :userId")
    int revokeAllTokensByUserId(@Param("userId") Long userId);

    /**
     * Delete all expired tokens (for scheduled cleanup)
     */
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiryDate < CURRENT_TIMESTAMP")
    int deleteExpiredTokens();
}
