package com.nt.security;

import java.time.Instant;
import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nt.auth.RoleType;
import com.nt.auth.UserRole;
import com.nt.repository.UserRepository;
import com.nt.dto.LoginRequestDto;
import com.nt.dto.LoginResponseDto;
import com.nt.dto.SignupRequestDto;
import com.nt.dto.SignupResponseDto;
import com.nt.entity.Admins;
import com.nt.entity.RefreshToken;
import com.nt.error.InvalidTokenException;
import com.nt.error.TokenExpiredException;
import com.nt.repository.AdminRepository;
import com.nt.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for authentication operations.
 * Handles login, signup, token refresh with rotation, and logout.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUserService {


	private final AdminRepository adminRepository;
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final AuthUtil authUtil;
	private final PasswordEncoder passwordEncoder;

	/**
	 * Authenticate user and generate tokens.
	 * Creates a new refresh token stored in database.
	 */
	@Transactional
	public LoginResponseDto loginUser(LoginRequestDto userRequestDto, String deviceId, String ipAddress) {
		log.info("Login attempt for user: {}", userRequestDto.getUsername());
		Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userRequestDto.getUsername(), userRequestDto.getPassword()));
		UserRole user = (UserRole) authenticate.getPrincipal();
		// Generate tokens
		String accessToken = authUtil.generateAccessToken(user);
		String refreshToken = authUtil.generateRefreshToken(user);
		// Store refresh token in database
		saveRefreshToken(refreshToken, user, deviceId, ipAddress);
		log.info("User logged in successfully: {}", user.getUsername());
		return LoginResponseDto.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.tokenType("Bearer")
				.expiresIn(authUtil.getAccessTokenExpiration())
				.refreshExpiresIn(authUtil.getRefreshTokenExpiration())
				.build();
	}

	/**
	 * Login with default device info
	 */
	public LoginResponseDto loginUser(LoginRequestDto userRequestDto) {
		return loginUser(userRequestDto, null, null);
	}

	/**
	 * Save refresh token to database with token rotation support.
	 */
	@Transactional
	public void saveRefreshToken(String token, UserRole user, String deviceId, String ipAddress) {
		RefreshToken refreshTokenEntity = RefreshToken.builder()
				.token(token)
				.user(user)
				.expiryDate(authUtil.getRefreshTokenExpiry().toInstant())
				.createdAt(Instant.now())
				.deviceId(deviceId)
				.ipAddress(ipAddress)
				.revoked(false)
				.build();
		refreshTokenRepository.save(refreshTokenEntity);
		log.debug("Refresh token saved for user: {}", user.getUsername());
	}

	/**
	 * Refresh token with rotation.
	 * Validates the old refresh token, revokes it, and issues a new one.
	 */
	@Transactional
	public LoginResponseDto refreshToken(String refreshToken) {
		log.info("Token refresh request received");
		
		// Validate token exists in database
		RefreshToken tokenEntity = refreshTokenRepository.findByToken(refreshToken)
				.orElseThrow(() -> new InvalidTokenException("Refresh token not found"));
		
		// Check if token is revoked
		if (tokenEntity.isRevoked()) {
			log.warn("Attempted to use revoked token");
			throw new InvalidTokenException("Refresh token has been revoked");
		}
		
		// Check if token is expired
		if (tokenEntity.isExpired()) {
			log.warn("Attempted to use expired token");
			throw new TokenExpiredException("Refresh token has expired");
		}
		
		// Get user from token
		UserRole user = tokenEntity.getUser();
		// Revoke old refresh token (token rotation)
		tokenEntity.setRevoked(true);
		refreshTokenRepository.save(tokenEntity);
		// Generate new access token
		String newAccessToken = authUtil.generateAccessToken(user);
		// Generate new refresh token
		String newRefreshToken = authUtil.generateRefreshToken(user);
		// Save new refresh token to database
		saveRefreshToken(newRefreshToken, user, tokenEntity.getDeviceId(), tokenEntity.getIpAddress());
		log.info("Token refreshed successfully for user: {}", user.getUsername());
		return LoginResponseDto.builder()
				.accessToken(newAccessToken)
				.refreshToken(newRefreshToken)
				.tokenType("Bearer")
				.expiresIn(authUtil.getAccessTokenExpiration())
				.refreshExpiresIn(authUtil.getRefreshTokenExpiration())
				.build();
	}
	/**
	 * Logout - revoke single refresh token
	 */
	@Transactional
	public void logout(String refreshToken) {
		log.info("Logout request received");
		refreshTokenRepository.findByToken(refreshToken).ifPresent(tokenEntity -> {
			tokenEntity.setRevoked(true);
			refreshTokenRepository.save(tokenEntity);
			log.info("Refresh token revoked for user: {}", tokenEntity.getUser().getUsername());
		});
	}

	/**
	 * Logout from all devices - revoke all refresh tokens for user
	 */
	@Transactional
	public void logoutAll(String username) {
		log.info("Logout from all devices request for user: {}", username);
		UserRole user = userRepository.findByEmail(username)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));
		refreshTokenRepository.revokeAllTokensByUserId(user.getId());
		log.info("All refresh tokens revoked for user: {}", username);
	}

	/**
	 * Sign up new user
	 */
	@Transactional
	public SignupResponseDto signupUser(SignupRequestDto signupRequestDto) {
		log.info("Signup request for user: {}", signupRequestDto.getUsername());
		UserRole existingUser = userRepository.findByEmail(signupRequestDto.getUsername()).orElse(null);
		if (existingUser != null) {
			throw new IllegalArgumentException("User already exists");
		}
		UserRole userToSave = new UserRole();
		userToSave.setUsername(signupRequestDto.getUsername());
		userToSave.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
		userToSave.setRoles(signupRequestDto.getRoles());
		UserRole userSaved = userRepository.save(userToSave);
		if (signupRequestDto.getRoles() != null && signupRequestDto.getRoles().contains(RoleType.ADMIN)) {
			adminRepository.save(
					Admins.builder()
							.email(signupRequestDto.getUsername())
							.gender(signupRequestDto.getGender())
							.name(signupRequestDto.getFullName())
							.createdAt(LocalDateTime.now())
							.birthDate(signupRequestDto.getDateOfBirth())
							.user(userSaved)
							.build());
		}
		log.info("User signed up successfully: {}", userSaved.getUsername());
		return new SignupResponseDto(userSaved.getId(), userSaved.getUsername(), signupRequestDto.getPassword());
	}
}
