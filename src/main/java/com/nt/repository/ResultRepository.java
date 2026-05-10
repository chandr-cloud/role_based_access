package com.nt.repository;

import com.nt.entity.Result;
import com.nt.enums.ResultStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

    @Query("SELECT r FROM Result r JOIN r.job j JOIN j.jobTitle jt WHERE " +
            "(:jobId IS NULL OR j.id = :jobId) " +
            "AND (:status IS NULL OR r.status = :status) " +
            "AND (:from IS NULL OR r.publishedOn >= :from) " +
            "AND (:to IS NULL OR r.publishedOn <= :to)")
    Page<Result> advancedSearch(
            @Param("jobId") Long jobId,
            @Param("status") ResultStatus status,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            Pageable pageable
    );
}

