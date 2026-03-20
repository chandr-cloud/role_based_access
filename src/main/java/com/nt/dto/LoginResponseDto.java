package com.nt.dto;

import java.util.HashSet;
import java.util.Set;

import com.nt.auth.RoleType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

	private String token;

	private String username;

	private String user;

	Set<RoleType> roles = new HashSet<>();
}
