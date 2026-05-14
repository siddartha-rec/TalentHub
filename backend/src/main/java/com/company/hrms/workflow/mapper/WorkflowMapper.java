package com.company.hrms.workflow.mapper;

import com.company.hrms.workflow.dto.*;
import com.company.hrms.workflow.entity.Workflow;
import com.company.hrms.workflow.entity.WorkflowStage;
import com.company.hrms.workflow.entity.WorkflowTransition;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface WorkflowMapper {
    
    WorkflowDTO toDto(Workflow workflow);
    
    Workflow toEntity(WorkflowDTO dto);
    
    WorkflowStageDTO toDto(WorkflowStage stage);
    
    WorkflowStage toEntity(WorkflowStageDTO dto);
    
    WorkflowTransitionDTO toDto(WorkflowTransition transition);
    
    WorkflowTransition toEntity(WorkflowTransitionDTO dto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stages", ignore = true)
    Workflow toEntity(CreateWorkflowRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "workflow", ignore = true)
    WorkflowStage toEntity(CreateStageRequest request);
    
    List<WorkflowDTO> toDtoList(List<Workflow> workflows);
}
