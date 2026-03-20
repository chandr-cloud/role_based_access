package com.nt.authcontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.dto.LoginRequestDto;
import com.nt.dto.LoginResponseDto;
import com.nt.dto.SignupRequestDto;
import com.nt.dto.SignupResponseDto;
import com.nt.security.AuthUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class AuthController {

	private final AuthUserService service;

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> loginUser(@RequestBody LoginRequestDto userRequestDto) {
		return ResponseEntity.ok(service.loginUser(userRequestDto));
	}

	@PostMapping("/signup")
	public ResponseEntity<SignupResponseDto> signupUser(@RequestBody SignupRequestDto signupRequestDto) {
		return ResponseEntity.ok(service.signupUser(signupRequestDto));
	}
}
