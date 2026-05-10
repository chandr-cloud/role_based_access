package com.nt.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ApiError> handleUsernameNotFoundException(UsernameNotFoundException ex) {
		log.warn("User not found: {}", ex.getMessage());
		ApiError apiError = new ApiError("Username not found with username: " + ex.getMessage(), HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(apiError, apiError.getStatusCode());
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex) {
		log.warn("Authentication failed: {}", ex.getMessage());
		ApiError apiError = new ApiError("Authentication failed: Invalid credentials", HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(JwtException.class)
	public ResponseEntity<ApiError> handleJwtException(JwtException ex) {
		log.warn("Invalid JWT token: {}", ex.getMessage());
		ApiError apiError = new ApiError("Invalid JWT token: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException ex) {
		log.warn("Access denied: {}", ex.getMessage());
		ApiError apiError = new ApiError("Access denied: Insufficient permissions", HttpStatus.FORBIDDEN);
		return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<ApiError> handleTokenExpiredException(TokenExpiredException ex) {
		log.warn("Token expired: {}", ex.getMessage());
		ApiError apiError = new ApiError("Token expired: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<ApiError> handleInvalidTokenException(InvalidTokenException ex) {
		log.warn("Invalid token: {}", ex.getMessage());
		ApiError apiError = new ApiError("Invalid token: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex) {
		log.warn("Bad request: {}", ex.getMessage());
		ApiError apiError = new ApiError("Bad request: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleGenericException(Exception ex) {
		log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
		ApiError apiError = new ApiError("An unexpected error occurred",
				HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
