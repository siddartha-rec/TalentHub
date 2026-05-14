package com.company.hrms.workflow.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateWorkflowRequest {
    private String name;
    private String description;
    private Boolean isDefault;
    private List<CreateStageRequest> stages;
}
