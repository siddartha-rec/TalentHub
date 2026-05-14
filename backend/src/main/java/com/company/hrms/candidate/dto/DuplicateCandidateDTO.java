package com.company.hrms.candidate.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DuplicateCandidateDTO {
    private Long candidateId;
    private String fullName;
    private String email;
    private String phone;
    private String linkedinUrl;
    private String matchType; // EXACT_EMAIL, EXACT_PHONE, EXACT_LINKEDIN, SIMILAR_NAME
    private Double matchScore;
}
