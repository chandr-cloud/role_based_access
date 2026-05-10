package com.nt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "job_titles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobTitle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(unique = true, nullable = false)
    private String slug;

    private String shortInfo;

    @OneToOne(mappedBy = "jobTitle")
    @JsonIgnore
    private Job job;
}