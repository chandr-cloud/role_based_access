package com.nt.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.dto.PatientResponseDto;
import com.nt.dto.RolesResponseDto;
import com.nt.service.AdminService;
import com.nt.service.DoctorService;
import com.nt.service.PatientService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PatientService patientService;
    private final AdminService adminService;

    @GetMapping("/patients")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }
    @GetMapping("/all/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, List<RolesResponseDto>>> getAllUserRoles() {
        return ResponseEntity.ok(adminService.getAllUserRoles());
    }
    
}
