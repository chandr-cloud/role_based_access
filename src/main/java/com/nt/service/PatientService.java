package com.nt.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.nt.dto.PatientResponseDto;
import com.nt.entity.Patient;
import com.nt.repository.PatientRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientService {

	private final PatientRepository patientRepository;
	private final ModelMapper modelMapper;

	@Transactional
	public PatientResponseDto getPatientById(Long patientId) {
		Patient patient = patientRepository.findById(patientId)
				.orElseThrow(() -> new EntityNotFoundException("Patient Not " + "Found with id: " + patientId));
		return modelMapper.map(patient, PatientResponseDto.class);
	}

	public List<PatientResponseDto> getAllPatients() {
		return patientRepository.findAll().stream().map(patient -> modelMapper.map(patient, PatientResponseDto.class))
				.collect(Collectors.toList());
	}
}
