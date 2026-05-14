package com.company.hrms.workflow.entity;

import com.company.hrms.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "workflow_stage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowStage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id", nullable = false)
    private Workflow workflow;

    @Column(name = "stage_code", nullable = false, length = 100)
    private String stageCode;

    @Column(name = "stage_name", nullable = false, length = 200)
    private String stageName;

    @Column(name = "sequence_order", nullable = false)
    private Integer sequenceOrder;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "is_terminal", nullable = false)
    @Builder.Default
    private Boolean isTerminal = false;
}
