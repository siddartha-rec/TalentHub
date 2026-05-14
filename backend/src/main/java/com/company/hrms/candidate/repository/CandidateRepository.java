package com.company.hrms.candidate.repository;

import com.company.hrms.candidate.entity.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    
    Page<Candidate> findByLastNameContainingIgnoreCase(String lastName, Pageable pageable);
    
    @Query("SELECT c FROM Candidate c LEFT JOIN FETCH c.recruiter LEFT JOIN FETCH c.workflow LEFT JOIN FETCH c.currentStage WHERE c.id = :id")
    Optional<Candidate> findByIdWithRelations(@Param("id") Long id);
    
    @Query("SELECT c FROM Candidate c WHERE c.email = :email OR c.phone = :phone")
    List<Candidate> findByEmailOrPhone(@Param("email") String email, @Param("phone") String phone);
    
    @Query("SELECT c FROM Candidate c WHERE LOWER(c.email) = LOWER(:email)")
    Optional<Candidate> findByEmailIgnoreCase(@Param("email") String email);
    
    @Query("SELECT c FROM Candidate c WHERE c.phone = :phone")
    List<Candidate> findByPhone(@Param("phone") String phone);
    
    @Query("SELECT c FROM Candidate c WHERE LOWER(c.linkedinUrl) = LOWER(:linkedinUrl)")
    List<Candidate> findByLinkedinUrlIgnoreCase(@Param("linkedinUrl") String linkedinUrl);
    
    @Query("SELECT c FROM Candidate c WHERE c.workflow.id = :workflowId AND c.currentStage.id = :stageId")
    Page<Candidate> findByWorkflowAndStage(@Param("workflowId") Long workflowId, 
                                           @Param("stageId") Long stageId, 
                                           Pageable pageable);
    
    @Query("SELECT c FROM Candidate c LEFT JOIN FETCH c.recruiter WHERE c.recruiter.id = :recruiterId")
    Page<Candidate> findByRecruiterId(@Param("recruiterId") Long recruiterId, Pageable pageable);
    
    @Query("SELECT c FROM Candidate c WHERE " +
           "(:keyword IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:email IS NULL OR LOWER(c.email) = LOWER(:email)) AND " +
           "(:phone IS NULL OR c.phone = :phone) AND " +
           "(:workflowId IS NULL OR c.workflow.id = :workflowId) AND " +
           "(:currentStageId IS NULL OR c.currentStage.id = :currentStageId) AND " +
           "(:recruiterId IS NULL OR c.recruiter.id = :recruiterId) AND " +
           "(:source IS NULL OR c.source = :source) AND " +
           "(:minExperience IS NULL OR c.experienceYears >= :minExperience) AND " +
           "(:maxExperience IS NULL OR c.experienceYears <= :maxExperience)")
    Page<Candidate> searchCandidates(@Param("keyword") String keyword,
                                     @Param("email") String email,
                                     @Param("phone") String phone,
                                     @Param("workflowId") Long workflowId,
                                     @Param("currentStageId") Long currentStageId,
                                     @Param("recruiterId") Long recruiterId,
                                     @Param("source") String source,
                                     @Param("minExperience") Double minExperience,
                                     @Param("maxExperience") Double maxExperience,
                                     Pageable pageable);
    
    @Query("SELECT c FROM Candidate c WHERE c.isDeleted = false ORDER BY c.createdAt DESC")
    Page<Candidate> findAllActive(Pageable pageable);
}
