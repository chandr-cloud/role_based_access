package com.nt.dto;

import com.nt.enums.ResultStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ResultRequestDto {
    private Long jobId;
    private String resultType;
    private LocalDate publishedOn;
    private ResultStatus status;
    private String resultUrl;
    private String remarks;
}

