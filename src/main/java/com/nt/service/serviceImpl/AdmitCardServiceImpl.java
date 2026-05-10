package com.nt.service.serviceImpl;

import com.nt.dto.AdmitCardRequestDto;
import com.nt.dto.AdmitCardResponseDto;
import com.nt.entity.AdmitCard;
import com.nt.entity.Job;
import com.nt.enums.AdmitCardStatus;
import com.nt.repository.AdmitCardRepository;
import com.nt.repository.JobRepository;
import com.nt.service.AdmitCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class AdmitCardServiceImpl implements AdmitCardService {

    private final AdmitCardRepository admitCardRepository;
    private final JobRepository jobRepository;

    @Override
    public AdmitCardResponseDto create(AdmitCardRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("AdmitCardRequestDto cannot be null");
        }

        Job job = jobRepository.findById(dto.getJobId())
                .orElseThrow(() -> new EntityNotFoundException("Job not found: " + dto.getJobId()));

        AdmitCard admitCard = AdmitCard.builder()
                .job(job)
                .examName(dto.getExamName())
                .examType(dto.getExamType())
                .admitCardReleaseDate(dto.getAdmitCardReleaseDate())
                .examDate(dto.getExamDate())
                .admitCardLastDate(dto.getAdmitCardLastDate())
                .status(dto.getStatus() != null ? dto.getStatus() : AdmitCardStatus.UPCOMING)
                .admitCardUrl(dto.getAdmitCardUrl())
                .examCenter(dto.getExamCenter())
                .importantInstructions(dto.getImportantInstructions())
                .build();

        AdmitCard saved = admitCardRepository.save(admitCard);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AdmitCardResponseDto getById(Long id) {
        return toDto(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdmitCardResponseDto> search(Long jobId, AdmitCardStatus status, LocalDate from, LocalDate to, Pageable pageable) {
        return admitCardRepository.search(jobId, status, from, to, pageable)
                .map(this::toDto);
    }

    @Override
    public AdmitCardResponseDto update(Long id, AdmitCardRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("AdmitCardRequestDto cannot be null");
        }

        AdmitCard existing = findById(id);

        Job job = jobRepository.findById(dto.getJobId())
                .orElseThrow(() -> new EntityNotFoundException("Job not found: " + dto.getJobId()));

        existing.setJob(job);
        existing.setExamName(dto.getExamName());
        existing.setExamType(dto.getExamType());
        existing.setAdmitCardReleaseDate(dto.getAdmitCardReleaseDate());
        existing.setExamDate(dto.getExamDate());
        existing.setAdmitCardLastDate(dto.getAdmitCardLastDate());
        existing.setStatus(dto.getStatus() != null ? dto.getStatus() : existing.getStatus());
        existing.setAdmitCardUrl(dto.getAdmitCardUrl());
        existing.setExamCenter(dto.getExamCenter());
        existing.setImportantInstructions(dto.getImportantInstructions());

        AdmitCard saved = admitCardRepository.save(existing);
        return toDto(saved);
    }

    @Override
    public AdmitCardResponseDto release(Long id) {
        AdmitCard existing = findById(id);
        existing.setStatus(AdmitCardStatus.AVAILABLE);
        existing.setAdmitCardReleaseDate(LocalDate.now());
        AdmitCard saved = admitCardRepository.save(existing);
        return toDto(saved);
    }

    @Override
    public AdmitCardResponseDto expire(Long id) {
        AdmitCard existing = findById(id);
        existing.setStatus(AdmitCardStatus.EXPIRED);
        AdmitCard saved = admitCardRepository.save(existing);
        return toDto(saved);
    }

    @Override
    public AdmitCardResponseDto cancel(Long id) {
        AdmitCard existing = findById(id);
        existing.setStatus(AdmitCardStatus.CANCELLED);
        AdmitCard saved = admitCardRepository.save(existing);
        return toDto(saved);
    }

    @Override
    public void delete(Long id) {
        AdmitCard existing = findById(id);
        admitCardRepository.delete(existing);
    }

    private AdmitCardResponseDto toDto(AdmitCard a) {
        if (a == null) {
            return null;
        }

        Job job = a.getJob();
        String jobTitle = job != null && job.getJobTitle() != null ? job.getJobTitle().getTitle() : null;

        return AdmitCardResponseDto.builder()
                .id(a.getId())
                .jobId(job != null ? job.getId() : null)
                .jobTitle(jobTitle)
                .examName(a.getExamName())
                .examType(a.getExamType())
                .admitCardReleaseDate(a.getAdmitCardReleaseDate())
                .examDate(a.getExamDate())
                .admitCardLastDate(a.getAdmitCardLastDate())
                .status(a.getStatus())
                .admitCardUrl(a.getAdmitCardUrl())
                .examCenter(a.getExamCenter())
                .importantInstructions(a.getImportantInstructions())
                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .build();
    }

    private AdmitCard findById(Long id) {
        if (id == null) {
            throw new EntityNotFoundException("Admit card not found: null");
        }
        return admitCardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Admit card not found: " + id));
    }
}

