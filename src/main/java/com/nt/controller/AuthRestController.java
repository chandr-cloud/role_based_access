package com.nt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.dto.LoginRequestDto;
import com.nt.dto.LoginResponseDto;
import com.nt.dto.RefreshTokenRequestDto;
import com.nt.dto.SignupRequestDto;
import com.nt.dto.SignupResponseDto;
import com.nt.security.AuthUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Public authentication controller for login, signup, refresh, and logout operations.
 * These endpoints are accessible without authentication.
 */
@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "APIs for user authentication")
public class AuthRestController {

	private final AuthUserService authUserService;

	@PostMapping("/login")
	@Operation(summary = "User login", description = "Authenticate user and receive access/refresh tokens")
	public ResponseEntity<LoginResponseDto> loginUser(@RequestBody LoginRequestDto userRequestDto) {
		log.info("Login request received for username: {}", userRequestDto.getUsername());
		return ResponseEntity.ok(authUserService.loginUser(userRequestDto));
	}

	@PostMapping("/refresh")
	@Operation(summary = "Refresh token", description = "Refresh access token using refresh token (with rotation)")
	public ResponseEntity<LoginResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto request) {
		log.info("Token refresh request received");
		return ResponseEntity.ok(authUserService.refreshToken(request.getRefreshToken()));
	}

	@PostMapping("/signup")
	@Operation(summary = "User signup", description = "Register new user account")
	public ResponseEntity<SignupResponseDto> signupUser(@RequestBody SignupRequestDto signupRequestDto) {
		log.info("Signup request received for username: {}", signupRequestDto.getUsername());
		return ResponseEntity.ok(authUserService.signupUser(signupRequestDto));
	}

	@PostMapping("/logout")
	@Operation(summary = "Logout", description = "Logout current session (revoke refresh token)")
	public ResponseEntity<Void> logout(@RequestBody RefreshTokenRequestDto request) {
		log.info("Logout request received");
		authUserService.logout(request.getRefreshToken());
		return ResponseEntity.ok().build();
	}
}
