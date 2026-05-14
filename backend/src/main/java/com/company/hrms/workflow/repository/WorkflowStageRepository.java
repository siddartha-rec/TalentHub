package com.company.hrms.workflow.repository;

import com.company.hrms.workflow.entity.WorkflowStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowStageRepository extends JpaRepository<WorkflowStage, Long> {
    
    List<WorkflowStage> findByWorkflowIdOrderBySequenceOrderAsc(Long workflowId);
    
    Optional<WorkflowStage> findByWorkflowIdAndStageCode(Long workflowId, String stageCode);
    
    @Query("SELECT ws FROM WorkflowStage ws LEFT JOIN FETCH ws.workflow WHERE ws.id = :id")
    Optional<WorkflowStage> findByIdWithWorkflow(@Param("id") Long id);
    
    List<WorkflowStage> findByWorkflowIdAndIsTerminal(Long workflowId, Boolean isTerminal);
}
