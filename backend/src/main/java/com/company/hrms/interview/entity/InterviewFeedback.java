package com.company.hrms.interview.entity;

import com.company.hrms.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "interview_feedback")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewFeedback extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id", nullable = false)
    private Interview interview;

    @Column(name = "technical_rating")
    private Integer technicalRating; // 1-5 scale

    @Column(name = "communication_rating")
    private Integer communicationRating; // 1-5 scale

    @Column(name = "problem_solving_rating")
    private Integer problemSolvingRating; // 1-5 scale

    @Column(name = "recommendation", length = 50)
    private String recommendation; // STRONG_HIRE, HIRE, NO_HIRE, STRONG_NO_HIRE

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @Column(name = "strengths", columnDefinition = "TEXT")
    private String strengths;

    @Column(name = "weaknesses", columnDefinition = "TEXT")
    private String weaknesses;

    @Column(name = "additional_comments", columnDefinition = "TEXT")
    private String additionalComments;
}
