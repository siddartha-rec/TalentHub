package com.company.hrms.candidate.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MergeCandidatesRequest {
    private Long primaryCandidateId;
    private Long secondaryCandidateId;
    private List<String> fieldsToKeepFromPrimary; // e.g., ["email", "phone"]
}
