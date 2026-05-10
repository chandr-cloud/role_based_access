package com.nt.controller;

import com.nt.dto.JobRequestDTO;
import com.nt.entity.Job;
import com.nt.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    //  CREATE (ADMIN ONLY)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody JobRequestDTO jobRequestDTO) {
        return ResponseEntity.ok(jobService.createJob(jobRequestDTO));
    }

    //  READ ALL (Public)
    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    //  READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    //  UPDATE (ADMIN ONLY)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job job) {
        return ResponseEntity.ok(jobService.updateJob(id, job));
    }

    //  DELETE (ADMIN ONLY)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.ok("Job deleted successfully");
    }

}