package com.nt.controller;

import com.nt.dto.JobResponseDto;
import com.nt.entity.Job;
import com.nt.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/jobs/search")
@RequiredArgsConstructor
@Slf4j
public class JobSearchController {

    private final JobService jobService;

    @GetMapping("/search")
    public ResponseEntity<?> advancedSearch(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            Pageable pageable) {
        return ResponseEntity.ok(jobService.advancedSearch(keyword, category, status, pageable));
    }
}