package com.nt.repository;

import com.nt.entity.Job;
import com.nt.enums.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface JobRepository extends JpaRepository<Job, Long> {


    @Query("SELECT j FROM Job j JOIN j.jobTitle jt WHERE " +
            "(:keyword IS NULL OR LOWER(jt.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(j.organization) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:category IS NULL OR LOWER(j.category) LIKE LOWER(CONCAT('%', :category, '%'))) " +
            "AND (:status IS NULL OR j.status = :status)")
    Page<Job> advancedSearch(@Param("keyword") String keyword,
                             @Param("category") String category,
                             @Param("status") JobStatus status,
                             Pageable pageable);


}
