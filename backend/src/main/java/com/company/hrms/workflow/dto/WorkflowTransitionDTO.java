package com.company.hrms.workflow.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowTransitionDTO {
    private Long id;
    private Long fromStageId;
    private String fromStageName;
    private Long toStageId;
    private String toStageName;
    private Long requiredPermissionId;
    private String requiredPermissionCode;
    private Boolean mandatoryComment;
    private Boolean requiresApproval;
    private String description;
}
