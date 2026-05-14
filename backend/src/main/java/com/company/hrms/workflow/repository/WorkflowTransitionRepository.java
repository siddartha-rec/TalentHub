package com.company.hrms.workflow.repository;

import com.company.hrms.workflow.entity.WorkflowTransition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowTransitionRepository extends JpaRepository<WorkflowTransition, Long> {
    
    List<WorkflowTransition> findByFromStageId(Long fromStageId);
    
    Optional<WorkflowTransition> findByFromStageIdAndToStageId(Long fromStageId, Long toStageId);
    
    @Query("SELECT wt FROM WorkflowTransition wt LEFT JOIN FETCH wt.fromStage LEFT JOIN FETCH wt.toStage LEFT JOIN FETCH wt.requiredPermission WHERE wt.id = :id")
    Optional<WorkflowTransition> findByIdWithDetails(@Param("id") Long id);
    
    @Query("SELECT wt FROM WorkflowTransition wt LEFT JOIN FETCH wt.fromStage LEFT JOIN FETCH wt.toStage LEFT JOIN FETCH wt.requiredPermission WHERE wt.fromStage.workflow.id = :workflowId")
    List<WorkflowTransition> findByFromStageWorkflowId(@Param("workflowId") Long workflowId);
}
