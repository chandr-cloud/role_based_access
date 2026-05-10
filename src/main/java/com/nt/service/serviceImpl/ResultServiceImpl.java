package com.nt.service.serviceImpl;

import com.nt.dto.ResultRequestDto;
import com.nt.dto.ResultResponseDto;
import com.nt.entity.Job;
import com.nt.entity.Result;
import com.nt.enums.ResultStatus;
import com.nt.repository.JobRepository;
import com.nt.repository.ResultRepository;
import com.nt.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;
    private final JobRepository jobRepository;

    @Override
    public ResultResponseDto create(ResultRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("ResultRequestDto cannot be null");
        }

        Job job = jobRepository.findById(dto.getJobId())
                .orElseThrow(() -> new EntityNotFoundException("Job not found: " + dto.getJobId()));

        Result result = Result.builder()
                .job(job)
                .resultType(dto.getResultType())
                .publishedOn(dto.getPublishedOn())
                .status(dto.getStatus() != null ? dto.getStatus() : ResultStatus.PENDING)
                .resultUrl(dto.getResultUrl())
                .remarks(dto.getRemarks())
                .build();

        Result saved = resultRepository.save(result);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultResponseDto getById(Long id) {
        return toDto(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResultResponseDto> search(Long jobId, ResultStatus status, LocalDate from, LocalDate to, Pageable pageable) {
        return resultRepository.advancedSearch(jobId, status, from, to, pageable)
                .map(this::toDto);
    }

    @Override
    public ResultResponseDto update(Long id, ResultRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("ResultRequestDto cannot be null");
        }

        Result existing = findById(id);

        Job job = jobRepository.findById(dto.getJobId())
                .orElseThrow(() -> new EntityNotFoundException("Job not found: " + dto.getJobId()));

        existing.setJob(job);
        existing.setResultType(dto.getResultType());
        existing.setPublishedOn(dto.getPublishedOn());
        existing.setStatus(dto.getStatus() != null ? dto.getStatus() : existing.getStatus());
        existing.setResultUrl(dto.getResultUrl());
        existing.setRemarks(dto.getRemarks());

        Result saved = resultRepository.save(existing);
        return toDto(saved);
    }

    @Override
    public ResultResponseDto publish(Long id) {
        Result existing = findById(id);
        existing.setStatus(ResultStatus.PUBLISHED);
        existing.setPublishedOn(LocalDate.now());
        Result saved = resultRepository.save(existing);
        return toDto(saved);
    }

    @Override
    public void delete(Long id) {
        Result existing = findById(id);
        resultRepository.delete(existing);
    }

    private ResultResponseDto toDto(Result r) {
        if (r == null) {
            return null;
        }

        Job job = r.getJob();
        String jobTitle = job != null && job.getJobTitle() != null ? job.getJobTitle().getTitle() : null;

        return ResultResponseDto.builder()
                .id(r.getId())
                .jobId(job != null ? job.getId() : null)
                .jobTitle(jobTitle)
                .resultType(r.getResultType())
                .publishedOn(r.getPublishedOn())
                .status(r.getStatus())
                .resultUrl(r.getResultUrl())
                .remarks(r.getRemarks())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }

    private Result findById(Long id) {
        if (id == null) {
            throw new EntityNotFoundException("Result not found: null");
        }
        return resultRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Result not found: " + id));
    }
}

