package com.company.hrms.workflow.entity;

import com.company.hrms.common.entity.BaseEntity;
import com.company.hrms.permission.entity.Permission;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "workflow_transition")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowTransition extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_stage_id", nullable = false)
    private WorkflowStage fromStage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_stage_id", nullable = false)
    private WorkflowStage toStage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "required_permission_id")
    private Permission requiredPermission;

    @Column(name = "mandatory_comment", nullable = false)
    @Builder.Default
    private Boolean mandatoryComment = false;

    @Column(name = "requires_approval", nullable = false)
    @Builder.Default
    private Boolean requiresApproval = false;

    @Column(name = "description", length = 500)
    private String description;
}
