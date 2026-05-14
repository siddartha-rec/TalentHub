package com.company.hrms.workflow.service;

import com.company.hrms.common.exception.ResourceNotFoundException;
import com.company.hrms.permission.entity.Permission;
import com.company.hrms.permission.repository.PermissionRepository;
import com.company.hrms.workflow.dto.*;
import com.company.hrms.workflow.entity.Workflow;
import com.company.hrms.workflow.entity.WorkflowStage;
import com.company.hrms.workflow.entity.WorkflowTransition;
import com.company.hrms.workflow.mapper.WorkflowMapper;
import com.company.hrms.workflow.repository.WorkflowRepository;
import com.company.hrms.workflow.repository.WorkflowStageRepository;
import com.company.hrms.workflow.repository.WorkflowTransitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final WorkflowStageRepository stageRepository;
    private final WorkflowTransitionRepository transitionRepository;
    private final PermissionRepository permissionRepository;
    private final WorkflowMapper mapper;

    @Transactional(readOnly = true)
    public List<WorkflowDTO> getAllWorkflows() {
        return workflowRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<WorkflowDTO> getPublishedWorkflows() {
        return workflowRepository.findByIsPublishedTrueOrderByVersionDesc().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public WorkflowDTO getWorkflowById(Long id) {
        Workflow workflow = workflowRepository.findByIdWithStages(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow not found with id: " + id));
        return mapper.toDto(workflow);
    }

    public WorkflowDTO createWorkflow(CreateWorkflowRequest request) {
        // Check if workflow with same name exists
        Optional<Workflow> existing = workflowRepository.findByNameAndVersion(
                request.getName(), 1);
        
        int version = existing.map(w -> w.getVersion() + 1).orElse(1);

        Workflow workflow = mapper.toEntity(request);
        workflow.setVersion(version);
        workflow.setPublished(false);

        // Create stages
        if (request.getStages() != null) {
            for (CreateStageRequest stageRequest : request.getStages()) {
                WorkflowStage stage = mapper.toEntity(stageRequest);
                stage.setWorkflow(workflow);
                workflow.addStage(stage);
            }
        }

        Workflow saved = workflowRepository.save(workflow);
        return mapper.toDto(saved);
    }

    public WorkflowDTO publishWorkflow(Long id) {
        Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow not found with id: " + id));
        
        if (workflow.getIsDefault()) {
            // Unset all other default workflows
            List<Workflow> defaults = workflowRepository.findByIsDefaultTrue();
            for (Workflow w : defaults) {
                w.setIsDefault(false);
                workflowRepository.save(w);
            }
        }

        workflow.setIsPublished(true);
        Workflow saved = workflowRepository.save(workflow);
        return mapper.toDto(saved);
    }

    public WorkflowDTO setAsDefault(Long id) {
        Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow not found with id: " + id));

        // Unset all other default workflows
        List<Workflow> defaults = workflowRepository.findByIsDefaultTrue();
        for (Workflow w : defaults) {
            w.setIsDefault(false);
            workflowRepository.save(w);
        }

        workflow.setIsDefault(true);
        Workflow saved = workflowRepository.save(workflow);
        return mapper.toDto(saved);
    }

    public WorkflowStageDTO addStage(Long workflowId, CreateStageRequest request) {
        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow not found with id: " + workflowId));

        WorkflowStage stage = mapper.toEntity(request);
        stage.setWorkflow(workflow);
        workflow.addStage(stage);

        workflowRepository.save(workflow);
        return mapper.toDto(stage);
    }

    public WorkflowTransitionDTO createTransition(Long workflowId, CreateTransitionRequest request) {
        WorkflowStage fromStage = stageRepository.findById(request.getFromStageId())
                .orElseThrow(() -> new ResourceNotFoundException("From stage not found"));
        
        WorkflowStage toStage = stageRepository.findById(request.getToStageId())
                .orElseThrow(() -> new ResourceNotFoundException("To stage not found"));

        // Validate stages belong to the same workflow
        if (!fromStage.getWorkflow().getId().equals(workflowId) || 
            !toStage.getWorkflow().getId().equals(workflowId)) {
            throw new IllegalArgumentException("Both stages must belong to the same workflow");
        }

        WorkflowTransition transition = WorkflowTransition.builder()
                .fromStage(fromStage)
                .toStage(toStage)
                .mandatoryComment(request.getMandatoryComment() != null ? request.getMandatoryComment() : false)
                .requiresApproval(request.getRequiresApproval() != null ? request.getRequiresApproval() : false)
                .description(request.getDescription())
                .build();

        if (request.getRequiredPermissionId() != null) {
            Permission permission = permissionRepository.findById(request.getRequiredPermissionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
            transition.setRequiredPermission(permission);
        }

        WorkflowTransition saved = transitionRepository.save(transition);
        return mapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<WorkflowTransitionDTO> getTransitionsForWorkflow(Long workflowId) {
        return transitionRepository.findByFromStageWorkflowId(workflowId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<WorkflowTransitionDTO> getAvailableTransitions(Long fromStageId) {
        return transitionRepository.findByFromStageId(fromStageId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private WorkflowTransitionDTO convertToDto(WorkflowTransition transition) {
        return WorkflowTransitionDTO.builder()
                .id(transition.getId())
                .fromStageId(transition.getFromStage().getId())
                .fromStageName(transition.getFromStage().getStageName())
                .toStageId(transition.getToStage().getId())
                .toStageName(transition.getToStage().getStageName())
                .requiredPermissionId(transition.getRequiredPermission() != null ? 
                        transition.getRequiredPermission().getId() : null)
                .requiredPermissionCode(transition.getRequiredPermission() != null ? 
                        transition.getRequiredPermission().getPermissionCode() : null)
                .mandatoryComment(transition.getMandatoryComment())
                .requiresApproval(transition.getRequiresApproval())
                .description(transition.getDescription())
                .build();
    }

    public void deleteWorkflow(Long id) {
        Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow not found with id: " + id));
        
        // Cannot delete published workflows with active candidates
        if (workflow.getIsPublished()) {
            throw new IllegalStateException("Cannot delete published workflow. Please unpublish first.");
        }

        workflowRepository.delete(workflow);
    }
}
