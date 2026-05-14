package com.company.hrms.workflow.repository;

import com.company.hrms.workflow.entity.Workflow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, Long> {
    
    Page<Workflow> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    List<Workflow> findByIsPublishedTrueOrderByVersionDesc();
    
    Optional<Workflow> findByNameAndVersion(String name, Integer version);
    
    List<Workflow> findByIsDefaultTrue();
    
    @Query("SELECT w FROM Workflow w LEFT JOIN FETCH w.stages WHERE w.id = :id")
    Optional<Workflow> findByIdWithStages(@Param("id") Long id);
    
    @Query("SELECT w FROM Workflow w LEFT JOIN FETCH w.stages WHERE w.isPublished = true")
    List<Workflow> findAllPublishedWithStages();
}
