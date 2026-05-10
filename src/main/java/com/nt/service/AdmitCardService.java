package com.nt.service;

import com.nt.dto.AdmitCardRequestDto;
import com.nt.dto.AdmitCardResponseDto;
import com.nt.enums.AdmitCardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface AdmitCardService {

    AdmitCardResponseDto create(AdmitCardRequestDto dto);

    AdmitCardResponseDto getById(Long id);

    Page<AdmitCardResponseDto> search(Long jobId, AdmitCardStatus status, LocalDate from, LocalDate to, Pageable pageable);

    AdmitCardResponseDto update(Long id, AdmitCardRequestDto dto);

    AdmitCardResponseDto release(Long id);

    AdmitCardResponseDto expire(Long id);

    AdmitCardResponseDto cancel(Long id);

    void delete(Long id);
}

