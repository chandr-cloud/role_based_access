package com.nt.dto;

import com.nt.enums.AdmitCardStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AdmitCardRequestDto {
    private Long jobId;
    private String examName;
    private String examType;
    private LocalDate admitCardReleaseDate;
    private LocalDate examDate;
    private LocalDate admitCardLastDate;
    private AdmitCardStatus status;
    private String admitCardUrl;
    private String examCenter;
    private String importantInstructions;
}

