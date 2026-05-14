package com.company.hrms.candidate.entity;

import com.company.hrms.common.entity.BaseEntity;
import com.company.hrms.user.entity.User;
import com.company.hrms.workflow.entity.Workflow;
import com.company.hrms.workflow.entity.WorkflowStage;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "candidates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate extends BaseEntity {

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", length = 200)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "alternate_phone", length = 20)
    private String alternatePhone;

    @Column(name = "dob")
    private LocalDate dateOfBirth;

    @Column(name = "gender", length = 20)
    private String gender;

    @Column(name = "current_company", length = 200)
    private String currentCompany;

    @Column(name = "designation", length = 200)
    private String designation;

    @Column(name = "experience_years")
    private Double experienceYears;

    @Column(name = "current_ctc")
    private Double currentCtc;

    @Column(name = "expected_ctc")
    private Double expectedCtc;

    @Column(name = "notice_period_days")
    private Integer noticePeriodDays;

    @Column(name = "skills", columnDefinition = "TEXT")
    private String skills;

    @Column(name = "linkedin_url", length = 500)
    private String linkedinUrl;

    @Column(name = "github_url", length = 500)
    private String githubUrl;

    @Column(name = "portfolio_url", length = 500)
    private String portfolioUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id")
    private User recruiter;

    @Column(name = "source", length = 100)
    private String source;

    @Column(name = "applied_date")
    private LocalDate appliedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id")
    private Workflow workflow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_stage_id")
    private WorkflowStage currentStage;

    @Column(name = "resume_url", length = 500)
    private String resumeUrl;

    @Column(name = "resume_version", nullable = false)
    @Builder.Default
    private Integer resumeVersion = 1;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<CandidateDocument> documents = new HashSet<>();

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<CandidateComment> comments = new HashSet<>();

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
