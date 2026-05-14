package com.company.hrms.workflow.entity;

import com.company.hrms.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workflow_master")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workflow extends BaseEntity {

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "is_published", nullable = false)
    @Builder.Default
    private Boolean isPublished = false;

    @Column(name = "is_default", nullable = false)
    @Builder.Default
    private Boolean isDefault = false;

    @OneToMany(mappedBy = "workflow", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("sequenceOrder ASC")
    @Builder.Default
    private List<WorkflowStage> stages = new ArrayList<>();

    public void addStage(WorkflowStage stage) {
        this.stages.add(stage);
        stage.setWorkflow(this);
    }

    public void removeStage(WorkflowStage stage) {
        this.stages.remove(stage);
        stage.setWorkflow(null);
    }
}
