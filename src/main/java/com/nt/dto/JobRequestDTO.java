package com.nt.dto;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class JobRequestDTO {

    private String title;
    private String shortInfo;
    private String organization;
    private String description;

    private String category;
    private String jobType;

    private Integer totalPosts;

    private Integer minAge;
    private Integer maxAge;
    private String ageRelaxation;

    private Double feeGeneral;
    private Double feeOBC;
    private Double feeSC;
    private Double feeST;
    private Double feePH;

    private String paymentMode;

    private LocalDate applyStartDate;
    private LocalDate applyLastDate;
    private LocalDate feeLastDate;
    private LocalDate correctionLastDate;
    private LocalDate examDate;

    private String status;

    private String officialWebsite;
    private String notificationUrl;

    private List<JobPostDetailDTO> postDetails;
}