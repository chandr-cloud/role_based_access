package com.nt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nt.enums.JobStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "job_title_id", nullable = false, unique = true)
    private JobTitle jobTitle;

    private String organization;

    @Column(length = 3000)
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status = JobStatus.UPCOMING;

    private String officialWebsite;
    private String notificationUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<JobPostDetail> postDetails;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}