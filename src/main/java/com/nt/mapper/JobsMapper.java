package com.nt.mapper;

import com.nt.dto.JobPostDetailResponseDto;
import com.nt.dto.JobResponseDto;
import com.nt.entity.Job;
import com.nt.entity.JobPostDetail;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JobsMapper {

    // =========================
    // ✅ Entity → Response DTO
    // =========================
    public JobResponseDto toDto(Job job) {

        return JobResponseDto.builder()
                .id(job.getId())
                .title(job.getJobTitle().getTitle())
                .shortInfo(job.getJobTitle().getShortInfo())
                .organization(job.getOrganization())
                .description(job.getDescription())
                .category(job.getCategory())
                .jobType(job.getJobType())
                .totalPosts(job.getTotalPosts())
                .minAge(job.getMinAge())
                .maxAge(job.getMaxAge())
                .ageRelaxation(job.getAgeRelaxation())
                .feeGeneral(job.getFeeGeneral())
                .feeOBC(job.getFeeOBC())
                .feeSC(job.getFeeSC())
                .feeST(job.getFeeST())
                .feePH(job.getFeePH())
                .paymentMode(job.getPaymentMode())
                .applyStartDate(job.getApplyStartDate())
                .applyLastDate(job.getApplyLastDate())
                .feeLastDate(job.getFeeLastDate())
                .correctionLastDate(job.getCorrectionLastDate())
                .examDate(job.getExamDate())
                .status(job.getStatus().name())
                .officialWebsite(job.getOfficialWebsite())
                .notificationUrl(job.getNotificationUrl())
                .createdAt(LocalDate.from(job.getCreatedAt()))
                .updatedAt(LocalDate.from(job.getUpdatedAt()))
                .postDetails(mapPostDetails(job.getPostDetails()))
                .build();
    }

    // =========================
    // ✅ List Mapping Helper
    // =========================
    private List<JobPostDetailResponseDto> mapPostDetails(List<JobPostDetail> details) {
        if (details == null) return List.of();

        return details.stream()
                .map(p -> JobPostDetailResponseDto.builder()
                        .id(p.getId())
                        .postName(p.getPostName())
                        .totalPosts(p.getPostCount())
                        .qualification(p.getEligibility())
                        .ageLimit("As per rules") // optional/custom
                        .build())
                .collect(Collectors.toList());
    }

    // =========================
    // ✅ Slug Generator
    // =========================
    private String generateSlug(String title) {
        return title.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }
}