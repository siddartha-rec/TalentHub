package com.company.hrms.candidate.entity;

import com.company.hrms.common.entity.BaseEntity;
import com.company.hrms.user.entity.User;
import com.company.hrms.workflow.entity.WorkflowStage;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidate_transition_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateTransitionHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_stage_id")
    private WorkflowStage fromStage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_stage_id", nullable = false)
    private WorkflowStage toStage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by", nullable = false)
    private User changedBy;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    @Column(name = "duration_in_previous_stage_hours")
    private Long durationInPreviousStageHours;

    @PrePersist
    public void calculateDuration() {
        if (fromStage != null && createdAt != null) {
            LocalDateTime previousChangeTime = getPreviousChangeTime();
            if (previousChangeTime != null) {
                this.durationInPreviousStageHours = Duration.between(previousChangeTime, LocalDateTime.now()).toHours();
            }
        }
    }

    private LocalDateTime getPreviousChangeTime() {
        // This would typically be set by a service before persisting
        return null;
    }
}
