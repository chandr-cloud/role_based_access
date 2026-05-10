package com.nt.service;

import com.nt.dto.ResultRequestDto;
import com.nt.dto.ResultResponseDto;
import com.nt.enums.ResultStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ResultService {

    ResultResponseDto create(ResultRequestDto dto);

    ResultResponseDto getById(Long id);

    Page<ResultResponseDto> search(Long jobId, ResultStatus status, LocalDate from, LocalDate to, Pageable pageable);

    ResultResponseDto update(Long id, ResultRequestDto dto);

    ResultResponseDto publish(Long id);

    void delete(Long id);
}

