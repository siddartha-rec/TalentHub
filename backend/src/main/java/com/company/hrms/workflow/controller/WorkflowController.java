package com.company.hrms.workflow.controller;

import com.company.hrms.workflow.dto.*;
import com.company.hrms.workflow.service.WorkflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/workflows")
@RequiredArgsConstructor
public class WorkflowController {

    private final WorkflowService workflowService;

    @GetMapping
    public ResponseEntity<List<WorkflowDTO>> getAllWorkflows() {
        return ResponseEntity.ok(workflowService.getAllWorkflows());
    }

    @GetMapping("/published")
    public ResponseEntity<List<WorkflowDTO>> getPublishedWorkflows() {
        return ResponseEntity.ok(workflowService.getPublishedWorkflows());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkflowDTO> getWorkflowById(@PathVariable Long id) {
        return ResponseEntity.ok(workflowService.getWorkflowById(id));
    }

    @PostMapping
    public ResponseEntity<WorkflowDTO> createWorkflow(@RequestBody CreateWorkflowRequest request) {
        WorkflowDTO created = workflowService.createWorkflow(request);
        return ResponseEntity.created(URI.create("/api/workflows/" + created.getId()))
                .body(created);
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<WorkflowDTO> publishWorkflow(@PathVariable Long id) {
        return ResponseEntity.ok(workflowService.publishWorkflow(id));
    }

    @PostMapping("/{id}/set-default")
    public ResponseEntity<WorkflowDTO> setAsDefault(@PathVariable Long id) {
        return ResponseEntity.ok(workflowService.setAsDefault(id));
    }

    @PostMapping("/{id}/stages")
    public ResponseEntity<WorkflowStageDTO> addStage(
            @PathVariable Long id,
            @RequestBody CreateStageRequest request) {
        return ResponseEntity.ok(workflowService.addStage(id, request));
    }

    @PostMapping("/{id}/transitions")
    public ResponseEntity<WorkflowTransitionDTO> createTransition(
            @PathVariable Long id,
            @RequestBody CreateTransitionRequest request) {
        return ResponseEntity.ok(workflowService.createTransition(id, request));
    }

    @GetMapping("/{id}/transitions")
    public ResponseEntity<List<WorkflowTransitionDTO>> getTransitionsForWorkflow(
            @PathVariable Long id) {
        return ResponseEntity.ok(workflowService.getTransitionsForWorkflow(id));
    }

    @GetMapping("/stages/{stageId}/transitions")
    public ResponseEntity<List<WorkflowTransitionDTO>> getAvailableTransitions(
            @PathVariable Long stageId) {
        return ResponseEntity.ok(workflowService.getAvailableTransitions(stageId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkflow(@PathVariable Long id) {
        workflowService.deleteWorkflow(id);
        return ResponseEntity.noContent().build();
    }
}
