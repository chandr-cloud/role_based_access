package com.nt.service.serviceImpl;

import com.nt.dto.JobPostDetailDTO;
import com.nt.dto.JobRequestDTO;
import com.nt.dto.JobResponseDto;
import com.nt.entity.Job;
import com.nt.entity.JobPostDetail;
import com.nt.entity.JobTitle;
import com.nt.enums.JobStatus;
import com.nt.mapper.JobsMapper;
import com.nt.repository.JobRepository;
import com.nt.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    private final JobsMapper jobsMapper;

    // ✅ CREATE JOB (FIXED)
    @Override
    public Job createJob(JobRequestDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException("JobRequestDTO cannot be null");
        }

        // ✅ Create JobTitle (NEW)
        JobTitle jobTitle = JobTitle.builder()
                .title(dto.getTitle())
                .slug(generateSlug(dto.getTitle()))
                .shortInfo(dto.getShortInfo())
                .build();

        // ✅ Create Job
        Job job = Job.builder()
                .jobTitle(jobTitle) // ✅ FIXED
                .organization(dto.getOrganization())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .jobType(dto.getJobType())
                .totalPosts(dto.getTotalPosts())
                .minAge(dto.getMinAge())
                .maxAge(dto.getMaxAge())
                .ageRelaxation(dto.getAgeRelaxation())
                .feeGeneral(dto.getFeeGeneral())
                .feeOBC(dto.getFeeOBC())
                .feeSC(dto.getFeeSC())
                .feeST(dto.getFeeST())
                .feePH(dto.getFeePH())
                .paymentMode(dto.getPaymentMode())
                .applyStartDate(dto.getApplyStartDate())
                .applyLastDate(dto.getApplyLastDate())
                .feeLastDate(dto.getFeeLastDate())
                .correctionLastDate(dto.getCorrectionLastDate())
                .examDate(dto.getExamDate())
                .status(parseStatus(dto.getStatus()))
                .officialWebsite(dto.getOfficialWebsite())
                .notificationUrl(dto.getNotificationUrl())
                .build();

        // 🔥 IMPORTANT: Sync both sides
        jobTitle.setJob(job);

        // ✅ Post Details
        job.setPostDetails(mapPostDetails(dto.getPostDetails(), job));

        return jobRepository.save(job);
    }

    // ✅ UPDATE (FIXED)
    @Override
    public Job updateJob(Long id, Job updatedJob) {

        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));

        // ✅ Update JobTitle
        if (updatedJob.getJobTitle() != null) {
            JobTitle existingTitle = existingJob.getJobTitle();

            if (updatedJob.getJobTitle().getTitle() != null) {
                existingTitle.setTitle(updatedJob.getJobTitle().getTitle());
                existingTitle.setSlug(generateSlug(updatedJob.getJobTitle().getTitle()));
            }

            if (updatedJob.getJobTitle().getShortInfo() != null) {
                existingTitle.setShortInfo(updatedJob.getJobTitle().getShortInfo());
            }
        }

        // ✅ Rest fields
        if (updatedJob.getOrganization() != null)
            existingJob.setOrganization(updatedJob.getOrganization());

        if (updatedJob.getDescription() != null)
            existingJob.setDescription(updatedJob.getDescription());

        if (updatedJob.getCategory() != null)
            existingJob.setCategory(updatedJob.getCategory());

        if (updatedJob.getJobType() != null)
            existingJob.setJobType(updatedJob.getJobType());

        if (updatedJob.getTotalPosts() != null)
            existingJob.setTotalPosts(updatedJob.getTotalPosts());

        if (updatedJob.getMinAge() != null)
            existingJob.setMinAge(updatedJob.getMinAge());

        if (updatedJob.getMaxAge() != null)
            existingJob.setMaxAge(updatedJob.getMaxAge());

        if (updatedJob.getStatus() != null)
            existingJob.setStatus(updatedJob.getStatus());

        // ✅ Post Details
        if (updatedJob.getPostDetails() != null) {
            existingJob.getPostDetails().clear();

            List<JobPostDetail> newDetails = updatedJob.getPostDetails()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(detail -> {
                        detail.setId(null);
                        detail.setJob(existingJob);
                        return detail;
                    })
                    .collect(Collectors.toList());

            existingJob.setPostDetails(newDetails);
        }

        return jobRepository.save(existingJob);
    }

    // ✅ HELPERS

    private List<JobPostDetail> mapPostDetails(List<JobPostDetailDTO> dtoList, Job job) {
        if (dtoList == null || dtoList.isEmpty()) {
            return Collections.emptyList();
        }

        return dtoList.stream()
                .filter(Objects::nonNull)
                .map(d -> JobPostDetail.builder()
                        .postName(d.getPostName())
                        .postCount(d.getPostCount())
                        .eligibility(d.getEligibility())
                        .job(job)
                        .build())
                .collect(Collectors.toList());
    }

    private JobStatus parseStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return JobStatus.UPCOMING;
        }
        try {
            return JobStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            return JobStatus.UPCOMING;
        }
    }

    private String generateSlug(String title) {
        if (title == null) return "";

        return title.toLowerCase()
                .replaceAll("[^a-z0-9]", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }

    // ✅ READ + DELETE (same)

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    @Override
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    public Page<Job> advancedSearch(String keyword, String category, String status, Pageable pageable) {
        JobStatus jobStatus = null;
        if (status != null && !status.isBlank()) {
            try {
                jobStatus = JobStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status value: " + status);
            }
        }
        return jobRepository.advancedSearch(keyword, category, jobStatus, pageable);
    }

}