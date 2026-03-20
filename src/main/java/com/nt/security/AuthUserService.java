package com.nt.security;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nt.auth.RoleType;
import com.nt.auth.UserRole;
import com.nt.authrepo.UserRepository;
import com.nt.dto.LoginRequestDto;
import com.nt.dto.LoginResponseDto;
import com.nt.dto.SignupRequestDto;
import com.nt.dto.SignupResponseDto;
import com.nt.entity.Admins;
import com.nt.entity.Doctor;
import com.nt.entity.Patient;
import com.nt.repository.AdminRepository;
import com.nt.repository.DoctorRepository;
import com.nt.repository.PatientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthUserService {

	private final PatientRepository patientRepository;

	private final AdminRepository adminRepository;

	private final DoctorRepository doctorRepository;

	private final AuthenticationManager authenticationManager;

	public final UserRepository repository;

	public final AuthUtil authUtil;

	public final PasswordEncoder passwordEncoder;

	public LoginResponseDto loginUser(LoginRequestDto userRequestDto) {
		Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userRequestDto.getUsername(), userRequestDto.getPassword()));
		UserRole userRole = (UserRole) authenticate.getPrincipal();
		String genertaeAccessToken = authUtil.genertaeAccessToken(userRole);
		return LoginResponseDto.builder().token(genertaeAccessToken).username(userRole.getUsername())
				.roles(userRole.getRoles()).build();

	}

	public SignupResponseDto signupUser(SignupRequestDto signupRequestDto) {
		UserRole user = repository.findByEmail(signupRequestDto.getUsername()).orElse(null);
		if (user != null)
			throw new IllegalArgumentException("user Already Exist...");
		UserRole userToSave = new UserRole();
		userToSave.setUsername(signupRequestDto.getUsername());
		userToSave.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
		userToSave.setRoles(signupRequestDto.getRoles());
		UserRole userSaved = repository.save(userToSave);
		if (!signupRequestDto.getRoles().isEmpty() && signupRequestDto.getRoles().contains(RoleType.PATIENT)) {
			patientRepository
					.save(Patient.builder().email(signupRequestDto.getUsername()).gender(signupRequestDto.getGender())
							.name(signupRequestDto.getFullName()).createdAt(LocalDateTime.now())
							.birthDate(signupRequestDto.getDateOfBirth()).user(userSaved).build());
		}

		if (!signupRequestDto.getRoles().isEmpty() && signupRequestDto.getRoles().contains(RoleType.DOCTOR)) {
			doctorRepository.save(Doctor.builder().email(signupRequestDto.getUsername())
					.name(signupRequestDto.getFullName()).specialization(signupRequestDto.getSpecialization())
					.user(userSaved).headDoctor(signupRequestDto.getHeadDoctor()).build());
		}
		if (!signupRequestDto.getRoles().isEmpty() && signupRequestDto.getRoles().contains(RoleType.ADMIN)) {
			adminRepository
					.save(Admins.builder().email(signupRequestDto.getUsername()).gender(signupRequestDto.getGender())
							.name(signupRequestDto.getFullName()).createdAt(LocalDateTime.now())
							.birthDate(signupRequestDto.getDateOfBirth()).user(userSaved).build());
		}
		return new SignupResponseDto(userSaved.getId(), userSaved.getUsername(), signupRequestDto.getPassword());
	}
}
