package com.nt.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobPostDetailResponseDto {

    private Long id;

    private String postName;
    private Integer totalPosts;
    private String qualification;
    private String ageLimit;
}