package com.nt.dto;

import com.nt.enums.AdmitCardStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class AdmitCardResponseDto {
    private Long id;
    private Long jobId;
    private String jobTitle;
    private String examName;
    private String examType;
    private LocalDate admitCardReleaseDate;
    private LocalDate examDate;
    private LocalDate admitCardLastDate;
    private AdmitCardStatus status;
    private String admitCardUrl;
    private String examCenter;
    private String importantInstructions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

