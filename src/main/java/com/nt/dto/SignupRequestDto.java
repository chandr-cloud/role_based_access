package com.nt.dto;

import java.time.LocalDate;
import java.util.Set;

import com.nt.auth.RoleType;
import com.nt.entity.Doctor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

	private String username;

	private String password;

	private LocalDate dateOfBirth;

	private String gender;

	private String fullName;

	private String insuranceId;

	private Set<RoleType> roles;

	private String specialization;

	private Boolean headDoctor;

}
