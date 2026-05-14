package com.company.hrms.workflow.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowDTO {
    private Long id;
    private String name;
    private Integer version;
    private String description;
    private Boolean isPublished;
    private Boolean isDefault;
    private List<WorkflowStageDTO> stages;
}
