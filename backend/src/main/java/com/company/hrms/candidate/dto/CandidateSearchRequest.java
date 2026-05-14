package com.company.hrms.candidate.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateSearchRequest {
    private String keyword;
    private String email;
    private String phone;
    private Long workflowId;
    private Long currentStageId;
    private Long recruiterId;
    private String source;
    private Double minExperience;
    private Double maxExperience;
    private String skills;
}
