package com.company.hrms.workflow.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTransitionRequest {
    private Long fromStageId;
    private Long toStageId;
    private Long requiredPermissionId;
    private Boolean mandatoryComment;
    private Boolean requiresApproval;
    private String description;
}
