package com.nt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response DTO for login and token refresh operations.
 * Contains access token, refresh token, and expiry information.
 */
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

	private String accessToken;

	private String refreshToken;

	private String tokenType;

	private long expiresIn;

	private long refreshExpiresIn;
}
