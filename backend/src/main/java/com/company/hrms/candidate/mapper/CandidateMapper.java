package com.company.hrms.candidate.mapper;

import com.company.hrms.candidate.dto.*;
import com.company.hrms.candidate.entity.Candidate;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CandidateMapper {
    
    CandidateDTO toDto(Candidate candidate);
    
    Candidate toEntity(CandidateDTO dto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "recruiter", ignore = true)
    @Mapping(target = "workflow", ignore = true)
    @Mapping(target = "currentStage", ignore = true)
    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Candidate toEntity(CreateCandidateRequest request);
    
    @Mapping(source = "recruiter.id", target = "recruiterId")
    @Mapping(source = "recruiter.name", target = "recruiterName")
    @Mapping(source = "workflow.id", target = "workflowId")
    @Mapping(source = "workflow.name", target = "workflowName")
    @Mapping(source = "currentStage.id", target = "currentStageId")
    @Mapping(source = "currentStage.stageName", target = "currentStageName")
    CandidateDTO toDtoWithRelations(Candidate candidate);
}
