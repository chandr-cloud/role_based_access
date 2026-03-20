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

@Component
public class AuthUtil {

	@Value("${jwt.secretKey}")
	private String jwtSecretKey;

	private SecretKey getsecretKey() {
		return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
	}

	public String genertaeAccessToken(UserRole user) {
		return Jwts.builder().subject(user.getUsername()).claim("password", user.getPassword().toString())
				.issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
				.signWith(getsecretKey()).compact();
	}

	public String getUsernameFromToken(String token) {
		return Jwts.parser().verifyWith(getsecretKey()).build().parseSignedClaims(token).getPayload().getSubject();
	}

}
