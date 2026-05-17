package com.nt.controller;

import com.nt.dto.ResultRequestDto;
import com.nt.dto.ResultResponseDto;
import com.nt.enums.ResultStatus;
import com.nt.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static org.springframework.http.ResponseEntity.noContent;

@RestController
@RequiredArgsConstructor
@RequestMapping("/results")
public class ResultController {

    private final ResultService resultService;

    @PostMapping
    public ResponseEntity<ResultResponseDto> create(@RequestBody ResultRequestDto dto) {
        return ResponseEntity.ok(resultService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(resultService.getById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ResultResponseDto>> search(
            @RequestParam(required = false) Long jobId,
            @RequestParam(required = false) ResultStatus status,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        return ResponseEntity.ok(resultService.search(jobId, status, from, to, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultResponseDto> update(@PathVariable Long id, @RequestBody ResultRequestDto dto) {
        return ResponseEntity.ok(resultService.update(id, dto));
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<ResultResponseDto> publish(@PathVariable Long id) {
        return ResponseEntity.ok(resultService.publish(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        resultService.delete(id);
        return noContent().build();
    }
}

