package com.company.hrms.candidate.service;

import com.company.hrms.audit.entity.AuditLog;
import com.company.hrms.audit.repository.AuditLogRepository;
import com.company.hrms.candidate.dto.*;
import com.company.hrms.candidate.entity.Candidate;
import com.company.hrms.candidate.entity.CandidateComment;
import com.company.hrms.candidate.entity.CandidateDocument;
import com.company.hrms.candidate.entity.CandidateTransitionHistory;
import com.company.hrms.candidate.mapper.CandidateMapper;
import com.company.hrms.candidate.repository.CandidateRepository;
import com.company.hrms.common.exception.ResourceNotFoundException;
import com.company.hrms.user.entity.User;
import com.company.hrms.user.repository.UserRepository;
import com.company.hrms.workflow.entity.Workflow;
import com.company.hrms.workflow.entity.WorkflowStage;
import com.company.hrms.workflow.repository.WorkflowRepository;
import com.company.hrms.workflow.repository.WorkflowStageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final UserRepository userRepository;
    private final WorkflowRepository workflowRepository;
    private final WorkflowStageRepository stageRepository;
    private final AuditLogRepository auditLogRepository;
    private final CandidateMapper mapper;

    @Transactional(readOnly = true)
    public Page<CandidateDTO> getAllCandidates(Pageable pageable) {
        return candidateRepository.findAllActive(pageable)
                .map(mapper::toDtoWithRelations);
    }

    @Transactional(readOnly = true)
    public CandidateDTO getCandidateById(Long id) {
        Candidate candidate = candidateRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));
        return mapper.toDtoWithRelations(candidate);
    }

    @Transactional(readOnly = true)
    public List<DuplicateCandidateDTO> checkDuplicates(CreateCandidateRequest request) {
        List<DuplicateCandidateDTO> duplicates = new ArrayList<>();

        // Check by email
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            Optional<Candidate> existingByEmail = candidateRepository.findByEmailIgnoreCase(request.getEmail());
            existingByEmail.ifPresent(c -> duplicates.add(DuplicateCandidateDTO.builder()
                    .candidateId(c.getId())
                    .fullName(c.getFullName())
                    .email(c.getEmail())
                    .phone(c.getPhone())
                    .linkedinUrl(c.getLinkedinUrl())
                    .matchType("EXACT_EMAIL")
                    .matchScore(100.0)
                    .build()));
        }

        // Check by phone
        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            List<Candidate> existingByPhone = candidateRepository.findByPhone(request.getPhone());
            for (Candidate c : existingByPhone) {
                if (!duplicates.stream().anyMatch(d -> d.getCandidateId().equals(c.getId()))) {
                    duplicates.add(DuplicateCandidateDTO.builder()
                            .candidateId(c.getId())
                            .fullName(c.getFullName())
                            .email(c.getEmail())
                            .phone(c.getPhone())
                            .linkedinUrl(c.getLinkedinUrl())
                            .matchType("EXACT_PHONE")
                            .matchScore(100.0)
                            .build());
                }
            }
        }

        // Check by LinkedIn URL
        if (request.getLinkedinUrl() != null && !request.getLinkedinUrl().isEmpty()) {
            List<Candidate> existingByLinkedIn = candidateRepository.findByLinkedinUrlIgnoreCase(request.getLinkedinUrl());
            for (Candidate c : existingByLinkedIn) {
                if (!duplicates.stream().anyMatch(d -> d.getCandidateId().equals(c.getId()))) {
                    duplicates.add(DuplicateCandidateDTO.builder()
                            .candidateId(c.getId())
                            .fullName(c.getFullName())
                            .email(c.getEmail())
                            .phone(c.getPhone())
                            .linkedinUrl(c.getLinkedinUrl())
                            .matchType("EXACT_LINKEDIN")
                            .matchScore(100.0)
                            .build());
                }
            }
        }

        // Check by similar name (simple implementation - can be enhanced with fuzzy matching)
        if (request.getFirstName() != null && request.getLastName() != null) {
            String fullName = (request.getFirstName() + " " + request.getLastName()).toLowerCase();
            List<Candidate> allCandidates = candidateRepository.findAll();
            for (Candidate c : allCandidates) {
                if (!duplicates.stream().anyMatch(d -> d.getCandidateId().equals(c.getId()))) {
                    String existingName = c.getFullName().toLowerCase();
                    double similarity = calculateStringSimilarity(fullName, existingName);
                    if (similarity > 0.85) { // 85% similarity threshold
                        duplicates.add(DuplicateCandidateDTO.builder()
                                .candidateId(c.getId())
                                .fullName(c.getFullName())
                                .email(c.getEmail())
                                .phone(c.getPhone())
                                .linkedinUrl(c.getLinkedinUrl())
                                .matchType("SIMILAR_NAME")
                                .matchScore(similarity * 100)
                                .build());
                    }
                }
            }
        }

        return duplicates;
    }

    private double calculateStringSimilarity(String s1, String s2) {
        if (s1.equals(s2)) return 1.0;
        
        int longerLength = Math.max(s1.length(), s2.length());
        if (longerLength == 0) return 1.0;
        
        int distance = computeLevenshteinDistance(s1, s2);
        return (longerLength - distance) / (double) longerLength;
    }

    private int computeLevenshteinDistance(String s1, String s2) {
        int[] prev = new int[s2.length() + 1];
        int[] curr = new int[s2.length() + 1];

        for (int j = 0; j <= s2.length(); j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            curr[0] = i;
            for (int j = 1; j <= s2.length(); j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                curr[j] = Math.min(Math.min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
            }
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[s2.length()];
    }

    public CandidateDTO createCandidate(CreateCandidateRequest request, Long userId) {
        Candidate candidate = mapper.toEntity(request);
        
        // Set applied date if not provided
        if (candidate.getAppliedDate() == null) {
            candidate.setAppliedDate(LocalDate.now());
        }

        // Set recruiter
        if (request.getRecruiterId() != null) {
            User recruiter = userRepository.findById(request.getRecruiterId())
                    .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
            candidate.setRecruiter(recruiter);
        }

        // Set workflow and initial stage
        if (request.getWorkflowId() != null) {
            Workflow workflow = workflowRepository.findById(request.getWorkflowId())
                    .orElseThrow(() -> new ResourceNotFoundException("Workflow not found"));
            candidate.setWorkflow(workflow);
            
            // Set first stage as current stage
            if (!workflow.getStages().isEmpty()) {
                WorkflowStage firstStage = workflow.getStages().stream()
                        .min((s1, s2) -> s1.getSequenceOrder().compareTo(s2.getSequenceOrder()))
                        .orElseThrow(() -> new IllegalStateException("Workflow has no valid stages"));
                candidate.setCurrentStage(firstStage);
            }
        }

        Candidate saved = candidateRepository.save(candidate);
        
        // Log audit
        auditLog(AuditLog.builder()
                .actionType("CANDIDATE_CREATED")
                .module("CANDIDATE")
                .recordId(saved.getId())
                .newValue(saved.getFullName())
                .createdBy(User.builder().id(userId).build())
                .build());

        return mapper.toDtoWithRelations(saved);
    }

    public CandidateDTO updateCandidate(Long id, CreateCandidateRequest request, Long userId) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));

        // Update fields
        candidate.setFirstName(request.getFirstName());
        candidate.setLastName(request.getLastName());
        candidate.setEmail(request.getEmail());
        candidate.setPhone(request.getPhone());
        candidate.setAlternatePhone(request.getAlternatePhone());
        candidate.setDateOfBirth(request.getDateOfBirth());
        candidate.setGender(request.getGender());
        candidate.setCurrentCompany(request.getCurrentCompany());
        candidate.setDesignation(request.getDesignation());
        candidate.setExperienceYears(request.getExperienceYears());
        candidate.setCurrentCtc(request.getCurrentCtc());
        candidate.setExpectedCtc(request.getExpectedCtc());
        candidate.setNoticePeriodDays(request.getNoticePeriodDays());
        candidate.setSkills(request.getSkills());
        candidate.setLinkedinUrl(request.getLinkedinUrl());
        candidate.setGithubUrl(request.getGithubUrl());
        candidate.setPortfolioUrl(request.getPortfolioUrl());
        candidate.setSource(request.getSource());

        if (request.getRecruiterId() != null) {
            User recruiter = userRepository.findById(request.getRecruiterId())
                    .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
            candidate.setRecruiter(recruiter);
        }

        Candidate saved = candidateRepository.save(candidate);
        
        auditLog(AuditLog.builder()
                .actionType("CANDIDATE_UPDATED")
                .module("CANDIDATE")
                .recordId(saved.getId())
                .newValue(saved.getFullName())
                .createdBy(User.builder().id(userId).build())
                .build());

        return mapper.toDtoWithRelations(saved);
    }

    public void archiveCandidate(Long id, Long userId) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));
        
        candidate.setIsDeleted(true);
        candidateRepository.save(candidate);
        
        auditLog(AuditLog.builder()
                .actionType("CANDIDATE_ARCHIVED")
                .module("CANDIDATE")
                .recordId(candidate.getId())
                .newValue("Archived")
                .createdBy(User.builder().id(userId).build())
                .build());
    }

    public void restoreCandidate(Long id, Long userId) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));
        
        candidate.setIsDeleted(false);
        candidateRepository.save(candidate);
        
        auditLog(AuditLog.builder()
                .actionType("CANDIDATE_RESTORED")
                .module("CANDIDATE")
                .recordId(candidate.getId())
                .newValue("Restored")
                .createdBy(User.builder().id(userId).build())
                .build());
    }

    public CandidateDTO mergeCandidates(MergeCandidatesRequest request, Long userId) {
        Candidate primary = candidateRepository.findByIdWithRelations(request.getPrimaryCandidateId())
                .orElseThrow(() -> new ResourceNotFoundException("Primary candidate not found"));
        
        Candidate secondary = candidateRepository.findByIdWithRelations(request.getSecondaryCandidateId())
                .orElseThrow(() -> new ResourceNotFoundException("Secondary candidate not found"));

        // Merge logic: keep primary's core info, merge documents, comments, history
        // This is a simplified implementation
        
        // Archive secondary candidate
        secondary.setIsDeleted(true);
        secondary.setIsMerged(true);
        candidateRepository.save(secondary);

        auditLog(AuditLog.builder()
                .actionType("CANDIDATE_MERGED")
                .module("CANDIDATE")
                .recordId(primary.getId())
                .newValue("Merged with candidate " + secondary.getId())
                .createdBy(User.builder().id(userId).build())
                .build());

        return mapper.toDtoWithRelations(primary);
    }

    @Transactional(readOnly = true)
    public Page<CandidateDTO> searchCandidates(CandidateSearchRequest searchRequest, Pageable pageable) {
        return candidateRepository.searchCandidates(
                searchRequest.getKeyword(),
                searchRequest.getEmail(),
                searchRequest.getPhone(),
                searchRequest.getWorkflowId(),
                searchRequest.getCurrentStageId(),
                searchRequest.getRecruiterId(),
                searchRequest.getSource(),
                searchRequest.getMinExperience(),
                searchRequest.getMaxExperience(),
                pageable
        ).map(mapper::toDtoWithRelations);
    }

    private void auditLog(AuditLog log) {
        log.setCreatedAt(LocalDateTime.now());
        auditLogRepository.save(log);
    }
}
