package com.company.hrms.role.repository;

import com.company.hrms.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleCode(String roleCode);

    @Query("SELECT r FROM Role r WHERE r.isActive = true ORDER BY r.roleName")
    List<Role> findAllActiveRoles();

    boolean existsByRoleCode(String roleCode);
}
