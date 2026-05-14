package com.company.hrms.workflow.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransitionCandidateRequest {
    private Long toStageId;
    private String remarks;
}
