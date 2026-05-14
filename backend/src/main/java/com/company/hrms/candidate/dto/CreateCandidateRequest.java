package com.company.hrms.candidate.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCandidateRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String alternatePhone;
    private LocalDate dateOfBirth;
    private String gender;
    private String currentCompany;
    private String designation;
    private Double experienceYears;
    private Double currentCtc;
    private Double expectedCtc;
    private Integer noticePeriodDays;
    private String skills;
    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;
    private Long recruiterId;
    private String source;
    private LocalDate appliedDate;
    private Long workflowId;
}
