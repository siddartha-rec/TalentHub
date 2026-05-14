package com.company.hrms.workflow.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateStageRequest {
    private String stageCode;
    private String stageName;
    private Integer sequenceOrder;
    private String description;
    private Boolean isTerminal;
}
