package com.nt.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.nt.entity.Appointment;
import com.nt.entity.Insurance;

import lombok.Data;

@Data
public class PatientResponseDto {
	private Long id;

	private String name;

	private String gender;

	private LocalDate birthDate;

	private LocalDateTime createdAt;

	private String email;

	private Insurance insurance;

	private List<Appointment> appointments = new ArrayList<>();
}
