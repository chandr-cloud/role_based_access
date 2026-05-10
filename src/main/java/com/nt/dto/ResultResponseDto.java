package com.nt.dto;

import com.nt.enums.ResultStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ResultResponseDto {
    private Long id;
    private Long jobId;
    private String jobTitle;
    private String resultType;
    private LocalDate publishedOn;
    private ResultStatus status;
    private String resultUrl;
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

