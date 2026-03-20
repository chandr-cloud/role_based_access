package com.nt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RolesResponseDto {
	private Long userId;
	private String username;
	private String roles;
	private String fullName;
}
