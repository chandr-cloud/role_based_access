package com.nt.controller;

import com.nt.dto.AdmitCardRequestDto;
import com.nt.dto.AdmitCardResponseDto;
import com.nt.enums.AdmitCardStatus;
import com.nt.service.AdmitCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import jakarta.validation.Valid;

import static org.springframework.http.ResponseEntity.noContent;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admit-cards")
public class AdmitCardController {

    private final AdmitCardService admitCardService;

    @PostMapping
    public ResponseEntity<AdmitCardResponseDto> create(@RequestBody AdmitCardRequestDto dto) {
        log.info("Creating admit card for jobId: {}", dto != null ? dto.getJobId() : null);
        return ResponseEntity.ok(admitCardService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdmitCardResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(admitCardService.getById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AdmitCardResponseDto>> search(
            @RequestParam(required = false) Long jobId,
            @RequestParam(required = false) AdmitCardStatus status,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to,
            @PageableDefault(size = 10, sort = "examDate") Pageable pageable
    ) {
        log.info("Searching admit cards - jobId: {}, status: {}", jobId, status);
        return ResponseEntity.ok(admitCardService.search(jobId, status, from, to, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdmitCardResponseDto> update(@PathVariable Long id, @RequestBody AdmitCardRequestDto dto) {
        return ResponseEntity.ok(admitCardService.update(id, dto));
    }

    @PatchMapping("/{id}/release")
    public ResponseEntity<AdmitCardResponseDto> release(@PathVariable Long id) {
        return ResponseEntity.ok(admitCardService.release(id));
    }

    @PatchMapping("/{id}/expire")
    public ResponseEntity<AdmitCardResponseDto> expire(@PathVariable Long id) {
        return ResponseEntity.ok(admitCardService.expire(id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AdmitCardResponseDto> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(admitCardService.cancel(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        admitCardService.delete(id);
        return noContent().build();
    }
}

