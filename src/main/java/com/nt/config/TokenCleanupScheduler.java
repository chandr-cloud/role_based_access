package com.nt.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nt.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Scheduled task to clean up expired refresh tokens from the database.
 * Runs daily at midnight to remove tokens that have expired.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TokenCleanupScheduler {

	private final RefreshTokenRepository refreshTokenRepository;

	/**
	 * Clean up expired tokens daily at midnight.
	 * This helps maintain database performance by removing old tokens.
	 */
	@Scheduled(cron = "0 0 0 * * ?") // Every day at midnight
	public void cleanupExpiredTokens() {
		log.info("Running scheduled cleanup of expired refresh tokens");
		int deletedCount = refreshTokenRepository.deleteExpiredTokens();
		log.info("Cleaned up {} expired refresh tokens", deletedCount);
	}
}
