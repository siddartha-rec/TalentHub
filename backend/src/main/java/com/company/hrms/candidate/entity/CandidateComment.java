package com.company.hrms.candidate.entity;

import com.company.hrms.common.entity.BaseEntity;
import com.company.hrms.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "candidate_comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateComment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "comment", nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(name = "is_internal", nullable = false)
    @Builder.Default
    private Boolean isInternal = true;
}
