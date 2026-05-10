package com.nt.repository;

import com.nt.entity.AdmitCard;
import com.nt.enums.AdmitCardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface AdmitCardRepository extends JpaRepository<AdmitCard, Long> {

    @Query("SELECT a FROM AdmitCard a JOIN a.job j JOIN j.jobTitle jt WHERE " +
            "(:jobId IS NULL OR j.id = :jobId) " +
            "AND (:status IS NULL OR a.status = :status) " +
            "AND (:from IS NULL OR a.examDate >= :from) " +
            "AND (:to IS NULL OR a.examDate <= :to)")
    Page<AdmitCard> search(
            @Param("jobId") Long jobId,
            @Param("status") AdmitCardStatus status,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            Pageable pageable
    );
}

