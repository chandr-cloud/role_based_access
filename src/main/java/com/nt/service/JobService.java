package com.nt.service;
import com.nt.dto.JobRequestDTO;
import com.nt.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobService {

        Job createJob(JobRequestDTO dto);

        List<Job> getAllJobs();

        Job getJobById(Long id);

        void deleteJob(Long id);

    public Job updateJob(Long id, Job updatedJob);

    Page<Job> advancedSearch(String keyword, String location, String status, Pageable pageable);
}
