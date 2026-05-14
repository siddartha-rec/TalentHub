package com.company.hrms.workflow.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowStageDTO {
    private Long id;
    private String stageCode;
    private String stageName;
    private Integer sequenceOrder;
    private String description;
    private Boolean isTerminal;
}
