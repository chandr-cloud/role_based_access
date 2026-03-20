package com.nt.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.nt.authrepo.UserRepository;
import com.nt.dto.RolesResponseDto;
import com.nt.repository.AdminRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final ModelMapper modelMapper;

	private final AdminRepository adminRepository;

	private final UserRepository userRepository;

	public Map<String, List<RolesResponseDto>> getAllUserRoles() {

		List<Object[]> allUsersWithRolesAndDetails = adminRepository.findAllUsersWithRolesAndDetails();

		Map<String, List<RolesResponseDto>> usersDto = allUsersWithRolesAndDetails.stream()
				.map(row -> new RolesResponseDto(((Number) row[0]).longValue(), (String) row[1], (String) row[2],
						(String) row[3]))
				.collect(Collectors.groupingBy(RolesResponseDto::getRoles));
		return usersDto;

	}

}
